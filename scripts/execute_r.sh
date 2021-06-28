#!/bin/bash

echo " ----- EXECUTING R MODELS WITH DESOLVE ------";
REPORT_PATH=report_r.tsv
rm -f $REPORT_PATH
while IFS= read -r dir; do
	cd "$dir"
	# name of containing directory
	dname=$(dirname "$dir")	
	name=$(basename "$dname")
	echo "---------- TESTING $dir --------------"
	# execute three times
	$RSCRIPT main.R > output
	if [ $? -ne 0 ]
	then
		echo "---------- FAILED $dir --------------";
        	sleep 1;
        	kill $$;
	else
		runtime1=$(tail -1 output)
	fi
        echo "---------- SUCCESS $dir --------------"
        # fill line of tsv
        cd - > /dev/null
        echo "$name     $runtime1" >> $REPORT_PATH
done < <(find $TEST_FOLDER -type d -iname R)

