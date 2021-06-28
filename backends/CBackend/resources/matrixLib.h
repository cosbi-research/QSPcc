#ifndef __MAT_LIB__
#define __MAT_LIB__

#include <stdio.h>
#include "structures.h"
#include "sparseLib.h"

#define DOUBLE_EQUALITY_TOL 10e-15
/* matrix storage orientation, will be substituted at translation time by C backend */
#define <ROW_COL_MAJOR>
/* treshold on the number of elements of iteration. if greater, parallel OMP threads will be used */
#define PARALLELIZABLE_FOR_TRESHOLD 9999999
#define PARALLELIZABLE_MULTIPLY_TRESHOLD 9999999
#define SLICE_BY_INDEX_CONVENTIONAL_VALUE -1
#define MATRIX_ACCESS_CONVENTIONAL_VALUE -2

#ifdef ROWMAJOR
#define mat_elem(a, y, x, m, n) (a + ((y) * (n) + (x)))
#else
#define mat_elem(a, y, x, m, n) (a + ((y) + (x) * (m)))
#endif

#ifdef DEBUG
#define ASSERT(cond, message, ...) if(!(cond)){ \
char msg[100];                             \
snprintf(msg, 100, message, __VA_ARGS__);  \
fprintf(stderr, "ERROR in %s at line %d (%s): %s\n", __FILE__, __LINE__, __func__, msg); \
 exit(1); \
}
#else
#define ASSERT(cond, message, ...) 
#endif

// access a generic array (void *)
#define GARRAY(string_with_type, result_type, input_array, access_index) ((result_type) (string_with_type[0]=='i')? ((int*) input_array)[access_index] : ((double*) input_array)[access_index])
// cast generic value (void) to result_type, knowing that the actual value is stored in string_with_type
#define GVALUE(string_with_type, result_type, input_value) ((result_type) (string_with_type[0]=='i')? *((int*) input_value) : *((double*) input_value))

//functions alternative to mkl
extern void viPowImpl( int n, int *a, int *b, double *output);
extern void vdPowImpl( int n, double *a, double *b, double* output);
extern void viExpImpl( int n, int *input, double *output);
extern void vdExpImpl( int n, double *input, double *output);
extern void siExpImpl( int n, islice input, double *output);
extern void sdExpImpl( int n, dslice input, double *output);
extern void viSinImpl( int n, int *input, double *output);
extern void vdSinImpl( int n, double *input, double *output);
extern void siSinImpl( int n, islice input, double *output);
extern void sdSinImpl( int n, dslice input, double *output);
extern void viCosImpl( int n, int *input, double *output);
extern void vdCosImpl( int n, double *input, double *output);
extern void siCosImpl( int n, islice input, double *output);
extern void sdCosImpl( int n, dslice input, double *output);
extern void viTanImpl( int n, int *input, double *output);
extern void vdTanImpl( int n, double *input, double *output);
extern void siTanImpl( int n, islice input, double *output);
extern void sdTanImpl( int n, dslice input, double *output);
extern void viLnImpl( int n, int *input, double *output);
extern void vdLnImpl( int n, double *input, double *output);
extern void siLnImpl( int n, islice input, double *output);
extern void sdLnImpl( int n, dslice input, double *output);
extern void viLog10Impl( int n, int *input, double *output);
extern void vdLog10Impl( int n, double *input, double *output);
extern void siLog10Impl( int n, islice input, double *output);
extern void sdLog10Impl( int n, dslice input, double *output);
extern void viSign( int n, int *input, double *output);
extern void vdSign( int n, double *input, double *output);
extern void siSign( int n, islice input, double *output);
extern void sdSign( int n, dslice input, double *output);
extern void viAbsImpl( int n, int *input, double *output);
extern void vdAbsImpl( int n, double *input, double *output);
extern void siAbsImpl( int n, islice input, double *output);
extern void sdAbsImpl( int n, dslice input, double *output);
extern void viAtan2Impl( int n, int *a, int *b, double *output);
extern void vdAtan2Impl( int n, double *a, double* b, double *output);
extern void siAtan2Impl( int n, islice a, islice b, double *output);
extern void sdAtan2Impl( int n, dslice a, dslice b, double *output);

