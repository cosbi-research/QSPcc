//copy to resources/memoryLib.c and also memoryLib.h
// also code will be rewwritten by code in reusources
// tomasoni@cosbi.eu
#ifndef __USER_IMPLEMENTED_MEM_FUNCTIONS__
#define __USER_IMPLEMENTED_MEM_FUNCTIONS__
/*user impelmented struct functions*/
#ifndef __HEADERS__
#define __HEADERS__
#define ADD_BYTES(base_addr, num_bytes) (((char *)(base_addr)) + (num_bytes))
#define SUBTRACT_BYTES(base_addr, num_bytes) (((char *)(base_addr)) - (num_bytes))
typedef int bool;
#define true 1
#define false 0
// it was 37500000
#define SIZE 17500000
#define SLL_MAX_SIZE SIZE/10
#include <stdio.h>
#include <time.h>
#include <string.h>
#include <stdlib.h>
#include <math.h>
#include <assert.h>
#include <nvector/nvector_serial.h>
#include <sundials/sundials_types.h>
#include "loadLib.h"
#include "structures.h"
#include "user-structures.h"
#include "sundialsLib.h"
#include "memoryLib.h"
#include "matrixLib.h"
#include "sparseLib.h"
#include "saveLib.h"
#include "functions.h"
#endif
/*
static double big[SIZE];
static block_header_t header;
static bool init = false;
static bool stephen = false;
static bool tomasoni = false;
static unsigned int tracker = 0;
static int cc = 0;

///// ---- STATIC LINKED LIST IMPLEMENTATION ----
typedef struct _block {
	double *data;
	size_t size;
} block;

typedef struct _sllNode {
	block *b;
	struct _sllNode *next;
} sllNode;

static block blocks[SLL_MAX_SIZE];
//count the blocks currently inserted in the nodes map (aka the number of nodes in the sll structure)
static int _blocks_count = 0;
static sllNode _nodes_map[SLL_MAX_SIZE];
//pointer to the last element used in the sll nodes buffer
static int _nodes_map_count = 0;
// map block data -> sllNode
static sllNode *data2node[SIZE];

// UTILITY METHODS
block *_newBlock(){
	if(_blocks_count < SLL_MAX_SIZE )
		return &blocks[_blocks_count++];
	else
		// linear search of a free block [LOOP]
		for(int i = 0 ; i < SLL_MAX_SIZE ; ++i )
			if(blocks[i].data == NULL)
				// free block that I can use
				return &blocks[i];
	return NULL;
}

// --- SLL AND STATIC MEMORY HANDLING MAIN FUNCTIONS ----
void sll_rm(double *data){
	// get sslNode for this data pointer
	size_t loc = data - &big[0];
	sllNode *toremove = data2node[loc];

	if( data == &big[0] ){
		// no previous block because this is the start of the static memory,
		// toremove is the node to remove
		toremove->b->size = -1;
		toremove->b->data = NULL;
		toremove->b = NULL;
		toremove->next = NULL;

	}else if(toremove != NULL){
		// toremove->next is the block to remove
		// ---- remove from linked list
		sllNode *prev = toremove;
		toremove = prev->next;
		assert(toremove->b->data == data && "Corrupted map data2node");
		sllNode *newnext = toremove->next;
		prev->next = newnext;
		if(newnext != NULL){
			// update newnext data2node loc
			// to contain his new previous node
			size_t newloc = newnext->b->data - &big[0];
			data2node[newloc] = prev;
		}	
		// remove used block
		toremove->b->size = -1;
		toremove->b->data = NULL;
		toremove->b = NULL;
		toremove->next = NULL;
	}
	// remove from map
	data2node[loc] = NULL;
}

double *sll_in(size_t size){
	block *b = NULL;
	// new ssl node to use, and his predecessor
	sllNode *n = NULL, *nprev = NULL;
	// last used ssl node in list
	sllNode *lastnode = NULL;

	size_t size_between_blocks;
	unsigned newnode = 0;
	// first guess for lastnode
	if( _nodes_map_count > 0 && _nodes_map[_nodes_map_count-1].next == NULL )
		lastnode = &_nodes_map[_nodes_map_count-1];
	// first fit implementation [LOOP]
	// and ssl block allocation alltogether
	for(int i = 0; (b == NULL || lastnode == NULL) && (i < _nodes_map_count)  ; ++i){
		// deleted node
		if(_nodes_map[i].b == NULL){
			// a deleted node can be used again
			if( n == NULL )
				n = &_nodes_map[i];
		}else{
			// remember last ssl node used
			if( _nodes_map[i].next == NULL )
				lastnode = &_nodes_map[i];
			// if there is space between allocations
			else if(b == NULL && (_nodes_map[i].b->data + _nodes_map[i].b->size) < _nodes_map[i].next->b->data){
				// check if there is enough room
				size_between_blocks = _nodes_map[i].next->b->data - (_nodes_map[i].b->data + _nodes_map[i].b->size);
				if(size_between_blocks >= size){
					// get new block
					b = _newBlock();
					if( b == NULL )
						return NULL;
					// get new ssl node if needed
					if( n == NULL ){
						n = &_nodes_map[_nodes_map_count];
						newnode = 1;
					}
					// set start point and size
					// start point
					b->data = _nodes_map[i].b->data + _nodes_map[i].b->size;
					// size
					b->size = size;
					//  ---- insert in linked list
					// save n predecessor
					nprev = &_nodes_map[i];
					// save previous next pointer of prev node in linked list			
					sllNode *tmp = _nodes_map[i].next;
					// update next pointer of prev node in linked list to point to new node
					_nodes_map[i].next = n;
					// update new node next pointer to the node previously pointed by prev node
					n->next = tmp;
					if(tmp != NULL){
						// update n->next data2node loc
						// to contain his new previous node
						size_t newloc = tmp->b->data - &big[0];
						data2node[newloc] = n;
					}
					// this new node points to the block found
					n->b = b;
				}
			}
		}
	}

	if(b == NULL){
		// no space found between allocations, get a new one	
		b = _newBlock();
		if( b == NULL )
			return NULL;

		// allocate size 
		b->size = size;
		if( n == NULL ){
			// NO DELETED NODE FOUND
			n = &_nodes_map[_nodes_map_count];
			newnode = 1;
		}
		// set data
		n->b = b;
		// this is the new last node
		n->next = NULL;
		if(lastnode != NULL){
			// save n predecessor
			nprev = lastnode;
			// old last node has a new next
			lastnode->next = n;
			// where this data starts?
			b->data = lastnode->b->data + lastnode->b->size;
		}else
			// if no old lastnode, this is the only used block
			// start from the beginning
			b->data = &big[0];	
	}
	
	// remember mapping data start -> sslNode
	unsigned loc = b->data - &big[0];
	// nprev here is null -> b->data = &big (data points to the start location of the static memory)
	assert(nprev != NULL || b->data == &big[0]);
	if( nprev == NULL )
		data2node[loc] = n;
	else
		data2node[loc] = nprev;

	// new node inserted in sll
	_nodes_map_count += newnode;
	return b->data;
}

///// ---- END ----------------------------------
*/

