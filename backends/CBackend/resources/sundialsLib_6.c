#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <stdarg.h>
#include <cvode/cvode.h>
/* --------------------- new -------------------------- */
#include <sunmatrix/sunmatrix_band.h>  /* access to band SUNMatrix             */
#include <sunlinsol/sunlinsol_band.h>  /* access to band SUNLinearSolver       */
#include <sunmatrix/sunmatrix_dense.h>    /* access to dense SUNMatrix                */
#include <sunlinsol/sunlinsol_dense.h>    /* access to dense SUNLinearSolver          */
#include <sunlinsol/sunlinsol_spgmr.h>   /* access to SPGMR SUNLinearSolver   */
#include <sunlinsol/sunlinsol_spbcgs.h>  /* access to SPBCGS SUNLinearSolver  */
#include <sunlinsol/sunlinsol_sptfqmr.h> /* access to SPTFQMR SUNLinearSolver */
#include <sunlinsol/sunlinsol_pcg.h> /* access to SPTFQMR SUNLinearSolver */
#include <sunnonlinsol/sunnonlinsol_newton.h>     /* access to the newton SUNNonlinearSolver      */
#include <sunnonlinsol/sunnonlinsol_fixedpoint.h> /* access to the fixed point SUNNonlinearSolver */

#if defined(SUN_LAPACK_DENSE) || defined(SUN_LAPACK_BAND)
#include <sunlinsol/sunlinsol_lapackdense.h> /* access to dense LAPACK solver */
#include <sunlinsol/sunlinsol_lapackband.h> /* access to dense LAPACK solver */
#endif

#include <cvode/cvode_bandpre.h>
#include <cvode/cvode_spils.h>
#include <cvode/cvode_diag.h>             /* access to CVDIAG linear solver           */
#include <cvode/cvode_direct.h>           /* access to CVDls interface                */
/* --------------------- end new -------------------------- */
#include <nvector/nvector_serial.h>
#include <sundials/sundials_dense.h>
#include <sundials/sundials_types.h>
#include <time.h>
#include <string.h>
#include "structures.h"
#include "sundialsLib.h"
#include "matrixLib.h"
#include "memoryLib.h"
#include "assert.h"
#include "math.h"

//static variable
static INTEGRATION_OPTS global_opts;

/* wraps the matrix function that defines the ODE in a sundial-compatible way */
void __wrap_matrix_function(double*(*fn)(double t, double *y, int y_dim1, int y_dim2), realtype t, N_Vector y, N_Vector ydot, void *user_data){
	//here you have to evaluate the function fn with the parameters in input to a sundials compatible function
	
	//transform the realtype t to a double
	double t_as_double = (double) t;

	//transform the N_vector y in a matrix that can be handled 
	//by the function fn (in this case, the N_Vector is implemented as a NVECTOR_SERIAL	
	realtype* y_as_realtype_matrix = NV_DATA_S(y);
	double* y_as_matrix= (double*) y_as_realtype_matrix;
	
	int dimension = NV_LENGTH_S(y);

	//calculate the output of fn
	//populate the N_Vector ydot with result
	
	double *s = fn(t_as_double, y_as_matrix, 1, dimension);
	
	int counter = 0;
	for(counter =0 ; counter<dimension; counter++){
		Ith(ydot, counter) = (realtype) s[counter];
	}
}

/* wraps the scalar function that defines the ODE in a sundial-compatible way */
void __wrap_scalar_function(double(*fn)(double t, double *y, int y_dim1, int y_dim2), realtype t, N_Vector y, N_Vector ydot, void *user_data){
	//here you have to evaluate the function fn with the parameters in input to a sundials compatible function
	
	//transform the realtype t to a double
	double t_as_double = (double) t;

	//transform the N_vector y in a matrix that can be handled 
	//by the function fn (in this case, the N_Vector is implemented as a NVECTOR_SERIAL	
	realtype* y_as_realtype_matrix = NV_DATA_S(y);
	double* y_as_matrix= (double*) y_as_realtype_matrix;
	
	int dimension = NV_LENGTH_S(y);

	//calculate the output of fn
	//populate the N_Vector ydot with result
	
	double s = fn(t_as_double, y_as_matrix, 1, dimension);
	
	Ith(ydot, 0) = s;
}

/*
* (SUNDIALS) Check function return value...
*   opt == 0 means SUNDIALS function allocates memory so check if
*            returned NULL pointer
*   opt == 1 means SUNDIALS function returns a flag so check if
*            flag >= 0
*   opt == 2 means function allocates memory so check if returned
*            NULL pointer
*/