//vdFloor with int* output
extern void vdiFloor( int n, double *input, int *output);
extern void sdiFloor( int n, dslice input, int *output);
//vdCeil with int* output
extern void vdiCeil(int n, double *input, int *output);
extern void sdiCeil(int n, dslice input, int *output);
//vdRound with int* output
extern void vdiRound(int n, double *input, int *output);
extern void sdiRound(int n, dslice input, int *output);

// SCALAR ONLY FUNCTIONS

#define MAX2(a,b) \
   ({ __typeof__ (a) _a = (a); \
       __typeof__ (b) _b = (b); \
     _a > _b ? _a : _b; })

#define MAX3(a,b,c) MAX2(MAX2(a,b),c)
#define MAX4(a,b,c) MAX2(MAX3(a,b,c),d)

#define MIN2(a,b) \
   ({ __typeof__ (a) _a = (a); \
       __typeof__ (b) _b = (b); \
     _a < _b ? _a : _b; })

#define MIN3(a,b,c) MIN2(MIN2(a,b),c)
#define MIN4(a,b,c) MIN2(MIN3(a,b,c),d)

#define GET_MACRO(_1,_2,_3,_4,NAME,...) NAME
// this will be used by the program
#define max(...) GET_MACRO(__VA_ARGS__, MAX4, MAX3, MAX2)(__VA_ARGS__)
#define min(...) GET_MACRO(__VA_ARGS__, MIN4, MIN3, MIN2)(__VA_ARGS__)
// sign function implementation for scalar
#define sign(__x) ((__x > 0) - (__x < 0))

// we still don't support bigint
extern int factorial(int n);

// END SCALAR ONLY FUNCTIONS

#define scalarSum(__x, __y, type) ((type)(__x + __y))
#define scalarDiv(__x, __y, type) ((type)(((double)__x) / __y))

// neede for standard deviation and variance
#define secondMoment(__x, __y, type) ((type)(__x + (__y * __y) ))
#define divMomentSquared(__x, __n, __avg, type) ((type) ( (((double)__x) / ( __n - 1 ) ) - (__avg * __avg) ) )
#define divMomentSquaredStd(__x, __n, __avg, type) ((type) sqrt( (((double)__x) / ( __n - 1 ) ) - (__avg * __avg) ) )

#define ISLICE_IS_EMPTY(__x)(__x.end<__x.start)
#define DSLICE_IS_EMPTY(__x)(__x.end<__x.start)

/*
* computes the maximum over two vectors pointwise
* actual implementation defined in user_structures.c
* ex. __Max(d, double2dMatrix)
*/
#define __Max(type) \
void type##Max(type result, type first, int dimfirst, type second, int dimsecond){              \
	assert(dimfirst == dimsecond && "Can't compute 'max' of incompatible matrices");              \
                                                                                                      \
	for(int counter=0; counter<dimfirst; ++counter){                                              \
		if(first.matrix[counter] > second.matrix[counter] )                                   \
			result.matrix[counter] = first.matrix[counter];                               \
		else                                                                                  \
			result.matrix[counter] = second.matrix[counter];                              \
	}                                                                                             \
}

/*
* computes the minimum over two vectors pointwise
* actual implementation defined in user_structures.c
* ex. __Max(d, double2dMatrix)
*/
#define __Min(type) \
void type##Min(type result, type first, int dimfirst, type second, int dimsecond){              \
	assert(dimfirst == dimsecond && "Can't compute 'min' of incompatible matrices");              \
                                                                                                      \
	for(int counter=0; counter<dimfirst; ++counter){                                              \
		if(first.matrix[counter] < second.matrix[counter] )                                   \
			result.matrix[counter] = first.matrix[counter];                               \
		else                                                                                  \
			result.matrix[counter] = second.matrix[counter];                              \
	}                                                                                             \
}

/*
* computes the sum over two vectors pointwise
* actual implementation defined in user_structures.c
* ex. __Sum(d, double2dMatrix)
*/
#define __Sum(type) \
void type##Sum(type result, type first, int dimfirst, type second, int dimsecond){              \
	assert(dimfirst == dimsecond && "Can't compute 'sum' of incompatible matrices");              \
                                                                                                      \
	for(int counter=0; counter<dimfirst; ++counter){                                              \
		result.matrix[counter] = first.matrix[counter] + second.matrix[counter];              \
	}                                                                                             \
}