int get_index(size_t size)  {
    if(size > 0 && size <= 64){
        return 0;
    }

    else if(size > 64 && size <= 128){
        return 1;
    }

    else if(size > 128 && size <= 256){
        return 2;
    }

    else if(size > 256 && size <= 512){
        return 3;

    }
    else if(size > 512 && size <= 1024){
        return 4;
    }
    else if(size > 1024 && size <= 2048){
        return 5;
    }
    else if(size > 2048 && size <= 4096){
        return 6;
    }
    else if(size > 4096 && size <=8192){
        return 7;
    }
    else if(size > 8192 && size <= 16384){
        return 8;
    }
    else if(size > 16384 && size <= 32768){
        return 9;
    }
    else if(size >32768 && size <= 65536){
        return 10;
    }
    else if(size > 65536 && size <= 131072){
        return 11;
    }
    else if(size > 131072 && size <= 262144){
        return 12;
    }
    else{
        return 13;
    }

}

/*
void init_metadata(){
    header.size = SIZE;
    block_t *head = (block_t *)big;
    void* new_loc = ADD_BYTES(big, SIZE);
    new_loc = SUBTRACT_BYTES(new_loc, sizeof(blocktail_t));
    blocktail_t *tail = (blocktail_t *)new_loc;
    
    tail->use = 0;
    tail->location = big;
    
    head->use = 0;
    head->size = SIZE;
    head->next = NULL;
    head->prev = NULL;
    header.head = (void*)head;
    
}
*/

/* OLD VERSION KEPT FOR REFERENCE
    if(stephen && init == false){
        init_metadata();
        init = true;
    }
    
    
    genMatrix* mat = (genMatrix *)toBeModified;
    int existingDimension = mat->__realsize;
    
    if(totalDimension > 0){
	    if(mat->matrix==NULL){
		int size = (totalDimension ) * sizeof(double);
		
		if(stephen || tomasoni){
		    if(stephen)
			    mat->matrix = (double *)mem_alloc(big, size);
		    else if(tomasoni)
			    mat->matrix = sll_in(size);
		    if(mat->matrix == NULL){
		        stephen = false; tomasoni = false;
		        mat->matrix = (double *) tc_malloc(size);
		    }
		}
		else{

		    mat->matrix= (double  *)  tc_malloc(size);
		    
		}
		// update real total size (may be different from dim1*...*dimN)
		mat->__realsize = size;
		// update dimN
	  	update_matrix_dims(toBeModified,temp,ndims);
		
	    }else if(totalDimension != existingDimension ){
		int size = (totalDimension ) * sizeof(double);
		size_t oldsize = existingDimension* sizeof(double);
		double *newmatrix;

		if(stephen || tomasoni){
		    if(stephen){
			newmatrix = (double *)mem_alloc(big, size);
			if(newmatrix != NULL){
				// copy back tmp to matrix
				if( oldsize < size )
					memcpy(newmatrix, mat->matrix, oldsize);
				else
					// size < oldsize
					memcpy(newmatrix, mat->matrix, size);
			}else{
				stephen = false; tomasoni = false;
				newmatrix= (double *) tc_malloc(size);
				// copy back tmp to matrix
				if( oldsize < size )
					memcpy(newmatrix, mat->matrix, oldsize);
				else
					// size < oldsize
					memcpy(newmatrix, mat->matrix, size);
			}

			mem_free(big, mat->matrix);
		    }else if(tomasoni){
			newmatrix = sll_in(size);		
			if(newmatrix != NULL){
				// copy back tmp to matrix
				if( oldsize < size )
					memcpy(newmatrix, mat->matrix, oldsize);
				else
					// size < oldsize
					memcpy(newmatrix, mat->matrix, size);
			}else{
				stephen = false; tomasoni = false;
				newmatrix= (double *) tc_malloc(size);
				// copy back tmp to matrix
				if( oldsize < size )
					memcpy(newmatrix, mat->matrix, oldsize);
				else
					// size < oldsize
					memcpy(newmatrix, mat->matrix, size);
			}
			sll_rm(mat->matrix);
		    }
		}
		else{
		    if( oldsize < size ){
			// forced to allocate
	    	        newmatrix= (double *) tc_malloc(size);
			memcpy(newmatrix, mat->matrix, oldsize);
		        tc_free(mat->matrix);
			mat->__realsize = size;
		    }else{
			// reuse same memory
			newmatrix = mat->matrix;
		    }
		
		}

	        mat->matrix = newmatrix;	

		// update dimN
	  	update_matrix_dims(toBeModified,temp,ndims);
	    }
    }else if(existingDimension == -1)
            // update dimN only (without allocating)
	    update_matrix_dims(toBeModified,temp,ndims);

*/

