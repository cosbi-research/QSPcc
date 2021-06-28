Source code published in

[41] Giulia Simoni, Chanchala Kaddi, Mengdi Tao, Federico Reali, Danilo Tomasoni, Corrado Priami, Karim Azer, Susana Neves-Zaph, Luca Marchetti, A robust computational pipeline for model-based and data-driven phenotype clustering, Bioinformatics, btaa948, https://doi.org/10.1093/bioinformatics/btaa948 

as "Supporting Information"
file named "btaa948_supplementary_data.zip"
retrieved from https://academic.oup.com/bioinformatics/advance-article/doi/10.1093/bioinformatics/btaa948/5952665#supplementary-data

This folder contains the MATLAB code published in [41] to perform the global sensitivity analysis on the GD1 QSP model. 
A variant of the code Param_var_local.m in the sub-folder 'GlobalSensitivity' 
was translated to C with QSPcc.  
This allowed to run the full sensitivity analysis in twelve days, 
while the MATLAB-only version required one month.

The public code uses cell arrays, that are currently not supported by QSPcc, 
but it is fairly easy to modify the code to avoid using cell arrays by replacing lines similar to

Xmain_add{j}=Xmain_a(:,[13,14,16])';

with 

Xmain_add(j,:,:)=Xmain_a(:,[13,14,16])';

and 

g1 = Xmain_add{j};

with 

g1 = Xmain_add(j,:,:);


Then you need a MEX interface to integrate the C translation with the MATLAB code.
We have already done this during model development at COSBI.
Contact the Bioinformatics lab led by lombardo@cosbi.eu for further information. 
