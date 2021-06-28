# Gnuplot script to produce plots of ODE

# Plot solution
set terminal pngcairo  transparent enhanced font "arial,10" fontscale 1.0 size 600, 400 
set output '<PROGRAM_NAME>_plot.png'
set bar 1.000000 front
set style circle radius graph 0.02, first 0.00000, 0.00000 
set style ellipse size graph 0.05, 0.03, first 0.00000 angle 0 units xy
#set key inside left top vertical Right noreverse enhanced autotitle box lt black linewidth 1.000 dashtype solid
set style textbox transparent margins  1.0,  1.0 border
set grid
set format y "%.1f"
set format x "%.0f"
set title "automatic plot for <PROGRAM_NAME>"
set title  font ",20" norotate
set xlabel "$t$"
set ylabel "$x$"
set nokey
m = 2
plot "<PROGRAM_NAME>.txt" using 1:2 with lines lw m

