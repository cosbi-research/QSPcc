#ifndef __MEMORY_LIB__
#define __MEMORY_LIB__

#include<stdarg.h>

#define EXTRA_SAFETY_MEMORY 10
#define MAX_SPARSE_INITIAL_DIMENSION 10000


// allocator to use, macos doesn't support tcmalloc
#if defined(DEBUG) || defined(MACOS)

#define qspcc_malloc malloc
#define qspcc_realloc realloc
#define qspcc_free free
#define qspcc_calloc calloc

#else

#include <gperftools/tcmalloc.h>
#define qspcc_malloc tc_malloc
#define qspcc_realloc tc_realloc
#define qspcc_free tc_free
#define qspcc_calloc tc_calloc
#define TCMALLOC

#endif

typedef struct _block_t{
    char use;
    unsigned int size;
    struct _block_t *next; // in bytes
    struct _block_t *prev; // in bytes
} block_t;

typedef struct _blocktail_t{
    char use;
    void* location;
} blocktail_t;

typedef struct _used_block_t{
    char use;
    unsigned int size;
    void* location;
    
} used_block_t;

typedef struct _block_header_t{
    void *head;
    unsigned int size;
} block_header_t;

extern void update_matrix_dims(void* matrix, int newdims[], int ndims);
extern int update_matrix_mul(void* matrix, int ndims);
extern void vdensurekeepCapacity(int ndims, void* toBeModified, ...);
extern void vdcheckkeepCapacity(int ndims, void* toBeModified, ...);
extern void viensurekeepCapacity(int ndims, void* toBeModified, ...);
extern void vicheckkeepCapacity(int ndims, void* toBeModified, ...);
extern void vdensureignoreCapacity(int ndims, void* toBeModified, ...);
extern void vdcheckignoreCapacity(int ndims, void* toBeModified, ...);
extern void viensureignoreCapacity(int ndims, void* toBeModified, ...);
extern void vicheckignoreCapacity(int ndims, void* toBeModified, ...);


extern void vdEnsureSparseCapacity(int ndims, int dimsToAllocate, void* toBeModified, ...);
extern void vdCheckSparseCapacity(int ndims, int dimsToAllocate, void* toBeModified, ...);
extern void viEnsureSparseCapacity(int ndims, int dimsToAllocate, void* toBeModified, ...);
extern void viCheckSparseCapacity(int ndims, int dimsToAllocate, void* toBeModified, ...);

extern void computePolyBasis(int *poly_basis, int *dims, int ndims);

#ifdef DEBUG

extern void viCheckFlatCapacity(const char* file, int fline, const char* funname, int ndims, void* toBeModified, int flatIdx);
extern void vdCheckFlatCapacity(const char* file, int fline, const char* funname, int ndims, void* toBeModified, int flatIdx);

#else

#define viCheckFlatCapacity(file, fline, funname, ndims, toBeModified, flatIndex) 
#define vdCheckFlatCapacity(file, fline, funname, ndims, toBeModified, flatIndex) 

#endif

void init_metadata(); // temp for now
int mem_start(void* start_loc, unsigned int size); // returns 0 if unsuccesful and otherwise non zero value
void* mem_alloc(void* start_loc, size_t size); // returns address where it ends up being stored
void mem_free(void* start_loc, void* block_loc); // frees block stored at start_loc. Assumes Free is called correctly
void* mem_resize(void* start_loc, void* block_loc, unsigned int newsize); // makes the block located at block_loc a different size
void* get_address(); //used to get address of static memory for testing
int get_size(); // size used
int num_free_blocks();



#endif