int check_flag(void *flagvalue, char *funcname, int opt)
{
    int *errflag;

    /* Check if SUNDIALS function returns NULL pointer; no memory allocated */
    if (opt == ZERO && flagvalue == NULL)
    {
        fprintf(stderr,
            "\nSUNDIALS_ERROR: %s() failed - returned NULL pointer\n\n",
            funcname
        );
        return ONE;
    }

    /* Check if flag < ZERO */
    else if (opt == ONE)
    {
        errflag = (int *)flagvalue;
        if (*errflag < ZERO)
        {
            fprintf(stderr,
                "\nSUNDIALS_ERROR: %s() failed with flag = %d\n\n",
                funcname, *errflag
            );
            return ONE;
        }
    }

    /* Check if function returned NULL pointer -- no memory allocated */
    else if (opt == TWO && flagvalue == NULL)
    {
        fprintf(stderr,
            "\nMEMORY_ERROR: %s() failed - returned NULL pointer\n\n",
            funcname
        );
        return ONE;
    }

    return ZERO;
}


/*
* just to make the method sun_init work with y as a int* instead of double*
*/
SUN_INIT *sun_init_i(SUN_INIT *s, int (*f)(realtype t, N_Vector y, N_Vector ydot, void *user_data), int *y, int y_len, double tinit_as_double, double tend_as_double, INTEGRATION_OPTS* opts, void *userdata){
	
	//convert the int* pointer to a double* one
	//(casting it's not enough)
	double *y_as_double = (double*)malloc(sizeof(double)*y_len);
	for(int i=0; i<y_len; i++){
		y_as_double[i]=(double)y[i];
	}
	
	return sun_init_d(s, f, y_as_double, y_len, tinit_as_double, tend_as_double, opts, userdata);	
}


/*
* SUNDIALS initializer. Operates on a 
 struct (defined somewhere near
* the top of this file). The goal is to consolidate all the common code within
* any of our supported Interfaces, which will improve maintainability and make
* it easier to understand what is going on.
*/

