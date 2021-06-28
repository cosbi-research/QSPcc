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
% Model name = Pathak2013 - MAPK activation in response to various abiotic stresses
%
% is http://identifiers.org/biomodels.db/MODEL1308220001
% is http://identifiers.org/biomodels.db/BIOMD0000000491
% isDescribedBy http://identifiers.org/pubmed/23847397
%


function Pathak2013()
%Initial conditions vector
	tic;
	x0=zeros(57,1);
	x0(1) = 0.5;
	x0(2) = 0.5;
	x0(3) = 0.5;
	x0(4) = 0.5;
	x0(5) = 0.5;
	x0(6) = 0.5;
	x0(7) = 0.8;
	x0(8) = 0.8;
	x0(9) = 0.8;
	x0(10) = 0.8;
	x0(11) = 0.8;
	x0(12) = 0.8;
	x0(13) = 1.0;
	x0(14) = 1.0;
	x0(15) = 1.0;
	x0(16) = 1.0;
	x0(17) = 1.0;
	x0(18) = 1.0;
	x0(19) = 1.0;
	x0(20) = 1.0;
	x0(21) = 1.0;
	x0(22) = 1.0;
	x0(23) = 1.0;
	x0(24) = 1.0;
	x0(25) = 1.0;
	x0(26) = 1.0;
	x0(27) = 1.0;
	x0(28) = 1.0;
	x0(29) = 1.0;
	x0(30) = 1.0;
	x0(31) = 1.0;
	x0(32) = 1.0;
	x0(33) = 1.2;
	x0(34) = 1.2;
	x0(35) = 1.2;
	x0(36) = 1.2;
	x0(37) = 1.2;
	x0(38) = 1.2;
	x0(39) = 1.2;
	x0(40) = 1.2;
	x0(41) = 1.2;
	x0(42) = 1.2;
	x0(43) = 1.2;
	x0(44) = 1.2;
	x0(45) = 1.2;
	x0(46) = 1.2;
	x0(47) = 1.2;
	x0(48) = 1.2;
	x0(49) = 1.5;
	x0(50) = 1.5;
	x0(51) = 1.5;
	x0(52) = 1.5;
	x0(53) = 1.5;
	x0(54) = 1.8;
	x0(55) = 2.0;
	x0(56) = 2.2;
	x0(57) = 2.5;


% Depending on whether you are using Octave or Matlab,
% you should comment / uncomment one of the following blocks.
% This should also be done for the definition of the function f below.
% Start Matlab code
	tspan=[0:0.01:1000];
	opts = odeset('AbsTol',1e-6, 'RelTol', 1e-3);
	[t,x]=ode23(@f,tspan,x0,opts);
% End Matlab code

% Start Octave code
%	t=linspace(0,100,100);
%	x=lsode('f',x0,t);
% End Octave code


	%plot(t,x);
	disp(toc);
	%csvwrite('results.csv',[t,x]);
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

% Compartment: id = default, name = default, constant
	compartment_default=1.0;
% Compartment: id = c1, name = Cytosol, constant
	compartment_c1=1.0;
% Compartment: id = c2, name = Nucleus, constant
	compartment_c2=1.0;
% Parameter:   id =  kdiss_re1, name = Dissociation constant of reaction re1
	global_par_kdiss_re1=1.0;
% Parameter:   id =  kass_re1, name = Association constant of reaction re1
	global_par_kass_re1=1.0;
% Parameter:   id =  kdiss_re2, name = Dissociation constant of reaction re2
	global_par_kdiss_re2=1.0;
% Parameter:   id =  kass_re2, name = Association constant of reaction re2
	global_par_kass_re2=1.0;
% Parameter:   id =  kdiss_re3, name = Dissociation constant of reaction re3
	global_par_kdiss_re3=1.0;
% Parameter:   id =  kass_re3, name = Association constant of reaction re3
	global_par_kass_re3=1.0;
% Parameter:   id =  kdiss_re4, name = Dissociation constant of reaction re4
	global_par_kdiss_re4=1.0;
% Parameter:   id =  kass_re4, name = Association constant of reaction re4
	global_par_kass_re4=1.0;
% Parameter:   id =  kdiss_re5, name = Dissociation constant of reaction re5
	global_par_kdiss_re5=1.0;
% Parameter:   id =  kass_re5, name = Association constant of reaction re5
	global_par_kass_re5=1.0;
% Parameter:   id =  kdiss_re6, name = Dissociation constant of reaction re6
	global_par_kdiss_re6=1.0;
% Parameter:   id =  kass_re6, name = Association constant of reaction re6
	global_par_kass_re6=1.0;
% Parameter:   id =  kdiss_re7, name = Dissociation constant of reaction re7
	global_par_kdiss_re7=1.0;
% Parameter:   id =  kass_re7, name = Association constant of reaction re7
	global_par_kass_re7=1.0;
% Parameter:   id =  kdiss_re8, name = Dissociation constant of reaction re8
	global_par_kdiss_re8=1.0;
% Parameter:   id =  kass_re8, name = Association constant of reaction re8
	global_par_kass_re8=1.0;
% Parameter:   id =  kdiss_re9, name = Dissociation constant of reaction re9
	global_par_kdiss_re9=1.0;
% Parameter:   id =  kass_re9, name = Association constant of reaction re9
	global_par_kass_re9=1.0;
% Parameter:   id =  kdiss_re10, name = Dissociation constant of reaction re10
	global_par_kdiss_re10=1.0;
% Parameter:   id =  kass_re10, name = Association constant of reaction re10
	global_par_kass_re10=1.0;
% Parameter:   id =  kdiss_re11, name = Dissociation constant of reaction re11
	global_par_kdiss_re11=1.0;
% Parameter:   id =  kass_re11, name = Association constant of reaction re11
	global_par_kass_re11=1.0;
% Parameter:   id =  kdiss_re12, name = Dissociation constant of reaction re12
	global_par_kdiss_re12=1.0;
