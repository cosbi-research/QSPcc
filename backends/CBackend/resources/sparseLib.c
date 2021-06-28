#include<math.h>
#include<assert.h>
#include <string.h>
#include<stdlib.h>
#include "matrixLib.h"
#include "memoryLib.h"
#include "sparseLib.h"
#include "user-structures.h"
/* parallel slicing of columns */
#include <pthread.h>
#include "qsort-inline.h"

#define sparseMatrixCol1Sum(type) \
void type##SparseMatrixCol1Sum(type##2dMatrix result, type##SparseMatrix in, int in_dims){ \
	assert(false && "Not implemented, summarize by col");                              \
}

#ifdef INT2DMATRIX
sparseMatrixCol1Sum(int)
#endif
#ifdef DOUBLE2DMATRIX
sparseMatrixCol1Sum(double)
#endif

#define sparseMatrixCol2Sum(type) \
void type##SparseMatrixCol2Sum(type##2dMatrix result, type##SparseMatrix in, int in_dims){ \
	int r,c;                                                                           \
	memset(result.matrix, 0, result.dim1*result.dim2*sizeof(type));                    \
	for(int curRow = 0; curRow < in.dim1 ; ++curRow ){                                 \
		for(int curCol=in.rowIndex[curRow]-1; curCol < in.rowIndex[curRow+1]-1 ; ++curCol){         \
			/* summarizing by rows, the results are accumulated on cols */                      \
			result.matrix[curRow] = scalarSum(result.matrix[curRow], in.matrix[curCol], type);  \
		}                                                                                           \
	}                                                                                                   \
}

#ifdef INT2DMATRIX
sparseMatrixCol2Sum(int)
#endif
#ifdef DOUBLE2DMATRIX
sparseMatrixCol2Sum(double)
#endif

#define sparseMatrixCol1Min(type) \
void type##SparseMatrixCol1Min(type##2dMatrix result, type##SparseMatrix in, int in_dims){ \
	assert(false && "Not implemented, summarize by col");                              \
}

#ifdef INT2DMATRIX
sparseMatrixCol1Min(int)
#endif
#ifdef DOUBLE2DMATRIX
sparseMatrixCol1Min(double)
#endif

#define sparseMatrixCol2Min(type) \
void type##SparseMatrixCol2Min(type##2dMatrix result, type##SparseMatrix in, int in_dims){ \
	int r,c;                                                                           \
	type v,val;                                                                        \
	memset(result.matrix, 0, result.dim1*result.dim2*sizeof(type));                    \
	for(int curRow = 0; curRow < in.dim1 ; ++curRow ){                                 \
		for(int curCol=in.rowIndex[curRow]-1; curCol < in.rowIndex[curRow+1]-1 ; ++curCol){         \
			/* summarizing by rows, the results are accumulated on cols */                      \
			if( in.matrix[curCol] < result.matrix[curRow] )                                     \
				result.matrix[curRow] = in.matrix[curCol];                                  \
		}                                                                                           \
	}                                                                                                   \
}

#ifdef INT2DMATRIX
sparseMatrixCol2Min(int)
#endif
#ifdef DOUBLE2DMATRIX
sparseMatrixCol2Min(double)
#endif

#define sparseMatrixCol1Max(type) \
void type##SparseMatrixCol1Max(type##2dMatrix result, type##SparseMatrix in, int in_dims){ \
	assert(false && "Not implemented, summarize by col");                              \
}

#ifdef INT2DMATRIX
sparseMatrixCol1Max(int)
#endif
#ifdef DOUBLE2DMATRIX
sparseMatrixCol1Max(double)
#endif

#define sparseMatrixCol2Max(type) \
void type##SparseMatrixCol2Max(type##2dMatrix result, type##SparseMatrix in, int in_dims){ \
	int r,c;                                                                           \
	type v,val;                                                                        \
	memset(result.matrix, 0, result.dim1*result.dim2*sizeof(type));                    \
	for(int curRow = 0; curRow < in.dim1 ; ++curRow ){                                 \
		for(int curCol=in.rowIndex[curRow]-1; curCol < in.rowIndex[curRow+1]-1 ; ++curCol){         \
			/* summarizing by rows, the results are accumulated on cols */                      \
			if( in.matrix[curCol] > result.matrix[curRow] )                                     \
				result.matrix[curRow] = in.matrix[curCol];                                  \
		}                                                                                           \
	}                                                                                                   \
}