void sparse_memcpy_inplace(void *matrix, int *newdims, int *olddims, int ndims, int totalOldDimension, int totalNewDimension, size_t size){
	char* output = (char *) matrix;
	unsigned dimsProduct=1;
	// how many zeros to be added
	unsigned howManyZeros;
	// number of elements correctly copied until now
	unsigned startEvery;
	unsigned totalDim;
	unsigned dimsUntilNow;
	unsigned curdim;
	/* base case */
	if(ndims == 1)
		return;


	#ifdef ROWMAJOR
	// row and col are inverted, 
	// work first on row (dim2), then on col (dim1)
	int dstart = 2;
	// dim1 = number of rows, dim2 = number of cols
	// work first on number of cols because we are rowmajor
	if(newdims[1] > olddims[1]){
		totalDim = min(totalOldDimension, totalNewDimension);
		howManyZeros = newdims[1] - olddims[1];
		startEvery = olddims[1];
		// every 'startEvery' copy from oldmatrix and leave room for 'howManyZeros'
		for( int k=startEvery ; k + howManyZeros <= totalDim ; k += startEvery + howManyZeros ){
			// make room for zeroes
			memmove(output + (k + howManyZeros)*size, output + k*size, (totalNewDimension - howManyZeros - k)*size);
			// write zeros
			memset(output + k*size, 0, howManyZeros*size);
		}
		// update "old" dimension
		if(olddims[1]!=0)
			totalOldDimension = totalOldDimension/olddims[1] * newdims[1];
	}
	
	curdim = max( newdims[1], olddims[1]);
	dimsProduct = curdim;

	if( newdims[0] > olddims[0] ){
		totalDim = min(totalOldDimension, totalNewDimension);
		howManyZeros = curdim*(newdims[0]-olddims[0]);
		// use newdims[1] in place of olddims[1] because maybe also dims[1] changed		
		startEvery = (curdim-1) + (olddims[0]-1)*curdim + 1;
		// every 'startEvery' copy from oldmatrix and leave room for 'howManyZeros'
		for( int k=startEvery ; k + howManyZeros <= totalDim ; k += startEvery + howManyZeros ){
			// make room for zeroes
			memmove(output + (k + howManyZeros)*size, output + k*size, (totalNewDimension - howManyZeros -k)*size);
			// write zeros
			memset(output + k*size, 0, howManyZeros*size);
		}
		// update "old" dimension
		if(olddims[0]!=0)
			totalOldDimension = totalOldDimension/olddims[0] * newdims[0];
	}
	curdim =  max( newdims[0], olddims[0]);
	dimsProduct *= curdim;
	#else
	// directly work in order
	int dstart = 0;
	#endif

	for(int d = dstart ; d < ndims ; ++d){
		if(newdims[d] > olddims[d]){
			totalDim = min(totalOldDimension, totalNewDimension);
			howManyZeros = dimsProduct * (newdims[d] - olddims[d]);
			startEvery = 0;
			dimsUntilNow = 1;
			for( int j = 0 ; j < d ; ++j ){
				curdim = max(olddims[j], newdims[j]);
				startEvery += (curdim-1)*dimsUntilNow;
				dimsUntilNow *= curdim;
			}
			// make d-th dimension with old dims (because d-th dimension is still unchanged)
			startEvery += (olddims[d]-1)*dimsUntilNow;
			/*dimsUntilNow *= olddims[d];*/
			
			// number of elements to copy
			startEvery += 1;
			// every 'startEvery' copy from oldmatrix and leave room for 'howManyZeros'
			for( int k=startEvery ; k + howManyZeros <= totalDim ; k += startEvery + howManyZeros ){
				// make room for zeroes
				memmove(output + (k + howManyZeros)*size, output + k*size, (totalNewDimension - howManyZeros -k)*size);
				// write zeros
				memset(output + k*size, 0, howManyZeros*size);
			}
			// update "old" dimension
			if(olddims[d]!=0)
				totalOldDimension = totalOldDimension/olddims[d] * newdims[d];
			// assume just one dimension at a time can change for efficiency
			// done = 1;			
			// break;
		}
		curdim =  max( newdims[d], olddims[d]);
		dimsProduct *= curdim;				
	}
}

/**
 * copy from old matrix to new matrix. this function takes care of copying in the right position
 * since if a dimension changes, the position of the elements in the flat array should also change.
 * for example a 2x2 matrix that becomes a 3x2 matrix (in colmajor) should "make room"
 * for an extra position every 2 elements 1 2 3 4 -> 1 2 0 3 4 0 
 *
 * note: this will change olddims
 * note2: this will assume at least one dimension can change at a time, and the updated dimension should be higher 
 */
