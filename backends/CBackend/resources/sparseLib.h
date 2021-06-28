#ifndef __SPARSE_MAT_LIB__
#define __SPARSE_MAT_LIB__

#ifdef INTEL_MKL_VERSION
#define QSPCC_INT MKL_INT
#else
#define QSPCC_INT long long int
#endif

typedef struct {
	/* add fields typical of matrix */
        double *matrix;
        int *poly_basis;
        int __realsize;
        int dim1;
        int dim2;
	/* i-th element is the column (0-based) of the i-th element of matrix */
        QSPCC_INT *columns;
	/* j-th element is the row (0-based) of the j-th element of matrix */
        QSPCC_INT *rowIndex;
	/* howmany cells are actually used */
        QSPCC_INT usedCells;
} doubleSparseMatrix;

typedef struct {
	/* add fields typical of matrix */
        int *matrix;
        int *poly_basis;
        int __realsize;
        int dim1;
        int dim2;
	/* i-th element is the column (0-based) of the i-th element of matrix */
        QSPCC_INT *columns;
	/* j-th element is the row (0-based) of the j-th element of matrix */
        QSPCC_INT *rowIndex;
	/* howmany cells are actually used */
        QSPCC_INT usedCells;
} intSparseMatrix;


#ifdef INT2DMATRIX
extern void intSparseMatrixCol1Sum(int2dMatrix result, intSparseMatrix in, int in_dims);
extern void intSparseMatrixCol2Sum(int2dMatrix result, intSparseMatrix in, int in_dims);
extern void intSparseMatrixCol1Min(int2dMatrix result, intSparseMatrix in, int in_dims);
extern void intSparseMatrixCol2Min(int2dMatrix result, intSparseMatrix in, int in_dims);
extern void intSparseMatrixCol1Min(int2dMatrix result, intSparseMatrix in, int in_dims);
extern void intSparseMatrixCol2Min(int2dMatrix result, intSparseMatrix in, int in_dims);
extern void intSparseMatrixCol1Max(int2dMatrix result, intSparseMatrix in, int in_dims);
extern void intSparseMatrixCol2Max(int2dMatrix result, intSparseMatrix in, int in_dims);
extern void intSparseMatrixCol1Mean(int2dMatrix result, intSparseMatrix in, int in_dims);
extern void intSparseMatrixCol2Mean(int2dMatrix result, intSparseMatrix in, int in_dims);
#endif

#ifdef DOUBLE2DMATRIX
extern void doubleSparseMatrixCol1Sum(double2dMatrix result, doubleSparseMatrix in, int in_dims);
extern void doubleSparseMatrixCol2Sum(double2dMatrix result, doubleSparseMatrix in, int in_dims);
extern void doubleSparseMatrixCol1Min(double2dMatrix result, doubleSparseMatrix in, int in_dims);
extern void doubleSparseMatrixCol2Min(double2dMatrix result, doubleSparseMatrix in, int in_dims);
extern void doubleSparseMatrixCol1Min(double2dMatrix result, doubleSparseMatrix in, int in_dims);
extern void doubleSparseMatrixCol2Min(double2dMatrix result, doubleSparseMatrix in, int in_dims);
extern void doubleSparseMatrixCol1Max(double2dMatrix result, doubleSparseMatrix in, int in_dims);
extern void doubleSparseMatrixCol2Max(double2dMatrix result, doubleSparseMatrix in, int in_dims);
extern void doubleSparseMatrixCol1Mean(double2dMatrix result, doubleSparseMatrix in, int in_dims);
extern void doubleSparseMatrixCol2Mean(double2dMatrix result, doubleSparseMatrix in, int in_dims);

#endif

extern void intInitSparseScalarMatrix(intSparseMatrix *H, int row, int column, void *value, char *vtype, int i_max, int j_max);
extern void intInitSparseMatrix(intSparseMatrix *H, int *rows, int *columns, void *values, char *vtype, int num_values, int i_max, int j_max);
extern void intInitPlainSparseMatrix(QSPCC_INT **orowIndex, QSPCC_INT **ocolumns, int **ovalues, QSPCC_INT *ousedcells, int num_values, int n_rows);
extern void iTransposeSparseMatrix(intSparseMatrix *dest, intSparseMatrix *src);

extern void doubleInitSparseScalarMatrix(doubleSparseMatrix *H, int row, int column, void *value, char *vtype, int i_max, int j_max);
extern void doubleInitSparseMatrix(doubleSparseMatrix *H, int *rows, int *columns, void *values, char *vtype, int num_values, int i_max, int j_max);
extern void doubleInitPlainSparseMatrix(QSPCC_INT **orowIndex, QSPCC_INT **ocolumns, double **ovalues, QSPCC_INT *ousedcells, int num_values, int n_rows);
extern void dTransposeSparseMatrix(doubleSparseMatrix *dest, doubleSparseMatrix *src);

/* used for mixed sparse/dense pointwise matrix expressions */
extern int sparseAccess(int r, int c, QSPCC_INT *rowIndex, QSPCC_INT *columns, QSPCC_INT usedCells);

/* sparse expressions */
int dSparsePlus(const doubleSparseMatrix *A, int i, int mark, double beta, int *w, double *x, int nz, doubleSparseMatrix *C);
int iSparsePlus(const intSparseMatrix *A, int i, int mark, double beta, int *w, int *x, int nz, intSparseMatrix *C);

int dSparseMinus(const doubleSparseMatrix *A, int i, int mark, double beta, int *w, double *x, int nz, doubleSparseMatrix *C);
int iSparseMinus(const intSparseMatrix *A, int i, int mark, double beta, int *w, int *x, int nz, intSparseMatrix *C);

int dSparseTimes(const doubleSparseMatrix *A, int i, int mark, double beta, int *w, double *x, int nz, doubleSparseMatrix *C);
int iSparseTimes(const intSparseMatrix *A, int i, int mark, double beta, int *w, int *x, int nz, intSparseMatrix *C);

int dSparseDivide(const doubleSparseMatrix *A, int i, int mark, double beta, int *w, double *x, int nz, doubleSparseMatrix *C);
int iSparseDivide(const intSparseMatrix *A, int i, int mark, double beta, int *w, int *x, int nz, intSparseMatrix *C);

/* slice special cases: simple slice by vectors of a sparse matrix */
void dSparseSliceMatrix(doubleSparseMatrix *dest, doubleSparseMatrix *src, int *rows, int rows_len, int *cols, int col_len);
void iSparseSliceMatrix(intSparseMatrix *dest, intSparseMatrix *src, int *rows, int rows_len, int *cols, int col_len);

#endif