/*
* computes the mean over two vectors pointwise
* actual implementation defined in user_structures.c
* ex. __Mean(d, double2dMatrix)
*/
#define __Mean(rtype, type) \
void type##Mean(rtype result, type first, int dimfirst, type second, int dimsecond){                   \
	assert(dimfirst == dimsecond && "Can't compute 'mean' of incompatible matrices");             \
                                                                                                      \
	for(int counter=0; counter<dimfirst; ++counter){                                              \
		result.matrix[counter] = (first.matrix[counter] + second.matrix[counter])/2.0;        \
	}                                                                                             \
}

#define __Var(rtype, type) \
void type##Var(rtype result, type first, int dimfirst, type second, int dimsecond){                   \
	assert(dimfirst == dimsecond && "Can't compute 'mean' of incompatible matrices");             \
        double mean;                                                                                  \
	for(int counter=0; counter<dimfirst; ++counter){                                              \
		mean = (first.matrix[counter] + second.matrix[counter])/2.0;                          \
		result.matrix[counter] = (first.matrix[counter]*first.matrix[counter] + second.matrix[counter]*second.matrix[counter]) - mean*mean; \
	}                                                                                             \
}

#define __Std(rtype, type) \
void type##Std(rtype result, type first, int dimfirst, type second, int dimsecond){                   \
	assert(dimfirst == dimsecond && "Can't compute 'mean' of incompatible matrices");             \
        double mean;                                                                                  \
	for(int counter=0; counter<dimfirst; ++counter){                                              \
		mean = (first.matrix[counter] + second.matrix[counter])/2.0;                          \
		result.matrix[counter] = sqrt( (first.matrix[counter]*first.matrix[counter] + second.matrix[counter]*second.matrix[counter]) - mean*mean ); \
	}                                                                                             \
}

/*
* computes the maximum over vector and scalar pointwise
* actual implementation defined in user_structures.c
* ex. __ScalarMatrixMax(double, double2dMatrix)
*/
#define __ScalarMatrixMax(basetype, type) \
void type##ScalarMatrixMax(type result, type first, int dimfirst, basetype second){                   \
                                                                                                      \
	for(int counter=0; counter<dimfirst; ++counter){                                              \
		if(first.matrix[counter] > second )                                                   \
			result.matrix[counter] = first.matrix[counter];                               \
		else                                                                                  \
			result.matrix[counter] = second;                                              \
	}                                                                                             \
}

/*
* computes the minimum over vector and scalar pointwise
* actual implementation defined in user_structures.c
* ex. __ScalarMatrixMax(double, double2dMatrix)
*/
#define __ScalarMatrixMin(basetype, type) \
void type##ScalarMatrixMin(type result, type first, int dimfirst, basetype second){                   \
                                                                                                      \
	for(int counter=0; counter<dimfirst; ++counter){                                              \
		if(first.matrix[counter] < second )                                                   \
			result.matrix[counter] = first.matrix[counter];                               \
		else                                                                                  \
			result.matrix[counter] = second;                                              \
	}                                                                                             \
}

/*
* computes the max over one 1D vector, returns a scalar
* actual implementation defined in user_structures.c
* ex. __Max(d, double2dMatrix)
*/
#define __scalarMax(rtype, type) \
void type##ScalarMax(rtype *result, type in, int nelems){                                             \
	rtype max = 0;                                                                                \
	int first = 1;                                                                                \
	for(int nelem=0; nelem < nelems ; ++nelem)                                                    \
		if(first){			                                                      \
			max = in.matrix[nelem];                                                       \
			first = 0;                                                                    \
		}else if(in.matrix[nelem] > max)                                                      \
			max = in.matrix[nelem];                                                       \
                                                                                                      \
	*result = max;                                                                                \
}

#define __scalarMin(rtype, type) \
void type##ScalarMin(rtype *result, type in, int nelems){                                             \
	rtype min = 0;                                                                                \
	int first = 1;                                                                                \
	for(int nelem=0; nelem < nelems ; ++nelem)                                                    \
		if(first){			                                                      \
			min = in.matrix[nelem];                                                       \
			first = 0;                                                                    \
		}else if(in.matrix[nelem] < min)                                                      \
			min = in.matrix[nelem];                                                       \
                                                                                                      \
	*result = min;                                                                                \
}