SUN_INIT *sun_init_d(SUN_INIT *s, int (*f)(realtype t, N_Vector y, N_Vector ydot, void *user_data), double *y, int y_len, double tinit_as_double, double tend_as_double, INTEGRATION_OPTS* opts, void *userdata)
{
	realtype tinit = (realtype) tinit_as_double;
	realtype tend = (realtype) tend_as_double; 

	if(opts == NULL)
		opts = setIntegrationOptions(DEFAULT_ABS_TOL, DEFAULT_REL_TOL, DEFAULT_MAX_STEP, DEFAULT_INITIAL_STEP, DEFAULT_SET_MAX_ORDER);		


        char init=(s==NULL) || (NV_LENGTH_S(s->y) != y_len);
	/* if null pointer allocate memory */
	if(s==NULL)
	   s = (SUN_INIT *) qspcc_malloc(sizeof(SUN_INIT));
	else{
	   /* free previously allocated memory */
	   N_VDestroy_Serial(s->y);
	   if(init){
		SUNContext_Free(&s->sunctx);
		CVodeFree(&s->cvode_mem);
           }
    	}

        if(init){
           /* Call CVodeCreate to create the solver memory and specify the
            * Backward Differentiation Formula and the use of a Newton iteration */
	   SUNContext_Create(NULL, &s->sunctx);
           s->cvode_mem = CVodeCreate(CV_BDF, s->sunctx);
           if (check_flag((void *)s->cvode_mem, "CVodeCreate", ZERO)) { return s; }
        }
	
	s->rtol = (realtype) opts->rel_tol;
	s->atol = (realtype) opts->abs_tol;
	realtype max_step = (realtype) opts->max_step;
	realtype initial_step = (realtype) opts->initial_step;
	s->max_order = opts->max_order;
	/* -------------- new ------------- */
	SUNMatrix A;
	SUNLinearSolver LS;
	SUNNonlinearSolver NLS;
	/* -------------- end new ------------- */
    int k;
    /* chiedere poi perché é inizializzato in questo modo */
    s->tout = tinit;
    s->t = tinit;

    /* Create serial vector of length N_SPECIES for initial conditions. */
    s->y = N_VNew_Serial(y_len, s->sunctx);

    if (check_flag((void *)s->y, "N_VNew_Serial", ZERO)) { return s; }

    /* initial conditions for y */
    /* ---- NEW -----------*/
    memcpy(NV_DATA_S(s->y), y, y_len*sizeof(double));
    /*for (k = ZERO; k < y_len; k++)
    {
        Ith(s->y, k) = y[k];
    }*/
    /* ---- end NEW -----------*/

    /* Call CVodeInit to initialize the integrator memory and specify the
    * user's right hand side function in y'=f(t,y), the inital time T0, and
    * the initial dependent variable vector y. */
    if(init)
        s->flag = CVodeInit(s->cvode_mem, f, tinit, s->y);
    else
	/* re-initializing saves some memory operations, reuse same f as before */
        s->flag = CVodeReInit(s->cvode_mem, tinit, s->y);
    if (check_flag(&s->flag, "CVodeInit", ONE)) { return s; }

    /* Call CVodeSVtolerances to specify the scalar relative tolerance
    * and vector absolute tolerances */
    s->flag = CVodeSStolerances(s->cvode_mem, s->rtol, s->atol);
    if (check_flag(&s->flag, "CVodeSVtolerances", ONE)) { return s; }

#if defined(SUN_DENSE)
    /* Create dense SUNMatrix for use in linear solves */
    A = SUNDenseMatrix(y_len, y_len, s->sunctx);
    s->matrix = A; /* save for deallocation */
    if(check_flag((void *)A, "SUNDenseMatrix", 0)) return s;

    /* Create dense SUNLinearSolver object for use by CVode */
    LS = SUNLinSol_Dense(s->y, A, s->sunctx);
    s->linearsolver = LS; /* save for deallocation */
    if(check_flag((void *)LS, "SUNLinSol_Dense", 0)) return s;

    /* Call CVDiag/ CVDlsSetLinearSolver / CVSpilsSetLinearSolver to attach the matrix and linear solver to CVode */
    s->flag = CVodeSetLinearSolver(s->cvode_mem, LS, A);
    if(check_flag(&s->flag, "CVodeSetLinearSolver", 1)) return s;
#elif defined(SUN_LAPACK_DENSE)
   /* Create dense SUNMatrix for use in linear solves */
    A = SUNDenseMatrix(y_len, y_len, s->sunctx);
    s->matrix = A; /* save for deallocation */
    if(check_flag((void *)A, "SUNDenseMatrix", 0)) return s;

   /* Create dense SUNLinearSolver object for use by CVode */
    LS = SUNLinSol_LapackDense(s->y, A, s->sunctx);
    s->linearsolver = LS; /* save for deallocation */
    if(check_flag((void *)LS, "SUNLinSol_LapackDense", 0)) return s;

    /* Call CVDiag/ CVDlsSetLinearSolver / CVSpilsSetLinearSolver to attach the matrix and linear solver to CVode */
    s->flag = CVodeSetLinearSolver(s->cvode_mem, LS, A);
    if(check_flag(&s->flag, "CVodeSetLinearSolver", 1)) return s;
#elif defined(SUN_SPGMR)
    /* Create dense SUNLinearSolver object for use by CVode */
#ifdef SUN_PRECONDITION_BAND
    LS = SUNLinSol_SPGMR(s->y, PREC_LEFT, SUN_SPGMR, s->sunctx);
#else
    LS = SUNLinSol_SPGMR(s->y, PREC_NONE, SUN_SPGMR, s->sunctx);
#endif
    s->linearsolver = LS; /* save for deallocation */
    if(check_flag((void *)LS, "SUNLinSol_SPGMR", 0)) return s;

    /* Call CVDiag/ CVDlsSetLinearSolver / CVSpilsSetLinearSolver to attach the matrix and linear solver to CVode */
    s->flag = CVodeSetLinearSolver(s->cvode_mem, LS, NULL);
    if(check_flag(&s->flag, "CVodeSetLinearSolver", 1)) return s;
#elif defined(SUN_LAPACK_BAND)
    /* Create dense SUNMatrix for use in linear solves */
    A = SUNBandMatrix(y_len, SUN_LAPACK_BAND, SUN_LAPACK_BAND, s->sunctx);
    s->matrix = A; /* save for deallocation */
    if(check_flag((void *)A, "SUNBandMatrix", 0)) return s;

    /* Create dense SUNLinearSolver object for use by CVode */
    LS = SUNLinSol_LapackBand(s->y, A, s->sunctx);
    s->linearsolver = LS; /* save for deallocation */
    if(check_flag((void *)LS, "SUNBandLinearSolver", 0)) return s;

    /* Call CVDiag/ CVDlsSetLinearSolver / CVSpilsSetLinearSolver to attach the matrix and linear solver to CVode */
    s->flag = CVodeSetLinearSolver(s->cvode_mem, LS, A);
    if(check_flag(&s->flag, "CVodeSetLinearSolver", 1)) return s;
#elif defined(SUN_BAND)
    /* Create dense SUNMatrix for use in linear solves */
    A = SUNBandMatrix(y_len, SUN_BAND, SUN_BAND, s->sunctx);
    s->matrix = A; /* save for deallocation */
    if(check_flag((void *)A, "SUNBandMatrix", 0)) return s;

    /* Create dense SUNLinearSolver object for use by CVode */
    LS = SUNLinSol_Band(s->y, A, s->sunctx);
    s->linearsolver = LS; /* save for deallocation */
    if(check_flag((void *)LS, "SUNBandLinearSolver", 0)) return s;

    /* Call CVDiag/ CVDlsSetLinearSolver / CVSpilsSetLinearSolver to attach the matrix and linear solver to CVode */
    s->flag = CVodeSetLinearSolver(s->cvode_mem, LS, A);
    if(check_flag(&s->flag, "CVodeSetLinearSolver", 1)) return s;
#else
    /* DEFAULT TO DENSE LINEAR SOLVER */
    /* Create dense SUNMatrix for use in linear solves */
    A = SUNDenseMatrix(y_len, y_len, s->sunctx);
    s->matrix = A; /* save for deallocation */
    if(check_flag((void *)A, "SUNDenseMatrix", 0)) return s;

    /* Create dense SUNLinearSolver object for use by CVode */
    LS = SUNLinSol_Dense(s->y, A, s->sunctx);
    s->linearsolver = LS; /* save for deallocation */
    if(check_flag((void *)LS, "SUNLinSol_Dense", 0)) return s;

    /* Call CVDiag/ CVDlsSetLinearSolver / CVSpilsSetLinearSolver to attach the matrix and linear solver to CVode */
    s->flag = CVodeSetLinearSolver(s->cvode_mem, LS, A);
    if(check_flag(&s->flag, "CVodeSetLinearSolver", 1)) return s;    
#endif
    
#ifdef SUN_PRECONDITION_BAND
    s->flag = CVBandPrecInit(s->cvode_mem, y_len, SUN_PRECONDITION_BAND, SUN_PRECONDITION_BAND);
    if(check_flag((void *)LS, "CVBandPrecInit", 0)) return s;
#endif

#if defined(SUN_NONLIN_FIXEDPOINT)
    /* create Newton nonlinear solver object */
    NLS = SUNNonlinSol_FixedPoint(s->y, SUN_NONLIN_FIXEDPOINT, s->sunctx);
    if(check_flag((void *)&NLS, "SUNNonlinSol_FixedPoint", 0)) return s;
    /* attach nonlinear solver object to CVode */
    s->flag = CVodeSetNonlinearSolver(s->cvode_mem, NLS);
    if(check_flag(&s->flag, "CVodeSetNonlinearSolver", 1)) return s;
#else
    /* create Newton nonlinear solver object */
    NLS = SUNNonlinSol_Newton(s->y, s->sunctx);
    if(check_flag((void *)&NLS, "SUNNonlinSol_Newton", 0)) return s;
    /* attach nonlinear solver object to CVode */
    s->flag = CVodeSetNonlinearSolver(s->cvode_mem, NLS);
    if(check_flag(&s->flag, "CVodeSetNonlinearSolver", 1)) return s;
#endif
    
    /* enforce minimum step size, otherwise sundials can halt */
    s->flag = CVodeSetMinStep(s->cvode_mem, DEFAULT_MIN_STEP);
    if (check_flag(&s->flag, "CVodeSetMinStep", ONE)) { return s; }

    /* vary the maximum steps */
    /* enforce maximum step*/
    if(max_step>0){
    	s->flag = CVodeSetMaxStep(s->cvode_mem, max_step);
    	if (check_flag(&s->flag, "CVodeSetMaxStep", ONE)) { return s; }
    }
    /* set the user data, that should contain the global variables */
    s->flag = CVodeSetUserData(s->cvode_mem, userdata);
     if (check_flag(&s->flag, "CVodeSetUSerData", ONE)) { return s; }
     
    /* maximum step number, just the default value because the matlab option doesn't exist*/ 
  	s->flag = CVodeSetMaxNumSteps(s->cvode_mem, DEFAULT_NUMBER_STEPS);
     if (check_flag(&s->flag, "CVodeSetMaxNumSteps", ONE)) { return s; }

     /* Maximum no.  of error test failures */
	s->flag = CVodeSetMaxErrTestFails(s->cvode_mem, DEFAULT_ERROR_TEST_FAILURES);
     if (check_flag(&s->flag, "CVodeSetMaxErrTestFails", ONE)) { return s; }
 
    /* Maximum no.  of nonlinear iterations */
	s->flag = CVodeSetMaxNonlinIters(s->cvode_mem, DEFAULT_NON_LINEAR_ITERS);
     if (check_flag(&s->flag, "CVodeSetMaxNonlinIters", ONE)) { return s; }
    
    /* Maximum no.  of nonlinear iterations */
	s->flag = CVodeSetMaxConvFails(s->cvode_mem, DEFAULT_CONVERGENCE_FAILURES);
     if (check_flag(&s->flag, "CVodeSetMaxConvFails", ONE)) { return s; }

    if(s->max_order>0){
 	 	s->flag = CVodeSetMaxOrd(s->cvode_mem, s->max_order);
 	    if (check_flag(&s->flag, "CVodeSetMaxOrder", ONE)) { return s; }
    }else
	/* default max order */
	s->max_order = DEFAULT_SET_MAX_ORDER;
    
    if(initial_step>0){
  		s->flag = CVodeSetInitStep(s->cvode_mem, initial_step);
   		if (check_flag(&s->flag, "CVodeSetInitStep", ONE)) { return s; }
    }
    return s;	
}


