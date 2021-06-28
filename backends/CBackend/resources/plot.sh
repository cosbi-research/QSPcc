#/bin/bash

set -e 

# to use this script you should first run your model/algorithm
# compiled with the option 'dump=on' in this way
#
# $ make dump=on
#
# so that the sundials internal steps will be recorded in the  _sundials_y_timeseries.csv and  _sundials_dydt_timeseries.csv respectively

sed 's/,$//;/^$/d' _sundials_y_timeseries.csv > y.csv
sed 's/,$//;/^$/d' _sundials_dydt_timeseries.csv > dydt.csv

NCOLS=$(head -1 y.csv | tr ',' '\n' | wc -l)

gnuplot <<- EOF
# Input file contains comma-separated values fields
set datafile separator ","
unset key
# Make the x axis labels easier to read.
set xtics rotate out

# output to png
set terminal png size 2048,1536
set output 'dydt.png'

# log_10 on y axis
#set logscale y 10

set key title "dydt" 
set xlabel "time [s]"
plot for [COL=2:${NCOLS}] 'dydt.csv' using 1:COL with linespoints linetype COL title "col ".COL

EOF


gnuplot <<- EOF
# Input file contains comma-separated values fields
set datafile separator ","
unset key
# Make the x axis labels easier to read.
set xtics rotate out

# output to png
set terminal png size 2048,1536
set output 'y.png'

# log_10 on y axis
#set logscale y 10

set key title "y"
set xlabel "time [s]"
plot for [COL=2:${NCOLS}] 'y.csv' using 1:COL with linespoints linetype COL title "col ".COL

EOF