#ifdef INT2DMATRIX
sparseMatrixCol2Max(int)
#endif
#ifdef DOUBLE2DMATRIX
sparseMatrixCol2Max(double)
#endif

#define sparseMatrixCol1Mean(type) \
void type##SparseMatrixCol1Mean(type##2dMatrix result, type##SparseMatrix in, int in_dims){ \
	assert(false && "Not implemented, summarize by col");                              \
}

#ifdef INT2DMATRIX
sparseMatrixCol1Mean(int)
#endif
#ifdef DOUBLE2DMATRIX
sparseMatrixCol1Mean(double)
#endif

#define sparseMatrixCol2Mean(type) \
void type##SparseMatrixCol2Mean(type##2dMatrix result, type##SparseMatrix in, int in_dims){ \
	int r,c;                                                                            \
	type v;                                                                             \
	memset(result.matrix, 0, result.dim1*result.dim2*sizeof(type));                     \
	for(int curRow = 0; curRow < in.dim1 ; ++curRow ){                                 \
		for(int curCol=in.rowIndex[curRow]-1; curCol < in.rowIndex[curRow+1]-1 ; ++curCol){         \
			/* summarizing by rows, the results are accumulated on cols */                      \
			result.matrix[curRow] = scalarSum(result.matrix[curRow], in.matrix[curCol], type);  \
		}                                                                                           \
	}                                                                                                   \
	for(int i=0; i < result.dim1*result.dim2 ; ++i)                                     \
		result.matrix[i] = scalarDiv(result.matrix[i], in.dim2, type);              \
}

#ifdef INT2DMATRIX
sparseMatrixCol2Mean(int)
#endif
#ifdef DOUBLE2DMATRIX
sparseMatrixCol2Mean(double)
#endif

typedef struct {
	int row;
	int column;
	int value;
} _sort_i;

typedef struct {
	int row;
	int column;
	double value;
} _sort_d;

static inline int _i_comparator(const _sort_i *p, const _sort_i *q)  
{ 
    int lr = p->row; 
    int rr = q->row;  
    int lc = p->column; 
    int rc = q->column;  
    if(lr > rr)
	return 1;
    else if(lr < rr)
	return -1;
    else
	return (lc > rc) - (lc < rc);
} 

DEF_QSORT(_sort_i, _i_comparator)

static inline int _d_comparator(const _sort_d *p, const _sort_d *q)  
{ 
    int lr = p->row; 
    int rr = q->row;  
    int lc = p->column; 
    int rc = q->column;  
    if(lr > rr)
	return 1;
    else if(lr < rr)
	return -1;
    else
	return (lc > rc) - (lc < rc);
} 

DEF_QSORT(_sort_d, _d_comparator)

/**
 * init a sparse matrix in CSR3 format for compatibility with MKL pardiso and other functions
 * https://software.intel.com/en-us/mkl-developer-reference-c-sparse-blas-csr-matrix-storage-format#CSR3
 * rows: 1-based array of row position of the corresponding value in vals
 * columns: 1-base array of column position of the corresponding value in vals A(rows(i), columns(i)) = values(i)
 * RETURNS 1-based sparse matrix in CSR3 format
 */