void sparse_memcpy(void *newmatrix, void *oldmatrix, int *newdims, int *olddims, int ndims, int totalOldDimension, int totalNewDimension, size_t size){
	// number of elements correctly copied until now
	unsigned startEvery;
	// at every d loop signs the position until we are done adding zeroes
	unsigned startFrom;
	// how many zeros to be added
	unsigned howManyZeros;
	// product of dims until now
	unsigned dimsProduct=1;
	unsigned dimsUntilNow;
	unsigned curdim;

	char *input = (char *) oldmatrix;
	char *output = (char *) newmatrix;

	if(ndims == 1){
		/* just a single memcpy is sufficient */
		memcpy(output , input, totalOldDimension*size);
		/* write zeros */
		memset(output + totalOldDimension*size, 0, (totalNewDimension-totalOldDimension)*size);
		return;
	}

	#ifdef ROWMAJOR
	// row and col are inverted, 
	// work first on row (dim2), then on col (dim1)
	int dstart = 2;
	// dim1 = number of rows, dim2 = number of cols
	// work first on number of cols because we are rowmajor
	if(newdims[1] > olddims[1]){
		howManyZeros = newdims[1] - olddims[1];
		startEvery = olddims[1];
		// every 'startEvery' copy from oldmatrix and leave room for 'howManyZeros'
		for( unsigned newMatrixPos = 0, k = 0 ; newMatrixPos + startEvery + howManyZeros <= totalNewDimension && k + startEvery <= totalOldDimension ; k += startEvery, newMatrixPos += startEvery + howManyZeros ){
			memcpy(output + newMatrixPos*size , input + k*size, startEvery*size);
			// write zeros
			memset(output + (newMatrixPos + startEvery)*size, 0, howManyZeros*size);
		}
		// update "old" dimension
		if(olddims[1]!=0)
			totalOldDimension = totalOldDimension/olddims[1] * newdims[1];
	}

	// use the greater between old cols (olddims[1]) and new cols (newdims[1])
	// because otherwise howManyZeros and startEvery won't have the correct value and won't behave like matlab
	curdim = max( newdims[1], olddims[1]);
	dimsProduct = curdim;

	if( newdims[0] > olddims[0] ){
		howManyZeros = dimsProduct * (newdims[0]-olddims[0]);
		startEvery = (curdim-1) + (olddims[0]-1)*curdim + 1;
		// every 'startEvery' copy from oldmatrix and leave room for 'howManyZeros'
		for( unsigned newMatrixPos = 0, k = 0 ; newMatrixPos + startEvery + howManyZeros <= totalNewDimension && k + startEvery <= totalOldDimension ; k += startEvery, newMatrixPos += startEvery + howManyZeros ){
			memcpy(output + newMatrixPos*size , input + k*size, startEvery*size);
			// write zeros
			memset(output + (newMatrixPos + startEvery)*size, 0, howManyZeros*size);
		}
		// update "old" dimension
		if(olddims[0]!=0)
			totalOldDimension = totalOldDimension/olddims[0] * newdims[0];
	}
	curdim =  max( newdims[0], olddims[0]);
	dimsProduct *= curdim;
	#else
	// directly work in order
	int dstart = 0;
	#endif

	for(int d = dstart ; d < ndims ; ++d){
		if(newdims[d] > olddims[d]){
			howManyZeros = dimsProduct * (newdims[d] - olddims[d]);
			startEvery = 0;
			dimsUntilNow = 1;
			for( int j = 0 ; j < d ; ++j ){
				curdim = max(olddims[j], newdims[j]);
				startEvery += (curdim-1)*dimsUntilNow;
				dimsUntilNow *= curdim;
			}
			// make d-th dimension with old dims (because d-th dimension is still unchanged)
			startEvery += (olddims[d]-1)*dimsUntilNow;
			/* dimsUntilNow *= olddims[d]; */
			
			// number of elements to copy
			startEvery += 1;
			// every 'startEvery' copy from oldmatrix and leave room for 'howManyZeros'
			for( unsigned newMatrixPos = 0, k = 0 ; newMatrixPos + startEvery + howManyZeros <= totalNewDimension && k + startEvery <= totalOldDimension ; k += startEvery, newMatrixPos += startEvery + howManyZeros ){
				memcpy(output + newMatrixPos*size , input + k*size, startEvery*size);
				// write zeros
				memset(output + (newMatrixPos + startEvery)*size, 0, howManyZeros*size);
			}
			// update "old" dimension
			if(olddims[d]!=0)
				totalOldDimension = totalOldDimension/olddims[d] * newdims[d];
			// assume just one dimension at a time can change for efficiency
			// done = 1;			
			// break;
		}
		curdim =  max( newdims[d], olddims[d]);
		dimsProduct *= curdim;		
	}
	
}

inline void computePolyBasis(int *poly_basis, int *dims, int ndims){
	if(ndims == 1)
		poly_basis[0] = 1;
	else if(ndims == 2 && (dims[0] == 1 || dims[1] == 1)){
		/* still 1D */
		poly_basis[0] = 1;
		poly_basis[1] = 1;
	}else{
#ifdef ROWMAJOR
		poly_basis[0] = dims[1];
		poly_basis[1] = 1;
#else
		poly_basis[0] = 1;
		poly_basis[1] = dims[0];
#endif
		int multiplier = dims[0]*dims[1];
		for(int n=2; n < ndims; ++n){
			poly_basis[n] = multiplier;
			multiplier *= dims[n];
		}
	}	
}

// a matrix access can be viewed as the scalar product between the access dimensions array (0-based)
// and the poly_basis array (1 dim1 dim1*dim2 in colmajor, dim2 1 dim1*dim2 in rowmajor)
// poly_basis can be interpreted as the geometrical base of the space spawned by the matrix
static inline void dComputePolyBasis(dgenMatrix *mat, int *dims, int ndims){
	computePolyBasis(mat->poly_basis, dims, ndims);
}

static inline void iComputePolyBasis(igenMatrix *mat, int *dims, int ndims){
	computePolyBasis(mat->poly_basis, dims, ndims);
}