/*--------------------------------------------------------------------------*/

/*
* SUNDIALS evaluator.
* 
*/

static inline realtype getStep(INTEGRATION_TIMES *timesteps, int k){
  if(timesteps->type == 'i'){
	return (realtype) ((int *)  timesteps->times)[k];
   }else{
	return (realtype) ((double *)  timesteps->times)[k];
   }
}

// memory-efficient version with virtual timesteps array defined by triple start,step,end
void sd_sun_eval(SUN_INIT *s, int dest_rows, int dest_cols, INTEGRATION_TIMES *timesteps, int transpose, int n_out_matrices, ...){
    va_list vl;
    va_start(vl, n_out_matrices);
    double *outmatrices[n_out_matrices];
    int i;
    for(i = 0 ; i < n_out_matrices ; ++i )
	outmatrices[i] = va_arg(vl, double *);
    
    assert(n_out_matrices <= 2 && "Only the first two output matrices will be used");
    int k, n, retcode, retry;
    int y_len = NV_LENGTH_S(s->y);
    s->start = clock();

    realtype start = timesteps->tin; 
    realtype step = timesteps->step;
    realtype end = timesteps->tend;
    int times_length = timesteps->len;

    char useArray = timesteps->times == NULL;
    if(useArray)
      // first output time
      s->tout = start + step;
    else
      s->tout = getStep(timesteps, 1);

    realtype *dest,*tdest;
    int transpose_shift = ZERO;
    /* ADAPTIVE CHANGE OF ORDER */
    int mode=ONE;
    int max_steps;
    double hu;

    if( n_out_matrices == 1 ){
    	tdest = (realtype *) outmatrices[0];
	dest = tdest;
        transpose_shift = ONE;
    }else{
        tdest = (realtype *) outmatrices[0];
        dest = (realtype *) outmatrices[1];
    }

    for (k = ONE; k < times_length; k++)
    {
        /*
        * As of version 2.3.0 (from documentation):
        *
        * Passing a value of ZERO for the maximum step size, the minimum step
        * size, or for maxsteps results in the solver using the corresponding
        * default value (infinity, 0, 500, respectively).
        */

        /* record output only at desired points */
        /* some interfaces like MATLAB prefer row-major output */    
        if (transpose)
        {
            tdest[(k) + (dest_rows *  (ZERO))] = s->t;

            for (n = ZERO; n < y_len; n++)                
            {
                dest[(k ) + (dest_rows * (transpose_shift + n))] = Ith(s->y, n);
            }
        }
            
        /* otherwise we can write column-major sequentially */
        else
        {    
            *tdest++ = s->t;
                
           for (n = ZERO; n < y_len; n++)
            {
                *dest++ = Ith(s->y, n);
            }
        }    

	retcode = CV_CONV_FAILURE;
	retry = 0;
	max_steps = DEFAULT_NUMBER_STEPS;
	while(retcode != CV_SUCCESS && retry++ < MAX_RETRY){
		if(mode == ZERO){
		   /*Test for sufficient increase in step size*/
		   retcode = CVodeGetLastStep(s->cvode_mem, &hu); 
		   if (retcode == CV_SUCCESS){
			   if (hu > RESET_TO_DEFAULT_ORDER_AFTER_H_ABOVE) {
				#ifdef DEBUG
				print_stats(s->cvode_mem);
				#endif
				/* Reset maximum order */
				CVodeSetMaxOrd(s->cvode_mem, s->max_order);
				/* Call CVodeSVtolerances to specify the scalar relative tolerance
				* and vector absolute tolerances */
				retcode = CVodeSStolerances(s->cvode_mem, s->rtol, s->atol);
				/* maximum step number, just the default value because the matlab option doesn't exist*/ 
				retcode = CVodeSetMaxNumSteps(s->cvode_mem, DEFAULT_NUMBER_STEPS);
				/* Reinitialize solver with tight tolerances */
				retcode = CVodeReInit(s->cvode_mem, s->t, s->y);
				if(retcode != CV_SUCCESS)
					fprintf(stderr, "Failed to reinitialize to time %.4f after re-setting integration order to %d, error code %d\n", s->t, s->max_order, retcode);
				mode = ONE;
			   } 
		   }else
			fprintf(stderr, "Failed to get last step size at current time %.4f, error code %d\n", s->t, retcode);
		}

		/* continue solving */
		retcode = CVode(s->cvode_mem, s->tout, s->y, &s->t, CV_NORMAL);
		if (retcode == CV_SUCCESS)
		{
			if(k<times_length-1)
				if(useArray)
					s->tout += step;
				else
					s->tout = getStep(timesteps, k+1);
		}else if(mode == ONE && (retcode == CV_TOO_MUCH_ACC))
			fprintf(stderr, "Too much accuracy requested at time %.4f\n", s->t);
		else if(mode == ONE && retcode == CV_TOO_MUCH_WORK){
			#ifdef DEBUG
			print_stats(s->cvode_mem);
			#endif
			max_steps *= 2;
			fprintf(stderr, "Too much work, raising maximum number of steps to %d at time %.4f\n", max_steps, s->t);
			/* maximum step number, just the default value because the matlab option doesn't exist*/ 
			retcode = CVodeSetMaxNumSteps(s->cvode_mem, max_steps);
			/* Reinitialize solver with loose tolerances */
			retcode = CVodeReInit(s->cvode_mem, s->t, s->y);
			/* retry */
			retcode = CVode(s->cvode_mem, s->tout, s->y, &s->t, CV_NORMAL);
			if(retcode != CV_SUCCESS)
				fprintf(stderr, "Failed to reach time %.4f\n", s->tout);
			else{
				if(k<times_length-1)
					if(useArray)
						s->tout += step;
					else
						s->tout = getStep(timesteps, k+1);
			}			
		}else if(mode == ONE && (retcode == CV_ERR_FAILURE || retcode == CV_CONV_FAILURE)){
			/* adapt step fallback to order 1, method backward euler */
			#ifdef DEBUG
			print_stats(s->cvode_mem);
			#endif
			/* Enforce backward Euler */
			retcode = CVodeSetMaxOrd(s->cvode_mem, 1);
			/* Call CVodeSVtolerances to specify the scalar relative tolerance
			* and vector absolute tolerances */
			retcode = CVodeSStolerances(s->cvode_mem, LOOSE_REL_TOL, LOOSE_ABS_TOL);
			/* maximum step number, just the default value because the matlab option doesn't exist*/ 
			retcode = CVodeSetMaxNumSteps(s->cvode_mem, LOOSE_NUMBER_STEPS);
			/* Reinitialize solver with loose tolerances */
			retcode = CVodeReInit(s->cvode_mem, s->t, s->y);
			mode = ZERO;
			if(retcode != CV_SUCCESS)
				fprintf(stderr, "Failed to reinitialize at time %.4f after lowering integration order to 1, error code %d\n", s->t, retcode);
			else{
				/* retry */
				retcode = CVode(s->cvode_mem, s->tout, s->y, &s->t, CV_NORMAL);
				if(retcode != CV_SUCCESS)
					fprintf(stderr, "Failed to reach time %.4f even after lowering integration order to 1, error code %d\n", s->tout, retcode);
				else{
					if(k<times_length-1)
						if(useArray)
							s->tout += step;
						else
							s->tout = getStep(timesteps, k+1);
				}
			}
		}else
			fprintf(stderr, "Failed to reach time %.4f, error code %d\n", s->tout, retcode);
	}
	if(retry >= MAX_RETRY){
		fprintf(stderr, "Can't reach time %.4f, retcode %d exiting.\n", s->tout, retcode);
		exit(1);
	}
    }

    /* write the last one*/
    if (transpose)
        {
            tdest[(times_length-1) + (dest_rows *  (ZERO))] = s->t;

            for (n = ZERO; n < y_len; n++)                
            {
                dest[(times_length-1 ) + (dest_rows * (transpose_shift + n))] = Ith(s->y, n);
            }
        }
            
        /* otherwise we can write column-major sequentially */
        else
        {
            *tdest++ = s->t;
                
           for (n = ZERO; n < y_len; n++)
            {
                *dest++ = Ith(s->y, n);
            }
        }    

    s->stop = clock();
}