#define initSparseMatrix(dtype, type, abs_fun) \
void type##InitSparseMatrix(type##SparseMatrix *H, int *rows, int *columns, void *values, char *vtype, int num_values, int n_rows, int n_cols){ \
	int r;                                                                                                                     \
	int curRow = 1;                                                                                                            \
	int oldIdx = 1;                                                                                                            \
	int i_max=n_rows;                                                                                                          \
        int j_max=n_cols;                                                                                                          \
                                                                                                                                   \
	_sort_##dtype smat[num_values];                                                                                            \
	int n_real_elems=0;                                                                                                        \
	/* it's important that the elements are ordered by row. sort them to make sure they are ordered */                         \
	for(int i=0 ; i < num_values ; ++i){                                                                                       \
		type curval = GARRAY(vtype, type, values, i);                                                                      \
		if(abs_fun(curval) < DOUBLE_EQUALITY_TOL)                                                                          \
			continue;                                                                                                  \
		/* skip zeroes */                                                                                                  \
		if(i_max < rows[i])                                                                                                \
			i_max = rows[i];                                                                                           \
		if(j_max < columns[i])                                                                                             \
			j_max = columns[i];                                                                                        \
		smat[n_real_elems].value = curval;                                                                                 \
		smat[n_real_elems].row = rows[i];                                                                                  \
		smat[n_real_elems++].column = columns[i];                                                                          \
	}                                                                                                                          \
                                                                                                                                   \
	QSORT(_sort_##dtype, _##dtype##_comparator) (smat, n_real_elems);                                                          \
	/* allocate */                                                                                                             \
	v##dtype##EnsureSparseCapacity(2, n_real_elems+1, H, i_max, j_max);                                                        \
                                                                                                                                   \
	int lastCol=-1;                                                                                                            \
	/* elements in the same row/same col are added together (same behaviour as MATLAB) */                                      \
	int real_mat_dim=0;                                                                                                        \
	for(r=0; r < n_real_elems; ++r){                                                                                           \
		if(lastCol == smat[r].column && curRow == smat[r].row){                                                            \
			H->matrix[real_mat_dim-1] += smat[r].value;                                                                \
		}else{                                                                                                             \
			if(real_mat_dim > 0 && abs_fun(H->matrix[real_mat_dim-1]) < DOUBLE_EQUALITY_TOL)                           \
				/* IF sum through then branch brougth the value to 0 (ex +1-1) */                                  \
				real_mat_dim--;                                                                                    \
			                                                                                                           \
			H->matrix[real_mat_dim] = smat[r].value;                                                                   \
			H->columns[real_mat_dim++] = smat[r].column;                                                               \
			/* set to curIdx eventual empty rows */                                                                    \
			if(curRow != smat[r].row){                                                                                 \
				H->rowIndex[curRow-1] = oldIdx;                                                                    \
				H->rowIndex[curRow] = real_mat_dim;                                                                \
				oldIdx = real_mat_dim;                                                                             \
				/* fill eventual empty rows with curidx */                                                         \
				for(int er=curRow+1; er < smat[r].row ; ++er)                                                      \
					H->rowIndex[er] = oldIdx;			                                           \
			}                                                                                                          \
			curRow = smat[r].row;                                                                                      \
			lastCol = smat[r].column;                                                                                  \
		}                                                                                                                  \
	}                                                                                                                          \
	/* save an extra position with 0. this is used so that sparseAccess has a way to index all the 0 elements */               \
	H->usedCells = real_mat_dim;                                                                                               \
	H->matrix[real_mat_dim] = 0;                                                                                               \
	/* set end of last row */                                                                                                  \
	H->rowIndex[curRow] = real_mat_dim;                                                                                        \
	/* fill eventually empty trailing rows */                                                                                  \
	for(r=curRow+1; r <= n_rows; ++r)                                                                                          \
	    H->rowIndex[r] = real_mat_dim;                                                                                         \
}

initSparseMatrix(d,double, fabs)
initSparseMatrix(i,int, abs)

