#include<stdio.h>
#include<stdlib.h>
#include <time.h>
#include<math.h>
#include <stdarg.h>
#include<assert.h>
#include <string.h>
#include <gperftools/tcmalloc.h>
#include "matrixLib.h"
#include "memoryLib.h"
#include "qsort-inline.h"
#include<limits.h>

// ---- begin factorial ---
/**
 * Returns the factorial of n. 
 *
 * @param n the number to consider
 * @return the factorial
 */

unsigned long product(unsigned long *currentN, int n){
  int m = n / 2;
  if (m == 0) return *currentN += 2;
  if (n == 2) return (*currentN += 2) * (*currentN += 2);
  return product(currentN, n - m) * product(currentN, m);
}

int floorlog(unsigned int bits) // returns 0 for bits=0
{
  int log = 0;
  if ((bits & 0xffff0000) != 0) {
    bits >>= 16;
    log = 16;
  }
  if (bits >= 256) {
    bits >>= 8;
    log += 8;
  }
  if (bits >= 16) {
    bits >>= 4;
    log += 4;
  }
  if (bits >= 4) {
    bits >>= 2;
    log += 2;
  }
  return log + (bits >> 1);
}

int factorial(int n) {
  if (n < 2)
    return 1;

  unsigned long p = 1;
  unsigned long r = 1;
  unsigned long currentN = 1;
  unsigned long res;

  int h = 0, shift = 0, high = 1;
  int log2n = floorlog(n);
  while (h != n) {
    shift += h;
    h = n >> log2n--;
    int len = high;
    high = (h - 1) | 1;
    len = (high - len) / 2;
    if (len > 0) {
	p = p*product(&currentN, len);
	r = r*p;
    }
  }
  res = r << shift;

#ifdef DEBUG
  if(res > INT_MAX) { 
	fprintf(stderr, "ERROR: factorial result exceeds maximum integer value\n"); 
  }
#endif
  return (int) res;
}

// ---- END FACTORIAL --

//my own implementation ad-hoc for integers
void *imemcpy(int *dest, const int *src, int n)
{
    int *dp = dest;
    const int *sp = src;
    while (n--)
        *dp++ = *sp++;
    return dest;
}

//viPow (n, a, b, y);
void viPowImpl( int n, int *a, int *b, double* output){
	for(int i = 0 ; i < n ; ++i){
		output[i] = pow(a[i], b[i]);
	}
}

//matrix pow alternative to mkl
void vdPowImpl( int n, double *a, double *b, double* output){
	for(int i = 0 ; i < n ; ++i){
		output[i] = pow(a[i], b[i]);
	}
}

//viExp( n, a, y );
void viExpImpl( int n, int *input, double *output){
	for(int i = 0 ; i < n ; ++i)
		output[i] = exp((double)input[i]);
}

// on sequence
void siExpImpl( int n, islice input, double *output){
	for(int i = 0 ; i < n ; ++i)
		output[i] = exp((double) viSliceAccess(&input, i));
}

//matrix exp alternative to mkl
void vdExpImpl( int n, double *input, double *output){
	for(int i = 0 ; i < n ; ++i)
		output[i] = exp(input[i]);
}

// on sequence
void sdExpImpl( int n, dslice input, double *output){
	for(int i = 0 ; i < n ; ++i)
		output[i] = exp(vdSliceAccess(&input, i));
}

//viSin( n, a, y );
void viSinImpl( int n, int *input, double *output){
	for(int i = 0 ; i < n ; ++i)
		output[i] = sin((double)input[i]);
}

// on sequence
void siSinImpl( int n, islice input, double *output){
	for(int i = 0 ; i < n ; ++i)
		output[i] = sin((double) viSliceAccess(&input, i));
}

//matrix sin alternative to mkl
void vdSinImpl( int n, double *input, double *output){
	for(int i = 0 ; i < n ; ++i)
		output[i] = sin(input[i]);
}

// on sequence
void sdSinImpl( int n, dslice input, double *output){
	for(int i = 0 ; i < n ; ++i)
		output[i] = sin(vdSliceAccess(&input, i));
}

//viCos( n, a, y );
void viCosImpl( int n, int *input, double *output){
	for(int i = 0 ; i < n ; ++i)
		output[i] = cos((double)input[i]);
}

// on sequence
void siCosImpl( int n, islice input, double *output){
	for(int i = 0 ; i < n ; ++i)
		output[i] = cos((double) viSliceAccess(&input, i));
}

//matrix sin alternative to mkl
void vdCosImpl( int n, double *input, double *output){
	for(int i = 0 ; i < n ; ++i)
		output[i] = cos(input[i]);
}

// on sequence
void sdCosImpl( int n, dslice input, double *output){
	for(int i = 0 ; i < n ; ++i)
		output[i] = cos(vdSliceAccess(&input, i));
}

//viTan( n, a, y );
void viTanImpl( int n, int *input, double *output){
	for(int i = 0 ; i < n ; ++i)
		output[i] = tan((double)input[i]);
}

// on sequence
void siTanImpl( int n, islice input, double *output){
	for(int i = 0 ; i < n ; ++i)
		output[i] = tan((double) viSliceAccess(&input, i));
}

//matrix sin alternative to mkl
void vdTanImpl( int n, double *input, double *output){
	for(int i = 0 ; i < n ; ++i)
		output[i] = tan(input[i]);
}

// on sequence
void sdTanImpl( int n, dslice input, double *output){
	for(int i = 0 ; i < n ; ++i)
		output[i] = tan(vdSliceAccess(&input, i));
}


// atan2 implementation
void viAtan2Impl( int n, int *a, int *b, double *output){
	for(int i = 0 ; i < n ; ++i)
		output[i] = atan2(a[i], b[i]);
}
void vdAtan2Impl( int n, double *a, double *b, double *output){
	for(int i = 0 ; i < n ; ++i)
		output[i] = atan2(a[i], b[i]);
}
void siAtan2Impl( int n, islice a, islice b, double *output){
	for(int i = 0 ; i < n ; ++i)
		output[i] = atan2(viSliceAccess(&a, i), viSliceAccess(&b, i));
}
void sdAtan2Impl( int n, dslice a, dslice b, double *output){
	for(int i = 0 ; i < n ; ++i)
		output[i] = atan2(vdSliceAccess(&a, i), vdSliceAccess(&b, i));
}


//viLog( n, a, y );
void viLnImpl( int n, int *input, double *output){
	for(int i = 0 ; i < n ; ++i)
		output[i] = log((double)input[i]);
}

// on sequence
void siLnImpl( int n, islice input, double *output){
	for(int i = 0 ; i < n ; ++i)
		output[i] = log((double) viSliceAccess(&input, i));
}

//matrix log alternative to mkl
void vdLnImpl( int n, double *input, double *output){
	for(int i = 0 ; i < n ; ++i)
		output[i] = log(input[i]);
}

// on sequence
void sdLnImpl( int n, dslice input, double *output){
	for(int i = 0 ; i < n ; ++i)
		output[i] = log(vdSliceAccess(&input, i));
}

//viLog10( n, a, y );
void viLog10Impl( int n, int *input, double *output){
	for(int i = 0 ; i < n ; ++i)
		output[i] = log10((double)input[i]);
}

// on sequence
void siLog10Impl( int n, islice input, double *output){
	for(int i = 0 ; i < n ; ++i)
		output[i] = log10((double) viSliceAccess(&input, i));
}

//matrix log10 alternative to mkl
void vdLog10Impl( int n, double *input, double *output){
	for(int i = 0 ; i < n ; ++i)
		output[i] = log10(input[i]);
}

// on sequence
void sdLog10Impl( int n, dslice input, double *output){
	for(int i = 0 ; i < n ; ++i)
		output[i] = log10(vdSliceAccess(&input, i));
}

//vdFloor(n, a, y) used instead of the mkl one because
//this can return a pointer to int, whereas the fist one
//doesn't do it
void vdiFloor(int n, double *input, int *output){
	for(int i = 0 ; i < n ; ++i)
		output[i] = (int) (input[i]);
}

// on sequence
void sdiFloor(int n, dslice input, int *output){
	for(int i = 0 ; i < n ; ++i)
		output[i] = (int) (vdSliceAccess(&input, i));
}

//vdCeil(n, a, y) used instead of the mkl function because
//this can return a pointer to int, whereas the fist one
//doesn't do it
void vdiCeil( int n, double *input, int *output){
	for(int i = 0 ; i < n ; ++i)
		output[i] = (int) ceil(input[i]);
}

// on sequence
void sdiCeil( int n, dslice input, int *output){
	for(int i = 0 ; i < n ; ++i)
		output[i] = (int) ceil(vdSliceAccess(&input, i));
}

//vdRound with int* output
void vdiRound(int n, double *input, int *output){
	for(int i = 0 ; i < n ; ++i)
		output[i] = (int) lround(input[i]);	
}
void sdiRound(int n, dslice input, int *output){
	for(int i = 0 ; i < n ; ++i)
		output[i] = (int) lround(vdSliceAccess(&input, i));
}

// sign implementation
void viSign( int n, int *input, double *output){
	for(int i = 0 ; i < n ; ++i)
		output[i] = (int) sign(input[i]);
}
void vdSign( int n, double *input, double *output){
	for(int i = 0 ; i < n ; ++i)
		output[i] = (double) sign(input[i]);
}
void siSign( int n, islice input, double *output){
	for(int i = 0 ; i < n ; ++i)
		output[i] = (int) sign(viSliceAccess(&input, i));
}
void sdSign( int n, dslice input, double *output){
	for(int i = 0 ; i < n ; ++i)
		output[i] = (double) sign(vdSliceAccess(&input, i));
}

// abs implementation
void viAbsImpl( int n, int *input, double *output){
	for(int i = 0 ; i < n ; ++i)
		output[i] = abs(input[i]);
}
void vdAbsImpl( int n, double *input, double *output){
	for(int i = 0 ; i < n ; ++i)
		output[i] = fabs(input[i]);
}
void siAbsImpl( int n, islice input, double *output){
	for(int i = 0 ; i < n ; ++i)
		output[i] = abs(viSliceAccess(&input, i));
}
void sdAbsImpl( int n, dslice input, double *output){
	for(int i = 0 ; i < n ; ++i)
		output[i] = fabs(vdSliceAccess(&input, i));
}

//test if the elements of the matrix toBeTested are infty
//write the results in the out matrix
void dmIsInf(int *out, double *toBeTested, int dimensions, ...){
	va_list vl;
	va_start(vl, dimensions);
	
	int mat_dim = 1;
	int i;
	for(i = 0 ; i < dimensions ; ++i ){
		mat_dim *= va_arg(vl,int);
	}
	
	for(i=0; i < mat_dim; ++i){
		out[i]=isinf(toBeTested[i]);
	}
}

//test if the elements of the matrix toBeTested are nan
//write the results in the out matrix
void dmIsNan(int *out, double *toBeTested, int dimensions, ...){
	va_list vl;
	va_start(vl, dimensions);
	
	int mat_dim = 1;
	int i;
	for(i = 0 ; i < dimensions ; ++i ){
		mat_dim *= va_arg(vl,int);
	}
	
	for(i=0; i < mat_dim; ++i){
		out[i]=isnan(toBeTested[i]);
	}
}

int viSequenceDimension(int start, int step, int end){
	return max(1, (end-start)/step + 1);
}

//initialize out_v with the given sequence
//to be used in a sliceMatrix function.
//out_v is assumed to be already allocated and large enough
int *viSequenceVector(int *out_vector, int start, int step, int end){
	int midx = 0;
	int out_v_len = viSequenceDimension(start,step,end);
	#pragma omp smid
	for(int i=start; i<=end ; i += step)
		out_vector[midx++] = i;
	return out_vector;
}

int vdSequenceDimension(double start, double step, double end){
	return (int) max(1.0, (end-start)/step + 1.0);
}

// equivalent to MATLAB logspace, compute the full vector given a slice in a logspace
double *vdLogSequenceVector(double *out_vector, double start, double step, double end){
	int midx = 0;
	double expStart=pow(10, start);
	double expEnd=pow(10, end);
	#pragma omp smid
	for(double i=expStart; i < (expEnd + SEQUENCE_TOL) ; i += step)
		out_vector[midx++] = pow(10, i);
	return out_vector;	
}

// equivalent to MATLAB linspace, compute the full vector given a slice in a logspace
double *vdSequenceVector(double *out_vector, double start, double step, double end){
	int midx = 0;
	#pragma omp smid
	for(double i=start; i < (end + SEQUENCE_TOL) ; i += step)
		out_vector[midx++] = i;
	return out_vector;	
}
// access to slice
int viSliceAccess(islice *slice, int ith){
	if(slice->linear)
		return slice->start + ith*(slice->step);
	else
		//logarithmic
		return pow(10, slice->start + ith*(slice->step));
}

double vdSliceAccess(dslice *slice, int ith){
	if(slice->linear)
		return slice->start + ith*(slice->step);
	else
		//logarithmic
		return pow(10, slice->start + ith*(slice->step));		
}

/* will hold both value and position of unique elements */
typedef struct {
	double value;
	int pos;
} _sort_d;

/* will hold both value and position of unique elements */
typedef struct {
	int value;
	int pos;
} _sort_i;

static inline int _unique_d_comparator(const _sort_d *p, const _sort_d *q)  
{ 
    double l = p->value; 
    double r = q->value;  
    if( l > r )
	return 1;
    else if( l < r)
	return -1; 
    else return 0; 
} 

DEF_QSORT(_sort_d, _unique_d_comparator)

static inline int _unique_i_comparator(const _sort_i *p, const _sort_i *q)  
{ 
    int l = p->value; 
    int r = q->value;  
    return (l - r); 
} 

DEF_QSORT(_sort_i, _unique_i_comparator)

static inline int _desc_d_comparator(const _sort_d *p, const _sort_d *q)  
{ 
    double l = p->value; 
    double r = q->value;  
    if( l > r )
	return -1;
    else if( l < r)
	return 1; 
    else return 0; 
} 

DEF_QSORT(_sort_d, _desc_d_comparator)

static inline int _desc_i_comparator(const _sort_i *p, const _sort_i *q)  
{ 
    int l = p->value; 
    int r = q->value;  
    if( l > r )
	return -1;
    else if( l < r)
	return 1; 
    else return 0; 
}

DEF_QSORT(_sort_i, _desc_i_comparator)

