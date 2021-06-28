#!/bin/bash

SUNDIALS_VERSIONS=(5_0)
TESTS=(sundials_v5)

for sundials_ver in ${SUNDIALS_VERSIONS[*]}; do
for test_set in ${TESTS[*]}; do

declare SUN_${sundials_ver}_INCLUDE_PATH
CUR_INCLUDE_PATH=SUN_${sundials_ver}_INCLUDE_PATH
declare SUN_${sundials_ver}_LIB_PATH
CUR_LIB_PATH=SUN_${sundials_ver}_LIB_PATH

echo "find $TEST_FOLDER -type d -iname C_${test_set}"

REPORT_PATH=report_c_${test_set}.tsv
rm -f $REPORT_PATH
while IFS= read -r dir; do 
	pushd "$dir" > /dev/null
	# name of containing directory
	dname=$(dirname "$dir")
	name=$(basename "$dname")
	echo "---------- TESTING $dir --------------"
	sh "$TRANSLATOR_HOME"/scripts/tags_to_paths.sh "${!CUR_INCLUDE_PATH}" "${!CUR_LIB_PATH}"

	# compile
	make OS=$MACHINE
	# move matlab results away
        if [ $? -ne 0 ]
        then
		echo "COMPILATION FAILED"
	else
		echo "COMPILATION OK"
	fi
	if test -f results.csv; then
		mv results.csv ..
	fi
	# execute three times
	./main > output
        if [ $? -ne 0 ]
        then
                echo "---------- EXECUTION FAILED $dir --------------";
		echo "Automatic testing failed, stopping now"
                sleep 1;
                kill $$;
        else
		echo "EXECUTION OK"
                runtime1=$(tail -1 output)
        fi
	# test results
	pushd .. > /dev/null
	# check for presence of results file
	#echo "Looking for output timeseries..."
	if test -f results.csv; then
		echo "Generating timeseries plot $(pwd)/timeseries.png ..."
		#python3 -c "import math, sys; work=[x.split(',')[:-1] for x in open('results.csv').readlines()]; nowork=[x.split(',')[:-1] for x in open('C_${test_set}/results.csv').readlines()]; all(map(lambda x : x < 1.0, [sum(map(lambda x: abs(float(x[0])-float(x[1])) / (1 + min(float(x[0]), float(x[1]))), zip(dline,mline))) for dline,mline in zip(work,nowork)])) or sys.exit('ERROR output changed! stop.');"
		if command -v gnuplot &> /dev/null
		then	
			# can be used in place of 3 in 'COL=2:2'
			# commented for speed
			#NCOLS=$(head -1 y.csv | tr ',' '\n' | wc -l)

			# plotting c timeseries
			gnuplot &> /dev/null <<- EOF
# Input file contains comma-separated values fields
set datafile separator ","
unset key
# Make the x axis labels easier to read.
set xtics rotate out

# output to png
set terminal png size 2048,1536
set output 'timeseries.png'

# log_10 on y axis
#set logscale y 10

set key title "MATLAB vs C timeseries" 
set xlabel "time [s]"
plot for [COL=2:2] 'C_${test_set}/results.csv' using 1:COL with linespoints lt rgb "red" title "C variable ".COL, \
     for [COL=2:2] 'results.csv' using 1:COL with linespoints lt rgb "green" title "MATLAB variable ".COL
EOF
		else
			echo "WARNING: gnuplot command could not be found, please install it to automatically generate the plot of the timeseries. Skipping."
		fi
		echo "done"
	
	else
		echo "No timeseries produced, skipping timeseries plot."
	fi
	
	echo "---------- SUCCESS $dir --------------"
	popd > /dev/null

	# fill line of tsv
	popd > /dev/null
	echo "$name	$runtime1" >> $REPORT_PATH

done < <(find $TEST_FOLDER -type d -iname C_${test_set})

done
done