#define __scalarSum(rtype, type) \
void type##ScalarSum(rtype *result, type in, int nelems){                                             \
	rtype min = 0;                                                                                \
	for(int nelem=0; nelem < nelems ; ++nelem)                                                    \
		min += in.matrix[nelem];                                                              \
                                                                                                      \
	*result = min;                                                                                \
}

#define __scalarMean(rtype, type) \
void type##ScalarMean(rtype *result, type in, int nelems){                                            \
	rtype min = 0;                                                                                \
	for(int nelem=0; nelem < nelems ; ++nelem)                                                    \
		min += in.matrix[nelem];                                                              \
                                                                                                      \
	*result = (rtype) ((double)min)/nelems;                                                       \
}

#define __scalarVar(rtype, type) \
void type##ScalarVar(rtype *result, type in, int nelems){                                             \
	double mean = 0;                                                                              \
	rtype secondAcc = 0;                                                                          \
	for(int nelem=0; nelem < nelems ; ++nelem){                                                   \
		mean += in.matrix[nelem];                                                             \
		secondAcc += in.matrix[nelem]*in.matrix[nelem];                                       \
	}                                                                                             \
        mean = (((double)mean)/nelems);                                                               \
	*result = (rtype) ( (secondAcc / (nelems - 1))  - mean*mean);                                 \
}

#define __scalarStd(rtype, type) \
void type##ScalarStd(rtype *result, type in, int nelems){                                             \
	double mean = 0;                                                                              \
	rtype secondAcc = 0;                                                                          \
	for(int nelem=0; nelem < nelems ; ++nelem){                                                   \
		mean += in.matrix[nelem];                                                             \
		secondAcc += in.matrix[nelem]*in.matrix[nelem];                                       \
	}                                                                                             \
        mean = (((double)mean)/nelems);                                                               \
	*result = (rtype) sqrt( ( (secondAcc / (nelems - 1))  - mean*mean) );                         \
}

// ---- FUNCTIONS THAT MANIPULATE A MATRIX AS A WHOLE (DOESN'T ALTER ONLY ELEMENTS, BUT ALSO DIMENSION) ----
// operates on 1D matrix
// *NOTE*: there is a difference between this unique implementation
//         and the MATLAB one, the output is not sorted.
extern int vdUnique(double *input, int ndims, int nargs, ...);
extern int viUnique(int *input, int ndims, int nargs, ...);

extern int vviSetdiff(int *s1, void *s2, const char *s2type, int s2dim, int s1dims, int nargs, ...);
extern int vvdSetdiff(double *s1, void *s2, const char *s2type, int s2dim, int s1dims, int nargs, ...);
// between a slice and a vector
extern int sviSetdiff(islice s1, void *s2, const char *s2type, int s2dim, int s1dims, int nargs, ...);
extern int svdSetdiff(dslice s1, void *s2, const char *s2type, int s2dim, int s1dims, int nargs, ...);
// ---- END

extern int vdUnion(double *s1, void *s2, const char *s2type, int s2dim, int s1dims, int nargs, ...);
extern int viUnion(int *s1, void *s2, const char *s2type, int s2dim, int s1dims, int nargs, ...);

extern int viSort(int *input, const char *direction, int ndims, int nargs, ...);
extern int vdSort(double *input, const char *direction, int ndims, int nargs, ...);

extern int viSequenceDimension(int start, int step, int end);
extern int *viSequenceVector(int *out_vector, int start, int step, int end);
extern int vdSequenceDimension(double start, double step, double end);
#define SEQUENCE_TOL 1e-7
extern double *vdSequenceVector(double *out_vector, double start, double step, double end);
extern double *vdLogSequenceVector(double *out_vector, double start, double step, double end);
extern int viSliceAccess(islice *slice, int ith);
extern double vdSliceAccess(dslice *slice, int ith);


extern void dTransposeMatrixInplace(double *out_matrix, int rows, int cols);
extern void dTransposeMatrix(void *dest, void *src, int src_h, int src_w);
extern void iTransposeMatrixInplace(int *out_matrix, int rows, int cols);
extern void iTransposeMatrix(void *dest, void *src, int src_h, int src_w);
//generic function to print a generic matrix
extern void printMatrix(void *m, int *poly_basis, size_t size, void (*print)(FILE *, char *), int dimensions, ...);
//all-param-defined version of previous function
extern void printMatrixAux(void *m, size_t size, void (*print)(FILE *, char *), int *dim, int *poly_basis, int dimensions, int *access_numbers, int ith_dim, FILE *f);
//specific print for each type
extern void printScalar(FILE *f, double *s);
extern void printInt(FILE *f, int *s);