/**
 * Sparse matrices, (update also sparse matrices if you change here)
*/
#define internalEnsureSparseCapacity(dtype, type) \
void v##dtype##InternalEnsureSparseCapacity(int ndims, int dimToAllocate, void* toBeModified, int *temp, int totalDimension, bool enlarge_only){   \
    type##SparseMatrix *mat = (type##SparseMatrix *) toBeModified;                                                \
    int existingDimension = mat->__realsize;                                                                      \
    if(totalDimension > 0){                                                                                       \
	    if(mat->matrix==NULL){                                                                                \
		if(dimToAllocate < 0)                                                                             \
			dimToAllocate = min((int)(totalDimension*0.1), MAX_SPARSE_INITIAL_DIMENSION);             \
		/* by default allocate 10% of total dimension */                                                  \
		int cells = (dimToAllocate + EXTRA_SAFETY_MEMORY);                                                \
		mat->matrix= (type  *)  qspcc_malloc(cells * sizeof(type));                                       \
		mat->columns= (QSPCC_INT  *)  qspcc_malloc(cells * sizeof(QSPCC_INT));                            \
		/* allocate directly max dimension for rowIndex because it will be used (see CSR specs) */        \
		mat->rowIndex= (QSPCC_INT  *)  qspcc_malloc((temp[0]+1) * sizeof(QSPCC_INT));                     \
		/* update real total size (may be different from dim1*...*dimN) */                                \
		mat->__realsize = cells;                                                                          \
		mat->usedCells = 0;                                                                               \
	        /* update dims (only if greater than previous value) */                                           \
		force_update_matrix_dims(toBeModified,temp,ndims);                                                \
	    }else{                                                                                                \
      	        /* update dims (only if greater than previous value) */                                           \
		if(enlarge_only){                                                                                 \
			update_matrix_dims(toBeModified,temp,ndims);                                              \
		}else{                                                                                            \
			force_update_matrix_dims(toBeModified,temp,ndims);                                        \
		}                                                                                                 \
		int newDimension = update_matrix_mul(toBeModified, ndims);                                        \
		if(dimToAllocate < 0)                                                                             \
			dimToAllocate = min((int)(newDimension*0.1), MAX_SPARSE_INITIAL_DIMENSION);               \
                                                                                                                  \
		/* this is a check in a loop, so if newdim>realdims we really need to enlarge the arrays */       \
		if(dimToAllocate > existingDimension){                                                            \
			if(dimToAllocate < 0)                                                                     \
				dimToAllocate = newDimension;                                                     \
			int newSize = dimToAllocate + EXTRA_SAFETY_MEMORY;                                        \
			/* realloc matrix */                                                                      \
			mat->matrix = (type *) qspcc_realloc(mat->matrix, newSize * sizeof(type));                \
			/* realloc columns */                                                                     \
			mat->columns = (QSPCC_INT *) qspcc_realloc(mat->columns, newSize * sizeof(QSPCC_INT));    \
			/* realloc rows */                                                                        \
			mat->rowIndex = (QSPCC_INT *) qspcc_realloc(mat->rowIndex, (temp[0]+1) * sizeof(QSPCC_INT)); \
			/* change size */                                                                         \
			mat->__realsize = newSize;                                                                \
		}/*else{ reuse the same memory }*/                                                                \
	    }                                                                                                     \
    }else if(totalDimension == 0)                                                                                 \
            /* update dimN only (without allocating) */                                                           \
	    force_update_matrix_dims(toBeModified,temp,ndims);                                                    \
    else                                                                                                          \
	    assert(false && "Trying to set matrix dimension to a negative value, stop.");                         \
}

internalEnsureSparseCapacity(d,double)
internalEnsureSparseCapacity(i,int)

#define HANDLE_MATRIX_DIMS(enlarge_only) HANDLE_MATRIX_DIMS_##enlarge_only
#define HANDLE_MATRIX_DIMS_ensure force_update_matrix_dims(toBeModified,temp,ndims)
#define HANDLE_MATRIX_DIMS_check update_matrix_dims(toBeModified,temp,ndims)

#define HANDLE_SPARSE_MEMCPY(keep_values, type) HANDLE_SPARSE_MEMCPY_##keep_values(type)
#define HANDLE_SPARSE_MEMCPY_keep(type) sparse_memcpy(newmatrix, mat->matrix, newdims, olddims, ndims, existingDimension, newDimension, sizeof(type))
#define HANDLE_SPARSE_MEMCPY_ignore(type) 

#define HANDLE_SPARSE_MEMCPY_INPLACE(keep_values, enlarge_only, type) HANDLE_SPARSE_MEMCPY_INPLACE_##keep_values(enlarge_only, type)
#define HANDLE_SPARSE_MEMCPY_INPLACE_keep(enlarge_only, type) HANDLE_SPARSE_MEMCPY_INPLACE_keep_##enlarge_only(type)
#define HANDLE_SPARSE_MEMCPY_INPLACE_keep_ensure(type) sparse_memcpy_inplace(mat->matrix, newdims, olddims, ndims, existingDimension, newDimension, sizeof(type))
#define HANDLE_SPARSE_MEMCPY_INPLACE_keep_check(type) if(newDimension > existingDimension) sparse_memcpy_inplace(mat->matrix, newdims, olddims, ndims, existingDimension, newDimension, sizeof(type))
#define HANDLE_SPARSE_MEMCPY_INPLACE_ignore(enlarge_only, type) 