#define __SortAlgorithm(type, tchar)                                                                                                                 \
void v##tchar##SortAlgorithm(_sort_##tchar input[], const char *direction, int dim, type *result, int *outputs[], int n_extra_outputs){              \
	if( !strncmp(direction, "ascend", 3) )                                                                                                       \
		QSORT(_sort_##tchar, _unique_##tchar##_comparator) (input, dim);                                                                     \
	else                                                                                                                                         \
		QSORT(_sort_##tchar, _desc_##tchar##_comparator) (input, dim);                                                                       \
                                                                                                                                                     \
	for(int i = 0 ; i < dim; ++i)                                                                                                                \
		switch(n_extra_outputs+1){                                                                                                           \
		case 2:                                                                                                                              \
			outputs[0][i] = input[i].pos;                                                                                                \
		case 1:                                                                                                                              \
			result[i] = input[i].value;                                                                                                  \
			break;                                                                                                                       \
		default:                                                                                                                             \
			assert(false && "Unsupported number of output arguments for v?Sort");                                                        \
		}                                                                                                                                    \
}

__SortAlgorithm(double, d)
__SortAlgorithm(int, i)

#define __Sort(type, tchar)                                                                                       \
int v##tchar##Sort(type *input, const char *direction, int ndims, int nargs, ...){                                \
	va_list vl;                                                                                               \
	va_start(vl, nargs);                                                                                      \
        int noutputs = nargs - ndims;                                                                             \
	/* unique results */                                                                                      \
	type *result;                                                                                             \
	/* extra outputs */                                                                                       \
	int *outputs[noutputs-1];                                                                                 \
	int i;                                                                                                    \
	for(i = 0 ; i < noutputs ; ++i )                                                                          \
		if(i == 0)                                                                                        \
			result = va_arg(vl,type *);                                                               \
		else                                                                                              \
			outputs[i-1] = va_arg(vl,int *);                                                          \
	/* total dimensions */                                                                                    \
	int dims[ndims];                                                                                          \
	/* the dimension along which I will sort */                                                               \
	int row_dimension = -1;                                                                                   \
	int sort_dimension = -1;                                                                                  \
	int n = 1;                                                                                                \
	int n_singlethon_dimensions = 0;                                                                          \
	for(i = 0 ; i < ndims ; ++i ){                                                                            \
		dims[i] = va_arg(vl, int);                                                                        \
		n *= dims[i];                                                                                     \
		if(dims[i] != 1 && row_dimension == -1)                                                           \
			row_dimension = i;                                                                        \
		else if(dims[i] != 1 && sort_dimension == -1)                                                     \
			sort_dimension = i;                                                                       \
		else if(dims[i] == 1)                                                                             \
			n_singlethon_dimensions++;                                                                \
	}                                                                                                         \
                                                                                                                  \
	if(row_dimension<0){                                                                                      \
		if( n == 1 )                                                                                      \
			/*single element given*/                                                                  \
			/* fill column in output result */                                                        \
			switch(noutputs){                                                                         \
			case 2:                                                                                   \
				outputs[0][0] = 1;                                                                \
			case 1:                                                                                   \
				result[0] = input[0];                                                             \
			break;                                                                                    \
			default:                                                                                  \
				assert(false && "Unsupported number of output arguments for v?Sort");             \
			}                                                                                         \
		return 0;                                                                                         \
	}	                                                                                                  \
                                                                                                                  \
                                                                                                                  \
	_sort_##tchar elems[dims[row_dimension]];                                                          \
	if(ndims - n_singlethon_dimensions == 1){                                                                 \
		/* copy from input to tmp array elems */                                                          \
		for(int c = 0; c < dims[row_dimension] ; ++c){                                                    \
			elems[c].value = input[c];                                                                \
			elems[c].pos = c+1;                                                                       \
		}                                                                                                 \
		v##tchar##SortAlgorithm(elems, direction, dims[row_dimension], result, outputs, noutputs-1);      \
	}else if(ndims - n_singlethon_dimensions > 1){                                                            \
		/* number of columns */                                                                           \
		int rows = dims[row_dimension];                                                                   \
		int cols = dims[sort_dimension];                                                                  \
		int otherPlaneDim = n / (rows*cols);                                                              \
		int otherDims, rowColDim, r, c;		                                                          \
		/* if a=[n x m x z] -> flattenIdx = k*n*m + r*m + c */                                            \
		/* -> to extract r for each of the other dimensions we can do */                                  \
		/* otherDims = flattenIdx % n*m */                                                                \
		/* flattenIdx - otherDims        -> r*m + c */                                                    \
		/* (flattenIdx - otherDims) / m  -> r */                                                          \
		/* (flattenIdx - otherDims) % m  -> c */                                                          \
                                                                                                                  \
		/* for on every dimnension except rows/cols */                                                    \
		for(int otherDim = 0 ; otherDim < otherPlaneDim ; ++otherDim )                                    \
			/* for every column */                                                                    \
			for(int c = 0 ; c < cols ; ++c){                                                          \
				/* for every row within column */                                                 \
				for(int r = 0 ; r < rows; ++r){                                                   \
					elems[r].value = input[otherDim*otherPlaneDim*cols + r*cols + c];         \
					elems[r].pos = r+1;                                                       \
				}                                                                                 \
				/* sort this column	 */		                                          \
				if( !strncmp(direction, "ascend", 3) )                                            \
					QSORT(_sort_##tchar, _unique_##tchar##_comparator) (elems, cols);         \
				else                                                                              \
					QSORT(_sort_##tchar, _desc_##tchar##_comparator) (elems, cols);           \
                                                                                                                  \
				for(int or = 0 ; or < rows ; ++or)                                                \
					/* fill column in output result */                                        \
					switch(noutputs){                                                         \
					case 2:                                                                   \
						outputs[0][otherDim*otherPlaneDim*cols + or*cols + c] = elems[or].pos;  \
					case 1:                                                                   \
						result[otherDim*otherPlaneDim*cols + or*cols + c] = elems[or].value;     \
					break;                                                                    \
					default:                                                                  \
						assert(false && "Unsupported number of output arguments for v?Sort"); \
					}                                                                         \
			}                                                                                         \
	}else                                                                                                     \
		assert(false && "Inconsistent dimension in v?Sort");                                              \
	                                                                                                          \
	return 0;                                                                                                 \
}

/* generate sort function for double and int types */
__Sort(double, d)
__Sort(int, i)

/* TODO this is not efficient, change algorithm to first sort, then look for duplicates */
int vdUnique(double *input, int ndims, int nargs, ...){
	va_list vl;
	va_start(vl, nargs);
	int noutputs = nargs - ndims;

	// unique results
	double *result;
	// extra outputs
	int *outputs[noutputs-1];
	int i;
	for(i = 0 ; i < noutputs ; ++i )
		if(i == 0)
			result = va_arg(vl,double *);
		else
			outputs[i-1] = va_arg(vl,int *);
	// total dimension
	int n = 1;
	for(i = 0 ; i < ndims ; ++i )
		n *= va_arg(vl, int);

	_sort_d uelems[n];
	int relems = 0;
	char dup;
	for(i = 0 ; i < n ; ++i){
		dup = 0;
		for(int j = 0 ; j < relems ; ++j)
			if(i != j && fabs(input[i] - uelems[j].value) < DOUBLE_EQUALITY_TOL ){
				dup = 1;
				break;
			}
		if(!dup){
			uelems[relems].value = input[i];
			uelems[relems].pos = i+1;
			relems++;
		}
	}

	// sort unique elements
	vdSortAlgorithm(uelems, "ascend", relems, result, outputs, noutputs-1);
	return relems;	
}

int viUnique(int *input, int ndims, int nargs, ...){
	va_list vl;
	va_start(vl, nargs);
	int noutputs = nargs - ndims;

	// unique results
	int *result;
	// extra outputs
	int *outputs[noutputs-1];
	int i;
	for(i = 0 ; i < noutputs ; ++i )
		if(i == 0)
			result = va_arg(vl,int *);
		else
			outputs[i-1] = va_arg(vl,int *);

	// total dimension
	int n = 1;
	for(i = 0 ; i < ndims ; ++i )
		n *= va_arg(vl, int);

	_sort_i uelems[n];
	int relems = 0;
	char dup;
	for(i = 0 ; i < n ; ++i){
		dup = 0;
		for(int j = 0 ; j < relems ; ++j)
			if(i != j && input[i] == uelems[j].value ){
				dup = 1;
				break;
			}
		if(!dup){
			uelems[relems].value = input[i];
			uelems[relems].pos = i+1;
			relems++;
		}
	}
	// sort unique elements
	viSortAlgorithm(uelems, "ascend", relems, result, outputs, noutputs-1);

	return relems;	
}

int vdUnion(double *s1, void *s2, const char *s2type, int s2dim, int s1dims, int nargs, ...){
	va_list vl;
	va_start(vl, nargs);
	int noutputs = nargs - s1dims;

	// unique results
	double *result;
	// extra outputs
	int *outputs[noutputs-1];
	int i;
	for(i = 0 ; i < noutputs ; ++i )
		if(i == 0)
			result = va_arg(vl,double *);
		else
			outputs[i-1] = va_arg(vl,int *);

	// total dimension
	int s1dim = 1;
	for(i = 0 ; i < s1dims ; ++i )
		s1dim *= va_arg(vl, int);

	/* result */
	_sort_d s1s2Union[s1dim+s2dim];
	/* elements with pos < s1dim belongs to set A */
	for( i = 0 ; i < s1dim ; ++i){
		s1s2Union[i].value = s1[i];
		s1s2Union[i].pos = i+1;
	}
	/* elements with pos > s1dim belongs to set B */
	for(  ; i < s1dim+s2dim ; ++i){
		if(s2type[0] == 'i')
			s1s2Union[i].value = (double) *(((int*) s2)+i-s1dim);
		else if(s2type[0] == 'd')
			s1s2Union[i].value = *(((double*) s2)+i-s1dim);
		else
			assert(false && "Unsupported setdiff parameter");
		s1s2Union[i].pos = i+1;
	}

	/* sort two arrays, so that difference is quick */
	QSORT(_sort_d, _unique_d_comparator) (s1s2Union, (s1dim+s2dim));
	
	int pos;
	double value;
	int iB = 0;
	int iA = 0;
	int iR = 0;
	for( i = 0 ; i < s1dim+s2dim ; ++i){
		if(i==0){
			/* add everything */
			value = s1s2Union[i].value;
			pos = s1s2Union[i].pos;
		}else if( fabs( s1s2Union[i-1].value - s1s2Union[i].value ) < DOUBLE_EQUALITY_TOL ){
			/* if this one is equal to previous one skip */
			value = -1.0;
			pos = -1;
		}else{
			/* if different from previous one add it */
			value = s1s2Union[i].value;
			pos = s1s2Union[i].pos;
		}

		if( pos > 0 )
			switch(noutputs){                                                                                                           
			case 3:
				if(pos > s1dim)                                                                                                                             
					outputs[1][iB++] = pos;                                                                                                
			case 2:                                                                                                                              
				if(pos <= s1dim)                                                                                                                             
					outputs[0][iA++] = pos;                                                                                                
			case 1:                                                                                                                              
				result[iR++] = value;                                                                                                  
				break;                                                                                                                       
			default:                                                                                                                             
				assert(false && "Unsupported number of output arguments for v?Union");                                                        
			}                                                                                                                                    

	}

	return iR;	
}

int viUnion(int *s1, void *s2, const char *s2type, int s2dim, int s1dims, int nargs, ...){
	va_list vl;
	va_start(vl, nargs);
	int noutputs = nargs - s1dims;

	// unique results
	int *result;
	// extra outputs
	int *outputs[noutputs-1];
	int i;
	for(i = 0 ; i < noutputs ; ++i )
		if(i == 0)
			result = va_arg(vl,int *);
		else
			outputs[i-1] = va_arg(vl,int *);

	// total dimension
	int s1dim = 1;
	for(i = 0 ; i < s1dims ; ++i )
		s1dim *= va_arg(vl, int);

	/* result */
	_sort_i s1s2Union[s1dim+s2dim];
	/* elements with pos < s1dim belongs to set A */
	for( i = 0 ; i < s1dim ; ++i){
		s1s2Union[i].value = s1[i];
		s1s2Union[i].pos = i+1;
	}
	/* elements with pos > s1dim belongs to set B */
	for(  ; i < s1dim+s2dim ; ++i){
		if(s2type[0] == 'i')
			s1s2Union[i].value = *(((int*) s2)+i-s1dim);
		else if(s2type[0] == 'd')
			s1s2Union[i].value = (int) *(((double*) s2)+i-s1dim);
		else
			assert(false && "Unsupported setdiff parameter");
		s1s2Union[i].pos = i+1;
	}

	/* sort two arrays, so that difference is quick */
	QSORT(_sort_i, _unique_i_comparator) (s1s2Union, (s1dim+s2dim));
	
	int pos;
	int value;
	int iB = 0;
	int iA = 0;
	int iR = 0;
	for( i = 0 ; i < s1dim+s2dim ; ++i){
		if(i==0){
			/* add everything */
			value = s1s2Union[i].value;
			pos = s1s2Union[i].pos;
		}else if( s1s2Union[i-1].value == s1s2Union[i].value ){
			/* if this one is equal to previous one skip */
			value = -1;
			pos = -1;
		}else{
			/* if different from previous one add it */
			value = s1s2Union[i].value;
			pos = s1s2Union[i].pos;
		}

		if( pos > 0 )
			switch(noutputs){                                                                                                           
			case 3:
				if(pos > s1dim)                                                                                                                             
					outputs[1][iB++] = pos;                                                                                                
			case 2:                                                                                                                              
				if(pos <= s1dim)                                                                                                                             
					outputs[0][iA++] = pos;                                                                                                
			case 1:                                                                                                                              
				result[iR++] = value;                                                                                                  
				break;                                                                                                                       
			default:                                                                                                                             
				assert(false && "Unsupported number of output arguments for v?Union");                                                        
			}                                                                                                                                    

	}

	return iR;	
}

int vvdSetdiff(double *s1, void *s2, const char *s2type, int s2dim, int s1dims, int nargs, ...){
	va_list vl;
	va_start(vl, nargs);
	int noutputs = nargs - s1dims;

	// unique results
	double *result;
	// extra outputs
	int *outputs[noutputs-1];
	int i;
	for(i = 0 ; i < noutputs ; ++i )
		if(i == 0)
			result = va_arg(vl,double *);
		else
			outputs[i-1] = va_arg(vl,int *);

	// total dimension
	int s1dim = 1;
	for(i = 0 ; i < s1dims ; ++i )
		s1dim *= va_arg(vl, int);


	_sort_d s1str[s1dim];
	_sort_d s2str[s2dim];
	// result
	_sort_d s1strDiff[s1dim];

	for( i = 0 ; i < s1dim ; ++i){
		s1str[i].value = s1[i];
		s1str[i].pos = i+1;
	}

	for( i = 0 ; i < s2dim ; ++i){
		if(s2type[0] == 'i')
			s2str[i].value = (double) *(((int*) s2)+i);
		else if(s2type[0] == 'd')
			s2str[i].value = *(((double*) s2)+i);
		else
			assert(false && "Unsupported setdiff parameter");
		s2str[i].pos = i+1;
	}

	/* sort two arrays, so that difference is quick */
	QSORT(_sort_d, _unique_d_comparator) (s1str, s1dim);
	/* sort two arrays, so that difference is quick */
	QSORT(_sort_d, _unique_d_comparator) (s2str, s2dim);

	int rs1dims = 0;
	int s1i = 0;
	for(int s2i = 0 ; s2i < s2dim && s1i < s1dim ; ){
		if( fabs( s1str[s1i].value - s2str[s2i].value ) < DOUBLE_EQUALITY_TOL ){
			/* = : s1i found in s2, so don't include */
			s1i++;
			/* exclude also eventual duplicates */
			while(s1i < s1dim && fabs( s1str[s1i].value - s2str[s2i].value ) < DOUBLE_EQUALITY_TOL)
				s1i++;	

			++s2i;			
		}else if( s1str[s1i].value < s2str[s2i].value ){
			/* < : I won't find s1i in s2 (because it's ordered), add it to results */
			s1strDiff[rs1dims].value = s1str[s1i].value;
			s1strDiff[rs1dims].pos = s1str[s1i].pos;
			rs1dims++;
			// consider next one
			s1i++;
		}else
			++s2i;
	}

	/* all the remaining elements should be inserted */
	/* we finished diff array but we still have elements in source array */
	for( ; s1i < s1dim ; ++s1i){
		s1strDiff[rs1dims].value = s1str[s1i].value;
		s1strDiff[rs1dims].pos = s1str[s1i].pos;
		rs1dims++;
	}

	for( i = 0 ; i < rs1dims; ++i)                                                                                                                
		switch(noutputs){                                                                                                           
		case 2:                                                                                                                              
			outputs[0][i] = s1strDiff[i].pos;                                                                                                
		case 1:                                                                                                                              
			result[i] = s1strDiff[i].value;                                                                                                  
			break;                                                                                                                       
		default:                                                                                                                             
			assert(false && "Unsupported number of output arguments for v?Setdiff");                                                        
		}                                                                                                                            

	return rs1dims;	
}

int vviSetdiff(int *s1, void *s2, const char *s2type, int s2dim, int s1dims, int nargs, ...){
	va_list vl;
	va_start(vl, nargs);
	int noutputs = nargs - s1dims;

	// unique results
	int *result;
	// extra outputs
	int *outputs[noutputs-1];
	int i;
	for(i = 0 ; i < noutputs ; ++i )
		if(i == 0)
			result = va_arg(vl,int *);
		else
			outputs[i-1] = va_arg(vl,int *);

	// total dimension
	int s1dim = 1;
	for(i = 0 ; i < s1dims ; ++i )
		s1dim *= va_arg(vl, int);


	_sort_i s1str[s1dim];
	_sort_i s2str[s2dim];
	// result
	_sort_i s1strDiff[s1dim];

	for( i = 0 ; i < s1dim ; ++i){
		s1str[i].value = s1[i];
		s1str[i].pos = i+1;
	}

	for( i = 0 ; i < s2dim ; ++i){
		if(s2type[0] == 'i')
			s2str[i].value = *(((int*) s2)+i);
		else if(s2type[0] == 'd')
			s2str[i].value = (int) *(((double*) s2)+i);
		else
			assert(false && "Unsupported setdiff parameter");
		s2str[i].pos = i+1;
	}

	/* sort two arrays, so that difference is quick */
	QSORT(_sort_i, _unique_i_comparator) (s1str, s1dim);
	/* sort two arrays, so that difference is quick */
	QSORT(_sort_i, _unique_i_comparator) (s2str, s2dim);

	int rs1dims = 0;
	int s1i = 0;
	for(int s2i = 0 ; s2i < s2dim && s1i < s1dim ; ){
		if( s1str[s1i].value == s2str[s2i].value ){
			/* = : s1i found in s2, so don't include */
			s1i++;
			/* exclude also eventual duplicates */
			while(s1i < s1dim && s1str[s1i].value == s2str[s2i].value)
				s1i++;				

			++s2i;
		}else if( s1str[s1i].value < s2str[s2i].value ){
			/* < : I won't find s1i in s2 (because it's ordered), add it to results */
			s1strDiff[rs1dims].value = s1str[s1i].value;
			s1strDiff[rs1dims].pos = s1str[s1i].pos;
			rs1dims++;
			// consider next one
			s1i++;
		}else
			++s2i;
	}

	/* all the remaining elements should be inserted */
	/* we finished diff array but we still have elements in source array */
	for( ; s1i < s1dim ; ++s1i){
		s1strDiff[rs1dims].value = s1str[s1i].value;
		s1strDiff[rs1dims].pos = s1str[s1i].pos;
		rs1dims++;
	}

	for( i = 0 ; i < rs1dims; ++i)                                                                                                                
		switch(noutputs){                                                                                                           
		case 2:                                                                                                                              
			outputs[0][i] = s1strDiff[i].pos;                                                                                                
		case 1:                                                                                                                              
			result[i] = s1strDiff[i].value;                                                                                                  
			break;                                                                                                                       
		default:                                                                                                                             
			assert(false && "Unsupported number of output arguments for v?Setdiff");                                                        
		}                                                                                                                                    


	return rs1dims;	
}

int sviSetdiff(islice s1, void *s2, const char *s2type, int s2dim, int s1dims, int nargs, ...){
	va_list vl;
	va_start(vl, nargs);
	int noutputs = nargs - s1dims;

	// unique results
	int *result;
	// extra outputs
	int *outputs[noutputs-1];
	int i;
	for(i = 0 ; i < noutputs ; ++i )
		if(i == 0)
			result = va_arg(vl,int *);
		else
			outputs[i-1] = va_arg(vl,int *);

	// total dimension
	int s1dim = s1.dim1;
	_sort_i s2str[s2dim];
	// result
	_sort_i s1strDiff[s1dim];

	for( i = 0 ; i < s2dim ; ++i){
		if(s2type[0] == 'i')
			s2str[i].value = *(((int*) s2)+i);
		else if(s2type[0] == 'd')
			s2str[i].value = (int) *(((double*) s2)+i);
		else
			assert(false && "Unsupported setdiff parameter");
		s2str[i].pos = i+1;
	}

	/* sort two arrays, so that difference is quick */
	/* slices are already sorted by definition */
	/* sort two arrays, so that difference is quick */
	QSORT(_sort_i, _unique_i_comparator) (s2str, s2dim);

	int rs1dims = 0;
	int s1i = 0;
	if( s1.step < 0 )
		// decreasing slice, start from end
		s1i = s1.dim1-1;
	for(int s2i = 0 ; s2i < s2dim && s1i < s1dim ; ){
		if( viSliceAccess(&s1, s1i) == s2str[s2i].value ){
			/* = : s1i found in s2, so don't include */
			if(s1.step < 0)
				s1i--;
			else
				s1i++;

			++s2i;
		}else if( viSliceAccess(&s1, s1i) < s2str[s2i].value ){
			/* < : I won't find s1i in s2 (because it's ordered), add it to results */
			s1strDiff[rs1dims].value = viSliceAccess(&s1, s1i);
			s1strDiff[rs1dims].pos = s1i+1;
			rs1dims++;
			// consider next one
			if(s1.step < 0)
				s1i--;
			else
				s1i++;
		}else
			++s2i;

	}

	/* all the remaining elements should be inserted */
	/* we finished diff array but we still have elements in source array */
	while( (( s1.step > 0 ) && (s1i < s1dim)) || (( s1.step < 0 ) && (s1i > 0)) ){
		s1strDiff[rs1dims].value = viSliceAccess(&s1, s1i);
		s1strDiff[rs1dims].pos = s1i+1;
		rs1dims++;

		if(s1.step > 0)
			++s1i;
		else
			--s1i;
	}

	for( i = 0 ; i < rs1dims; ++i)                                                                                                                
		switch(noutputs){                                                                                                           
		case 2:                                                                                                                              
			outputs[0][i] = s1strDiff[i].pos;                                                                                                
		case 1:                                                                                                                              
			result[i] = s1strDiff[i].value;                                                                                                  
			break;                                                                                                                       
		default:                                                                                                                             
			assert(false && "Unsupported number of output arguments for v?Setdiff");                                                        
		}                                                                                                                                    


	return rs1dims;	
}

int svdSetdiff(dslice s1, void *s2, const char *s2type, int s2dim, int s1dims, int nargs, ...){
	va_list vl;
	va_start(vl, nargs);
	int noutputs = nargs - s1dims;

	// unique results
	double *result;
	// extra outputs
	int *outputs[noutputs-1];
	int i;
	for(i = 0 ; i < noutputs ; ++i )
		if(i == 0)
			result = va_arg(vl,double *);
		else
			outputs[i-1] = va_arg(vl,int *);

	// total dimension
	int s1dim = s1.dim1;
	_sort_d s2str[s2dim];
	// result
	_sort_d s1strDiff[s1dim];

	for( i = 0 ; i < s2dim ; ++i){
		if(s2type[0] == 'i')
			s2str[i].value = (double) *(((int*) s2)+i);
		else if(s2type[0] == 'd')
			s2str[i].value = *(((double*) s2)+i);
		else
			assert(false && "Unsupported setdiff parameter");
		s2str[i].pos = i+1;
	}

	/* sort two arrays, so that difference is quick */
	/* slices are already sorted by definition */
	/* sort two arrays, so that difference is quick */
	QSORT(_sort_d, _unique_d_comparator) (s2str, s2dim);

	int rs1dims = 0;
	int s1i = 0;
	if( s1.step < 0 )
		// decreasing slice, start from end
		s1i = s1.dim1-1;
	for(int s2i = 0 ; s2i < s2dim && s1i < s1dim ; ){
		if( fabs( vdSliceAccess(&s1, s1i) - s2str[s2i].value ) < DOUBLE_EQUALITY_TOL ){
			/* = : s1i found in s2, so don't include */
			if(s1.step < 0)
				s1i--;
			else
				s1i++;

			++s2i;
		}else if( vdSliceAccess(&s1, s1i) < s2str[s2i].value ){
			/* < : I won't find s1i in s2 (because it's ordered), add it to results */
			s1strDiff[rs1dims].value = vdSliceAccess(&s1, s1i);
			s1strDiff[rs1dims].pos = s1i+1;
			rs1dims++;
			// consider next one
			if(s1.step < 0)
				s1i--;
			else
				s1i++;
		}else
			++s2i;

	}

	/* all the remaining elements should be inserted */
	/* we finished diff array but we still have elements in source array */
	while( (( s1.step > 0 ) && (s1i < s1dim)) || (( s1.step < 0 ) && (s1i > 0)) ){
		s1strDiff[rs1dims].value = vdSliceAccess(&s1, s1i);
		s1strDiff[rs1dims].pos = s1i+1;
		rs1dims++;

		if(s1.step > 0)
			++s1i;
		else
			--s1i;
	}

	for( i = 0 ; i < rs1dims; ++i)                                                                                                                
		switch(noutputs){                                                                                                           
		case 2:                                                                                                                              
			outputs[0][i] = s1strDiff[i].pos;                                                                                                
		case 1:                                                                                                                              
			result[i] = s1strDiff[i].value;                                                                                                  
			break;                                                                                                                       
		default:                                                                                                                             
			assert(false && "Unsupported number of output arguments for v?Setdiff");                                                        
		}                                                                                                                                    


	return rs1dims;	
}

// mat_dim: the matrix to be accessed dimensions
// poly_basis: the polinomial base of the matrix. computing the flat index is equivalent to the scalar product between the access numbers and the polinomial base of the matrix, 
//             that is dim2, 1, dim1*dim2 ... in rowmajor
//                     1, dim1, dim1*dim2 ... in colmajor
// mat_dimensions: the number of dimensions of the matrix to be accessed
// access_numbers: the matrix access parameters ex. a(1,2,3) -> access_numbers = 1,2,3
// n_access_numbers: the length of access_numbers vector
int flattenMatrixAccessAlgorithm(int *mat_dim, int *poly_basis, int mat_dimensions, int *access_numbers, int n_access_numbers){
	//algorithm implementation
	int n_direct_access = n_access_numbers;
	bool last_flat = false;

	if(mat_dimensions == 1)
		/* trivial 1D case */
		return access_numbers[0]-1;
	if(n_access_numbers < mat_dimensions){
		// last one interpreted as flat matrix access
		n_direct_access = n_access_numbers-1;
		last_flat = true;
	}/*else*/
		/* access numbers above mat_dimensions will be ignored */
		/*assert(false && "More indexes than matrix dimensions");*/

	int flat_access_number = 0;
	int access_number;
	int n;
	for (n = 0; n < n_direct_access; ++n) {
		access_number = access_numbers[n];
		//here access_number should be lower than the selected dimension
		//if all dimensions are lower than this access number this is an invalid access number
		assert(access_number <= mat_dim[n] && "Index exceeds array bounds");
		flat_access_number += poly_basis[n] * (access_number - 1);
	}
	
	// just one non-one access number, this is a flat access index
	if(last_flat){
		
		// flat index! the behaviour depends on the orientation of the matrix,
		// row major or column major? 
		// colmajor is the most efficient here because we need to emulate
		// MATLAB behaviour that stores matrices in column major
		int implicitMultiIndex = access_numbers[n_direct_access] - 1;
		#ifdef ROWMAJOR
		if( n_direct_access == 0 ){
			// -- PURE FLAT INDEX -- A(10) ON A 4X4 MATRIX
			// convert column-major flat index to row-major flat index
		 	//the column order of the columns have to be respected
		 	//previously the matrix was saved				 		
			int residual = implicitMultiIndex % (mat_dim[0]*mat_dim[1]);
	 	
	 		int i = residual % mat_dim[0];
	 		int j =  residual / mat_dim[0];
		
	 		//computes the new index that inverts rows and columns 
	 		//(it is the oldindex + (i*numCol+j) - (j*numRow+i)
	 		return implicitMultiIndex + i*(mat_dim[1]-1)+j*(1-mat_dim[0]);
		}else if(n_direct_access == 1){
			// combination of the form i*ncol + j + z*numcol + t*numcol*numplanes + ...
			// where i is known.
			// remove j, then multiply by nrow the result and sum to combine to the 
			// explicit multi-index
			int j = implicitMultiIndex % mat_dim[1];
			// remove columns, since we are in rowmajor
			int realMultiIndex = implicitMultiIndex - j;
			return flat_access_number + j + mat_dim[0] * realMultiIndex;
		}else{
			// equivalent to colmajor
			//any other should be multiplied by row*col*..
			return flat_access_number + poly_basis[n] * implicitMultiIndex;
		}
		#else
		return flat_access_number + poly_basis[n] * implicitMultiIndex;
		#endif
	}

	return flat_access_number;
}

// returns the C index of the C array
// for a generic N-D matrix access that starts from 1
// mat_dimensions: the number of dimension of the matrix being accessed
// n_access_numbers: the number of indexes
// ... will contain: mat_dimensions integers, to specify each of the mat_dimensions
//                     n_access_numbers integers, to specyfy each access number
int matrixAccess(int *poly_basis, int mat_dimensions, int n_access_numbers, ...){
	va_list vl;
	va_start(vl, n_access_numbers);

	int mat_dim[mat_dimensions];
	for(int i = 0 ; i < mat_dimensions ; ++i ){
		mat_dim[i] = va_arg(vl,int);
	}

	int access_numbers[n_access_numbers];
	for(int i = 0 ; i < n_access_numbers ; ++i ){
		access_numbers[i] = va_arg(vl,int);
	}
	return flattenMatrixAccessAlgorithm(mat_dim, poly_basis, mat_dimensions, access_numbers, n_access_numbers);
}

int _tripletsIdx(int n, char *slice_type){
	int r=0;
	for(int i=0; i<n; ++i)
		if( !slice_type[i] )
			r++;
	return r;
}

int _matrixIdx(int n, char *slice_type){
	int r=0;
	for(int i=0; i<n; ++i)
		if( slice_type[i] )
			r++;
	return r;
}

// helper function returns the correct flat index for the current row ordering imposed at compilation time
static inline int c2r(int flatIdx, int dim1, int dim2){
#ifdef ROWMAJOR
	return colMajor2RowMajor(flatIdx, dim1, dim2);
#else
	return flatIdx;
#endif
}

static inline int r2i(int flatIdx, int dim1, int dim2){
#ifdef ROWMAJOR
	// the flattened matrix access is i*numCol+j 
 	return flatIdx / dim2;
#else
	// the flattened matrix access is i+j*numRow 
	return flatIdx % dim1;
#endif
}

static inline int r2j(int flatIdx, int dim1, int dim2){
#ifdef ROWMAJOR
	// the flattened matrix access is i*numCol+j 
 	return flatIdx % dim2;
#else
	// the flattened matrix access is i+j*numRow 
 	return flatIdx / dim1;
#endif
}

/**
 * function to apply a generic function on every element of a N-dimensional matrix
 */
void mapply(void *m, size_t size, void (*print)(char *, int *, int, void *), int *dim, int *poly_basis, int dimensions, void *extra){
	assert(dimensions > 0);

	int i,j,flat_idx;

	if(dimensions == 1){
		int access_numbers[dimensions];

		/* total number of elements element */
		for(flat_idx=0; flat_idx < dim[0] ; ++flat_idx){
			/* access number is flat_idx + 1 */
			access_numbers[0] = flat_idx + 1;
			print(((char *)m) + flat_idx * size, access_numbers, dimensions, extra );
		}
	}else{
		int access_numbers[dimensions];
		int curPlane;
		int total_dims = 1;
		for(int n=0; n < dimensions; ++n)
			total_dims *= dim[n];

		/* total number of elements element */
		for(flat_idx=0; flat_idx < total_dims ; ++flat_idx){
			/* generate access numbers, poly_basis[0]=1 for colmajor, dim2 for rowmajor */
			curPlane = 1;
			for(int d=0 ; d < dimensions ; ++d){
				access_numbers[d] = flat_idx / curPlane;
				curPlane *= dim[d];
				access_numbers[d] = (access_numbers[d] % dim[d]) + 1;
			}
			print(((char *)m) + c2r(flat_idx, dim[0], dim[1]) * size, access_numbers, dimensions, extra );
		}
	}
}

/* context used to allow recursive call of SliceMatrixAlgorithm from within an mapply call */
typedef struct {
	int *mat_access_idxs;
	int mat_access_idxs_len;
	int *slice_access_idxs;
	int slice_access_idxs_len;
	int n;
	int *out_matrix_idx;
	// used only in case of boolean slice
	// because the output can be smaller than input slicing matrix
	int *update_matrix_dims;
	int n_update_matrix_dims;
	int *out_matrix_poly_basis;
	int *out_matrix_dims;
	//case int
	int **out_matrix;
	QSPCC_INT **outRowIndex;
	QSPCC_INT **outColumns; 
	QSPCC_INT *outUsedCells;
	int *outRealsize;
	int out_matrix_len;
	// case int
	int *in_matrix;
	QSPCC_INT *rowIndex;
	QSPCC_INT *columns;
	int *in_matrix_dims;
	int *in_matrix_poly_basis;
	int in_matrix_len;
	int (*slice_triplets)[3];
	int n_slice_triplets;
	char *slice_type;
	int n_params;
	int **mat_slices;
	int **mat_slices_dims;
	int **mat_slices_poly_basis;
	int *mat_slices_len;
	int *original_mat_slices_len;
	int n_mat_slices;
	// cur mat dimensions (needed for setting in matrix access_indexes)
	int mat_slices_idx;
	// output position matrix
	int *out_mat_access_idxs;
	char single_matrix_access;
	char last_type;
} intint_slice_ctx;

/* context used to allow recursive call of SliceMatrixAlgorithm from within an mapply call */
typedef struct {
	int *mat_access_idxs;
	int mat_access_idxs_len;
	int *slice_access_idxs;
	int slice_access_idxs_len;
	int n;
	int *out_matrix_idx;
	// used only in case of boolean slice
	// because the output can be smaller than input slicing matrix
	int *update_matrix_dims;
	int n_update_matrix_dims;
	int *out_matrix_poly_basis;
	int *out_matrix_dims;
	//case double
	double **out_matrix;
	QSPCC_INT **outRowIndex;
	QSPCC_INT **outColumns; 
	QSPCC_INT *outUsedCells;
	int *outRealsize;
	int out_matrix_len;
	// case int
	int *in_matrix;
	QSPCC_INT *rowIndex;
	QSPCC_INT *columns;
	int *in_matrix_dims;
	int *in_matrix_poly_basis;
	int in_matrix_len;
	int (*slice_triplets)[3];
	int n_slice_triplets;
	char *slice_type;
	int n_params;
	int **mat_slices;
	int **mat_slices_dims;
	int **mat_slices_poly_basis;
	int *mat_slices_len;
	int *original_mat_slices_len;
	int n_mat_slices;
	// cur mat dimensions (needed for setting in matrix access_indexes)
	int mat_slices_idx;
	// output position matrix
	int *out_mat_access_idxs;
	char single_matrix_access;
	char last_type;
} doubleint_slice_ctx;

typedef struct {
	// fields needed for recursive call
	int *mat_access_idxs;
	int mat_access_idxs_len;
	int *slice_access_idxs;
	int slice_access_idxs_len;
	int n;
	int *out_matrix_idx;
	// used only in case of boolean slice
	// because the output can be smaller than input slicing matrix
	int *update_matrix_dims;
	int n_update_matrix_dims;
	int *out_matrix_poly_basis;
	int *out_matrix_dims;
	//case double
	double **out_matrix;
	QSPCC_INT **outRowIndex;
	QSPCC_INT **outColumns; 
	QSPCC_INT *outUsedCells;
	int *outRealsize;
	int out_matrix_len;
	// case double
	double *in_matrix;
	QSPCC_INT *rowIndex;
	QSPCC_INT *columns;
	int *in_matrix_dims;
	int *in_matrix_poly_basis;
	int in_matrix_len;
	int (*slice_triplets)[3];
	int n_slice_triplets;
	char *slice_type;
	int n_params;
	int **mat_slices;
	int **mat_slices_dims;
	int **mat_slices_poly_basis;
	int *mat_slices_len;
	int *original_mat_slices_len;
	int n_mat_slices;
	// cur mat dimensions (needed for setting in matrix access_indexes)
	int mat_slices_idx;
	// output position matrix
	int *out_mat_access_idxs;
	char single_matrix_access;
	char last_type;
} doubledouble_slice_ctx;

typedef struct {
	// fields needed for recursive call
	int *mat_access_idxs;
	int mat_access_idxs_len;
	int *slice_access_idxs;
	int slice_access_idxs_len;
	int n;
	int *out_matrix_idx;
	// used only in case of boolean slice
	// because the output can be smaller than input slicing matrix
	int *update_matrix_dims;
	int n_update_matrix_dims;
	int *out_matrix_poly_basis;
	int *out_matrix_dims;
	//case int
	int **out_matrix;
	QSPCC_INT **outRowIndex;
	QSPCC_INT **outColumns; 
	QSPCC_INT *outUsedCells;
	int *outRealsize;
	int out_matrix_len;
	// case double
	double *in_matrix;
	QSPCC_INT *rowIndex;
	QSPCC_INT *columns;
	int *in_matrix_dims;
	int *in_matrix_poly_basis;
	int in_matrix_len;
	int (*slice_triplets)[3];
	int n_slice_triplets;
	char *slice_type;
	int n_params;
	int **mat_slices;
	int **mat_slices_dims;
	int **mat_slices_poly_basis;
	int *mat_slices_len;
	int *original_mat_slices_len;
	int n_mat_slices;
	// cur mat dimensions (needed for setting in matrix access_indexes)
	int mat_slices_idx;
	// output position matrix
	int *out_mat_access_idxs;
	char single_matrix_access;
	char last_type;
} intdouble_slice_ctx;

/**
 * ith_dim: the current dimension of the n-dimensional index matrix we are iterating on, starting from 1. 
 */
#define __SliceMatrixCallback(type_in,type_out) \
void type_out##type_in##SliceMatrixCallback(int *val, int *access_numbers, int dimensions, void *extra){                                                                      \
				type_out##type_in##_slice_ctx *ctx = (type_out##type_in##_slice_ctx *) extra;                                                                 \
				/* val is the flattened index in the input matrix that we want to access, indexed by column */                                                \
				/* compute all access indexes that will raise the flatten index val */                                                                        \
				/* access numbers corresponds to the position of the value pointed by the flat index val in the output matrix */			      \
																					      \
				if(ctx->single_matrix_access)														      \
					for(int d = 0; d < dimensions; ++d)                                                                                                   \
						ctx->slice_access_idxs[ctx->n + d] = access_numbers[d];                                                                       \
				else																	      \
					/* count as 1 dimension regardless of the real dimension of this matrix. */							      \
					ctx->slice_access_idxs[ctx->n] = (ctx->slice_access_idxs[ctx->n] % ctx->out_matrix_dims[ctx->n]) + 1;				      \
																					      \
				/* if boolean matrix original dimension is the number of dimensions of the original boolean matrix while this is a 1D matrix */               \
				ctx->mat_access_idxs[ctx->n] = *val;                                                                          				      \
																					      \
				int mat_slice_len;															      \
				/* if last type is matrix and this is last matrix */											      \
				if(ctx->single_matrix_access || (ctx->last_type && ctx->n + ctx->original_mat_slices_len[ctx->mat_slices_idx] >= ctx->mat_access_idxs_len))   \
					mat_slice_len = ctx->mat_access_idxs_len;											      \
				else																	      \
					mat_slice_len = ctx->n + 1;													      \
																					      \
				if( ctx->n + 1 < ctx->mat_access_idxs_len )												      \
					ctx->mat_access_idxs[ctx->n + 1] = MATRIX_ACCESS_CONVENTIONAL_VALUE;                                                  		      \
				                                                                                                                                              \
				/* recursive call to add mat_access_idxs for the next dimension */                                                                            \
				type_out##type_in##SliceMatrixAlgorithm(ctx->mat_access_idxs, ctx->mat_access_idxs_len, ctx->slice_access_idxs, ctx->slice_access_idxs_len,   \
							mat_slice_len,                                                                                                 	       \
							ctx->out_matrix_idx, ctx->update_matrix_dims, ctx->out_matrix_poly_basis, ctx->out_matrix_dims,                         \
							ctx->out_matrix, ctx->outRowIndex, ctx->outColumns, ctx->outUsedCells, ctx->outRealsize, ctx->out_matrix_len,            \
							ctx->in_matrix, ctx->rowIndex, ctx->columns, ctx->in_matrix_dims, ctx->in_matrix_poly_basis,                              \
							ctx->in_matrix_len, ctx->slice_triplets,                                                                                   \
                                                        ctx->n_slice_triplets, ctx->slice_type, ctx->n_params, ctx->mat_slices, ctx->mat_slices_dims, ctx->mat_slices_poly_basis,   \
							ctx->mat_slices_len, ctx->original_mat_slices_len, ctx->n_mat_slices, ctx->single_matrix_access, ctx->last_type);           \
}