void sun_eval(SUN_INIT *s, int dest_rows, int dest_cols, realtype *integration_times, int times_length, int transpose, int n_out_matrices, ...)
{
    va_list vl;
    va_start(vl, n_out_matrices);
    double *outmatrices[n_out_matrices];
    int i;
    for(i = 0 ; i < n_out_matrices ; ++i )
	outmatrices[i] = va_arg(vl, double *);

    assert(n_out_matrices <= 2 && "Only the first two output matrices will be used");
    int k, n;
    int y_len = NV_LENGTH_S(s->y);
    s->start = clock();   
    s->tout = integration_times[1];

    realtype *dest,*tdest;
    int transpose_shift = ZERO;
    if( n_out_matrices == 1 ){
    	tdest = (realtype *) outmatrices[0];
	dest = tdest;
        transpose_shift = ONE;
    }else{
        tdest = (realtype *) outmatrices[0];
        dest = (realtype *) outmatrices[1];
    }

    for (k = ONE; k < times_length; k++)
    {
        /*
        * As of version 2.3.0 (from documentation):
        *
        * Passing a value of ZERO for the maximum step size, the minimum step
        * size, or for maxsteps results in the solver using the corresponding
        * default value (infinity, 0, 500, respectively).
        */

        /* record output only at desired points */
        /* some interfaces like MATLAB prefer row-major output */    
        if (transpose)
        {
            tdest[(k) + (dest_rows *  (ZERO))] = s->t;

            for (n = ZERO; n < y_len; n++)                
            {
                dest[(k ) + (dest_rows * (transpose_shift + n))] = Ith(s->y, n);
            }
        }
            
        /* otherwise we can write column-major sequentially */
        else
        {    
            *tdest++ = s->t;
                
           for (n = ZERO; n < y_len; n++)
            {
                *dest++ = Ith(s->y, n);
            }
        }    
            
        /* continue solving */
        if (CVode(s->cvode_mem, s->tout, s->y, &s->t, CV_NORMAL) == CV_SUCCESS)
        {
        if(k<times_length-1)
            s->tout = integration_times[k+1];
        }
    }
    
    /* write the last one*/
    if (transpose)
        {
            tdest[(times_length-1) + (dest_rows *  (ZERO))] = s->t;

            for (n = ZERO; n < y_len; n++)                
            {
                dest[(times_length-1 ) + (dest_rows * (transpose_shift + n))] = Ith(s->y, n);
            }
        }
            
        /* otherwise we can write column-major sequentially */
        else
        {
           *tdest++ = s->t;
                
           for (n = ZERO; n < y_len; n++)
            {
                *dest++ = Ith(s->y, n);
            }
        }    

    s->stop = clock();
}