% Parameter:   id =  kass_re12, name = Association constant of reaction re12
	global_par_kass_re12=1.0;
% Parameter:   id =  kdiss_re13, name = Dissociation constant of reaction re13
	global_par_kdiss_re13=1.0;
% Parameter:   id =  kass_re13, name = Association constant of reaction re13
	global_par_kass_re13=1.0;
% Parameter:   id =  kdiss_re14, name = Dissociation constant of reaction re14
	global_par_kdiss_re14=1.0;
% Parameter:   id =  kass_re14, name = Association constant of reaction re14
	global_par_kass_re14=1.0;
% Parameter:   id =  kdiss_re15, name = Dissociation constant of reaction re15
	global_par_kdiss_re15=1.0;
% Parameter:   id =  kass_re15, name = Association constant of reaction re15
	global_par_kass_re15=1.0;
% Parameter:   id =  kdiss_re16, name = Dissociation constant of reaction re16
	global_par_kdiss_re16=1.0;
% Parameter:   id =  kass_re16, name = Association constant of reaction re16
	global_par_kass_re16=1.0;
% Parameter:   id =  kdiss_re17, name = Dissociation constant of reaction re17
	global_par_kdiss_re17=1.0;
% Parameter:   id =  kass_re17, name = Association constant of reaction re17
	global_par_kass_re17=1.0;
% Parameter:   id =  kdiss_re18, name = Dissociation constant of reaction re18
	global_par_kdiss_re18=1.0;
% Parameter:   id =  kass_re18, name = Association constant of reaction re18
	global_par_kass_re18=1.0;
% Parameter:   id =  kdiss_re19, name = Dissociation constant of reaction re19
	global_par_kdiss_re19=1.0;
% Parameter:   id =  kass_re19, name = Association constant of reaction re19
	global_par_kass_re19=1.0;
% Parameter:   id =  kdiss_re20, name = Dissociation constant of reaction re20
	global_par_kdiss_re20=1.0;
% Parameter:   id =  kass_re20, name = Association constant of reaction re20
	global_par_kass_re20=1.0;
% Parameter:   id =  kdiss_re21, name = Dissociation constant of reaction re21
	global_par_kdiss_re21=1.0;
% Parameter:   id =  kass_re21, name = Association constant of reaction re21
	global_par_kass_re21=1.0;
% Parameter:   id =  kdiss_re22, name = Dissociation constant of reaction re22
	global_par_kdiss_re22=1.0;
% Parameter:   id =  kass_re22, name = Association constant of reaction re22
	global_par_kass_re22=1.0;
% Parameter:   id =  kdiss_re23, name = Dissociation constant of reaction re23
	global_par_kdiss_re23=1.0;
% Parameter:   id =  kass_re23, name = Association constant of reaction re23
	global_par_kass_re23=1.0;
% Parameter:   id =  kdiss_re24, name = Dissociation constant of reaction re24
	global_par_kdiss_re24=1.0;
% Parameter:   id =  kass_re24, name = Association constant of reaction re24
	global_par_kass_re24=1.0;
% Parameter:   id =  kdiss_re25, name = Dissociation constant of reaction re25
	global_par_kdiss_re25=1.0;
% Parameter:   id =  kass_re25, name = Association constant of reaction re25
	global_par_kass_re25=1.0;
% Parameter:   id =  kdiss_re26, name = Dissociation constant of reaction re26
	global_par_kdiss_re26=1.0;
% Parameter:   id =  kass_re26, name = Association constant of reaction re26
	global_par_kass_re26=1.0;
% Parameter:   id =  kdiss_re27, name = Dissociation constant of reaction re27
	global_par_kdiss_re27=1.0;
% Parameter:   id =  kass_re27, name = Association constant of reaction re27
	global_par_kass_re27=1.0;
% Parameter:   id =  kdiss_re28, name = Dissociation constant of reaction re28
	global_par_kdiss_re28=1.0;
% Parameter:   id =  kass_re28, name = Association constant of reaction re28
	global_par_kass_re28=1.0;
% Parameter:   id =  kdiss_re29, name = Dissociation constant of reaction re29
	global_par_kdiss_re29=1.0;
% Parameter:   id =  kass_re29, name = Association constant of reaction re29
	global_par_kass_re29=1.0;
% Parameter:   id =  kdiss_re30, name = Dissociation constant of reaction re30
	global_par_kdiss_re30=1.0;
% Parameter:   id =  kass_re30, name = Association constant of reaction re30
	global_par_kass_re30=1.0;
% Parameter:   id =  kdiss_re31, name = Dissociation constant of reaction re31
	global_par_kdiss_re31=1.0;
% Parameter:   id =  kass_re31, name = Association constant of reaction re31
	global_par_kass_re31=1.0;
% Parameter:   id =  kdiss_re32, name = Dissociation constant of reaction re32
	global_par_kdiss_re32=1.0;
% Parameter:   id =  kass_re32, name = Association constant of reaction re32
	global_par_kass_re32=1.0;
% Parameter:   id =  kdiss_re33, name = Dissociation constant of reaction re33
	global_par_kdiss_re33=1.0;
% Parameter:   id =  kass_re33, name = Association constant of reaction re33
	global_par_kass_re33=1.0;
% Parameter:   id =  kdiss_re34, name = Dissociation constant of reaction re34
	global_par_kdiss_re34=1.0;
% Parameter:   id =  kass_re34, name = Association constant of reaction re34
	global_par_kass_re34=1.0;
% Parameter:   id =  kdiss_re35, name = Dissociation constant of reaction re35
	global_par_kdiss_re35=1.0;
% Parameter:   id =  kass_re35, name = Association constant of reaction re35
	global_par_kass_re35=1.0;
% Parameter:   id =  kdiss_re36, name = Dissociation constant of reaction re36
	global_par_kdiss_re36=1.0;
