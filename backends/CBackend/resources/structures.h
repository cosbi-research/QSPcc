#ifndef __STRUCT__
#define __STRUCT__

#include <math.h>
#include <float.h>
#include<stdbool.h>


#define intNull -1
#define INT_IS_NULL(__x) (__x == intNull)

#define doubleNull NAN
#define DOUBLE_IS_NULL(__x) isnan(__x)


typedef struct {
	int start;
	int step;
	int end;
	int dim1;
	// linear or logarithmic slice
	bool linear;
} islice;

#define ISLICE_IS_NULL(x) (x.start == intNull && x.step == intNull && x.end == intNull && x.dim1 == intNull)

typedef struct {
	double start;
	double step;
	double end;
	int dim1;
	// linear or logarithmic slice
	bool linear;
} dslice;

#define DSLICE_IS_NULL(x) (x.start == doubleNull && x.step == doubleNull && x.end == doubleNull && x.dim1 == intNull)

#endif