__SliceMatrixCallback(int,int)
__SliceMatrixCallback(double,int)
__SliceMatrixCallback(double,double)
__SliceMatrixCallback(int,double)


#define __ensurePlainSparseMatrixCapacity(type) \
static void type##EnsurePlainSparseMatrixCapacity(type **out_matrix, QSPCC_INT **outRowIndex, QSPCC_INT **outColumns, QSPCC_INT *outUsedCells, int *outRealsize, int *out_matrix_idx){ \
	if( *out_matrix_idx >= *outRealsize ){                                                                                                                                           \
		/* realloc to make sure it fits */                                                                                                                                       \
		/* make this here and not outside because outside I can't know how many values will be fitted in */                                                                      \
		*outRealsize = (*outRealsize) * 2;                                                                                                                                       \
		*out_matrix= (type  *)  qspcc_realloc(*out_matrix, (*outRealsize) * sizeof(type));                                                                                       \
		*outColumns= (QSPCC_INT  *)  qspcc_realloc(*outColumns, (*outRealsize) * sizeof(QSPCC_INT));                                                                             \
		*outRowIndex= (QSPCC_INT  *)  qspcc_realloc(*outRowIndex, (*outRealsize) * sizeof(QSPCC_INT));                                                                           \
	}                                                                                                                                                       	                 \
}