extern int matrixAccess(int *poly_basis, int mat_dimensions, int n_access_numbers, ...);
// mat_dim: the matrix to be accessed dimensions
// mat_dimensions: the number of dimensions of the matrix to be accessed
// access_numbers: the matrix access parameters ex. a(1,2,3) -> access_numbers = 1,2,3
// n_access_numbers: the length of access_numbers vector
int flattenMatrixAccessAlgorithm(int *mat_dim, int *poly_basis, int mat_dimensions, int *access_numbers, int n_access_numbers);


extern int *doubledoubleSliceMatrix(double **out_matrix, int *out_matrix_poly_basis, QSPCC_INT **outRowIndex, QSPCC_INT **outColumns, QSPCC_INT *outUsedCells, int *outRealsize, double *in_matrix, 
				int *in_matrix_poly_basis, QSPCC_INT *rowIndex, QSPCC_INT *columns, int in_matrix_len, int out_matrix_len, 
				int (*slice_triplets)[3], int n_slice_triplets, char *slice_type, int n_mat_slices, ... );
extern void doubledoubleSliceMatrixAlgorithm(int *mat_access_idxs, int mat_access_idxs_len, int *slice_access_idxs, int slice_access_idxs_len, 
						int n, int *out_matrix_idx, int *update_matrix_dims,               
						int *out_matrix_poly_basis, int *out_matrix_dims, double **out_matrix, QSPCC_INT **outRowIndex, QSPCC_INT **outColumns, QSPCC_INT *outUsedCells, 
						int *outRealsize, int out_matrix_len, 
						double *in_matrix, QSPCC_INT *rowIndex, QSPCC_INT *columns, int *in_matrix_dims, int *in_matrix_poly_basis, int in_matrix_len,         
						int (*slice_triplets)[3],                                                                                                                
                                                int n_slice_triplets,                                                                                                                    
						char *slice_type, int n_params, int **mat_slices, int **mat_slices_dims, int **mat_slices_poly_basis,                                                  
						int *mat_slices_len, int *original_mat_slices_len, int n_mat_slices, char single_matrix_access, char last_type);

extern int *doubleintSliceMatrix(double **out_matrix, int *out_matrix_poly_basis, QSPCC_INT **outRowIndex, QSPCC_INT **outColumns, QSPCC_INT *outUsedCells, int *outRealsize, int *in_matrix, 
				int *in_matrix_poly_basis, QSPCC_INT *rowIndex, QSPCC_INT *columns, int in_matrix_len, int out_matrix_len, 
				int (*slice_triplets)[3], int n_slice_triplets, char *slice_type, int n_mat_slices, ... );
extern void doubleintSliceMatrixAlgorithm(int *mat_access_idxs, int mat_access_idxs_len, int *slice_access_idxs, int slice_access_idxs_len, 
						int n, int *out_matrix_idx, int *update_matrix_dims,               
						int *out_matrix_poly_basis, int *out_matrix_dims, double **out_matrix, QSPCC_INT **outRowIndex, QSPCC_INT **outColumns, QSPCC_INT *outUsedCells, 
						int *outRealsize, int out_matrix_len, 
						int *in_matrix, QSPCC_INT *rowIndex, QSPCC_INT *columns, int *in_matrix_dims, int *in_matrix_poly_basis, int in_matrix_len,         
						int (*slice_triplets)[3],                                                                                                                
                                                int n_slice_triplets,                                                                                                                    
						char *slice_type, int n_params, int **mat_slices, int **mat_slices_dims, int **mat_slices_poly_basis,                                                  
						int *mat_slices_len, int *original_mat_slices_len, int n_mat_slices, char single_matrix_access, char last_type);

extern int *intdoubleSliceMatrix(int **out_matrix, int *out_matrix_poly_basis, QSPCC_INT **outRowIndex, QSPCC_INT **outColumns, QSPCC_INT *outUsedCells, int *outRealsize, double *in_matrix, 
				int *in_matrix_poly_basis, QSPCC_INT *rowIndex, QSPCC_INT *columns, int in_matrix_len, int out_matrix_len, 
				int (*slice_triplets)[3], int n_slice_triplets, char *slice_type, int n_mat_slices, ... );
