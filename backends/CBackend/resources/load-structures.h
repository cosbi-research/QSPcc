#ifndef __LOAD_LIB_STRUCT__
#define __LOAD_LIB_STRUCT__

#define COL_MAX_NUMBER	30
#define MAX_STRUCT_NUMBER 10

/**
 * Holds the return value of the function globalGetColumnId()
 * unique in this implementation
 */
typedef struct {
	double *values;
	int columnLenght;
} INPUT_COLUMN;

/*
* the CSV struct aims to store the content of a CSV file
* the first line of the csv is supposed to be the header, whereas other cells
* are its content
*/
typedef struct InputLoaded { 
    
     int isMat;
    /* csv values */
    double *values[COL_MAX_NUMBER];
	int values_len[COL_MAX_NUMBER];
    /* csv headears*/
    char *headers[COL_MAX_NUMBER];
    int headerLength;
    
    char *structName[MAX_STRUCT_NUMBER];
    struct InputLoaded *structLoaded[MAX_STRUCT_NUMBER];
    int structLenght;

	INPUT_COLUMN*(*getColumn)(const struct InputLoaded *inputLoaded, const char *headerName); 
} INPUT;

#endif
