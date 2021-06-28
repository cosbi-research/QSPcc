#!/bin/bash

#HELPER SCRIPT

# fill-in makefile
sed -i.bak "s|\[SUN_INCLUDE_PATH\]|$1|g" objs/Makefile
sed -i.bak "s|\[MATIO_INCLUDE_PATH\]|$MATIO_INCLUDE_PATH|g" objs/Makefile
sed -i.bak "s|\[HDF5_INCLUDE_PATH\]|$HDF5_INCLUDE_PATH|g" objs/Makefile
sed -i.bak "s|\[TCMALLOC_INCLUDE_PATH\]|$TCMALLOC_INCLUDE_PATH|g" objs/Makefile

sed -i.bak "s|\[SUN_LIB_PATH\]|$2|g" objs/Makefile
sed -i.bak "s|\[MKL_LIB_PATH\]|$MKL_LIB_PATH|g" objs/Makefile
sed -i.bak "s|\[MATIO_LIB_PATH\]|$MATIO_LIB_PATH|g" objs/Makefile
sed -i.bak "s|\[HDF5_LIB_PATH\]|$HDF5_LIB_PATH|g" objs/Makefile
sed -i.bak "s|\[TCMALLOC_LIB_PATH\]|$TCMALLOC_LIB_PATH|g" objs/Makefile
sed -i.bak "s|\[STD_LIB_PATH\]|$STD_LIB_PATH|g" objs/Makefile

# fill-in makefile mac
sed -i.bak "s|\[SUN_INCLUDE_PATH\]|$1|g" objs/Makefile.mac
sed -i.bak "s|\[MATIO_INCLUDE_PATH\]|$MATIO_INCLUDE_PATH|g" objs/Makefile.mac
sed -i.bak "s|\[HDF5_INCLUDE_PATH\]|$HDF5_INCLUDE_PATH|g" objs/Makefile.mac
sed -i.bak "s|\[TCMALLOC_INCLUDE_PATH\]|$TCMALLOC_INCLUDE_PATH|g" objs/Makefile.mac

sed -i.bak "s|\[SUN_LIB_PATH\]|$2|g" objs/Makefile.mac
sed -i.bak "s|\[MKL_LIB_PATH\]|$MKL_LIB_PATH|g" objs/Makefile.mac
sed -i.bak "s|\[MATIO_LIB_PATH\]|$MATIO_LIB_PATH|g" objs/Makefile.mac
sed -i.bak "s|\[HDF5_LIB_PATH\]|$HDF5_LIB_PATH|g" objs/Makefile.mac
sed -i.bak "s|\[TCMALLOC_LIB_PATH\]|$TCMALLOC_LIB_PATH|g" objs/Makefile.mac
sed -i.bak "s|\[STD_LIB_PATH\]|$STD_LIB_PATH|g" objs/Makefile

#delete the backup files created with -sed
find ./ -name '*.bak' -delete

