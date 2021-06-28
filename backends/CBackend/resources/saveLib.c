#include <stdio.h>
#include <assert.h>
#include "saveLib.h"
#include "matrixLib.h"
#include <stdarg.h>

void siSave(const char *path, int val){
	FILE *f = fopen(path, "w");
	assert( f != NULL && "Error opening file");	

	fprintf(f, "%d\n", val);

	fclose(f);
}

void sdSave(const char *path, double val){
	FILE *f = fopen(path, "w");
	assert( f != NULL && "Error opening file");	

	fprintf(f, "%.17e\n", val);

	fclose(f);
}

void ssSave(const char *path, const char *val){
	FILE *f = fopen(path, "w");
	assert( f != NULL && "Error opening file");	

	fprintf(f, "%s\n", val);

	fclose(f);
}

// ---- PRINT ND matrices -------------------------
void viSaveSingleElement(FILE *f, int *s){
	fprintf(f, "%d,", *s);
}

// vals: matrix to be saved
// vals_dims: number of dimensions of the matrix
// ... will contain:
//           the dim1, .. , dimN of the matrix
void viSave(const char *path, int *vals, int *poly_basis, int vals_dims, ...){
	va_list vl;
	va_start(vl, vals_dims);

	int vals_dim[vals_dims];
	int access_numbers[vals_dims];
	int i;
	for(i = 0 ; i < vals_dims ; ++i ){
		vals_dim[i] = va_arg(vl,int);
		access_numbers[i] = 1; //start from 1
	}

	FILE *f = fopen(path, "w");
	assert( f != NULL && "Error opening file");	

	printMatrixAux(vals, sizeof(int), (void (*)(FILE *, char *)) viSaveSingleElement, vals_dim, poly_basis, vals_dims, access_numbers, 0, f);

	fclose(f);
}

void vdSaveSingleElement(FILE *f, double *s){
	fprintf(f, "%.17e,", *s);
}

// vals: matrix to be saved
// vals_dims: number of dimensions of the matrix
// ... will contain:
//           the dim1, .. , dimN of the matrix
void vdSave(const char *path, const char *mode, double *vals, int *poly_basis, int vals_dims, ...){
	va_list vl;
	va_start(vl, vals_dims);

	int vals_dim[vals_dims];
	int access_numbers[vals_dims];
	int i;
	for(i = 0 ; i < vals_dims ; ++i ){
		vals_dim[i] = va_arg(vl,int);
		access_numbers[i] = 1; //start from 1
	}

	
	FILE *f = fopen(path, mode);
	assert( f != NULL && "Error opening file");	

	printMatrixAux(vals, sizeof(double), (void (*)(FILE *, char *)) vdSaveSingleElement, vals_dim, poly_basis, vals_dims, access_numbers, 0, f);

	fclose(f);
}
// END
