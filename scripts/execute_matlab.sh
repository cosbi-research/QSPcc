#!/bin/bash


echo " ----- EXECUTING MATLAB MODELS WITH ODEXXX ------";
REPORT_PATH=report_matlab.tsv
rm -f $REPORT_PATH
while IFS= read -r mainscript; do
	dir=$(dirname "$mainscript")
	cd "$dir"
	# name of containing directory
	name=$(basename "$dir")
	scriptname=$(basename "$mainscript")
	echo "---------- TESTING $dir --------------"
	# execute three times
       	matlab -nodisplay -nosplash -nosoftwareopengl -nodesktop -r "run $scriptname; exit" > output
        if [ $? -ne 0 ]
        then
                echo "---------- FAILED $dir --------------";
                sleep 1;
                kill $$;
        else
                runtime1=$(tail -3 output | tr -d '\040\011\012\015')
        fi
	echo "---------- SUCCESS $dir --------------" 
        # fill line of tsv
        cd - > /dev/null
        echo "$name     $runtime1" >> $REPORT_PATH
done < <(find $TEST_FOLDER -type f -not -path "*tagged*" -not -path "*multipleassign*" -iname main.m)

