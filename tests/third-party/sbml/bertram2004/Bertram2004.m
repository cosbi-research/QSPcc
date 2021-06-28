% This file works with MATLAB and is automatically generated with 
% the System Biology Format Converter (http://sbfc.sourceforge.net/)
% from an SBML file. 
% To run this file with Octave you must edit the comments providing
% the definition of the ode solver and the signature for the 
% xdot function.
%
% The conversion system has the following limitations:
%  - You may have to re order some reactions and Assignment Rules definition
%  - Delays are not taken into account
%  - You should change the lsode parameters (start, end, steps) to get better results
%

%
% Model name = Bertram2004_PancreaticBetaCell_modelB
%
% is http://identifiers.org/biomodels.db/MODEL1006230042
% is http://identifiers.org/biomodels.db/BIOMD0000000373
% isDescribedBy http://identifiers.org/pubmed/15347584
% isDerivedFrom http://identifiers.org/pubmed/15294427
%


function Bertram2004()
	tic;
%Initial conditions vector
	x0=zeros(14,1);
	x0(8) = -60.0;
	x0(9) = 0.025;
	x0(10) = 0.25;
	x0(11) = 185.0;
	x0(12) = 200.0;
	x0(13) = 40.0;
	x0(14) = 780.0;
	x0(1) = -60.0;
	x0(2) = 0.025;
	x0(3) = 0.25;
	x0(4) = 185.0;
	x0(5) = 200.0;
	x0(6) = 40.0;
	x0(7) = 780.0;


% Depending on whether you are using Octave or Matlab,
% you should comment / uncomment one of the following blocks.
% This should also be done for the definition of the function f below.
% Start Matlab code
	tspan=[0:0.01:1000];
	opts = odeset('AbsTol',1e-6,'RelTol',1e-3);
	[t,x]=ode23(@f,tspan,x0,opts);
% End Matlab code

% Start Octave code
%	t=linspace(0,100,100);
%	x=lsode('f',x0,t);
% End Octave code

	disp(toc);
	%plot(t,x);
	%csvwrite('results.csv', [t, x]);
end



% Depending on whether you are using Octave or Matlab,
% you should comment / uncomment one of the following blocks.
% This should also be done for the definition of the function f below.
% Start Matlab code
function xdot=f(t,x)
% End Matlab code

% Start Octave code
%function xdot=f(x,t)
% End Octave code

% Compartment: id = COMpartment, name = COMpartment, constant
	compartment_COMpartment=1.0;
% Parameter:   id =  IK, name = IK
% Parameter:   id =  ICa, name = ICa
% Parameter:   id =  IKCa, name = IKCa
% Parameter:   id =  Cm, name = Cm
	global_par_Cm=5300.0;
% Parameter:   id =  gK, name = gK
	global_par_gK=2700.0;
% Parameter:   id =  gKCa, name = gKCa
	global_par_gKCa=600.0;
% Parameter:   id =  kd, name = kd
	global_par_kd=0.5;
% Parameter:   id =  gCa, name = gCa
	global_par_gCa=1000.0;
% Parameter:   id =  minf, name = minf
% Parameter:   id =  VCa, name = VCa
	global_par_VCa=25.0;
% Parameter:   id =  taun, name = taun
	global_par_taun=20.0;
% Parameter:   id =  ninf, name = ninf
% Parameter:   id =  fcyt, name = fcyt
	global_par_fcyt=0.01;
% Parameter:   id =  Jmem, name = Jmem
% Parameter:   id =  Jer, name = Jer
% Parameter:   id =  fer, name = fer
	global_par_fer=0.01;
% Parameter:   id =  sigmaV, name = sigmaV
	global_par_sigmaV=31.0;
% Parameter:   id =  pleak, name = pleak
	global_par_pleak=2.0E-4;
% Parameter:   id =  Kserca, name = Kserca
	global_par_Kserca=0.4;
% Parameter:   id =  lambdaer, name = lambdaer
	global_par_lambdaer=1.0;
% Parameter:   id =  epser, name = epser
	global_par_epser=1.0;
% Parameter:   id =  alpha, name = alpha
	global_par_alpha=4.5E-6;
% Parameter:   id =  kpmca, name = kpmca
	global_par_kpmca=0.2;
% Parameter:   id =  Jserca, name = Jserca
% Parameter:   id =  Jleak, name = Jleak
% Parameter:   id =  rgpdh, name = rgpdh
% Parameter:   id =  Rgk, name = Rgk
	global_par_Rgk=0.2;
% Parameter:   id =  atot, name = atot
	global_par_atot=3000.0;
% Parameter:   id =  pfkbas, name = pfkbas
	global_par_pfkbas=0.06;
% Parameter:   id =  f6p, name = f6p
% Parameter:   id =  lambda, name = lambda
	global_par_lambda=0.005;
% Parameter:   id =  pfk, name = pfk
% Parameter:   id =  bottom1, name = bottom1
	global_par_bottom1=1.0;
% Parameter:   id =  topa1, name = topa1
	global_par_topa1=0.0;
% Parameter:   id =  k1, name = k1
	global_par_k1=30.0;
% Parameter:   id =  k2, name = k2
	global_par_k2=1.0;
% Parameter:   id =  k3, name = k3
	global_par_k3=50000.0;
% Parameter:   id =  k4, name = k4
	global_par_k4=1000.0;
% Parameter:   id =  cat, name = cat
	global_par_cat=2.0;
% Parameter:   id =  weight2, name = weight2
% Parameter:   id =  topa2, name = topa2
% Parameter:   id =  bottom2, name = bottom2
% Parameter:   id =  topa3, name = topa3
% Parameter:   id =  weight3, name = weight3
% Parameter:   id =  bottom3, name = bottom3
% Parameter:   id =  famp, name = famp
	global_par_famp=0.02;
% Parameter:   id =  fatp, name = fatp
	global_par_fatp=20.0;
% Parameter:   id =  ffbp, name = ffbp
	global_par_ffbp=0.2;
% Parameter:   id =  fbt, name = fbt
	global_par_fbt=20.0;
% Parameter:   id =  fmt, name = fmt
	global_par_fmt=20.0;
% Parameter:   id =  weight4, name = weight4
% Parameter:   id =  topa4, name = topa4
% Parameter:   id =  bottom4, name = bottom4
% Parameter:   id =  weight5, name = weight5
% Parameter:   id =  topa5, name = topa5
% Parameter:   id =  bottom5, name = bottom5
% Parameter:   id =  weight6, name = weight6
% Parameter:   id =  topa6, name = topa6
% Parameter:   id =  bottom6, name = bottom6
% Parameter:   id =  weight7, name = weight7
% Parameter:   id =  topa7, name = topa7
% Parameter:   id =  bottom7, name = bottom7
% Parameter:   id =  weight8, name = weight8
% Parameter:   id =  topa8, name = topa8
% Parameter:   id =  bottom8, name = bottom8
% Parameter:   id =  weight9, name = weight9
% Parameter:   id =  topa9, name = topa9
% Parameter:   id =  bottom9, name = bottom9
% Parameter:   id =  weight10, name = weight10
% Parameter:   id =  topa10, name = topa10
% Parameter:   id =  bottom10, name = bottom10
% Parameter:   id =  weight11, name = weight11
% Parameter:   id =  topa11, name = topa11
% Parameter:   id =  bottom11, name = bottom11
% Parameter:   id =  weight12, name = weight12
% Parameter:   id =  topa12, name = topa12
% Parameter:   id =  bottom12, name = bottom12
% Parameter:   id =  weight13, name = weight13
% Parameter:   id =  topa13, name = topa13
% Parameter:   id =  bottom13, name = bottom13
% Parameter:   id =  weight14, name = weight14
% Parameter:   id =  topa14, name = topa14
% Parameter:   id =  bottom14, name = bottom14
% Parameter:   id =  weight15, name = weight15
% Parameter:   id =  topa15, name = topa15
% Parameter:   id =  bottom15, name = bottom15
% Parameter:   id =  weight16, name = weight16
% Parameter:   id =  topa16, name = topa16
% Parameter:   id =  bottom16, name = bottom16
% Parameter:   id =  topb, name = topb
% Parameter:   id =  mgadp, name = mgadp
% Parameter:   id =  adp3m, name = adp3m
% Parameter:   id =  atp4m, name = atp4m
% Parameter:   id =  topo, name = topo
% Parameter:   id =  bottomo, name = bottomo
% Parameter:   id =  katpo, name = katpo
% Parameter:   id =  IKATP, name = IKATP
% Parameter:   id =  VK, name = VK
	global_par_VK=-75.0;
% Parameter:   id =  gkatpbar, name = gkatpbar
	global_par_gkatpbar=25000.0;
% Parameter:   id =  kdd, name = kdd
	global_par_kdd=17.0;
% Parameter:   id =  ktd, name = ktd
	global_par_ktd=26.0;
% Parameter:   id =  ktt, name = ktt
	global_par_ktt=1.0;
% Parameter:   id =  atp, name = atp
% Parameter:   id =  fback, name = fback
% Parameter:   id =  taua, name = taua
	global_par_taua=300000.0;
% Parameter:   id =  r1, name = r1
	global_par_r1=0.35;
% Parameter:   id =  r, name = r
	global_par_r=1.0;
% Parameter:   id =  y, name = y
% Parameter:   id =  vg, name = vg
	global_par_vg=2.2;
% Parameter:   id =  kg, name = kg
	global_par_kg=10.0;
% Parameter:   id =  amp, name = amp
% Parameter:   id =  rad, name = rad
% Parameter:   id =  ratio, name = ratio
% assignmentRule: variable = IK
	global_par_IK=global_par_gK*x(2)*(x(1)-global_par_VK);
% assignmentRule: variable = IKCa
	global_par_IKCa=global_par_gKCa/(1+(global_par_kd/x(3))^2)*(x(1)-global_par_VK);
% assignmentRule: variable = minf
	global_par_minf=1/(1+exp((-(20+x(1)/1))/12));
% assignmentRule: variable = ICa
	global_par_ICa=global_par_gCa*global_par_minf*(x(1)-global_par_VCa);
% assignmentRule: variable = ninf
	global_par_ninf=1/(1+exp((-(16+x(1)/1))/5));
% assignmentRule: variable = Jmem
	global_par_Jmem=-(global_par_alpha*global_par_ICa+global_par_kpmca*x(3));
% assignmentRule: variable = Jserca
	global_par_Jserca=global_par_Kserca*x(3);
% assignmentRule: variable = Jleak
	global_par_Jleak=global_par_pleak*(x(4)-x(3));
% assignmentRule: variable = Jer
	global_par_Jer=global_par_epser*(global_par_Jleak-global_par_Jserca)/global_par_lambdaer;
% assignmentRule: variable = rgpdh
	global_par_rgpdh=0.2*abs(x(6)*1/1^2)^(1/2);
% assignmentRule: variable = f6p
	global_par_f6p=0.3*x(5);
% assignmentRule: variable = topa2
	global_par_topa2=global_par_topa1;
% assignmentRule: variable = weight3
	global_par_weight3=global_par_f6p^2/(global_par_k3*1);
% assignmentRule: variable = topa3
	global_par_topa3=global_par_topa2+global_par_weight3;
% assignmentRule: variable = weight5
	global_par_weight5=x(6)/global_par_k2;
% assignmentRule: variable = weight7
	global_par_weight7=x(6)*global_par_f6p^2/(global_par_k2*global_par_k3*global_par_ffbp*1);
% assignmentRule: variable = mgadp
	global_par_mgadp=0.165*x(7);
% assignmentRule: variable = adp3m
	global_par_adp3m=0.135*x(7);
% assignmentRule: variable = topo
	global_par_topo=0.08*(1+2*global_par_mgadp/(global_par_kdd*1))+0.89*(global_par_mgadp/(global_par_kdd*1))^2;
% assignmentRule: variable = y
	global_par_y=global_par_vg*global_par_rgpdh/(global_par_kg+global_par_rgpdh);
% assignmentRule: variable = fback
	global_par_fback=global_par_r+global_par_y;
% assignmentRule: variable = rad
	global_par_rad=abs((x(7)-global_par_atot)^2-4*x(7)^2)^(1/2)/1;
% assignmentRule: variable = atp
	global_par_atp=0.5*((global_par_atot-x(7))+global_par_rad*1);
% assignmentRule: variable = weight2
	global_par_weight2=global_par_atp^2/(global_par_k4*1);
% assignmentRule: variable = bottom2
	global_par_bottom2=global_par_bottom1+global_par_weight2;
% assignmentRule: variable = bottom3
	global_par_bottom3=global_par_bottom2+global_par_weight3;
% assignmentRule: variable = weight4
	global_par_weight4=(global_par_f6p*global_par_atp)^2/(global_par_fatp*global_par_k3*global_par_k4*1^2);
% assignmentRule: variable = topa4
	global_par_topa4=global_par_topa3+global_par_weight4;
% assignmentRule: variable = bottom4
	global_par_bottom4=global_par_bottom3+global_par_weight4;
% assignmentRule: variable = topa5
	global_par_topa5=global_par_topa4;
% assignmentRule: variable = bottom5
	global_par_bottom5=global_par_bottom4+global_par_weight5;
% assignmentRule: variable = weight6
	global_par_weight6=x(6)*global_par_atp^2/(global_par_k2*global_par_k4*global_par_fbt*1);
% assignmentRule: variable = topa6
	global_par_topa6=global_par_topa5;
% assignmentRule: variable = bottom6
	global_par_bottom6=global_par_bottom5+global_par_weight6;
% assignmentRule: variable = topa7
	global_par_topa7=global_par_topa6+global_par_weight7;
% assignmentRule: variable = bottom7
	global_par_bottom7=global_par_bottom6+global_par_weight7;
% assignmentRule: variable = weight8
	global_par_weight8=x(6)*global_par_f6p^2*global_par_atp^2/(global_par_k2*global_par_k3*global_par_k4*global_par_ffbp*global_par_fbt*global_par_fatp*1^2);
% assignmentRule: variable = topa8
	global_par_topa8=global_par_topa7+global_par_weight8;
% assignmentRule: variable = topa9
	global_par_topa9=global_par_topa8;
% assignmentRule: variable = bottom8
	global_par_bottom8=global_par_bottom7+global_par_weight8;
% assignmentRule: variable = topa10
	global_par_topa10=global_par_topa9;
% assignmentRule: variable = atp4m
	global_par_atp4m=0.05*global_par_atp;
% assignmentRule: variable = bottomo
	global_par_bottomo=(1+global_par_mgadp/(global_par_kdd*1))^2*(1+global_par_adp3m/(global_par_ktd*1)+global_par_atp4m/(global_par_ktt*1));
% assignmentRule: variable = katpo
	global_par_katpo=global_par_topo/global_par_bottomo;
% assignmentRule: variable = IKATP
	global_par_IKATP=global_par_gkatpbar*global_par_katpo*(x(1)-global_par_VK);
% assignmentRule: variable = amp
	global_par_amp=x(7)*x(7)/global_par_atp;
% assignmentRule: variable = weight9
	global_par_weight9=global_par_amp/global_par_k1;
% assignmentRule: variable = bottom9
	global_par_bottom9=global_par_bottom8+global_par_weight9;
% assignmentRule: variable = weight10
	global_par_weight10=global_par_amp*global_par_atp^2/(global_par_k1*global_par_k4*global_par_fmt*1);
% assignmentRule: variable = bottom10
	global_par_bottom10=global_par_bottom9+global_par_weight10;
% assignmentRule: variable = weight11
	global_par_weight11=global_par_amp*global_par_f6p^2/(global_par_k1*global_par_k3*global_par_famp*1);
% assignmentRule: variable = topa11
	global_par_topa11=global_par_topa10+global_par_weight11;
% assignmentRule: variable = bottom11
	global_par_bottom11=global_par_bottom10+global_par_weight11;
% assignmentRule: variable = weight12
	global_par_weight12=global_par_amp*global_par_f6p^2*global_par_atp^2/(global_par_k1*global_par_k3*global_par_k4*global_par_famp*global_par_fmt*global_par_fatp*1^2);
% assignmentRule: variable = topa12
	global_par_topa12=global_par_topa11+global_par_weight12;
% assignmentRule: variable = bottom12
	global_par_bottom12=global_par_bottom11+global_par_weight12;
% assignmentRule: variable = weight13
	global_par_weight13=global_par_amp*x(6)/(global_par_k1*global_par_k2);
% assignmentRule: variable = topa13
	global_par_topa13=global_par_topa12;
% assignmentRule: variable = bottom13
	global_par_bottom13=global_par_bottom12+global_par_weight13;
% assignmentRule: variable = weight14
	global_par_weight14=global_par_amp*x(6)*global_par_atp^2/(global_par_k1*global_par_k2*global_par_k4*global_par_fbt*global_par_fmt*1);
% assignmentRule: variable = topa14
	global_par_topa14=global_par_topa13;
% assignmentRule: variable = bottom14
	global_par_bottom14=global_par_bottom13+global_par_weight14;
% assignmentRule: variable = weight15
	global_par_weight15=global_par_amp*x(6)*global_par_f6p^2/(global_par_k1*global_par_k2*global_par_k3*global_par_ffbp*global_par_famp*1);
% assignmentRule: variable = topa15
	global_par_topa15=global_par_topa14;
% assignmentRule: variable = bottom15
	global_par_bottom15=global_par_bottom14+global_par_weight15;
% assignmentRule: variable = weight16
	global_par_weight16=global_par_amp*x(6)*global_par_f6p^2*global_par_atp^2/(global_par_k1*global_par_k2*global_par_k3*global_par_k4*global_par_ffbp*global_par_famp*global_par_fbt*global_par_fmt*global_par_fatp*1^2);
% assignmentRule: variable = topa16
	global_par_topa16=global_par_topa15+global_par_weight16;
% assignmentRule: variable = bottom16
	global_par_bottom16=global_par_bottom15+global_par_weight16;
% assignmentRule: variable = topb
	global_par_topb=global_par_weight15;
% assignmentRule: variable = pfk
	global_par_pfk=1*(global_par_pfkbas*global_par_cat*global_par_topa16+global_par_cat*global_par_topb)/global_par_bottom16;
% assignmentRule: variable = ratio
	global_par_ratio=global_par_atp/x(7);
% rateRule: variable = V
x(1) = x(8);
% rateRule: variable = n
x(2) = x(9);
% rateRule: variable = c
x(3) = x(10);
% rateRule: variable = cer
x(4) = x(11);
% rateRule: variable = g6p
x(5) = x(12);
% rateRule: variable = fbp
x(6) = x(13);
% rateRule: variable = adp
x(7) = x(14);



	xdot=zeros(14,1);
	% rateRule: variable = V
	xdot(8) = (-(global_par_IK+global_par_ICa+global_par_IKCa+global_par_IKATP))/global_par_Cm;
	% rateRule: variable = n
	xdot(9) = (global_par_ninf-x(2))/global_par_taun;
	% rateRule: variable = c
	xdot(10) = global_par_fcyt*(global_par_Jmem+global_par_Jer);
	% rateRule: variable = cer
	xdot(11) = (-global_par_fer)*global_par_sigmaV*global_par_Jer;
	% rateRule: variable = g6p
	xdot(12) = global_par_lambda*(global_par_Rgk-global_par_pfk);
	% rateRule: variable = fbp
	xdot(13) = global_par_lambda*(global_par_pfk/1-0.5*global_par_rgpdh);
	% rateRule: variable = adp
	xdot(14) = (global_par_atp-x(7)*exp(global_par_fback*(1-x(3)/global_par_r1)))/(global_par_taua*1);
	
% Species:   id = V, name = V, defined in a rule 	xdot(1) = x(1);
	
% Species:   id = n, name = n, defined in a rule 	xdot(2) = x(2);
	
% Species:   id = c, name = c, defined in a rule 	xdot(3) = x(3);
	
% Species:   id = cer, name = cer, defined in a rule 	xdot(4) = x(4);
	
% Species:   id = g6p, name = g6p, defined in a rule 	xdot(5) = x(5);
	
% Species:   id = fbp, name = fbp, defined in a rule 	xdot(6) = x(6);
	
% Species:   id = adp, name = adp, defined in a rule 	xdot(7) = x(7);
end

% adding few functions representing operators used in SBML but not present directly 
% in either matlab or octave. 
function z=pow(x,y),z=x^y;end
function z=root(x,y),z=y^(1/x);end


