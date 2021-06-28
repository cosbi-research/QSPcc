#!/bin/bash
NANO_TO_MILLI=1000000
REPORT_PATH=report_single.tsv
cd $1
# name of containing directory
name=$(basename $(dirname $1))        
echo "---------- executing $1 --------------"
sh $TRANSLATOR_HOME/scripts/tags_to_paths.sh $SUN_3_INCLUDE_PATH $SUN_3_LIB_PATH
# compile
make OS=$MACHINE

runtime1=$(./main | tail -1)

cd - > /dev/null
echo "$name	$runtime1" >> $REPORT_PATH
