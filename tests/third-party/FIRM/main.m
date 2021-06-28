% This file works with OCTAVE and is automatically generated with 
% the System Biology Format Converter (http://sbfc.sourceforge.net/)
% from an SBML file.
% To run this file with Matlab you must edit the comments providing
% the definition of the ode solver and the signature for the 
% xdot function.
%
% The conversion system has the following limitations:
%  - You may have to re order some reactions and Assignment Rules definition
%  - Delays are not taken into account
%  - You should change the lsode parameters (start, end, steps) to get better results
%

%
% Model name = Palsson2013 - Fully-integrated immune response model (FIRM)
%
% is http://identifiers.org/biomodels.db/MODEL1603310000
% is http://identifiers.org/biomodels.db/BIOMD0000000608
% isDescribedBy http://identifiers.org/pubmed/24074340
% isDerivedFrom http://identifiers.org/pubmed/15038983
% isDerivedFrom http://identifiers.org/pubmed/5500468
% isDerivedFrom http://identifiers.org/pubmed/3156189
%


    tic;
%Initial conditions vector
	x0=zeros(52,1);
	x0(1) = 1.0E8;
	x0(2) = 0.0;
	x0(3) = 1.0E-7;
	x0(4) = 100000.0;
	x0(5) = 0.0;
	x0(6) = 5.0E7;
	x0(7) = 0.0;
	x0(8) = 0.0;
	x0(9) = 0.0;
	x0(10) = 0.0;
	x0(11) = 78431.3726;
	x0(12) = 1111.18351;
	x0(13) = 3000.49554;
	x0(14) = 0.0;
	x0(15) = 0.0;
	x0(16) = 18750.0;
	x0(17) = 0.0;
	x0(18) = 0.0;
	x0(19) = 0.0;
	x0(20) = 0.0;
	x0(21) = 0.0;
	x0(22) = 0.0;
	x0(23) = 0.0;
	x0(24) = 1111.11111;
	x0(25) = 50000.0;
	x0(26) = 0.0;
	x0(27) = 0.0;
	x0(28) = 0.0;
	x0(29) = 0.0;
	x0(30) = 0.0;
	x0(31) = 0.0;
	x0(32) = 0.0;
	x0(33) = 0.0;
	x0(34) = 0.0;
	x0(35) = 0.0;
	x0(36) = 0.0;
	x0(37) = 0.0;
	x0(38) = 275.0;
	x0(39) = 0.0;
	x0(40) = 0.0;
	x0(41) = 0.0;
	x0(42) = 0.0;
	x0(43) = 1.875E7;
	x0(44) = 0.0;
	x0(45) = 0.0;
	x0(46) = 0.0;
	x0(47) = 0.0;
	x0(48) = 0.0;
	x0(49) = 0.0;
	x0(50) = 0.0;
	x0(51) = 0.0;
	x0(52) = 0.0;


% Depending on whether you are using Octave or Matlab,
% you should comment / uncomment one of the following blocks.
% This should also be done for the definition of the function f below.
% Start Matlab code
	tspan=[0:0.01:100];
	opts = odeset('AbsTol',1e-3);
	[t,x]=ode23(@f,tspan,x0,opts);
% End Matlab code

% Start Octave code
%	t=linspace(0,100,100);
%	x=lsode('f',x0,t);
% End Octave code

    disp(toc);
	%plot(t,x);



% Depending on whether you are using Octave or Matlab,
% you should comment / uncomment one of the following blocks.
% This should also be done for the definition of the function f below.
% Start Matlab code
function xdot=f(t,x)
% End Matlab code

% Start Octave code
%function xdot=f(x,t)
% End Octave code

% Compartment: id = compartmentOne, name = compartmentOne, constant
	compartment_compartmentOne=1.0;
% Parameter:   id =  Alpha4, name = Alpha4
	global_par_Alpha4=0.5;
% Parameter:   id =  Alpha5, name = Alpha5
	global_par_Alpha5=1.25E-7;
% Parameter:   id =  Alpha6, name = Alpha6
	global_par_Alpha6=1.25E-8;
% Parameter:   id =  Alpha61, name = Alpha61
	global_par_Alpha61=10.0;
% Parameter:   id =  Alpha62, name = Alpha62
	global_par_Alpha62=10.0;
% Parameter:   id =  Beta90, name = Beta90
	global_par_Beta90=1000.0;
% Parameter:   id =  c2, name = c2
	global_par_c2=1000000.0;
% Parameter:   id =  c4, name = c4
	global_par_c4=0.15;
% Parameter:   id =  c12, name = c12
	global_par_c12=500000.0;
% Parameter:   id =  c15, name = c15
	global_par_c15=150000.0;
% Parameter:   id =  c17, name = c17
	global_par_c17=10000.0;
% Parameter:   id =  c22, name = c22
	global_par_c22=3000.0;
% Parameter:   id =  c24, name = c24
	global_par_c24=15000.0;
% Parameter:   id =  c25, name = c25
	global_par_c25=100000.0;
% Parameter:   id =  c52, name = c52
	global_par_c52=15000.0;
% Parameter:   id =  c55, name = c55
	global_par_c55=50.0;
% Parameter:   id =  c72, name = c72
	global_par_c72=51.0;
% Parameter:   id =  c80, name = c80
	global_par_c80=5000.0;
% Parameter:   id =  cF, name = cF
	global_par_cF=1000.0;
% Parameter:   id =  cf11, name = cf11
	global_par_cf11=100.0;
% Parameter:   id =  cf12, name = cf12
	global_par_cf12=150.0;
% Parameter:   id =  cf27, name = cf27
	global_par_cf27=30.0;
% Parameter:   id =  cf28, name = cf28
	global_par_cf28=50.0;
% Parameter:   id =  cf29, name = cf29
	global_par_cf29=2.0;
% Parameter:   id =  cf86, name = cf86
	global_par_cf86=50.0;
% Parameter:   id =  cI, name = cI
	global_par_cI=50.0;
% Parameter:   id =  ci12, name = ci12
	global_par_ci12=1000.0;
% Parameter:   id =  ci72, name = ci72
	global_par_ci72=0.05;
% Parameter:   id =  ci80, name = ci80
	global_par_ci80=50.0;
% Parameter:   id =  cii80, name = cii80
	global_par_cii80=100000.0;
% Parameter:   id =  Delta11, name = Delta11
	global_par_Delta11=0.36;
% Parameter:   id =  Delta12, name = Delta12
	global_par_Delta12=0.4;
% Parameter:   id =  Delta21, name = Delta21
	global_par_Delta21=1.0E-4;
% Parameter:   id =  Delta27, name = Delta27
	global_par_Delta27=0.1;
% Parameter:   id =  Delta29, name = Delta29
	global_par_Delta29=0.05;
% Parameter:   id =  Delta36, name = Delta36
	global_par_Delta36=2.4;
% Parameter:   id =  Delta38, name = Delta38
	global_par_Delta38=2.4;
% Parameter:   id =  Delta39, name = Delta39
	global_par_Delta39=0.0;
% Parameter:   id =  Delta43, name = Delta43
	global_par_Delta43=2.4;
% Parameter:   id =  Delta50, name = Delta50
	global_par_Delta50=1.0E-4;
% Parameter:   id =  Delta54, name = Delta54
	global_par_Delta54=0.001;
% Parameter:   id =  Delta57, name = Delta57
	global_par_Delta57=0.0;
% Parameter:   id =  Deltai12, name = Deltai12
	global_par_Deltai12=0.009;
% Parameter:   id =  Deltai27, name = Deltai27
	global_par_Deltai27=0.001;
% Parameter:   id =  endpoint, name = endpoint
	global_par_endpoint=400.0;
% Parameter:   id =  Eta10, name = Eta10
	global_par_Eta10=0.05;
% Parameter:   id =  Eta13, name = Eta13
	global_par_Eta13=1.0;
% Parameter:   id =  Eta14, name = Eta14
	global_par_Eta14=0.0;
% Parameter:   id =  Eta16, name = Eta16
	global_par_Eta16=0.01;
% Parameter:   id =  Eta18, name = Eta18
	global_par_Eta18=0.02;
% Parameter:   id =  Eta20, name = Eta20
	global_par_Eta20=0.102;
% Parameter:   id =  Eta26, name = Eta26
	global_par_Eta26=0.3333;
% Parameter:   id =  Eta28, name = Eta28
	global_par_Eta28=0.3333;
% Parameter:   id =  Eta30, name = Eta30
	global_par_Eta30=0.3333;
% Parameter:   id =  Eta33, name = Eta33
	global_par_Eta33=0.3333;
% Parameter:   id =  Eta35, name = Eta35
	global_par_Eta35=2.4;
% Parameter:   id =  Eta44, name = Eta44
	global_par_Eta44=1.2;
% Parameter:   id =  Eta47, name = Eta47
	global_par_Eta47=0.0024;
% Parameter:   id =  Eta48, name = Eta48
	global_par_Eta48=0.024;
% Parameter:   id =  Eta49, name = Eta49
	global_par_Eta49=0.048;
% Parameter:   id =  Eta53, name = Eta53
	global_par_Eta53=0.02;
% Parameter:   id =  Eta56, name = Eta56
	global_par_Eta56=0.02;
% Parameter:   id =  Eta58, name = Eta58
	global_par_Eta58=0.0;
% Parameter:   id =  Eta60, name = Eta60
	global_par_Eta60=0.001;
% Parameter:   id =  Eta69, name = Eta69
	global_par_Eta69=2.77;
% Parameter:   id =  Eta73, name = Eta73
	global_par_Eta73=3.6968;
% Parameter:   id =  Eta75, name = Eta75
	global_par_Eta75=1.188;
% Parameter:   id =  Eta79, name = Eta79
	global_par_Eta79=1.0;
% Parameter:   id =  Eta81, name = Eta81
	global_par_Eta81=3.0;
% Parameter:   id =  Eta83, name = Eta83
	global_par_Eta83=0.0;
% Parameter:   id =  Eta84, name = Eta84
	global_par_Eta84=0.3333;
% Parameter:   id =  Eta85, name = Eta85
	global_par_Eta85=0.02;
% Parameter:   id =  Eta95, name = Eta95
	global_par_Eta95=4.8;
% Parameter:   id =  Eta96, name = Eta96
	global_par_Eta96=4.8;
% Parameter:   id =  Eta99, name = Eta99
	global_par_Eta99=4.8;
% Parameter:   id =  Eta100, name = Eta100
	global_par_Eta100=4.8;
% Parameter:   id =  fi12, name = fi12
	global_par_fi12=2.333;
% Parameter:   id =  fi27, name = fi27
	global_par_fi27=4.1;
% Parameter:   id =  fi29, name = fi29
	global_par_fi29=0.12;
% Parameter:   id =  fii27, name = fii27
	global_par_fii27=4.8;
% Parameter:   id =  Gamma17, name = Gamma17
	global_par_Gamma17=0.2;
% Parameter:   id =  Gamma23, name = Gamma23
	global_par_Gamma23=0.9;
% Parameter:   id =  Gamma24, name = Gamma24
	global_par_Gamma24=0.9;
% Parameter:   id =  Gamma31, name = Gamma31
	global_par_Gamma31=0.3333;
% Parameter:   id =  Gamma32, name = Gamma32
	global_par_Gamma32=0.9;
% Parameter:   id =  Gamma37, name = Gamma37
	global_par_Gamma37=0.9;
% Parameter:   id =  Gamma40, name = Gamma40
	global_par_Gamma40=0.9;
% Parameter:   id =  Gamma41, name = Gamma41
	global_par_Gamma41=0.9;
% Parameter:   id =  Gamma51, name = Gamma51
	global_par_Gamma51=0.9;
% Parameter:   id =  Gamma52, name = Gamma52
	global_par_Gamma52=0.9;
% Parameter:   id =  k2, name = k2
	global_par_k2=0.4;
% Parameter:   id =  k3, name = k3
	global_par_k3=0.11;
% Parameter:   id =  K3s, name = K3s
	global_par_K3s=50.0;
% Parameter:   id =  k7, name = k7
	global_par_k7=1.0E-7;
% Parameter:   id =  k63, name = k63
	global_par_k63=2.0;
% Parameter:   id =  K90, name = K90
	global_par_K90=100.0;
% Parameter:   id =  K93, name = K93
	global_par_K93=100.0;
% Parameter:   id =  Mu1, name = Mu1
	global_par_Mu1=0.005;
% Parameter:   id =  Mu8, name = Mu8
	global_par_Mu8=0.1;
% Parameter:   id =  Mu9, name = Mu9
	global_par_Mu9=0.04;
% Parameter:   id =  Mu15, name = Mu15
	global_par_Mu15=0.02;
% Parameter:   id =  Mu19, name = Mu19
	global_par_Mu19=0.1;
% Parameter:   id =  Mu22, name = Mu22
	global_par_Mu22=0.9;
% Parameter:   id =  Mu25, name = Mu25
	global_par_Mu25=0.4;
% Parameter:   id =  Mu28, name = Mu28
	global_par_Mu28=1.0;
% Parameter:   id =  Mu42, name = Mu42
	global_par_Mu42=2.4;
% Parameter:   id =  Mu55, name = Mu55
	global_par_Mu55=1.0;
% Parameter:   id =  Mu59, name = Mu59
	global_par_Mu59=1.0;
% Parameter:   id =  Mu86, name = Mu86
	global_par_Mu86=1.0;
% Parameter:   id =  MuI, name = MuI
	global_par_MuI=9.0;
% Parameter:   id =  Mui9, name = Mui9
	global_par_Mui9=125000.0;
% Parameter:   id =  P46, name = P46
	global_par_P46=9.0;
% Parameter:   id =  P87, name = P87
	global_par_P87=3.0;
% Parameter:   id =  P88, name = P88
	global_par_P88=3.0;
% Parameter:   id =  P89, name = P89
	global_par_P89=100000.0;
% Parameter:   id =  q68a, name = q68a
	global_par_q68a=0.0029;
% Parameter:   id =  q68b, name = q68b
	global_par_q68b=0.0218;
% Parameter:   id =  q72a, name = q72a
	global_par_q72a=0.006;
% Parameter:   id =  q72b, name = q72b
	global_par_q72b=5.0E-5;
% Parameter:   id =  q72c, name = q72c
	global_par_q72c=1.0E-4;
% Parameter:   id =  q72d, name = q72d
	global_par_q72d=1.0E-4;
% Parameter:   id =  q72e, name = q72e
	global_par_q72e=1.0E-4;
% Parameter:   id =  q74, name = q74
	global_par_q74=0.0035;
% Parameter:   id =  q78a, name = q78a
	global_par_q78a=2.75E-6;
% Parameter:   id =  q78b, name = q78b
	global_par_q78b=8.0E-4;
% Parameter:   id =  q80, name = q80
	global_par_q80=0.02;
% Parameter:   id =  q82, name = q82
	global_par_q82=0.0;
% Parameter:   id =  Rho9, name = Rho9
	global_par_Rho9=5000.0;
% Parameter:   id =  Rho15, name = Rho15
	global_par_Rho15=500.0;
% Parameter:   id =  Rho19, name = Rho19
	global_par_Rho19=1000.0;
% Parameter:   id =  Rho21, name = Rho21
	global_par_Rho21=100.0;
% Parameter:   id =  Rho34, name = Rho34
	global_par_Rho34=10.0;
% Parameter:   id =  Rho50, name = Rho50
	global_par_Rho50=100.0;
% Parameter:   id =  Rho80, name = Rho80
	global_par_Rho80=700.0;
% Parameter:   id =  v14, name = v14
	global_par_v14=0.0;
% Parameter:   id =  v30, name = v30
	global_par_v30=0.0;
% Parameter:   id =  v41, name = v41
	global_par_v41=0.0;
% Parameter:   id =  v57, name = v57
	global_par_v57=0.0;
% Parameter:   id =  v58, name = v58
	global_par_v58=0.0;
% Parameter:   id =  v59, name = v59
	global_par_v59=0.0;
% Parameter:   id =  v64, name = v64
	global_par_v64=0.0;
% Parameter:   id =  v65, name = v65
	global_par_v65=0.0;
% Parameter:   id =  v66, name = v66
	global_par_v66=0.0;
% Parameter:   id =  v67, name = v67
	global_par_v67=0.0;
% Parameter:   id =  v70, name = v70
	global_par_v70=0.0;
% Parameter:   id =  v71, name = v71
	global_par_v71=0.0;
% Parameter:   id =  v76, name = v76
	global_par_v76=0.0;
% Parameter:   id =  v77, name = v77
	global_par_v77=0.0;
% Parameter:   id =  v82, name = v82
	global_par_v82=0.0;
% Parameter:   id =  v83, name = v83
	global_par_v83=0.0;
% Parameter:   id =  v87, name = v87
	global_par_v87=0.0;
% Parameter:   id =  volBlood, name = volBlood
	global_par_volBlood=4500.0;
% Parameter:   id =  volLung, name = volLung
	global_par_volLung=1000.0;
% Parameter:   id =  volLymphB, name = volLymphB
	global_par_volLymphB=150.0;
% Parameter:   id =  volLymphT, name = volLymphT
	global_par_volLymphT=10.0;
% Parameter:   id =  volMA, name = volMA
	global_par_volMA=1.6E-8;
% Parameter:   id =  volMI, name = volMI
	global_par_volMI=1.6E-8;
% Parameter:   id =  volMR, name = volMR
	global_par_volMR=8.0E-9;
% Parameter:   id =  volT, name = volT
	global_par_volT=2.0E-9;
% Parameter:   id =  volTB, name = volTB
	global_par_volTB=1.0E-12;
% Parameter:   id =  w9, name = w9
	global_par_w9=0.14;
% Parameter:   id =  APC, name = APC
% Parameter:   id =  Beta91, name = Beta91
% Parameter:   id =  Beta92, name = Beta92
% Parameter:   id =  Beta93, name = Beta93
% Parameter:   id =  Beta94, name = Beta94
% Parameter:   id =  Beta97, name = Beta97
% Parameter:   id =  Beta98, name = Beta98
% Parameter:   id =  FACTOR, name = FACTOR
% Parameter:   id =  INFLAM, name = INFLAM
% Parameter:   id =  K91, name = K91
% Parameter:   id =  K92, name = K92
% Parameter:   id =  K94, name = K94
% Parameter:   id =  K97, name = K97
% Parameter:   id =  K98, name = K98
% Parameter:   id =  MA, name = MA
% Parameter:   id =  MB, name = MB
% Parameter:   id =  MC, name = MC
% Parameter:   id =  MD, name = MD
% Parameter:   id =  ME, name = ME
% Parameter:   id =  MF, name = MF
% Parameter:   id =  MG, name = MG
% Parameter:   id =  MH, name = MH
% Parameter:   id =  MI, name = MI
% Parameter:   id =  MJ, name = MJ
% Parameter:   id =  MK, name = MK
% Parameter:   id =  q45c, name = q45c
% Parameter:   id =  v1, name = v1
% Parameter:   id =  v10, name = v10
% Parameter:   id =  v100, name = v100
% Parameter:   id =  v11, name = v11
% Parameter:   id =  v12, name = v12
% Parameter:   id =  v13, name = v13
% Parameter:   id =  v15, name = v15
% Parameter:   id =  v16, name = v16
% Parameter:   id =  v17, name = v17
% Parameter:   id =  v18, name = v18
% Parameter:   id =  v19, name = v19
% Parameter:   id =  v2, name = v2
% Parameter:   id =  v20, name = v20
% Parameter:   id =  v21, name = v21
% Parameter:   id =  v22, name = v22
% Parameter:   id =  v23, name = v23
% Parameter:   id =  v24, name = v24
% Parameter:   id =  v25, name = v25
% Parameter:   id =  v26, name = v26
% Parameter:   id =  v27, name = v27
% Parameter:   id =  v28, name = v28
% Parameter:   id =  v29, name = v29
% Parameter:   id =  v3, name = v3
% Parameter:   id =  v31, name = v31
% Parameter:   id =  v32, name = v32
% Parameter:   id =  v33, name = v33
% Parameter:   id =  v34, name = v34
% Parameter:   id =  v35, name = v35
% Parameter:   id =  v36, name = v36
% Parameter:   id =  v37, name = v37
% Parameter:   id =  v38, name = v38
% Parameter:   id =  v39, name = v39
% Parameter:   id =  v4, name = v4
% Parameter:   id =  v40, name = v40
% Parameter:   id =  v42, name = v42
% Parameter:   id =  v43, name = v43
% Parameter:   id =  v44, name = v44
% Parameter:   id =  v45, name = v45
% Parameter:   id =  v46, name = v46
% Parameter:   id =  v47, name = v47
% Parameter:   id =  v48, name = v48
% Parameter:   id =  v49, name = v49
% Parameter:   id =  v5, name = v5
% Parameter:   id =  v50, name = v50
% Parameter:   id =  v51, name = v51
% Parameter:   id =  v52, name = v52
% Parameter:   id =  v53, name = v53
% Parameter:   id =  v54, name = v54
% Parameter:   id =  v55, name = v55
% Parameter:   id =  v56, name = v56
% Parameter:   id =  v6, name = v6
% Parameter:   id =  v60, name = v60
% Parameter:   id =  v61, name = v61
% Parameter:   id =  v62, name = v62
% Parameter:   id =  v63, name = v63
% Parameter:   id =  v68, name = v68
% Parameter:   id =  v69, name = v69
% Parameter:   id =  v7, name = v7
% Parameter:   id =  v72, name = v72
% Parameter:   id =  v73, name = v73
% Parameter:   id =  v74, name = v74
% Parameter:   id =  v75, name = v75
% Parameter:   id =  v78, name = v78
% Parameter:   id =  v79, name = v79
% Parameter:   id =  v8, name = v8
% Parameter:   id =  v80, name = v80
% Parameter:   id =  v81, name = v81
% Parameter:   id =  v84, name = v84
% Parameter:   id =  v85, name = v85
% Parameter:   id =  v86, name = v86
% Parameter:   id =  v88, name = v88
% Parameter:   id =  v89, name = v89
% Parameter:   id =  v9, name = v9
% Parameter:   id =  v90, name = v90
% Parameter:   id =  v91, name = v91
% Parameter:   id =  v92, name = v92
% Parameter:   id =  v93, name = v93
% Parameter:   id =  v94, name = v94
% Parameter:   id =  v95, name = v95
% Parameter:   id =  v96, name = v96
% Parameter:   id =  v97, name = v97
% Parameter:   id =  v98, name = v98
% Parameter:   id =  v99, name = v99
% Parameter:   id =  vMI, name = vMI
% Parameter:   id =  volSite, name = volSite
% Parameter:   id =  c59, name = c59
	global_par_c59=0.0;
% Parameter:   id =  c61, name = c61
	global_par_c61=0.0;
% Parameter:   id =  c62, name = c62
	global_par_c62=0.0;
% Parameter:   id =  cAPC, name = cAPC
	global_par_cAPC=0.0;
% Parameter:   id =  q45a, name = q45a
	global_par_q45a=0.0;
% Parameter:   id =  q45b, name = q45b
	global_par_q45b=0.0;
% assignmentRule: variable = APC
	global_par_APC=(x(1)+x(2))*x(30)/(global_par_cAPC+x(30));
% assignmentRule: variable = Beta91
	global_par_Beta91=global_par_Beta90;
% assignmentRule: variable = Beta92
	global_par_Beta92=global_par_Beta90;
% assignmentRule: variable = Beta93
	global_par_Beta93=global_par_Beta90;
% assignmentRule: variable = Beta94
	global_par_Beta94=global_par_Beta90;
% assignmentRule: variable = Beta97
	global_par_Beta97=global_par_Beta93;
% assignmentRule: variable = Beta98
	global_par_Beta98=global_par_Beta93;
% assignmentRule: variable = FACTOR
	global_par_FACTOR=x(8)*x(29)/(global_par_cF+x(29));
% assignmentRule: variable = INFLAM
	global_par_INFLAM=global_par_MuI*x(29)/(global_par_cF+x(29))/(global_par_cI+x(29)/(global_par_cF+x(29)));
% assignmentRule: variable = K91
	global_par_K91=global_par_K90;
% assignmentRule: variable = K92
	global_par_K92=global_par_K90;
% assignmentRule: variable = K94
	global_par_K94=global_par_K93;
% assignmentRule: variable = K97
	global_par_K97=global_par_K93;
% assignmentRule: variable = K98
	global_par_K98=global_par_K93;
% assignmentRule: variable = volSite
	global_par_volSite=global_par_volBlood;
% assignmentRule: variable = MA
	global_par_MA=x(5)/x(3);
% assignmentRule: variable = vMI
	global_par_vMI=global_par_volMI*x(3);
% assignmentRule: variable = MC
	global_par_MC=global_par_volLung/global_par_vMI;
% assignmentRule: variable = MB
	global_par_MB=x(5)/x(3)*global_par_MC;
% assignmentRule: variable = MD
	global_par_MD=global_par_volLung/global_par_volLymphT;
% assignmentRule: variable = ME
	global_par_ME=global_par_volLymphT/global_par_volBlood;
% assignmentRule: variable = MF
	global_par_MF=global_par_volBlood/global_par_volLung;
% assignmentRule: variable = MG
	global_par_MG=global_par_volLung/global_par_volBlood;
% assignmentRule: variable = MH
	global_par_MH=global_par_volBlood/global_par_volLymphB;
% assignmentRule: variable = MI
	global_par_MI=global_par_volSite/global_par_volBlood;
% assignmentRule: variable = MJ
	global_par_MJ=global_par_volLymphB/global_par_volBlood;
% assignmentRule: variable = MK
	global_par_MK=global_par_volBlood/global_par_volSite;
% assignmentRule: variable = q45c
	global_par_q45c=global_par_q45b;
% assignmentRule: variable = v1
	global_par_v1=global_par_Mu1*x(4)/global_par_volLung;
% assignmentRule: variable = v10
	global_par_v10=global_par_Eta10*x(1)/global_par_volLung;
% assignmentRule: variable = v100
	global_par_v100=global_par_Eta100*x(52)/global_par_volLung;
% assignmentRule: variable = v11
	global_par_v11=global_par_Delta11*x(2)/global_par_volLung*x(35)/(x(35)+global_par_cf11*global_par_volLung);
% assignmentRule: variable = v12
	global_par_v12=global_par_Delta12*x(1)/global_par_volLung*(x(4)+x(5))/(global_par_c12*global_par_volLung+x(4)+x(5))*x(39)/(x(39)+global_par_fi12*x(33)+global_par_cf12*global_par_volLung)+global_par_Deltai12*x(1)/global_par_volLung*x(29)/(global_par_ci12+x(29));
% assignmentRule: variable = v13
	global_par_v13=global_par_Eta13*x(2)/global_par_volLung;
% assignmentRule: variable = v15
	global_par_v15=global_par_Rho15+global_par_Mu15*x(4)/(global_par_c15*global_par_volLung+x(4))+(global_par_Rho21+global_par_Rho50)*global_par_INFLAM;
% assignmentRule: variable = v16
	global_par_v16=global_par_Eta16*x(6)/global_par_volLung;
% assignmentRule: variable = v17
	global_par_v17=global_par_Gamma17*x(6)/global_par_volLung*x(4)/(global_par_c17*global_par_volLung+x(4))+global_par_Gamma17*x(6)/global_par_volLung*1*global_par_INFLAM;
% assignmentRule: variable = v18
	global_par_v18=global_par_Eta18*x(10)/global_par_volLymphT;
% assignmentRule: variable = v19
	global_par_v19=global_par_Rho19+global_par_Mu19*x(10)/global_par_volLymphT;
% assignmentRule: variable = v2
	global_par_v2=global_par_k2*x(1)/global_par_volLung*x(4)/(x(4)+global_par_c2*global_par_volLung);
% assignmentRule: variable = v20
	global_par_v20=global_par_Eta20*x(11)/global_par_volLymphT;
% assignmentRule: variable = v21
	global_par_v21=global_par_Rho21+global_par_Delta21*x(11)/global_par_volLymphT*x(10)/global_par_volLymphT;
% assignmentRule: variable = v22
	global_par_v22=global_par_Mu22*x(12)/global_par_volLymphT/(global_par_c22+(x(12)/global_par_volLymphT)^2);
% assignmentRule: variable = v23
	global_par_v23=global_par_Gamma23*x(12)/global_par_volLymphT;
% assignmentRule: variable = v24
	global_par_v24=global_par_Gamma24*x(13)/global_par_volBlood*x(2)/(global_par_c24*global_par_volLung+x(2));
% assignmentRule: variable = v25
	global_par_v25=global_par_Mu25*x(7)/global_par_volLung*x(2)/(global_par_c25*global_par_volLung+x(2));
% assignmentRule: variable = v26
	global_par_v26=global_par_Eta26*x(7)/global_par_volLung;
% assignmentRule: variable = v27
	global_par_v27=global_par_Delta27*x(7)/global_par_volLung*x(38)/global_par_volLung*x(36)/(x(36)+global_par_fi27*x(33)+global_par_fii27*x(35)+global_par_cf27*global_par_volLung)+global_par_Deltai27*x(7)/global_par_volLung*global_par_APC;
% assignmentRule: variable = v28
	global_par_v28=global_par_Eta28*x(8)/global_par_volLung;
% assignmentRule: variable = v29
	global_par_v29=global_par_Delta29*x(7)/global_par_volLung*x(33)/(x(33)+global_par_fi29*x(39)+global_par_cf29*global_par_volLung);
% assignmentRule: variable = v3
	global_par_v3=global_par_k3*x(3)/global_par_volLung*(x(5)/x(3))^2/((x(5)/x(3))^2+global_par_K3s^2);
% assignmentRule: variable = v31
	global_par_v31=global_par_Gamma31*x(9)/global_par_volLung;
% assignmentRule: variable = v32
	global_par_v32=global_par_Gamma32*x(14)/global_par_volBlood;
% assignmentRule: variable = v33
	global_par_v33=global_par_Eta33*x(15)/global_par_volLymphB;
% assignmentRule: variable = v34
	global_par_v34=global_par_Rho34;
% assignmentRule: variable = v35
	global_par_v35=global_par_Eta35*x(16)/global_par_volSite*x(43)/(x(43)+x(44)+1E-5);
% assignmentRule: variable = v36
	global_par_v36=global_par_Delta36*x(16)/global_par_volSite*x(44)/(x(43)+x(44)+1E-5);
% assignmentRule: variable = v37
	global_par_v37=global_par_Gamma37*x(17)/global_par_volSite;
% assignmentRule: variable = v38
	global_par_v38=global_par_Delta38*x(18)/global_par_volBlood/2*x(47)/(x(47)+x(48)+1E-5);
% assignmentRule: variable = v39
	global_par_v39=global_par_Eta47*x(19)/global_par_volBlood;
% assignmentRule: variable = v4
	global_par_v4=global_par_Alpha4*x(3)/global_par_volLung*x(8)/x(3)/(x(8)/x(3)+global_par_c4*global_par_volLung);
% assignmentRule: variable = v40
	global_par_v40=global_par_Gamma40*x(18)/global_par_volBlood;
% assignmentRule: variable = v42
	global_par_v42=global_par_Mu42*x(20)/global_par_volLymphB*x(48)/(x(47)+x(48)+1E-5);
% assignmentRule: variable = v43
	global_par_v43=global_par_Delta43*x(20)/global_par_volLymphB/2*x(47)/(x(47)+x(48)+1E-5);
% assignmentRule: variable = v44
	global_par_v44=global_par_Eta44*x(21)/global_par_volLymphB;
% assignmentRule: variable = v45
	global_par_v45=global_par_q45a*x(18)/global_par_volBlood+global_par_q45b*x(20)/global_par_volBlood+global_par_q45c*x(21)/global_par_volBlood;
% assignmentRule: variable = v46
	global_par_v46=global_par_P46*(x(22)/global_par_volBlood-x(23)/global_par_volLung);
% assignmentRule: variable = v47
	global_par_v47=global_par_Eta47*x(19)/global_par_volBlood;
% assignmentRule: variable = v48
	global_par_v48=global_par_Eta48*x(20)/global_par_volLymphB;
% assignmentRule: variable = v49
	global_par_v49=global_par_Eta49*x(22)/global_par_volBlood;
% assignmentRule: variable = v5
	global_par_v5=global_par_Alpha5*x(2)/global_par_volLung*x(4)/global_par_volLung;
% assignmentRule: variable = v50
	global_par_v50=global_par_Rho50+global_par_Delta50*x(11)/global_par_volLymphT*x(10)/global_par_volLymphT;
% assignmentRule: variable = v51
	global_par_v51=global_par_Gamma51*x(24)/global_par_volLymphT;
% assignmentRule: variable = v52
	global_par_v52=global_par_Gamma52*x(25)/global_par_volBlood*x(2)/(global_par_c52*global_par_volLung+x(2));
% assignmentRule: variable = v53
	global_par_v53=global_par_Eta53*x(26)/global_par_volLung;
% assignmentRule: variable = v54
	global_par_v54=global_par_Delta54*x(26)/global_par_volLung*x(29)/global_par_volLung;
% assignmentRule: variable = v55
	global_par_v55=global_par_Mu55*x(27)/global_par_volLung*global_par_FACTOR/(global_par_cF+global_par_FACTOR);
% assignmentRule: variable = v56
	global_par_v56=global_par_Eta56*x(27)/global_par_volLung;
% assignmentRule: variable = v6
	global_par_v6=global_par_Alpha6*x(1)/global_par_volLung*x(4)/global_par_volLung;
% assignmentRule: variable = v60
	global_par_v60=global_par_Eta60*x(29)/global_par_volLung;
% assignmentRule: variable = v61
	global_par_v61=global_par_Alpha61*x(2)/global_par_volLung*x(29)/(global_par_c61+x(29));
% assignmentRule: variable = v62
	global_par_v62=global_par_Alpha61*x(27)/global_par_volLung*x(29)/(global_par_c61+x(29));
% assignmentRule: variable = v63
	global_par_v63=global_par_k63*x(30)/global_par_volLung;
% assignmentRule: variable = v68
	global_par_v68=global_par_q68a*x(7)/global_par_volLung+global_par_q68b*x(9)/global_par_volLung;
% assignmentRule: variable = v69
	global_par_v69=global_par_Eta69*x(33)/global_par_volLung;
% assignmentRule: variable = v7
	global_par_v7=global_par_k7*x(6)/global_par_volLung*x(4)/global_par_volLung;
% assignmentRule: variable = v72
	global_par_v72=global_par_q72a*x(2)/global_par_volLung*global_par_c72*global_par_volLung/(x(35)+global_par_ci72*x(39)+global_par_c72*global_par_volLung)+global_par_q72b*x(8)/global_par_volLung+global_par_q72c*x(9)/global_par_volLung+global_par_q72d*x(7)/global_par_volLung+global_par_q72e*x(3)/global_par_volLung;
% assignmentRule: variable = v73
	global_par_v73=global_par_Eta73*x(35)/global_par_volLung;
% assignmentRule: variable = v74
	global_par_v74=global_par_q74*x(10)/global_par_volLymphT;
% assignmentRule: variable = v75
	global_par_v75=global_par_Eta75*x(36)/global_par_volLymphT;
% assignmentRule: variable = v78
	global_par_v78=global_par_q78a*x(1)/global_par_volLung+global_par_q78b*x(2)/global_par_volLung;
% assignmentRule: variable = v79
	global_par_v79=global_par_Eta79*x(38)/global_par_volLung;
% assignmentRule: variable = v8
	global_par_v8=global_par_Mu8*x(5)/global_par_vMI*(1-(x(5)/x(3))^2/((x(5)/x(3))^2+global_par_K3s^2));
% assignmentRule: variable = v80
	global_par_v80=global_par_Rho80*(x(4)+x(5))/(global_par_c80*global_par_volLung+x(4)+x(5))*x(38)/(global_par_ci80*global_par_volLung+x(38))+global_par_q80*x(8)*x(2)/(global_par_cii80*global_par_volLung+x(2));
% assignmentRule: variable = v81
	global_par_v81=global_par_Eta81*x(39)/global_par_volLung;
% assignmentRule: variable = v84
	global_par_v84=global_par_Eta84*x(13)/global_par_volBlood;
% assignmentRule: variable = v85
	global_par_v85=global_par_Eta85*x(25)/global_par_volBlood;
% assignmentRule: variable = v86
	global_par_v86=global_par_Mu86*x(8)/global_par_volLung*global_par_FACTOR/(global_par_cf86+global_par_FACTOR);
% assignmentRule: variable = v88
	global_par_v88=global_par_P88*(x(30)/global_par_volLung-x(41)/global_par_volBlood);
% assignmentRule: variable = v89
	global_par_v89=global_par_P89*(x(41)/global_par_volBlood-x(42)/global_par_volSite);
% assignmentRule: variable = v9
	global_par_v9=global_par_Rho9+global_par_Mu9*(x(2)/global_par_volLung+global_par_w9*x(3)/global_par_volLung)+global_par_Mui9*global_par_INFLAM;
% assignmentRule: variable = v90
	global_par_v90=global_par_Beta90*(x(42)/global_par_volSite*x(43)/global_par_volSite-x(44)/global_par_volSite/global_par_K90);
% assignmentRule: variable = v91
	global_par_v91=global_par_Beta91*(x(42)/global_par_volSite*x(45)/global_par_volSite-x(46)/global_par_volSite/global_par_K91);
% assignmentRule: variable = v92
	global_par_v92=global_par_Beta92*(x(41)/global_par_volSite*x(47)/global_par_volSite-x(48)/global_par_volSite/global_par_K92);
% assignmentRule: variable = v93
	global_par_v93=global_par_Beta93*(x(41)/global_par_volBlood*x(22)/global_par_volBlood-x(49)/global_par_volBlood/global_par_K93);
% assignmentRule: variable = v94
	global_par_v94=global_par_Beta94*(x(41)/global_par_volBlood*x(49)/global_par_volBlood-x(50)/global_par_volBlood/global_par_K94);
% assignmentRule: variable = v95
	global_par_v95=global_par_Eta95*x(49)/global_par_volBlood;
% assignmentRule: variable = v96
	global_par_v96=global_par_Eta96*x(50)/global_par_volBlood;
% assignmentRule: variable = v97
	global_par_v97=global_par_Beta97*(x(4)/global_par_volLung*x(23)/global_par_volLung-x(51)/global_par_volLung/global_par_K97);
% assignmentRule: variable = v98
	global_par_v98=global_par_Beta98*(x(4)/global_par_volLung*x(51)/global_par_volLung-x(52)/global_par_volLung/global_par_K98);
% assignmentRule: variable = v99
	global_par_v99=global_par_Eta99*x(51)/global_par_volLung;

% Reaction: id = R_1
	reaction_R_1=global_par_Beta90*x(4)*x(51)/global_par_volLung;

% Reaction: id = R_2
	reaction_R_2=global_par_Beta90*x(52)/global_par_K93;

% Reaction: id = R_3
	reaction_R_3=global_par_Eta100*x(52);

% Reaction: id = R_4
	reaction_R_4=global_par_Beta90*x(4)*x(23)/global_par_volLung;

% Reaction: id = R_5
	reaction_R_5=global_par_Beta90*x(51)/global_par_K93;

% Reaction: id = R_6
	reaction_R_6=global_par_Eta99*x(51);

% Reaction: id = R_7
	reaction_R_7=global_par_Beta90*x(41)*x(49)/global_par_volBlood;

% Reaction: id = R_8
	reaction_R_8=global_par_Beta90*x(50)/global_par_K93;

% Reaction: id = R_9
	reaction_R_9=global_par_Eta96*x(50);

% Reaction: id = R_10
	reaction_R_10=global_par_Beta90*x(41)*x(22)/global_par_volBlood;

% Reaction: id = R_11
	reaction_R_11=global_par_Beta90*x(49)/global_par_K93;

% Reaction: id = R_12
	reaction_R_12=global_par_Eta95*x(49);

% Reaction: id = R_13
	reaction_R_13=global_par_Beta90*x(41)*x(47)/global_par_volBlood;

% Reaction: id = R_14
	reaction_R_14=global_par_Beta90*x(48)/global_par_K90;

% Reaction: id = R_15
	reaction_R_15=global_par_Gamma37*x(17);

% Reaction: id = R_16
	reaction_R_16=global_par_v41*global_par_volLymphB;

% Reaction: id = R_17
	reaction_R_17=global_par_Gamma40*x(18);

% Reaction: id = R_18
	reaction_R_18=global_par_Delta38*x(18)*x(47)/(x(47)+x(48)+1E-5);

% Reaction: id = R_19
	reaction_R_19=global_par_Beta90*x(42)*x(45)/global_par_volBlood;

% Reaction: id = R_20
	reaction_R_20=global_par_Beta90*x(46)/global_par_K90;

% Reaction: id = R_21
	reaction_R_21=global_par_Delta36*x(16)*x(44)/(x(43)+x(44)+1E-5);

% Reaction: id = R_22
	reaction_R_22=global_par_Beta90*x(42)*x(43)/global_par_volBlood;

% Reaction: id = R_23
	reaction_R_23=global_par_Beta90*x(44)/global_par_K90;

% Reaction: id = R_24
	reaction_R_24=global_par_Rho34*global_par_volBlood;

% Reaction: id = R_25
	reaction_R_25=global_par_Eta47*x(19);

% Reaction: id = R_26
	reaction_R_26=global_par_Eta35*x(16)*x(43)/(x(43)+x(44)+1E-5);

% Reaction: id = R_27
	reaction_R_27=global_par_P89*x(41);

% Reaction: id = R_28
	reaction_R_28=global_par_P89*x(42);

% Reaction: id = R_29
	reaction_R_29=global_par_v87*global_par_volLung;

% Reaction: id = R_30
	reaction_R_30=global_par_P88*x(30);

% Reaction: id = R_31
	reaction_R_31=global_par_volLung*x(41)*global_par_P88/global_par_volBlood;

% Reaction: id = R_32
	reaction_R_32=global_par_v82*global_par_volLung;

% Reaction: id = R_33
	reaction_R_33=global_par_v83*global_par_volLung;

% Reaction: id = R_34
	reaction_R_34=global_par_Rho80*global_par_volLung*x(38)*x(4)/(global_par_ci80*global_par_volLung+x(38))/(global_par_c80*global_par_volLung+x(4)+x(5))+global_par_Rho80*global_par_volLung*x(38)*x(5)/(global_par_ci80*global_par_volLung+x(38))/(global_par_c80*global_par_volLung+x(4)+x(5));

% Reaction: id = R_35
	reaction_R_35=global_par_volLung*x(2)*x(8)*global_par_q80/(global_par_cii80*global_par_volLung+x(2));

% Reaction: id = R_36
	reaction_R_36=global_par_Eta81*x(39);

% Reaction: id = R_37
	reaction_R_37=global_par_v77*global_par_volLung;

% Reaction: id = R_38
	reaction_R_38=global_par_q78a*x(1);

% Reaction: id = R_39
	reaction_R_39=global_par_q78b*x(2);

% Reaction: id = R_40
	reaction_R_40=global_par_Eta79*x(38);

% Reaction: id = R_41
	reaction_R_41=global_par_v76*global_par_volBlood;

% Reaction: id = R_42
	reaction_R_42=global_par_v77*global_par_volBlood;

% Reaction: id = R_43
	reaction_R_43=global_par_q74*x(10);

% Reaction: id = R_44
	reaction_R_44=global_par_Eta75*x(36)+global_par_v76*global_par_volLymphT;

% Reaction: id = R_45
	reaction_R_45=global_par_c72*global_par_q72a*x(2)*1/(x(35)+global_par_ci72*x(39)+global_par_c72*global_par_volLung);

% Reaction: id = R_46
	reaction_R_46=global_par_q72b*x(8);

% Reaction: id = R_47
	reaction_R_47=global_par_q72c*x(9);

% Reaction: id = R_48
	reaction_R_48=global_par_q72d*x(7);

% Reaction: id = R_49
	reaction_R_49=global_par_q72e*x(3);

% Reaction: id = R_50
	reaction_R_50=global_par_Eta73*x(35);

% Reaction: id = R_51
	reaction_R_51=global_par_v70*global_par_volLymphB;

% Reaction: id = R_52
	reaction_R_52=global_par_v71*global_par_volLymphB;

% Reaction: id = R_53
	reaction_R_53=global_par_q68a*x(7);

% Reaction: id = R_54
	reaction_R_54=global_par_q68b*x(9);

% Reaction: id = R_55
	reaction_R_55=global_par_Eta69*x(33);

% Reaction: id = R_56
	reaction_R_56=global_par_v66*global_par_volLung;

% Reaction: id = R_57
	reaction_R_57=global_par_v67*global_par_volLung;

% Reaction: id = R_58
	reaction_R_58=global_par_v64*global_par_volLung;

% Reaction: id = R_59
	reaction_R_59=global_par_v65*global_par_volLung;

% Reaction: id = R_60
	reaction_R_60=global_par_Eta60*x(29);

% Reaction: id = R_61
	reaction_R_61=global_par_Alpha61*x(2)*x(29)/(100000+x(29));

% Reaction: id = R_62
	reaction_R_62=global_par_Alpha61*x(27)*x(29)/(100000+x(29));

% Reaction: id = R_63
	reaction_R_63=global_par_k63*x(30);

% Reaction: id = R_64
	reaction_R_64=global_par_v59*global_par_volLung;

% Reaction: id = R_65
	reaction_R_65=global_par_v57*global_par_volLung;

% Reaction: id = R_66
	reaction_R_66=global_par_v58*global_par_volLung;

% Reaction: id = R_67
	reaction_R_67=x(26)*x(29)*global_par_Delta54/global_par_volLung;

% Reaction: id = R_68
	reaction_R_68=x(8)*x(27)/global_par_volLung*x(29)*global_par_volLung*global_par_Mu55/(global_par_cF+x(29))/(global_par_cF+global_par_FACTOR);

% Reaction: id = R_69
	reaction_R_69=global_par_Eta56*x(27);

% Reaction: id = R_70
	reaction_R_70=global_par_Gamma52*x(25)*x(2)/(global_par_c52*global_par_volLung+x(2));

% Reaction: id = R_71
	reaction_R_71=global_par_Eta53*x(26);

% Reaction: id = R_72
	reaction_R_72=global_par_Gamma51*x(24);

% Reaction: id = R_73
	reaction_R_73=global_par_Eta85*x(25);

% Reaction: id = R_74
	reaction_R_74=global_par_Rho50*global_par_volLymphT;

% Reaction: id = R_75
	reaction_R_75=x(10)*x(11)*global_par_Delta50/global_par_volLymphT;

% Reaction: id = R_76
	reaction_R_76=global_par_P46*x(22);

% Reaction: id = R_77
	reaction_R_77=global_par_volBlood*x(23)*global_par_P46/global_par_volLung;

% Reaction: id = R_78
	reaction_R_78=x(18);

% Reaction: id = R_79
	reaction_R_79=x(20);

% Reaction: id = R_80
	reaction_R_80=x(21);

% Reaction: id = R_81
	reaction_R_81=global_par_Eta49*x(22);

% Reaction: id = R_82
	reaction_R_82=global_par_Delta43*x(20)*x(47)/(x(47)+x(48)+1E-5);

% Reaction: id = R_83
	reaction_R_83=global_par_Eta44*x(21);

% Reaction: id = R_84
	reaction_R_84=global_par_Mu42*x(20)*x(48)/(x(47)+x(48)+1E-5);

% Reaction: id = R_85
	reaction_R_85=global_par_Eta48*x(20);

% Reaction: id = R_86
	reaction_R_86=global_par_Gamma32*x(14);

% Reaction: id = R_87
	reaction_R_87=global_par_Eta33*x(15);

% Reaction: id = R_88
	reaction_R_88=global_par_Gamma31*x(9);

% Reaction: id = R_89
	reaction_R_89=global_par_Gamma23*x(12);

% Reaction: id = R_90
	reaction_R_90=global_par_Gamma24*x(13)*x(2)/(global_par_c24*global_par_volLung+x(2));

% Reaction: id = R_91
	reaction_R_91=global_par_Eta84*x(13);

% Reaction: id = R_92
	reaction_R_92=global_par_Rho21*global_par_volLymphT;

% Reaction: id = R_93
	reaction_R_93=x(10)*x(11)*global_par_Delta21/global_par_volLymphT;

% Reaction: id = R_94
	reaction_R_94=x(12)*global_par_Mu22/(global_par_c22+(x(12)/global_par_volLymphT)^2);

% Reaction: id = R_95
	reaction_R_95=global_par_Rho19*global_par_volLymphT;

% Reaction: id = R_96
	reaction_R_96=global_par_Mu19*x(10);

% Reaction: id = R_97
	reaction_R_97=global_par_Eta20*x(11);

% Reaction: id = R_98
	reaction_R_98=global_par_Gamma17*x(6)*x(4)/(global_par_c17*global_par_volLung+x(4));

% Reaction: id = R_99
	reaction_R_99=x(6)/global_par_volLymphT/global_par_volLung*x(29)*global_par_volLymphT*global_par_volLung*global_par_MuI*global_par_Gamma17/(global_par_cF+x(29))/(global_par_cI+x(29)/(global_par_cF+x(29)));

% Reaction: id = R_100
	reaction_R_100=global_par_Eta18*x(10);

% Reaction: id = R_101
	reaction_R_101=global_par_Delta29*x(7)*x(33)/(x(33)+global_par_fi29*x(39)+global_par_cf29*global_par_volLung);

% Reaction: id = R_102
	reaction_R_102=global_par_v30*global_par_volLung;

% Reaction: id = R_103
	reaction_R_103=x(36)*x(38)*global_par_Delta27/global_par_volLung*x(7)/(x(36)+global_par_fi27*x(33)+global_par_fii27*x(35)+global_par_cf27*global_par_volLung);

% Reaction: id = R_104
	reaction_R_104=global_par_Deltai27*x(1)*x(30)*x(7)/(10000000+x(30));

% Reaction: id = R_105
	reaction_R_105=global_par_Deltai27*x(2)*x(30)*x(7)/(10000000+x(30));

% Reaction: id = R_106
	reaction_R_106=x(8)*x(8)/global_par_volLung*x(29)*global_par_volLung*global_par_Mu86/(global_par_cF+x(29))/(global_par_cf86+global_par_FACTOR);

% Reaction: id = R_107
	reaction_R_107=global_par_Eta28*x(8);

% Reaction: id = R_108
	reaction_R_108=global_par_Mu25*x(7)*x(2)/(global_par_c25*global_par_volLung+x(2));

% Reaction: id = R_109
	reaction_R_109=global_par_Eta26*x(7);

% Reaction: id = R_110
	reaction_R_110=global_par_Deltai27*x(2)*x(7)*x(30)/(10000000+x(30));

% Reaction: id = R_111
	reaction_R_111=global_par_Rho15*global_par_volLung;

% Reaction: id = R_112
	reaction_R_112=global_par_volLung*x(4)*global_par_Mu15/(global_par_c15*global_par_volLung+x(4));

% Reaction: id = R_113
	reaction_R_113=x(29)*global_par_volLung*global_par_Rho21*global_par_MuI/(global_par_cF+x(29))/(global_par_cI+x(29)/(global_par_cF+x(29)))+x(29)*global_par_volLung*global_par_Rho50*global_par_MuI/(global_par_cF+x(29))/(global_par_cI+x(29)/(global_par_cF+x(29)));

% Reaction: id = R_114
	reaction_R_114=global_par_Eta16*x(6);

% Reaction: id = R_115
	reaction_R_115=x(6)/global_par_volLung*x(29)*global_par_volLung*global_par_MuI*global_par_Gamma17/(global_par_cF+x(29))/(global_par_cI+x(29)/(global_par_cF+x(29)));

% Reaction: id = R_116
	reaction_R_116=x(3)*x(1)*x(4)*25*global_par_k2/(x(4)+global_par_c2*global_par_volLung)/x(3);

% Reaction: id = R_117
	reaction_R_117=global_par_Mu8*x(3)*x(5)/x(3);

% Reaction: id = R_118
	reaction_R_118=global_par_Mu8*x(3)*x(5)*(x(5)/x(3))^2/((x(5)/x(3))^2+global_par_K3s^2)/x(3)+x(3)*x(5)*global_par_volLung*global_par_v14/x(3)/x(3)+x(3)*x(5)*x(3)/x(3)*(x(5)/x(3))^2*global_par_k3/((x(5)/x(3))^2+global_par_K3s^2)/x(3);

% Reaction: id = R_119
	reaction_R_119=x(3)*x(5)*x(3)/x(3)*x(8)*global_par_Alpha4/x(3)/(x(8)/x(3)+global_par_c4*global_par_volLung)/x(3);

% Reaction: id = R_120
	reaction_R_120=global_par_Mu1*x(4);

% Reaction: id = R_121
	reaction_R_121=global_par_volLung*x(5)*global_par_v14/x(3)+global_par_k3*x(3)*1/((x(5)/x(3))^2+global_par_K3s^2)*x(5)/x(3)*(x(5)/x(3))^2;

% Reaction: id = R_122
	reaction_R_122=x(3)*x(5)/global_par_volLung/x(3)*x(8)*global_par_volLung*global_par_Alpha4/x(3)/(x(8)/x(3)+global_par_c4*global_par_volLung);

% Reaction: id = R_123
	reaction_R_123=x(2)*x(4)*global_par_Alpha5/global_par_volLung;

% Reaction: id = R_124
	reaction_R_124=x(1)*x(4)*global_par_Alpha6/global_par_volLung;

% Reaction: id = R_125
	reaction_R_125=x(4)*x(6)*global_par_k7/global_par_volLung;

% Reaction: id = R_126
	reaction_R_126=global_par_k2*x(1)*x(4)/(x(4)+global_par_c2*global_par_volLung);

% Reaction: id = R_127
	reaction_R_127=global_par_v14*global_par_volLung;

% Reaction: id = R_128
	reaction_R_128=global_par_k3*x(3)/((x(5)/x(3))^2+global_par_K3s^2)*(x(5)/x(3))^2;

% Reaction: id = R_129
	reaction_R_129=x(3)/global_par_volLung*x(8)*global_par_volLung*global_par_Alpha4/x(3)/(x(8)/x(3)+global_par_c4*global_par_volLung);

% Reaction: id = R_130
	reaction_R_130=global_par_Delta12*x(1)*x(39)/(x(39)+global_par_fi12*x(33)+global_par_cf12*global_par_volLung)*x(4)/(global_par_c12*global_par_volLung+x(4)+x(5));

% Reaction: id = R_131
	reaction_R_131=global_par_Delta12*x(1)*x(39)/(x(39)+global_par_fi12*x(33)+global_par_cf12*global_par_volLung)*x(5)/(global_par_c12*global_par_volLung+x(4)+x(5));

% Reaction: id = R_132
	reaction_R_132=global_par_Deltai12*x(1)*x(29)/(global_par_ci12+x(29));

% Reaction: id = R_133
	reaction_R_133=global_par_Eta13*x(2);

% Reaction: id = R_134
	reaction_R_134=global_par_Delta11*x(2)*x(35)/(x(35)+global_par_cf11*global_par_volLung);

% Reaction: id = R_135
	reaction_R_135=global_par_Rho9*global_par_volLung;

% Reaction: id = R_136
	reaction_R_136=global_par_Mu9*x(2);

% Reaction: id = R_137
	reaction_R_137=global_par_Mu9*global_par_w9*x(3);

% Reaction: id = R_138
	reaction_R_138=x(29)*global_par_volLung*global_par_Mui9*global_par_MuI/(global_par_cF+x(29))/(global_par_cI+x(29)/(global_par_cF+x(29)));

% Reaction: id = R_139
	reaction_R_139=global_par_Eta10*x(1);

% Reaction: id = R_140
	reaction_R_140=global_par_Delta12*x(39)*x(1)/(x(39)+global_par_fi12*x(33)+global_par_cf12*global_par_volLung)*x(4)/(global_par_c12*global_par_volLung+x(4)+x(5));

	xdot=zeros(52,1);
	
% Species:   id = x_1, name = x_1, affected by kineticLaw
	xdot(1) = (1/(compartment_compartmentOne))*((-1.0 * reaction_R_38) + ( 1.0 * reaction_R_38) + (-1.0 * reaction_R_65) + (-1.0 * reaction_R_104) + ( 1.0 * reaction_R_104) + (-1.0 * reaction_R_116) + ( 1.0 * reaction_R_116) + (-1.0 * reaction_R_124) + ( 1.0 * reaction_R_124) + (-1.0 * reaction_R_126) + (-1.0 * reaction_R_130) + ( 1.0 * reaction_R_130) + (-1.0 * reaction_R_131) + (-1.0 * reaction_R_132) + ( 1.0 * reaction_R_134) + ( 1.0 * reaction_R_135) + ( 1.0 * reaction_R_136) + ( 1.0 * reaction_R_137) + ( 1.0 * reaction_R_138) + (-1.0 * reaction_R_139) + (-1.0 * reaction_R_140));
	
% Species:   id = x_2, name = x_2, affected by kineticLaw
	xdot(2) = (1/(compartment_compartmentOne))*((-1.0 * reaction_R_35) + ( 1.0 * reaction_R_35) + (-1.0 * reaction_R_39) + ( 1.0 * reaction_R_39) + (-1.0 * reaction_R_45) + ( 1.0 * reaction_R_45) + (-1.0 * reaction_R_61) + ( 1.0 * reaction_R_61) + (-1.0 * reaction_R_70) + ( 1.0 * reaction_R_70) + (-1.0 * reaction_R_90) + ( 1.0 * reaction_R_90) + (-1.0 * reaction_R_105) + ( 1.0 * reaction_R_105) + (-1.0 * reaction_R_108) + ( 1.0 * reaction_R_108) + (-1.0 * reaction_R_110) + ( 1.0 * reaction_R_110) + (-1.0 * reaction_R_123) + ( 1.0 * reaction_R_123) + ( 1.0 * reaction_R_130) + ( 1.0 * reaction_R_131) + ( 1.0 * reaction_R_132) + (-1.0 * reaction_R_133) + (-1.0 * reaction_R_134) + (-1.0 * reaction_R_136) + ( 1.0 * reaction_R_136));
	
% Species:   id = x_3, name = x_3, affected by kineticLaw
	xdot(3) = (1/(compartment_compartmentOne))*((-1.0 * reaction_R_49) + ( 1.0 * reaction_R_49) + (-1.0 * reaction_R_116) + ( 1.0 * reaction_R_116) + (-1.0 * reaction_R_117) + ( 1.0 * reaction_R_117) + (-1.0 * reaction_R_118) + ( 1.0 * reaction_R_118) + (-1.0 * reaction_R_119) + ( 1.0 * reaction_R_119) + (-1.0 * reaction_R_121) + ( 1.0 * reaction_R_121) + (-1.0 * reaction_R_122) + ( 1.0 * reaction_R_122) + ( 1.0 * reaction_R_126) + (-1.0 * reaction_R_127) + (-1.0 * reaction_R_128) + (-1.0 * reaction_R_129) + (-1.0 * reaction_R_137) + ( 1.0 * reaction_R_137));
	
% Species:   id = x_4, name = x_4, affected by kineticLaw
	xdot(4) = (1/(compartment_compartmentOne))*((-1.0 * reaction_R_1) + ( 1.0 * reaction_R_2) + (-1.0 * reaction_R_4) + ( 1.0 * reaction_R_5) + (-1.0 * reaction_R_29) + (-1.0 * reaction_R_34) + ( 1.0 * reaction_R_34) + (-1.0 * reaction_R_98) + ( 1.0 * reaction_R_98) + (-1.0 * reaction_R_112) + ( 1.0 * reaction_R_112) + (-1.0 * reaction_R_116) + ( 1.0 * reaction_R_116) + (-1.0 * reaction_R_120) + ( 2.0 * reaction_R_120) + ( 1.0 * reaction_R_121) + ( 1.0 * reaction_R_122) + (-1.0 * reaction_R_123) + (-1.0 * reaction_R_124) + (-1.0 * reaction_R_125) + (-25.0 * reaction_R_126) + (-1.0 * reaction_R_130) + ( 1.0 * reaction_R_130) + (-1.0 * reaction_R_131) + ( 1.0 * reaction_R_131) + (-1.0 * reaction_R_140) + ( 1.0 * reaction_R_140));
	
% Species:   id = x_5, name = x_5, affected by kineticLaw
	xdot(5) = (1/(compartment_compartmentOne))*((-1.0 * reaction_R_34) + ( 1.0 * reaction_R_34) + ( 1.0 * reaction_R_116) + (-1.0 * reaction_R_117) + ( 2.0 * reaction_R_117) + (-1.0 * reaction_R_118) + (-1.0 * reaction_R_119) + (-1.0 * reaction_R_121) + ( 1.0 * reaction_R_121) + (-1.0 * reaction_R_122) + ( 1.0 * reaction_R_122) + (-1.0 * reaction_R_128) + ( 1.0 * reaction_R_128) + (-1.0 * reaction_R_130) + ( 1.0 * reaction_R_130) + (-1.0 * reaction_R_131) + ( 1.0 * reaction_R_131) + (-1.0 * reaction_R_140) + ( 1.0 * reaction_R_140));
	
% Species:   id = x_6, name = x_6, affected by kineticLaw
	xdot(6) = (1/(compartment_compartmentOne))*((-1.0 * reaction_R_98) + (-1.0 * reaction_R_99) + ( 1.0 * reaction_R_99) + ( 1.0 * reaction_R_111) + ( 1.0 * reaction_R_112) + ( 1.0 * reaction_R_113) + (-1.0 * reaction_R_114) + (-1.0 * reaction_R_115) + (-1.0 * reaction_R_125) + ( 1.0 * reaction_R_125));
	
% Species:   id = x_7, name = x_7, affected by kineticLaw
	xdot(7) = (1/(compartment_compartmentOne))*((-1.0 * reaction_R_48) + ( 1.0 * reaction_R_48) + (-1.0 * reaction_R_53) + ( 1.0 * reaction_R_53) + ( 1.0 * reaction_R_90) + (-1.0 * reaction_R_101) + (-1.0 * reaction_R_103) + (-1.0 * reaction_R_104) + (-1.0 * reaction_R_105) + ( 1.0 * reaction_R_105) + (-1.0 * reaction_R_108) + ( 2.0 * reaction_R_108) + (-1.0 * reaction_R_109) + (-1.0 * reaction_R_110));
	
% Species:   id = x_8, name = x_8, affected by kineticLaw
	xdot(8) = (1/(compartment_compartmentOne))*((-1.0 * reaction_R_35) + ( 1.0 * reaction_R_35) + (-1.0 * reaction_R_46) + ( 1.0 * reaction_R_46) + (-1.0 * reaction_R_68) + ( 1.0 * reaction_R_68) + ( 1.0 * reaction_R_103) + ( 1.0 * reaction_R_104) + ( 1.0 * reaction_R_105) + (-1.0 * reaction_R_106) + ( 2.0 * reaction_R_106) + (-1.0 * reaction_R_107) + (-1.0 * reaction_R_119) + ( 1.0 * reaction_R_119) + (-1.0 * reaction_R_122) + ( 1.0 * reaction_R_122) + (-1.0 * reaction_R_129) + ( 1.0 * reaction_R_129));
	
% Species:   id = x_9, name = x_9, affected by kineticLaw
	xdot(9) = (1/(compartment_compartmentOne))*((-1.0 * reaction_R_47) + ( 1.0 * reaction_R_47) + (-1.0 * reaction_R_54) + ( 1.0 * reaction_R_54) + (-1.0 * reaction_R_88) + ( 1.0 * reaction_R_101) + (-1.0 * reaction_R_102));
	
% Species:   id = x_10, name = x_10, affected by kineticLaw
	xdot(10) = (1/(compartment_compartmentOne))*((-1.0 * reaction_R_43) + ( 1.0 * reaction_R_43) + (-1.0 * reaction_R_75) + ( 1.0 * reaction_R_75) + (-1.0 * reaction_R_93) + ( 1.0 * reaction_R_93) + (-1.0 * reaction_R_96) + ( 1.0 * reaction_R_96) + ( 1.0 * reaction_R_98) + ( 1.0 * reaction_R_99) + (-1.0 * reaction_R_100));
	
% Species:   id = x_11, name = x_11, affected by kineticLaw
	xdot(11) = (1/(compartment_compartmentOne))*((-1.0 * reaction_R_74) + (-1.0 * reaction_R_75) + (-1.0 * reaction_R_92) + (-1.0 * reaction_R_93) + ( 1.0 * reaction_R_95) + ( 1.0 * reaction_R_96) + (-1.0 * reaction_R_97));
	
% Species:   id = x_12, name = x_12, affected by kineticLaw
	xdot(12) = (1/(compartment_compartmentOne))*((-1.0 * reaction_R_89) + ( 1.0 * reaction_R_92) + ( 1.0 * reaction_R_93) + (-1.0 * reaction_R_94) + ( 2.0 * reaction_R_94));
	
% Species:   id = x_13, name = x_13, affected by kineticLaw
	xdot(13) = (1/(compartment_compartmentOne))*(( 1.0 * reaction_R_89) + (-1.0 * reaction_R_90) + (-1.0 * reaction_R_91));
	
% Species:   id = x_14, name = x_14, affected by kineticLaw
	xdot(14) = (1/(compartment_compartmentOne))*((-1.0 * reaction_R_86) + ( 1.0 * reaction_R_88));
	
% Species:   id = x_15, name = x_15, affected by kineticLaw
	xdot(15) = (1/(compartment_compartmentOne))*(( 1.0 * reaction_R_86) + (-1.0 * reaction_R_87));
	
% Species:   id = x_16, name = x_16, affected by kineticLaw
	xdot(16) = (1/(compartment_compartmentOne))*((-1.0 * reaction_R_21) + ( 1.0 * reaction_R_24) + ( 1.0 * reaction_R_25) + (-1.0 * reaction_R_26));
	
% Species:   id = x_17, name = x_17, affected by kineticLaw
	xdot(17) = (1/(compartment_compartmentOne))*((-1.0 * reaction_R_15) + ( 1.0 * reaction_R_21));
	
% Species:   id = x_18, name = x_18, affected by kineticLaw
	xdot(18) = (1/(compartment_compartmentOne))*(( 1.0 * reaction_R_15) + ( 1.0 * reaction_R_16) + (-1.0 * reaction_R_17) + (-0.5 * reaction_R_18) + (-1.0 * reaction_R_78) + ( 1.0 * reaction_R_78));
	
% Species:   id = x_19, name = x_19, affected by kineticLaw
	xdot(19) = (1/(compartment_compartmentOne))*(( 0.5 * reaction_R_18) + (-2.0 * reaction_R_25));
	
% Species:   id = x_20, name = x_20, affected by kineticLaw
	xdot(20) = (1/(compartment_compartmentOne))*((-1.0 * reaction_R_16) + ( 1.0 * reaction_R_17) + (-1.0 * reaction_R_79) + ( 1.0 * reaction_R_79) + (-0.5 * reaction_R_82) + (-1.0 * reaction_R_84) + ( 2.0 * reaction_R_84) + (-1.0 * reaction_R_85));
	
% Species:   id = x_21, name = x_21, affected by kineticLaw
	xdot(21) = (1/(compartment_compartmentOne))*((-1.0 * reaction_R_80) + ( 1.0 * reaction_R_80) + ( 0.5 * reaction_R_82) + (-1.0 * reaction_R_83));
	
% Species:   id = x_22, name = x_22, affected by kineticLaw
	xdot(22) = (1/(compartment_compartmentOne))*((-1.0 * reaction_R_10) + ( 1.0 * reaction_R_11) + (-1.0 * reaction_R_76) + ( 1.0 * reaction_R_77) + ( 1000000.0 * reaction_R_78) + ( 1000000.0 * reaction_R_79) + ( 1000000.0 * reaction_R_80) + (-1.0 * reaction_R_81));
	
% Species:   id = x_23, name = x_23, affected by kineticLaw
	xdot(23) = (1/(compartment_compartmentOne))*((-1.0 * reaction_R_4) + ( 1.0 * reaction_R_5) + ( 1.0 * reaction_R_76) + (-1.0 * reaction_R_77));
	
% Species:   id = x_24, name = x_24, affected by kineticLaw
	xdot(24) = (1/(compartment_compartmentOne))*((-1.0 * reaction_R_72) + ( 1.0 * reaction_R_74) + ( 1.0 * reaction_R_75));
	
% Species:   id = x_25, name = x_25, affected by kineticLaw
	xdot(25) = (1/(compartment_compartmentOne))*((-1.0 * reaction_R_70) + ( 1.0 * reaction_R_72) + (-1.0 * reaction_R_73));
	
% Species:   id = x_26, name = x_26, affected by kineticLaw
	xdot(26) = (1/(compartment_compartmentOne))*((-1.0 * reaction_R_67) + ( 1.0 * reaction_R_70) + (-1.0 * reaction_R_71));
	
% Species:   id = x_27, name = x_27, affected by kineticLaw
	xdot(27) = (1/(compartment_compartmentOne))*((-1.0 * reaction_R_62) + ( 1.0 * reaction_R_62) + ( 1.0 * reaction_R_67) + (-1.0 * reaction_R_68) + ( 2.0 * reaction_R_68) + (-1.0 * reaction_R_69));
	
% Species:   id = x_28, name = x_28, affected by kineticLaw
	xdot(28) = (1/(compartment_compartmentOne))*(( 1.0 * reaction_R_65) + (-1.0 * reaction_R_66));
	
% Species:   id = x_29, name = x_29, affected by kineticLaw
	xdot(29) = (1/(compartment_compartmentOne))*((-1.0 * reaction_R_60) + (-1.0 * reaction_R_61) + (-1.0 * reaction_R_62) + ( 1.0 * reaction_R_64) + (-1.0 * reaction_R_67) + ( 1.0 * reaction_R_67) + (-1.0 * reaction_R_68) + ( 1.0 * reaction_R_68) + (-1.0 * reaction_R_99) + ( 1.0 * reaction_R_99) + (-1.0 * reaction_R_106) + ( 1.0 * reaction_R_106) + (-1.0 * reaction_R_113) + ( 1.0 * reaction_R_113) + (-1.0 * reaction_R_115) + ( 1.0 * reaction_R_115) + (-1.0 * reaction_R_132) + ( 1.0 * reaction_R_132) + (-1.0 * reaction_R_138) + ( 1.0 * reaction_R_138));
	
% Species:   id = x_30, name = x_30, affected by kineticLaw
	xdot(30) = (1/(compartment_compartmentOne))*((-1.0 * reaction_R_30) + ( 1.0 * reaction_R_31) + ( 1.0 * reaction_R_60) + ( 1.0 * reaction_R_61) + ( 1.0 * reaction_R_62) + (-1.0 * reaction_R_63) + (-1.0 * reaction_R_104) + ( 1.0 * reaction_R_104) + (-1.0 * reaction_R_105) + ( 1.0 * reaction_R_105) + (-1.0 * reaction_R_110) + ( 1.0 * reaction_R_110));
	
% Species:   id = x_31, name = x_31, affected by kineticLaw
	xdot(31) = (1/(compartment_compartmentOne))*(( 1.0 * reaction_R_58) + (-1.0 * reaction_R_59));
	
% Species:   id = x_32, name = x_32, affected by kineticLaw
	xdot(32) = (1/(compartment_compartmentOne))*(( 1.0 * reaction_R_56) + (-1.0 * reaction_R_57));
	
% Species:   id = x_33, name = x_33, affected by kineticLaw
	xdot(33) = (1/(compartment_compartmentOne))*(( 1.0 * reaction_R_53) + ( 1.0 * reaction_R_54) + (-1.0 * reaction_R_55) + (-1.0 * reaction_R_101) + ( 1.0 * reaction_R_101) + (-1.0 * reaction_R_103) + ( 1.0 * reaction_R_103) + (-1.0 * reaction_R_130) + ( 1.0 * reaction_R_130) + (-1.0 * reaction_R_131) + ( 1.0 * reaction_R_131) + (-1.0 * reaction_R_140) + ( 1.0 * reaction_R_140));
	
% Species:   id = x_34, name = x_34, affected by kineticLaw
	xdot(34) = (1/(compartment_compartmentOne))*(( 1.0 * reaction_R_51) + (-1.0 * reaction_R_52));
	
% Species:   id = x_35, name = x_35, affected by kineticLaw
	xdot(35) = (1/(compartment_compartmentOne))*((-1.0 * reaction_R_45) + ( 2.0 * reaction_R_45) + ( 1.0 * reaction_R_46) + ( 1.0 * reaction_R_47) + ( 1.0 * reaction_R_48) + ( 1.0 * reaction_R_49) + (-1.0 * reaction_R_50) + (-1.0 * reaction_R_103) + ( 1.0 * reaction_R_103) + (-1.0 * reaction_R_134) + ( 1.0 * reaction_R_134));
	
% Species:   id = x_36, name = x_36, affected by kineticLaw
	xdot(36) = (1/(compartment_compartmentOne))*(( 1.0 * reaction_R_43) + (-1.0 * reaction_R_44) + (-1.0 * reaction_R_103) + ( 1.0 * reaction_R_103));
	
% Species:   id = x_37, name = x_37, affected by kineticLaw
	xdot(37) = (1/(compartment_compartmentOne))*(( 1.0 * reaction_R_41) + (-1.0 * reaction_R_42));
	
% Species:   id = x_38, name = x_38, affected by kineticLaw
	xdot(38) = (1/(compartment_compartmentOne))*((-1.0 * reaction_R_34) + ( 1.0 * reaction_R_34) + ( 1.0 * reaction_R_37) + ( 1.0 * reaction_R_38) + ( 1.0 * reaction_R_39) + (-1.0 * reaction_R_40) + (-1.0 * reaction_R_103) + ( 1.0 * reaction_R_103));
	
% Species:   id = x_39, name = x_39, affected by kineticLaw
	xdot(39) = (1/(compartment_compartmentOne))*(( 1.0 * reaction_R_34) + ( 1.0 * reaction_R_35) + (-1.0 * reaction_R_36) + (-1.0 * reaction_R_45) + ( 1.0 * reaction_R_45) + (-1.0 * reaction_R_101) + ( 1.0 * reaction_R_101) + (-1.0 * reaction_R_130) + ( 1.0 * reaction_R_130) + (-1.0 * reaction_R_131) + ( 1.0 * reaction_R_131) + (-1.0 * reaction_R_140) + ( 1.0 * reaction_R_140));
	
% Species:   id = x_40, name = x_40, affected by kineticLaw
	xdot(40) = (1/(compartment_compartmentOne))*(( 1.0 * reaction_R_32) + (-1.0 * reaction_R_33));
	
% Species:   id = x_41, name = x_41, affected by kineticLaw
	xdot(41) = (1/(compartment_compartmentOne))*((-1.0 * reaction_R_7) + ( 1.0 * reaction_R_8) + (-1.0 * reaction_R_10) + ( 1.0 * reaction_R_11) + (-1.0 * reaction_R_13) + ( 1.0 * reaction_R_14) + (-1.0 * reaction_R_27) + ( 1.0 * reaction_R_28) + ( 1.0 * reaction_R_29) + ( 1.0 * reaction_R_30) + (-1.0 * reaction_R_31));
	
% Species:   id = x_42, name = x_42, affected by kineticLaw
	xdot(42) = (1/(compartment_compartmentOne))*((-1.0 * reaction_R_19) + ( 1.0 * reaction_R_20) + (-1.0 * reaction_R_22) + ( 1.0 * reaction_R_23) + ( 1.0 * reaction_R_27) + (-1.0 * reaction_R_28));
	
% Species:   id = x_43, name = x_43, affected by kineticLaw
	xdot(43) = (1/(compartment_compartmentOne))*((-1000.0 * reaction_R_21) + (-1.0 * reaction_R_22) + ( 1.0 * reaction_R_23) + ( 1000.0 * reaction_R_24) + ( 1000.0 * reaction_R_25) + (-1000.0 * reaction_R_26));
	
% Species:   id = x_44, name = x_44, affected by kineticLaw
	xdot(44) = (1/(compartment_compartmentOne))*((-1.0 * reaction_R_21) + ( 1.0 * reaction_R_21) + ( 1.0 * reaction_R_22) + (-1.0 * reaction_R_23) + (-1.0 * reaction_R_26) + ( 1.0 * reaction_R_26));
	
% Species:   id = x_45, name = x_45, affected by kineticLaw
	xdot(45) = (1/(compartment_compartmentOne))*((-1000.0 * reaction_R_15) + (-1.0 * reaction_R_19) + ( 1.0 * reaction_R_20) + ( 1000.0 * reaction_R_21));
	
% Species:   id = x_46, name = x_46, affected by kineticLaw
	xdot(46) = (1/(compartment_compartmentOne))*(( 1.0 * reaction_R_19) + (-1.0 * reaction_R_20));
	
% Species:   id = x_47, name = x_47, affected by kineticLaw
	xdot(47) = (1/(compartment_compartmentOne))*((-1.0 * reaction_R_13) + ( 1.0 * reaction_R_14) + ( 1000.0 * reaction_R_15) + ( 1000.0 * reaction_R_16) + (-1000.0 * reaction_R_17) + (-500.0 * reaction_R_18) + (-1.0 * reaction_R_82) + ( 1.0 * reaction_R_82) + (-1.0 * reaction_R_84) + ( 1.0 * reaction_R_84));
	
% Species:   id = x_48, name = x_48, affected by kineticLaw
	xdot(48) = (1/(compartment_compartmentOne))*(( 1.0 * reaction_R_13) + (-1.0 * reaction_R_14) + (-1.0 * reaction_R_18) + ( 1.0 * reaction_R_18) + (-1.0 * reaction_R_82) + ( 1.0 * reaction_R_82) + (-1.0 * reaction_R_84) + ( 1.0 * reaction_R_84));
	
% Species:   id = x_49, name = x_49, affected by kineticLaw
	xdot(49) = (1/(compartment_compartmentOne))*((-1.0 * reaction_R_7) + ( 1.0 * reaction_R_8) + ( 1.0 * reaction_R_10) + (-1.0 * reaction_R_11) + (-1.0 * reaction_R_12));
	
% Species:   id = x_50, name = x_50, affected by kineticLaw
	xdot(50) = (1/(compartment_compartmentOne))*(( 1.0 * reaction_R_7) + (-1.0 * reaction_R_8) + (-1.0 * reaction_R_9));
	
% Species:   id = x_51, name = x_51, affected by kineticLaw
	xdot(51) = (1/(compartment_compartmentOne))*((-1.0 * reaction_R_1) + ( 1.0 * reaction_R_2) + ( 1.0 * reaction_R_4) + (-1.0 * reaction_R_5) + (-1.0 * reaction_R_6));
	
% Species:   id = x_52, name = x_52, affected by kineticLaw
	xdot(52) = (1/(compartment_compartmentOne))*(( 1.0 * reaction_R_1) + (-1.0 * reaction_R_2) + (-1.0 * reaction_R_3));
end

% adding few functions representing operators used in SBML but not present directly 
% in either matlab or octave. 
function z=pow(x,y),z=x^y;end
function z=root(x,y),z=y^(1/x);end
%function z = piecewise(varargin)
%	numArgs = nargin;
%	result = 0;
%	foundResult = 0;
%	for k=1:2: numArgs-1
%		if varargin{k+1} == 1
%			result = varargin{k};
%			foundResult = 1;
%			break;
%		end
%	end
%	if foundResult == 0
%		result = varargin{numArgs};
%	end
%	z = result;
%end


