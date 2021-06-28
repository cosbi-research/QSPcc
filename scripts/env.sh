#!/bin/bash

# sundials 2 include and lib directory
export SUN_2_7_INCLUDE_PATH=/opt/sundials_2.7.0/include
export SUN_2_7_LIB_PATH=/opt/sundials_2.7.0/lib
# sundials 3 include and lib directory
export SUN_3_1_INCLUDE_PATH=/opt/sundials_3.1.0/include
export SUN_3_1_LIB_PATH=/opt/sundials_3.1.0/lib
# sundials 4 include and lib directory
export SUN_4_1_INCLUDE_PATH=/usr/local/sundials-4.1.0/include
export SUN_4_1_LIB_PATH=/usr/local/sundials-4.1.0/lib
# sundials 5 include and lib directory
export SUN_5_0_INCLUDE_PATH=/usr/local/sundials-5.7.0/include
export SUN_5_0_LIB_PATH=/usr/local/sundials-5.7.0/lib
# standard C libraries dir, usually shouldn't be changed
export STD_LIB_PATH=/usr/lib
# tcmalloc include and lib directory (for efficient memory handling)
export TCMALLOC_LIB_PATH=/usr/local/tcmalloc/lib
export TCMALLOC_INCLUDE_PATH=/usr/local/tcmalloc/include
export TMPDIR=/tmp

unameOut="$(uname -s)"
PATHSEP=":"
case "${unameOut}" in
    Linux*)     MACHINE=Linux;;
    Darwin*)    MACHINE=Mac;;
    CYGWIN*)    MACHINE=Cygwin
		PATHSEP=";"
		TRANSLATOR_HOME="`cygpath -pw $TRANSLATOR_HOME`"
		;;
    MINGW*)     MACHINE=MinGw;;
    *)          MACHINE="UNKNOWN:${unameOut}"
esac
export MACHINE
export PATHSEP

export RSCRIPT=/usr/bin/Rscript

export TEST_FOLDER=$TMPDIR/tests

export NANO_TO_MILLI=1000000