% Parameter:   id =  kass_re36, name = Association constant of reaction re36
	global_par_kass_re36=1.0;
% Parameter:   id =  kdiss_re37, name = Dissociation constant of reaction re37
	global_par_kdiss_re37=1.0;
% Parameter:   id =  kass_re37, name = Association constant of reaction re37
	global_par_kass_re37=1.0;
% Parameter:   id =  kdiss_re38, name = Dissociation constant of reaction re38
	global_par_kdiss_re38=1.0;
% Parameter:   id =  kass_re38, name = Association constant of reaction re38
	global_par_kass_re38=1.0;
% Parameter:   id =  kdiss_re39, name = Dissociation constant of reaction re39
	global_par_kdiss_re39=1.0;
% Parameter:   id =  kass_re39, name = Association constant of reaction re39
	global_par_kass_re39=1.0;
% Parameter:   id =  kdiss_re40, name = Dissociation constant of reaction re40
	global_par_kdiss_re40=1.0;
% Parameter:   id =  kass_re40, name = Association constant of reaction re40
	global_par_kass_re40=1.0;
% Parameter:   id =  kdiss_re41, name = Dissociation constant of reaction re41
	global_par_kdiss_re41=1.0;
% Parameter:   id =  kass_re41, name = Association constant of reaction re41
	global_par_kass_re41=1.0;
% Parameter:   id =  kdiss_re42, name = Dissociation constant of reaction re42
	global_par_kdiss_re42=1.0;
% Parameter:   id =  kass_re42, name = Association constant of reaction re42
	global_par_kass_re42=1.0;
% Parameter:   id =  kdiss_re43, name = Dissociation constant of reaction re43
	global_par_kdiss_re43=1.0;
% Parameter:   id =  kass_re43, name = Association constant of reaction re43
	global_par_kass_re43=1.0;
% Parameter:   id =  kdiss_re44, name = Dissociation constant of reaction re44
	global_par_kdiss_re44=1.0;
% Parameter:   id =  kass_re44, name = Association constant of reaction re44
	global_par_kass_re44=1.0;
% Parameter:   id =  kdiss_re45, name = Dissociation constant of reaction re45
	global_par_kdiss_re45=1.0;
% Parameter:   id =  kass_re45, name = Association constant of reaction re45
	global_par_kass_re45=1.0;
% Parameter:   id =  kdiss_re46, name = Dissociation constant of reaction re46
	global_par_kdiss_re46=1.0;
% Parameter:   id =  kass_re46, name = Association constant of reaction re46
	global_par_kass_re46=1.0;
% Parameter:   id =  kdiss_re47, name = Dissociation constant of reaction re47
	global_par_kdiss_re47=1.0;
% Parameter:   id =  kass_re47, name = Association constant of reaction re47
	global_par_kass_re47=1.0;
% Parameter:   id =  kdiss_re48, name = Dissociation constant of reaction re48
	global_par_kdiss_re48=1.0;
% Parameter:   id =  kass_re48, name = Association constant of reaction re48
	global_par_kass_re48=1.0;
% Parameter:   id =  kdiss_re49, name = Dissociation constant of reaction re49
	global_par_kdiss_re49=1.0;
% Parameter:   id =  kass_re49, name = Association constant of reaction re49
	global_par_kass_re49=1.0;
% Parameter:   id =  kdiss_re50, name = Dissociation constant of reaction re50
	global_par_kdiss_re50=1.0;
% Parameter:   id =  kass_re50, name = Association constant of reaction re50
	global_par_kass_re50=1.0;
% Parameter:   id =  kdiss_re51, name = Dissociation constant of reaction re51
	global_par_kdiss_re51=1.0;
% Parameter:   id =  kass_re51, name = Association constant of reaction re51
	global_par_kass_re51=1.0;
% Parameter:   id =  kdiss_re52, name = Dissociation constant of reaction re52
	global_par_kdiss_re52=1.0;
% Parameter:   id =  kass_re52, name = Association constant of reaction re52
	global_par_kass_re52=1.0;
% Parameter:   id =  kdiss_re53, name = Dissociation constant of reaction re53
	global_par_kdiss_re53=1.0;
% Parameter:   id =  kass_re53, name = Association constant of reaction re53
	global_par_kass_re53=1.0;
% Parameter:   id =  kdiss_re54, name = Dissociation constant of reaction re54
	global_par_kdiss_re54=1.0;
% Parameter:   id =  kass_re54, name = Association constant of reaction re54
	global_par_kass_re54=1.0;
% Parameter:   id =  kdiss_re55, name = Dissociation constant of reaction re55
	global_par_kdiss_re55=1.0;
% Parameter:   id =  kass_re55, name = Association constant of reaction re55
	global_par_kass_re55=1.0;
% Parameter:   id =  kdiss_re56, name = Dissociation constant of reaction re56
	global_par_kdiss_re56=1.0;
% Parameter:   id =  kass_re56, name = Association constant of reaction re56
	global_par_kass_re56=1.0;
% Parameter:   id =  kdiss_re57, name = Dissociation constant of reaction re57
	global_par_kdiss_re57=1.0;
% Parameter:   id =  kass_re57, name = Association constant of reaction re57
	global_par_kass_re57=1.0;
% Parameter:   id =  kdiss_re58, name = Dissociation constant of reaction re58
	global_par_kdiss_re58=1.0;
% Parameter:   id =  kass_re58, name = Association constant of reaction re58
	global_par_kass_re58=1.0;
% Parameter:   id =  kdiss_re59, name = Dissociation constant of reaction re59
	global_par_kdiss_re59=1.0;
% Parameter:   id =  kass_re59, name = Association constant of reaction re59
	global_par_kass_re59=1.0;
% Parameter:   id =  kdiss_re60, name = Dissociation constant of reaction re60
	global_par_kdiss_re60=1.0;
% Parameter:   id =  kass_re60, name = Association constant of reaction re60
	global_par_kass_re60=1.0;