#define ensureCapacity(dtype, type, enlarge_only, keep_values) \
void v##dtype##enlarge_only##keep_values##Capacity(int ndims, void* toBeModified, ...){ \
                                                                   \
    va_list vl;                                                    \
                                                                   \
    va_start(vl,toBeModified);                                     \
                                                                   \
    int totalDimension = 1;                                        \
    int temp[ndims];                                               \
    for(int dimCounter = 0; dimCounter <ndims;dimCounter++){       \
        temp[dimCounter]=va_arg(vl, int);                          \
        totalDimension*=temp[dimCounter];                          \
    }                                                              \
    /* enlarge only = false, this can lower the matrix dimension*/ \
                                                                                                                          \
    dtype##genMatrix* mat = (dtype##genMatrix *)toBeModified;                                                             \
    int existingDimension = update_matrix_mul(toBeModified, ndims);                                                       \
    int realDimension = mat->__realsize;                                                                                  \
    if(totalDimension > 0){                                                                                               \
	    if(mat->matrix==NULL){                                                                                        \
		size_t size = (totalDimension + EXTRA_SAFETY_MEMORY) * sizeof(type); 	                                  \
		mat->matrix= (type  *)  qspcc_malloc(size);                                                               \
		mat->poly_basis = (int *) qspcc_malloc( ndims * sizeof(int) );                                            \
		/* update real total size (may be different from dim1*...*dimN) */                                        \
		mat->__realsize = totalDimension + EXTRA_SAFETY_MEMORY;                                                   \
	        /* update dims (only if greater than previous value) */                                                   \
		force_update_matrix_dims(toBeModified,temp,ndims);                                                        \
		/* compute poly_basis once for all, */                                                                    \
		/* so that matrixAccess reduces to a dot product between vectors */                                       \
		dtype##ComputePolyBasis(mat, temp, ndims);                                                                \
	    }else{                                                                                                        \
 		int olddims[ndims];                                                                                       \
      	        /* fill olddims with dimensions of toBeModified matrix */                                                 \
		get_matrix_dims(toBeModified, olddims, ndims);                                                            \
		/* get old and new dims needed to copy the old matrix to the new one */                                   \
		int newdims[ndims];                                                                                       \
                                                                                                                          \
		if(totalDimension != existingDimension ){                                                                 \
			type *newmatrix;                                                                                  \
	 		/* update dims (only if greater than previous value) */                                           \
			HANDLE_MATRIX_DIMS(enlarge_only);                                                                 \
			/* fill newdims with dimensions of toBeModified matrix */                                         \
			get_matrix_dims(toBeModified, newdims, ndims);                                                    \
			/* re-compute poly_basis  */                                                                      \
			/* so that matrixAccess reduces to a dot product between vectors */                               \
			dtype##ComputePolyBasis(mat, newdims, ndims);                                                     \
			int newDimension = update_matrix_mul(toBeModified, ndims);                                        \
		                                                                                                          \
			if( newDimension > realDimension ){                                                               \
			  int newSize = newDimension + EXTRA_SAFETY_MEMORY;                                               \
			  size_t size = newSize * sizeof(type);                                                           \
			  /* forced to allocate */                                                                        \
		    	  newmatrix= (type *) qspcc_malloc(size);                                                         \
		                                                                                                          \
			  /* we can't memcpy because the layout of the matrix changed in first,second,third... dimension (even more than one at the same time) */  \
			  HANDLE_SPARSE_MEMCPY(keep_values, type);                                                                                                 \
			  qspcc_free(mat->matrix);                                                                                                                 \
			  mat->__realsize = newSize;                                                                                                               \
			  mat->matrix = newmatrix;	                                                                  \
			}else{                                                                                            \
			  /* even if reuse the same memory, still we need to relayout */                                  \
			  HANDLE_SPARSE_MEMCPY_INPLACE(keep_values, enlarge_only, type);                                  \
			  /* reuse same memory */                                                                         \
			}                                                                                                 \
		}else{                                                                                                    \
			/* update dimN only (without allocating) */                                                       \
			force_update_matrix_dims(toBeModified,temp,ndims);                                                \
			/* fill newdims with dimensions of toBeModified matrix */                                         \
			get_matrix_dims(toBeModified, newdims, ndims);                                                    \
			/* DON'T change dimension in this case, because the callee will do it (transpose or reshape) */   \
			/* reshape, to make updated memory coherent with new dimension */                                 \
			/*vdReshapeAlgorithm(mat->matrix, mat->matrix, existingDimension, olddims, ndims, newdims, ndims); */  \
			/* re-compute poly_basis  */                                                                      \
			/* so that matrixAccess reduces to a dot product between vectors */                               \
			dtype##ComputePolyBasis(mat, newdims, ndims);                                                     \
		}                                                                                                         \
	    }                                                                                                             \
    }else if(totalDimension == 0)                                                                                         \
            /* update dimN only (without allocating) */                                                                   \
	    force_update_matrix_dims(toBeModified,temp,ndims);                                                            \
    else                                                                                                                  \
	    assert(false && "Trying to set matrix dimension to a negative value, stop.");                                 \
}

// double and integer
ensureCapacity(d, double, ensure, keep)
ensureCapacity(i, int, ensure, keep)
ensureCapacity(d, double, check, keep)
ensureCapacity(i, int, check, keep)
ensureCapacity(d, double, ensure, ignore)
ensureCapacity(i, int, ensure, ignore)
ensureCapacity(d, double, check, ignore)
ensureCapacity(i, int, check, ignore)

#define ensureSparseCapacity(dtype) \
void v##dtype##EnsureSparseCapacity(int ndims, int dimToAllocate, void* toBeModified, ...){ \
                                                                   \
    va_list vl;                                                    \
                                                                   \
    va_start(vl,toBeModified);                                     \
                                                                   \
    int totalDimension = 1;                                        \
    int temp[ndims];                                               \
    for(int dimCounter = 0; dimCounter <2;dimCounter++){       \
        temp[dimCounter]=va_arg(vl, int);                          \
        totalDimension*=temp[dimCounter];                          \
    }                                                              \
    /* enlarge only = false, this can lower the matrix dimension*/ \
    v##dtype##InternalEnsureSparseCapacity(2, dimToAllocate, toBeModified, temp, totalDimension, false); \
}

// double and integer
ensureSparseCapacity(d)
ensureSparseCapacity(i)

#define checkSparseCapacity(dtype) \
void v##dtype##CheckSparseCapacity(int ndims, int dimToAllocate, void* toBeModified, ...){ \
                                                                   \
    va_list vl;                                                    \
                                                                   \
    va_start(vl,toBeModified);                                     \
                                                                   \
    int totalDimension = 1;                                        \
    int temp[ndims];                                               \
    for(int dimCounter = 0; dimCounter <2;dimCounter++){           \
        temp[dimCounter]=va_arg(vl, int);                          \
        totalDimension*=temp[dimCounter];                          \
    }                                                              \
    /* enlarge only = true, this can enlarge only the matrix dimension*/ \
    v##dtype##InternalEnsureSparseCapacity(2, dimToAllocate, toBeModified, temp, totalDimension, true); \
}