__ensurePlainSparseMatrixCapacity(double)
__ensurePlainSparseMatrixCapacity(int)

// n: nth-matrix dimension to consider
// out_matrix_idx: reference to the index that should be filled now in the out matrix
#define __sliceMatrixAlgorithm(type_in,type_out)                                                                                                                                    \
void type_out##type_in##SliceMatrixAlgorithm(int *mat_access_idxs, int mat_access_idxs_len, int *slice_access_idxs, int slice_access_idxs_len, int n, int *out_matrix_idx,          \
						int *update_matrix_dims, int *out_matrix_poly_basis, int *out_matrix_dims, type_out **out_matrix,                                   \
						QSPCC_INT **outRowIndex, QSPCC_INT **outColumns, QSPCC_INT *outUsedCells,                                                           \
						int *outRealsize, int out_matrix_len,                                                                                               \
						type_in *in_matrix, QSPCC_INT *rowIndex, QSPCC_INT *columns, int *in_matrix_dims, int *in_matrix_poly_basis, int in_matrix_len,     \
						int (*slice_triplets)[3],                                                                                                           \
                                                int n_slice_triplets,                                                                                                               \
						char *slice_type, int n_params, int **mat_slices, int **mat_slices_dims, int **mat_slices_poly_basis,                               \
						int *mat_slices_len, int *original_mat_slices_len, int n_mat_slices,                                                                \
						char single_matrix_access, char last_type){                                                                                         \
	int i, copy_idx, c_copy_idx, c_out_idx;                                                                                                                                     \
	type_out copy_val;                                                                                                                                                          \
	char sparse_in_matrix = rowIndex != NULL;                                                                                                                                   \
	/* used only for boolean slice because the output matrix can have less values than input slicing matrix */                                                                  \
	int n_update_matrix_dims = 0;	                                                                                                                                            \
	                                                                                                                                                                            \
	if( n < n_params){                                                                                                                                                          \
		/* condition to be a slice access */                                                                                                                                \
		if(    slice_type == NULL || !slice_type[n] ) {                                                                                                                     \
			/* there are elements to be recursively filled from skipped slices */                                                                                       \
			int slice_triplets_idx = _tripletsIdx(n,slice_type);                                                                                                        \
			/* check also not to overcome array size */		                                                                                                    \
			assert( slice_triplets_idx < n_slice_triplets);                                                                                                             \
			/* this slicing is based on triplets */                                                                                                                     \
			int start = slice_triplets[slice_triplets_idx][0];                                                                                                          \
			int step = slice_triplets[slice_triplets_idx][1];                                                                                                           \
			int end = slice_triplets[slice_triplets_idx][2];                                                                                                            \
			/* keep in mind start starts from 1, C starts from 0 */                                                                                                     \
			slice_access_idxs[n] = 0;                                                           									    \
			for(copy_idx=start; copy_idx<=end ; copy_idx += step){                                                                                                      \
				/* fix value for nth triplet */                                                                                                                     \
				mat_access_idxs[n] = copy_idx;                                                                                                  		    \
				slice_access_idxs[n]++; 															    \
				/* recursive call to add mat_access_idxs for the next dimension */                                                                                  \
				type_out##type_in##SliceMatrixAlgorithm(mat_access_idxs, mat_access_idxs_len, slice_access_idxs, slice_access_idxs_len, n+1, out_matrix_idx,        \
							   update_matrix_dims,                                                                                                      \
							   out_matrix_poly_basis, out_matrix_dims, out_matrix, outRowIndex, outColumns, outUsedCells, outRealsize,                  \
							   out_matrix_len,                                                                                                          \
							   in_matrix, rowIndex, columns, in_matrix_dims, in_matrix_poly_basis, in_matrix_len, slice_triplets,                       \
                                                           n_slice_triplets, slice_type, n_params, mat_slices, mat_slices_dims, mat_slices_poly_basis,                              \
							   mat_slices_len, original_mat_slices_len, n_mat_slices, single_matrix_access, last_type);                                 \
			}                                                                                                                                                           \
		}else if( slice_type[n] ){                                                                                                                                          \
			/* this slicing si based on a matrix with indexes to be copied */                                                                                           \
			int mat_slices_idx = _matrixIdx(n, slice_type);                                                                                                             \
			/* check also not to overcome array size */                                                                                                                 \
			assert( mat_slices_idx < n_mat_slices );                                                                                                                    \
			int *mat_slice = mat_slices[mat_slices_idx];                                                                                                                \
			int mat_slice_len = mat_slices_len[mat_slices_idx];                                                                                                         \
			int *mat_slice_dims = mat_slices_dims[mat_slices_idx];                                                                                                      \
			int *mat_slice_poly_basis = mat_slices_poly_basis[mat_slices_idx];                                                                                          \
			type_out##type_in##_slice_ctx context = { .mat_access_idxs = mat_access_idxs, .mat_access_idxs_len=mat_access_idxs_len,                                     \
						.slice_access_idxs=slice_access_idxs, .slice_access_idxs_len=slice_access_idxs_len,                                                 \
						.n = n, .out_matrix_idx = out_matrix_idx, .update_matrix_dims = update_matrix_dims, .out_matrix_poly_basis=out_matrix_poly_basis,   \
						.out_matrix_dims = out_matrix_dims, .n_update_matrix_dims = n_update_matrix_dims,                                                   \
						.out_matrix = out_matrix, .outRowIndex=outRowIndex, .outColumns=outColumns, .outUsedCells=outUsedCells, .outRealsize=outRealsize,   \
						.out_matrix_len = out_matrix_len, .in_matrix = in_matrix,                                                                           \
						.rowIndex = rowIndex, .columns = columns, .in_matrix_dims = in_matrix_dims, .in_matrix_poly_basis = in_matrix_poly_basis,           \
						.in_matrix_len = in_matrix_len,                                                                                                     \
						.slice_triplets = slice_triplets,                                                                                                   \
						.n_slice_triplets = n_slice_triplets, .slice_type = slice_type, .n_params=n_params, .mat_slices = mat_slices,                       \
						.mat_slices_dims = mat_slices_dims, .mat_slices_poly_basis=mat_slices_poly_basis,                                                   \
						.mat_slices_len = mat_slices_len, .original_mat_slices_len = original_mat_slices_len, .n_mat_slices = n_mat_slices,                 \
						.mat_slices_idx = mat_slices_idx, .single_matrix_access = single_matrix_access, .last_type=last_type };               		    \
																						    \
			if(slice_access_idxs_len == 1){                                                                                                                             \
				/* optimize as a loop */                                                                                                                            \
				if(sparse_in_matrix){                                                                                                                               \
					int r,c;                                                                                                                                    \
					for(int nel=0; nel < mat_slice_dims[0] ; ++nel){                                                                                            \
						/* express flat index in terms of rows-cols, 0-based */                                                                             \
						r = r2i(mat_slice[nel]-1, in_matrix_dims[0], in_matrix_dims[1]);							            \
						c = r2j(mat_slice[nel]-1, in_matrix_dims[0], in_matrix_dims[1]);							            \
							                                                                                                                            \
						char found = 0;                                                                                                                     \
						/* get value in input matrix if present */                                                                                          \
						for(int si=rowIndex[r]-1; si<rowIndex[r+1]-1 ; ++si)                                                                                \
							if( columns[si] == c+1){                                                                                                    \
								found = 1;                                                                                                          \
								copy_val =(double) in_matrix[si];                                                                                   \
								break;                                                                                                              \
							}else if( columns[si] > c+1 )                                                                                               \
								/* columns are ordered */                                                                                           \
								break;                                                                                                              \
						/* if not found means this is an empty position -> 0 */                                                                             \
						if(found){                                                                                                                          \
							type_out##EnsurePlainSparseMatrixCapacity(out_matrix, outRowIndex, outColumns, outUsedCells, outRealsize, out_matrix_idx);  \
							/* assume output is columnar, same behaviour of MATLAB */                                                                   \
							(*outRowIndex)[*out_matrix_idx] = nel+1;							                            \
							(*outColumns)[*out_matrix_idx] = 1;                                                                                         \
							(*out_matrix)[(*out_matrix_idx)++] = (type_out) copy_val;                                                                   \
						}                                                                                                                                   \
					}                                                                                                                                           \
				}else                                                                                                                                               \
					for(int nel=0; nel < mat_slice_dims[0] ; ++nel)                                                                                             \
						/* copy */                                                                                                                          \
						if(in_matrix_len < 2)                                                                                                               \
							(*out_matrix)[(*out_matrix_idx)++] = (type_out) in_matrix[mat_slice[nel]-1];                                                \
						else                                                                                                                                \
							(*out_matrix)[(*out_matrix_idx)++] = (type_out) in_matrix[c2r(mat_slice[nel]-1, in_matrix_dims[0], in_matrix_dims[1])];     \
			}else{                                                                                                                                                      \
				/* execute callback on every element in matrix starting from first dimension */                                                                     \
				mapply(mat_slice, sizeof(int), (void (*)(char *, int *, int, void *)) type_out##type_in##SliceMatrixCallback,                                       \
				       mat_slice_dims, mat_slice_poly_basis, mat_slice_len,  &context);                                                                             \
			}                                                                                                                                                           \
		}                                                                                                                                                                   \
	}else{                                                                                                                                                                      \
		/* end of recursion we have all the slicing indexes */                                                                                                              \
		/* ex for 1,1,n_row | 1,1,1 we will arrive here with mat_access_idxs = [1,1] or [2,1] or [3,1] ... */                                                               \
		if(sparse_in_matrix){                                                                                                                                               \
			char found = 0;                                                                                                                                             \
			/* get start/end position of row with rowIndex */			                                                                                    \
			for(int si=rowIndex[mat_access_idxs[0]-1]-1; si<rowIndex[mat_access_idxs[0]]-1 ; ++si)                                                                      \
				if( columns[si] == mat_access_idxs[1]){                                                                                                             \
					found = 1;                                                                                                                                  \
					copy_val =(type_out) in_matrix[si];                                                                                                         \
					break;                                                                                                                                      \
				}else if( columns[si] > mat_access_idxs[1] )                                                                                                        \
					/* columns are ordered */                                                                                                                   \
					break;                                                                                                                                      \
			/* if not found means this is an empty position -> 0 */                                                                                                     \
			if(found){                                                                                                                                                  \
				type_out##EnsurePlainSparseMatrixCapacity(out_matrix, outRowIndex, outColumns, outUsedCells, outRealsize, out_matrix_idx);                          \
				(*out_matrix)[*out_matrix_idx] = (double) copy_val;                                                                                                 \
				/* assured to be within boundaries of output matrices */                                                                                            \
				(*outRowIndex)[*out_matrix_idx] = slice_access_idxs[0];                                                                                             \
				(*outColumns)[(*out_matrix_idx)++] = slice_access_idxs[1];                                                                                          \
			}                                                                                                                                                           \
		}else{                                                                                                                                                              \
			/* build a column major flat index from a 1-based array of normal/flat indexes in column major */                                                           \
			c_copy_idx = mat_access_idxs[0]-1;  															    \
			int curPlane = 1;																	    \
			for(int d = 1; d < mat_access_idxs_len && mat_access_idxs[d] != MATRIX_ACCESS_CONVENTIONAL_VALUE ; ++d){						    \
				curPlane *= in_matrix_dims[d-1];														    \
				/* build colmajor of normal indices, all the same way thanks to the properties of the column major polinomial basis */				    \
				c_copy_idx += (mat_access_idxs[d]-1)*curPlane;													    \
			}																			    \
			c_out_idx = flattenMatrixAccessAlgorithm(out_matrix_dims, out_matrix_poly_basis, out_matrix_len, slice_access_idxs, slice_access_idxs_len);		    \
			/* copy! */      																	    \
			if(mat_access_idxs_len < 2)                                                                                                                                 \
				(*out_matrix)[c_out_idx] = (type_out) in_matrix[c_copy_idx];                                                                         		    \
			else																			    \
				(*out_matrix)[c_out_idx] = (type_out) in_matrix[c2r(c_copy_idx, in_matrix_dims[0], in_matrix_dims[1])];                                             \
		}                                                                                                                                                                   \
		/* end recursion, go back one level and update mat_access_idxs */                                                                                                   \
	}                                                                                                                                                                           \
}