/*--------------------------------------------------------------------------*/

/*
* SUNDIALS summarizer. Same idea as above.
*/

void print_stats(void *cvode_mem)
{
    int flag;
    long int nst, nfe, nsetups, nje, nfeLS, nni, ncfn, netf, nge;

    flag = CVodeGetNumSteps(cvode_mem, &nst);
    check_flag(&flag, "CVodeGetNumSteps", ONE);

    flag = CVodeGetNumRhsEvals(cvode_mem, &nfe);
    check_flag(&flag, "CVodeGetNumRhsEvals", ONE);

    flag = CVodeGetNumLinSolvSetups(cvode_mem, &nsetups);
    check_flag(&flag, "CVodeGetNumLinSolvSetups", ONE);

    flag = CVodeGetNumErrTestFails(cvode_mem, &netf);
    check_flag(&flag, "CVodeGetNumErrTestFails", ONE);

    flag = CVodeGetNumNonlinSolvIters(cvode_mem, &nni);
    check_flag(&flag, "CVodeGetNumNonlinSolvIters", ONE);

    flag = CVodeGetNumNonlinSolvConvFails(cvode_mem, &ncfn);
    check_flag(&flag, "CVodeGetNumNonlinSolvConvFails", ONE);

    flag = CVodeGetNumJacEvals(cvode_mem, &nje);
    check_flag(&flag, "CVodeGetNumJacEvals", ONE);

    flag = CVodeGetNumLinRhsEvals(cvode_mem, &nfeLS);
    check_flag(&flag, "CVodeGetNumLinRhsEvals", ONE);

    flag = CVodeGetNumGEvals(cvode_mem, &nge);
    check_flag(&flag, "CVodeGetNumGEvals", ONE);

    fprintf(stderr, "Final Statistics:"NEW_LINE);
    fprintf(stderr,
        "nst = %-6ld nfe = %-6ld nsetups = %-6ld nfeLS = %-6ld nje = %ld",
        nst, nfe, nsetups, nfeLS, nje
    );
    fprintf(stderr, NEW_LINE);
    fprintf(stderr, "nni = %-6ld ncfn = %-6ld netf = %-6ld nge = %ld",
        nni, ncfn, netf, nge
    );
    fprintf(stderr, NEW_LINE);
}