% Parameter:   id =  kdiss_re61, name = Dissociation constant of reaction re61
	global_par_kdiss_re61=1.0;
% Parameter:   id =  kass_re61, name = Association constant of reaction re61
	global_par_kass_re61=1.0;
% Parameter:   id =  kdiss_re62, name = Dissociation constant of reaction re62
	global_par_kdiss_re62=1.0;
% Parameter:   id =  kass_re62, name = Association constant of reaction re62
	global_par_kass_re62=1.0;
% Parameter:   id =  kdiss_re63, name = Dissociation constant of reaction re63
	global_par_kdiss_re63=1.0;
% Parameter:   id =  kass_re63, name = Association constant of reaction re63
	global_par_kass_re63=1.0;
% Parameter:   id =  kdiss_re64, name = Dissociation constant of reaction re64
	global_par_kdiss_re64=1.0;
% Parameter:   id =  kass_re64, name = Association constant of reaction re64
	global_par_kass_re64=1.0;
% Parameter:   id =  kdiss_re65, name = Dissociation constant of reaction re65
	global_par_kdiss_re65=1.0;
% Parameter:   id =  kass_re65, name = Association constant of reaction re65
	global_par_kass_re65=1.0;
% Parameter:   id =  kdiss_re66, name = Dissociation constant of reaction re66
	global_par_kdiss_re66=1.0;
% Parameter:   id =  kass_re66, name = Association constant of reaction re66
	global_par_kass_re66=1.0;
% Parameter:   id =  kdiss_re67, name = Dissociation constant of reaction re67
	global_par_kdiss_re67=1.0;
% Parameter:   id =  kass_re67, name = Association constant of reaction re67
	global_par_kass_re67=1.0;
% Parameter:   id =  kdiss_re68, name = Dissociation constant of reaction re68
	global_par_kdiss_re68=1.0;
% Parameter:   id =  kass_re68, name = Association constant of reaction re68
	global_par_kass_re68=1.0;
% Parameter:   id =  kdiss_re69, name = Dissociation constant of reaction re69
	global_par_kdiss_re69=1.0;
% Parameter:   id =  kass_re69, name = Association constant of reaction re69
	global_par_kass_re69=1.0;
% Parameter:   id =  kdiss_re70, name = Dissociation constant of reaction re70
	global_par_kdiss_re70=1.0;
% Parameter:   id =  kass_re70, name = Association constant of reaction re70
	global_par_kass_re70=1.0;
% Parameter:   id =  kdiss_re71, name = Dissociation constant of reaction re71
	global_par_kdiss_re71=1.0;
% Parameter:   id =  kass_re71, name = Association constant of reaction re71
	global_par_kass_re71=1.0;
% Parameter:   id =  kdiss_re72, name = Dissociation constant of reaction re72
	global_par_kdiss_re72=1.0;
% Parameter:   id =  kass_re72, name = Association constant of reaction re72
	global_par_kass_re72=1.0;
% Parameter:   id =  kdiss_re73, name = Dissociation constant of reaction re73
	global_par_kdiss_re73=1.0;
% Parameter:   id =  kass_re73, name = Association constant of reaction re73
	global_par_kass_re73=1.0;
% Parameter:   id =  kdiss_re74, name = Dissociation constant of reaction re74
	global_par_kdiss_re74=1.0;
% Parameter:   id =  kass_re74, name = Association constant of reaction re74
	global_par_kass_re74=1.0;
% Parameter:   id =  kdiss_re75, name = Dissociation constant of reaction re75
	global_par_kdiss_re75=1.0;
% Parameter:   id =  kass_re75, name = Association constant of reaction re75
	global_par_kass_re75=1.0;
% Parameter:   id =  kdiss_re76, name = Dissociation constant of reaction re76
	global_par_kdiss_re76=1.0;
% Parameter:   id =  kass_re76, name = Association constant of reaction re76
	global_par_kass_re76=1.0;
% Parameter:   id =  kdiss_re77, name = Dissociation constant of reaction re77
	global_par_kdiss_re77=1.0;
% Parameter:   id =  kass_re77, name = Association constant of reaction re77
	global_par_kass_re77=1.0;
% Parameter:   id =  kdiss_re78, name = Dissociation constant of reaction re78
	global_par_kdiss_re78=1.0;
% Parameter:   id =  kass_re78, name = Association constant of reaction re78
	global_par_kass_re78=1.0;
% Parameter:   id =  kdiss_re79, name = Dissociation constant of reaction re79
	global_par_kdiss_re79=1.0;
% Parameter:   id =  kass_re79, name = Association constant of reaction re79
	global_par_kass_re79=1.0;
% Parameter:   id =  kdiss_re80, name = Dissociation constant of reaction re80
	global_par_kdiss_re80=1.0;
% Parameter:   id =  kass_re80, name = Association constant of reaction re80
	global_par_kass_re80=1.0;
% Parameter:   id =  kdiss_re81, name = Dissociation constant of reaction re81
	global_par_kdiss_re81=1.0;
% Parameter:   id =  kass_re81, name = Association constant of reaction re81
	global_par_kass_re81=1.0;
% Parameter:   id =  kdiss_re82, name = Dissociation constant of reaction re82
	global_par_kdiss_re82=1.0;
% Parameter:   id =  kass_re82, name = Association constant of reaction re82
	global_par_kass_re82=1.0;
% Parameter:   id =  kdiss_re83, name = Dissociation constant of reaction re83
	global_par_kdiss_re83=1.0;
% Parameter:   id =  kass_re83, name = Association constant of reaction re83
	global_par_kass_re83=1.0;
% Parameter:   id =  kdiss_re84, name = Dissociation constant of reaction re84
	global_par_kdiss_re84=1.0;
% Parameter:   id =  kass_re84, name = Association constant of reaction re84
	global_par_kass_re84=1.0;
% Parameter:   id =  kdiss_re85, name = Dissociation constant of reaction re85
	global_par_kdiss_re85=1.0;