// define slice matrix for int matrices
__sliceMatrixAlgorithm(int,int)
__sliceMatrixAlgorithm(double,int)
// define slice matrix for double matrices
__sliceMatrixAlgorithm(double,double)
__sliceMatrixAlgorithm(int,double)

// slice the in_matrix and put result in out_matrix.
// n slicing triplets provided
// most efficient (and generic) way to slice
// out_matrix : the already allocated matrix to be filled
// outRowIndex, outColumns, outUsedCells, outRealsize: sparse matrix pointers, the function can enlarge the matrix ondemand.
// in_matrix : the matrix whose values will be copied
// rowIndex/columns: the 1-based indices of the values stored inside matrix, if the input matrix is sparse, null otherwise
// in_matrix_len: number of dimensions of the in_matrix matrix
// out_matrix_len: number of dimensions of the out_matrix matrix
// slice_triplets: a 2D array of N slice triplets (an array of arrays of length 3)
//                 a slice triplet is <start,step,end> 
// n_slice_triplets: the number of slice triplets
// slice_type: a boolean 0/1/2 saved as char for memory efficiency. 
//                       0 means the nth index is a slice index, 
//                       1 means the nth index is a matrix index.
//                       2 means the nth index is a matrix index, to be used as a boolean matrix index (non-zero entries should be copied)
//             his dimension should be n_slice_triplets + n_mat_slices
//             if null, all will be assumed to be slice index.
// n_mat_slices: the number of matrix slices
// ... will contain:
//             in_matrix_len integers, the dimensions of the input matrix
//             out_matrix_len integers, the dimensions of the output matrix
//             n_mat_slices*4 elements, each tuple is an <int *mat_slice, int mat_slice_dims, int mat_slice_poly_basis, int mat_slice_len> in this order
#define __sliceMatrix(type_in,type_out)                                                                                                                          \
int *type_out##type_in##SliceMatrix(type_out **out_matrix, int *out_matrix_poly_basis,                                                                            \
				    QSPCC_INT **outRowIndex, QSPCC_INT **outColumns, QSPCC_INT *outUsedCells, int *outRealsize,                                    \
				    type_in *in_matrix, int *in_matrix_poly_basis, QSPCC_INT *rowIndex, QSPCC_INT *columns, int in_matrix_len, int out_matrix_len,  \
                                    int (*slice_triplets)[3], int n_slice_triplets, char *slice_type, int n_mat_slices, ... ){                                       \
	assert(n_slice_triplets <= in_matrix_len);                                                                                                                                     \
	va_list vl;                                                                                                                                                                    \
	va_start(vl, n_mat_slices);                                                                                                                                                    \
	int n, nslices;                                                                                                                                                                \
	int out_matrix_idx = 0;                                                                                                                                                        \
	int n_bool_slice_type = 0;                                                                                                                                                     \
	char sparse_out_matrix = outRowIndex != NULL;                                                                                                                                  \
        int n_matrices=0;                                                                                                                                                              \
	/* this is true in the only case when the input matrix dimension counts and shape also the output matrix */                                                                    \
	char single_matrix_access = n_slice_triplets == 0 && n_mat_slices == 1;                                                                                                        \
	/* position of boolean matrices in the array of matrices */                                                                                                                    \
	int n_bool_matrices_pos[n_mat_slices];                                                                                                                                         \
	/* position of boolean matrices in the global slice array */                                                                                                                   \
	int n_bool_matrices_slice_pos[n_mat_slices];                                                                                                                                   \
                                                                                                                                                                                       \
	int in_matrix_dims[in_matrix_len];                                                                                                                                             \
	for(n = 0; n < in_matrix_len ; ++n){                                                                                                                                           \
		in_matrix_dims[n] = va_arg(vl,int);                                                                                                                                    \
	}                                                                                                                                                                              \
	int out_matrix_dims[out_matrix_len];                                                                                                                                           \
	for(n = 0; n < out_matrix_len ; ++n){                                                                                                                                          \
		out_matrix_dims[n] = va_arg(vl,int);                                                                                                                                   \
	}                                                                                                                                                                              \
	for(n = 0; n < n_slice_triplets ; ++n){                                                                                                                                        \
		if(slice_type[n] == 2){                                                                                                                                                \
			n_bool_matrices_slice_pos[n_bool_slice_type] = n;                                                                                                              \
			n_bool_matrices_pos[n_bool_slice_type++]=n_matrices++;                                                                                                         \
		}else if(slice_type[n] == 1)                                                                                                                                           \
			n_matrices++;                                                                                                                                                  \
	}                                                                                                                                                                              \
	int *mat_slices[n_mat_slices];                                                                                                                                                 \
	int *mat_slices_dims[n_mat_slices];                                                                                                                                            \
	int *mat_slices_poly_basis[n_mat_slices];                                                                                                                                      \
	int mat_slices_len[n_mat_slices];                                                                                                                                              \
	int original_mat_slices_len[n_mat_slices];                                                                                                                                     \
	int total_slices = n_slice_triplets, output_slices = n_slice_triplets;                                                                                                         \
	char last_type = slice_type[n_slice_triplets + n_mat_slices - 1];                                                                                                              \
                                                                                                                                                                                       \
	for(n = 0; n < n_mat_slices ; ++n){                                                                                                                                            \
		mat_slices[n] = va_arg(vl, int *);                                                                                                                                     \
		mat_slices_dims[n] = va_arg(vl, int *);                                                                                                                                \
		mat_slices_poly_basis[n] = va_arg(vl, int *);                                                                                                                          \
		mat_slices_len[n] = va_arg(vl, int);                                                                                                                                   \
		original_mat_slices_len[n] = mat_slices_len[n];                                                                                                                        \
		/* if last access is with matrix count all dimensions of last matrix */                                                                                                \
		if(last_type && n == n_mat_slices - 1)                                                                                                                                 \
			total_slices = in_matrix_len;                                                                                                                                  \
		else                                                                                                                                                                   \
			total_slices++;                                                                                                                                                \
		output_slices++; /* matrix count as 1 in output matrix */                                                                                                              \
		if(slice_type[n_slice_triplets + n] == 2){                                                                                                                             \
			n_bool_matrices_slice_pos[n_bool_slice_type] = n_slice_triplets + n;                                                                                           \
			n_bool_matrices_pos[n_bool_slice_type++]=n_matrices;                                                                                                           \
		}                                                                                                                                                                      \
		n_matrices++;                                                                                                                                                          \
	}                                                                                                                                                                              \
                                                                                                                                                                                       \
	if(single_matrix_access)																		       \
		output_slices = mat_slices_len[0];                                                                                                                                     \
																						       \
	/* final output matrix dimensions */                                                                                                                                           \
	int *update_matrix_dims = NULL;                                                                                                                                                \
	int bool_total_dims[n_bool_slice_type];                                                                                                                                        \
	int bool_max_total_dims=0;                                                                                                                                                     \
	int bool_max_dims=0;                                                                                                                                                           \
	int pos,slicepos,mat_len;                                                                                                                                                      \
	/* compute flat dimension for each matrix to be converted */                                                                                                                   \
	for(int b=0 ; b < n_bool_slice_type ; ++b){                                                                                                                                    \
		int total_dims = 1;                                                                                                                                                    \
		pos = n_bool_matrices_pos[b];                                                                                                                                          \
		for(int d = 0 ; d < mat_slices_len[pos]; ++d)                                                                                                                          \
			total_dims *= mat_slices_dims[pos][d];                                                                                                                         \
		bool_total_dims[b] = total_dims;                                                                                                                                       \
		if(total_dims > bool_max_total_dims)                                                                                                                                   \
			bool_max_total_dims = total_dims;                                                                                                                              \
		if(mat_slices_len[pos] > bool_max_dims)                                                                                                                                \
			bool_max_dims = mat_slices_len[pos];                                                                                                                           \
	}                                                                                                                                                                              \
	/* converted matrices goes here */                                                                                                                                             \
	int bool_as_idx[n_bool_slice_type][bool_max_total_dims];                                                                                                              	       \
	int bool_poly_basis_as_idx[n_bool_slice_type][bool_max_total_dims];                                                                                                   	       \
	int bool_out_matrix_poly_basis[out_matrix_len];                                                                                                                        	       \
                                 																		       \
	if(n_bool_slice_type){                                                                                                                                                         \
		update_matrix_dims = (int *) qspcc_calloc(n_bool_slice_type, sizeof(int));                                                                                             \
		int *cur_mat_slices_dims;                                                                                                                                              \
		/* fill converted matrices */                                                                                                                                          \
		for(int b=0 ; b < n_bool_slice_type ; ++b){                                                                                                                            \
			pos = n_bool_matrices_pos[b];                                                                                                                                  \
			slicepos = n_bool_matrices_slice_pos[b];                                                                                                                       \
			cur_mat_slices_dims = mat_slices_dims[pos];                                                                                                                    \
			mat_len = mat_slices_len[pos];                                                                                                                                 \
			/* count non-zero elements and generate flat array of flat_indexes corresponding to this boolean matrix */                                                     \
			if( mat_slices_len[pos] < 2 ){                                                                                                                                 \
				for(int flat_idx=0 ; flat_idx < bool_total_dims[b] ; ++flat_idx)                                                                                       \
					if( mat_slices[pos][flat_idx] )                                                                                                                \
						bool_as_idx[b][update_matrix_dims[b]++] = flat_idx + 1;                                                                                \
			}else{  	                                                                                                                                               \
				for(int flat_idx=0 ; flat_idx < bool_total_dims[b] ; ++flat_idx)                                                                                       \
					if( mat_slices[pos][c2r(flat_idx, cur_mat_slices_dims[0], cur_mat_slices_dims[1])] )                                                           \
						bool_as_idx[b][update_matrix_dims[b]++] = flat_idx + 1;                                                                                \
			}																			       \
			/* substitute boolean matrix with flat list of indices */                                                                                                      \
			mat_slices[pos] = bool_as_idx[b];                                                                                                                              \
			mat_slices_dims[pos][0] = update_matrix_dims[b];                                                                                                               \
			mat_slices_len[pos] = 1;                                                                                                                                       \
			/* compute polinomial basis for new "hybrid" matrix */                                                                                                         \
			mat_slices_poly_basis[pos] = bool_poly_basis_as_idx[b];                                                                                                        \
			mat_slices_poly_basis[pos][0] = 1;                                                                                                                             \
			/* update output matrix dimension (only locally to this function, needed for output flat index computation) */                                                 \
			out_matrix_dims[slicepos] = update_matrix_dims[b];                                                                                                             \
		}                                                                                                                                                                      \
		out_matrix_poly_basis = bool_out_matrix_poly_basis;			                                                                                       	       \
		computePolyBasis(out_matrix_poly_basis, out_matrix_dims, out_matrix_len);                                                                                              \
	}                                                                                                                                                                              \
                                                                                                                                                                                       \
	/* access numbers in the apply matrices a(x) access numbers is the set of possible positions of values in x */                                                                 \
       	int access_numbers[output_slices];                                                                                                                                             \
	memset(access_numbers, 0, output_slices * sizeof(int));                                                                                                                        \
                                                                                                                                                                                       \
	/* initially out/row/col are used as triple accumulators of elements that finally will be stored as a CSR sparse matrix by initPlainSparseMatrix */                            \
	if( sparse_out_matrix && *outRealsize >= mat_slices_dims[0][0] )                                                                                                               \
		*outRowIndex= (QSPCC_INT  *)  qspcc_realloc(*outRowIndex, (*outRealsize) * sizeof(QSPCC_INT));                                                                         \
	                                                                                                                                                                               \
	int mat_access_idxs[total_slices];                                                                                                                                             \
	n = 0;	                                                                                                                                                                       \
	type_out##type_in##SliceMatrixAlgorithm(mat_access_idxs, total_slices, access_numbers, output_slices, n, &out_matrix_idx, update_matrix_dims, out_matrix_poly_basis,           \
					out_matrix_dims, out_matrix, outRowIndex, outColumns, outUsedCells, outRealsize, out_matrix_len,                                               \
					in_matrix, rowIndex, columns, in_matrix_dims, in_matrix_poly_basis, in_matrix_len,                                                             \
					slice_triplets, n_slice_triplets, slice_type, n_mat_slices + n_slice_triplets,                                                                 \
					mat_slices, mat_slices_dims, mat_slices_poly_basis, mat_slices_len, original_mat_slices_len, n_mat_slices,                                     \
					single_matrix_access, last_type);                                                                                                              \
                                                                                                                                                                                       \
	if(sparse_out_matrix){                                                                                                                                                         \
		/* now that's finished initialize full sparse matrix from the three lists of row/col/value */                                                                          \
		if(n_bool_slice_type)                                                                                                                                                  \
			type_out##InitPlainSparseMatrix(outRowIndex, outColumns, out_matrix, outUsedCells, out_matrix_idx, update_matrix_dims[0]);                                     \
		else                                                                                                                                                                   \
			type_out##InitPlainSparseMatrix(outRowIndex, outColumns, out_matrix, outUsedCells, out_matrix_idx, mat_slices_dims[0][0]);                                     \
	}                                                                                                                                                                              \
                                                                                                                                                                                       \
	return update_matrix_dims;                                                                                                                                                     \
}