// double and integer
checkSparseCapacity(d)
checkSparseCapacity(i)

/*
 * check capacity given a flat array.
 * never updates dimensions just check, and raise error if dimension insufficient
 */
#ifdef DEBUG

#define checkFlatCapacity(dtype) \
void v##dtype##CheckFlatCapacity(const char* file, int fline, const char* funname, int ndims, void *toBeModified, int flatIndex){   \
	int existingDimension = update_matrix_mul(toBeModified, ndims);                                                             \
	if(flatIndex > existingDimension){                                                                                         \
		fprintf(stderr, "ERROR: in C file %s:%d in %s Index exceeds array bounds.\n",file, fline, funname);                  \
		assert(false && "Index exceeds array bounds.");                                                                      \
	}                                                                                                                            \
}

// double and integer
checkFlatCapacity(d)
checkFlatCapacity(i)

#endif

/*
void* mem_alloc(void* start_loc, size_t size){
    assert(size > 0 && "Cannot reserve non-positive memory size");

    //size is at least as big for a free block to be made
    size = size + (sizeof(block_t) - sizeof(blocktail_t));
    
    //make size is a multiple of eight
    if(size % 8 != 0){
        size = size + (8 - (size & 7));
    }
    
    //get head of free list
    void* startplace = header.head;
    block_t *first = (block_t *)startplace;
    
    //first fit implementation
    if(first == NULL){
        //for now return NULL if there are no free blocks to be used.
        return first;
    }
    
    while( ((first-> size)) < size + sizeof(used_block_t) + sizeof(blocktail_t)){
        first = first->next;
        if(first == NULL){
            //if no blocks found return NULL for now
            return first;
        }
    }
    

    cc++;
    
    
    //TEMP SAVE THE PREV AND NEXT
    block_t *tempprev = first->prev;
    block_t *tempnext = first->next;
    
    //temploc
    void* allocstart = (void*)first;
    
    //size of free block
    int freesize = first->size;
    

    //get location at end of bytes
    void* allocend = ADD_BYTES(ADD_BYTES(allocstart, sizeof(used_block_t)), size);
    //get start of new free block
    void* newfreestart = ADD_BYTES(allocend, sizeof(blocktail_t));
    
    long allocsize = (long)newfreestart - (long)allocstart;
    
    
    
    //get the new free block to add to the free list
    void* freeblockend = ADD_BYTES(allocstart, freesize);
    //get the size of the start of the tail for the new freeblock
    void* newfreeend = SUBTRACT_BYTES(freeblockend, sizeof(blocktail_t));
    long freeblocksize = (long)freeblockend - (long)newfreestart;
    bool createblock = true;
    
    
    // if there is not enough room for a new freeblock
    if(sizeof(block_t) + sizeof(blocktail_t) + 8 > freeblocksize){
        allocend = SUBTRACT_BYTES(freeblockend, sizeof(blocktail_t));
        createblock = false;
    }
    
    used_block_t *use = (used_block_t *)first;
    use->use = 1;
    use->location = allocstart;
    use->size = allocsize;
    
    blocktail_t *tail = (blocktail_t *)allocend;
    tail->use = 1;
    tail->location = allocstart;
    
    
    
    if(createblock){
        
        block_t *newstart = (block_t *)newfreestart;
        blocktail_t *newend = (blocktail_t *)newfreeend;
        
        
        newstart->use = 0;
        newstart->next = tempnext;
        newstart->prev = tempprev;
        newstart->size = freeblocksize;
        
        
        newend->use = 0;
        newend->location = newfreestart;
        
        
        
        
        //update the header to point to new position if this free block was at the head
        if((long)header.head == (long)first){
            
            header.head = (void*)newstart;
        }
        
        
        //update the previous freeblock to point to the new position
        else{
            if(tempprev != NULL){
                tempprev->next = newstart;
            }
            
            if(tempnext != NULL){
                tempnext->prev = newstart;
                
            }
            
        }
        
    }
    
    else{
        //in this case the free block is being complelety used.
        // Properly set the previous and next blocks.
        
        if((long)header.head == (long)startplace){
            header.head = (void*)tempnext;
        }
        
        else{
            if(tempprev != NULL){
                tempprev->next = tempnext;
            }
            
            if(tempnext != NULL){
                tempnext->prev = tempprev;
            }
            
        }
        
        
    }
    
    return (void *)ADD_BYTES(allocstart, sizeof(used_block_t));
    //have free block at this point
    
}

void mem_free(void* start_loc, void* block_loc){
    
    
    
    //get the used block that the user wants to remove
    void* allocstart = SUBTRACT_BYTES(block_loc, sizeof(used_block_t));
    used_block_t *usedblock = (used_block_t *)allocstart;
    
    
    //get the blocks before and after the new free block to seperate into cases
    bool freebefore = false;
    bool freeafter = false;
    
    
    //block before
    void* blocklocb4 = SUBTRACT_BYTES(allocstart, sizeof(blocktail_t));
    blocktail_t *blocktailb4 = NULL;
    //potential block after this blocks
    block_t *blockbefore = NULL;
    //little check to make sure pointer doesnt exceed the boundries
    if((long)blocklocb4 > (long)big){
        
        blocktailb4 = (blocktail_t*)blocklocb4;
        
        if(blocktailb4->use == 0){
            blockbefore = (block_t *)blocktailb4->location;
            freebefore = true;
        }
        
    }
    
    //block after
    void* blocklocafter = ADD_BYTES(allocstart, usedblock->size);
    //printf("%d", usedblock->size);
    //potential free block after this block
    block_t *blockafter = (block_t *)blocklocafter;
    //little check to make sure pointer doesnt exceed the boundries
    if((long)blocklocafter < (long)big + (long)SIZE){
        
        if(blockafter->use == 1){
            //blockafter = NULL;
        }
        else{
            freeafter = true;
        }
        
        
    }
    
    //case 1 (alloc block before and after the freed block)
    if(!freebefore && !freeafter){
        
        block_t *freeblock = (block_t *)allocstart;
        int totalsize = ((used_block_t *)allocstart)->size;
        void* freeend = SUBTRACT_BYTES(ADD_BYTES(allocstart, totalsize), sizeof(blocktail_t));
        blocktail_t *freeblockend = (blocktail_t *)freeend;
        
        //get current head of free list
        block_t *currhead = (block_t *)header.head;
        
        
        freeblock->size = totalsize;
        freeblock->use = 0;
        freeblock->next = currhead;
        
        header.head = (void *)freeblock;
        
        if(currhead != NULL){
            currhead->prev = freeblock;
        }
        
        freeblock->prev = NULL;
        
        //update tail
        freeblockend->use = 0;
        freeblockend->location = allocstart;
        
    }
    
    
    //case 2 (free block before and alloc block after)
    if(freebefore && !freeafter){
        
        blockbefore->size = blockbefore->size + usedblock->size;
        
        block_t* temphead = (block_t *)header.head;
        block_t* tempprev = blockbefore->prev;
        block_t* tempnext = blockbefore->next;
        
        
        if((long)header.head == (long)blockbefore){
            header.head = (void*)blockbefore;
            
        }
        
        else{
            
            header.head = (void*)blockbefore;
            blockbefore->next = temphead;
            
            if(temphead != NULL){
                temphead->prev = blockbefore;
            }
            
            blockbefore->prev = NULL;
            
            if(tempprev != NULL){
                tempprev->next = tempnext;
            }
            
            if(tempnext != NULL){
                tempnext->prev = tempprev;
            }
            
        }
        
        
        //update tail
        void* blocktloc = SUBTRACT_BYTES(ADD_BYTES(allocstart, usedblock->size), sizeof(blocktail_t));
        blocktail_t *blockt = (blocktail_t *)blocktloc;
        
        blockt->use = 0;
        blockt->location = (void*)blockbefore;
        
        
    }
    
    //case 3 (alloc block before and free block after)
    if(!freebefore && freeafter){
        
        block_t *newfree = (block_t *)allocstart;
        
        block_t* temphead = (block_t *)header.head;
        block_t* tempprev = blockafter->prev;
        block_t* tempnext = blockafter->next;
        
        header.head = (void *)newfree;
        
        newfree->use = 0;
        newfree->size = usedblock->size + blockafter->size;
        newfree->next = temphead;
        
        
        if((long)header.head == (long)blockafter){
            header.head = (void *)newfree;
        }
        
        else{
            
            if(temphead != NULL){
                temphead->prev = newfree;
            }
            
            newfree->prev = NULL;
            
            if(tempprev != NULL){
                tempprev->next = tempnext;
            }
            
            if(tempnext != NULL){
                tempnext->prev = tempprev;
            }
        }
        
        //update tail
        void* blocktloc = SUBTRACT_BYTES(ADD_BYTES(allocstart, newfree->size), sizeof(blocktail_t));
        blocktail_t *blockt = (blocktail_t *)blocktloc;
        
        blockt->use = 0;
        blockt->location = allocstart;
        
    }
    
    //case 4 (free block before and after)
    if(freebefore && freeafter){
        
        block_t* temphead = (block_t *)header.head;
        
        block_t* tempprevleft = blockbefore->prev;
        block_t* tempnextleft = blockbefore->next;
        block_t* tempprevright = blockafter->prev;
        block_t* tempnextright = blockafter->next;
        
        //update size
        blockbefore->size = blockbefore->size + usedblock->size + blockafter->size;
        
        header.head = (void *)blockbefore;
        
        
        if((long)blockbefore == (long)header.head){
            if(tempprevright != NULL){
                tempprevright->next = tempnextright;
            }
            
            if (tempnextright != NULL){
                tempnextright->prev = tempprevright;
            }
            
        }
        
        else if((long)blockafter == (long)header.head){
            if(tempprevleft != NULL){
                tempprevleft->next = tempnextleft;
            }
            
            if(tempnextleft != NULL){
                tempnextleft->prev = tempprevleft;
            }
            
        }
        
        else{
            
            blockbefore->next = temphead;
            
            if(temphead != NULL){
                temphead->prev = blockbefore;
            }
            
            blockbefore->prev = NULL;
            
            if(tempprevleft != NULL){
                tempprevleft->next = tempnextleft;
            }
            
            if(tempnextleft != NULL){
                tempnextleft->prev = tempprevleft;
            }
            
            if(tempprevright != NULL){
                tempprevright->next = tempnextright;
            }
            if(tempnextright != NULL){
                tempnextright->prev = tempprevright;
            }
        }
        
        
        //update tail
        void* blocktloc = SUBTRACT_BYTES(ADD_BYTES(allocstart, usedblock->size + blockafter->size), sizeof(blocktail_t));
        blocktail_t *blockt = (blocktail_t *)blocktloc;
        
        blockt->use = 0;
        blockt->location = (void*)blockbefore;
        
        
    }
    
}

void* mem_resize(void* start_loc, void* block_loc, unsigned int newsize){
    return NULL;
}

void* get_address(){
    return ADD_BYTES(big, sizeof(used_block_t));
}

int get_size(){
    return SIZE;
}

int num_free_blocks(){
    int counter = 0;
    block_t *start = (block_t *)header.head;
    while((void *)start != NULL){
        counter++;
        start = start->next;
    }
    
    return counter;
    
}
*/

#endif