% Parameter:   id =  kass_re85, name = Association constant of reaction re85
	global_par_kass_re85=1.0;
% Parameter:   id =  kdiss_re86, name = Dissociation constant of reaction re86
	global_par_kdiss_re86=1.0;
% Parameter:   id =  kass_re86, name = Association constant of reaction re86
	global_par_kass_re86=1.0;

% Reaction: id = re1
	reaction_re1=global_par_kass_re1*x(1)-global_par_kdiss_re1*x(7);

% Reaction: id = re2
	reaction_re2=global_par_kass_re2*x(2)-global_par_kdiss_re2*x(7);

% Reaction: id = re3
	reaction_re3=global_par_kass_re3*x(2)-global_par_kdiss_re3*x(8);

% Reaction: id = re4
	reaction_re4=global_par_kass_re4*x(2)-global_par_kdiss_re4*x(9);

% Reaction: id = re5
	reaction_re5=global_par_kass_re5*x(6)-global_par_kdiss_re5*x(12);

% Reaction: id = re6
	reaction_re6=global_par_kass_re6*x(6)-global_par_kdiss_re6*x(11);

% Reaction: id = re7
	reaction_re7=global_par_kass_re7*x(6)-global_par_kdiss_re7*x(10);

% Reaction: id = re8
	reaction_re8=global_par_kass_re8*x(3)-global_par_kdiss_re8*x(9);

% Reaction: id = re9
	reaction_re9=global_par_kass_re9*x(3)-global_par_kdiss_re9*x(7);

% Reaction: id = re10
	reaction_re10=global_par_kass_re10*x(4)-global_par_kdiss_re10*x(7);

% Reaction: id = re11
	reaction_re11=global_par_kass_re11*x(5)-global_par_kdiss_re11*x(7);

% Reaction: id = re12
	reaction_re12=global_par_kass_re12*x(13)-global_par_kdiss_re12*x(14);

% Reaction: id = re13
	reaction_re13=global_par_kass_re13*x(7)-global_par_kdiss_re13*x(13);

% Reaction: id = re14
	reaction_re14=global_par_kass_re14*x(8)-global_par_kdiss_re14*x(13);

% Reaction: id = re15
	reaction_re15=global_par_kass_re15*x(9)-global_par_kdiss_re15*x(13);

% Reaction: id = re16
	reaction_re16=global_par_kass_re16*x(10)-global_par_kdiss_re16*x(13);

% Reaction: id = re17
	reaction_re17=global_par_kass_re17*x(14)-global_par_kdiss_re17*x(15);

% Reaction: id = re18
	reaction_re18=global_par_kass_re18*x(7)-global_par_kdiss_re18*x(15);

% Reaction: id = re19
	reaction_re19=global_par_kass_re19*x(14)-global_par_kdiss_re19*x(16);

% Reaction: id = re20
	reaction_re20=global_par_kass_re20*x(11)-global_par_kdiss_re20*x(16);

% Reaction: id = re21
	reaction_re21=global_par_kass_re21*x(12)-global_par_kdiss_re21*x(16);

% Reaction: id = re22
	reaction_re22=global_par_kass_re22*x(17)-global_par_kdiss_re22*x(18);

% Reaction: id = re23
	reaction_re23=global_par_kass_re23*x(14)-global_par_kdiss_re23*x(17);

% Reaction: id = re24
	reaction_re24=global_par_kass_re24*x(18)-global_par_kdiss_re24*x(21);

% Reaction: id = re25
	reaction_re25=global_par_kass_re25*x(18)-global_par_kdiss_re25*x(22);

% Reaction: id = re26
	reaction_re26=global_par_kass_re26*x(18)-global_par_kdiss_re26*x(23);

% Reaction: id = re27
	reaction_re27=global_par_kass_re27*x(18)-global_par_kdiss_re27*x(24);

% Reaction: id = re28
	reaction_re28=global_par_kass_re28*x(18)-global_par_kdiss_re28*x(25);

% Reaction: id = re29
	reaction_re29=global_par_kass_re29*x(18)-global_par_kdiss_re29*x(26);

% Reaction: id = re30
	reaction_re30=global_par_kass_re30*x(18)-global_par_kdiss_re30*x(27);

% Reaction: id = re31
	reaction_re31=global_par_kass_re31*x(18)-global_par_kdiss_re31*x(28);

% Reaction: id = re32
	reaction_re32=global_par_kass_re32*x(19)-global_par_kdiss_re32*x(20);

% Reaction: id = re33
	reaction_re33=global_par_kass_re33*x(18)-global_par_kdiss_re33*x(19);

% Reaction: id = re34
	reaction_re34=global_par_kass_re34*x(15)-global_par_kdiss_re34*x(21);

% Reaction: id = re35
	reaction_re35=global_par_kass_re35*x(15)-global_par_kdiss_re35*x(22);

% Reaction: id = re36
	reaction_re36=global_par_kass_re36*x(16)-global_par_kdiss_re36*x(28);

% Reaction: id = re37
	reaction_re37=global_par_kass_re37*x(20)-global_par_kdiss_re37*x(29);

% Reaction: id = re38
	reaction_re38=global_par_kass_re38*x(20)-global_par_kdiss_re38*x(30);

% Reaction: id = re39
	reaction_re39=global_par_kass_re39*x(20)-global_par_kdiss_re39*x(31);

% Reaction: id = re40
	reaction_re40=global_par_kass_re40*x(20)-global_par_kdiss_re40*x(32);

% Reaction: id = re41
	reaction_re41=global_par_kass_re41*x(22)-global_par_kdiss_re41*x(30);

% Reaction: id = re42
	reaction_re42=global_par_kass_re42*x(22)-global_par_kdiss_re42*x(31);

% Reaction: id = re43
	reaction_re43=global_par_kass_re43*x(22)-global_par_kdiss_re43*x(32);

