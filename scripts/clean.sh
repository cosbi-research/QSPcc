#!/bin/bash

if [ -d "$TEST_FOLDER" ]
then	

for dir in $(find $TEST_FOLDER -type d -iname C_*); do 
	rm -rf $dir 2> /dev/null
done

for dir in $(find $TEST_FOLDER -type d -iname R); do 
        rm -rf $dir 2> /dev/null
done


for dir in $(find $TEST_FOLDER -type d -iname dist); do 
        rm -rf $dir 2> /dev/null
done

for dir in $(find $TEST_FOLDER -type f); do 
        rm -rf $dir 2> /dev/null 
done

fi