extern void intdoubleSliceMatrixAlgorithm(int *mat_access_idxs, int mat_access_idxs_len, int *slice_access_idxs, int slice_access_idxs_len, 
						int n, int *out_matrix_idx, int *update_matrix_dims,               
						int *out_matrix_poly_basis, int *out_matrix_dims, int **out_matrix, QSPCC_INT **outRowIndex, QSPCC_INT **outColumns, QSPCC_INT *outUsedCells, 
						int *outRealsize, int out_matrix_len, 
						double *in_matrix, QSPCC_INT *rowIndex, QSPCC_INT *columns, int *in_matrix_dims, int *in_matrix_poly_basis, int in_matrix_len,         
						int (*slice_triplets)[3],                                                                                                                
                                                int n_slice_triplets,                                                                                                                    
						char *slice_type, int n_params, int **mat_slices, int **mat_slices_dims, int **mat_slices_poly_basis,                                                  
						int *mat_slices_len, int *original_mat_slices_len, int n_mat_slices, char single_matrix_access, char last_type);

extern int *intintSliceMatrix(int **out_matrix, int *out_matrix_poly_basis, QSPCC_INT **outRowIndex, QSPCC_INT **outColumns, QSPCC_INT *outUsedCells, int *outRealsize, int *in_matrix, 
				int *in_matrix_poly_basis, QSPCC_INT *rowIndex, QSPCC_INT *columns, int in_matrix_len, int out_matrix_len, 
				int (*slice_triplets)[3], int n_slice_triplets, char *slice_type, int n_mat_slices, ... );
extern void intintSliceMatrixAlgorithm(int *mat_access_idxs, int mat_access_idxs_len, int *slice_access_idxs, int slice_access_idxs_len, 
						int n, int *out_matrix_idx, int *update_matrix_dims,               
						int *out_matrix_poly_basis, int *out_matrix_dims, int **out_matrix, QSPCC_INT **outRowIndex, QSPCC_INT **outColumns, QSPCC_INT *outUsedCells, 
						int *outRealsize, int out_matrix_len, 
						int *in_matrix, QSPCC_INT *rowIndex, QSPCC_INT *columns, int *in_matrix_dims, int *in_matrix_poly_basis, int in_matrix_len,         
						int (*slice_triplets)[3],                                                                                                                
                                                int n_slice_triplets,                                                                                                                    
						char *slice_type, int n_params, int **mat_slices, int **mat_slices_dims, int **mat_slices_poly_basis,                                                  
						int *mat_slices_len, int *original_mat_slices_len, int n_mat_slices, char single_matrix_access, char last_type);

/**
 * function to apply a generic function on every element of a N-dimensional matrix
 */
void mapply(void *m, size_t size, void (*print)(char *, int *, int, void *), int *dim, int* poly_bases, int dimensions, void *extra);

// interpolation functions
extern double sdNearestNeighborInterp(double *x, int x_dim, double *y, int y_dim, double interpolation_point);

//test matrix values
extern void dmIsInf(int *out, double *toBeTested, int dimensions, ...);
extern void dmIsNan(int *out, double *toBeTested, int dimensions, ...);

// functions to multiply matrices
extern void dMultiplyMatrices(double *A, double *B, double *C, int m, int n, int k);
extern void iMultiplyMatrices(int *A, int *B, int *C, int m, int n, int k);

// solve linear systems
extern void dSolveLinearSystem(double *A, double *B, double *C, int m, int na, int nb);
extern void iSolveLinearSystem(int *A, int *B, double *C, int m, int na, int nb);

//gaussian simulator
//number of iid variables used to get the gaussian simulator 
// through the central limit theorem
#define CENTRAL_LIMIT_IID_NUM 25
extern double *randnImpl(double *outmatrix, double mean, double sigma, int dimensions, ...);
// random numbers from uniform distribution
extern double *randuImpl(double *outmatrix, double from, double to, int dimensions, ...);
// random numbers
extern int *randiImpl(int *outmatrix, int from, int to, int dimensions, ...);
// kronecker tensor product
extern void dKroneckerTensorProduct(double *A, double *B, double *C, int m, int n, int q, int p);
extern void iKroneckerTensorProduct(int *A, int *B, int *C, int m, int n, int q, int p);
// repeat a generic matrix n times on rows, m on cols, z on z axis and so on.
extern void dRepeatMatrix(double* out, int *out_poly_basis, double* in, int *in_poly_basis, int in_mat_dims_num, int totalParams, ...);
extern void iRepeatMatrix(int* out, int *out_poly_basis, int* in, int *in_poly_basis, int in_mat_dims_num, int totalParams, ...);