double timediff(clock_t t1, clock_t t2)
{
    return ((double)t2 - t1) / CLOCKS_PER_SEC;
}

/*
* set the integration options
*/
INTEGRATION_OPTS* setIntegrationOptions(double absTol, double relTol, double maxStep, double initialStep, int maxOrder){
	
	INTEGRATION_OPTS *int_opts = &global_opts;
	
	int_opts->abs_tol = absTol;
	int_opts->rel_tol = relTol;
	int_opts->max_step = maxStep;
	int_opts->initial_step = initialStep;
	int_opts->max_order = maxOrder;

	return int_opts;
}

SUN_INIT *sun_free(SUN_INIT *s)
{
    /* ------------------------------------------------------------- OUTPUT */

    /* print elapsed time*/
    /*fprintf(stderr, "%.3f s"NEW_LINE, timediff(s->start, s->stop));*/

    /* print final statistics */
    /*print_stats(s->cvode_mem);*/

    /* ----------------------------------------------------------- CLEAN UP */
    /* free y vector */
    //N_VDestroy_Serial(s->y);

    /* free integrator memory */
    //CVodeFree(&s->cvode_mem);

    /* free linear solver */
    SUNLinSolFree(s->linearsolver);

#if defined(SUN_DENSE)
    /* free matrix */
    SUNMatDestroy(s->matrix);
#elif defined(SUN_SPGMR)
#elif defined(SUN_BAND)
    /* free matrix */
    SUNMatDestroy(s->matrix);
#else
    /* DEFAULT TO DENSE LINEAR SOLVER */
    /* free matrix */
    SUNMatDestroy(s->matrix);
#endif

    return s;
}

