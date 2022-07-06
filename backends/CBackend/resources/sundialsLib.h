#ifndef __SUN_LIB__
#define __SUN_LIB__

#define QSPCC_SUNDIALS_VERSION <QSPCC_SUNDIALS_VERSION>

#if QSPCC_SUNDIALS_VERSION > 2
#include <sunmatrix/sunmatrix_dense.h>    /* access to dense SUNMatrix                */
#include <sunlinsol/sunlinsol_dense.h>    /* access to dense SUNLinearSolver          */
#include <sunlinsol/sunlinsol_spgmr.h>   /* access to SPGMR SUNLinearSolver   */
#include <sunlinsol/sunlinsol_spbcgs.h>  /* access to SPBCGS SUNLinearSolver  */
#include <sunlinsol/sunlinsol_sptfqmr.h> /* access to SPTFQMR SUNLinearSolver */
#include <sunlinsol/sunlinsol_pcg.h> /* access to SPTFQMR SUNLinearSolver */
#endif

#if QSPCC_SUNDIALS_VERSION == 6
#include <sundials/sundials_context.h>
#endif

extern void __wrap_matrix_function(double*(*fn)(double t, double *y, int y_dim1, int y_dim2), realtype t, N_Vector y, N_Vector ydot, void *user_data);
extern void __wrap_scalar_function(double(*fn)(double t, double *y, int y_dim1, int y_dim2), realtype t, N_Vector y, N_Vector ydot, void *user_data);

#define ZERO            0
#define ONE             1
#define TWO             2

#define OMP_THREAD_NUM 4
#define MAX_RETRY 3
#define LOOSE_REL_TOL 1.0E-2
#define LOOSE_ABS_TOL 1.0E-3
#define LOOSE_NUMBER_STEPS 999999
#define RESET_TO_DEFAULT_ORDER_AFTER_H_ABOVE 0.5

#define DEFAULT_REL_TOL 1.0E-3
#define DEFAULT_ABS_TOL 1.0E-6
#define DEFAULT_MAX_STEP -1
#define DEFAULT_NUMBER_STEPS 999
#define DEFAULT_MIN_STEP DBL_EPSILON
#define DEFAULT_SET_MAX_ORDER 5
#define DEFAULT_INITIAL_STEP -1.0
#define DEFAULT_ERROR_TEST_FAILURES 7
#define DEFAULT_NON_LINEAR_ITERS 10
#define DEFAULT_CONVERGENCE_FAILURES 10
#define NEW_LINE        "\n"            /* can be sequence of character(s)  */

/*
* Ith(v,i) references the ith component of the vector 'v', where 'i' is in the
* range [1..NEQ] and NEQ is defined below. The Ith macro is defined using the
* N_VIth macro in 'nvector.h'. 'N_VIth()' numbers the components of a vector
* starting from 0.
*/

#define Ith(v,i)        NV_Ith_S(v,i)   /* Ith numbers components 1..NEQ    */

/*
* struct used to store the options for the numerical solver
*/
typedef struct numerical_integration_options{
	double abs_tol;
	double rel_tol;
	double max_step;
	double initial_step;
	int max_order;
} INTEGRATION_OPTS;

typedef struct integration_times_def {
	void *times;
	realtype tin;
	realtype tend;
	realtype step;
	int len;
	char type; /* if times is an array of realtype or int */
} INTEGRATION_TIMES;


/*
* SUNDIALS initializer data structure. Allows us to consolidate all SUNDIALS-
* related stuff, and use a single function to initialize it.
*/

typedef struct SUNDIALS_INITIALIZER
{
    /* clock variables */
    clock_t start;
    clock_t stop;

    /* miscellaneous variables */
    int flag;
    int max_order;
    realtype atol;
    realtype rtol;
    realtype t;
    realtype tout;

    /* solver memory */
    void *cvode_mem;
    /* serial vector */
    N_Vector y;
    /* additional data structures */
#if QSPCC_SUNDIALS_VERSION > 2
    SUNMatrix matrix;
    SUNLinearSolver linearsolver;
#endif
#if QSPCC_SUNDIALS_VERSION == 6
    SUNContext sunctx;
#endif
} SUN_INIT;


extern SUN_INIT *sun_init_i(SUN_INIT *s, int (*f)(realtype t, N_Vector y, N_Vector ydot, void *user_data), int *y, int y_len, double tinit_as_double, double tend_as_double, INTEGRATION_OPTS* opts, void *userdata);
extern SUN_INIT *sun_init_d(SUN_INIT *s, int (*f)(realtype t, N_Vector y, N_Vector ydot, void *user_data), double *y, int y_len, realtype tinit, realtype tend, INTEGRATION_OPTS* opts, void *user_data);

// different parameters for the same algorithm. sundials evaluator
extern void sd_sun_eval(SUN_INIT *s, int dest_rows, int dest_cols, INTEGRATION_TIMES *times, int transpose, int n_out_matrices, ...);

extern SUN_INIT *sun_free(SUN_INIT *s);
extern void print_results(realtype *res, int row_num, int col_num);
extern void print_stats(void *cvode_mem);

extern int getIntegrationTimes(realtype *userDefinedTimes, int size, int stepNumber);

extern INTEGRATION_TIMES *vdIntegrationTimes(realtype *userDefinedTimes, int numberOfIterations, int maxStepNumber);
extern INTEGRATION_TIMES *viIntegrationTimes(int *userDefinedTimes, int numberOfIterations, int maxStepNumber);
extern INTEGRATION_TIMES *viSlicedIntegrationTimes(int start, int end, int sliced_constant);
extern INTEGRATION_TIMES *vdSlicedIntegrationTimes(realtype start, realtype end, realtype sliced_constant);

extern INTEGRATION_OPTS *setIntegrationOptions(double absTol, double relTol, double maxStep, double initialStep, int maxOrder);

#endif