#define initPlainSparseMatrix(type, abs_fun) \
void type##InitPlainSparseMatrix(QSPCC_INT **orowIndex, QSPCC_INT **ocolumns, type **ovalues, QSPCC_INT *ousedcells, int num_values, int n_rows){ \
	int r;                                                                                                                                      \
	int curRow = 1;                                                                                                                             \
	int oldIdx = 1;                                                                                                                             \
	int curIdx = 1;                                                                                                                             \
	QSPCC_INT *rows = *orowIndex;                                                                                                               \
	QSPCC_INT *columns = *ocolumns;                                                                                                             \
	type *values = *ovalues;                                                                                                                    \
                                                                                                                                                    \
	_sort_d smat[num_values];                                                                                                            \
	int n_real_elems=0;                                                                                                                         \
	/* inputs are ordered but oRowIndex/ocolumns contains row information of the original */                                                    \
	/* input matrix */	                                                                                                                    \
	for(int i=0 ; i < num_values ; ++i){                                                                                                        \
		if(abs_fun(values[i]) < DOUBLE_EQUALITY_TOL)                                                                                        \
			continue;                                                                                                                   \
		                                                                                                                                    \
		smat[n_real_elems].value = values[i];                                                                                               \
		smat[n_real_elems].row = (int) rows[i];                                                                                             \
		smat[n_real_elems++].column = (int) columns[i];                                                                                     \
	}                                                                                                                                           \
        /* TRUNCATE if rowIndex is too big */                                                                                                       \
	*orowIndex = (QSPCC_INT *) qspcc_realloc(*orowIndex, (n_rows+1) * sizeof(QSPCC_INT));                                                       \
	                                                                                                                                            \
	int lastCol=-1;                                                                                                                             \
	/* elements in the same row/same col are added together (same behaviour as MATLAB) */                                                       \
	int real_mat_dim=0;                                                                                                                         \
	for(r=0; r < n_real_elems; ++r){                                                                                                            \
		if(lastCol == smat[r].column && curRow == smat[r].row)                                                                              \
			(*ovalues)[real_mat_dim-1] += smat[r].value;                                                                                \
		else{                                                                                                                               \
			(*ovalues)[real_mat_dim] = smat[r].value;                                                                                   \
			(*ocolumns)[real_mat_dim++] = smat[r].column;                                                                               \
				                                                                                                                    \
			if(curRow != smat[r].row){                                                                                                  \
				(*orowIndex)[curRow-1] = oldIdx;                                                                                    \
				(*orowIndex)[curRow] = curIdx;                                                                                      \
				oldIdx = curIdx;                                                                                                    \
					                                                                                                            \
				for(int er=curRow+1; er < smat[r].row ; ++er)                                                                       \
					(*orowIndex)[er] = oldIdx;			                                                            \
			}                                                                                                                           \
			curIdx++;                                                                                                                   \
			curRow = smat[r].row;                                                                                                       \
			lastCol = smat[r].column;                                                                                                   \
		}                                                                                                                                   \
	}                                                                                                                                           \
	(*ousedcells) = real_mat_dim;                                                                                                               \
	(*ovalues)[real_mat_dim] = 0;                                                                                                               \
	(*orowIndex)[curRow] = curIdx;                                                                                                              \
	for(r=curRow+1; r <= n_rows; ++r)                                                                                                           \
		(*orowIndex)[r] = curIdx;                                                                                                           \
}

initPlainSparseMatrix(double, fabs);
initPlainSparseMatrix(int, abs);

#define initSparseScalarMatrix(type) \
void type##InitSparseScalarMatrix(type##SparseMatrix *H, int row, int column, void *value, char *vtype, int i_max, int j_max){     \
	H->usedCells = 1;                                                                                            \
	/* save an extra position with 0. this is used so that sparseAccess has a way to index all the 0 elements */ \
	H->matrix[H->usedCells]=0;                                                                                   \
	H->matrix[0] = GVALUE(vtype, type, value);                                                                   \
	H->columns[0] = column;                                                                                      \
	int r;                                                                                                       \
	for(r=0; r < row ; ++r)                                                                                      \
		H->rowIndex[r] = 1;                                                                                  \
	H->rowIndex[row]=2;                                                                                          \
	for(   ; r < i_max ; ++r)                                                                                    \
		H->rowIndex[r] = 2;                                                                                  \
}

initSparseScalarMatrix(double)
initSparseScalarMatrix(int)

