#ifndef __LOAD_LIB__
#define __LOAD_LIB__

#include <assert.h>
#include "user-structures.h"
#include "load-structures.h"

#define DELIMITS	" ,\t"
#define ROW_MAX_NUMBER	5000
#define MAX_INPUT_LOADS	5
#define BUFFER_MAX_SIZE	1024

static int input_number = 0;

// global (to this file) static table of data)
static INPUT inputs[MAX_INPUT_LOADS];

//ausiliary variables to keep track
static char tableHeaders[MAX_INPUT_LOADS][COL_MAX_NUMBER][BUFFER_MAX_SIZE];
static int tableColSize[MAX_INPUT_LOADS][COL_MAX_NUMBER];
static double tableCols[MAX_INPUT_LOADS][COL_MAX_NUMBER][ROW_MAX_NUMBER];
static char structNames[MAX_INPUT_LOADS][BUFFER_MAX_SIZE]; 

// temporary buffer for csv loads
static double csvBuffer[COL_MAX_NUMBER*ROW_MAX_NUMBER];


/*
* prototype of the function to load the CSV
*/
#define csvLoad(filepath, n_args, ...) vdCsvLoad(__FILE__,__LINE__,__func__,filepath, n_args , ##__VA_ARGS__)

#ifdef DOUBLE2DMATRIX
double2dMatrix *vdCsvLoad(const char* file, int fline, const char* funname, const char* filePath, int n_args, ...);
#else
void *vdCsvLoad(const char* file, int fline, const char* funname, const char* filePath, int n_args, ...);
#endif

INPUT_COLUMN *globalGetColumn(const char *headerName);
INPUT_COLUMN *globalGetColumnIn(const char *headerName, const INPUT*input);
INPUT* globalGetStruct(const char *structName);
INPUT* globalGetStructIn(const char *structName, const INPUT* input);
INPUT_COLUMN *getColumn(const INPUT *input, const char *headerName);

static INPUT_COLUMN get_column_result;

#endif