// define doubleSliceMatrix
__sliceMatrix(double,double)
__sliceMatrix(int,double)
// define intSliceMatrix
__sliceMatrix(int,int)
__sliceMatrix(double,int)

void printInt(FILE *f, int *s){
	fprintf(f, "%d ", *s);
}

void printScalar(FILE *f, double *s){
	fprintf(f, "%.5e ", *s);
}

void printMatrixAux(void *m, size_t size, void (*print)(FILE *, char *), int *dim, int *poly_basis, int dimensions, int *access_numbers, int ith_dim, FILE *f){
	int flat_idx, cols, d, j;	
	//enumerate all values for this dimension
	for(d=1; d<=dim[ith_dim] ; ++d){
		access_numbers[ith_dim] = d;
		
		if(ith_dim == dimensions-1){
			//if last dimension	(we enter here only for 1d matrices)
			flat_idx = flattenMatrixAccessAlgorithm(dim, poly_basis, dimensions, access_numbers, dimensions);
			print(f, ((char *)m) + flat_idx * size );
		}else if( ith_dim == dimensions-2 ){
			//print 2d matrix
			cols = dim[ith_dim+1];
            		for (j = 1; j <= cols; ++j){
				access_numbers[ith_dim+1] = j;
				flat_idx = flattenMatrixAccessAlgorithm(dim, poly_basis, dimensions, access_numbers, dimensions);
				// apply print to this element
	            		print(f, ((char *)m) + flat_idx * size );
			}
	                fprintf(f, "\n");
		}else{
			//if more than 2 dimensions left recursive call
			char *msg1 = "Dimension "; // 10 chars
			//number 2 chars
			char *msg2 = " value "; // 7 chars
			//number 4 chars
			// : 1 chars
			//2 spaces between borders			
			//1 space for \0
			char borders[ith_dim+2];
			for(j=0; j<=ith_dim ; ++j)
				borders[j] = '#';
			borders[ith_dim+1]='\0';
			fprintf(f, "%s %s%d%s%d: %s\n", borders, msg1, ith_dim+1, msg2, d, borders);
			//recursive call
			printMatrixAux(m, size, print, dim, poly_basis, dimensions, access_numbers, ith_dim+1, f);
		}
	}

	fprintf(f, "\n");
}

void printMatrix(void *m, int *poly_basis, size_t size, void (*print)(FILE *, char *), int dimensions, ...){
	va_list vl;
	va_start(vl, dimensions);
	assert(dimensions > 0);

	int dim[dimensions];
	int access_numbers[dimensions];
	int i,d;

	for(i=0; i<dimensions ; ++i){
		dim[i] = va_arg(vl,int);
		access_numbers[i] = 1; //start from 1
	}

	printMatrixAux(m, size, print, dim, poly_basis, dimensions, access_numbers, 0, stdout);
}

void dTransposeMatrix(void *dest, void *src, int src_h, int src_w)
{
	int i, j;
	double (*d)[src_h] = dest, (*s)[src_w] = src;
	for (i = 0; i < src_h; i++)
		for (j = 0; j < src_w; j++)
			d[j][i] = s[i][j];
}

void iTransposeMatrix(void *dest, void *src, int src_h, int src_w)
{
	int i, j;
	int (*d)[src_h] = dest, (*s)[src_w] = src;
	for (i = 0; i < src_h; i++)
		for (j = 0; j < src_w; j++)
			d[j][i] = s[i][j];
}

void dTransposeMatrixInplace(double *m, int h, int w){
	int start, next, i;
	double tmp;
	// 1d matrix transpose is meaningless
	if(h == 1 || w == 1)
		return;

	for (start = 0; start <= w * h - 1; start++) {
		next = start;
		i = 0;
		do {	i++;
			next = (next % h) * w + next / h;
		} while (next > start);
		if (next < start || i == 1) continue;
 
		tmp = m[next = start];
		do {
			i = (next % h) * w + next / h;
			m[next] = (i == start) ? tmp : m[i];
			next = i;
		} while (next > start);
	}
}

void iTransposeMatrixInplace(int *m, int h, int w){
	int start, next, i;
	int tmp;

	// 1d matrix transpose is meaningless
	if(h == 1 || w == 1)
		return;
 
	for (start = 0; start <= w * h - 1; start++) {
		next = start;
		i = 0;
		do {	i++;
			next = (next % h) * w + next / h;
		} while (next > start);
		if (next < start || i == 1) continue;
 
		tmp = m[next = start];
		do {
			i = (next % h) * w + next / h;
			m[next] = (i == start) ? tmp : m[i];
			next = i;
		} while (next > start);
	}
}
 
void swap_row(double *a, double *b, int r1, int r2, int n)
{
	double tmp, *p1, *p2;
	int i;
 
	if (r1 == r2) return;
	for (i = 0; i < n; i++) {
		p1 = mat_elem(a, r1, i, n, n);
		p2 = mat_elem(a, r2, i, n, n);
		tmp = *p1, *p1 = *p2, *p2 = tmp;
	}
	tmp = b[r1], b[r1] = b[r2], b[r2] = tmp;
}
 
void gauss_eliminate(double *a, double *b, double *x, int n)
{
#define A(y, x) (*mat_elem(a, y, x, n, n))
	int i, j, col, row, max_row,dia;
	double max, tmp;
 
	for (dia = 0; dia < n; dia++) {
		max_row = dia, max = A(dia, dia);
 
		for (row = dia + 1; row < n; row++)
			if ((tmp = fabs(A(row, dia))) > max)
				max_row = row, max = tmp;
 
		swap_row(a, b, dia, max_row, n);
 
		for (row = dia + 1; row < n; row++) {
			tmp = A(row, dia) / A(dia, dia);
			for (col = dia+1; col < n; col++)
				A(row, col) -= tmp * A(dia, col);
			A(row, dia) = 0;
			b[row] -= tmp * b[dia];
		}
	}
	for (row = n - 1; row >= 0; row--) {
		tmp = b[row];
		for (j = n - 1; j > row; j--)
			tmp -= x[j] * A(row, j);
		x[row] = tmp / A(row, row);
	}
#undef A
}

void dSolveLinearSystem(double *A, double *B, double *C, int m, int na, int nb){
	assert(m * nb == na && "Linear system solver for non-square matrix, or with multiple known terms not implemented, please enable MKL (remove -nomkl)");
	double LU[m * na];
	memcpy(LU, A, m*na*sizeof(double));
	gauss_eliminate(LU, B, C, m);
}

void iSolveLinearSystem(int *A, int *B, double *C, int m, int na, int nb){
	assert(m * nb == na && "Linear system solver for non-square matrix, or with multiple known terms not implemented, please enable MKL (remove -nomkl)");
	double LU[m * na];
	double b[m*nb];
	for(int i = 0 ; i < m * na ; ++i)
		LU[i] = (double) A[i];
	for(int i = 0 ; i < m * nb ; ++i)
		b[i] = (double) B[i];
	gauss_eliminate(LU, b, C, m);
}

/**
* function used alternatively to the mkl one (cblas_dgemm)
* to multiply two matrices C = A*B
* the memory for C should have been already allocated
* TODO implement case if not ROWMAJOR
*/
void dMultiplyMatrices(double *A, double *B, double *C, int m, int n, int k){
	int i = 0;
	int j = 0;
	unsigned curpos = 0;
	int counter = 0;
	
	#pragma omp parallel shared(C) private(i, j, curpos, counter) if(m > PARALLELIZABLE_MULTIPLY_TRESHOLD)
	{	
		//computes the result matrix elements (m rows and n columns)
		#pragma omp for schedule(static)
		for(i=0; i<m; i++){
			for(j=0; j<n; j++){
				curpos = n*i+j;
				C[curpos] = 0.;
				for(counter = 0; counter < k; counter++){
					// ith row of A and jth column of B
					C[curpos] += A[i*k + counter]*B[counter*n + j];			
				}
			}
		}
	}
}

/**
* function used alternatively to the mkl one (cblas_dgemm)
* to multiply two matrices
*/
void iMultiplyMatrices(int *A, int *B, int *C, int m, int n, int k){
	int i = 0;
	int j = 0;
	unsigned curpos;
	int counter = 0;
	
	#pragma omp parallel shared(C) private(i, j, curpos, counter)
	{	
		//computes the result matrix elements (m rows and n columns)
		#pragma omp for schedule(static)
		for(i=0; i<m; i++){
			for(j=0; j<n; j++){
				curpos = n*i+j;
				C[curpos] = 0;
				for(counter = 0; counter < k; counter++){
					// ith row of A and jth column of B
					C[curpos] += A[i*k + counter]*B[counter*n + j];			
				}
			}
		}
	}
}

/**
* function used alternatively to the mkl one (cblas_dgemm)
* to multiply two matrices C = A*B
* the memory for C should have been already allocated
* TODO implement case if not ROWMAJOR
*/
#define __kron(funtype, type)  \
void funtype##KroneckerTensorProduct(type *A, type *B, type *C, int m, int n, int q, int p){ \
	/* int crow = m*q; */                                                                \
	int ccol = n*p;                                                                      \
        size_t startRow,startCol;                                                            \
	for(size_t i=0;i<m;i++){                                                             \
		for(size_t j=0;j<n;j++){                                                     \
			startRow = i*q;                                                      \
			startCol = j*p;                                                      \
			type const x = A[i*n+j];                                             \
			for(size_t k=0;k<q;k++){                                             \
				for(size_t l=0;l<p;l++){                                     \
					C[(startRow+k)*ccol + (startCol+l)] = x*B[k*p+l];    \
				}                                                            \
			}                                                                    \
		}                                                                            \
	}                                                                                    \
}