% Reaction: id = re44
	reaction_re44=global_par_kass_re44*x(28)-global_par_kdiss_re44*x(30);

% Reaction: id = re45
	reaction_re45=global_par_kass_re45*x(33)-global_par_kdiss_re45*x(34);

% Reaction: id = re46
	reaction_re46=global_par_kass_re46*x(35)-global_par_kdiss_re46*x(36);

% Reaction: id = re47
	reaction_re47=global_par_kass_re47*x(37)-global_par_kdiss_re47*x(38);

% Reaction: id = re48
	reaction_re48=global_par_kass_re48*x(39)-global_par_kdiss_re48*x(40);

% Reaction: id = re49
	reaction_re49=global_par_kass_re49*x(41)-global_par_kdiss_re49*x(42);

% Reaction: id = re50
	reaction_re50=global_par_kass_re50*x(43)-global_par_kdiss_re50*x(44);

% Reaction: id = re51
	reaction_re51=global_par_kass_re51*x(45)-global_par_kdiss_re51*x(46);

% Reaction: id = re52
	reaction_re52=global_par_kass_re52*x(47)-global_par_kdiss_re52*x(48);

% Reaction: id = re53
	reaction_re53=global_par_kass_re53*x(49)-global_par_kdiss_re53*x(50);

% Reaction: id = re54
	reaction_re54=global_par_kass_re54*x(51)-global_par_kdiss_re54*x(52);

% Reaction: id = re55
	reaction_re55=global_par_kass_re55*x(29)-global_par_kdiss_re55*x(37);

% Reaction: id = re56
	reaction_re56=global_par_kass_re56*x(29)-global_par_kdiss_re56*x(33);

% Reaction: id = re57
	reaction_re57=global_par_kass_re57*x(30)-global_par_kdiss_re57*x(35);

% Reaction: id = re58
	reaction_re58=global_par_kass_re58*x(30)-global_par_kdiss_re58*x(41);

% Reaction: id = re59
	reaction_re59=global_par_kass_re59*x(30)-global_par_kdiss_re59*x(47);

% Reaction: id = re60
	reaction_re60=global_par_kass_re60*x(31)-global_par_kdiss_re60*x(33);

% Reaction: id = re61
	reaction_re61=global_par_kass_re61*x(31)-global_par_kdiss_re61*x(45);

% Reaction: id = re62
	reaction_re62=global_par_kass_re62*x(31)-global_par_kdiss_re62*x(39);

% Reaction: id = re63
	reaction_re63=global_par_kass_re63*x(32)-global_par_kdiss_re63*x(47);

% Reaction: id = re64
	reaction_re64=global_par_kass_re64*x(32)-global_par_kdiss_re64*x(45);

% Reaction: id = re65
	reaction_re65=global_par_kass_re65*x(32)-global_par_kdiss_re65*x(35);

% Reaction: id = re66
	reaction_re66=global_par_kass_re66*x(20)-global_par_kdiss_re66*x(56);

% Reaction: id = re67
	reaction_re67=global_par_kass_re67*x(20)-global_par_kdiss_re67*x(49);

% Reaction: id = re68
	reaction_re68=global_par_kass_re68*x(20)-global_par_kdiss_re68*x(51);

% Reaction: id = re69
	reaction_re69=global_par_kass_re69*x(20)-global_par_kdiss_re69*x(53);

% Reaction: id = re70
	reaction_re70=global_par_kass_re70*x(20)-global_par_kdiss_re70*x(54);

% Reaction: id = re71
	reaction_re71=global_par_kass_re71*x(20)-global_par_kdiss_re71*x(55);

% Reaction: id = re72
	reaction_re72=global_par_kass_re72*x(40)-global_par_kdiss_re72*x(57);

% Reaction: id = re73
	reaction_re73=global_par_kass_re73*x(53)-global_par_kdiss_re73*x(57);

% Reaction: id = re74
	reaction_re74=global_par_kass_re74*x(54)-global_par_kdiss_re74*x(57);

% Reaction: id = re75
	reaction_re75=global_par_kass_re75*x(52)-global_par_kdiss_re75*x(57);

% Reaction: id = re76
	reaction_re76=global_par_kass_re76*x(50)-global_par_kdiss_re76*x(57);

% Reaction: id = re77
	reaction_re77=global_par_kass_re77*x(56)-global_par_kdiss_re77*x(57);

% Reaction: id = re78
	reaction_re78=global_par_kass_re78*x(48)-global_par_kdiss_re78*x(57);

% Reaction: id = re79
	reaction_re79=global_par_kass_re79*x(30)-global_par_kdiss_re79*x(43);

% Reaction: id = re80
	reaction_re80=global_par_kass_re80*x(55)-global_par_kdiss_re80*x(57);

% Reaction: id = re81
	reaction_re81=global_par_kass_re81*x(42)-global_par_kdiss_re81*x(57);

% Reaction: id = re82
	reaction_re82=global_par_kass_re82*x(44)-global_par_kdiss_re82*x(57);

% Reaction: id = re83
	reaction_re83=global_par_kass_re83*x(38)-global_par_kdiss_re83*x(57);

% Reaction: id = re84
	reaction_re84=global_par_kass_re84*x(36)-global_par_kdiss_re84*x(57);

% Reaction: id = re85
	reaction_re85=global_par_kass_re85*x(34)-global_par_kdiss_re85*x(57);

% Reaction: id = re86
	reaction_re86=global_par_kass_re86*x(46)-global_par_kdiss_re86*x(57);



	xdot=zeros(57,1);
	
% Species:   id = s1, name = Cold, affected by kineticLaw
	xdot(1) = (-1.0 * reaction_re1);
	
% Species:   id = s2, name = Salt, affected by kineticLaw
	xdot(2) = (-1.0 * reaction_re2) + (-1.0 * reaction_re3) + (-1.0 * reaction_re4);
	
% Species:   id = s3, name = Drought, affected by kineticLaw
	xdot(3) = (-1.0 * reaction_re8) + (-1.0 * reaction_re9);
	