/*
* return the size of the integration array
*/
INTEGRATION_TIMES *vdIntegrationTimes(realtype *userDefinedTimes, int numberOfIterations, int defaultStepNumber){
    realtype *times;
	INTEGRATION_TIMES *ret = (INTEGRATION_TIMES *) qspcc_malloc(sizeof(INTEGRATION_TIMES));
	ret->type='d';
	if(numberOfIterations == 2){		
		ret->tin = userDefinedTimes[0];
		ret->tend = userDefinedTimes[1];
		realtype tinc = (ret->tend-ret->tin)/defaultStepNumber;
		ret->step = tinc;
		ret->times = NULL;
		ret->len = defaultStepNumber;
	}else if (numberOfIterations > 2){
		ret->times = userDefinedTimes;
		ret->len = numberOfIterations;
		ret->tin = userDefinedTimes[0];
		ret->tend = userDefinedTimes[numberOfIterations-1];
		ret->step = -1;
	}else{
		assert(0 && "the times specified by the user should be at least two");
		return NULL;
	}
	return ret;
}

INTEGRATION_TIMES *viIntegrationTimes(int *userDefinedTimes, int numberOfIterations, int defaultStepNumber){
    realtype *times;
	INTEGRATION_TIMES *ret = (INTEGRATION_TIMES *) qspcc_malloc(sizeof(INTEGRATION_TIMES));
	ret->type='i';
	if(numberOfIterations == 2){		
		ret->tin = (realtype)userDefinedTimes[0];
		ret->tend = (realtype)userDefinedTimes[1];
		ret->step = (ret->tend-ret->tin)/defaultStepNumber;
		ret->times = NULL;
		ret->len = defaultStepNumber;
	}else if (numberOfIterations > 2){
		ret->tin = (realtype) userDefinedTimes[0];
		ret->tend = (realtype) userDefinedTimes[numberOfIterations-1];
		ret->step = -1;
		ret->times = userDefinedTimes;
		ret->len = numberOfIterations;
	}else{
		assert(0 && "the times specified by the user should be at least two");
		return NULL;
	}
	return ret;
}

INTEGRATION_TIMES *viSlicedIntegrationTimes(int start, int sliced_constant, int end){
	INTEGRATION_TIMES *ret = (INTEGRATION_TIMES *) qspcc_malloc(sizeof(INTEGRATION_TIMES));

	ret->tin = (realtype) start;
	ret->tend = (realtype) end;
	ret->step = (realtype) sliced_constant;

	int numberOfIteration = viSequenceDimension(start, sliced_constant, end);
	ret->len = numberOfIteration;
	
	ret->times = NULL;
	
	return ret;
}

INTEGRATION_TIMES *vdSlicedIntegrationTimes(realtype start, realtype sliced_constant, realtype end){
	INTEGRATION_TIMES *ret = (INTEGRATION_TIMES *) qspcc_malloc(sizeof(INTEGRATION_TIMES));

	ret->tin = start;
	ret->tend = end;
	ret->step = sliced_constant;
	
	int numberOfIteration = vdSequenceDimension((double) start, (double) sliced_constant, (double) end);
	ret->len = numberOfIteration;
	
	ret->times = NULL;
	
	return ret;
}