__kron(d, double)
__kron(i, int)

#define __recursiveSubCopy(type) \
void type##RecursiveSubCopy(int n, size_t *start_access_numbers, int *access_numbers, type *out, int *out_mat_dims, int *out_poly_basis, type *in, int *in_mat_dims, int *in_poly_basis, int mat_dims_num){\
                                                                                                                                                                     \
	if( n < mat_dims_num){                                                                                                                                       \
		for(int i = 1 ; i <= in_mat_dims[n] ; ++i){                                                                                                          \
			access_numbers[n] = i;                                                                                                                       \
			type##RecursiveSubCopy(n+1, start_access_numbers, access_numbers, out, out_mat_dims, out_poly_basis, in, in_mat_dims, in_poly_basis, mat_dims_num);         \
		}                                                                                                                                                    \
	}else{                                                                                                                                                       \
		/* end of recursion all access numbers were set */                                                                                                   \
		size_t in_flat_idx = flattenMatrixAccessAlgorithm(in_mat_dims, in_poly_basis, mat_dims_num, access_numbers, mat_dims_num);                                          \
		/* column access starts from start col in output matrix */                                                                                           \
		for(int i = 0; i < mat_dims_num ; ++i)                                                                                                               \
			access_numbers[i] += start_access_numbers[i];                                                                                                \
		size_t out_flat_idx = flattenMatrixAccessAlgorithm(out_mat_dims, out_poly_basis, mat_dims_num, access_numbers, mat_dims_num);                                       \
		for(int i = 0; i < mat_dims_num ; ++i)                                                                                                               \
			access_numbers[i] -= start_access_numbers[i];                                                                                                \
		out[out_flat_idx] = in[in_flat_idx];                                                                                                                 \
	}                                                                                                                                                            \
}

__recursiveSubCopy(double)
__recursiveSubCopy(int)

/* out_reps : number of repetitions along every dimension */
#define __recursiveCopy(type) \
void type##RecursiveCopy(int n, size_t *start_access_numbers, int *access_numbers, int *out_reps, type *out, int *out_mat_dims, int *out_poly_basis, type *in, int *in_mat_dims, int *in_poly_basis, int mat_dims_num){  \
                                                                                                                                                                                \
	if( n < mat_dims_num ){                                                                                                                                                 \
		for(int i = 0 ; i < out_reps[n] ; ++i){                                                                                                                         \
			start_access_numbers[n] = i*in_mat_dims[n];                                                                                                             \
			type##RecursiveCopy(n+1, start_access_numbers, access_numbers, out_reps, out, out_mat_dims, out_poly_basis, in, in_mat_dims, in_poly_basis, mat_dims_num);     \
		}                                                                                                                                                               \
	}else{                                                                                                                                                                  \
		/* now we have the initial position of the sub-matrix, let's recursively copy the in matrix starting from there */                                              \
		type##RecursiveSubCopy(0, start_access_numbers, access_numbers, out, out_mat_dims, out_poly_basis, in, in_mat_dims, in_poly_basis, mat_dims_num);                      \
	}                                                                                                                                                                       \
}

__recursiveCopy(double)
__recursiveCopy(int)

#define __repeatMatrix(stype, type) \
void stype##RepeatMatrix(type* out, int *out_poly_basis, type* in, int *in_poly_basis, int in_mat_dims_num, int totalParams, ...){       \
	va_list vl;                                                                                                                      \
	va_start(vl, totalParams);                                                                                                       \
	int in_repeat_num = totalParams-in_mat_dims_num;                                                                                 \
                                                                                                                                         \
	int in_mat_nelems = 1;                                                                                                           \
	int in_mat_dims[in_mat_dims_num];                                                                                                \
	for(int i = 0 ; i < in_mat_dims_num ; ++i ){                                                                                     \
		in_mat_dims[i] = va_arg(vl,int);                                                                                         \
		in_mat_nelems *= in_mat_dims[i];                                                                                         \
	}                                                                                                                                \
	int in_repeat[in_repeat_num];                                                                                                    \
	for(int i = 0 ; i < in_repeat_num ; ++i )                                                                                        \
		in_repeat[i] = va_arg(vl,int);                                                                                           \
                                                                                                                                         \
	int col_rep;                                                                                                                     \
	/* if 1 repeat number that number */                                                                                             \
	/* should be multiplied along rows and also cols */                                                                              \
	if(in_repeat_num == 1)                                                                                                           \
		col_rep = in_repeat[0];                                                                                                  \
	else                                                                                                                             \
		col_rep = in_repeat[1];                                                                                                  \
                                                                                                                                         \
	int row_rep;                                                                                                                     \
	row_rep = in_repeat[0];                                                                                                          \
                                                                                                                                         \
	int out_mat_dims[in_mat_dims_num];                                                                                               \
	for(int i = 0 ; i < in_mat_dims_num ; ++i)                                                                                       \
		if( i < in_repeat_num )                                                                                                  \
			out_mat_dims[i] = in_mat_dims[i]*in_repeat[i];                                                                   \
		else if( i == 1 && in_repeat_num == 1 )                                                                                  \
			out_mat_dims[i] = in_mat_dims[i]*in_repeat[0];                                                                   \
		else                                                                                                                     \
			out_mat_dims[i] = in_mat_dims[i];                                                                                \
			                                                                                                                 \
	size_t start_access_numbers[in_mat_dims_num];                                                                                    \
	int access_numbers[in_mat_dims_num];                                                                                             \
	memset(start_access_numbers, 0, in_mat_dims_num*sizeof(size_t));                                                                 \
	memset(access_numbers, 0, in_mat_dims_num*sizeof(int));                                                                          \
                                                                                                                                         \
	/* first replicate columns, then use memcpy to fill remaining rows */                                                            \
	if(in_mat_dims_num == 1){                                                                                                        \
		/* 1D vector */                                                                                                          \
		/* use memcpy also for column copying */                                                                                 \
                                                                                                                                         \
		size_t pos = 0;                                                                                                          \
		for(int r=0; r < col_rep ; ++r){                                                                                         \
			memcpy(out + pos, in, sizeof(type)*in_mat_nelems);                                                               \
			pos += in_mat_nelems;                                                                                            \
		}                                                                                                                        \
		size_t n_col_rep_elems = in_mat_nelems*col_rep;                                                                          \
		pos = n_col_rep_elems;                                                                                                   \
		/* then copy the first N lines row_rep-1 number of times */                                                              \
		/* to obtain row_rep repetitions   */                                                                                    \
		for(int r=0; r < row_rep - 1 ; ++r){                                                                                     \
			memcpy(out + pos, in, sizeof(type)*n_col_rep_elems);                                                             \
			pos += n_col_rep_elems;                                                                                          \
		}                                                                                                                        \
	}else if(col_rep == 1){                                                                                                          \
		/* just copy by col */                                                                                                   \
		/* start_access_numbers[0] = 0; */                                                                                       \
		for(int r = 0; r < row_rep ; ++r){                                                                                       \
			type##RecursiveSubCopy(0, start_access_numbers, access_numbers, out, out_mat_dims, out_poly_basis, in, in_mat_dims, in_poly_basis, in_mat_dims_num);  \
			start_access_numbers[0] += in_mat_dims[0];                                                                       \
		}                                                                                                                        \
	}else if(row_rep == 1){                                                                                                          \
		/* just copy by col */                                                                                                   \
		/* start_access_numbers[1] = 0; */                                                                                       \
		for(int r = 0; r < col_rep ; ++r){                                                                                       \
			type##RecursiveSubCopy(0, start_access_numbers, access_numbers, out, out_mat_dims, out_poly_basis, in, in_mat_dims, in_poly_basis, in_mat_dims_num);  \
			start_access_numbers[1] += in_mat_dims[1];                                                                       \
		}                                                                                                                        \
	}else{                                                                                                                           \
		/* fill with recursive call */                                                                                           \
		type##RecursiveCopy(0, start_access_numbers, access_numbers, in_repeat, out, out_mat_dims, out_poly_basis, in, in_mat_dims, in_poly_basis, in_mat_dims_num);  \
	}                                                                                                                                \
}

__repeatMatrix(d, double)
__repeatMatrix(i, int)

//once assured that the total original dimension is equal to the total output dimension, it invert the order of
//the original pointer so that the flattened vector will be ordered column-wise, and write it on the output pointer
void viReshape(int* pointer, int* output, int originalDimensionNum, int totalDimensionNumber, ...){

	va_list vl;
	va_start(vl, totalDimensionNumber);

	int outputDimensionNum = totalDimensionNumber-originalDimensionNum;

	size_t originalDimensions[originalDimensionNum];
	for(int i = 0 ; i < originalDimensionNum ; ++i ){
	        originalDimensions[i] =  va_arg(vl,size_t);
	}
	size_t outputDimensions[outputDimensionNum];
	for(int i = originalDimensionNum ; i < outputDimensionNum+originalDimensionNum ; ++i ){
		outputDimensions[i-originalDimensionNum] = va_arg(vl,size_t);
	}
	
 	//check if the number of element of the original vector is compatible with the output one
 	int originalTotNum = 1;
 	for(int i = 0; i < originalDimensionNum; i++){
 		originalTotNum *= originalDimensions[i];
 	}
 	
 	int outputTotNum = 1;
 	for(int i = 0; i < outputDimensionNum; i++){
 		outputTotNum *= outputDimensions[i];
 	}
 	
 	assert(originalTotNum == outputTotNum);
 	
	viReshapeAlgorithm(pointer, output, originalTotNum, originalDimensions, originalDimensionNum, outputDimensions, outputDimensionNum);
}

void viReshapeAlgorithm(int *pointer, int *output, int originalTotNum, size_t *originalDimensions, int originalDimensionNum, size_t *outputDimensions, int outputDimensionNum){
 	//if the original dimensions are just one, we don't have to do nothing
	#ifdef ROWMAJOR
 	int temp[originalTotNum];
 	if(originalDimensionNum <= 1){
		if(output != pointer)
	 		memcpy( output, pointer, originalTotNum*sizeof(int));
		return;
 	}else{
 		//turn into column major
 		turnIntoColumnMajor(pointer, temp, sizeof(int), originalDimensionNum, originalDimensions);
 	}
 	
 	turnIntoRowMajor(temp, output, sizeof(int), outputDimensionNum, outputDimensions);
	#else
	// if already stored in column major we just need to change the dimensions (already done before)
	// and copy the memory as-is
	memcpy( output, pointer, originalTotNum*sizeof(int));	
	#endif
}


void siReshape(int* output, int start, int end, int step, int outputDimensionNum, ...){
	
	va_list vl;
	va_start(vl, outputDimensionNum);
	size_t outputDimensions[outputDimensionNum];
	for(int i = 0 ; i < outputDimensionNum ; ++i ){
		outputDimensions[i] = va_arg(vl,size_t);
	}

	int totalDimension = viSequenceDimension(start, step, end);
	int temp[totalDimension];
	viSequenceVector(temp, start, step, end);

	//copy on an array
	#ifdef ROWMAJOR
	//turn it row-major with respect to the output dimensions
	turnIntoRowMajor(temp, output, sizeof(int), outputDimensionNum, outputDimensions);
	#else
	turnIntoColumnMajor(temp, output, sizeof(int), outputDimensionNum, outputDimensions);
	#endif
}

//turns a row-major expressed matrix into a column major expressed one
void turnIntoColumnMajor(void* pointer, void* output, size_t size, int dimNumber, size_t *dimensions){
	
	//get all the element number
 	int totNum = 1;
 	for(int i = 0; i < dimNumber; i++){
 		totNum *= dimensions[i];
 	}
 	
 	int numberElementsForFloor = dimensions[0]*dimensions[1];
 	 //the column order of the columns have to be respected
 	//previously the matrix was saved
 	int residual = 0;
 	int i = 0;
 	int j = 0;
 		
 	int newIndex = 0;
 	for(int k = 0; k < totNum; ++k){
 		residual = k % numberElementsForFloor;
 	
 		// the flattened matrix access is i*numCol+j 
 		j = residual % dimensions[1];
 		i =  residual / dimensions[1];
			
 		//computes the new index that inverts rows and columns 
 		//(it is the oldindex - (i*numCol+j) + j*numRow+i)
 		newIndex = k + i*(1-dimensions[1])+j*(dimensions[0]-1);
 		memcpy(((char *)output) + newIndex*size,  ((char *)pointer) + k*size, size);
 	}
}

//turns a column-major expressed matrix into a row-major expressed one
void turnIntoRowMajor(void* pointer, void* output, size_t size, int dimNumber, size_t *dimensions){

	//get all the element number
 	int totNum = 1;
 	for(int i = 0; i < dimNumber; i++){
 		totNum *= dimensions[i];
 	}
 	
 	int numberElementsForFloor = dimensions[0]*dimensions[1];
 	 //the column order of the columns have to be respected
 	//previously the matrix was saved
 	int residual = 0;
 	int i = 0;
 	int j = 0;
 		
 	int newIndex = 0;
 	for(int k = 0; k < totNum; ++k){
 		residual = k % numberElementsForFloor;
 	
 		// the flattened matrix access is i*numCol+j 
 		i = residual % dimensions[0];
 		j =  residual / dimensions[0];
 		
 		//computes the new index that inverts rows and columns 
 		//(it is the oldindex - (i*numCol+j) + j*numRow+i)
 		newIndex = k + i*(dimensions[1]-1)+j*(1-dimensions[0]);
 		memcpy(((char *)output) + newIndex*size,  ((char *)pointer) + k*size, size);
 	}
}

//once assured that the total original dimension is equal to the total output dimension, it invert the order of
//the original pointer so that the flattened vector will be ordered column-wise, and write it on the output pointer
void vdReshape(double* pointer, double* output, int originalDimensionNum, int totalDimension, ...){

	va_list vl;
	va_start(vl, totalDimension);

	int outputDimensionNum = totalDimension - originalDimensionNum;

	size_t originalDimensions[originalDimensionNum];
	for(int i = 0 ; i < originalDimensionNum ; ++i ){
		originalDimensions[i] = va_arg(vl,size_t);
	}
	size_t outputDimensions[outputDimensionNum];
	for(int i = originalDimensionNum ; i < outputDimensionNum+originalDimensionNum ; ++i ){
		outputDimensions[i-originalDimensionNum] = va_arg(vl,size_t);
	}
	
	
 	//check if the number of element of the original vector is compatible with the output one
 	int originalTotNum = 1;
 	for(int i = 0; i < originalDimensionNum; i++){
 		originalTotNum *= originalDimensions[i];
 	}
 	
 	int outputTotNum = 1;
 	for(int i = 0; i < outputDimensionNum; i++){
 		outputTotNum *= outputDimensions[i];
 	}
 	
 	assert(originalTotNum == outputTotNum);
 		
	vdReshapeAlgorithm(pointer, output, originalTotNum, originalDimensions, originalDimensionNum, outputDimensions, outputDimensionNum);
}