/* transpose a 1-based CSR3 sparse matrix */
#define transposeSparse(type, dtype) \
void dtype##TransposeSparseMatrix(type##SparseMatrix *dest, type##SparseMatrix *src)                                  \
{                                                                                                                     \
	/* to transpose, re-create a triple struct with inverted indexes */                                           \
	_sort_##dtype smat[src->usedCells];                                                                           \
	int n_real_elems=0;                                                                                           \
	int curRow = 1;                                                                                               \
	int oldIdx = 1;                                                                                               \
	int curIdx = 1;                                                                                               \
	int r = 0;                                                                                                    \
	for(r=0; r < src->dim1; ++r)                                                                                  \
		for(int c=src->rowIndex[r]; c < src->rowIndex[r+1] ; ++c){                                            \
			/* invert row and col */                                                                      \
			smat[n_real_elems].row = src->columns[c-1];                                                   \
			smat[n_real_elems].column = r+1;                                                              \
			smat[n_real_elems++].value = src->matrix[c-1];                                                \
		}                                                                                                     \
                                                                                                                      \
	QSORT(_sort_##dtype, _##dtype##_comparator) (smat, n_real_elems);                                             \
	/* allocate */                                                                                                \
	v##dtype##EnsureSparseCapacity(2, n_real_elems+1, dest, src->dim2, src->dim1);                                \
	/* save an extra position with 0. this is used so that sparseAccess has a way to index all the 0 elements */  \
	dest->usedCells = n_real_elems+1;                                                                             \
	dest->matrix[n_real_elems] = 0;                                                                               \
	for(r=0; r < n_real_elems; ++r){                                                                              \
		dest->matrix[r] = smat[r].value;                                                                      \
		dest->columns[r] = smat[r].column;                                                                    \
		/* set to curIdx eventual empty rows */                                                               \
		if(curRow != smat[r].row){                                                                            \
			dest->rowIndex[curRow-1] = oldIdx;                                                            \
			dest->rowIndex[curRow] = curIdx;                                                              \
			oldIdx = curIdx;                                                                              \
			/* fill eventual empty rows with curidx */                                                    \
			for(int er=curRow+1; er < smat[r].row ; ++er)                                                 \
				dest->rowIndex[er] = oldIdx;			                                      \
		}                                                                                                     \
		curIdx++;                                                                                             \
		curRow = smat[r].row;                                                                                 \
	}                                                                                                             \
	/* set end of last row */                                                                                     \
	dest->rowIndex[curRow] = curIdx;                                                                              \
	/* fill eventually empty trailing rows */                                                                     \
	for(r=curRow+1; r <= src->dim1; ++r)                                                                          \
	    dest->rowIndex[r] = curIdx;                                                                               \
}

transposeSparse(double,d)
transposeSparse(int,i)

/* x = x + beta * A(i,:), where x is a dense vector and A(i,:) is sparse */
#define __sparseOp(dtype, type, operation, op) \
int dtype##Sparse##operation(const type##SparseMatrix *A, int i, int mark, double beta, int *w, type *x, int nz,    \
    type##SparseMatrix *C)									                    \
{														    \
    QSPCC_INT j, p;												    \
    QSPCC_INT *Ap, *Aj, *Cj;											    \
    type *Ax; 													    \
    Ap = A->rowIndex ; Aj = A->columns ; Ax = A->matrix ; Cj = C->columns;					    \
    for (p = Ap [i]-1 ; p < Ap [i+1]-1 ; p++)									    \
    {														    \
	j = Aj [p] - 1;				/* A(i,j) is nonzero */						    \
														    \
	if (w [j] < mark)											    \
	{													    \
	    w [j] = mark ;			/* i is new entry in column j */				    \
	    Cj [nz++] = j + 1;			/* add j to pattern of C(i,:) */				    \
	    x [j] = beta * Ax [p] ;	/* x(j) = beta*A(i,j) */						    \
	}													    \
	else x [j] op##= beta * Ax [p] ;	/* j exists in C(i,:) already */				    \
    }														    \
    return (nz) ;												    \
}

__sparseOp(d, double, Plus, +);
__sparseOp(i, int, Plus, +);

__sparseOp(d, double, Minus, -);
__sparseOp(i, int, Minus, -);

__sparseOp(d, double, Times, *);
__sparseOp(i, int, Times, *);

__sparseOp(d, double, Divide, /);
__sparseOp(i, int, Divide, /);

int cmpfunc(const void * a, const void * b) {
   return ( *(QSPCC_INT*)a - *(QSPCC_INT*)b );
}

/* non-mkl version */
#define __sparseSliceMatrix(dtype,type) \
void dtype##SparseSliceMatrix(type##SparseMatrix *dest, type##SparseMatrix *src, int *rows, int rows_len, int *cols, int col_len){  \
	QSPCC_INT nel = 0;                                                                                                          \
	QSPCC_INT cval;                                                                                                             \
	QSPCC_INT rowStart, rowEnd;                                                                                                 \
	int row, r;                                                                                                                 \
	dest->rowIndex[0] = nel+1;                                                                                                  \
                                                                                                                                    \
	if(src->dim2 == 1)                                                                                                          \
		for(r=0 ; r<rows_len ; ++r){                                                                                        \
			row = rows[r];		                                                                                    \
			rowStart = src->rowIndex[row-1]-1;                                                                          \
			rowEnd = src->rowIndex[row]-1;                                                                              \
			if(rowStart < rowEnd){                                                                                      \
				dest->columns[nel] = 1;                                                                             \
				dest->matrix[nel++] = src->matrix[rowStart];                                                        \
			}                                                                                                           \
			dest->rowIndex[r+1] = nel+1;                                                                                \
		}                                                                                                                   \
	else{                                                                                                                       \
		int cmap, start_pos;                                                                                                \
		QSPCC_INT curval;                                                                                                   \
		int curpos;                                                                                                         \
		int columns_map[src->dim2];                                                                                         \
                                                                                                                                    \
		/* generic case, search each column given in the sorted subportion of the array columns */                          \
		for(r=0 ; r<rows_len ; ++r){                                                                                        \
			row = rows[r];		                                                                                    \
			rowStart = src->rowIndex[row-1]-1;                                                                          \
			rowEnd = src->rowIndex[row]-1;                                                                              \
			if(rowStart < rowEnd){                                                                                      \
				start_pos = 0;                                                                                      \
				for(cmap = 0 ; cmap < rowEnd-rowStart ; ++cmap){                                                    \
					curpos = rowStart+cmap;                                                                     \
					curval = src->columns[curpos];                                                              \
					/* fill zeroes until curval */                                                              \
					if(curval - start_pos > 1)                                                                  \
						memset(columns_map + start_pos, -1, (curval - start_pos - 1)*sizeof(int));          \
					/* save in map the position of the column curval */                                         \
					columns_map[curval-1] = curpos;                                                             \
					start_pos = curval;                                                                         \
				}                                                                                                   \
				if(src->dim2 - start_pos > 1)                                                                       \
					memset(columns_map + start_pos, -1, (src->dim2 - start_pos)*sizeof(int));                   \
				/* there is at least a non-zero column in this row */                                               \
				for(int c=0; c<col_len; ++c){                                                                       \
					cval = cols[c];                                                                             \
					curpos = columns_map[cval-1];                                                               \
					if( curpos >= 0 ){                                                                          \
						assert(src->columns[curpos] == cval);                                               \
						dest->columns[nel] = c+1;                                                           \
						dest->matrix[nel++] = src->matrix[curpos];                                          \
					}                                                                                           \
				}                                                                                                   \
			}                                                                                                           \
			dest->rowIndex[r+1] = nel+1;                                                                                \
		}                                                                                                                   \
	}                                                                                                                           \
	dest->usedCells = nel+1;                                                                                                    \
}

__sparseSliceMatrix(d,double)
__sparseSliceMatrix(i,int)

/*
 * r = 0-based row index
 * c = 1-based column
 * returns c if column c has a value in target matrix, usedCells otherwise
 *         usedCells position contains always 0
 */
int sparseAccess(int r, int c, QSPCC_INT *rowIndex, QSPCC_INT *columns, QSPCC_INT usedCells){
	// check if this colon has a value in this row, otherwise return 0
	for(int ec=rowIndex[r]-1; ec < rowIndex[r+1]-1 ; ++ec){
		assert(ec < usedCells && "Out of bound access to sparse matrix");
		if(columns[ec] == c)
			return ec;
		else if(columns[ec] > c)
			/* take advantage of the fact that the columns are ordered */
			return usedCells;
	}
	// in last position we always store a 0, so that sparseaccess have a way to index all the 0 elements
	return usedCells;
}
