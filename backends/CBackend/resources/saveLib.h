#ifndef __SAVE_LIB__
#define __SAVE_LIB__

extern void siSave(const char *path, int val);
extern void sdSave(const char *path, double val);
extern void ssSave(const char *path, const char *val);
// save ND matrices
extern void viSave(const char *path, int *vals, int *poly_basis, int vals_dims, ...);
extern void vdSave(const char *path, const char *mode, double *vals, int *poly_basis, int vals_dims, ...);
#endif