% Species:   id = s4, name = H2O2, affected by kineticLaw
	xdot(4) = (-1.0 * reaction_re10);
	
% Species:   id = s5, name = Heavy Metal, affected by kineticLaw
	xdot(5) = (-1.0 * reaction_re11);
	
% Species:   id = s6, name = Ethylene, affected by kineticLaw
	xdot(6) = (-1.0 * reaction_re5) + (-1.0 * reaction_re6) + (-1.0 * reaction_re7);
	
% Species:   id = s7, name = RLKs, affected by kineticLaw
	xdot(7) = ( 1.0 * reaction_re1) + ( 1.0 * reaction_re2) + ( 1.0 * reaction_re9) + ( 1.0 * reaction_re10) + ( 1.0 * reaction_re11) + (-1.0 * reaction_re13) + (-1.0 * reaction_re18);
	
% Species:   id = s8, name = LRR, affected by kineticLaw
	xdot(8) = ( 1.0 * reaction_re3) + (-1.0 * reaction_re14);
	
% Species:   id = s9, name = CRKs, affected by kineticLaw
	xdot(9) = ( 1.0 * reaction_re4) + ( 1.0 * reaction_re8) + (-1.0 * reaction_re15);
	
% Species:   id = s10, name = LecRK2, affected by kineticLaw
	xdot(10) = ( 1.0 * reaction_re7) + (-1.0 * reaction_re16);
	
% Species:   id = s11, name = ETR1, affected by kineticLaw
	xdot(11) = ( 1.0 * reaction_re6) + (-1.0 * reaction_re20);
	
% Species:   id = s12, name = ETR2, affected by kineticLaw
	xdot(12) = ( 1.0 * reaction_re5) + (-1.0 * reaction_re21);
	
% Species:   id = s13, name = MAPKKK, affected by kineticLaw
	xdot(13) = (-1.0 * reaction_re12) + ( 1.0 * reaction_re13) + ( 1.0 * reaction_re14) + ( 1.0 * reaction_re15) + ( 1.0 * reaction_re16);
	
% Species:   id = s14, name = MAPKKK, affected by kineticLaw
	xdot(14) = ( 1.0 * reaction_re12) + (-1.0 * reaction_re17) + (-1.0 * reaction_re19) + (-1.0 * reaction_re23);
	
% Species:   id = s15, name = MAPKKK1, affected by kineticLaw
	xdot(15) = ( 1.0 * reaction_re17) + ( 1.0 * reaction_re18) + (-1.0 * reaction_re34) + (-1.0 * reaction_re35);
	
% Species:   id = s16, name = CTR1, affected by kineticLaw
	xdot(16) = ( 1.0 * reaction_re19) + ( 1.0 * reaction_re20) + ( 1.0 * reaction_re21) + (-1.0 * reaction_re36);
	
% Species:   id = s17, name = MAPKK, affected by kineticLaw
	xdot(17) = (-1.0 * reaction_re22) + ( 1.0 * reaction_re23);
	
% Species:   id = s18, name = MAPKK, affected by kineticLaw
	xdot(18) = ( 1.0 * reaction_re22) + (-1.0 * reaction_re24) + (-1.0 * reaction_re25) + (-1.0 * reaction_re26) + (-1.0 * reaction_re27) + (-1.0 * reaction_re28) + (-1.0 * reaction_re29) + (-1.0 * reaction_re30) + (-1.0 * reaction_re31) + (-1.0 * reaction_re33);
	
% Species:   id = s27, name = MAPK, affected by kineticLaw
	xdot(19) = (-1.0 * reaction_re32) + ( 1.0 * reaction_re33);
	
% Species:   id = s28, name = MAPK, affected by kineticLaw
	xdot(20) = ( 1.0 * reaction_re32) + (-1.0 * reaction_re37) + (-1.0 * reaction_re38) + (-1.0 * reaction_re39) + (-1.0 * reaction_re40) + (-1.0 * reaction_re66) + (-1.0 * reaction_re67) + (-1.0 * reaction_re68) + (-1.0 * reaction_re69) + (-1.0 * reaction_re70) + (-1.0 * reaction_re71);
	
% Species:   id = s19, name = MAPKK1, affected by kineticLaw
	xdot(21) = ( 1.0 * reaction_re24) + ( 1.0 * reaction_re34);
	
% Species:   id = s20, name = MAPKK2, affected by kineticLaw
	xdot(22) = ( 1.0 * reaction_re25) + ( 1.0 * reaction_re35) + (-1.0 * reaction_re41) + (-1.0 * reaction_re42) + (-1.0 * reaction_re43);
	
% Species:   id = s21, name = MAPKK3, affected by kineticLaw
	xdot(23) = ( 1.0 * reaction_re26);
	
% Species:   id = s22, name = MAPKK4, affected by kineticLaw
	xdot(24) = ( 1.0 * reaction_re27);
	
% Species:   id = s23, name = MAPKK5, affected by kineticLaw
	xdot(25) = ( 1.0 * reaction_re28);
	
% Species:   id = s24, name = MAPKK6, affected by kineticLaw
	xdot(26) = ( 1.0 * reaction_re29);
	
% Species:   id = s25, name = MAPKK7, affected by kineticLaw
	xdot(27) = ( 1.0 * reaction_re30);
	
% Species:   id = s26, name = MAPKK9, affected by kineticLaw
	xdot(28) = ( 1.0 * reaction_re31) + ( 1.0 * reaction_re36) + (-1.0 * reaction_re44);
	
% Species:   id = s29, name = MAPK2, affected by kineticLaw
	xdot(29) = ( 1.0 * reaction_re37) + (-1.0 * reaction_re55) + (-1.0 * reaction_re56);
	