extern double modulBetweenDoubles(double first, double second);

extern int iContainedInArray(int n, int *input, int element);
extern int iContainedInSlice(int start, int step, int end, int element);
extern int dContainedInArray(int n, double *input, double element);
extern int dContainedInSlice(double start, double step, double end, double element);
extern void viContainedInArray(int n, int *input, int m, int *toBeChecked, int *output);
extern void viContainedInSlice(int start, int step, int end, int m, int *toBeChecked, int *output);
extern void siContainedInArray(int n, int *input, int start, int step, int end, int *output);
extern void siContainedInSlice(int inputStart, int inputStep, int inputEnd, int start, int step, int end, int *output);
extern void vdContainedInArray(int n, double *input, int m, double *toBeChecked, int *output);
extern void vdContainedInSlice(double inputStart, double inputStep, double inputEnd, int m, double *toBeChecked, int *output);
extern void sdContainedInArray(int n, double *input, double start, double step, double end, int *output);
extern void sdContainedInSlice(double inputStart, double inputStep, double inputEnd, double start, double step, double end, int *output);

//inverts the order in the flattened array of a matrix from row-major to column-major
extern void turnIntoColumnMajor(void* pointer, void* output, size_t size, int dimNumber, int *dimensions);
extern void turnIntoRowMajor(void* pointer, void* output, size_t size, int dimNumber, int *dimensions);
extern void viReshape(int* pointer, int* output, int originalDimensionNum, int totalDimensionNum, ...);
extern void viReshapeAlgorithm(int *pointer, int *output, int originalTotNum, int *originalDimensions, int originalDimensionNum, int *outputDimensions, int outputDimensionNum);
extern void siReshape(int* output, int start, int end, int step, int outputDimensionNum, ...);
// utilities to convert flat rowmajor index to colmajor and viceversa
// -------------- POINTER POSITION INVERTER --------------------------
/*
 * convert a 0-based flat index in rowmajor to a 0-based flat index in colmajor
 */
static inline int rowMajor2ColMajor(int flatIdx, int dim1, int dim2){
  int residual = flatIdx % (dim1*dim2);
  return (dim1<=1 || dim2<=1)? flatIdx : flatIdx + (residual/dim2)*(1-dim2) + (residual%dim2)*(dim1-1);
  /*
  int numberElementsForFloor = dim1*dim2;
  int residual = flatIdx % numberElementsForFloor;
  // the flattened matrix access is i*numCol+j 
  int j = residual % dim2;
  int i =  residual / dim2;
  return flatIdx + i*(1-dim2)+j*(dim1-1);
  */
}

/*
 * convert a 0-based flat index in colmajor to a 0-based flat index in rowmajor
 */
static inline int colMajor2RowMajor(int flatIdx, int dim1, int dim2){
  int residual = flatIdx % (dim1*dim2);
  return (dim1<=1 || dim2<=1)? flatIdx : flatIdx + (residual%dim1)*(dim2-1) + (residual/dim1)*(1-dim1);
}

extern void vdReshape(double* pointer, double* output, int originalDimensionNum, int totalDimensionNum, ...);
extern void vdReshapeAlgorithm(double *pointer, double *output, int originalTotNum, int *originalDimensions, int originalDimensionNum, int *outputDimensions, int outputDimensionNum);
extern void sdReshape(double* output, double start, double end, double step, int outputDimensionNum, ...);

//sum cumulatively matrix elements
extern void viCumSum(int *pointer, int* output, int dimNumber ,...);
extern void vdCumSum(double *pointer, double* output, int dimNumber ,...);
extern void siCumSum(int* output, int start, int end, int step);
extern void sdCumSum(double* output, double start, double end, double step);

// interpolation
extern double dInterp(double* xValues, int xValuesLen, double* yValues, int yValuesLen, double xToBeInterpolated);

#endif
