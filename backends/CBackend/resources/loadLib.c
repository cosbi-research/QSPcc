#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <strings.h>
#include <assert.h>
#include "loadLib.h"
#include "memoryLib.h"
#include "user-structures.h"

/**
 * fake struct "method", it will search just in the current INPUT struct
 */
INPUT_COLUMN* getColumn(const INPUT *input, const char *headerName){
    
    INPUT_COLUMN* res = globalGetColumnIn(headerName, input);

	if(res == NULL){
		printf("header %s not found in input\n", headerName);
 		exit(1);
 		return NULL;
	}else{
		return res;
	}
}

/**
 * Global functions to search for data in all the INPUT structs
 */
INPUT_COLUMN* globalGetColumn(const char *headerName){
    int i,k;
	for(k=0 ; k < MAX_INPUT_LOADS ; ++k){
		INPUT_COLUMN* res = globalGetColumnIn(headerName, &inputs[k]);
		if(res!=NULL){
 			return res;
		} 
	}

	printf("header %s not found in inputs\n", headerName);
 	exit(1);
	return NULL;
}

/*
* find the column within the structure passed as argument
*/
INPUT_COLUMN* globalGetColumnIn(const char *headerName, const INPUT* input){
	for(int i=0; i < input->headerLength; i++){
	    	/* check if the header is the requested one*/
	    	if (strcmp(headerName, input->headers[i]) == 0){
				get_column_result.values = input->values[i];
				get_column_result.columnLenght = input->values_len[i];
	    		return &get_column_result;
			}
	}
	
	if(input->structLenght>0){
		for(int is = 0; is< input->structLenght; is++){
			INPUT_COLUMN* res = globalGetColumnIn(headerName, input->structLoaded[is]);
			if(res!=NULL){
				return res;
			}
		}
	}
	
	return NULL;
}

/*
* recovers an inner struct inside the loaded INPUTS
* this will be useful to load the input variables as "baseline.input_value"
* where baseline is the matlab struct name
*/
INPUT* globalGetStruct(const char *structName){
	int i,k;
	for(k=0 ; k < MAX_INPUT_LOADS ; ++k){
	    for(i=0; i < inputs[k].structLenght; i++){
	    	/* check if the header is the requested one*/
	    	if (strcmp(structName, inputs[k].structName[i]) == 0){
	    		return inputs[k].structLoaded[i];
			}else{
				INPUT* res = globalGetStructIn(structName, inputs[k].structLoaded[i]);
				if(res!=NULL){
					return res;
				}
			}
		}
	}

	printf("struct %s not found in input\n", structName);
 	exit(1);
	return NULL;
}

/*
* find the column within the structure passed as argument
*/
INPUT* globalGetStructIn(const char *structName, const INPUT* input){	
	if(input->structLenght>0){
		for(int is = 0; is< input->structLenght; is++){
			if(strcmp(input->structName[is], structName) == 0){
				return input->structLoaded[is];
			}else{	
				INPUT* res = globalGetStructIn(structName, input->structLoaded[is]);
				if(res!=NULL){
					return res;
				}
			}
		}
	}
	
	return NULL;
}


#ifdef DOUBLE2DMATRIX
/*
* load the csv file, you can then access directly the column contained as if they were arrays
* NOTE: assume double2dMatrix is defined!
*/
double2dMatrix *vdCsvLoad(const char* file, int fline, const char* funname, const char* filePath, int n_args, ...){	
	va_list vl;
	va_start(vl, n_args);
	
	double2dMatrix *ret = malloc(sizeof(double2dMatrix));
	ret->matrix=NULL;
	ret->dim1=0;
	ret->dim2=0;
	
	FILE* fp = fopen(filePath, "r");
	if(fp==NULL){
		printf("Unable to open the file '%s' as reported in the original source script.\n", filePath);
		printf("Please verify that the C file %s:%d in %s is referencing a valid csv file.\n",file, fline, funname);
		printf("You might want to use the mat2csv.m script?\n");
		exit(1);
	}
	
	if(n_args > 2){
		printf("Unable to open the file '%s' as reported in the original source script, too many arguments at %s:%d in %s.\n", filePath, file, fline, funname);
		exit(1);
	}

	int rowstart = 0;
	int colstart = 0;
	if(n_args > 0)
		rowstart = va_arg(vl,int);
	if(n_args > 1)
		colstart = va_arg(vl,int);

	/* buffered file read by line, puts the results in the tab matrix */
	char line[BUFFER_MAX_SIZE];
	int nrow;
	int ncol;
	int maxcol = 0;
   	char *val;
	int csvIdx=0;
    for (nrow = 0; (fgets(line, BUFFER_MAX_SIZE, fp) != NULL); nrow++){
		if( nrow < rowstart )
			continue;	
		assert( nrow < ROW_MAX_NUMBER && "Failed to load CSV, too many lines" );

		val = strtok(line, DELIMITS);
		/* remove trailing \r\n if any */
		val[strcspn(val, "\r\n")] = 0;
		if(val[0] == '\0')
			/* skip line */
			continue;
        
   		for (ncol = 0; val != NULL; ncol++){
			if( ncol < colstart )
				continue;
		val[strcspn(val, "\r\n")] = 0;
		if(val[0] != '\0'){
			if(strcasecmp(val, "nan") )
				sscanf(val, "%lf", &csvBuffer[csvIdx++]);
			else
				csvBuffer[csvIdx++]=NAN;
		}
    	val = strtok(NULL, DELIMITS);
    }
	
	if(maxcol < ncol)
		maxcol = ncol;
    }
    fclose(fp);

    // allocate double2dMatrix
    vdensureignoreCapacity(2, ret, nrow-rowstart, maxcol-colstart);
    memcpy(ret->matrix, csvBuffer, sizeof(double) * (ret->dim1) * (ret->dim2));
    return ret; 
}
#else
void *vdCsvLoad(const char* file, int fline, const char* funname, const char* filePath, int n_args, ...){	
	assert(0);
	return NULL;
}

#endif