% Species:   id = s30, name = MAPK3, affected by kineticLaw
	xdot(30) = ( 1.0 * reaction_re38) + ( 1.0 * reaction_re41) + ( 1.0 * reaction_re44) + (-1.0 * reaction_re57) + (-1.0 * reaction_re58) + (-1.0 * reaction_re59) + (-1.0 * reaction_re79);
	
% Species:   id = s31, name = MAPK4, affected by kineticLaw
	xdot(31) = ( 1.0 * reaction_re39) + ( 1.0 * reaction_re42) + (-1.0 * reaction_re60) + (-1.0 * reaction_re61) + (-1.0 * reaction_re62);
	
% Species:   id = s32, name = MAPK6, affected by kineticLaw
	xdot(32) = ( 1.0 * reaction_re40) + ( 1.0 * reaction_re43) + (-1.0 * reaction_re63) + (-1.0 * reaction_re64) + (-1.0 * reaction_re65);
	
% Species:   id = s33, name = WRKY1, affected by kineticLaw
	xdot(33) = (-1.0 * reaction_re45) + ( 1.0 * reaction_re56) + ( 1.0 * reaction_re60);
	
% Species:   id = s34, name = WRKY1, affected by kineticLaw
	xdot(34) = ( 1.0 * reaction_re45) + (-1.0 * reaction_re85);
	
% Species:   id = s35, name = WRKY12, affected by kineticLaw
	xdot(35) = (-1.0 * reaction_re46) + ( 1.0 * reaction_re57) + ( 1.0 * reaction_re65);
	
% Species:   id = s36, name = WRKY12, affected by kineticLaw
	xdot(36) = ( 1.0 * reaction_re46) + (-1.0 * reaction_re84);
	
% Species:   id = s37, name = WRKY8, affected by kineticLaw
	xdot(37) = (-1.0 * reaction_re47) + ( 1.0 * reaction_re55);
	
% Species:   id = s38, name = WRKY8, affected by kineticLaw
	xdot(38) = ( 1.0 * reaction_re47) + (-1.0 * reaction_re83);
	
% Species:   id = s39, name = WRKY25, affected by kineticLaw
	xdot(39) = (-1.0 * reaction_re48) + ( 1.0 * reaction_re62);
	
% Species:   id = s40, name = WRKY25, affected by kineticLaw
	xdot(40) = ( 1.0 * reaction_re48) + (-1.0 * reaction_re72);
	
% Species:   id = s41, name = WRKY22, affected by kineticLaw
	xdot(41) = (-1.0 * reaction_re49) + ( 1.0 * reaction_re58);
	
% Species:   id = s42, name = WRKY22, affected by kineticLaw
	xdot(42) = ( 1.0 * reaction_re49) + (-1.0 * reaction_re81);
	
% Species:   id = s43, name = WRKY29, affected by kineticLaw
	xdot(43) = (-1.0 * reaction_re50) + ( 1.0 * reaction_re79);
	
% Species:   id = s44, name = WRKY29, affected by kineticLaw
	xdot(44) = ( 1.0 * reaction_re50) + (-1.0 * reaction_re82);
	
% Species:   id = s45, name = WRKY33, affected by kineticLaw
	xdot(45) = (-1.0 * reaction_re51) + ( 1.0 * reaction_re61) + ( 1.0 * reaction_re64);
	
% Species:   id = s46, name = WRKY33, affected by kineticLaw
	xdot(46) = ( 1.0 * reaction_re51) + (-1.0 * reaction_re86);
	
% Species:   id = s47, name = WRKY28, affected by kineticLaw
	xdot(47) = (-1.0 * reaction_re52) + ( 1.0 * reaction_re59) + ( 1.0 * reaction_re63);
	
% Species:   id = s48, name = WRKY28, affected by kineticLaw
	xdot(48) = ( 1.0 * reaction_re52) + (-1.0 * reaction_re78);
	
% Species:   id = s49, name = MYB2, affected by kineticLaw
	xdot(49) = (-1.0 * reaction_re53) + ( 1.0 * reaction_re67);
	
% Species:   id = s50, name = MYB2, affected by kineticLaw
	xdot(50) = ( 1.0 * reaction_re53) + (-1.0 * reaction_re76);
	
% Species:   id = s51, name = MYB4, affected by kineticLaw
	xdot(51) = (-1.0 * reaction_re54) + ( 1.0 * reaction_re68);
	
% Species:   id = s52, name = MYB4, affected by kineticLaw
	xdot(52) = ( 1.0 * reaction_re54) + (-1.0 * reaction_re75);
	
% Species:   id = s53, name = MYB44, affected by kineticLaw
	xdot(53) = ( 1.0 * reaction_re69) + (-1.0 * reaction_re73);
	
% Species:   id = s54, name = NAC, affected by kineticLaw
	xdot(54) = ( 1.0 * reaction_re70) + (-1.0 * reaction_re74);
	
% Species:   id = s55, name = bZIP, affected by kineticLaw
	xdot(55) = ( 1.0 * reaction_re71) + (-1.0 * reaction_re80);
	
% Species:   id = s56, name = AP2, affected by kineticLaw
	xdot(56) = ( 1.0 * reaction_re66) + (-1.0 * reaction_re77);
	
% Species:   id = s57, name = Response, affected by kineticLaw
	xdot(57) = ( 1.0 * reaction_re72) + ( 1.0 * reaction_re73) + ( 1.0 * reaction_re74) + ( 1.0 * reaction_re75) + ( 1.0 * reaction_re76) + ( 1.0 * reaction_re77) + ( 1.0 * reaction_re78) + ( 1.0 * reaction_re80) + ( 1.0 * reaction_re81) + ( 1.0 * reaction_re82) + ( 1.0 * reaction_re83) + ( 1.0 * reaction_re84) + ( 1.0 * reaction_re85) + ( 1.0 * reaction_re86);
end

% adding few functions representing operators used in SBML but not present directly 
% in either matlab or octave. 
function z=pow(x,y),z=x^y;end
function z=root(x,y),z=y^(1/x);end