void vdReshapeAlgorithm(double *pointer, double *output, int originalTotNum, size_t *originalDimensions, int originalDimensionNum, size_t *outputDimensions, int outputDimensionNum){
 	#ifdef ROWMAJOR
 	double temp[originalTotNum];
 	if(originalDimensionNum <= 1){
		if(output != pointer)
			/* avoid memcpy if input == output */
	 		memcpy( output, pointer, originalTotNum*sizeof(double));
		return;
 	}else{
 		//turn into column major
 		turnIntoColumnMajor(pointer, temp, sizeof(double), originalDimensionNum, originalDimensions);
 	}
 	
 	turnIntoRowMajor(temp, output, sizeof(double), outputDimensionNum, outputDimensions);
	#else
	// if already stored in column major we just need to change the dimensions (already done before)
	// and copy the memory as-is
	if(output != pointer)
		/* avoid memcpy if input == output */
		memcpy( output, pointer, originalTotNum*sizeof(double));	
	#endif
}

void sdReshape(double* output, double start, double end, double step, int outputDimensionNum, ...){
	
	va_list vl;
	va_start(vl, outputDimensionNum);

	size_t outputDimensions[outputDimensionNum];
	for(int i = 0 ; i < outputDimensionNum ; ++i ){
		outputDimensions[i] = va_arg(vl,size_t);
	}
	
	int flatIdx=0;
	int residual,outputIdx;
	for(double i=start; i < (end + SEQUENCE_TOL) ; i += step){
#ifdef ROWMAJOR
		if(outputDimensionNum >= 2)
			outputIdx = colMajor2RowMajor(flatIdx++, outputDimensions[0], outputDimensions[1]);
		else
			outputIdx = flatIdx++;
#else
		outputIdx = flatIdx++;
#endif
		output[outputIdx] = (double) i;
	}
}

// ---------------CUM SUM FUNCTION------------------------

//sums cumulatively the elements of the matrix on the column for integer
void viCumSum(int *pointer, int* output, int dimNumber ,...){
	//reads the matrix dimension	
	va_list vl;
	va_start(vl, dimNumber);

	int dimensions[dimNumber];
	int totalDimension = 1;
	for(int i = 0 ; i < dimNumber ; ++i ){
		dimensions[i] = va_arg(vl,int);
		totalDimension *= dimensions[i];
	}
	
	//if it has just one dimension, there is nothing to do (column or row vector)
	if(dimNumber==1 || (dimNumber==2 && dimensions[0]==1)){
				//populates the flattened output matrix
		int cumSum = 0;
		for(int k=0; k<totalDimension; k++){
			cumSum += pointer[k];
			output[k] = cumSum;
		}
	}else{
		int floorDimension = dimensions[0]*dimensions[1];
		
		int i;
		int j;
		int residual;
		int fixedDimensionIndex;
		int rowCounter;
		
		//populates the flattened output matrix
		for(int k=0; k<totalDimension; k++){
			residual = k % floorDimension;
 	
 			// the flattened matrix access is i*numElementsPerCol+j 
 			//so we can get i and j
 			j = residual % dimensions[1];
 			i =  residual / dimensions[1];
 			
 			//now we sum all the elements on the same floor with row leq than i
 			fixedDimensionIndex = k - i * dimensions[1];
 			
 			if(i==0){
 				output[k] = pointer[k];
 			}else{
 				output[k] = pointer[k] + output[fixedDimensionIndex + (i-1)*dimensions[1]];
 			}
		}
	}
}

//sums cumulatively the elements of the matrix on the column for double
void vdCumSum(double *pointer, double* output, int dimNumber ,...){
	//reads the matrix dimension	
	va_list vl;
	va_start(vl, dimNumber);

	int dimensions[dimNumber];

	int totalDimension = 1;
	for(int i = 0 ; i < dimNumber ; ++i ){
		dimensions[i] = va_arg(vl,int);
		totalDimension *= dimensions[i];
	}
	
	//if it has just one dimension, there is nothing to do (column or row vector)
	if(dimNumber==1 || (dimNumber==2 && dimensions[0]==1)){
		//populates the flattened output matrix
		double cumSum = 0.0;
		for(int k=0; k<totalDimension; k++){
			cumSum += pointer[k];
			output[k] = cumSum;
		}
	}else{
		int floorDimension = dimensions[0]*dimensions[1];
		
		int i;
		int j;
		int residual;
		int fixedDimensionIndex;
		int rowCounter;
		
		//populates the flattened output matrix
		for(int k=0; k<totalDimension; k++){
			residual = k % floorDimension;
 	
 			// the flattened matrix access is i*numElementsPerCol+j 
 			//so we can get i and j
 			j = residual % dimensions[1];
 			i =  residual / dimensions[1];
 			
 			//now we sum all the elements on the same floor with row leq than i
 			fixedDimensionIndex = k - i * dimensions[1];
 			if(i==0){
 				output[k] = pointer[k];
 			}else{
 				output[k] = pointer[k] + output[fixedDimensionIndex + (i-1)*dimensions[1]];
 			}
		}
	}
}

//sums cumulatively the elements of a slice for integers
void siCumSum(int* output, int start, int end, int step){
	viSequenceVector(output, start, step, end);
	int cumSum = 0;
	int totalDimension = viSequenceDimension(start, step, end);
	for(int k=0; k<totalDimension; k++){
		cumSum += output[k];
		output[k] = cumSum;
	}
}


//sums cumulatively the elements of a slice for doubles
void sdCumSum(double* output, double start, double end, double step){
	vdSequenceVector(output, start, step, end);
	double cumSum = 0.0;
	int totalDimension = vdSequenceDimension(start, step, end);
	for(int k=0; k<totalDimension; k++){
		cumSum += output[k];
		output[k] = cumSum;
	}
}

// -------------- GAUSSIAN SIMULATOR ---------------------------------
/**
*gaussian simulator, returns a random number taken from a gaussian with 
* average 0 and standard deviation = 1
* it is derived from the central limit theorem 
* http://c-faq.com/lib/gaussian.html
*/
double standardGaussRand(){
	double x = 0;
	int i;
	for(i=0; i < CENTRAL_LIMIT_IID_NUM; i++){
		x+= (double) rand() / RAND_MAX;
	}
	x-= CENTRAL_LIMIT_IID_NUM / 2.0;
	x /= sqrt(CENTRAL_LIMIT_IID_NUM / 12.0);

	return x;
}

/**
* gaussian simulator, returns a random number taken from a gaussian with
* average= mean and standard deviation = sigma 
*/
double gaussRand(double mean, double sigma){
	return (standardGaussRand() + mean)*sigma; 
}

/**
* gaussian simulator
* returns a pointer to a double array of dimensions gaussian random numbers
* extracted from dimensions gaussian i.i.d with average = mean and standard deviation = sigma
*/
double *randnImpl(double *outmatrix, double mean, double sigma, int dimensions, ...){
	//reading the matrix dimensions
	va_list vl;
	va_start(vl, dimensions);
	assert(dimensions > 0);

	int dim_tot = 1;
	int i;

	for(i=0; i<dimensions ; ++i)
		dim_tot *= va_arg(vl,int);

	int counter = 0;
	for(counter = 0; counter < dim_tot; counter++){
		outmatrix[counter] = gaussRand(mean, sigma);	
	}
	
	return outmatrix;
}

/**
 * return a vector of random integers
 */
int *randiImpl(int *outmatrix, int from, int to, int dimensions, ...){
	//reading the matrix dimensions
	va_list vl;
	va_start(vl, dimensions);
	assert(dimensions > 0);

	int dim_tot = 1;
	int i;
	time_t t;

	for(i=0; i<dimensions ; ++i){
		dim_tot *= va_arg(vl,int);
	}

	int counter = 0;
	/* range+1 because we need a random value in interval 0..range extreme included */
	int range = to - from + 1;
	if(range == 0)
		/* if equal no choice.. */
		for(counter = 0; counter < dim_tot; counter++)
			outmatrix[counter] = from;	
	else
		for(counter = 0; counter < dim_tot; counter++)
			outmatrix[counter] = rand() % range + from;	
	
	return outmatrix;
}

/**
 * return a vector of random numbers with uniform distribution
 */
double *randuImpl(double *outmatrix, double from, double to, int dimensions, ...){
	//reading the matrix dimensions
	va_list vl;
	va_start(vl, dimensions);
	assert(dimensions > 0);

	int dim_tot = 1;
	int i;
	time_t t;

	for(i=0; i<dimensions ; ++i)
		dim_tot *= va_arg(vl,int);

	double range = to-from;
	int counter = 0;
	if(range == 0)
		/* if equal no choice.. */
		for(counter = 0; counter < dim_tot; counter++)
			outmatrix[counter] = from;	
	else
		for(counter = 0; counter < dim_tot; counter++)
			outmatrix[counter] = range*((double)rand() / (double)RAND_MAX) + from;	
	
	return outmatrix;
}

//CONTAINMENT FUNCTIONS
// function that tells if the flattened matrix input contains 
//the scalar element for integers

//checks if an int is contained in an array
int iContainedInArray(int n, int *input, int element){
	for(int i=0; i<n; ++i){
		if(input[i]==element){
			return 1;
		}
	}
	return 0;
}

//checks id an int is contained in a slice
int iContainedInSlice(int start, int step, int end, int element){
	if((element-start)%step == 0 && element<=end && element>=start)
		return 1;
	else
		return 0;
}

// checks if a double is contained in an array
int dContainedInArray(int n, double *input, double element){
	for(int i=0; i<n; ++i){
		if(input[i]==element){
			return 1;
		}
	}
	return 0;
}

//checks if a double is contained in a slice -double-
int dContainedInSlice(double start, double step, double end, double element){
	if(modulBetweenDoubles(element-start,step) < DOUBLE_EQUALITY_TOL && element<=end && element>=start)
		return 1;
	else
		return 0;
}

// checks if an array is contained in another array -int- 
void viContainedInArray(int n, int *input, int m, int *toBeChecked, int *output){
	//initialize the output matrix with all 1
	for(int j=0; j<m; ++j){
		output[j]=0;
	}
	
	//by cycling over the input matrix, populates the "true" positions of the output matrix
	for(int i=0; i<n; ++i){
		for(int j=0; j<m; j++){
			if(toBeChecked[j]==input[i]){
				output[j]=1;
			}
		}
	}
}

//checks if an array is contained in a slice -int-
void viContainedInSlice(int start, int step, int end, int m, int *toBeChecked, int *output){
	//initialize the output matrix with all 1
	for(int j=0; j<m; ++j){
		output[j]=0;
	}
	
	//by cycling over the input matrix, populates the "true" positions of the output matrix
	for(int j=0; j<m; j++){
		if((toBeChecked[j]-start)%step == 0 && toBeChecked[j]<=end && toBeChecked[j]>=start){
			output[j]=1;
		}
	}
}

//checks if a slice is contained in an array -int-
void siContainedInArray(int n, int *input, int start, int step, int end, int *output){
	//initialize the output matrix with all 1
	int m = viSequenceDimension(start, step, end);
	for(int j=0; j<m; ++j){
		output[j]=0;
	}
	
	//by cycling over the input matrix, populates the "true" positions of the output matrix
	for(int i=0; i<n; ++i){
		if((input[i]-start)%step == 0 && input[i]<=end && input[i]>=start){
			output[(input[i]-start)/step]=1;
		}
	}
}

//checks if a slice is contained in another slice -int-
void siContainedInSlice(int inputStart, int inputStep, int inputEnd, int start, int step, int end, int *output){
	//initialize the output matrix with all 1
	int m = viSequenceDimension(start, step, end);
	for(int j=0; j<m; ++j){
		output[j]=0;
	}
	
	//by cycling over the input matrix, populates the "true" positions of the output matrix
	for(int toBeChecked=start; toBeChecked<=end; toBeChecked=toBeChecked+step){
		if((toBeChecked-inputStart)%inputStep == 0 && toBeChecked<=inputEnd && toBeChecked>=inputStart){
			output[(toBeChecked-start)/step]=1;
		}
	}
}

// checks if an array is contained in another array -doubles-
void vdContainedInArray(int n, double *input, int m, double *toBeChecked, int *output){
	//initialize the output matrix with all 1
	for(int j=0; j<m; ++j){
		output[j]=0;
	}
	
	//by cycling over the input matrix, populates the "true" positions of the output matrix
	for(int i=0; i<n; ++i){
		for(int j=0; j<m; j++){
			if(toBeChecked[j]==input[i]){
				output[j]=1;
			}
		}
	}
}

//checks if an array is contained in a slice -doubles-
void vdContainedInSlice(double inputStart, double inputStep, double inputEnd, int m, double *toBeChecked, int *output){
	//initialize the output matrix with all 1
	for(int j=0; j<m; ++j){
		output[j]=0;
	}
	
	//by cycling over the input matrix, populates the "true" positions of the output matrix
	for(int j=0; j<m; j++){
		if(modulBetweenDoubles((toBeChecked[j]-inputStart),inputStep)<DOUBLE_EQUALITY_TOL && toBeChecked[j]<=inputEnd && toBeChecked[j]>=inputStart){
			output[j]=1;
		}
	}
}

//checks if a slice is contained in an array -doubles-
void sdContainedInArray(int n, double *input, double start, double step, double end, int *output){
	//initialize the output matrix with all 1
	int m = vdSequenceDimension(start, step, end);
	for(int j=0; j<m; ++j){
		output[j]=0;
	}
	
	//by cycling over the input matrix, populates the "true" positions of the output matrix
	for(int i=0; i<n; ++i){
		if(modulBetweenDoubles((input[i]-start),step)<DOUBLE_EQUALITY_TOL && input[i]<=end && input[i]>=start){
			output[(int) ((input[i]-start)/step)]=1;
		}
	}
}

//checks if a slice is contained in another slice -doubles-
void sdContainedInSlice(double inputStart, double inputStep, double inputEnd, double start, double step, double end, int *output){
	//initialize the output matrix with all 1
	int m = vdSequenceDimension(start, step, end);
	for(int j=0; j<m; ++j){
		output[j]=0;
	}
	
	//by cycling over the input matrix, populates the "true" positions of the output matrix
	for(double toBeChecked=start; toBeChecked<=end; toBeChecked=toBeChecked+step){
		if(modulBetweenDoubles((toBeChecked-inputStart),inputStep)<DOUBLE_EQUALITY_TOL && toBeChecked<=inputEnd && toBeChecked>=inputStart){
			output[(int) ((toBeChecked-start)/step)]=1;
		}
	}
}

//useful to check the % operator between doubles
double modulBetweenDoubles(double first, double second){
	int times = (int) (first / second);
	return first - times*second;
}

/*
* interpolates the values in xToBeInterpolated with the nearest values of the passed point series
*/
double dInterp(double* xValues, int xValuesLen, double* yValues, int yValuesLen, double xToBeInterpolated){
	double minDistance = -1;
	int nearestIndex = -1;
	//iterates over the xValues to find the nearest one
	for(int j=0; j<xValuesLen; j++){
		
		double distance = fabs(xValues[j]-xToBeInterpolated);
		
		if(nearestIndex == -1 || minDistance > distance){
			nearestIndex = j;
			minDistance = distance;
		}		
	}
	
	return yValues[nearestIndex];
}

void vdInterp(double *yValuesInterp, double* xValues, int xValuesLen, double* yValues, int yValuesLen, double* xsToBeInterpolated, int xsDim){
	int i = 0;
	for(i = 0; i < xsDim ; ++i)
		yValuesInterp[i] = dInterp(xValues, xValuesLen, yValues, yValuesLen, xsToBeInterpolated[i]);
}
