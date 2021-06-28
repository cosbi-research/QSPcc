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
% Model name = Capuani2015 - Binding of Cbl and Grb2 to EGFR (Early Activation Model - EAM)
%
% is http://identifiers.org/biomodels.db/MODEL1505190000
% is http://identifiers.org/biomodels.db/BIOMD0000000595
% isDescribedBy http://identifiers.org/pubmed/26264748
%


function Capuani2015()
tic;
%Initial conditions vector
	x0=zeros(218,1);
	x0(1) = 17.0;
	x0(2) = 2.80975332814237E-5;
	x0(3) = 3.32;
	x0(4) = 0.0146119024667186;
	x0(5) = 0;
	x0(6) = 0;
	x0(7) = 0;
	x0(8) = 0;
	x0(9) = 0;
	x0(10) = 0;
	x0(11) = 0.652109629206172;
	x0(12) = 0.0;
	x0(13) = 0.0;
	x0(14) = 0.0;
	x0(15) = 0.0;
	x0(16) = 0.0;
	x0(17) = 0.0;
	x0(18) = 0.0;
	x0(19) = 0.0;
	x0(20) = 0.0;
	x0(21) = 0.0;
	x0(22) = 0.0;
	x0(23) = 0.0;
	x0(24) = 0.0;
	x0(25) = 0.0;
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
	x0(38) = 0.0;
	x0(39) = 0.0;
	x0(40) = 0.0;
	x0(41) = 0.0;
	x0(42) = 0.0;
	x0(43) = 0.0;
	x0(44) = 0.0;
	x0(45) = 0.0;
	x0(46) = 0.0;
	x0(47) = 0.0;
	x0(48) = 0.0;
	x0(49) = 0.0;
	x0(50) = 0.0;
	x0(51) = 0.0;
	x0(52) = 0.0;
	x0(53) = 0.0;
	x0(54) = 0.0;
	x0(55) = 0.0;
	x0(56) = 0.0;
	x0(57) = 0.0;
	x0(58) = 0.0;
	x0(59) = 0.0;
	x0(60) = 0.0;
	x0(61) = 0.0;
	x0(62) = 0.0;
	x0(63) = 0.0203784259126929;
	x0(64) = 0.0;
	x0(65) = 0.0;
	x0(66) = 0.0;
	x0(67) = 0.0;
	x0(68) = 0.0;
	x0(69) = 0.0;
	x0(70) = 0.0;
	x0(71) = 0.0;
	x0(72) = 0.0;
	x0(73) = 0.0;
	x0(74) = 0.0;
	x0(75) = 0.0;
	x0(76) = 0.0;
	x0(77) = 0.0;
	x0(78) = 0.0;
	x0(79) = 0.0;
	x0(80) = 0.0;
	x0(81) = 0.0;
	x0(82) = 0.0;
	x0(83) = 0.0;
	x0(84) = 0.0;
	x0(85) = 0.0;
	x0(86) = 0.0;
	x0(87) = 0.0;
	x0(88) = 0.0;
	x0(89) = 0.0;
	x0(90) = 0.0;
	x0(91) = 0.0;
	x0(92) = 0.0;
	x0(93) = 0.0;
	x0(94) = 0.0;
	x0(95) = 0.0;
	x0(96) = 0.0;
	x0(97) = 0.0;
	x0(98) = 0.0;
	x0(99) = 0.0;
	x0(100) = 0.0;
	x0(101) = 0.0;
	x0(102) = 0.0;
	x0(103) = 0.0;
	x0(104) = 0.0;
	x0(105) = 0.0;
	x0(106) = 0.0;
	x0(107) = 0.0;
	x0(108) = 0.0;
	x0(109) = 0.0;
	x0(110) = 0.0;
	x0(111) = 0.0;
	x0(112) = 0.0;
	x0(113) = 0.0;
	x0(114) = 0.0;
	x0(115) = 0.157511944881135;
	x0(116) = 0.0;
	x0(117) = 0.0;
	x0(118) = 0.0;
	x0(119) = 0.0;
	x0(120) = 0.0;
	x0(121) = 0.0;
	x0(122) = 0.0;
	x0(123) = 0.0;
	x0(124) = 0.0;
	x0(125) = 0.0;
	x0(126) = 0.0;
	x0(127) = 0.0;
	x0(128) = 0.0;
	x0(129) = 0.0;
	x0(130) = 0.0;
	x0(131) = 0.0;
	x0(132) = 0.0;
	x0(133) = 0.0;
	x0(134) = 0.0;
	x0(135) = 0.0;
	x0(136) = 0.0;
	x0(137) = 0.0;
	x0(138) = 0.0;
	x0(139) = 0.0;
	x0(140) = 0.0;
	x0(141) = 0.0;
	x0(142) = 0.0;
	x0(143) = 0.0;
	x0(144) = 0.0;
	x0(145) = 0.0;
	x0(146) = 0.0;
	x0(147) = 0.0;
	x0(148) = 0.0;
	x0(149) = 0.0;
	x0(150) = 0.0;
	x0(151) = 0.0;
	x0(152) = 0.0;
	x0(153) = 0.0;
	x0(154) = 0.0;
	x0(155) = 0.0;
	x0(156) = 0.0;
	x0(157) = 0.0;
	x0(158) = 0.0;
	x0(159) = 0.0;
	x0(160) = 0.0;
	x0(161) = 0.0;
	x0(162) = 0.0;
	x0(163) = 0.0;
	x0(164) = 0.0;
	x0(165) = 0.0;
	x0(166) = 0.0;
	x0(167) = 0.0;
	x0(168) = 0.0;
	x0(169) = 0.0;
	x0(170) = 0.0;
	x0(171) = 0.0;
	x0(172) = 0.0;
	x0(173) = 0.0;
	x0(174) = 0.0;
	x0(175) = 0.0;
	x0(176) = 0.0;
	x0(177) = 0.0;
	x0(178) = 0.0;
	x0(179) = 0.0;
	x0(180) = 0.0;
	x0(181) = 0.0;
	x0(182) = 0.0;
	x0(183) = 0.0;
	x0(184) = 0.0;
	x0(185) = 0.0;
	x0(186) = 0.0;
	x0(187) = 0.0;
	x0(188) = 0.0;
	x0(189) = 0.0;
	x0(190) = 0.0;
	x0(191) = 0.0;
	x0(192) = 0.0;
	x0(193) = 0.0;
	x0(194) = 0.0;
	x0(195) = 0.0;
	x0(196) = 0.0;
	x0(197) = 0.0;
	x0(198) = 0.0;
	x0(199) = 0.0;
	x0(200) = 0.0;
	x0(201) = 0.0;
	x0(202) = 0.0;
	x0(203) = 0.0;
	x0(204) = 0.0;
	x0(205) = 0.0;
	x0(206) = 0.0;
	x0(207) = 0.0;
	x0(208) = 0.0;
	x0(209) = 0.0;
	x0(210) = 0.0;
	x0(211) = 0.0;
	x0(212) = 0.0;
	x0(213) = 0.0;
	x0(214) = 0.0;
	x0(215) = 0.0;
	x0(216) = 0.0;
	x0(217) = 0.0;
	x0(218) = 0.0;


% Depending on whether you are using Octave or Matlab,
% you should comment / uncomment one of the following blocks.
% This should also be done for the definition of the function f below.
% Start Matlab code
	tspan=[0:0.01:120];
	opts = odeset('AbsTol',1e-6,'RelTol',1e-3);
	[t,x]=ode23(@f,tspan,x0,opts);
% End Matlab code
disp(toc);
% Start Octave code
%	t=linspace(0,100,100);
%	x=lsode('f',x0,t);
% End Octave code

csvwrite('results.csv',[t,x]);
%	plot(t,x);
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

% Compartment: id = cell, name = cell, constant
	compartment_cell=1.0;
% Parameter:   id =  Ke1, name = Ke1
% Parameter:   id =  K45P, name = K45P
% Parameter:   id =  KcgP, name = KcgP
% Parameter:   id =  ku, name = ku
% Parameter:   id =  ko, name = ko
% Parameter:   id =  k1b, name = k1b
% Parameter:   id =  k1o, name = k1o
% Parameter:   id =  kptp68, name = kptp68
% Parameter:   id =  kkin68, name = kkin68
% Parameter:   id =  kb45, name = kb45
% Parameter:   id =  ku45M, name = ku45M
% Parameter:   id =  kb45P, name = kb45P
% Parameter:   id =  kb68, name = kb68
% Parameter:   id =  ku68, name = ku68
% Parameter:   id =  kb68P, name = kb68P
% Parameter:   id =  ku68M, name = ku68M
% Parameter:   id =  kbcg, name = kbcg
% Parameter:   id =  kbcgP, name = kbcgP
% Parameter:   id =  kucgM, name = kucgM
% Parameter:   id =  Ltot, name = Ltot
	global_par_Ltot=0.0;
% Parameter:   id =  RT, name = RT
	global_par_RT=0.83;
% Parameter:   id =  CblT, name = CblT
	global_par_CblT=0.01464;
% Parameter:   id =  Grb2T, name = Grb2T
	global_par_Grb2T=3.32;
% Parameter:   id =  K, name = K
	global_par_K=3.38608;
% Parameter:   id =  Ke, name = Ke
	global_par_Ke=32.0;
% Parameter:   id =  K1, name = K1
	global_par_K1=52.0;
% Parameter:   id =  kptp, name = kptp
	global_par_kptp=0.016;
% Parameter:   id =  kkin, name = kkin
	global_par_kkin=0.289502;
% Parameter:   id =  K45, name = K45
	global_par_K45=0.201361;
% Parameter:   id =  Kcg, name = Kcg
	global_par_Kcg=0.006356;
% Parameter:   id =  ku45, name = ku45
	global_par_ku45=0.001011;
% Parameter:   id =  kucg, name = kucg
	global_par_kucg=0.309271;
% Parameter:   id =  kb, name = kb
	global_par_kb=5.0;
% Parameter:   id =  kc, name = kc
	global_par_kc=10.0;
% Parameter:   id =  kuDIM, name = kuDIM
	global_par_kuDIM=1.0546;
% Parameter:   id =  kbDIM, name = kbDIM
	global_par_kbDIM=200.0;
% Parameter:   id =  k1u, name = k1u
	global_par_k1u=0.15;
% Parameter:   id =  k1c, name = k1c
	global_par_k1c=10.0;
% Parameter:   id =  PYMax, name = PYMax
	global_par_PYMax=2.34537527177036;
% Parameter:   id =  UbMax, name = UbMax
	global_par_UbMax=0.014306;
% Parameter:   id =  floc, name = floc
	global_par_floc=20000.0;
% Parameter:   id =  CblWT, name = CblWT
	global_par_CblWT=0.01464;
% Parameter:   id =  CblFactor, name = CblFactor
	global_par_CblFactor=1.0;
% assignmentRule: variable = Ke1
	global_par_Ke1=global_par_K*global_par_Ke/global_par_K1;
% assignmentRule: variable = K45P
	global_par_K45P=global_par_K45/global_par_floc;
% assignmentRule: variable = KcgP
	global_par_KcgP=global_par_Kcg/global_par_floc;
% assignmentRule: variable = ku
	global_par_ku=global_par_kb*global_par_K;
% assignmentRule: variable = ko
	global_par_ko=global_par_kc/global_par_Ke;
% assignmentRule: variable = k1b
	global_par_k1b=global_par_k1u/global_par_K1;
% assignmentRule: variable = k1o
	global_par_k1o=global_par_k1c/global_par_Ke1;
% assignmentRule: variable = kkin68
	global_par_kkin68=global_par_kkin;
% assignmentRule: variable = kptp68
	global_par_kptp68=global_par_kptp;
% assignmentRule: variable = kb45
	global_par_kb45=global_par_ku45/global_par_K45;
% assignmentRule: variable = kb68
	global_par_kb68=global_par_kb45;
% assignmentRule: variable = ku68
	global_par_ku68=global_par_ku45;
% assignmentRule: variable = kbcg
	global_par_kbcg=global_par_kucg/global_par_Kcg;
% assignmentRule: variable = ku45M
	global_par_ku45M=global_par_ku45;
% assignmentRule: variable = kb45P
	global_par_kb45P=global_par_ku45M/global_par_K45P;
% assignmentRule: variable = kb68P
	global_par_kb68P=global_par_kb45P;
% assignmentRule: variable = ku68M
	global_par_ku68M=global_par_ku45M;
% assignmentRule: variable = kucgM
	global_par_kucgM=global_par_kucg;
% assignmentRule: variable = kbcgP
	global_par_kbcgP=global_par_kucgM/global_par_KcgP;
% assignmentRule: variable = SumM
	x(9)=x(63)+x(64)+x(65)+x(66)+x(67)+x(68)+x(69)+x(70)+x(71)+x(72)+x(73)+x(74)+x(75)+x(76)+x(77)+x(78)+x(79)+x(80)+x(81)+x(82)+x(83)+x(84)+x(85)+x(86)+x(87)+x(88);
% assignmentRule: variable = SumML
	x(10)=x(89)+x(90)+x(91)+x(92)+x(93)+x(94)+x(95)+x(96)+x(97)+x(98)+x(99)+x(100)+x(101)+x(102)+x(103)+x(104)+x(105)+x(106)+x(107)+x(108)+x(109)+x(110)+x(111)+x(112)+x(113)+x(114);
% assignmentRule: variable = PY
	x(5)=x(12)+x(13)+x(14)+x(38)+x(39)+x(40)+x(64)+x(65)+x(66)+x(90)+x(91)+x(92)+x(116)+x(117)+x(118)+x(142)+x(143)+x(144)+x(168)+x(169)+x(170)+x(194)+x(195)+x(196)+1*(x(15)+x(16)+x(17)+x(41)+x(42)+x(43)+x(67)+x(68)+x(69)+x(93)+x(94)+x(95)+x(119)+x(120)+x(121)+x(145)+x(146)+x(147)+x(171)+x(172)+x(173)+x(197)+x(198)+x(199))+2*(x(18)+x(19)+x(20)+x(21)+x(22)+x(23)+x(24)+x(25)+x(44)+x(45)+x(46)+x(47)+x(48)+x(49)+x(50)+x(51)+x(70)+x(71)+x(72)+x(73)+x(74)+x(75)+x(76)+x(77)+x(96)+x(97)+x(98)+x(99)+x(100)+x(101)+x(102)+x(103)+x(122)+x(123)+x(124)+x(125)+x(126)+x(127)+x(128)+x(129)+x(148)+x(149)+x(150)+x(151)+x(152)+x(153)+x(154)+x(155)+x(174)+x(175)+x(176)+x(177)+x(178)+x(179)+x(180)+x(181)+x(200)+x(201)+x(202)+x(203)+x(204)+x(205)+x(206)+x(207))+2*(x(26)+x(27)+x(28)+x(52)+x(53)+x(54)+x(78)+x(79)+x(80)+x(104)+x(105)+x(106)+x(130)+x(131)+x(132)+x(156)+x(157)+x(158)+x(182)+x(183)+x(184)+x(208)+x(209)+x(210))+3*(x(29)+x(30)+x(31)+x(32)+x(33)+x(34)+x(35)+x(36)+x(55)+x(56)+x(57)+x(58)+x(59)+x(60)+x(61)+x(62)+x(81)+x(82)+x(83)+x(84)+x(85)+x(86)+x(87)+x(88)+x(107)+x(108)+x(109)+x(110)+x(111)+x(112)+x(113)+x(114)+x(133)+x(134)+x(135)+x(136)+x(137)+x(138)+x(139)+x(140)+x(159)+x(160)+x(161)+x(162)+x(163)+x(164)+x(165)+x(166)+x(185)+x(186)+x(187)+x(188)+x(189)+x(190)+x(191)+x(192)+x(211)+x(212)+x(213)+x(214)+x(215)+x(216)+x(217)+x(218));
% assignmentRule: variable = Ub
	x(6)=x(24)+x(35)+x(50)+x(61)+x(76)+x(87)+x(102)+x(113)+x(128)+x(139)+x(154)+x(165)+x(180)+x(191)+x(206)+x(217)+x(13)+x(19)+x(30)+x(39)+x(45)+x(56)+x(65)+x(71)+x(82)+x(91)+x(97)+x(108)+x(117)+x(123)+x(134)+x(143)+x(149)+x(160)+x(169)+x(175)+x(186)+x(195)+x(201)+x(212)+x(14)+x(20)+x(31)+x(40)+x(46)+x(57)+x(66)+x(72)+x(83)+x(92)+x(98)+x(109)+x(118)+x(124)+x(135)+x(144)+x(150)+x(161)+x(170)+x(176)+x(187)+x(196)+x(202)+x(213)+x(23)+x(34)+x(49)+x(60)+x(75)+x(86)+x(101)+x(112)+x(127)+x(138)+x(153)+x(164)+x(179)+x(190)+x(205)+x(216)+x(25)+x(36)+x(51)+x(62)+x(77)+x(88)+x(103)+x(114)+x(129)+x(140)+x(155)+x(166)+x(181)+x(192)+x(207)+x(218);
% assignmentRule: variable = PYNorm
	x(7)=x(5)/global_par_PYMax;
% assignmentRule: variable = UbNorm
	x(8)=x(6)/(global_par_UbMax*global_par_CblFactor);

% Reaction: id = r1, name = Rc10UU has site Y1045 dephosphorylated
	reaction_r1=global_par_kptp*x(12);

% Reaction: id = r2, name = Rc01UU has site Y1068/Y1086 dephosphorylated
	reaction_r2=global_par_kptp68*x(15);

% Reaction: id = r3, name = Rc11UU has site Y1045 dephosphorylated
	reaction_r3=global_par_kptp*x(18);

% Reaction: id = r4, name = Rc11UU has site Y1068/Y1086 dephosphorylated
	reaction_r4=global_par_kptp68*x(18);

% Reaction: id = r5, name = Rc11CU has site Y1068/Y1086 dephosphorylated
	reaction_r5=global_par_kptp68*x(19);

% Reaction: id = r6, name = Rc11LU has site Y1068/Y1086 dephosphorylated
	reaction_r6=global_par_kptp68*x(20);

% Reaction: id = r7, name = Rc11UG has site Y1045 dephosphorylated
	reaction_r7=global_par_kptp*x(21);

% Reaction: id = r8, name = Rc11UL has site Y1045 dephosphorylated
	reaction_r8=global_par_kptp*x(22);

% Reaction: id = r9, name = Rc02UU has site Y1068/Y1086 dephosphorylated
	reaction_r9=2*global_par_kptp68*x(26);

% Reaction: id = r10, name = Rc02UG has site Y1068/Y1-86 dephosphorylated
	reaction_r10=global_par_kptp68*x(27);

% Reaction: id = r11, name = Rc02UL has site Y1068/Y1-86 dephosphorylated
	reaction_r11=global_par_kptp68*x(28);

% Reaction: id = r12, name = Rc12UU has site Y1045 dephosphorylated
	reaction_r12=global_par_kptp*x(29);

% Reaction: id = r13, name = Rc12UU has site Y1068/Y1086 dephosphorylated
	reaction_r13=2*global_par_kptp68*x(29);

% Reaction: id = r14, name = Rc12CU has site Y1068/Y1086 dephosphorylated
	reaction_r14=2*global_par_kptp68*x(30);

% Reaction: id = r15, name = Rc12LU has site Y1068/Y1086 dephosphorylated
	reaction_r15=2*global_par_kptp68*x(31);

% Reaction: id = r16, name = Rc12UG has site Y1045 dephosphorylated
	reaction_r16=global_par_kptp*x(32);

% Reaction: id = r17, name = Rc12UG has site Y1068/Y1-86 dephosphorylated
	reaction_r17=global_par_kptp68*x(32);

% Reaction: id = r18, name = Rc12UL has site Y1045 dephosphorylated
	reaction_r18=global_par_kptp*x(33);

% Reaction: id = r19, name = Rc12UL has site Y1068/Y1-86 dephosphorylated
	reaction_r19=global_par_kptp68*x(33);

% Reaction: id = r20, name = Rc12CG has site Y1068/Y1-86 dephosphorylated
	reaction_r20=global_par_kptp68*x(34);

% Reaction: id = r21, name = Rc12CC has site Y1068/Y1-86 dephosphorylated
	reaction_r21=global_par_kptp68*x(35);

% Reaction: id = r22, name = Rc12LG has site Y1068/Y1-86 dephosphorylated
	reaction_r22=global_par_kptp68*x(36);

% Reaction: id = r23, name = RcL10UU has site Y1045 dephosphorylated
	reaction_r23=global_par_kptp*x(38);

% Reaction: id = r24, name = RcL01UU has site Y1068/Y1086 dephosphorylated
	reaction_r24=global_par_kptp68*x(41);

% Reaction: id = r25, name = RcL11UU has site Y1045 dephosphorylated
	reaction_r25=global_par_kptp*x(44);

% Reaction: id = r26, name = RcL11UU has site Y1068/Y1086 dephosphorylated
	reaction_r26=global_par_kptp68*x(44);

% Reaction: id = r27, name = RcL11CU has site Y1068/Y1086 dephosphorylated
	reaction_r27=global_par_kptp68*x(45);

% Reaction: id = r28, name = RcL11LU has site Y1068/Y1086 dephosphorylated
	reaction_r28=global_par_kptp68*x(46);

% Reaction: id = r29, name = RcL11UG has site Y1045 dephosphorylated
	reaction_r29=global_par_kptp*x(47);

% Reaction: id = r30, name = RcL11UL has site Y1045 dephosphorylated
	reaction_r30=global_par_kptp*x(48);

% Reaction: id = r31, name = RcL02UU has site Y1068/Y1086 dephosphorylated
	reaction_r31=2*global_par_kptp68*x(52);

% Reaction: id = r32, name = RcL02UG has site Y1068/Y1-86 dephosphorylated
	reaction_r32=global_par_kptp68*x(53);

% Reaction: id = r33, name = RcL02UL has site Y1068/Y1-86 dephosphorylated
	reaction_r33=global_par_kptp68*x(54);

% Reaction: id = r34, name = RcL12UU has site Y1045 dephosphorylated
	reaction_r34=global_par_kptp*x(55);

% Reaction: id = r35, name = RcL12UU has site Y1068/Y1086 dephosphorylated
	reaction_r35=2*global_par_kptp68*x(55);

% Reaction: id = r36, name = RcL12CU has site Y1068/Y1086 dephosphorylated
	reaction_r36=2*global_par_kptp68*x(56);

% Reaction: id = r37, name = RcL12LU has site Y1068/Y1086 dephosphorylated
	reaction_r37=2*global_par_kptp68*x(57);

% Reaction: id = r38, name = RcL12UG has site Y1045 dephosphorylated
	reaction_r38=global_par_kptp*x(58);

% Reaction: id = r39, name = RcL12UG has site Y1068/Y1-86 dephosphorylated
	reaction_r39=global_par_kptp68*x(58);

% Reaction: id = r40, name = RcL12UL has site Y1045 dephosphorylated
	reaction_r40=global_par_kptp*x(59);

% Reaction: id = r41, name = RcL12UL has site Y1068/Y1-86 dephosphorylated
	reaction_r41=global_par_kptp68*x(59);

% Reaction: id = r42, name = RcL12CG has site Y1068/Y1-86 dephosphorylated
	reaction_r42=global_par_kptp68*x(60);

% Reaction: id = r43, name = RcL12CC has site Y1068/Y1-86 dephosphorylated
	reaction_r43=global_par_kptp68*x(61);

% Reaction: id = r44, name = RcL12LG has site Y1068/Y1-86 dephosphorylated
	reaction_r44=global_par_kptp68*x(62);

% Reaction: id = r45, name = R10UU has site Y1045 dephosphorylated
	reaction_r45=global_par_kptp*x(64);

% Reaction: id = r46, name = R01UU has site Y1068/Y1086 dephosphorylated
	reaction_r46=global_par_kptp68*x(67);

% Reaction: id = r47, name = R11UU has site Y1045 dephosphorylated
	reaction_r47=global_par_kptp*x(70);

% Reaction: id = r48, name = R11UU has site Y1068/Y1086 dephosphorylated
	reaction_r48=global_par_kptp68*x(70);

% Reaction: id = r49, name = R11CU has site Y1068/Y1086 dephosphorylated
	reaction_r49=global_par_kptp68*x(71);

% Reaction: id = r50, name = R11LU has site Y1068/Y1086 dephosphorylated
	reaction_r50=global_par_kptp68*x(72);

% Reaction: id = r51, name = R11UG has site Y1045 dephosphorylated
	reaction_r51=global_par_kptp*x(73);

% Reaction: id = r52, name = R11UL has site Y1045 dephosphorylated
	reaction_r52=global_par_kptp*x(74);

% Reaction: id = r53, name = R02UU has site Y1068/Y1086 dephosphorylated
	reaction_r53=2*global_par_kptp68*x(78);

% Reaction: id = r54, name = R02UG has site Y1068/Y1-86 dephosphorylated
	reaction_r54=global_par_kptp68*x(79);

% Reaction: id = r55, name = R02UL has site Y1068/Y1-86 dephosphorylated
	reaction_r55=global_par_kptp68*x(80);

% Reaction: id = r56, name = R12UU has site Y1045 dephosphorylated
	reaction_r56=global_par_kptp*x(81);

% Reaction: id = r57, name = R12UU has site Y1068/Y1086 dephosphorylated
	reaction_r57=2*global_par_kptp68*x(81);

% Reaction: id = r58, name = R12CU has site Y1068/Y1086 dephosphorylated
	reaction_r58=2*global_par_kptp68*x(82);

% Reaction: id = r59, name = R12LU has site Y1068/Y1086 dephosphorylated
	reaction_r59=2*global_par_kptp68*x(83);

% Reaction: id = r60, name = R12UG has site Y1045 dephosphorylated
	reaction_r60=global_par_kptp*x(84);

% Reaction: id = r61, name = R12UG has site Y1068/Y1-86 dephosphorylated
	reaction_r61=global_par_kptp68*x(84);

% Reaction: id = r62, name = R12UL has site Y1045 dephosphorylated
	reaction_r62=global_par_kptp*x(85);

% Reaction: id = r63, name = R12UL has site Y1068/Y1-86 dephosphorylated
	reaction_r63=global_par_kptp68*x(85);

% Reaction: id = r64, name = R12CG has site Y1068/Y1-86 dephosphorylated
	reaction_r64=global_par_kptp68*x(86);

% Reaction: id = r65, name = R12CC has site Y1068/Y1-86 dephosphorylated
	reaction_r65=global_par_kptp68*x(87);

% Reaction: id = r66, name = R12LG has site Y1068/Y1-86 dephosphorylated
	reaction_r66=global_par_kptp68*x(88);

% Reaction: id = r67, name = RL10UU has site Y1045 dephosphorylated
	reaction_r67=global_par_kptp*x(90);

% Reaction: id = r68, name = RL01UU has site Y1068/Y1086 dephosphorylated
	reaction_r68=global_par_kptp68*x(93);

% Reaction: id = r69, name = RL11UU has site Y1045 dephosphorylated
	reaction_r69=global_par_kptp*x(96);

% Reaction: id = r70, name = RL11UU has site Y1068/Y1086 dephosphorylated
	reaction_r70=global_par_kptp68*x(96);

% Reaction: id = r71, name = RL11CU has site Y1068/Y1086 dephosphorylated
	reaction_r71=global_par_kptp68*x(97);

% Reaction: id = r72, name = RL11LU has site Y1068/Y1086 dephosphorylated
	reaction_r72=global_par_kptp68*x(98);

% Reaction: id = r73, name = RL11UG has site Y1045 dephosphorylated
	reaction_r73=global_par_kptp*x(99);

% Reaction: id = r74, name = RL11UL has site Y1045 dephosphorylated
	reaction_r74=global_par_kptp*x(100);

% Reaction: id = r75, name = RL02UU has site Y1068/Y1086 dephosphorylated
	reaction_r75=2*global_par_kptp68*x(104);

% Reaction: id = r76, name = RL02UG has site Y1068/Y1-86 dephosphorylated
	reaction_r76=global_par_kptp68*x(105);

% Reaction: id = r77, name = RL02UL has site Y1068/Y1-86 dephosphorylated
	reaction_r77=global_par_kptp68*x(106);

% Reaction: id = r78, name = RL12UU has site Y1045 dephosphorylated
	reaction_r78=global_par_kptp*x(107);

% Reaction: id = r79, name = RL12UU has site Y1068/Y1086 dephosphorylated
	reaction_r79=2*global_par_kptp68*x(107);

% Reaction: id = r80, name = RL12CU has site Y1068/Y1086 dephosphorylated
	reaction_r80=2*global_par_kptp68*x(108);

% Reaction: id = r81, name = RL12LU has site Y1068/Y1086 dephosphorylated
	reaction_r81=2*global_par_kptp68*x(109);

% Reaction: id = r82, name = RL12UG has site Y1045 dephosphorylated
	reaction_r82=global_par_kptp*x(110);

% Reaction: id = r83, name = RL12UG has site Y1068/Y1-86 dephosphorylated
	reaction_r83=global_par_kptp68*x(110);

% Reaction: id = r84, name = RL12UL has site Y1045 dephosphorylated
	reaction_r84=global_par_kptp*x(111);

% Reaction: id = r85, name = RL12UL has site Y1068/Y1-86 dephosphorylated
	reaction_r85=global_par_kptp68*x(111);

% Reaction: id = r86, name = RL12CG has site Y1068/Y1-86 dephosphorylated
	reaction_r86=global_par_kptp68*x(112);

% Reaction: id = r87, name = RL12CC has site Y1068/Y1-86 dephosphorylated
	reaction_r87=global_par_kptp68*x(113);

% Reaction: id = r88, name = RL12LG has site Y1068/Y1-86 dephosphorylated
	reaction_r88=global_par_kptp68*x(114);

% Reaction: id = r89, name = Di10UU has site Y1045 dephosphorylated
	reaction_r89=global_par_kptp*x(116);

% Reaction: id = r90, name = Di01UU has site Y1068/Y1086 dephosphorylated
	reaction_r90=global_par_kptp68*x(119);

% Reaction: id = r91, name = Di11UU has site Y1045 dephosphorylated
	reaction_r91=global_par_kptp*x(122);

% Reaction: id = r92, name = Di11UU has site Y1068/Y1086 dephosphorylated
	reaction_r92=global_par_kptp68*x(122);

% Reaction: id = r93, name = Di11CU has site Y1068/Y1086 dephosphorylated
	reaction_r93=global_par_kptp68*x(123);

% Reaction: id = r94, name = Di11LU has site Y1068/Y1086 dephosphorylated
	reaction_r94=global_par_kptp68*x(124);

% Reaction: id = r95, name = Di11UG has site Y1045 dephosphorylated
	reaction_r95=global_par_kptp*x(125);

% Reaction: id = r96, name = Di11UL has site Y1045 dephosphorylated
	reaction_r96=global_par_kptp*x(126);

% Reaction: id = r97, name = Di02UU has site Y1068/Y1086 dephosphorylated
	reaction_r97=2*global_par_kptp68*x(130);

% Reaction: id = r98, name = Di02UG has site Y1068/Y1-86 dephosphorylated
	reaction_r98=global_par_kptp68*x(131);

% Reaction: id = r99, name = Di02UL has site Y1068/Y1-86 dephosphorylated
	reaction_r99=global_par_kptp68*x(132);

% Reaction: id = r100, name = Di12UU has site Y1045 dephosphorylated
	reaction_r100=global_par_kptp*x(133);

% Reaction: id = r101, name = Di12UU has site Y1068/Y1086 dephosphorylated
	reaction_r101=2*global_par_kptp68*x(133);

% Reaction: id = r102, name = Di12CU has site Y1068/Y1086 dephosphorylated
	reaction_r102=2*global_par_kptp68*x(134);

% Reaction: id = r103, name = Di12LU has site Y1068/Y1086 dephosphorylated
	reaction_r103=2*global_par_kptp68*x(135);

% Reaction: id = r104, name = Di12UG has site Y1045 dephosphorylated
	reaction_r104=global_par_kptp*x(136);

% Reaction: id = r105, name = Di12UG has site Y1068/Y1-86 dephosphorylated
	reaction_r105=global_par_kptp68*x(136);

% Reaction: id = r106, name = Di12UL has site Y1045 dephosphorylated
	reaction_r106=global_par_kptp*x(137);

% Reaction: id = r107, name = Di12UL has site Y1068/Y1-86 dephosphorylated
	reaction_r107=global_par_kptp68*x(137);

% Reaction: id = r108, name = Di12CG has site Y1068/Y1-86 dephosphorylated
	reaction_r108=global_par_kptp68*x(138);

% Reaction: id = r109, name = Di12CC has site Y1068/Y1-86 dephosphorylated
	reaction_r109=global_par_kptp68*x(139);

% Reaction: id = r110, name = Di12LG has site Y1068/Y1-86 dephosphorylated
	reaction_r110=global_par_kptp68*x(140);

% Reaction: id = r111, name = Da10UU has site Y1045 dephosphorylated
	reaction_r111=global_par_kptp*x(142);

% Reaction: id = r112, name = Da01UU has site Y1068/Y1086 dephosphorylated
	reaction_r112=global_par_kptp68*x(145);

% Reaction: id = r113, name = Da11UU has site Y1045 dephosphorylated
	reaction_r113=global_par_kptp*x(148);

% Reaction: id = r114, name = Da11UU has site Y1068/Y1086 dephosphorylated
	reaction_r114=global_par_kptp68*x(148);

% Reaction: id = r115, name = Da11CU has site Y1068/Y1086 dephosphorylated
	reaction_r115=global_par_kptp68*x(149);

% Reaction: id = r116, name = Da11LU has site Y1068/Y1086 dephosphorylated
	reaction_r116=global_par_kptp68*x(150);

% Reaction: id = r117, name = Da11UG has site Y1045 dephosphorylated
	reaction_r117=global_par_kptp*x(151);

% Reaction: id = r118, name = Da11UL has site Y1045 dephosphorylated
	reaction_r118=global_par_kptp*x(152);

% Reaction: id = r119, name = Da02UU has site Y1068/Y1086 dephosphorylated
	reaction_r119=2*global_par_kptp68*x(156);

% Reaction: id = r120, name = Da02UG has site Y1068/Y1-86 dephosphorylated
	reaction_r120=global_par_kptp68*x(157);

% Reaction: id = r121, name = Da02UL has site Y1068/Y1-86 dephosphorylated
	reaction_r121=global_par_kptp68*x(158);

% Reaction: id = r122, name = Da12UU has site Y1045 dephosphorylated
	reaction_r122=global_par_kptp*x(159);

% Reaction: id = r123, name = Da12UU has site Y1068/Y1086 dephosphorylated
	reaction_r123=2*global_par_kptp68*x(159);

% Reaction: id = r124, name = Da12CU has site Y1068/Y1086 dephosphorylated
	reaction_r124=2*global_par_kptp68*x(160);

% Reaction: id = r125, name = Da12LU has site Y1068/Y1086 dephosphorylated
	reaction_r125=2*global_par_kptp68*x(161);

% Reaction: id = r126, name = Da12UG has site Y1045 dephosphorylated
	reaction_r126=global_par_kptp*x(162);

% Reaction: id = r127, name = Da12UG has site Y1068/Y1-86 dephosphorylated
	reaction_r127=global_par_kptp68*x(162);

% Reaction: id = r128, name = Da12UL has site Y1045 dephosphorylated
	reaction_r128=global_par_kptp*x(163);

% Reaction: id = r129, name = Da12UL has site Y1068/Y1-86 dephosphorylated
	reaction_r129=global_par_kptp68*x(163);

% Reaction: id = r130, name = Da12CG has site Y1068/Y1-86 dephosphorylated
	reaction_r130=global_par_kptp68*x(164);

% Reaction: id = r131, name = Da12CC has site Y1068/Y1-86 dephosphorylated
	reaction_r131=global_par_kptp68*x(165);

% Reaction: id = r132, name = Da12LG has site Y1068/Y1-86 dephosphorylated
	reaction_r132=global_par_kptp68*x(166);

% Reaction: id = r133, name = DiL10UU has site Y1045 dephosphorylated
	reaction_r133=global_par_kptp*x(168);

% Reaction: id = r134, name = DiL01UU has site Y1068/Y1086 dephosphorylated
	reaction_r134=global_par_kptp68*x(171);

% Reaction: id = r135, name = DiL11UU has site Y1045 dephosphorylated
	reaction_r135=global_par_kptp*x(174);

% Reaction: id = r136, name = DiL11UU has site Y1068/Y1086 dephosphorylated
	reaction_r136=global_par_kptp68*x(174);

% Reaction: id = r137, name = DiL11CU has site Y1068/Y1086 dephosphorylated
	reaction_r137=global_par_kptp68*x(175);

% Reaction: id = r138, name = DiL11LU has site Y1068/Y1086 dephosphorylated
	reaction_r138=global_par_kptp68*x(176);

% Reaction: id = r139, name = DiL11UG has site Y1045 dephosphorylated
	reaction_r139=global_par_kptp*x(177);

% Reaction: id = r140, name = DiL11UL has site Y1045 dephosphorylated
	reaction_r140=global_par_kptp*x(178);

% Reaction: id = r141, name = DiL02UU has site Y1068/Y1086 dephosphorylated
	reaction_r141=2*global_par_kptp68*x(182);

% Reaction: id = r142, name = DiL02UG has site Y1068/Y1-86 dephosphorylated
	reaction_r142=global_par_kptp68*x(183);

% Reaction: id = r143, name = DiL02UL has site Y1068/Y1-86 dephosphorylated
	reaction_r143=global_par_kptp68*x(184);

% Reaction: id = r144, name = DiL12UU has site Y1045 dephosphorylated
	reaction_r144=global_par_kptp*x(185);

% Reaction: id = r145, name = DiL12UU has site Y1068/Y1086 dephosphorylated
	reaction_r145=2*global_par_kptp68*x(185);

% Reaction: id = r146, name = DiL12CU has site Y1068/Y1086 dephosphorylated
	reaction_r146=2*global_par_kptp68*x(186);

% Reaction: id = r147, name = DiL12LU has site Y1068/Y1086 dephosphorylated
	reaction_r147=2*global_par_kptp68*x(187);

% Reaction: id = r148, name = DiL12UG has site Y1045 dephosphorylated
	reaction_r148=global_par_kptp*x(188);

% Reaction: id = r149, name = DiL12UG has site Y1068/Y1-86 dephosphorylated
	reaction_r149=global_par_kptp68*x(188);

% Reaction: id = r150, name = DiL12UL has site Y1045 dephosphorylated
	reaction_r150=global_par_kptp*x(189);

% Reaction: id = r151, name = DiL12UL has site Y1068/Y1-86 dephosphorylated
	reaction_r151=global_par_kptp68*x(189);

% Reaction: id = r152, name = DiL12CG has site Y1068/Y1-86 dephosphorylated
	reaction_r152=global_par_kptp68*x(190);

% Reaction: id = r153, name = DiL12CC has site Y1068/Y1-86 dephosphorylated
	reaction_r153=global_par_kptp68*x(191);

% Reaction: id = r154, name = DiL12LG has site Y1068/Y1-86 dephosphorylated
	reaction_r154=global_par_kptp68*x(192);

% Reaction: id = r155, name = DaL10UU has site Y1045 dephosphorylated
	reaction_r155=global_par_kptp*x(194);

% Reaction: id = r156, name = DaL01UU has site Y1068/Y1086 dephosphorylated
	reaction_r156=global_par_kptp68*x(197);

% Reaction: id = r157, name = DaL11UU has site Y1045 dephosphorylated
	reaction_r157=global_par_kptp*x(200);

% Reaction: id = r158, name = DaL11UU has site Y1068/Y1086 dephosphorylated
	reaction_r158=global_par_kptp68*x(200);

% Reaction: id = r159, name = DaL11CU has site Y1068/Y1086 dephosphorylated
	reaction_r159=global_par_kptp68*x(201);

% Reaction: id = r160, name = DaL11LU has site Y1068/Y1086 dephosphorylated
	reaction_r160=global_par_kptp68*x(202);

% Reaction: id = r161, name = DaL11UG has site Y1045 dephosphorylated
	reaction_r161=global_par_kptp*x(203);

% Reaction: id = r162, name = DaL11UL has site Y1045 dephosphorylated
	reaction_r162=global_par_kptp*x(204);

% Reaction: id = r163, name = DaL02UU has site Y1068/Y1086 dephosphorylated
	reaction_r163=2*global_par_kptp68*x(208);

% Reaction: id = r164, name = DaL02UG has site Y1068/Y1-86 dephosphorylated
	reaction_r164=global_par_kptp68*x(209);

% Reaction: id = r165, name = DaL02UL has site Y1068/Y1-86 dephosphorylated
	reaction_r165=global_par_kptp68*x(210);

% Reaction: id = r166, name = DaL12UU has site Y1045 dephosphorylated
	reaction_r166=global_par_kptp*x(211);

% Reaction: id = r167, name = DaL12UU has site Y1068/Y1086 dephosphorylated
	reaction_r167=2*global_par_kptp68*x(211);

% Reaction: id = r168, name = DaL12CU has site Y1068/Y1086 dephosphorylated
	reaction_r168=2*global_par_kptp68*x(212);

% Reaction: id = r169, name = DaL12LU has site Y1068/Y1086 dephosphorylated
	reaction_r169=2*global_par_kptp68*x(213);

% Reaction: id = r170, name = DaL12UG has site Y1045 dephosphorylated
	reaction_r170=global_par_kptp*x(214);

% Reaction: id = r171, name = DaL12UG has site Y1068/Y1-86 dephosphorylated
	reaction_r171=global_par_kptp68*x(214);

% Reaction: id = r172, name = DaL12UL has site Y1045 dephosphorylated
	reaction_r172=global_par_kptp*x(215);

% Reaction: id = r173, name = DaL12UL has site Y1068/Y1-86 dephosphorylated
	reaction_r173=global_par_kptp68*x(215);

% Reaction: id = r174, name = DaL12CG has site Y1068/Y1-86 dephosphorylated
	reaction_r174=global_par_kptp68*x(216);

% Reaction: id = r175, name = DaL12CC has site Y1068/Y1-86 dephosphorylated
	reaction_r175=global_par_kptp68*x(217);

% Reaction: id = r176, name = DaL12LG has site Y1068/Y1-86 dephosphorylated
	reaction_r176=global_par_kptp68*x(218);

% Reaction: id = r177, name = Da00UU has site Y1045 phosphorylated
	reaction_r177=global_par_kkin*x(141);

% Reaction: id = r178, name = Da00UU has site Y1068/Y1086 phosphorylated
	reaction_r178=2*global_par_kkin68*x(141);

% Reaction: id = r179, name = Da10UU has site Y1068/Y1086 phosphorylated
	reaction_r179=2*global_par_kkin68*x(142);

% Reaction: id = r180, name = Da10CU has site Y1068/Y1086 phosphorylated
	reaction_r180=2*global_par_kkin68*x(143);

% Reaction: id = r181, name = Da10LU has site Y1068/Y1086 phosphorylated
	reaction_r181=2*global_par_kkin68*x(144);

% Reaction: id = r182, name = Da01UU has site Y1045 phosphorylated
	reaction_r182=global_par_kkin*x(145);

% Reaction: id = r183, name = Da01UU has site Y1068/Y1086 phosphorylated
	reaction_r183=global_par_kkin68*x(145);

% Reaction: id = r184, name = Da01UG has site Y1045 phosphorylated
	reaction_r184=global_par_kkin*x(146);

% Reaction: id = r185, name = Da01UG has site Y1068/Y1086 phosphorylated
	reaction_r185=global_par_kkin68*x(146);

% Reaction: id = r186, name = Da01UL has site Y1045 phosphorylated
	reaction_r186=global_par_kkin*x(147);

% Reaction: id = r187, name = Da01UL has site Y1068/Y1086 phosphorylated
	reaction_r187=global_par_kkin68*x(147);

% Reaction: id = r188, name = Da11UU has site Y1068/Y1086 phosphorylated
	reaction_r188=global_par_kkin68*x(148);

% Reaction: id = r189, name = Da11CU has site Y1068/Y1086 phosphorylated
	reaction_r189=global_par_kkin68*x(149);

% Reaction: id = r190, name = Da11LU has site Y1068/Y1086 phosphorylated
	reaction_r190=global_par_kkin68*x(150);

% Reaction: id = r191, name = Da11UG has site Y1068/Y1086 phosphorylated
	reaction_r191=global_par_kkin68*x(151);

% Reaction: id = r192, name = Da11UL has site Y1068/Y1086 phosphorylated
	reaction_r192=global_par_kkin68*x(152);

% Reaction: id = r193, name = Da11CG has site Y1068/Y1086 phosphorylated
	reaction_r193=global_par_kkin68*x(153);

% Reaction: id = r194, name = Da11CC has site Y1068/Y1086 phosphorylated
	reaction_r194=global_par_kkin68*x(154);

% Reaction: id = r195, name = Da11LG has site Y1068/Y1086 phosphorylated
	reaction_r195=global_par_kkin68*x(155);

% Reaction: id = r196, name = Da02UU has site Y1045 phosphorylated
	reaction_r196=global_par_kkin*x(156);

% Reaction: id = r197, name = Da02UG has site Y1045 phosphorylated
	reaction_r197=global_par_kkin*x(157);

% Reaction: id = r198, name = Da02UL has site Y1045 phosphorylated
	reaction_r198=global_par_kkin*x(158);

% Reaction: id = r199, name = DaL00UU has site Y1045 phosphorylated
	reaction_r199=global_par_kkin*x(193);

% Reaction: id = r200, name = DaL00UU has site Y1068/Y1086 phosphorylated
	reaction_r200=2*global_par_kkin68*x(193);

% Reaction: id = r201, name = DaL10UU has site Y1068/Y1086 phosphorylated
	reaction_r201=2*global_par_kkin68*x(194);

% Reaction: id = r202, name = DaL10CU has site Y1068/Y1086 phosphorylated
	reaction_r202=2*global_par_kkin68*x(195);

% Reaction: id = r203, name = DaL10LU has site Y1068/Y1086 phosphorylated
	reaction_r203=2*global_par_kkin68*x(196);

% Reaction: id = r204, name = DaL01UU has site Y1045 phosphorylated
	reaction_r204=global_par_kkin*x(197);

% Reaction: id = r205, name = DaL01UU has site Y1068/Y1086 phosphorylated
	reaction_r205=global_par_kkin68*x(197);

% Reaction: id = r206, name = DaL01UG has site Y1045 phosphorylated
	reaction_r206=global_par_kkin*x(198);

% Reaction: id = r207, name = DaL01UG has site Y1068/Y1086 phosphorylated
	reaction_r207=global_par_kkin68*x(198);

% Reaction: id = r208, name = DaL01UL has site Y1045 phosphorylated
	reaction_r208=global_par_kkin*x(199);

% Reaction: id = r209, name = DaL01UL has site Y1068/Y1086 phosphorylated
	reaction_r209=global_par_kkin68*x(199);

% Reaction: id = r210, name = DaL11UU has site Y1068/Y1086 phosphorylated
	reaction_r210=global_par_kkin68*x(200);

% Reaction: id = r211, name = DaL11CU has site Y1068/Y1086 phosphorylated
	reaction_r211=global_par_kkin68*x(201);

% Reaction: id = r212, name = DaL11LU has site Y1068/Y1086 phosphorylated
	reaction_r212=global_par_kkin68*x(202);

% Reaction: id = r213, name = DaL11UG has site Y1068/Y1086 phosphorylated
	reaction_r213=global_par_kkin68*x(203);

% Reaction: id = r214, name = DaL11UL has site Y1068/Y1086 phosphorylated
	reaction_r214=global_par_kkin68*x(204);

% Reaction: id = r215, name = DaL11CG has site Y1068/Y1086 phosphorylated
	reaction_r215=global_par_kkin68*x(205);

% Reaction: id = r216, name = DaL11CC has site Y1068/Y1086 phosphorylated
	reaction_r216=global_par_kkin68*x(206);

% Reaction: id = r217, name = DaL11LG has site Y1068/Y1086 phosphorylated
	reaction_r217=global_par_kkin68*x(207);

% Reaction: id = r218, name = DaL02UU has site Y1045 phosphorylated
	reaction_r218=global_par_kkin*x(208);

% Reaction: id = r219, name = DaL02UG has site Y1045 phosphorylated
	reaction_r219=global_par_kkin*x(209);

% Reaction: id = r220, name = DaL02UL has site Y1045 phosphorylated
	reaction_r220=global_par_kkin*x(210);

% Reaction: id = r221, name = Cbl and Grb2  bind yielding  CG
	reaction_r221=global_par_kbcg*x(2)*x(3);

% Reaction: id = r222, name = CG  dissociates to  Cbl and Grb2
	reaction_r222=global_par_kucg*x(4);

% Reaction: id = r223, name = Cbl and Rc10UU  bind yielding  Rc10CU
	reaction_r223=global_par_kb45*x(2)*x(12);

% Reaction: id = r224, name = Rc10CU  dissociates to  Cbl and Rc10UU
	reaction_r224=global_par_ku45*x(13);

% Reaction: id = r225, name = CG and Rc10UU  bind yielding  Rc10LU
	reaction_r225=global_par_kb45*x(4)*x(12);

% Reaction: id = r226, name = Rc10LU  dissociates to  CG and Rc10UU
	reaction_r226=global_par_ku45*x(14);

% Reaction: id = r227, name = Grb2 and Rc10CU  bind yielding  Rc10LU
	reaction_r227=global_par_kbcg*x(3)*x(13);

% Reaction: id = r228, name = Rc10LU  dissociates to  Grb2 and Rc10CU
	reaction_r228=global_par_kucg*x(14);

% Reaction: id = r229, name = Grb2 and Rc01UU  bind yielding  Rc01UG
	reaction_r229=global_par_kb68*x(3)*x(15);

% Reaction: id = r230, name = Rc01UG  dissociates to  Grb2 and Rc01UU
	reaction_r230=global_par_ku68*x(16);

% Reaction: id = r231, name = CG and Rc01UU  bind yielding  Rc01UL
	reaction_r231=global_par_kb68*x(4)*x(15);

% Reaction: id = r232, name = Rc01UL  dissociates to  CG and Rc01UU
	reaction_r232=global_par_ku68*x(17);

% Reaction: id = r233, name = Cbl and Rc01UG  bind yielding  Rc01UL
	reaction_r233=global_par_kbcg*x(2)*x(16);

% Reaction: id = r234, name = Rc01UL  dissociates to Cbl and Rc01UG
	reaction_r234=global_par_kucg*x(17);

% Reaction: id = r235, name = Cbl and Rc11UU  bind yielding  Rc11CU
	reaction_r235=global_par_kb45*x(2)*x(18);

% Reaction: id = r236, name = Rc11CU  dissociates to  Cbl and Rc11UU
	reaction_r236=global_par_ku45*x(19);

% Reaction: id = r237, name = CG and Rc11UU  bind yielding  Rc11LU
	reaction_r237=global_par_kb45*x(4)*x(18);

% Reaction: id = r238, name = Rc11LU  dissociates to  CG and Rc11UU
	reaction_r238=global_par_ku45*x(20);

% Reaction: id = r239, name = Grb2 and Rc11UU  bind yielding  Rc11UG
	reaction_r239=global_par_kb68*x(3)*x(18);

% Reaction: id = r240, name = Rc11UG  dissociates to  Grb2 and Rc11UU
	reaction_r240=global_par_ku68*x(21);

% Reaction: id = r241, name = CG and Rc11UU  bind yielding  Rc11UL
	reaction_r241=global_par_kb68*x(4)*x(18);

% Reaction: id = r242, name = Rc11UL  dissociates to  CG and Rc11UU
	reaction_r242=global_par_ku68*x(22);

% Reaction: id = r243, name = Grb2 and Rc11CU  bind yielding  Rc11LU
	reaction_r243=global_par_kbcg*x(3)*x(19);

% Reaction: id = r244, name = Rc11LU  dissociates to  Grb2 and Rc11CU
	reaction_r244=global_par_kucg*x(20);

% Reaction: id = r245, name = Grb2 and Rc11CU  bind yielding  Rc11CG
	reaction_r245=global_par_kb68*x(3)*x(19);

% Reaction: id = r246, name = Rc11CG  dissociates to  Grb2 and Rc11CU
	reaction_r246=global_par_ku68*x(23);

% Reaction: id = r247, name = Grb2 and Rc11LU  bind yielding  Rc11LG
	reaction_r247=global_par_kb68*x(3)*x(20);

% Reaction: id = r248, name = Rc11LG  dissociates to  Grb2 and Rc11LU
	reaction_r248=global_par_ku68*x(25);

% Reaction: id = r249, name = Rc11LU  transforms in (singly-bound -> doubly-bound)  Rc11CC
	reaction_r249=global_par_kb68P*x(20);

% Reaction: id = r250, name = Rc11CC  tranforms in (doubly-bound -> singly-bound)  Rc11LU
	reaction_r250=global_par_ku68M*x(24);

% Reaction: id = r251, name = Cbl and Rc11UG  bind yielding  Rc11CG
	reaction_r251=global_par_kb45*x(2)*x(21);

% Reaction: id = r252, name = Rc11CG  dissociates to  Cbl and Rc11UG
	reaction_r252=global_par_ku45*x(23);

% Reaction: id = r253, name = CG and Rc11UG  bind yielding  Rc11LG
	reaction_r253=global_par_kb45*x(4)*x(21);

% Reaction: id = r254, name = Rc11LG  dissociates to  CG and Rc11UG
	reaction_r254=global_par_ku45*x(25);

% Reaction: id = r255, name = Cbl and Rc11UG  bind yielding  Rc11UL
	reaction_r255=global_par_kbcg*x(2)*x(21);

% Reaction: id = r256, name = Rc11UL  dissociates to Cbl and Rc11UG
	reaction_r256=global_par_kucg*x(22);

% Reaction: id = r257, name = Rc11UL  transforms in (singly-bound -> doubly-bound)  Rc11CC
	reaction_r257=global_par_kb45P*x(22);

% Reaction: id = r258, name = Rc11CC  tranforms in (doubly-bound -> singly-bound)  Rc11UL
	reaction_r258=global_par_ku45M*x(24);

% Reaction: id = r259, name = Rc11CG  transforms in (Cbl bind Grb2 directly)  Rc11CC
	reaction_r259=global_par_kbcgP*x(23);

% Reaction: id = r260, name = Rc11CC  tranforms in (Cbl-Grb2 loose direct binding, but stay bound to EGFR)  Rc11CG
	reaction_r260=global_par_kucgM*x(24);

% Reaction: id = r261, name = Grb2 and Rc11CG  bind yielding  Rc11LG
	reaction_r261=global_par_kbcg*x(3)*x(23);

% Reaction: id = r262, name = Rc11LG  dissociates to  Grb2 and Rc11CG
	reaction_r262=global_par_kucg*x(25);

% Reaction: id = r263, name = Grb2 and Rc02UU  bind yielding  Rc02UG
	reaction_r263=2*global_par_kb68*x(3)*x(26);

% Reaction: id = r264, name = Rc02UG  dissociates to  Grb2 and Rc02UU
	reaction_r264=global_par_ku68*x(27);

% Reaction: id = r265, name = CG and Rc02UU  bind yielding  Rc02UL
	reaction_r265=2*global_par_kb68*x(4)*x(26);

% Reaction: id = r266, name = Rc02UL  dissociates to  CG and Rc02UU
	reaction_r266=global_par_ku68*x(28);

% Reaction: id = r267, name = Cbl and Rc02UG  bind yielding  Rc02UL
	reaction_r267=global_par_kbcg*x(2)*x(27);

% Reaction: id = r268, name = Rc02UL  dissociates to Cbl and Rc02UG
	reaction_r268=global_par_kucg*x(28);

% Reaction: id = r269, name = Cbl and Rc12UU  bind yielding  Rc12CU
	reaction_r269=global_par_kb45*x(2)*x(29);

% Reaction: id = r270, name = Rc12CU  dissociates to  Cbl and Rc12UU
	reaction_r270=global_par_ku45*x(30);

% Reaction: id = r271, name = CG and Rc12UU  bind yielding  Rc12LU
	reaction_r271=global_par_kb45*x(4)*x(29);

% Reaction: id = r272, name = Rc12LU  dissociates to  CG and Rc12UU
	reaction_r272=global_par_ku45*x(31);

% Reaction: id = r273, name = Grb2 and Rc12UU  bind yielding  Rc12UG
	reaction_r273=2*global_par_kb68*x(3)*x(29);

% Reaction: id = r274, name = Rc12UG  dissociates to  Grb2 and Rc12UU
	reaction_r274=global_par_ku68*x(32);

% Reaction: id = r275, name = CG and Rc12UU  bind yielding  Rc12UL
	reaction_r275=2*global_par_kb68*x(4)*x(29);

% Reaction: id = r276, name = Rc12UL  dissociates to  CG and Rc12UU
	reaction_r276=global_par_ku68*x(33);

% Reaction: id = r277, name = Grb2 and Rc12CU  bind yielding  Rc12LU
	reaction_r277=global_par_kbcg*x(3)*x(30);

% Reaction: id = r278, name = Rc12LU  dissociates to  Grb2 and Rc12CU
	reaction_r278=global_par_kucg*x(31);

% Reaction: id = r279, name = Grb2 and Rc12CU  bind yielding  Rc12CG
	reaction_r279=2*global_par_kb68*x(3)*x(30);

% Reaction: id = r280, name = Rc12CG  dissociates to  Grb2 and Rc12CU
	reaction_r280=global_par_ku68*x(34);

% Reaction: id = r281, name = Grb2 and Rc12LU  bind yielding  Rc12LG
	reaction_r281=2*global_par_kb68*x(3)*x(31);

% Reaction: id = r282, name = Rc12LG  dissociates to  Grb2 and Rc12LU
	reaction_r282=global_par_ku68*x(36);

% Reaction: id = r283, name = Rc12LU  transforms in (singly-bound -> doubly-bound)  Rc12CC
	reaction_r283=2*global_par_kb68P*x(31);

% Reaction: id = r284, name = Rc12CC  tranforms in (doubly-bound -> singly-bound)  Rc12LU
	reaction_r284=global_par_ku68M*x(35);

% Reaction: id = r285, name = Cbl and Rc12UG  bind yielding  Rc12CG
	reaction_r285=global_par_kb45*x(2)*x(32);

% Reaction: id = r286, name = Rc12CG  dissociates to  Cbl and Rc12UG
	reaction_r286=global_par_ku45*x(34);

% Reaction: id = r287, name = CG and Rc12UG  bind yielding  Rc12LG
	reaction_r287=global_par_kb45*x(4)*x(32);

% Reaction: id = r288, name = Rc12LG  dissociates to  CG and Rc12UG
	reaction_r288=global_par_ku45*x(36);

% Reaction: id = r289, name = Cbl and Rc12UG  bind yielding  Rc12UL
	reaction_r289=global_par_kbcg*x(2)*x(32);

% Reaction: id = r290, name = Rc12UL  dissociates to Cbl and Rc12UG
	reaction_r290=global_par_kucg*x(33);

% Reaction: id = r291, name = Rc12UL  transforms in (singly-bound -> doubly-bound)  Rc12CC
	reaction_r291=global_par_kb45P*x(33);

% Reaction: id = r292, name = Rc12CC  tranforms in (doubly-bound -> singly-bound)  Rc12UL
	reaction_r292=global_par_ku45M*x(35);

% Reaction: id = r293, name = Rc12CG  transforms in (Cbl bind Grb2 directly)  Rc12CC
	reaction_r293=global_par_kbcgP*x(34);

% Reaction: id = r294, name = Rc12CC  tranforms in (Cbl-Grb2 loose direct binding, but stay bound to EGFR)  Rc12CG
	reaction_r294=global_par_kucgM*x(35);

% Reaction: id = r295, name = Grb2 and Rc12CG  bind yielding  Rc12LG
	reaction_r295=global_par_kbcg*x(3)*x(34);

% Reaction: id = r296, name = Rc12LG  dissociates to  Grb2 and Rc12CG
	reaction_r296=global_par_kucg*x(36);

% Reaction: id = r297, name = Cbl and RcL10UU  bind yielding  RcL10CU
	reaction_r297=global_par_kb45*x(2)*x(38);

% Reaction: id = r298, name = RcL10CU  dissociates to  Cbl and RcL10UU
	reaction_r298=global_par_ku45*x(39);

% Reaction: id = r299, name = CG and RcL10UU  bind yielding  RcL10LU
	reaction_r299=global_par_kb45*x(4)*x(38);

% Reaction: id = r300, name = RcL10LU  dissociates to  CG and RcL10UU
	reaction_r300=global_par_ku45*x(40);

% Reaction: id = r301, name = Grb2 and RcL10CU  bind yielding  RcL10LU
	reaction_r301=global_par_kbcg*x(3)*x(39);

% Reaction: id = r302, name = RcL10LU  dissociates to  Grb2 and RcL10CU
	reaction_r302=global_par_kucg*x(40);

% Reaction: id = r303, name = Grb2 and RcL01UU  bind yielding  RcL01UG
	reaction_r303=global_par_kb68*x(3)*x(41);

% Reaction: id = r304, name = RcL01UG  dissociates to  Grb2 and RcL01UU
	reaction_r304=global_par_ku68*x(42);

% Reaction: id = r305, name = CG and RcL01UU  bind yielding  RcL01UL
	reaction_r305=global_par_kb68*x(4)*x(41);

% Reaction: id = r306, name = RcL01UL  dissociates to  CG and RcL01UU
	reaction_r306=global_par_ku68*x(43);

% Reaction: id = r307, name = Cbl and RcL01UG  bind yielding  RcL01UL
	reaction_r307=global_par_kbcg*x(2)*x(42);

% Reaction: id = r308, name = RcL01UL  dissociates to Cbl and RcL01UG
	reaction_r308=global_par_kucg*x(43);

% Reaction: id = r309, name = Cbl and RcL11UU  bind yielding  RcL11CU
	reaction_r309=global_par_kb45*x(2)*x(44);

% Reaction: id = r310, name = RcL11CU  dissociates to  Cbl and RcL11UU
	reaction_r310=global_par_ku45*x(45);

% Reaction: id = r311, name = CG and RcL11UU  bind yielding  RcL11LU
	reaction_r311=global_par_kb45*x(4)*x(44);

% Reaction: id = r312, name = RcL11LU  dissociates to  CG and RcL11UU
	reaction_r312=global_par_ku45*x(46);

% Reaction: id = r313, name = Grb2 and RcL11UU  bind yielding  RcL11UG
	reaction_r313=global_par_kb68*x(3)*x(44);

% Reaction: id = r314, name = RcL11UG  dissociates to  Grb2 and RcL11UU
	reaction_r314=global_par_ku68*x(47);

% Reaction: id = r315, name = CG and RcL11UU  bind yielding  RcL11UL
	reaction_r315=global_par_kb68*x(4)*x(44);

% Reaction: id = r316, name = RcL11UL  dissociates to  CG and RcL11UU
	reaction_r316=global_par_ku68*x(48);

% Reaction: id = r317, name = Grb2 and RcL11CU  bind yielding  RcL11LU
	reaction_r317=global_par_kbcg*x(3)*x(45);

% Reaction: id = r318, name = RcL11LU  dissociates to  Grb2 and RcL11CU
	reaction_r318=global_par_kucg*x(46);

% Reaction: id = r319, name = Grb2 and RcL11CU  bind yielding  RcL11CG
	reaction_r319=global_par_kb68*x(3)*x(45);

% Reaction: id = r320, name = RcL11CG  dissociates to  Grb2 and RcL11CU
	reaction_r320=global_par_ku68*x(49);

% Reaction: id = r321, name = Grb2 and RcL11LU  bind yielding  RcL11LG
	reaction_r321=global_par_kb68*x(3)*x(46);

% Reaction: id = r322, name = RcL11LG  dissociates to  Grb2 and RcL11LU
	reaction_r322=global_par_ku68*x(51);

% Reaction: id = r323, name = RcL11LU  transforms in (singly-bound -> doubly-bound)  RcL11CC
	reaction_r323=global_par_kb68P*x(46);

% Reaction: id = r324, name = RcL11CC  tranforms in (doubly-bound -> singly-bound)  RcL11LU
	reaction_r324=global_par_ku68M*x(50);

% Reaction: id = r325, name = Cbl and RcL11UG  bind yielding  RcL11CG
	reaction_r325=global_par_kb45*x(2)*x(47);

% Reaction: id = r326, name = RcL11CG  dissociates to  Cbl and RcL11UG
	reaction_r326=global_par_ku45*x(49);

% Reaction: id = r327, name = CG and RcL11UG  bind yielding  RcL11LG
	reaction_r327=global_par_kb45*x(4)*x(47);

% Reaction: id = r328, name = RcL11LG  dissociates to  CG and RcL11UG
	reaction_r328=global_par_ku45*x(51);

% Reaction: id = r329, name = Cbl and RcL11UG  bind yielding  RcL11UL
	reaction_r329=global_par_kbcg*x(2)*x(47);

% Reaction: id = r330, name = RcL11UL  dissociates to Cbl and RcL11UG
	reaction_r330=global_par_kucg*x(48);

% Reaction: id = r331, name = RcL11UL  transforms in (singly-bound -> doubly-bound)  RcL11CC
	reaction_r331=global_par_kb45P*x(48);

% Reaction: id = r332, name = RcL11CC  tranforms in (doubly-bound -> singly-bound)  RcL11UL
	reaction_r332=global_par_ku45M*x(50);

% Reaction: id = r333, name = RcL11CG  transforms in (Cbl bind Grb2 directly)  RcL11CC
	reaction_r333=global_par_kbcgP*x(49);

% Reaction: id = r334, name = RcL11CC  tranforms in (Cbl-Grb2 loose direct binding, but stay bound to EGFR)  RcL11CG
	reaction_r334=global_par_kucgM*x(50);

% Reaction: id = r335, name = Grb2 and RcL11CG  bind yielding  RcL11LG
	reaction_r335=global_par_kbcg*x(3)*x(49);

% Reaction: id = r336, name = RcL11LG  dissociates to  Grb2 and RcL11CG
	reaction_r336=global_par_kucg*x(51);

% Reaction: id = r337, name = Grb2 and RcL02UU  bind yielding  RcL02UG
	reaction_r337=2*global_par_kb68*x(3)*x(52);

% Reaction: id = r338, name = RcL02UG  dissociates to  Grb2 and RcL02UU
	reaction_r338=global_par_ku68*x(53);

% Reaction: id = r339, name = CG and RcL02UU  bind yielding  RcL02UL
	reaction_r339=2*global_par_kb68*x(4)*x(52);

% Reaction: id = r340, name = RcL02UL  dissociates to  CG and RcL02UU
	reaction_r340=global_par_ku68*x(54);

% Reaction: id = r341, name = Cbl and RcL02UG  bind yielding  RcL02UL
	reaction_r341=global_par_kbcg*x(2)*x(53);

% Reaction: id = r342, name = RcL02UL  dissociates to Cbl and RcL02UG
	reaction_r342=global_par_kucg*x(54);

% Reaction: id = r343, name = Cbl and RcL12UU  bind yielding  RcL12CU
	reaction_r343=global_par_kb45*x(2)*x(55);

% Reaction: id = r344, name = RcL12CU  dissociates to  Cbl and RcL12UU
	reaction_r344=global_par_ku45*x(56);

% Reaction: id = r345, name = CG and RcL12UU  bind yielding  RcL12LU
	reaction_r345=global_par_kb45*x(4)*x(55);

% Reaction: id = r346, name = RcL12LU  dissociates to  CG and RcL12UU
	reaction_r346=global_par_ku45*x(57);

% Reaction: id = r347, name = Grb2 and RcL12UU  bind yielding  RcL12UG
	reaction_r347=2*global_par_kb68*x(3)*x(55);

% Reaction: id = r348, name = RcL12UG  dissociates to  Grb2 and RcL12UU
	reaction_r348=global_par_ku68*x(58);

% Reaction: id = r349, name = CG and RcL12UU  bind yielding  RcL12UL
	reaction_r349=2*global_par_kb68*x(4)*x(55);

% Reaction: id = r350, name = RcL12UL  dissociates to  CG and RcL12UU
	reaction_r350=global_par_ku68*x(59);

% Reaction: id = r351, name = Grb2 and RcL12CU  bind yielding  RcL12LU
	reaction_r351=global_par_kbcg*x(3)*x(56);

% Reaction: id = r352, name = RcL12LU  dissociates to  Grb2 and RcL12CU
	reaction_r352=global_par_kucg*x(57);

% Reaction: id = r353, name = Grb2 and RcL12CU  bind yielding  RcL12CG
	reaction_r353=2*global_par_kb68*x(3)*x(56);

% Reaction: id = r354, name = RcL12CG  dissociates to  Grb2 and RcL12CU
	reaction_r354=global_par_ku68*x(60);

% Reaction: id = r355, name = Grb2 and RcL12LU  bind yielding  RcL12LG
	reaction_r355=2*global_par_kb68*x(3)*x(57);

% Reaction: id = r356, name = RcL12LG  dissociates to  Grb2 and RcL12LU
	reaction_r356=global_par_ku68*x(62);

% Reaction: id = r357, name = RcL12LU  transforms in (singly-bound -> doubly-bound)  RcL12CC
	reaction_r357=2*global_par_kb68P*x(57);

% Reaction: id = r358, name = RcL12CC  tranforms in (doubly-bound -> singly-bound)  RcL12LU
	reaction_r358=global_par_ku68M*x(61);

% Reaction: id = r359, name = Cbl and RcL12UG  bind yielding  RcL12CG
	reaction_r359=global_par_kb45*x(2)*x(58);

% Reaction: id = r360, name = RcL12CG  dissociates to  Cbl and RcL12UG
	reaction_r360=global_par_ku45*x(60);

% Reaction: id = r361, name = CG and RcL12UG  bind yielding  RcL12LG
	reaction_r361=global_par_kb45*x(4)*x(58);

% Reaction: id = r362, name = RcL12LG  dissociates to  CG and RcL12UG
	reaction_r362=global_par_ku45*x(62);

% Reaction: id = r363, name = Cbl and RcL12UG  bind yielding  RcL12UL
	reaction_r363=global_par_kbcg*x(2)*x(58);

% Reaction: id = r364, name = RcL12UL  dissociates to Cbl and RcL12UG
	reaction_r364=global_par_kucg*x(59);

% Reaction: id = r365, name = RcL12UL  transforms in (singly-bound -> doubly-bound)  RcL12CC
	reaction_r365=global_par_kb45P*x(59);

% Reaction: id = r366, name = RcL12CC  tranforms in (doubly-bound -> singly-bound)  RcL12UL
	reaction_r366=global_par_ku45M*x(61);

% Reaction: id = r367, name = RcL12CG  transforms in (Cbl bind Grb2 directly)  RcL12CC
	reaction_r367=global_par_kbcgP*x(60);

% Reaction: id = r368, name = RcL12CC  tranforms in (Cbl-Grb2 loose direct binding, but stay bound to EGFR)  RcL12CG
	reaction_r368=global_par_kucgM*x(61);

% Reaction: id = r369, name = Grb2 and RcL12CG  bind yielding  RcL12LG
	reaction_r369=global_par_kbcg*x(3)*x(60);

% Reaction: id = r370, name = RcL12LG  dissociates to  Grb2 and RcL12CG
	reaction_r370=global_par_kucg*x(62);

% Reaction: id = r371, name = Cbl and R10UU  bind yielding  R10CU
	reaction_r371=global_par_kb45*x(2)*x(64);

% Reaction: id = r372, name = R10CU  dissociates to  Cbl and R10UU
	reaction_r372=global_par_ku45*x(65);

% Reaction: id = r373, name = CG and R10UU  bind yielding  R10LU
	reaction_r373=global_par_kb45*x(4)*x(64);

% Reaction: id = r374, name = R10LU  dissociates to  CG and R10UU
	reaction_r374=global_par_ku45*x(66);

% Reaction: id = r375, name = Grb2 and R10CU  bind yielding  R10LU
	reaction_r375=global_par_kbcg*x(3)*x(65);

% Reaction: id = r376, name = R10LU  dissociates to  Grb2 and R10CU
	reaction_r376=global_par_kucg*x(66);

% Reaction: id = r377, name = Grb2 and R01UU  bind yielding  R01UG
	reaction_r377=global_par_kb68*x(3)*x(67);

% Reaction: id = r378, name = R01UG  dissociates to  Grb2 and R01UU
	reaction_r378=global_par_ku68*x(68);

% Reaction: id = r379, name = CG and R01UU  bind yielding  R01UL
	reaction_r379=global_par_kb68*x(4)*x(67);

% Reaction: id = r380, name = R01UL  dissociates to  CG and R01UU
	reaction_r380=global_par_ku68*x(69);

% Reaction: id = r381, name = Cbl and R01UG  bind yielding  R01UL
	reaction_r381=global_par_kbcg*x(2)*x(68);

% Reaction: id = r382, name = R01UL  dissociates to Cbl and R01UG
	reaction_r382=global_par_kucg*x(69);

% Reaction: id = r383, name = Cbl and R11UU  bind yielding  R11CU
	reaction_r383=global_par_kb45*x(2)*x(70);

% Reaction: id = r384, name = R11CU  dissociates to  Cbl and R11UU
	reaction_r384=global_par_ku45*x(71);

% Reaction: id = r385, name = CG and R11UU  bind yielding  R11LU
	reaction_r385=global_par_kb45*x(4)*x(70);

% Reaction: id = r386, name = R11LU  dissociates to  CG and R11UU
	reaction_r386=global_par_ku45*x(72);

% Reaction: id = r387, name = Grb2 and R11UU  bind yielding  R11UG
	reaction_r387=global_par_kb68*x(3)*x(70);

% Reaction: id = r388, name = R11UG  dissociates to  Grb2 and R11UU
	reaction_r388=global_par_ku68*x(73);

% Reaction: id = r389, name = CG and R11UU  bind yielding  R11UL
	reaction_r389=global_par_kb68*x(4)*x(70);

% Reaction: id = r390, name = R11UL  dissociates to  CG and R11UU
	reaction_r390=global_par_ku68*x(74);

% Reaction: id = r391, name = Grb2 and R11CU  bind yielding  R11LU
	reaction_r391=global_par_kbcg*x(3)*x(71);

% Reaction: id = r392, name = R11LU  dissociates to  Grb2 and R11CU
	reaction_r392=global_par_kucg*x(72);

% Reaction: id = r393, name = Grb2 and R11CU  bind yielding  R11CG
	reaction_r393=global_par_kb68*x(3)*x(71);

% Reaction: id = r394, name = R11CG  dissociates to  Grb2 and R11CU
	reaction_r394=global_par_ku68*x(75);

% Reaction: id = r395, name = Grb2 and R11LU  bind yielding  R11LG
	reaction_r395=global_par_kb68*x(3)*x(72);

% Reaction: id = r396, name = R11LG  dissociates to  Grb2 and R11LU
	reaction_r396=global_par_ku68*x(77);

% Reaction: id = r397, name = R11LU  transforms in (singly-bound -> doubly-bound)  R11CC
	reaction_r397=global_par_kb68P*x(72);

% Reaction: id = r398, name = R11CC  tranforms in (doubly-bound -> singly-bound)  R11LU
	reaction_r398=global_par_ku68M*x(76);

% Reaction: id = r399, name = Cbl and R11UG  bind yielding  R11CG
	reaction_r399=global_par_kb45*x(2)*x(73);

% Reaction: id = r400, name = R11CG  dissociates to  Cbl and R11UG
	reaction_r400=global_par_ku45*x(75);

% Reaction: id = r401, name = CG and R11UG  bind yielding  R11LG
	reaction_r401=global_par_kb45*x(4)*x(73);

% Reaction: id = r402, name = R11LG  dissociates to  CG and R11UG
	reaction_r402=global_par_ku45*x(77);

% Reaction: id = r403, name = Cbl and R11UG  bind yielding  R11UL
	reaction_r403=global_par_kbcg*x(2)*x(73);

% Reaction: id = r404, name = R11UL  dissociates to Cbl and R11UG
	reaction_r404=global_par_kucg*x(74);

% Reaction: id = r405, name = R11UL  transforms in (singly-bound -> doubly-bound)  R11CC
	reaction_r405=global_par_kb45P*x(74);

% Reaction: id = r406, name = R11CC  tranforms in (doubly-bound -> singly-bound)  R11UL
	reaction_r406=global_par_ku45M*x(76);

% Reaction: id = r407, name = R11CG  transforms in (Cbl bind Grb2 directly)  R11CC
	reaction_r407=global_par_kbcgP*x(75);

% Reaction: id = r408, name = R11CC  tranforms in (Cbl-Grb2 loose direct binding, but stay bound to EGFR)  R11CG
	reaction_r408=global_par_kucgM*x(76);

% Reaction: id = r409, name = Grb2 and R11CG  bind yielding  R11LG
	reaction_r409=global_par_kbcg*x(3)*x(75);

% Reaction: id = r410, name = R11LG  dissociates to  Grb2 and R11CG
	reaction_r410=global_par_kucg*x(77);

% Reaction: id = r411, name = Grb2 and R02UU  bind yielding  R02UG
	reaction_r411=2*global_par_kb68*x(3)*x(78);

% Reaction: id = r412, name = R02UG  dissociates to  Grb2 and R02UU
	reaction_r412=global_par_ku68*x(79);

% Reaction: id = r413, name = CG and R02UU  bind yielding  R02UL
	reaction_r413=2*global_par_kb68*x(4)*x(78);

% Reaction: id = r414, name = R02UL  dissociates to  CG and R02UU
	reaction_r414=global_par_ku68*x(80);

% Reaction: id = r415, name = Cbl and R02UG  bind yielding  R02UL
	reaction_r415=global_par_kbcg*x(2)*x(79);

% Reaction: id = r416, name = R02UL  dissociates to Cbl and R02UG
	reaction_r416=global_par_kucg*x(80);

% Reaction: id = r417, name = Cbl and R12UU  bind yielding  R12CU
	reaction_r417=global_par_kb45*x(2)*x(81);

% Reaction: id = r418, name = R12CU  dissociates to  Cbl and R12UU
	reaction_r418=global_par_ku45*x(82);

% Reaction: id = r419, name = CG and R12UU  bind yielding  R12LU
	reaction_r419=global_par_kb45*x(4)*x(81);

% Reaction: id = r420, name = R12LU  dissociates to  CG and R12UU
	reaction_r420=global_par_ku45*x(83);

% Reaction: id = r421, name = Grb2 and R12UU  bind yielding  R12UG
	reaction_r421=2*global_par_kb68*x(3)*x(81);

% Reaction: id = r422, name = R12UG  dissociates to  Grb2 and R12UU
	reaction_r422=global_par_ku68*x(84);

% Reaction: id = r423, name = CG and R12UU  bind yielding  R12UL
	reaction_r423=2*global_par_kb68*x(4)*x(81);

% Reaction: id = r424, name = R12UL  dissociates to  CG and R12UU
	reaction_r424=global_par_ku68*x(85);

% Reaction: id = r425, name = Grb2 and R12CU  bind yielding  R12LU
	reaction_r425=global_par_kbcg*x(3)*x(82);

% Reaction: id = r426, name = R12LU  dissociates to  Grb2 and R12CU
	reaction_r426=global_par_kucg*x(83);

% Reaction: id = r427, name = Grb2 and R12CU  bind yielding  R12CG
	reaction_r427=2*global_par_kb68*x(3)*x(82);

% Reaction: id = r428, name = R12CG  dissociates to  Grb2 and R12CU
	reaction_r428=global_par_ku68*x(86);

% Reaction: id = r429, name = Grb2 and R12LU  bind yielding  R12LG
	reaction_r429=2*global_par_kb68*x(3)*x(83);

% Reaction: id = r430, name = R12LG  dissociates to  Grb2 and R12LU
	reaction_r430=global_par_ku68*x(88);

% Reaction: id = r431, name = R12LU  transforms in (singly-bound -> doubly-bound)  R12CC
	reaction_r431=2*global_par_kb68P*x(83);

% Reaction: id = r432, name = R12CC  tranforms in (doubly-bound -> singly-bound)  R12LU
	reaction_r432=global_par_ku68M*x(87);

% Reaction: id = r433, name = Cbl and R12UG  bind yielding  R12CG
	reaction_r433=global_par_kb45*x(2)*x(84);

% Reaction: id = r434, name = R12CG  dissociates to  Cbl and R12UG
	reaction_r434=global_par_ku45*x(86);

% Reaction: id = r435, name = CG and R12UG  bind yielding  R12LG
	reaction_r435=global_par_kb45*x(4)*x(84);

% Reaction: id = r436, name = R12LG  dissociates to  CG and R12UG
	reaction_r436=global_par_ku45*x(88);

% Reaction: id = r437, name = Cbl and R12UG  bind yielding  R12UL
	reaction_r437=global_par_kbcg*x(2)*x(84);

% Reaction: id = r438, name = R12UL  dissociates to Cbl and R12UG
	reaction_r438=global_par_kucg*x(85);

% Reaction: id = r439, name = R12UL  transforms in (singly-bound -> doubly-bound)  R12CC
	reaction_r439=global_par_kb45P*x(85);

% Reaction: id = r440, name = R12CC  tranforms in (doubly-bound -> singly-bound)  R12UL
	reaction_r440=global_par_ku45M*x(87);

% Reaction: id = r441, name = R12CG  transforms in (Cbl bind Grb2 directly)  R12CC
	reaction_r441=global_par_kbcgP*x(86);

% Reaction: id = r442, name = R12CC  tranforms in (Cbl-Grb2 loose direct binding, but stay bound to EGFR)  R12CG
	reaction_r442=global_par_kucgM*x(87);

% Reaction: id = r443, name = Grb2 and R12CG  bind yielding  R12LG
	reaction_r443=global_par_kbcg*x(3)*x(86);

% Reaction: id = r444, name = R12LG  dissociates to  Grb2 and R12CG
	reaction_r444=global_par_kucg*x(88);

% Reaction: id = r445, name = Cbl and RL10UU  bind yielding  RL10CU
	reaction_r445=global_par_kb45*x(2)*x(90);

% Reaction: id = r446, name = RL10CU  dissociates to  Cbl and RL10UU
	reaction_r446=global_par_ku45*x(91);

% Reaction: id = r447, name = CG and RL10UU  bind yielding  RL10LU
	reaction_r447=global_par_kb45*x(4)*x(90);

% Reaction: id = r448, name = RL10LU  dissociates to  CG and RL10UU
	reaction_r448=global_par_ku45*x(92);

% Reaction: id = r449, name = Grb2 and RL10CU  bind yielding  RL10LU
	reaction_r449=global_par_kbcg*x(3)*x(91);

% Reaction: id = r450, name = RL10LU  dissociates to  Grb2 and RL10CU
	reaction_r450=global_par_kucg*x(92);

% Reaction: id = r451, name = Grb2 and RL01UU  bind yielding  RL01UG
	reaction_r451=global_par_kb68*x(3)*x(93);

% Reaction: id = r452, name = RL01UG  dissociates to  Grb2 and RL01UU
	reaction_r452=global_par_ku68*x(94);

% Reaction: id = r453, name = CG and RL01UU  bind yielding  RL01UL
	reaction_r453=global_par_kb68*x(4)*x(93);

% Reaction: id = r454, name = RL01UL  dissociates to  CG and RL01UU
	reaction_r454=global_par_ku68*x(95);

% Reaction: id = r455, name = Cbl and RL01UG  bind yielding  RL01UL
	reaction_r455=global_par_kbcg*x(2)*x(94);

% Reaction: id = r456, name = RL01UL  dissociates to Cbl and RL01UG
	reaction_r456=global_par_kucg*x(95);

% Reaction: id = r457, name = Cbl and RL11UU  bind yielding  RL11CU
	reaction_r457=global_par_kb45*x(2)*x(96);

% Reaction: id = r458, name = RL11CU  dissociates to  Cbl and RL11UU
	reaction_r458=global_par_ku45*x(97);

% Reaction: id = r459, name = CG and RL11UU  bind yielding  RL11LU
	reaction_r459=global_par_kb45*x(4)*x(96);

% Reaction: id = r460, name = RL11LU  dissociates to  CG and RL11UU
	reaction_r460=global_par_ku45*x(98);

% Reaction: id = r461, name = Grb2 and RL11UU  bind yielding  RL11UG
	reaction_r461=global_par_kb68*x(3)*x(96);

% Reaction: id = r462, name = RL11UG  dissociates to  Grb2 and RL11UU
	reaction_r462=global_par_ku68*x(99);

% Reaction: id = r463, name = CG and RL11UU  bind yielding  RL11UL
	reaction_r463=global_par_kb68*x(4)*x(96);

% Reaction: id = r464, name = RL11UL  dissociates to  CG and RL11UU
	reaction_r464=global_par_ku68*x(100);

% Reaction: id = r465, name = Grb2 and RL11CU  bind yielding  RL11LU
	reaction_r465=global_par_kbcg*x(3)*x(97);

% Reaction: id = r466, name = RL11LU  dissociates to  Grb2 and RL11CU
	reaction_r466=global_par_kucg*x(98);

% Reaction: id = r467, name = Grb2 and RL11CU  bind yielding  RL11CG
	reaction_r467=global_par_kb68*x(3)*x(97);

% Reaction: id = r468, name = RL11CG  dissociates to  Grb2 and RL11CU
	reaction_r468=global_par_ku68*x(101);

% Reaction: id = r469, name = Grb2 and RL11LU  bind yielding  RL11LG
	reaction_r469=global_par_kb68*x(3)*x(98);

% Reaction: id = r470, name = RL11LG  dissociates to  Grb2 and RL11LU
	reaction_r470=global_par_ku68*x(103);

% Reaction: id = r471, name = RL11LU  transforms in (singly-bound -> doubly-bound)  RL11CC
	reaction_r471=global_par_kb68P*x(98);

% Reaction: id = r472, name = RL11CC  tranforms in (doubly-bound -> singly-bound)  RL11LU
	reaction_r472=global_par_ku68M*x(102);

% Reaction: id = r473, name = Cbl and RL11UG  bind yielding  RL11CG
	reaction_r473=global_par_kb45*x(2)*x(99);

% Reaction: id = r474, name = RL11CG  dissociates to  Cbl and RL11UG
	reaction_r474=global_par_ku45*x(101);

% Reaction: id = r475, name = CG and RL11UG  bind yielding  RL11LG
	reaction_r475=global_par_kb45*x(4)*x(99);

% Reaction: id = r476, name = RL11LG  dissociates to  CG and RL11UG
	reaction_r476=global_par_ku45*x(103);

% Reaction: id = r477, name = Cbl and RL11UG  bind yielding  RL11UL
	reaction_r477=global_par_kbcg*x(2)*x(99);

% Reaction: id = r478, name = RL11UL  dissociates to Cbl and RL11UG
	reaction_r478=global_par_kucg*x(100);

% Reaction: id = r479, name = RL11UL  transforms in (singly-bound -> doubly-bound)  RL11CC
	reaction_r479=global_par_kb45P*x(100);

% Reaction: id = r480, name = RL11CC  tranforms in (doubly-bound -> singly-bound)  RL11UL
	reaction_r480=global_par_ku45M*x(102);

% Reaction: id = r481, name = RL11CG  transforms in (Cbl bind Grb2 directly)  RL11CC
	reaction_r481=global_par_kbcgP*x(101);

% Reaction: id = r482, name = RL11CC  tranforms in (Cbl-Grb2 loose direct binding, but stay bound to EGFR)  RL11CG
	reaction_r482=global_par_kucgM*x(102);

% Reaction: id = r483, name = Grb2 and RL11CG  bind yielding  RL11LG
	reaction_r483=global_par_kbcg*x(3)*x(101);

% Reaction: id = r484, name = RL11LG  dissociates to  Grb2 and RL11CG
	reaction_r484=global_par_kucg*x(103);

% Reaction: id = r485, name = Grb2 and RL02UU  bind yielding  RL02UG
	reaction_r485=2*global_par_kb68*x(3)*x(104);

% Reaction: id = r486, name = RL02UG  dissociates to  Grb2 and RL02UU
	reaction_r486=global_par_ku68*x(105);

% Reaction: id = r487, name = CG and RL02UU  bind yielding  RL02UL
	reaction_r487=2*global_par_kb68*x(4)*x(104);

% Reaction: id = r488, name = RL02UL  dissociates to  CG and RL02UU
	reaction_r488=global_par_ku68*x(106);

% Reaction: id = r489, name = Cbl and RL02UG  bind yielding  RL02UL
	reaction_r489=global_par_kbcg*x(2)*x(105);

% Reaction: id = r490, name = RL02UL  dissociates to Cbl and RL02UG
	reaction_r490=global_par_kucg*x(106);

% Reaction: id = r491, name = Cbl and RL12UU  bind yielding  RL12CU
	reaction_r491=global_par_kb45*x(2)*x(107);

% Reaction: id = r492, name = RL12CU  dissociates to  Cbl and RL12UU
	reaction_r492=global_par_ku45*x(108);

% Reaction: id = r493, name = CG and RL12UU  bind yielding  RL12LU
	reaction_r493=global_par_kb45*x(4)*x(107);

% Reaction: id = r494, name = RL12LU  dissociates to  CG and RL12UU
	reaction_r494=global_par_ku45*x(109);

% Reaction: id = r495, name = Grb2 and RL12UU  bind yielding  RL12UG
	reaction_r495=2*global_par_kb68*x(3)*x(107);

% Reaction: id = r496, name = RL12UG  dissociates to  Grb2 and RL12UU
	reaction_r496=global_par_ku68*x(110);

% Reaction: id = r497, name = CG and RL12UU  bind yielding  RL12UL
	reaction_r497=2*global_par_kb68*x(4)*x(107);

% Reaction: id = r498, name = RL12UL  dissociates to  CG and RL12UU
	reaction_r498=global_par_ku68*x(111);

% Reaction: id = r499, name = Grb2 and RL12CU  bind yielding  RL12LU
	reaction_r499=global_par_kbcg*x(3)*x(108);

% Reaction: id = r500, name = RL12LU  dissociates to  Grb2 and RL12CU
	reaction_r500=global_par_kucg*x(109);

% Reaction: id = r501, name = Grb2 and RL12CU  bind yielding  RL12CG
	reaction_r501=2*global_par_kb68*x(3)*x(108);

% Reaction: id = r502, name = RL12CG  dissociates to  Grb2 and RL12CU
	reaction_r502=global_par_ku68*x(112);

% Reaction: id = r503, name = Grb2 and RL12LU  bind yielding  RL12LG
	reaction_r503=2*global_par_kb68*x(3)*x(109);

% Reaction: id = r504, name = RL12LG  dissociates to  Grb2 and RL12LU
	reaction_r504=global_par_ku68*x(114);

% Reaction: id = r505, name = RL12LU  transforms in (singly-bound -> doubly-bound)  RL12CC
	reaction_r505=2*global_par_kb68P*x(109);

% Reaction: id = r506, name = RL12CC  tranforms in (doubly-bound -> singly-bound)  RL12LU
	reaction_r506=global_par_ku68M*x(113);

% Reaction: id = r507, name = Cbl and RL12UG  bind yielding  RL12CG
	reaction_r507=global_par_kb45*x(2)*x(110);

% Reaction: id = r508, name = RL12CG  dissociates to  Cbl and RL12UG
	reaction_r508=global_par_ku45*x(112);

% Reaction: id = r509, name = CG and RL12UG  bind yielding  RL12LG
	reaction_r509=global_par_kb45*x(4)*x(110);

% Reaction: id = r510, name = RL12LG  dissociates to  CG and RL12UG
	reaction_r510=global_par_ku45*x(114);

% Reaction: id = r511, name = Cbl and RL12UG  bind yielding  RL12UL
	reaction_r511=global_par_kbcg*x(2)*x(110);

% Reaction: id = r512, name = RL12UL  dissociates to Cbl and RL12UG
	reaction_r512=global_par_kucg*x(111);

% Reaction: id = r513, name = RL12UL  transforms in (singly-bound -> doubly-bound)  RL12CC
	reaction_r513=global_par_kb45P*x(111);

% Reaction: id = r514, name = RL12CC  tranforms in (doubly-bound -> singly-bound)  RL12UL
	reaction_r514=global_par_ku45M*x(113);

% Reaction: id = r515, name = RL12CG  transforms in (Cbl bind Grb2 directly)  RL12CC
	reaction_r515=global_par_kbcgP*x(112);

% Reaction: id = r516, name = RL12CC  tranforms in (Cbl-Grb2 loose direct binding, but stay bound to EGFR)  RL12CG
	reaction_r516=global_par_kucgM*x(113);

% Reaction: id = r517, name = Grb2 and RL12CG  bind yielding  RL12LG
	reaction_r517=global_par_kbcg*x(3)*x(112);

% Reaction: id = r518, name = RL12LG  dissociates to  Grb2 and RL12CG
	reaction_r518=global_par_kucg*x(114);

% Reaction: id = r519, name = Cbl and Di10UU  bind yielding  Di10CU
	reaction_r519=global_par_kb45*x(2)*x(116);

% Reaction: id = r520, name = Di10CU  dissociates to  Cbl and Di10UU
	reaction_r520=global_par_ku45*x(117);

% Reaction: id = r521, name = CG and Di10UU  bind yielding  Di10LU
	reaction_r521=global_par_kb45*x(4)*x(116);

% Reaction: id = r522, name = Di10LU  dissociates to  CG and Di10UU
	reaction_r522=global_par_ku45*x(118);

% Reaction: id = r523, name = Grb2 and Di10CU  bind yielding  Di10LU
	reaction_r523=global_par_kbcg*x(3)*x(117);

% Reaction: id = r524, name = Di10LU  dissociates to  Grb2 and Di10CU
	reaction_r524=global_par_kucg*x(118);

% Reaction: id = r525, name = Grb2 and Di01UU  bind yielding  Di01UG
	reaction_r525=global_par_kb68*x(3)*x(119);

% Reaction: id = r526, name = Di01UG  dissociates to  Grb2 and Di01UU
	reaction_r526=global_par_ku68*x(120);

% Reaction: id = r527, name = CG and Di01UU  bind yielding  Di01UL
	reaction_r527=global_par_kb68*x(4)*x(119);

% Reaction: id = r528, name = Di01UL  dissociates to  CG and Di01UU
	reaction_r528=global_par_ku68*x(121);

% Reaction: id = r529, name = Cbl and Di01UG  bind yielding  Di01UL
	reaction_r529=global_par_kbcg*x(2)*x(120);

% Reaction: id = r530, name = Di01UL  dissociates to Cbl and Di01UG
	reaction_r530=global_par_kucg*x(121);

% Reaction: id = r531, name = Cbl and Di11UU  bind yielding  Di11CU
	reaction_r531=global_par_kb45*x(2)*x(122);

% Reaction: id = r532, name = Di11CU  dissociates to  Cbl and Di11UU
	reaction_r532=global_par_ku45*x(123);

% Reaction: id = r533, name = CG and Di11UU  bind yielding  Di11LU
	reaction_r533=global_par_kb45*x(4)*x(122);

% Reaction: id = r534, name = Di11LU  dissociates to  CG and Di11UU
	reaction_r534=global_par_ku45*x(124);

% Reaction: id = r535, name = Grb2 and Di11UU  bind yielding  Di11UG
	reaction_r535=global_par_kb68*x(3)*x(122);

% Reaction: id = r536, name = Di11UG  dissociates to  Grb2 and Di11UU
	reaction_r536=global_par_ku68*x(125);

% Reaction: id = r537, name = CG and Di11UU  bind yielding  Di11UL
	reaction_r537=global_par_kb68*x(4)*x(122);

% Reaction: id = r538, name = Di11UL  dissociates to  CG and Di11UU
	reaction_r538=global_par_ku68*x(126);

% Reaction: id = r539, name = Grb2 and Di11CU  bind yielding  Di11LU
	reaction_r539=global_par_kbcg*x(3)*x(123);

% Reaction: id = r540, name = Di11LU  dissociates to  Grb2 and Di11CU
	reaction_r540=global_par_kucg*x(124);

% Reaction: id = r541, name = Grb2 and Di11CU  bind yielding  Di11CG
	reaction_r541=global_par_kb68*x(3)*x(123);

% Reaction: id = r542, name = Di11CG  dissociates to  Grb2 and Di11CU
	reaction_r542=global_par_ku68*x(127);

% Reaction: id = r543, name = Grb2 and Di11LU  bind yielding  Di11LG
	reaction_r543=global_par_kb68*x(3)*x(124);

% Reaction: id = r544, name = Di11LG  dissociates to  Grb2 and Di11LU
	reaction_r544=global_par_ku68*x(129);

% Reaction: id = r545, name = Di11LU  transforms in (singly-bound -> doubly-bound)  Di11CC
	reaction_r545=global_par_kb68P*x(124);

% Reaction: id = r546, name = Di11CC  tranforms in (doubly-bound -> singly-bound)  Di11LU
	reaction_r546=global_par_ku68M*x(128);

% Reaction: id = r547, name = Cbl and Di11UG  bind yielding  Di11CG
	reaction_r547=global_par_kb45*x(2)*x(125);

% Reaction: id = r548, name = Di11CG  dissociates to  Cbl and Di11UG
	reaction_r548=global_par_ku45*x(127);

% Reaction: id = r549, name = CG and Di11UG  bind yielding  Di11LG
	reaction_r549=global_par_kb45*x(4)*x(125);

% Reaction: id = r550, name = Di11LG  dissociates to  CG and Di11UG
	reaction_r550=global_par_ku45*x(129);

% Reaction: id = r551, name = Cbl and Di11UG  bind yielding  Di11UL
	reaction_r551=global_par_kbcg*x(2)*x(125);

% Reaction: id = r552, name = Di11UL  dissociates to Cbl and Di11UG
	reaction_r552=global_par_kucg*x(126);

% Reaction: id = r553, name = Di11UL  transforms in (singly-bound -> doubly-bound)  Di11CC
	reaction_r553=global_par_kb45P*x(126);

% Reaction: id = r554, name = Di11CC  tranforms in (doubly-bound -> singly-bound)  Di11UL
	reaction_r554=global_par_ku45M*x(128);

% Reaction: id = r555, name = Di11CG  transforms in (Cbl bind Grb2 directly)  Di11CC
	reaction_r555=global_par_kbcgP*x(127);

% Reaction: id = r556, name = Di11CC  tranforms in (Cbl-Grb2 loose direct binding, but stay bound to EGFR)  Di11CG
	reaction_r556=global_par_kucgM*x(128);

% Reaction: id = r557, name = Grb2 and Di11CG  bind yielding  Di11LG
	reaction_r557=global_par_kbcg*x(3)*x(127);

% Reaction: id = r558, name = Di11LG  dissociates to  Grb2 and Di11CG
	reaction_r558=global_par_kucg*x(129);

% Reaction: id = r559, name = Grb2 and Di02UU  bind yielding  Di02UG
	reaction_r559=2*global_par_kb68*x(3)*x(130);

% Reaction: id = r560, name = Di02UG  dissociates to  Grb2 and Di02UU
	reaction_r560=global_par_ku68*x(131);

% Reaction: id = r561, name = CG and Di02UU  bind yielding  Di02UL
	reaction_r561=2*global_par_kb68*x(4)*x(130);

% Reaction: id = r562, name = Di02UL  dissociates to  CG and Di02UU
	reaction_r562=global_par_ku68*x(132);

% Reaction: id = r563, name = Cbl and Di02UG  bind yielding  Di02UL
	reaction_r563=global_par_kbcg*x(2)*x(131);

% Reaction: id = r564, name = Di02UL  dissociates to Cbl and Di02UG
	reaction_r564=global_par_kucg*x(132);

% Reaction: id = r565, name = Cbl and Di12UU  bind yielding  Di12CU
	reaction_r565=global_par_kb45*x(2)*x(133);

% Reaction: id = r566, name = Di12CU  dissociates to  Cbl and Di12UU
	reaction_r566=global_par_ku45*x(134);

% Reaction: id = r567, name = CG and Di12UU  bind yielding  Di12LU
	reaction_r567=global_par_kb45*x(4)*x(133);

% Reaction: id = r568, name = Di12LU  dissociates to  CG and Di12UU
	reaction_r568=global_par_ku45*x(135);

% Reaction: id = r569, name = Grb2 and Di12UU  bind yielding  Di12UG
	reaction_r569=2*global_par_kb68*x(3)*x(133);

% Reaction: id = r570, name = Di12UG  dissociates to  Grb2 and Di12UU
	reaction_r570=global_par_ku68*x(136);

% Reaction: id = r571, name = CG and Di12UU  bind yielding  Di12UL
	reaction_r571=2*global_par_kb68*x(4)*x(133);

% Reaction: id = r572, name = Di12UL  dissociates to  CG and Di12UU
	reaction_r572=global_par_ku68*x(137);

% Reaction: id = r573, name = Grb2 and Di12CU  bind yielding  Di12LU
	reaction_r573=global_par_kbcg*x(3)*x(134);

% Reaction: id = r574, name = Di12LU  dissociates to  Grb2 and Di12CU
	reaction_r574=global_par_kucg*x(135);

% Reaction: id = r575, name = Grb2 and Di12CU  bind yielding  Di12CG
	reaction_r575=2*global_par_kb68*x(3)*x(134);

% Reaction: id = r576, name = Di12CG  dissociates to  Grb2 and Di12CU
	reaction_r576=global_par_ku68*x(138);

% Reaction: id = r577, name = Grb2 and Di12LU  bind yielding  Di12LG
	reaction_r577=2*global_par_kb68*x(3)*x(135);

% Reaction: id = r578, name = Di12LG  dissociates to  Grb2 and Di12LU
	reaction_r578=global_par_ku68*x(140);

% Reaction: id = r579, name = Di12LU  transforms in (singly-bound -> doubly-bound)  Di12CC
	reaction_r579=2*global_par_kb68P*x(135);

% Reaction: id = r580, name = Di12CC  tranforms in (doubly-bound -> singly-bound)  Di12LU
	reaction_r580=global_par_ku68M*x(139);

% Reaction: id = r581, name = Cbl and Di12UG  bind yielding  Di12CG
	reaction_r581=global_par_kb45*x(2)*x(136);

% Reaction: id = r582, name = Di12CG  dissociates to  Cbl and Di12UG
	reaction_r582=global_par_ku45*x(138);

% Reaction: id = r583, name = CG and Di12UG  bind yielding  Di12LG
	reaction_r583=global_par_kb45*x(4)*x(136);

% Reaction: id = r584, name = Di12LG  dissociates to  CG and Di12UG
	reaction_r584=global_par_ku45*x(140);

% Reaction: id = r585, name = Cbl and Di12UG  bind yielding  Di12UL
	reaction_r585=global_par_kbcg*x(2)*x(136);

% Reaction: id = r586, name = Di12UL  dissociates to Cbl and Di12UG
	reaction_r586=global_par_kucg*x(137);

% Reaction: id = r587, name = Di12UL  transforms in (singly-bound -> doubly-bound)  Di12CC
	reaction_r587=global_par_kb45P*x(137);

% Reaction: id = r588, name = Di12CC  tranforms in (doubly-bound -> singly-bound)  Di12UL
	reaction_r588=global_par_ku45M*x(139);

% Reaction: id = r589, name = Di12CG  transforms in (Cbl bind Grb2 directly)  Di12CC
	reaction_r589=global_par_kbcgP*x(138);

% Reaction: id = r590, name = Di12CC  tranforms in (Cbl-Grb2 loose direct binding, but stay bound to EGFR)  Di12CG
	reaction_r590=global_par_kucgM*x(139);

% Reaction: id = r591, name = Grb2 and Di12CG  bind yielding  Di12LG
	reaction_r591=global_par_kbcg*x(3)*x(138);

% Reaction: id = r592, name = Di12LG  dissociates to  Grb2 and Di12CG
	reaction_r592=global_par_kucg*x(140);

% Reaction: id = r593, name = Cbl and Da10UU  bind yielding  Da10CU
	reaction_r593=global_par_kb45*x(2)*x(142);

% Reaction: id = r594, name = Da10CU  dissociates to  Cbl and Da10UU
	reaction_r594=global_par_ku45*x(143);

% Reaction: id = r595, name = CG and Da10UU  bind yielding  Da10LU
	reaction_r595=global_par_kb45*x(4)*x(142);

% Reaction: id = r596, name = Da10LU  dissociates to  CG and Da10UU
	reaction_r596=global_par_ku45*x(144);

% Reaction: id = r597, name = Grb2 and Da10CU  bind yielding  Da10LU
	reaction_r597=global_par_kbcg*x(3)*x(143);

% Reaction: id = r598, name = Da10LU  dissociates to  Grb2 and Da10CU
	reaction_r598=global_par_kucg*x(144);

% Reaction: id = r599, name = Grb2 and Da01UU  bind yielding  Da01UG
	reaction_r599=global_par_kb68*x(3)*x(145);

% Reaction: id = r600, name = Da01UG  dissociates to  Grb2 and Da01UU
	reaction_r600=global_par_ku68*x(146);

% Reaction: id = r601, name = CG and Da01UU  bind yielding  Da01UL
	reaction_r601=global_par_kb68*x(4)*x(145);

% Reaction: id = r602, name = Da01UL  dissociates to  CG and Da01UU
	reaction_r602=global_par_ku68*x(147);

% Reaction: id = r603, name = Cbl and Da01UG  bind yielding  Da01UL
	reaction_r603=global_par_kbcg*x(2)*x(146);

% Reaction: id = r604, name = Da01UL  dissociates to Cbl and Da01UG
	reaction_r604=global_par_kucg*x(147);

% Reaction: id = r605, name = Cbl and Da11UU  bind yielding  Da11CU
	reaction_r605=global_par_kb45*x(2)*x(148);

% Reaction: id = r606, name = Da11CU  dissociates to  Cbl and Da11UU
	reaction_r606=global_par_ku45*x(149);

% Reaction: id = r607, name = CG and Da11UU  bind yielding  Da11LU
	reaction_r607=global_par_kb45*x(4)*x(148);

% Reaction: id = r608, name = Da11LU  dissociates to  CG and Da11UU
	reaction_r608=global_par_ku45*x(150);

% Reaction: id = r609, name = Grb2 and Da11UU  bind yielding  Da11UG
	reaction_r609=global_par_kb68*x(3)*x(148);

% Reaction: id = r610, name = Da11UG  dissociates to  Grb2 and Da11UU
	reaction_r610=global_par_ku68*x(151);

% Reaction: id = r611, name = CG and Da11UU  bind yielding  Da11UL
	reaction_r611=global_par_kb68*x(4)*x(148);

% Reaction: id = r612, name = Da11UL  dissociates to  CG and Da11UU
	reaction_r612=global_par_ku68*x(152);

% Reaction: id = r613, name = Grb2 and Da11CU  bind yielding  Da11LU
	reaction_r613=global_par_kbcg*x(3)*x(149);

% Reaction: id = r614, name = Da11LU  dissociates to  Grb2 and Da11CU
	reaction_r614=global_par_kucg*x(150);

% Reaction: id = r615, name = Grb2 and Da11CU  bind yielding  Da11CG
	reaction_r615=global_par_kb68*x(3)*x(149);

% Reaction: id = r616, name = Da11CG  dissociates to  Grb2 and Da11CU
	reaction_r616=global_par_ku68*x(153);

% Reaction: id = r617, name = Grb2 and Da11LU  bind yielding  Da11LG
	reaction_r617=global_par_kb68*x(3)*x(150);

% Reaction: id = r618, name = Da11LG  dissociates to  Grb2 and Da11LU
	reaction_r618=global_par_ku68*x(155);

% Reaction: id = r619, name = Da11LU  transforms in (singly-bound -> doubly-bound)  Da11CC
	reaction_r619=global_par_kb68P*x(150);

% Reaction: id = r620, name = Da11CC  tranforms in (doubly-bound -> singly-bound)  Da11LU
	reaction_r620=global_par_ku68M*x(154);

% Reaction: id = r621, name = Cbl and Da11UG  bind yielding  Da11CG
	reaction_r621=global_par_kb45*x(2)*x(151);

% Reaction: id = r622, name = Da11CG  dissociates to  Cbl and Da11UG
	reaction_r622=global_par_ku45*x(153);

% Reaction: id = r623, name = CG and Da11UG  bind yielding  Da11LG
	reaction_r623=global_par_kb45*x(4)*x(151);

% Reaction: id = r624, name = Da11LG  dissociates to  CG and Da11UG
	reaction_r624=global_par_ku45*x(155);

% Reaction: id = r625, name = Cbl and Da11UG  bind yielding  Da11UL
	reaction_r625=global_par_kbcg*x(2)*x(151);

% Reaction: id = r626, name = Da11UL  dissociates to Cbl and Da11UG
	reaction_r626=global_par_kucg*x(152);

% Reaction: id = r627, name = Da11UL  transforms in (singly-bound -> doubly-bound)  Da11CC
	reaction_r627=global_par_kb45P*x(152);

% Reaction: id = r628, name = Da11CC  tranforms in (doubly-bound -> singly-bound)  Da11UL
	reaction_r628=global_par_ku45M*x(154);

% Reaction: id = r629, name = Da11CG  transforms in (Cbl bind Grb2 directly)  Da11CC
	reaction_r629=global_par_kbcgP*x(153);

% Reaction: id = r630, name = Da11CC  tranforms in (Cbl-Grb2 loose direct binding, but stay bound to EGFR)  Da11CG
	reaction_r630=global_par_kucgM*x(154);

% Reaction: id = r631, name = Grb2 and Da11CG  bind yielding  Da11LG
	reaction_r631=global_par_kbcg*x(3)*x(153);

% Reaction: id = r632, name = Da11LG  dissociates to  Grb2 and Da11CG
	reaction_r632=global_par_kucg*x(155);

% Reaction: id = r633, name = Grb2 and Da02UU  bind yielding  Da02UG
	reaction_r633=2*global_par_kb68*x(3)*x(156);

% Reaction: id = r634, name = Da02UG  dissociates to  Grb2 and Da02UU
	reaction_r634=global_par_ku68*x(157);

% Reaction: id = r635, name = CG and Da02UU  bind yielding  Da02UL
	reaction_r635=2*global_par_kb68*x(4)*x(156);

% Reaction: id = r636, name = Da02UL  dissociates to  CG and Da02UU
	reaction_r636=global_par_ku68*x(158);

% Reaction: id = r637, name = Cbl and Da02UG  bind yielding  Da02UL
	reaction_r637=global_par_kbcg*x(2)*x(157);

% Reaction: id = r638, name = Da02UL  dissociates to Cbl and Da02UG
	reaction_r638=global_par_kucg*x(158);

% Reaction: id = r639, name = Cbl and Da12UU  bind yielding  Da12CU
	reaction_r639=global_par_kb45*x(2)*x(159);

% Reaction: id = r640, name = Da12CU  dissociates to  Cbl and Da12UU
	reaction_r640=global_par_ku45*x(160);

% Reaction: id = r641, name = CG and Da12UU  bind yielding  Da12LU
	reaction_r641=global_par_kb45*x(4)*x(159);

% Reaction: id = r642, name = Da12LU  dissociates to  CG and Da12UU
	reaction_r642=global_par_ku45*x(161);

% Reaction: id = r643, name = Grb2 and Da12UU  bind yielding  Da12UG
	reaction_r643=2*global_par_kb68*x(3)*x(159);

% Reaction: id = r644, name = Da12UG  dissociates to  Grb2 and Da12UU
	reaction_r644=global_par_ku68*x(162);

% Reaction: id = r645, name = CG and Da12UU  bind yielding  Da12UL
	reaction_r645=2*global_par_kb68*x(4)*x(159);

% Reaction: id = r646, name = Da12UL  dissociates to  CG and Da12UU
	reaction_r646=global_par_ku68*x(163);

% Reaction: id = r647, name = Grb2 and Da12CU  bind yielding  Da12LU
	reaction_r647=global_par_kbcg*x(3)*x(160);

% Reaction: id = r648, name = Da12LU  dissociates to  Grb2 and Da12CU
	reaction_r648=global_par_kucg*x(161);

% Reaction: id = r649, name = Grb2 and Da12CU  bind yielding  Da12CG
	reaction_r649=2*global_par_kb68*x(3)*x(160);

% Reaction: id = r650, name = Da12CG  dissociates to  Grb2 and Da12CU
	reaction_r650=global_par_ku68*x(164);

% Reaction: id = r651, name = Grb2 and Da12LU  bind yielding  Da12LG
	reaction_r651=2*global_par_kb68*x(3)*x(161);

% Reaction: id = r652, name = Da12LG  dissociates to  Grb2 and Da12LU
	reaction_r652=global_par_ku68*x(166);

% Reaction: id = r653, name = Da12LU  transforms in (singly-bound -> doubly-bound)  Da12CC
	reaction_r653=2*global_par_kb68P*x(161);

% Reaction: id = r654, name = Da12CC  tranforms in (doubly-bound -> singly-bound)  Da12LU
	reaction_r654=global_par_ku68M*x(165);

% Reaction: id = r655, name = Cbl and Da12UG  bind yielding  Da12CG
	reaction_r655=global_par_kb45*x(2)*x(162);

% Reaction: id = r656, name = Da12CG  dissociates to  Cbl and Da12UG
	reaction_r656=global_par_ku45*x(164);

% Reaction: id = r657, name = CG and Da12UG  bind yielding  Da12LG
	reaction_r657=global_par_kb45*x(4)*x(162);

% Reaction: id = r658, name = Da12LG  dissociates to  CG and Da12UG
	reaction_r658=global_par_ku45*x(166);

% Reaction: id = r659, name = Cbl and Da12UG  bind yielding  Da12UL
	reaction_r659=global_par_kbcg*x(2)*x(162);

% Reaction: id = r660, name = Da12UL  dissociates to Cbl and Da12UG
	reaction_r660=global_par_kucg*x(163);

% Reaction: id = r661, name = Da12UL  transforms in (singly-bound -> doubly-bound)  Da12CC
	reaction_r661=global_par_kb45P*x(163);

% Reaction: id = r662, name = Da12CC  tranforms in (doubly-bound -> singly-bound)  Da12UL
	reaction_r662=global_par_ku45M*x(165);

% Reaction: id = r663, name = Da12CG  transforms in (Cbl bind Grb2 directly)  Da12CC
	reaction_r663=global_par_kbcgP*x(164);

% Reaction: id = r664, name = Da12CC  tranforms in (Cbl-Grb2 loose direct binding, but stay bound to EGFR)  Da12CG
	reaction_r664=global_par_kucgM*x(165);

% Reaction: id = r665, name = Grb2 and Da12CG  bind yielding  Da12LG
	reaction_r665=global_par_kbcg*x(3)*x(164);

% Reaction: id = r666, name = Da12LG  dissociates to  Grb2 and Da12CG
	reaction_r666=global_par_kucg*x(166);

% Reaction: id = r667, name = Cbl and DiL10UU  bind yielding  DiL10CU
	reaction_r667=global_par_kb45*x(2)*x(168);

% Reaction: id = r668, name = DiL10CU  dissociates to  Cbl and DiL10UU
	reaction_r668=global_par_ku45*x(169);

% Reaction: id = r669, name = CG and DiL10UU  bind yielding  DiL10LU
	reaction_r669=global_par_kb45*x(4)*x(168);

% Reaction: id = r670, name = DiL10LU  dissociates to  CG and DiL10UU
	reaction_r670=global_par_ku45*x(170);

% Reaction: id = r671, name = Grb2 and DiL10CU  bind yielding  DiL10LU
	reaction_r671=global_par_kbcg*x(3)*x(169);

% Reaction: id = r672, name = DiL10LU  dissociates to  Grb2 and DiL10CU
	reaction_r672=global_par_kucg*x(170);

% Reaction: id = r673, name = Grb2 and DiL01UU  bind yielding  DiL01UG
	reaction_r673=global_par_kb68*x(3)*x(171);

% Reaction: id = r674, name = DiL01UG  dissociates to  Grb2 and DiL01UU
	reaction_r674=global_par_ku68*x(172);

% Reaction: id = r675, name = CG and DiL01UU  bind yielding  DiL01UL
	reaction_r675=global_par_kb68*x(4)*x(171);

% Reaction: id = r676, name = DiL01UL  dissociates to  CG and DiL01UU
	reaction_r676=global_par_ku68*x(173);

% Reaction: id = r677, name = Cbl and DiL01UG  bind yielding  DiL01UL
	reaction_r677=global_par_kbcg*x(2)*x(172);

% Reaction: id = r678, name = DiL01UL  dissociates to Cbl and DiL01UG
	reaction_r678=global_par_kucg*x(173);

% Reaction: id = r679, name = Cbl and DiL11UU  bind yielding  DiL11CU
	reaction_r679=global_par_kb45*x(2)*x(174);

% Reaction: id = r680, name = DiL11CU  dissociates to  Cbl and DiL11UU
	reaction_r680=global_par_ku45*x(175);

% Reaction: id = r681, name = CG and DiL11UU  bind yielding  DiL11LU
	reaction_r681=global_par_kb45*x(4)*x(174);

% Reaction: id = r682, name = DiL11LU  dissociates to  CG and DiL11UU
	reaction_r682=global_par_ku45*x(176);

% Reaction: id = r683, name = Grb2 and DiL11UU  bind yielding  DiL11UG
	reaction_r683=global_par_kb68*x(3)*x(174);

% Reaction: id = r684, name = DiL11UG  dissociates to  Grb2 and DiL11UU
	reaction_r684=global_par_ku68*x(177);

% Reaction: id = r685, name = CG and DiL11UU  bind yielding  DiL11UL
	reaction_r685=global_par_kb68*x(4)*x(174);

% Reaction: id = r686, name = DiL11UL  dissociates to  CG and DiL11UU
	reaction_r686=global_par_ku68*x(178);

% Reaction: id = r687, name = Grb2 and DiL11CU  bind yielding  DiL11LU
	reaction_r687=global_par_kbcg*x(3)*x(175);

% Reaction: id = r688, name = DiL11LU  dissociates to  Grb2 and DiL11CU
	reaction_r688=global_par_kucg*x(176);

% Reaction: id = r689, name = Grb2 and DiL11CU  bind yielding  DiL11CG
	reaction_r689=global_par_kb68*x(3)*x(175);

% Reaction: id = r690, name = DiL11CG  dissociates to  Grb2 and DiL11CU
	reaction_r690=global_par_ku68*x(179);

% Reaction: id = r691, name = Grb2 and DiL11LU  bind yielding  DiL11LG
	reaction_r691=global_par_kb68*x(3)*x(176);

% Reaction: id = r692, name = DiL11LG  dissociates to  Grb2 and DiL11LU
	reaction_r692=global_par_ku68*x(181);

% Reaction: id = r693, name = DiL11LU  transforms in (singly-bound -> doubly-bound)  DiL11CC
	reaction_r693=global_par_kb68P*x(176);

% Reaction: id = r694, name = DiL11CC  tranforms in (doubly-bound -> singly-bound)  DiL11LU
	reaction_r694=global_par_ku68M*x(180);

% Reaction: id = r695, name = Cbl and DiL11UG  bind yielding  DiL11CG
	reaction_r695=global_par_kb45*x(2)*x(177);

% Reaction: id = r696, name = DiL11CG  dissociates to  Cbl and DiL11UG
	reaction_r696=global_par_ku45*x(179);

% Reaction: id = r697, name = CG and DiL11UG  bind yielding  DiL11LG
	reaction_r697=global_par_kb45*x(4)*x(177);

% Reaction: id = r698, name = DiL11LG  dissociates to  CG and DiL11UG
	reaction_r698=global_par_ku45*x(181);

% Reaction: id = r699, name = Cbl and DiL11UG  bind yielding  DiL11UL
	reaction_r699=global_par_kbcg*x(2)*x(177);

% Reaction: id = r700, name = DiL11UL  dissociates to Cbl and DiL11UG
	reaction_r700=global_par_kucg*x(178);

% Reaction: id = r701, name = DiL11UL  transforms in (singly-bound -> doubly-bound)  DiL11CC
	reaction_r701=global_par_kb45P*x(178);

% Reaction: id = r702, name = DiL11CC  tranforms in (doubly-bound -> singly-bound)  DiL11UL
	reaction_r702=global_par_ku45M*x(180);

% Reaction: id = r703, name = DiL11CG  transforms in (Cbl bind Grb2 directly)  DiL11CC
	reaction_r703=global_par_kbcgP*x(179);

% Reaction: id = r704, name = DiL11CC  tranforms in (Cbl-Grb2 loose direct binding, but stay bound to EGFR)  DiL11CG
	reaction_r704=global_par_kucgM*x(180);

% Reaction: id = r705, name = Grb2 and DiL11CG  bind yielding  DiL11LG
	reaction_r705=global_par_kbcg*x(3)*x(179);

% Reaction: id = r706, name = DiL11LG  dissociates to  Grb2 and DiL11CG
	reaction_r706=global_par_kucg*x(181);

% Reaction: id = r707, name = Grb2 and DiL02UU  bind yielding  DiL02UG
	reaction_r707=2*global_par_kb68*x(3)*x(182);

% Reaction: id = r708, name = DiL02UG  dissociates to  Grb2 and DiL02UU
	reaction_r708=global_par_ku68*x(183);

% Reaction: id = r709, name = CG and DiL02UU  bind yielding  DiL02UL
	reaction_r709=2*global_par_kb68*x(4)*x(182);

% Reaction: id = r710, name = DiL02UL  dissociates to  CG and DiL02UU
	reaction_r710=global_par_ku68*x(184);

% Reaction: id = r711, name = Cbl and DiL02UG  bind yielding  DiL02UL
	reaction_r711=global_par_kbcg*x(2)*x(183);

% Reaction: id = r712, name = DiL02UL  dissociates to Cbl and DiL02UG
	reaction_r712=global_par_kucg*x(184);

% Reaction: id = r713, name = Cbl and DiL12UU  bind yielding  DiL12CU
	reaction_r713=global_par_kb45*x(2)*x(185);

% Reaction: id = r714, name = DiL12CU  dissociates to  Cbl and DiL12UU
	reaction_r714=global_par_ku45*x(186);

% Reaction: id = r715, name = CG and DiL12UU  bind yielding  DiL12LU
	reaction_r715=global_par_kb45*x(4)*x(185);

% Reaction: id = r716, name = DiL12LU  dissociates to  CG and DiL12UU
	reaction_r716=global_par_ku45*x(187);

% Reaction: id = r717, name = Grb2 and DiL12UU  bind yielding  DiL12UG
	reaction_r717=2*global_par_kb68*x(3)*x(185);

% Reaction: id = r718, name = DiL12UG  dissociates to  Grb2 and DiL12UU
	reaction_r718=global_par_ku68*x(188);

% Reaction: id = r719, name = CG and DiL12UU  bind yielding  DiL12UL
	reaction_r719=2*global_par_kb68*x(4)*x(185);

% Reaction: id = r720, name = DiL12UL  dissociates to  CG and DiL12UU
	reaction_r720=global_par_ku68*x(189);

% Reaction: id = r721, name = Grb2 and DiL12CU  bind yielding  DiL12LU
	reaction_r721=global_par_kbcg*x(3)*x(186);

% Reaction: id = r722, name = DiL12LU  dissociates to  Grb2 and DiL12CU
	reaction_r722=global_par_kucg*x(187);

% Reaction: id = r723, name = Grb2 and DiL12CU  bind yielding  DiL12CG
	reaction_r723=2*global_par_kb68*x(3)*x(186);

% Reaction: id = r724, name = DiL12CG  dissociates to  Grb2 and DiL12CU
	reaction_r724=global_par_ku68*x(190);

% Reaction: id = r725, name = Grb2 and DiL12LU  bind yielding  DiL12LG
	reaction_r725=2*global_par_kb68*x(3)*x(187);

% Reaction: id = r726, name = DiL12LG  dissociates to  Grb2 and DiL12LU
	reaction_r726=global_par_ku68*x(192);

% Reaction: id = r727, name = DiL12LU  transforms in (singly-bound -> doubly-bound)  DiL12CC
	reaction_r727=2*global_par_kb68P*x(187);

% Reaction: id = r728, name = DiL12CC  tranforms in (doubly-bound -> singly-bound)  DiL12LU
	reaction_r728=global_par_ku68M*x(191);

% Reaction: id = r729, name = Cbl and DiL12UG  bind yielding  DiL12CG
	reaction_r729=global_par_kb45*x(2)*x(188);

% Reaction: id = r730, name = DiL12CG  dissociates to  Cbl and DiL12UG
	reaction_r730=global_par_ku45*x(190);

% Reaction: id = r731, name = CG and DiL12UG  bind yielding  DiL12LG
	reaction_r731=global_par_kb45*x(4)*x(188);

% Reaction: id = r732, name = DiL12LG  dissociates to  CG and DiL12UG
	reaction_r732=global_par_ku45*x(192);

% Reaction: id = r733, name = Cbl and DiL12UG  bind yielding  DiL12UL
	reaction_r733=global_par_kbcg*x(2)*x(188);

% Reaction: id = r734, name = DiL12UL  dissociates to Cbl and DiL12UG
	reaction_r734=global_par_kucg*x(189);

% Reaction: id = r735, name = DiL12UL  transforms in (singly-bound -> doubly-bound)  DiL12CC
	reaction_r735=global_par_kb45P*x(189);

% Reaction: id = r736, name = DiL12CC  tranforms in (doubly-bound -> singly-bound)  DiL12UL
	reaction_r736=global_par_ku45M*x(191);

% Reaction: id = r737, name = DiL12CG  transforms in (Cbl bind Grb2 directly)  DiL12CC
	reaction_r737=global_par_kbcgP*x(190);

% Reaction: id = r738, name = DiL12CC  tranforms in (Cbl-Grb2 loose direct binding, but stay bound to EGFR)  DiL12CG
	reaction_r738=global_par_kucgM*x(191);

% Reaction: id = r739, name = Grb2 and DiL12CG  bind yielding  DiL12LG
	reaction_r739=global_par_kbcg*x(3)*x(190);

% Reaction: id = r740, name = DiL12LG  dissociates to  Grb2 and DiL12CG
	reaction_r740=global_par_kucg*x(192);

% Reaction: id = r741, name = Cbl and DaL10UU  bind yielding  DaL10CU
	reaction_r741=global_par_kb45*x(2)*x(194);

% Reaction: id = r742, name = DaL10CU  dissociates to  Cbl and DaL10UU
	reaction_r742=global_par_ku45*x(195);

% Reaction: id = r743, name = CG and DaL10UU  bind yielding  DaL10LU
	reaction_r743=global_par_kb45*x(4)*x(194);

% Reaction: id = r744, name = DaL10LU  dissociates to  CG and DaL10UU
	reaction_r744=global_par_ku45*x(196);

% Reaction: id = r745, name = Grb2 and DaL10CU  bind yielding  DaL10LU
	reaction_r745=global_par_kbcg*x(3)*x(195);

% Reaction: id = r746, name = DaL10LU  dissociates to  Grb2 and DaL10CU
	reaction_r746=global_par_kucg*x(196);

% Reaction: id = r747, name = Grb2 and DaL01UU  bind yielding  DaL01UG
	reaction_r747=global_par_kb68*x(3)*x(197);

% Reaction: id = r748, name = DaL01UG  dissociates to  Grb2 and DaL01UU
	reaction_r748=global_par_ku68*x(198);

% Reaction: id = r749, name = CG and DaL01UU  bind yielding  DaL01UL
	reaction_r749=global_par_kb68*x(4)*x(197);

% Reaction: id = r750, name = DaL01UL  dissociates to  CG and DaL01UU
	reaction_r750=global_par_ku68*x(199);

% Reaction: id = r751, name = Cbl and DaL01UG  bind yielding  DaL01UL
	reaction_r751=global_par_kbcg*x(2)*x(198);

% Reaction: id = r752, name = DaL01UL  dissociates to Cbl and DaL01UG
	reaction_r752=global_par_kucg*x(199);

% Reaction: id = r753, name = Cbl and DaL11UU  bind yielding  DaL11CU
	reaction_r753=global_par_kb45*x(2)*x(200);

% Reaction: id = r754, name = DaL11CU  dissociates to  Cbl and DaL11UU
	reaction_r754=global_par_ku45*x(201);

% Reaction: id = r755, name = CG and DaL11UU  bind yielding  DaL11LU
	reaction_r755=global_par_kb45*x(4)*x(200);

% Reaction: id = r756, name = DaL11LU  dissociates to  CG and DaL11UU
	reaction_r756=global_par_ku45*x(202);

% Reaction: id = r757, name = Grb2 and DaL11UU  bind yielding  DaL11UG
	reaction_r757=global_par_kb68*x(3)*x(200);

% Reaction: id = r758, name = DaL11UG  dissociates to  Grb2 and DaL11UU
	reaction_r758=global_par_ku68*x(203);

% Reaction: id = r759, name = CG and DaL11UU  bind yielding  DaL11UL
	reaction_r759=global_par_kb68*x(4)*x(200);

% Reaction: id = r760, name = DaL11UL  dissociates to  CG and DaL11UU
	reaction_r760=global_par_ku68*x(204);

% Reaction: id = r761, name = Grb2 and DaL11CU  bind yielding  DaL11LU
	reaction_r761=global_par_kbcg*x(3)*x(201);

% Reaction: id = r762, name = DaL11LU  dissociates to  Grb2 and DaL11CU
	reaction_r762=global_par_kucg*x(202);

% Reaction: id = r763, name = Grb2 and DaL11CU  bind yielding  DaL11CG
	reaction_r763=global_par_kb68*x(3)*x(201);

% Reaction: id = r764, name = DaL11CG  dissociates to  Grb2 and DaL11CU
	reaction_r764=global_par_ku68*x(205);

% Reaction: id = r765, name = Grb2 and DaL11LU  bind yielding  DaL11LG
	reaction_r765=global_par_kb68*x(3)*x(202);

% Reaction: id = r766, name = DaL11LG  dissociates to  Grb2 and DaL11LU
	reaction_r766=global_par_ku68*x(207);

% Reaction: id = r767, name = DaL11LU  transforms in (singly-bound -> doubly-bound)  DaL11CC
	reaction_r767=global_par_kb68P*x(202);

% Reaction: id = r768, name = DaL11CC  tranforms in (doubly-bound -> singly-bound)  DaL11LU
	reaction_r768=global_par_ku68M*x(206);

% Reaction: id = r769, name = Cbl and DaL11UG  bind yielding  DaL11CG
	reaction_r769=global_par_kb45*x(2)*x(203);

% Reaction: id = r770, name = DaL11CG  dissociates to  Cbl and DaL11UG
	reaction_r770=global_par_ku45*x(205);

% Reaction: id = r771, name = CG and DaL11UG  bind yielding  DaL11LG
	reaction_r771=global_par_kb45*x(4)*x(203);

% Reaction: id = r772, name = DaL11LG  dissociates to  CG and DaL11UG
	reaction_r772=global_par_ku45*x(207);

% Reaction: id = r773, name = Cbl and DaL11UG  bind yielding  DaL11UL
	reaction_r773=global_par_kbcg*x(2)*x(203);

% Reaction: id = r774, name = DaL11UL  dissociates to Cbl and DaL11UG
	reaction_r774=global_par_kucg*x(204);

% Reaction: id = r775, name = DaL11UL  transforms in (singly-bound -> doubly-bound)  DaL11CC
	reaction_r775=global_par_kb45P*x(204);

% Reaction: id = r776, name = DaL11CC  tranforms in (doubly-bound -> singly-bound)  DaL11UL
	reaction_r776=global_par_ku45M*x(206);

% Reaction: id = r777, name = DaL11CG  transforms in (Cbl bind Grb2 directly)  DaL11CC
	reaction_r777=global_par_kbcgP*x(205);

% Reaction: id = r778, name = DaL11CC  tranforms in (Cbl-Grb2 loose direct binding, but stay bound to EGFR)  DaL11CG
	reaction_r778=global_par_kucgM*x(206);

% Reaction: id = r779, name = Grb2 and DaL11CG  bind yielding  DaL11LG
	reaction_r779=global_par_kbcg*x(3)*x(205);

% Reaction: id = r780, name = DaL11LG  dissociates to  Grb2 and DaL11CG
	reaction_r780=global_par_kucg*x(207);

% Reaction: id = r781, name = Grb2 and DaL02UU  bind yielding  DaL02UG
	reaction_r781=2*global_par_kb68*x(3)*x(208);

% Reaction: id = r782, name = DaL02UG  dissociates to  Grb2 and DaL02UU
	reaction_r782=global_par_ku68*x(209);

% Reaction: id = r783, name = CG and DaL02UU  bind yielding  DaL02UL
	reaction_r783=2*global_par_kb68*x(4)*x(208);

% Reaction: id = r784, name = DaL02UL  dissociates to  CG and DaL02UU
	reaction_r784=global_par_ku68*x(210);

% Reaction: id = r785, name = Cbl and DaL02UG  bind yielding  DaL02UL
	reaction_r785=global_par_kbcg*x(2)*x(209);

% Reaction: id = r786, name = DaL02UL  dissociates to Cbl and DaL02UG
	reaction_r786=global_par_kucg*x(210);

% Reaction: id = r787, name = Cbl and DaL12UU  bind yielding  DaL12CU
	reaction_r787=global_par_kb45*x(2)*x(211);

% Reaction: id = r788, name = DaL12CU  dissociates to  Cbl and DaL12UU
	reaction_r788=global_par_ku45*x(212);

% Reaction: id = r789, name = CG and DaL12UU  bind yielding  DaL12LU
	reaction_r789=global_par_kb45*x(4)*x(211);

% Reaction: id = r790, name = DaL12LU  dissociates to  CG and DaL12UU
	reaction_r790=global_par_ku45*x(213);

% Reaction: id = r791, name = Grb2 and DaL12UU  bind yielding  DaL12UG
	reaction_r791=2*global_par_kb68*x(3)*x(211);

% Reaction: id = r792, name = DaL12UG  dissociates to  Grb2 and DaL12UU
	reaction_r792=global_par_ku68*x(214);

% Reaction: id = r793, name = CG and DaL12UU  bind yielding  DaL12UL
	reaction_r793=2*global_par_kb68*x(4)*x(211);

% Reaction: id = r794, name = DaL12UL  dissociates to  CG and DaL12UU
	reaction_r794=global_par_ku68*x(215);

% Reaction: id = r795, name = Grb2 and DaL12CU  bind yielding  DaL12LU
	reaction_r795=global_par_kbcg*x(3)*x(212);

% Reaction: id = r796, name = DaL12LU  dissociates to  Grb2 and DaL12CU
	reaction_r796=global_par_kucg*x(213);

% Reaction: id = r797, name = Grb2 and DaL12CU  bind yielding  DaL12CG
	reaction_r797=2*global_par_kb68*x(3)*x(212);

% Reaction: id = r798, name = DaL12CG  dissociates to  Grb2 and DaL12CU
	reaction_r798=global_par_ku68*x(216);

% Reaction: id = r799, name = Grb2 and DaL12LU  bind yielding  DaL12LG
	reaction_r799=2*global_par_kb68*x(3)*x(213);

% Reaction: id = r800, name = DaL12LG  dissociates to  Grb2 and DaL12LU
	reaction_r800=global_par_ku68*x(218);

% Reaction: id = r801, name = DaL12LU  transforms in (singly-bound -> doubly-bound)  DaL12CC
	reaction_r801=2*global_par_kb68P*x(213);

% Reaction: id = r802, name = DaL12CC  tranforms in (doubly-bound -> singly-bound)  DaL12LU
	reaction_r802=global_par_ku68M*x(217);

% Reaction: id = r803, name = Cbl and DaL12UG  bind yielding  DaL12CG
	reaction_r803=global_par_kb45*x(2)*x(214);

% Reaction: id = r804, name = DaL12CG  dissociates to  Cbl and DaL12UG
	reaction_r804=global_par_ku45*x(216);

% Reaction: id = r805, name = CG and DaL12UG  bind yielding  DaL12LG
	reaction_r805=global_par_kb45*x(4)*x(214);

% Reaction: id = r806, name = DaL12LG  dissociates to  CG and DaL12UG
	reaction_r806=global_par_ku45*x(218);

% Reaction: id = r807, name = Cbl and DaL12UG  bind yielding  DaL12UL
	reaction_r807=global_par_kbcg*x(2)*x(214);

% Reaction: id = r808, name = DaL12UL  dissociates to Cbl and DaL12UG
	reaction_r808=global_par_kucg*x(215);

% Reaction: id = r809, name = DaL12UL  transforms in (singly-bound -> doubly-bound)  DaL12CC
	reaction_r809=global_par_kb45P*x(215);

% Reaction: id = r810, name = DaL12CC  tranforms in (doubly-bound -> singly-bound)  DaL12UL
	reaction_r810=global_par_ku45M*x(217);

% Reaction: id = r811, name = DaL12CG  transforms in (Cbl bind Grb2 directly)  DaL12CC
	reaction_r811=global_par_kbcgP*x(216);

% Reaction: id = r812, name = DaL12CC  tranforms in (Cbl-Grb2 loose direct binding, but stay bound to EGFR)  DaL12CG
	reaction_r812=global_par_kucgM*x(217);

% Reaction: id = r813, name = Grb2 and DaL12CG  bind yielding  DaL12LG
	reaction_r813=global_par_kbcg*x(3)*x(216);

% Reaction: id = r814, name = DaL12LG  dissociates to  Grb2 and DaL12CG
	reaction_r814=global_par_kucg*x(218);

% Reaction: id = r815, name = R00UU closes
	reaction_r815=global_par_kc*x(63);

% Reaction: id = r816, name = R10UU closes
	reaction_r816=global_par_kc*x(64);

% Reaction: id = r817, name = R10CU closes
	reaction_r817=global_par_kc*x(65);

% Reaction: id = r818, name = R10LU closes
	reaction_r818=global_par_kc*x(66);

% Reaction: id = r819, name = R01UU closes
	reaction_r819=global_par_kc*x(67);

% Reaction: id = r820, name = R01UG closes
	reaction_r820=global_par_kc*x(68);

% Reaction: id = r821, name = R01UL closes
	reaction_r821=global_par_kc*x(69);

% Reaction: id = r822, name = R11UU closes
	reaction_r822=global_par_kc*x(70);

% Reaction: id = r823, name = R11CU closes
	reaction_r823=global_par_kc*x(71);

% Reaction: id = r824, name = R11LU closes
	reaction_r824=global_par_kc*x(72);

% Reaction: id = r825, name = R11UG closes
	reaction_r825=global_par_kc*x(73);

% Reaction: id = r826, name = R11UL closes
	reaction_r826=global_par_kc*x(74);

% Reaction: id = r827, name = R11CG closes
	reaction_r827=global_par_kc*x(75);

% Reaction: id = r828, name = R11CC closes
	reaction_r828=global_par_kc*x(76);

% Reaction: id = r829, name = R11LG closes
	reaction_r829=global_par_kc*x(77);

% Reaction: id = r830, name = R02UU closes
	reaction_r830=global_par_kc*x(78);

% Reaction: id = r831, name = R02UG closes
	reaction_r831=global_par_kc*x(79);

% Reaction: id = r832, name = R02UL closes
	reaction_r832=global_par_kc*x(80);

% Reaction: id = r833, name = R12UU closes
	reaction_r833=global_par_kc*x(81);

% Reaction: id = r834, name = R12CU closes
	reaction_r834=global_par_kc*x(82);

% Reaction: id = r835, name = R12LU closes
	reaction_r835=global_par_kc*x(83);

% Reaction: id = r836, name = R12UG closes
	reaction_r836=global_par_kc*x(84);

% Reaction: id = r837, name = R12UL closes
	reaction_r837=global_par_kc*x(85);

% Reaction: id = r838, name = R12CG closes
	reaction_r838=global_par_kc*x(86);

% Reaction: id = r839, name = R12CC closes
	reaction_r839=global_par_kc*x(87);

% Reaction: id = r840, name = R12LG closes
	reaction_r840=global_par_kc*x(88);

% Reaction: id = r841, name = Rc00UU opens
	reaction_r841=global_par_ko*x(11);

% Reaction: id = r842, name = Rc10UU opens
	reaction_r842=global_par_ko*x(12);

% Reaction: id = r843, name = Rc10CU opens
	reaction_r843=global_par_ko*x(13);

% Reaction: id = r844, name = Rc10LU opens
	reaction_r844=global_par_ko*x(14);

% Reaction: id = r845, name = Rc01UU opens
	reaction_r845=global_par_ko*x(15);

% Reaction: id = r846, name = Rc01UG opens
	reaction_r846=global_par_ko*x(16);

% Reaction: id = r847, name = Rc01UL opens
	reaction_r847=global_par_ko*x(17);

% Reaction: id = r848, name = Rc11UU opens
	reaction_r848=global_par_ko*x(18);

% Reaction: id = r849, name = Rc11CU opens
	reaction_r849=global_par_ko*x(19);

% Reaction: id = r850, name = Rc11LU opens
	reaction_r850=global_par_ko*x(20);

% Reaction: id = r851, name = Rc11UG opens
	reaction_r851=global_par_ko*x(21);

% Reaction: id = r852, name = Rc11UL opens
	reaction_r852=global_par_ko*x(22);

% Reaction: id = r853, name = Rc11CG opens
	reaction_r853=global_par_ko*x(23);

% Reaction: id = r854, name = Rc11CC opens
	reaction_r854=global_par_ko*x(24);

% Reaction: id = r855, name = Rc11LG opens
	reaction_r855=global_par_ko*x(25);

% Reaction: id = r856, name = Rc02UU opens
	reaction_r856=global_par_ko*x(26);

% Reaction: id = r857, name = Rc02UG opens
	reaction_r857=global_par_ko*x(27);

% Reaction: id = r858, name = Rc02UL opens
	reaction_r858=global_par_ko*x(28);

% Reaction: id = r859, name = Rc12UU opens
	reaction_r859=global_par_ko*x(29);

% Reaction: id = r860, name = Rc12CU opens
	reaction_r860=global_par_ko*x(30);

% Reaction: id = r861, name = Rc12LU opens
	reaction_r861=global_par_ko*x(31);

% Reaction: id = r862, name = Rc12UG opens
	reaction_r862=global_par_ko*x(32);

% Reaction: id = r863, name = Rc12UL opens
	reaction_r863=global_par_ko*x(33);

% Reaction: id = r864, name = Rc12CG opens
	reaction_r864=global_par_ko*x(34);

% Reaction: id = r865, name = Rc12CC opens
	reaction_r865=global_par_ko*x(35);

% Reaction: id = r866, name = Rc12LG opens
	reaction_r866=global_par_ko*x(36);

% Reaction: id = r867, name = RL00UU closes
	reaction_r867=global_par_k1c*x(89);

% Reaction: id = r868, name = RL10UU closes
	reaction_r868=global_par_k1c*x(90);

% Reaction: id = r869, name = RL10CU closes
	reaction_r869=global_par_k1c*x(91);

% Reaction: id = r870, name = RL10LU closes
	reaction_r870=global_par_k1c*x(92);

% Reaction: id = r871, name = RL01UU closes
	reaction_r871=global_par_k1c*x(93);

% Reaction: id = r872, name = RL01UG closes
	reaction_r872=global_par_k1c*x(94);

% Reaction: id = r873, name = RL01UL closes
	reaction_r873=global_par_k1c*x(95);

% Reaction: id = r874, name = RL11UU closes
	reaction_r874=global_par_k1c*x(96);

% Reaction: id = r875, name = RL11CU closes
	reaction_r875=global_par_k1c*x(97);

% Reaction: id = r876, name = RL11LU closes
	reaction_r876=global_par_k1c*x(98);

% Reaction: id = r877, name = RL11UG closes
	reaction_r877=global_par_k1c*x(99);

% Reaction: id = r878, name = RL11UL closes
	reaction_r878=global_par_k1c*x(100);

% Reaction: id = r879, name = RL11CG closes
	reaction_r879=global_par_k1c*x(101);

% Reaction: id = r880, name = RL11CC closes
	reaction_r880=global_par_k1c*x(102);

% Reaction: id = r881, name = RL11LG closes
	reaction_r881=global_par_k1c*x(103);

% Reaction: id = r882, name = RL02UU closes
	reaction_r882=global_par_k1c*x(104);

% Reaction: id = r883, name = RL02UG closes
	reaction_r883=global_par_k1c*x(105);

% Reaction: id = r884, name = RL02UL closes
	reaction_r884=global_par_k1c*x(106);

% Reaction: id = r885, name = RL12UU closes
	reaction_r885=global_par_k1c*x(107);

% Reaction: id = r886, name = RL12CU closes
	reaction_r886=global_par_k1c*x(108);

% Reaction: id = r887, name = RL12LU closes
	reaction_r887=global_par_k1c*x(109);

% Reaction: id = r888, name = RL12UG closes
	reaction_r888=global_par_k1c*x(110);

% Reaction: id = r889, name = RL12UL closes
	reaction_r889=global_par_k1c*x(111);

% Reaction: id = r890, name = RL12CG closes
	reaction_r890=global_par_k1c*x(112);

% Reaction: id = r891, name = RL12CC closes
	reaction_r891=global_par_k1c*x(113);

% Reaction: id = r892, name = RL12LG closes
	reaction_r892=global_par_k1c*x(114);

% Reaction: id = r893, name = RcL00UU opens
	reaction_r893=global_par_k1o*x(37);

% Reaction: id = r894, name = RcL10UU opens
	reaction_r894=global_par_k1o*x(38);

% Reaction: id = r895, name = RcL10CU opens
	reaction_r895=global_par_k1o*x(39);

% Reaction: id = r896, name = RcL10LU opens
	reaction_r896=global_par_k1o*x(40);

% Reaction: id = r897, name = RcL01UU opens
	reaction_r897=global_par_k1o*x(41);

% Reaction: id = r898, name = RcL01UG opens
	reaction_r898=global_par_k1o*x(42);

% Reaction: id = r899, name = RcL01UL opens
	reaction_r899=global_par_k1o*x(43);

% Reaction: id = r900, name = RcL11UU opens
	reaction_r900=global_par_k1o*x(44);

% Reaction: id = r901, name = RcL11CU opens
	reaction_r901=global_par_k1o*x(45);

% Reaction: id = r902, name = RcL11LU opens
	reaction_r902=global_par_k1o*x(46);

% Reaction: id = r903, name = RcL11UG opens
	reaction_r903=global_par_k1o*x(47);

% Reaction: id = r904, name = RcL11UL opens
	reaction_r904=global_par_k1o*x(48);

% Reaction: id = r905, name = RcL11CG opens
	reaction_r905=global_par_k1o*x(49);

% Reaction: id = r906, name = RcL11CC opens
	reaction_r906=global_par_k1o*x(50);

% Reaction: id = r907, name = RcL11LG opens
	reaction_r907=global_par_k1o*x(51);

% Reaction: id = r908, name = RcL02UU opens
	reaction_r908=global_par_k1o*x(52);

% Reaction: id = r909, name = RcL02UG opens
	reaction_r909=global_par_k1o*x(53);

% Reaction: id = r910, name = RcL02UL opens
	reaction_r910=global_par_k1o*x(54);

% Reaction: id = r911, name = RcL12UU opens
	reaction_r911=global_par_k1o*x(55);

% Reaction: id = r912, name = RcL12CU opens
	reaction_r912=global_par_k1o*x(56);

% Reaction: id = r913, name = RcL12LU opens
	reaction_r913=global_par_k1o*x(57);

% Reaction: id = r914, name = RcL12UG opens
	reaction_r914=global_par_k1o*x(58);

% Reaction: id = r915, name = RcL12UL opens
	reaction_r915=global_par_k1o*x(59);

% Reaction: id = r916, name = RcL12CG opens
	reaction_r916=global_par_k1o*x(60);

% Reaction: id = r917, name = RcL12CC opens
	reaction_r917=global_par_k1o*x(61);

% Reaction: id = r918, name = RcL12LG opens
	reaction_r918=global_par_k1o*x(62);

% Reaction: id = r919, name = R00UU binds ligand
	reaction_r919=global_par_kb*x(63)*x(1);

% Reaction: id = r920, name = R10UU binds ligand
	reaction_r920=global_par_kb*x(64)*x(1);

% Reaction: id = r921, name = R10CU binds ligand
	reaction_r921=global_par_kb*x(65)*x(1);

% Reaction: id = r922, name = R10LU binds ligand
	reaction_r922=global_par_kb*x(66)*x(1);

% Reaction: id = r923, name = R01UU binds ligand
	reaction_r923=global_par_kb*x(67)*x(1);

% Reaction: id = r924, name = R01UG binds ligand
	reaction_r924=global_par_kb*x(68)*x(1);

% Reaction: id = r925, name = R01UL binds ligand
	reaction_r925=global_par_kb*x(69)*x(1);

% Reaction: id = r926, name = R11UU binds ligand
	reaction_r926=global_par_kb*x(70)*x(1);

% Reaction: id = r927, name = R11CU binds ligand
	reaction_r927=global_par_kb*x(71)*x(1);

% Reaction: id = r928, name = R11LU binds ligand
	reaction_r928=global_par_kb*x(72)*x(1);

% Reaction: id = r929, name = R11UG binds ligand
	reaction_r929=global_par_kb*x(73)*x(1);

% Reaction: id = r930, name = R11UL binds ligand
	reaction_r930=global_par_kb*x(74)*x(1);

% Reaction: id = r931, name = R11CG binds ligand
	reaction_r931=global_par_kb*x(75)*x(1);

% Reaction: id = r932, name = R11CC binds ligand
	reaction_r932=global_par_kb*x(76)*x(1);

% Reaction: id = r933, name = R11LG binds ligand
	reaction_r933=global_par_kb*x(77)*x(1);

% Reaction: id = r934, name = R02UU binds ligand
	reaction_r934=global_par_kb*x(78)*x(1);

% Reaction: id = r935, name = R02UG binds ligand
	reaction_r935=global_par_kb*x(79)*x(1);

% Reaction: id = r936, name = R02UL binds ligand
	reaction_r936=global_par_kb*x(80)*x(1);

% Reaction: id = r937, name = R12UU binds ligand
	reaction_r937=global_par_kb*x(81)*x(1);

% Reaction: id = r938, name = R12CU binds ligand
	reaction_r938=global_par_kb*x(82)*x(1);

% Reaction: id = r939, name = R12LU binds ligand
	reaction_r939=global_par_kb*x(83)*x(1);

% Reaction: id = r940, name = R12UG binds ligand
	reaction_r940=global_par_kb*x(84)*x(1);

% Reaction: id = r941, name = R12UL binds ligand
	reaction_r941=global_par_kb*x(85)*x(1);

% Reaction: id = r942, name = R12CG binds ligand
	reaction_r942=global_par_kb*x(86)*x(1);

% Reaction: id = r943, name = R12CC binds ligand
	reaction_r943=global_par_kb*x(87)*x(1);

% Reaction: id = r944, name = R12LG binds ligand
	reaction_r944=global_par_kb*x(88)*x(1);

% Reaction: id = r945, name = Rc00UU binds ligand
	reaction_r945=global_par_k1b*x(11)*x(1);

% Reaction: id = r946, name = Rc10UU binds ligand
	reaction_r946=global_par_k1b*x(12)*x(1);

% Reaction: id = r947, name = Rc10CU binds ligand
	reaction_r947=global_par_k1b*x(13)*x(1);

% Reaction: id = r948, name = Rc10LU binds ligand
	reaction_r948=global_par_k1b*x(14)*x(1);

% Reaction: id = r949, name = Rc01UU binds ligand
	reaction_r949=global_par_k1b*x(15)*x(1);

% Reaction: id = r950, name = Rc01UG binds ligand
	reaction_r950=global_par_k1b*x(16)*x(1);

% Reaction: id = r951, name = Rc01UL binds ligand
	reaction_r951=global_par_k1b*x(17)*x(1);

% Reaction: id = r952, name = Rc11UU binds ligand
	reaction_r952=global_par_k1b*x(18)*x(1);

% Reaction: id = r953, name = Rc11CU binds ligand
	reaction_r953=global_par_k1b*x(19)*x(1);

% Reaction: id = r954, name = Rc11LU binds ligand
	reaction_r954=global_par_k1b*x(20)*x(1);

% Reaction: id = r955, name = Rc11UG binds ligand
	reaction_r955=global_par_k1b*x(21)*x(1);

% Reaction: id = r956, name = Rc11UL binds ligand
	reaction_r956=global_par_k1b*x(22)*x(1);

% Reaction: id = r957, name = Rc11CG binds ligand
	reaction_r957=global_par_k1b*x(23)*x(1);

% Reaction: id = r958, name = Rc11CC binds ligand
	reaction_r958=global_par_k1b*x(24)*x(1);

% Reaction: id = r959, name = Rc11LG binds ligand
	reaction_r959=global_par_k1b*x(25)*x(1);

% Reaction: id = r960, name = Rc02UU binds ligand
	reaction_r960=global_par_k1b*x(26)*x(1);

% Reaction: id = r961, name = Rc02UG binds ligand
	reaction_r961=global_par_k1b*x(27)*x(1);

% Reaction: id = r962, name = Rc02UL binds ligand
	reaction_r962=global_par_k1b*x(28)*x(1);

% Reaction: id = r963, name = Rc12UU binds ligand
	reaction_r963=global_par_k1b*x(29)*x(1);

% Reaction: id = r964, name = Rc12CU binds ligand
	reaction_r964=global_par_k1b*x(30)*x(1);

% Reaction: id = r965, name = Rc12LU binds ligand
	reaction_r965=global_par_k1b*x(31)*x(1);

% Reaction: id = r966, name = Rc12UG binds ligand
	reaction_r966=global_par_k1b*x(32)*x(1);

% Reaction: id = r967, name = Rc12UL binds ligand
	reaction_r967=global_par_k1b*x(33)*x(1);

% Reaction: id = r968, name = Rc12CG binds ligand
	reaction_r968=global_par_k1b*x(34)*x(1);

% Reaction: id = r969, name = Rc12CC binds ligand
	reaction_r969=global_par_k1b*x(35)*x(1);

% Reaction: id = r970, name = Rc12LG binds ligand
	reaction_r970=global_par_k1b*x(36)*x(1);

% Reaction: id = r971, name = Di00UU partner binds ligand
	reaction_r971=global_par_kb*x(115)*x(1);

% Reaction: id = r972, name = Di10UU partner binds ligand
	reaction_r972=global_par_kb*x(116)*x(1);

% Reaction: id = r973, name = Di10CU partner binds ligand
	reaction_r973=global_par_kb*x(117)*x(1);

% Reaction: id = r974, name = Di10LU partner binds ligand
	reaction_r974=global_par_kb*x(118)*x(1);

% Reaction: id = r975, name = Di01UU partner binds ligand
	reaction_r975=global_par_kb*x(119)*x(1);

% Reaction: id = r976, name = Di01UG partner binds ligand
	reaction_r976=global_par_kb*x(120)*x(1);

% Reaction: id = r977, name = Di01UL partner binds ligand
	reaction_r977=global_par_kb*x(121)*x(1);

% Reaction: id = r978, name = Di11UU partner binds ligand
	reaction_r978=global_par_kb*x(122)*x(1);

% Reaction: id = r979, name = Di11CU partner binds ligand
	reaction_r979=global_par_kb*x(123)*x(1);

% Reaction: id = r980, name = Di11LU partner binds ligand
	reaction_r980=global_par_kb*x(124)*x(1);

% Reaction: id = r981, name = Di11UG partner binds ligand
	reaction_r981=global_par_kb*x(125)*x(1);

% Reaction: id = r982, name = Di11UL partner binds ligand
	reaction_r982=global_par_kb*x(126)*x(1);

% Reaction: id = r983, name = Di11CG partner binds ligand
	reaction_r983=global_par_kb*x(127)*x(1);

% Reaction: id = r984, name = Di11CC partner binds ligand
	reaction_r984=global_par_kb*x(128)*x(1);

% Reaction: id = r985, name = Di11LG partner binds ligand
	reaction_r985=global_par_kb*x(129)*x(1);

% Reaction: id = r986, name = Di02UU partner binds ligand
	reaction_r986=global_par_kb*x(130)*x(1);

% Reaction: id = r987, name = Di02UG partner binds ligand
	reaction_r987=global_par_kb*x(131)*x(1);

% Reaction: id = r988, name = Di02UL partner binds ligand
	reaction_r988=global_par_kb*x(132)*x(1);

% Reaction: id = r989, name = Di12UU partner binds ligand
	reaction_r989=global_par_kb*x(133)*x(1);

% Reaction: id = r990, name = Di12CU partner binds ligand
	reaction_r990=global_par_kb*x(134)*x(1);

% Reaction: id = r991, name = Di12LU partner binds ligand
	reaction_r991=global_par_kb*x(135)*x(1);

% Reaction: id = r992, name = Di12UG partner binds ligand
	reaction_r992=global_par_kb*x(136)*x(1);

% Reaction: id = r993, name = Di12UL partner binds ligand
	reaction_r993=global_par_kb*x(137)*x(1);

% Reaction: id = r994, name = Di12CG partner binds ligand
	reaction_r994=global_par_kb*x(138)*x(1);

% Reaction: id = r995, name = Di12CC partner binds ligand
	reaction_r995=global_par_kb*x(139)*x(1);

% Reaction: id = r996, name = Di12LG partner binds ligand
	reaction_r996=global_par_kb*x(140)*x(1);

% Reaction: id = r997, name = Di00UU binds ligand
	reaction_r997=global_par_kb*x(115)*x(1);

% Reaction: id = r998, name = Di10UU binds ligand
	reaction_r998=global_par_kb*x(116)*x(1);

% Reaction: id = r999, name = Di10CU binds ligand
	reaction_r999=global_par_kb*x(117)*x(1);

% Reaction: id = r1000, name = Di10LU binds ligand
	reaction_r1000=global_par_kb*x(118)*x(1);

% Reaction: id = r1001, name = Di01UU binds ligand
	reaction_r1001=global_par_kb*x(119)*x(1);

% Reaction: id = r1002, name = Di01UG binds ligand
	reaction_r1002=global_par_kb*x(120)*x(1);

% Reaction: id = r1003, name = Di01UL binds ligand
	reaction_r1003=global_par_kb*x(121)*x(1);

% Reaction: id = r1004, name = Di11UU binds ligand
	reaction_r1004=global_par_kb*x(122)*x(1);

% Reaction: id = r1005, name = Di11CU binds ligand
	reaction_r1005=global_par_kb*x(123)*x(1);

% Reaction: id = r1006, name = Di11LU binds ligand
	reaction_r1006=global_par_kb*x(124)*x(1);

% Reaction: id = r1007, name = Di11UG binds ligand
	reaction_r1007=global_par_kb*x(125)*x(1);

% Reaction: id = r1008, name = Di11UL binds ligand
	reaction_r1008=global_par_kb*x(126)*x(1);

% Reaction: id = r1009, name = Di11CG binds ligand
	reaction_r1009=global_par_kb*x(127)*x(1);

% Reaction: id = r1010, name = Di11CC binds ligand
	reaction_r1010=global_par_kb*x(128)*x(1);

% Reaction: id = r1011, name = Di11LG binds ligand
	reaction_r1011=global_par_kb*x(129)*x(1);

% Reaction: id = r1012, name = Di02UU binds ligand
	reaction_r1012=global_par_kb*x(130)*x(1);

% Reaction: id = r1013, name = Di02UG binds ligand
	reaction_r1013=global_par_kb*x(131)*x(1);

% Reaction: id = r1014, name = Di02UL binds ligand
	reaction_r1014=global_par_kb*x(132)*x(1);

% Reaction: id = r1015, name = Di12UU binds ligand
	reaction_r1015=global_par_kb*x(133)*x(1);

% Reaction: id = r1016, name = Di12CU binds ligand
	reaction_r1016=global_par_kb*x(134)*x(1);

% Reaction: id = r1017, name = Di12LU binds ligand
	reaction_r1017=global_par_kb*x(135)*x(1);

% Reaction: id = r1018, name = Di12UG binds ligand
	reaction_r1018=global_par_kb*x(136)*x(1);

% Reaction: id = r1019, name = Di12UL binds ligand
	reaction_r1019=global_par_kb*x(137)*x(1);

% Reaction: id = r1020, name = Di12CG binds ligand
	reaction_r1020=global_par_kb*x(138)*x(1);

% Reaction: id = r1021, name = Di12CC binds ligand
	reaction_r1021=global_par_kb*x(139)*x(1);

% Reaction: id = r1022, name = Di12LG binds ligand
	reaction_r1022=global_par_kb*x(140)*x(1);

% Reaction: id = r1023, name = Da00UU binds ligand
	reaction_r1023=global_par_kb*x(141)*x(1);

% Reaction: id = r1024, name = Da10UU binds ligand
	reaction_r1024=global_par_kb*x(142)*x(1);

% Reaction: id = r1025, name = Da10CU binds ligand
	reaction_r1025=global_par_kb*x(143)*x(1);

% Reaction: id = r1026, name = Da10LU binds ligand
	reaction_r1026=global_par_kb*x(144)*x(1);

% Reaction: id = r1027, name = Da01UU binds ligand
	reaction_r1027=global_par_kb*x(145)*x(1);

% Reaction: id = r1028, name = Da01UG binds ligand
	reaction_r1028=global_par_kb*x(146)*x(1);

% Reaction: id = r1029, name = Da01UL binds ligand
	reaction_r1029=global_par_kb*x(147)*x(1);

% Reaction: id = r1030, name = Da11UU binds ligand
	reaction_r1030=global_par_kb*x(148)*x(1);

% Reaction: id = r1031, name = Da11CU binds ligand
	reaction_r1031=global_par_kb*x(149)*x(1);

% Reaction: id = r1032, name = Da11LU binds ligand
	reaction_r1032=global_par_kb*x(150)*x(1);

% Reaction: id = r1033, name = Da11UG binds ligand
	reaction_r1033=global_par_kb*x(151)*x(1);

% Reaction: id = r1034, name = Da11UL binds ligand
	reaction_r1034=global_par_kb*x(152)*x(1);

% Reaction: id = r1035, name = Da11CG binds ligand
	reaction_r1035=global_par_kb*x(153)*x(1);

% Reaction: id = r1036, name = Da11CC binds ligand
	reaction_r1036=global_par_kb*x(154)*x(1);

% Reaction: id = r1037, name = Da11LG binds ligand
	reaction_r1037=global_par_kb*x(155)*x(1);

% Reaction: id = r1038, name = Da02UU binds ligand
	reaction_r1038=global_par_kb*x(156)*x(1);

% Reaction: id = r1039, name = Da02UG binds ligand
	reaction_r1039=global_par_kb*x(157)*x(1);

% Reaction: id = r1040, name = Da02UL binds ligand
	reaction_r1040=global_par_kb*x(158)*x(1);

% Reaction: id = r1041, name = Da12UU binds ligand
	reaction_r1041=global_par_kb*x(159)*x(1);

% Reaction: id = r1042, name = Da12CU binds ligand
	reaction_r1042=global_par_kb*x(160)*x(1);

% Reaction: id = r1043, name = Da12LU binds ligand
	reaction_r1043=global_par_kb*x(161)*x(1);

% Reaction: id = r1044, name = Da12UG binds ligand
	reaction_r1044=global_par_kb*x(162)*x(1);

% Reaction: id = r1045, name = Da12UL binds ligand
	reaction_r1045=global_par_kb*x(163)*x(1);

% Reaction: id = r1046, name = Da12CG binds ligand
	reaction_r1046=global_par_kb*x(164)*x(1);

% Reaction: id = r1047, name = Da12CC binds ligand
	reaction_r1047=global_par_kb*x(165)*x(1);

% Reaction: id = r1048, name = Da12LG binds ligand
	reaction_r1048=global_par_kb*x(166)*x(1);

% Reaction: id = r1049, name = DiL00UU partner binds ligand
	reaction_r1049=global_par_kb*x(167)*x(1);

% Reaction: id = r1050, name = DiL10UU partner binds ligand
	reaction_r1050=global_par_kb*x(168)*x(1);

% Reaction: id = r1051, name = DiL10CU partner binds ligand
	reaction_r1051=global_par_kb*x(169)*x(1);

% Reaction: id = r1052, name = DiL10LU partner binds ligand
	reaction_r1052=global_par_kb*x(170)*x(1);

% Reaction: id = r1053, name = DiL01UU partner binds ligand
	reaction_r1053=global_par_kb*x(171)*x(1);

% Reaction: id = r1054, name = DiL01UG partner binds ligand
	reaction_r1054=global_par_kb*x(172)*x(1);

% Reaction: id = r1055, name = DiL01UL partner binds ligand
	reaction_r1055=global_par_kb*x(173)*x(1);

% Reaction: id = r1056, name = DiL11UU partner binds ligand
	reaction_r1056=global_par_kb*x(174)*x(1);

% Reaction: id = r1057, name = DiL11CU partner binds ligand
	reaction_r1057=global_par_kb*x(175)*x(1);

% Reaction: id = r1058, name = DiL11LU partner binds ligand
	reaction_r1058=global_par_kb*x(176)*x(1);

% Reaction: id = r1059, name = DiL11UG partner binds ligand
	reaction_r1059=global_par_kb*x(177)*x(1);

% Reaction: id = r1060, name = DiL11UL partner binds ligand
	reaction_r1060=global_par_kb*x(178)*x(1);

% Reaction: id = r1061, name = DiL11CG partner binds ligand
	reaction_r1061=global_par_kb*x(179)*x(1);

% Reaction: id = r1062, name = DiL11CC partner binds ligand
	reaction_r1062=global_par_kb*x(180)*x(1);

% Reaction: id = r1063, name = DiL11LG partner binds ligand
	reaction_r1063=global_par_kb*x(181)*x(1);

% Reaction: id = r1064, name = DiL02UU partner binds ligand
	reaction_r1064=global_par_kb*x(182)*x(1);

% Reaction: id = r1065, name = DiL02UG partner binds ligand
	reaction_r1065=global_par_kb*x(183)*x(1);

% Reaction: id = r1066, name = DiL02UL partner binds ligand
	reaction_r1066=global_par_kb*x(184)*x(1);

% Reaction: id = r1067, name = DiL12UU partner binds ligand
	reaction_r1067=global_par_kb*x(185)*x(1);

% Reaction: id = r1068, name = DiL12CU partner binds ligand
	reaction_r1068=global_par_kb*x(186)*x(1);

% Reaction: id = r1069, name = DiL12LU partner binds ligand
	reaction_r1069=global_par_kb*x(187)*x(1);

% Reaction: id = r1070, name = DiL12UG partner binds ligand
	reaction_r1070=global_par_kb*x(188)*x(1);

% Reaction: id = r1071, name = DiL12UL partner binds ligand
	reaction_r1071=global_par_kb*x(189)*x(1);

% Reaction: id = r1072, name = DiL12CG partner binds ligand
	reaction_r1072=global_par_kb*x(190)*x(1);

% Reaction: id = r1073, name = DiL12CC partner binds ligand
	reaction_r1073=global_par_kb*x(191)*x(1);

% Reaction: id = r1074, name = DiL12LG partner binds ligand
	reaction_r1074=global_par_kb*x(192)*x(1);

% Reaction: id = r1075, name = RL00UU dissociates ligand
	reaction_r1075=global_par_ku*x(89);

% Reaction: id = r1076, name = RL10UU dissociates ligand
	reaction_r1076=global_par_ku*x(90);

% Reaction: id = r1077, name = RL10CU dissociates ligand
	reaction_r1077=global_par_ku*x(91);

% Reaction: id = r1078, name = RL10LU dissociates ligand
	reaction_r1078=global_par_ku*x(92);

% Reaction: id = r1079, name = RL01UU dissociates ligand
	reaction_r1079=global_par_ku*x(93);

% Reaction: id = r1080, name = RL01UG dissociates ligand
	reaction_r1080=global_par_ku*x(94);

% Reaction: id = r1081, name = RL01UL dissociates ligand
	reaction_r1081=global_par_ku*x(95);

% Reaction: id = r1082, name = RL11UU dissociates ligand
	reaction_r1082=global_par_ku*x(96);

% Reaction: id = r1083, name = RL11CU dissociates ligand
	reaction_r1083=global_par_ku*x(97);

% Reaction: id = r1084, name = RL11LU dissociates ligand
	reaction_r1084=global_par_ku*x(98);

% Reaction: id = r1085, name = RL11UG dissociates ligand
	reaction_r1085=global_par_ku*x(99);

% Reaction: id = r1086, name = RL11UL dissociates ligand
	reaction_r1086=global_par_ku*x(100);

% Reaction: id = r1087, name = RL11CG dissociates ligand
	reaction_r1087=global_par_ku*x(101);

% Reaction: id = r1088, name = RL11CC dissociates ligand
	reaction_r1088=global_par_ku*x(102);

% Reaction: id = r1089, name = RL11LG dissociates ligand
	reaction_r1089=global_par_ku*x(103);

% Reaction: id = r1090, name = RL02UU dissociates ligand
	reaction_r1090=global_par_ku*x(104);

% Reaction: id = r1091, name = RL02UG dissociates ligand
	reaction_r1091=global_par_ku*x(105);

% Reaction: id = r1092, name = RL02UL dissociates ligand
	reaction_r1092=global_par_ku*x(106);

% Reaction: id = r1093, name = RL12UU dissociates ligand
	reaction_r1093=global_par_ku*x(107);

% Reaction: id = r1094, name = RL12CU dissociates ligand
	reaction_r1094=global_par_ku*x(108);

% Reaction: id = r1095, name = RL12LU dissociates ligand
	reaction_r1095=global_par_ku*x(109);

% Reaction: id = r1096, name = RL12UG dissociates ligand
	reaction_r1096=global_par_ku*x(110);

% Reaction: id = r1097, name = RL12UL dissociates ligand
	reaction_r1097=global_par_ku*x(111);

% Reaction: id = r1098, name = RL12CG dissociates ligand
	reaction_r1098=global_par_ku*x(112);

% Reaction: id = r1099, name = RL12CC dissociates ligand
	reaction_r1099=global_par_ku*x(113);

% Reaction: id = r1100, name = RL12LG dissociates ligand
	reaction_r1100=global_par_ku*x(114);

% Reaction: id = r1101, name = RcL00UU dissociates ligand
	reaction_r1101=global_par_k1u*x(37);

% Reaction: id = r1102, name = RcL10UU dissociates ligand
	reaction_r1102=global_par_k1u*x(38);

% Reaction: id = r1103, name = RcL10CU dissociates ligand
	reaction_r1103=global_par_k1u*x(39);

% Reaction: id = r1104, name = RcL10LU dissociates ligand
	reaction_r1104=global_par_k1u*x(40);

% Reaction: id = r1105, name = RcL01UU dissociates ligand
	reaction_r1105=global_par_k1u*x(41);

% Reaction: id = r1106, name = RcL01UG dissociates ligand
	reaction_r1106=global_par_k1u*x(42);

% Reaction: id = r1107, name = RcL01UL dissociates ligand
	reaction_r1107=global_par_k1u*x(43);

% Reaction: id = r1108, name = RcL11UU dissociates ligand
	reaction_r1108=global_par_k1u*x(44);

% Reaction: id = r1109, name = RcL11CU dissociates ligand
	reaction_r1109=global_par_k1u*x(45);

% Reaction: id = r1110, name = RcL11LU dissociates ligand
	reaction_r1110=global_par_k1u*x(46);

% Reaction: id = r1111, name = RcL11UG dissociates ligand
	reaction_r1111=global_par_k1u*x(47);

% Reaction: id = r1112, name = RcL11UL dissociates ligand
	reaction_r1112=global_par_k1u*x(48);

% Reaction: id = r1113, name = RcL11CG dissociates ligand
	reaction_r1113=global_par_k1u*x(49);

% Reaction: id = r1114, name = RcL11CC dissociates ligand
	reaction_r1114=global_par_k1u*x(50);

% Reaction: id = r1115, name = RcL11LG dissociates ligand
	reaction_r1115=global_par_k1u*x(51);

% Reaction: id = r1116, name = RcL02UU dissociates ligand
	reaction_r1116=global_par_k1u*x(52);

% Reaction: id = r1117, name = RcL02UG dissociates ligand
	reaction_r1117=global_par_k1u*x(53);

% Reaction: id = r1118, name = RcL02UL dissociates ligand
	reaction_r1118=global_par_k1u*x(54);

% Reaction: id = r1119, name = RcL12UU dissociates ligand
	reaction_r1119=global_par_k1u*x(55);

% Reaction: id = r1120, name = RcL12CU dissociates ligand
	reaction_r1120=global_par_k1u*x(56);

% Reaction: id = r1121, name = RcL12LU dissociates ligand
	reaction_r1121=global_par_k1u*x(57);

% Reaction: id = r1122, name = RcL12UG dissociates ligand
	reaction_r1122=global_par_k1u*x(58);

% Reaction: id = r1123, name = RcL12UL dissociates ligand
	reaction_r1123=global_par_k1u*x(59);

% Reaction: id = r1124, name = RcL12CG dissociates ligand
	reaction_r1124=global_par_k1u*x(60);

% Reaction: id = r1125, name = RcL12CC dissociates ligand
	reaction_r1125=global_par_k1u*x(61);

% Reaction: id = r1126, name = RcL12LG dissociates ligand
	reaction_r1126=global_par_k1u*x(62);

% Reaction: id = r1127, name = DaL00UU partner dissociates its ligand
	reaction_r1127=global_par_ku*x(193);

% Reaction: id = r1128, name = DaL10UU partner dissociates its ligand
	reaction_r1128=global_par_ku*x(194);

% Reaction: id = r1129, name = DaL10CU partner dissociates its ligand
	reaction_r1129=global_par_ku*x(195);

% Reaction: id = r1130, name = DaL10LU partner dissociates its ligand
	reaction_r1130=global_par_ku*x(196);

% Reaction: id = r1131, name = DaL01UU partner dissociates its ligand
	reaction_r1131=global_par_ku*x(197);

% Reaction: id = r1132, name = DaL01UG partner dissociates its ligand
	reaction_r1132=global_par_ku*x(198);

% Reaction: id = r1133, name = DaL01UL partner dissociates its ligand
	reaction_r1133=global_par_ku*x(199);

% Reaction: id = r1134, name = DaL11UU partner dissociates its ligand
	reaction_r1134=global_par_ku*x(200);

% Reaction: id = r1135, name = DaL11CU partner dissociates its ligand
	reaction_r1135=global_par_ku*x(201);

% Reaction: id = r1136, name = DaL11LU partner dissociates its ligand
	reaction_r1136=global_par_ku*x(202);

% Reaction: id = r1137, name = DaL11UG partner dissociates its ligand
	reaction_r1137=global_par_ku*x(203);

% Reaction: id = r1138, name = DaL11UL partner dissociates its ligand
	reaction_r1138=global_par_ku*x(204);

% Reaction: id = r1139, name = DaL11CG partner dissociates its ligand
	reaction_r1139=global_par_ku*x(205);

% Reaction: id = r1140, name = DaL11CC partner dissociates its ligand
	reaction_r1140=global_par_ku*x(206);

% Reaction: id = r1141, name = DaL11LG partner dissociates its ligand
	reaction_r1141=global_par_ku*x(207);

% Reaction: id = r1142, name = DaL02UU partner dissociates its ligand
	reaction_r1142=global_par_ku*x(208);

% Reaction: id = r1143, name = DaL02UG partner dissociates its ligand
	reaction_r1143=global_par_ku*x(209);

% Reaction: id = r1144, name = DaL02UL partner dissociates its ligand
	reaction_r1144=global_par_ku*x(210);

% Reaction: id = r1145, name = DaL12UU partner dissociates its ligand
	reaction_r1145=global_par_ku*x(211);

% Reaction: id = r1146, name = DaL12CU partner dissociates its ligand
	reaction_r1146=global_par_ku*x(212);

% Reaction: id = r1147, name = DaL12LU partner dissociates its ligand
	reaction_r1147=global_par_ku*x(213);

% Reaction: id = r1148, name = DaL12UG partner dissociates its ligand
	reaction_r1148=global_par_ku*x(214);

% Reaction: id = r1149, name = DaL12UL partner dissociates its ligand
	reaction_r1149=global_par_ku*x(215);

% Reaction: id = r1150, name = DaL12CG partner dissociates its ligand
	reaction_r1150=global_par_ku*x(216);

% Reaction: id = r1151, name = DaL12CC partner dissociates its ligand
	reaction_r1151=global_par_ku*x(217);

% Reaction: id = r1152, name = DaL12LG partner dissociates its ligand
	reaction_r1152=global_par_ku*x(218);

% Reaction: id = r1153, name = DaL00UU dissociates its ligand
	reaction_r1153=global_par_ku*x(193);

% Reaction: id = r1154, name = DaL10UU dissociates its ligand
	reaction_r1154=global_par_ku*x(194);

% Reaction: id = r1155, name = DaL10CU dissociates its ligand
	reaction_r1155=global_par_ku*x(195);

% Reaction: id = r1156, name = DaL10LU dissociates its ligand
	reaction_r1156=global_par_ku*x(196);

% Reaction: id = r1157, name = DaL01UU dissociates its ligand
	reaction_r1157=global_par_ku*x(197);

% Reaction: id = r1158, name = DaL01UG dissociates its ligand
	reaction_r1158=global_par_ku*x(198);

% Reaction: id = r1159, name = DaL01UL dissociates its ligand
	reaction_r1159=global_par_ku*x(199);

% Reaction: id = r1160, name = DaL11UU dissociates its ligand
	reaction_r1160=global_par_ku*x(200);

% Reaction: id = r1161, name = DaL11CU dissociates its ligand
	reaction_r1161=global_par_ku*x(201);

% Reaction: id = r1162, name = DaL11LU dissociates its ligand
	reaction_r1162=global_par_ku*x(202);

% Reaction: id = r1163, name = DaL11UG dissociates its ligand
	reaction_r1163=global_par_ku*x(203);

% Reaction: id = r1164, name = DaL11UL dissociates its ligand
	reaction_r1164=global_par_ku*x(204);

% Reaction: id = r1165, name = DaL11CG dissociates its ligand
	reaction_r1165=global_par_ku*x(205);

% Reaction: id = r1166, name = DaL11CC dissociates its ligand
	reaction_r1166=global_par_ku*x(206);

% Reaction: id = r1167, name = DaL11LG dissociates its ligand
	reaction_r1167=global_par_ku*x(207);

% Reaction: id = r1168, name = DaL02UU dissociates its ligand
	reaction_r1168=global_par_ku*x(208);

% Reaction: id = r1169, name = DaL02UG dissociates its ligand
	reaction_r1169=global_par_ku*x(209);

% Reaction: id = r1170, name = DaL02UL dissociates its ligand
	reaction_r1170=global_par_ku*x(210);

% Reaction: id = r1171, name = DaL12UU dissociates its ligand
	reaction_r1171=global_par_ku*x(211);

% Reaction: id = r1172, name = DaL12CU dissociates its ligand
	reaction_r1172=global_par_ku*x(212);

% Reaction: id = r1173, name = DaL12LU dissociates its ligand
	reaction_r1173=global_par_ku*x(213);

% Reaction: id = r1174, name = DaL12UG dissociates its ligand
	reaction_r1174=global_par_ku*x(214);

% Reaction: id = r1175, name = DaL12UL dissociates its ligand
	reaction_r1175=global_par_ku*x(215);

% Reaction: id = r1176, name = DaL12CG dissociates its ligand
	reaction_r1176=global_par_ku*x(216);

% Reaction: id = r1177, name = DaL12CC dissociates its ligand
	reaction_r1177=global_par_ku*x(217);

% Reaction: id = r1178, name = DaL12LG dissociates its ligand
	reaction_r1178=global_par_ku*x(218);

% Reaction: id = r1179, name = Da00UU partner dissociates its ligand
	reaction_r1179=global_par_ku*x(141);

% Reaction: id = r1180, name = Da10UU partner dissociates its ligand
	reaction_r1180=global_par_ku*x(142);

% Reaction: id = r1181, name = Da10CU partner dissociates its ligand
	reaction_r1181=global_par_ku*x(143);

% Reaction: id = r1182, name = Da10LU partner dissociates its ligand
	reaction_r1182=global_par_ku*x(144);

% Reaction: id = r1183, name = Da01UU partner dissociates its ligand
	reaction_r1183=global_par_ku*x(145);

% Reaction: id = r1184, name = Da01UG partner dissociates its ligand
	reaction_r1184=global_par_ku*x(146);

% Reaction: id = r1185, name = Da01UL partner dissociates its ligand
	reaction_r1185=global_par_ku*x(147);

% Reaction: id = r1186, name = Da11UU partner dissociates its ligand
	reaction_r1186=global_par_ku*x(148);

% Reaction: id = r1187, name = Da11CU partner dissociates its ligand
	reaction_r1187=global_par_ku*x(149);

% Reaction: id = r1188, name = Da11LU partner dissociates its ligand
	reaction_r1188=global_par_ku*x(150);

% Reaction: id = r1189, name = Da11UG partner dissociates its ligand
	reaction_r1189=global_par_ku*x(151);

% Reaction: id = r1190, name = Da11UL partner dissociates its ligand
	reaction_r1190=global_par_ku*x(152);

% Reaction: id = r1191, name = Da11CG partner dissociates its ligand
	reaction_r1191=global_par_ku*x(153);

% Reaction: id = r1192, name = Da11CC partner dissociates its ligand
	reaction_r1192=global_par_ku*x(154);

% Reaction: id = r1193, name = Da11LG partner dissociates its ligand
	reaction_r1193=global_par_ku*x(155);

% Reaction: id = r1194, name = Da02UU partner dissociates its ligand
	reaction_r1194=global_par_ku*x(156);

% Reaction: id = r1195, name = Da02UG partner dissociates its ligand
	reaction_r1195=global_par_ku*x(157);

% Reaction: id = r1196, name = Da02UL partner dissociates its ligand
	reaction_r1196=global_par_ku*x(158);

% Reaction: id = r1197, name = Da12UU partner dissociates its ligand
	reaction_r1197=global_par_ku*x(159);

% Reaction: id = r1198, name = Da12CU partner dissociates its ligand
	reaction_r1198=global_par_ku*x(160);

% Reaction: id = r1199, name = Da12LU partner dissociates its ligand
	reaction_r1199=global_par_ku*x(161);

% Reaction: id = r1200, name = Da12UG partner dissociates its ligand
	reaction_r1200=global_par_ku*x(162);

% Reaction: id = r1201, name = Da12UL partner dissociates its ligand
	reaction_r1201=global_par_ku*x(163);

% Reaction: id = r1202, name = Da12CG partner dissociates its ligand
	reaction_r1202=global_par_ku*x(164);

% Reaction: id = r1203, name = Da12CC partner dissociates its ligand
	reaction_r1203=global_par_ku*x(165);

% Reaction: id = r1204, name = Da12LG partner dissociates its ligand
	reaction_r1204=global_par_ku*x(166);

% Reaction: id = r1205, name = DiL00UU dissociates its ligand
	reaction_r1205=global_par_ku*x(167);

% Reaction: id = r1206, name = DiL10UU dissociates its ligand
	reaction_r1206=global_par_ku*x(168);

% Reaction: id = r1207, name = DiL10CU dissociates its ligand
	reaction_r1207=global_par_ku*x(169);

% Reaction: id = r1208, name = DiL10LU dissociates its ligand
	reaction_r1208=global_par_ku*x(170);

% Reaction: id = r1209, name = DiL01UU dissociates its ligand
	reaction_r1209=global_par_ku*x(171);

% Reaction: id = r1210, name = DiL01UG dissociates its ligand
	reaction_r1210=global_par_ku*x(172);

% Reaction: id = r1211, name = DiL01UL dissociates its ligand
	reaction_r1211=global_par_ku*x(173);

% Reaction: id = r1212, name = DiL11UU dissociates its ligand
	reaction_r1212=global_par_ku*x(174);

% Reaction: id = r1213, name = DiL11CU dissociates its ligand
	reaction_r1213=global_par_ku*x(175);

% Reaction: id = r1214, name = DiL11LU dissociates its ligand
	reaction_r1214=global_par_ku*x(176);

% Reaction: id = r1215, name = DiL11UG dissociates its ligand
	reaction_r1215=global_par_ku*x(177);

% Reaction: id = r1216, name = DiL11UL dissociates its ligand
	reaction_r1216=global_par_ku*x(178);

% Reaction: id = r1217, name = DiL11CG dissociates its ligand
	reaction_r1217=global_par_ku*x(179);

% Reaction: id = r1218, name = DiL11CC dissociates its ligand
	reaction_r1218=global_par_ku*x(180);

% Reaction: id = r1219, name = DiL11LG dissociates its ligand
	reaction_r1219=global_par_ku*x(181);

% Reaction: id = r1220, name = DiL02UU dissociates its ligand
	reaction_r1220=global_par_ku*x(182);

% Reaction: id = r1221, name = DiL02UG dissociates its ligand
	reaction_r1221=global_par_ku*x(183);

% Reaction: id = r1222, name = DiL02UL dissociates its ligand
	reaction_r1222=global_par_ku*x(184);

% Reaction: id = r1223, name = DiL12UU dissociates its ligand
	reaction_r1223=global_par_ku*x(185);

% Reaction: id = r1224, name = DiL12CU dissociates its ligand
	reaction_r1224=global_par_ku*x(186);

% Reaction: id = r1225, name = DiL12LU dissociates its ligand
	reaction_r1225=global_par_ku*x(187);

% Reaction: id = r1226, name = DiL12UG dissociates its ligand
	reaction_r1226=global_par_ku*x(188);

% Reaction: id = r1227, name = DiL12UL dissociates its ligand
	reaction_r1227=global_par_ku*x(189);

% Reaction: id = r1228, name = DiL12CG dissociates its ligand
	reaction_r1228=global_par_ku*x(190);

% Reaction: id = r1229, name = DiL12CC dissociates its ligand
	reaction_r1229=global_par_ku*x(191);

% Reaction: id = r1230, name = DiL12LG dissociates its ligand
	reaction_r1230=global_par_ku*x(192);

% Reaction: id = r1231, name = R00UU dimerizes to  Di00UU
	reaction_r1231=global_par_kbDIM*x(63)*x(9);

% Reaction: id = r1232, name = R10UU dimerizes to  Di10UU
	reaction_r1232=global_par_kbDIM*x(64)*x(9);

% Reaction: id = r1233, name = R10CU dimerizes to  Di10CU
	reaction_r1233=global_par_kbDIM*x(65)*x(9);

% Reaction: id = r1234, name = R10LU dimerizes to  Di10LU
	reaction_r1234=global_par_kbDIM*x(66)*x(9);

% Reaction: id = r1235, name = R01UU dimerizes to  Di01UU
	reaction_r1235=global_par_kbDIM*x(67)*x(9);

% Reaction: id = r1236, name = R01UG dimerizes to  Di01UG
	reaction_r1236=global_par_kbDIM*x(68)*x(9);

% Reaction: id = r1237, name = R01UL dimerizes to  Di01UL
	reaction_r1237=global_par_kbDIM*x(69)*x(9);

% Reaction: id = r1238, name = R11UU dimerizes to  Di11UU
	reaction_r1238=global_par_kbDIM*x(70)*x(9);

% Reaction: id = r1239, name = R11CU dimerizes to  Di11CU
	reaction_r1239=global_par_kbDIM*x(71)*x(9);

% Reaction: id = r1240, name = R11LU dimerizes to  Di11LU
	reaction_r1240=global_par_kbDIM*x(72)*x(9);

% Reaction: id = r1241, name = R11UG dimerizes to  Di11UG
	reaction_r1241=global_par_kbDIM*x(73)*x(9);

% Reaction: id = r1242, name = R11UL dimerizes to  Di11UL
	reaction_r1242=global_par_kbDIM*x(74)*x(9);

% Reaction: id = r1243, name = R11CG dimerizes to  Di11CG
	reaction_r1243=global_par_kbDIM*x(75)*x(9);

% Reaction: id = r1244, name = R11CC dimerizes to  Di11CC
	reaction_r1244=global_par_kbDIM*x(76)*x(9);

% Reaction: id = r1245, name = R11LG dimerizes to  Di11LG
	reaction_r1245=global_par_kbDIM*x(77)*x(9);

% Reaction: id = r1246, name = R02UU dimerizes to  Di02UU
	reaction_r1246=global_par_kbDIM*x(78)*x(9);

% Reaction: id = r1247, name = R02UG dimerizes to  Di02UG
	reaction_r1247=global_par_kbDIM*x(79)*x(9);

% Reaction: id = r1248, name = R02UL dimerizes to  Di02UL
	reaction_r1248=global_par_kbDIM*x(80)*x(9);

% Reaction: id = r1249, name = R12UU dimerizes to  Di12UU
	reaction_r1249=global_par_kbDIM*x(81)*x(9);

% Reaction: id = r1250, name = R12CU dimerizes to  Di12CU
	reaction_r1250=global_par_kbDIM*x(82)*x(9);

% Reaction: id = r1251, name = R12LU dimerizes to  Di12LU
	reaction_r1251=global_par_kbDIM*x(83)*x(9);

% Reaction: id = r1252, name = R12UG dimerizes to  Di12UG
	reaction_r1252=global_par_kbDIM*x(84)*x(9);

% Reaction: id = r1253, name = R12UL dimerizes to  Di12UL
	reaction_r1253=global_par_kbDIM*x(85)*x(9);

% Reaction: id = r1254, name = R12CG dimerizes to  Di12CG
	reaction_r1254=global_par_kbDIM*x(86)*x(9);

% Reaction: id = r1255, name = R12CC dimerizes to  Di12CC
	reaction_r1255=global_par_kbDIM*x(87)*x(9);

% Reaction: id = r1256, name = R12LG dimerizes to  Di12LG
	reaction_r1256=global_par_kbDIM*x(88)*x(9);

% Reaction: id = r1257, name = R00UU dimerizes to  Da00UU
	reaction_r1257=global_par_kbDIM*x(63)*x(10);

% Reaction: id = r1258, name = R10UU dimerizes to  Da10UU
	reaction_r1258=global_par_kbDIM*x(64)*x(10);

% Reaction: id = r1259, name = R10CU dimerizes to  Da10CU
	reaction_r1259=global_par_kbDIM*x(65)*x(10);

% Reaction: id = r1260, name = R10LU dimerizes to  Da10LU
	reaction_r1260=global_par_kbDIM*x(66)*x(10);

% Reaction: id = r1261, name = R01UU dimerizes to  Da01UU
	reaction_r1261=global_par_kbDIM*x(67)*x(10);

% Reaction: id = r1262, name = R01UG dimerizes to  Da01UG
	reaction_r1262=global_par_kbDIM*x(68)*x(10);

% Reaction: id = r1263, name = R01UL dimerizes to  Da01UL
	reaction_r1263=global_par_kbDIM*x(69)*x(10);

% Reaction: id = r1264, name = R11UU dimerizes to  Da11UU
	reaction_r1264=global_par_kbDIM*x(70)*x(10);

% Reaction: id = r1265, name = R11CU dimerizes to  Da11CU
	reaction_r1265=global_par_kbDIM*x(71)*x(10);

% Reaction: id = r1266, name = R11LU dimerizes to  Da11LU
	reaction_r1266=global_par_kbDIM*x(72)*x(10);

% Reaction: id = r1267, name = R11UG dimerizes to  Da11UG
	reaction_r1267=global_par_kbDIM*x(73)*x(10);

% Reaction: id = r1268, name = R11UL dimerizes to  Da11UL
	reaction_r1268=global_par_kbDIM*x(74)*x(10);

% Reaction: id = r1269, name = R11CG dimerizes to  Da11CG
	reaction_r1269=global_par_kbDIM*x(75)*x(10);

% Reaction: id = r1270, name = R11CC dimerizes to  Da11CC
	reaction_r1270=global_par_kbDIM*x(76)*x(10);

% Reaction: id = r1271, name = R11LG dimerizes to  Da11LG
	reaction_r1271=global_par_kbDIM*x(77)*x(10);

% Reaction: id = r1272, name = R02UU dimerizes to  Da02UU
	reaction_r1272=global_par_kbDIM*x(78)*x(10);

% Reaction: id = r1273, name = R02UG dimerizes to  Da02UG
	reaction_r1273=global_par_kbDIM*x(79)*x(10);

% Reaction: id = r1274, name = R02UL dimerizes to  Da02UL
	reaction_r1274=global_par_kbDIM*x(80)*x(10);

% Reaction: id = r1275, name = R12UU dimerizes to  Da12UU
	reaction_r1275=global_par_kbDIM*x(81)*x(10);

% Reaction: id = r1276, name = R12CU dimerizes to  Da12CU
	reaction_r1276=global_par_kbDIM*x(82)*x(10);

% Reaction: id = r1277, name = R12LU dimerizes to  Da12LU
	reaction_r1277=global_par_kbDIM*x(83)*x(10);

% Reaction: id = r1278, name = R12UG dimerizes to  Da12UG
	reaction_r1278=global_par_kbDIM*x(84)*x(10);

% Reaction: id = r1279, name = R12UL dimerizes to  Da12UL
	reaction_r1279=global_par_kbDIM*x(85)*x(10);

% Reaction: id = r1280, name = R12CG dimerizes to  Da12CG
	reaction_r1280=global_par_kbDIM*x(86)*x(10);

% Reaction: id = r1281, name = R12CC dimerizes to  Da12CC
	reaction_r1281=global_par_kbDIM*x(87)*x(10);

% Reaction: id = r1282, name = R12LG dimerizes to  Da12LG
	reaction_r1282=global_par_kbDIM*x(88)*x(10);

% Reaction: id = r1283, name = RL00UU dimerizes to  DiL00UU
	reaction_r1283=global_par_kbDIM*x(89)*x(9);

% Reaction: id = r1284, name = RL10UU dimerizes to  DiL10UU
	reaction_r1284=global_par_kbDIM*x(90)*x(9);

% Reaction: id = r1285, name = RL10CU dimerizes to  DiL10CU
	reaction_r1285=global_par_kbDIM*x(91)*x(9);

% Reaction: id = r1286, name = RL10LU dimerizes to  DiL10LU
	reaction_r1286=global_par_kbDIM*x(92)*x(9);

% Reaction: id = r1287, name = RL01UU dimerizes to  DiL01UU
	reaction_r1287=global_par_kbDIM*x(93)*x(9);

% Reaction: id = r1288, name = RL01UG dimerizes to  DiL01UG
	reaction_r1288=global_par_kbDIM*x(94)*x(9);

% Reaction: id = r1289, name = RL01UL dimerizes to  DiL01UL
	reaction_r1289=global_par_kbDIM*x(95)*x(9);

% Reaction: id = r1290, name = RL11UU dimerizes to  DiL11UU
	reaction_r1290=global_par_kbDIM*x(96)*x(9);

% Reaction: id = r1291, name = RL11CU dimerizes to  DiL11CU
	reaction_r1291=global_par_kbDIM*x(97)*x(9);

% Reaction: id = r1292, name = RL11LU dimerizes to  DiL11LU
	reaction_r1292=global_par_kbDIM*x(98)*x(9);

% Reaction: id = r1293, name = RL11UG dimerizes to  DiL11UG
	reaction_r1293=global_par_kbDIM*x(99)*x(9);

% Reaction: id = r1294, name = RL11UL dimerizes to  DiL11UL
	reaction_r1294=global_par_kbDIM*x(100)*x(9);

% Reaction: id = r1295, name = RL11CG dimerizes to  DiL11CG
	reaction_r1295=global_par_kbDIM*x(101)*x(9);

% Reaction: id = r1296, name = RL11CC dimerizes to  DiL11CC
	reaction_r1296=global_par_kbDIM*x(102)*x(9);

% Reaction: id = r1297, name = RL11LG dimerizes to  DiL11LG
	reaction_r1297=global_par_kbDIM*x(103)*x(9);

% Reaction: id = r1298, name = RL02UU dimerizes to  DiL02UU
	reaction_r1298=global_par_kbDIM*x(104)*x(9);

% Reaction: id = r1299, name = RL02UG dimerizes to  DiL02UG
	reaction_r1299=global_par_kbDIM*x(105)*x(9);

% Reaction: id = r1300, name = RL02UL dimerizes to  DiL02UL
	reaction_r1300=global_par_kbDIM*x(106)*x(9);

% Reaction: id = r1301, name = RL12UU dimerizes to  DiL12UU
	reaction_r1301=global_par_kbDIM*x(107)*x(9);

% Reaction: id = r1302, name = RL12CU dimerizes to  DiL12CU
	reaction_r1302=global_par_kbDIM*x(108)*x(9);

% Reaction: id = r1303, name = RL12LU dimerizes to  DiL12LU
	reaction_r1303=global_par_kbDIM*x(109)*x(9);

% Reaction: id = r1304, name = RL12UG dimerizes to  DiL12UG
	reaction_r1304=global_par_kbDIM*x(110)*x(9);

% Reaction: id = r1305, name = RL12UL dimerizes to  DiL12UL
	reaction_r1305=global_par_kbDIM*x(111)*x(9);

% Reaction: id = r1306, name = RL12CG dimerizes to  DiL12CG
	reaction_r1306=global_par_kbDIM*x(112)*x(9);

% Reaction: id = r1307, name = RL12CC dimerizes to  DiL12CC
	reaction_r1307=global_par_kbDIM*x(113)*x(9);

% Reaction: id = r1308, name = RL12LG dimerizes to  DiL12LG
	reaction_r1308=global_par_kbDIM*x(114)*x(9);

% Reaction: id = r1309, name = RL00UU dimerizes to  DaL00UU
	reaction_r1309=global_par_kbDIM*x(89)*x(10);

% Reaction: id = r1310, name = RL10UU dimerizes to  DaL10UU
	reaction_r1310=global_par_kbDIM*x(90)*x(10);

% Reaction: id = r1311, name = RL10CU dimerizes to  DaL10CU
	reaction_r1311=global_par_kbDIM*x(91)*x(10);

% Reaction: id = r1312, name = RL10LU dimerizes to  DaL10LU
	reaction_r1312=global_par_kbDIM*x(92)*x(10);

% Reaction: id = r1313, name = RL01UU dimerizes to  DaL01UU
	reaction_r1313=global_par_kbDIM*x(93)*x(10);

% Reaction: id = r1314, name = RL01UG dimerizes to  DaL01UG
	reaction_r1314=global_par_kbDIM*x(94)*x(10);

% Reaction: id = r1315, name = RL01UL dimerizes to  DaL01UL
	reaction_r1315=global_par_kbDIM*x(95)*x(10);

% Reaction: id = r1316, name = RL11UU dimerizes to  DaL11UU
	reaction_r1316=global_par_kbDIM*x(96)*x(10);

% Reaction: id = r1317, name = RL11CU dimerizes to  DaL11CU
	reaction_r1317=global_par_kbDIM*x(97)*x(10);

% Reaction: id = r1318, name = RL11LU dimerizes to  DaL11LU
	reaction_r1318=global_par_kbDIM*x(98)*x(10);

% Reaction: id = r1319, name = RL11UG dimerizes to  DaL11UG
	reaction_r1319=global_par_kbDIM*x(99)*x(10);

% Reaction: id = r1320, name = RL11UL dimerizes to  DaL11UL
	reaction_r1320=global_par_kbDIM*x(100)*x(10);

% Reaction: id = r1321, name = RL11CG dimerizes to  DaL11CG
	reaction_r1321=global_par_kbDIM*x(101)*x(10);

% Reaction: id = r1322, name = RL11CC dimerizes to  DaL11CC
	reaction_r1322=global_par_kbDIM*x(102)*x(10);

% Reaction: id = r1323, name = RL11LG dimerizes to  DaL11LG
	reaction_r1323=global_par_kbDIM*x(103)*x(10);

% Reaction: id = r1324, name = RL02UU dimerizes to  DaL02UU
	reaction_r1324=global_par_kbDIM*x(104)*x(10);

% Reaction: id = r1325, name = RL02UG dimerizes to  DaL02UG
	reaction_r1325=global_par_kbDIM*x(105)*x(10);

% Reaction: id = r1326, name = RL02UL dimerizes to  DaL02UL
	reaction_r1326=global_par_kbDIM*x(106)*x(10);

% Reaction: id = r1327, name = RL12UU dimerizes to  DaL12UU
	reaction_r1327=global_par_kbDIM*x(107)*x(10);

% Reaction: id = r1328, name = RL12CU dimerizes to  DaL12CU
	reaction_r1328=global_par_kbDIM*x(108)*x(10);

% Reaction: id = r1329, name = RL12LU dimerizes to  DaL12LU
	reaction_r1329=global_par_kbDIM*x(109)*x(10);

% Reaction: id = r1330, name = RL12UG dimerizes to  DaL12UG
	reaction_r1330=global_par_kbDIM*x(110)*x(10);

% Reaction: id = r1331, name = RL12UL dimerizes to  DaL12UL
	reaction_r1331=global_par_kbDIM*x(111)*x(10);

% Reaction: id = r1332, name = RL12CG dimerizes to  DaL12CG
	reaction_r1332=global_par_kbDIM*x(112)*x(10);

% Reaction: id = r1333, name = RL12CC dimerizes to  DaL12CC
	reaction_r1333=global_par_kbDIM*x(113)*x(10);

% Reaction: id = r1334, name = RL12LG dimerizes to  DaL12LG
	reaction_r1334=global_par_kbDIM*x(114)*x(10);

% Reaction: id = r1335, name = Di00UU loses partner
	reaction_r1335=global_par_kuDIM*x(115);

% Reaction: id = r1336, name = Di10UU loses partner
	reaction_r1336=global_par_kuDIM*x(116);

% Reaction: id = r1337, name = Di10CU loses partner
	reaction_r1337=global_par_kuDIM*x(117);

% Reaction: id = r1338, name = Di10LU loses partner
	reaction_r1338=global_par_kuDIM*x(118);

% Reaction: id = r1339, name = Di01UU loses partner
	reaction_r1339=global_par_kuDIM*x(119);

% Reaction: id = r1340, name = Di01UG loses partner
	reaction_r1340=global_par_kuDIM*x(120);

% Reaction: id = r1341, name = Di01UL loses partner
	reaction_r1341=global_par_kuDIM*x(121);

% Reaction: id = r1342, name = Di11UU loses partner
	reaction_r1342=global_par_kuDIM*x(122);

% Reaction: id = r1343, name = Di11CU loses partner
	reaction_r1343=global_par_kuDIM*x(123);

% Reaction: id = r1344, name = Di11LU loses partner
	reaction_r1344=global_par_kuDIM*x(124);

% Reaction: id = r1345, name = Di11UG loses partner
	reaction_r1345=global_par_kuDIM*x(125);

% Reaction: id = r1346, name = Di11UL loses partner
	reaction_r1346=global_par_kuDIM*x(126);

% Reaction: id = r1347, name = Di11CG loses partner
	reaction_r1347=global_par_kuDIM*x(127);

% Reaction: id = r1348, name = Di11CC loses partner
	reaction_r1348=global_par_kuDIM*x(128);

% Reaction: id = r1349, name = Di11LG loses partner
	reaction_r1349=global_par_kuDIM*x(129);

% Reaction: id = r1350, name = Di02UU loses partner
	reaction_r1350=global_par_kuDIM*x(130);

% Reaction: id = r1351, name = Di02UG loses partner
	reaction_r1351=global_par_kuDIM*x(131);

% Reaction: id = r1352, name = Di02UL loses partner
	reaction_r1352=global_par_kuDIM*x(132);

% Reaction: id = r1353, name = Di12UU loses partner
	reaction_r1353=global_par_kuDIM*x(133);

% Reaction: id = r1354, name = Di12CU loses partner
	reaction_r1354=global_par_kuDIM*x(134);

% Reaction: id = r1355, name = Di12LU loses partner
	reaction_r1355=global_par_kuDIM*x(135);

% Reaction: id = r1356, name = Di12UG loses partner
	reaction_r1356=global_par_kuDIM*x(136);

% Reaction: id = r1357, name = Di12UL loses partner
	reaction_r1357=global_par_kuDIM*x(137);

% Reaction: id = r1358, name = Di12CG loses partner
	reaction_r1358=global_par_kuDIM*x(138);

% Reaction: id = r1359, name = Di12CC loses partner
	reaction_r1359=global_par_kuDIM*x(139);

% Reaction: id = r1360, name = Di12LG loses partner
	reaction_r1360=global_par_kuDIM*x(140);

% Reaction: id = r1361, name = DiL00UU loses partner
	reaction_r1361=global_par_kuDIM*x(167);

% Reaction: id = r1362, name = DiL10UU loses partner
	reaction_r1362=global_par_kuDIM*x(168);

% Reaction: id = r1363, name = DiL10CU loses partner
	reaction_r1363=global_par_kuDIM*x(169);

% Reaction: id = r1364, name = DiL10LU loses partner
	reaction_r1364=global_par_kuDIM*x(170);

% Reaction: id = r1365, name = DiL01UU loses partner
	reaction_r1365=global_par_kuDIM*x(171);

% Reaction: id = r1366, name = DiL01UG loses partner
	reaction_r1366=global_par_kuDIM*x(172);

% Reaction: id = r1367, name = DiL01UL loses partner
	reaction_r1367=global_par_kuDIM*x(173);

% Reaction: id = r1368, name = DiL11UU loses partner
	reaction_r1368=global_par_kuDIM*x(174);

% Reaction: id = r1369, name = DiL11CU loses partner
	reaction_r1369=global_par_kuDIM*x(175);

% Reaction: id = r1370, name = DiL11LU loses partner
	reaction_r1370=global_par_kuDIM*x(176);

% Reaction: id = r1371, name = DiL11UG loses partner
	reaction_r1371=global_par_kuDIM*x(177);

% Reaction: id = r1372, name = DiL11UL loses partner
	reaction_r1372=global_par_kuDIM*x(178);

% Reaction: id = r1373, name = DiL11CG loses partner
	reaction_r1373=global_par_kuDIM*x(179);

% Reaction: id = r1374, name = DiL11CC loses partner
	reaction_r1374=global_par_kuDIM*x(180);

% Reaction: id = r1375, name = DiL11LG loses partner
	reaction_r1375=global_par_kuDIM*x(181);

% Reaction: id = r1376, name = DiL02UU loses partner
	reaction_r1376=global_par_kuDIM*x(182);

% Reaction: id = r1377, name = DiL02UG loses partner
	reaction_r1377=global_par_kuDIM*x(183);

% Reaction: id = r1378, name = DiL02UL loses partner
	reaction_r1378=global_par_kuDIM*x(184);

% Reaction: id = r1379, name = DiL12UU loses partner
	reaction_r1379=global_par_kuDIM*x(185);

% Reaction: id = r1380, name = DiL12CU loses partner
	reaction_r1380=global_par_kuDIM*x(186);

% Reaction: id = r1381, name = DiL12LU loses partner
	reaction_r1381=global_par_kuDIM*x(187);

% Reaction: id = r1382, name = DiL12UG loses partner
	reaction_r1382=global_par_kuDIM*x(188);

% Reaction: id = r1383, name = DiL12UL loses partner
	reaction_r1383=global_par_kuDIM*x(189);

% Reaction: id = r1384, name = DiL12CG loses partner
	reaction_r1384=global_par_kuDIM*x(190);

% Reaction: id = r1385, name = DiL12CC loses partner
	reaction_r1385=global_par_kuDIM*x(191);

% Reaction: id = r1386, name = DiL12LG loses partner
	reaction_r1386=global_par_kuDIM*x(192);

% Reaction: id = r1387, name = Da00UU loses partner
	reaction_r1387=global_par_kuDIM*x(141);

% Reaction: id = r1388, name = Da10UU loses partner
	reaction_r1388=global_par_kuDIM*x(142);

% Reaction: id = r1389, name = Da10CU loses partner
	reaction_r1389=global_par_kuDIM*x(143);

% Reaction: id = r1390, name = Da10LU loses partner
	reaction_r1390=global_par_kuDIM*x(144);

% Reaction: id = r1391, name = Da01UU loses partner
	reaction_r1391=global_par_kuDIM*x(145);

% Reaction: id = r1392, name = Da01UG loses partner
	reaction_r1392=global_par_kuDIM*x(146);

% Reaction: id = r1393, name = Da01UL loses partner
	reaction_r1393=global_par_kuDIM*x(147);

% Reaction: id = r1394, name = Da11UU loses partner
	reaction_r1394=global_par_kuDIM*x(148);

% Reaction: id = r1395, name = Da11CU loses partner
	reaction_r1395=global_par_kuDIM*x(149);

% Reaction: id = r1396, name = Da11LU loses partner
	reaction_r1396=global_par_kuDIM*x(150);

% Reaction: id = r1397, name = Da11UG loses partner
	reaction_r1397=global_par_kuDIM*x(151);

% Reaction: id = r1398, name = Da11UL loses partner
	reaction_r1398=global_par_kuDIM*x(152);

% Reaction: id = r1399, name = Da11CG loses partner
	reaction_r1399=global_par_kuDIM*x(153);

% Reaction: id = r1400, name = Da11CC loses partner
	reaction_r1400=global_par_kuDIM*x(154);

% Reaction: id = r1401, name = Da11LG loses partner
	reaction_r1401=global_par_kuDIM*x(155);

% Reaction: id = r1402, name = Da02UU loses partner
	reaction_r1402=global_par_kuDIM*x(156);

% Reaction: id = r1403, name = Da02UG loses partner
	reaction_r1403=global_par_kuDIM*x(157);

% Reaction: id = r1404, name = Da02UL loses partner
	reaction_r1404=global_par_kuDIM*x(158);

% Reaction: id = r1405, name = Da12UU loses partner
	reaction_r1405=global_par_kuDIM*x(159);

% Reaction: id = r1406, name = Da12CU loses partner
	reaction_r1406=global_par_kuDIM*x(160);

% Reaction: id = r1407, name = Da12LU loses partner
	reaction_r1407=global_par_kuDIM*x(161);

% Reaction: id = r1408, name = Da12UG loses partner
	reaction_r1408=global_par_kuDIM*x(162);

% Reaction: id = r1409, name = Da12UL loses partner
	reaction_r1409=global_par_kuDIM*x(163);

% Reaction: id = r1410, name = Da12CG loses partner
	reaction_r1410=global_par_kuDIM*x(164);

% Reaction: id = r1411, name = Da12CC loses partner
	reaction_r1411=global_par_kuDIM*x(165);

% Reaction: id = r1412, name = Da12LG loses partner
	reaction_r1412=global_par_kuDIM*x(166);

% Reaction: id = r1413, name = DaL00UU loses partner
	reaction_r1413=global_par_kuDIM*x(193);

% Reaction: id = r1414, name = DaL10UU loses partner
	reaction_r1414=global_par_kuDIM*x(194);

% Reaction: id = r1415, name = DaL10CU loses partner
	reaction_r1415=global_par_kuDIM*x(195);

% Reaction: id = r1416, name = DaL10LU loses partner
	reaction_r1416=global_par_kuDIM*x(196);

% Reaction: id = r1417, name = DaL01UU loses partner
	reaction_r1417=global_par_kuDIM*x(197);

% Reaction: id = r1418, name = DaL01UG loses partner
	reaction_r1418=global_par_kuDIM*x(198);

% Reaction: id = r1419, name = DaL01UL loses partner
	reaction_r1419=global_par_kuDIM*x(199);

% Reaction: id = r1420, name = DaL11UU loses partner
	reaction_r1420=global_par_kuDIM*x(200);

% Reaction: id = r1421, name = DaL11CU loses partner
	reaction_r1421=global_par_kuDIM*x(201);

% Reaction: id = r1422, name = DaL11LU loses partner
	reaction_r1422=global_par_kuDIM*x(202);

% Reaction: id = r1423, name = DaL11UG loses partner
	reaction_r1423=global_par_kuDIM*x(203);

% Reaction: id = r1424, name = DaL11UL loses partner
	reaction_r1424=global_par_kuDIM*x(204);

% Reaction: id = r1425, name = DaL11CG loses partner
	reaction_r1425=global_par_kuDIM*x(205);

% Reaction: id = r1426, name = DaL11CC loses partner
	reaction_r1426=global_par_kuDIM*x(206);

% Reaction: id = r1427, name = DaL11LG loses partner
	reaction_r1427=global_par_kuDIM*x(207);

% Reaction: id = r1428, name = DaL02UU loses partner
	reaction_r1428=global_par_kuDIM*x(208);

% Reaction: id = r1429, name = DaL02UG loses partner
	reaction_r1429=global_par_kuDIM*x(209);

% Reaction: id = r1430, name = DaL02UL loses partner
	reaction_r1430=global_par_kuDIM*x(210);

% Reaction: id = r1431, name = DaL12UU loses partner
	reaction_r1431=global_par_kuDIM*x(211);

% Reaction: id = r1432, name = DaL12CU loses partner
	reaction_r1432=global_par_kuDIM*x(212);

% Reaction: id = r1433, name = DaL12LU loses partner
	reaction_r1433=global_par_kuDIM*x(213);

% Reaction: id = r1434, name = DaL12UG loses partner
	reaction_r1434=global_par_kuDIM*x(214);

% Reaction: id = r1435, name = DaL12UL loses partner
	reaction_r1435=global_par_kuDIM*x(215);

% Reaction: id = r1436, name = DaL12CG loses partner
	reaction_r1436=global_par_kuDIM*x(216);

% Reaction: id = r1437, name = DaL12CC loses partner
	reaction_r1437=global_par_kuDIM*x(217);

% Reaction: id = r1438, name = DaL12LG loses partner
	reaction_r1438=global_par_kuDIM*x(218);

% Reaction: id = r1439, name = R00UU extra homodimerizes to  Di00UU
	reaction_r1439=global_par_kbDIM*x(63)*x(63);

% Reaction: id = r1440, name = R10UU extra homodimerizes to  Di10UU
	reaction_r1440=global_par_kbDIM*x(64)*x(64);

% Reaction: id = r1441, name = R10CU extra homodimerizes to  Di10CU
	reaction_r1441=global_par_kbDIM*x(65)*x(65);

% Reaction: id = r1442, name = R10LU extra homodimerizes to  Di10LU
	reaction_r1442=global_par_kbDIM*x(66)*x(66);

% Reaction: id = r1443, name = R01UU extra homodimerizes to  Di01UU
	reaction_r1443=global_par_kbDIM*x(67)*x(67);

% Reaction: id = r1444, name = R01UG extra homodimerizes to  Di01UG
	reaction_r1444=global_par_kbDIM*x(68)*x(68);

% Reaction: id = r1445, name = R01UL extra homodimerizes to  Di01UL
	reaction_r1445=global_par_kbDIM*x(69)*x(69);

% Reaction: id = r1446, name = R11UU extra homodimerizes to  Di11UU
	reaction_r1446=global_par_kbDIM*x(70)*x(70);

% Reaction: id = r1447, name = R11CU extra homodimerizes to  Di11CU
	reaction_r1447=global_par_kbDIM*x(71)*x(71);

% Reaction: id = r1448, name = R11LU extra homodimerizes to  Di11LU
	reaction_r1448=global_par_kbDIM*x(72)*x(72);

% Reaction: id = r1449, name = R11UG extra homodimerizes to  Di11UG
	reaction_r1449=global_par_kbDIM*x(73)*x(73);

% Reaction: id = r1450, name = R11UL extra homodimerizes to  Di11UL
	reaction_r1450=global_par_kbDIM*x(74)*x(74);

% Reaction: id = r1451, name = R11CG extra homodimerizes to  Di11CG
	reaction_r1451=global_par_kbDIM*x(75)*x(75);

% Reaction: id = r1452, name = R11CC extra homodimerizes to  Di11CC
	reaction_r1452=global_par_kbDIM*x(76)*x(76);

% Reaction: id = r1453, name = R11LG extra homodimerizes to  Di11LG
	reaction_r1453=global_par_kbDIM*x(77)*x(77);

% Reaction: id = r1454, name = R02UU extra homodimerizes to  Di02UU
	reaction_r1454=global_par_kbDIM*x(78)*x(78);

% Reaction: id = r1455, name = R02UG extra homodimerizes to  Di02UG
	reaction_r1455=global_par_kbDIM*x(79)*x(79);

% Reaction: id = r1456, name = R02UL extra homodimerizes to  Di02UL
	reaction_r1456=global_par_kbDIM*x(80)*x(80);

% Reaction: id = r1457, name = R12UU extra homodimerizes to  Di12UU
	reaction_r1457=global_par_kbDIM*x(81)*x(81);

% Reaction: id = r1458, name = R12CU extra homodimerizes to  Di12CU
	reaction_r1458=global_par_kbDIM*x(82)*x(82);

% Reaction: id = r1459, name = R12LU extra homodimerizes to  Di12LU
	reaction_r1459=global_par_kbDIM*x(83)*x(83);

% Reaction: id = r1460, name = R12UG extra homodimerizes to  Di12UG
	reaction_r1460=global_par_kbDIM*x(84)*x(84);

% Reaction: id = r1461, name = R12UL extra homodimerizes to  Di12UL
	reaction_r1461=global_par_kbDIM*x(85)*x(85);

% Reaction: id = r1462, name = R12CG extra homodimerizes to  Di12CG
	reaction_r1462=global_par_kbDIM*x(86)*x(86);

% Reaction: id = r1463, name = R12CC extra homodimerizes to  Di12CC
	reaction_r1463=global_par_kbDIM*x(87)*x(87);

% Reaction: id = r1464, name = R12LG extra homodimerizes to  Di12LG
	reaction_r1464=global_par_kbDIM*x(88)*x(88);

% Reaction: id = r1465, name = RL00UU extra homodimerizes to  DaL00UU
	reaction_r1465=global_par_kbDIM*x(89)*x(89);

% Reaction: id = r1466, name = RL10UU extra homodimerizes to  DaL10UU
	reaction_r1466=global_par_kbDIM*x(90)*x(90);

% Reaction: id = r1467, name = RL10CU extra homodimerizes to  DaL10CU
	reaction_r1467=global_par_kbDIM*x(91)*x(91);

% Reaction: id = r1468, name = RL10LU extra homodimerizes to  DaL10LU
	reaction_r1468=global_par_kbDIM*x(92)*x(92);

% Reaction: id = r1469, name = RL01UU extra homodimerizes to  DaL01UU
	reaction_r1469=global_par_kbDIM*x(93)*x(93);

% Reaction: id = r1470, name = RL01UG extra homodimerizes to  DaL01UG
	reaction_r1470=global_par_kbDIM*x(94)*x(94);

% Reaction: id = r1471, name = RL01UL extra homodimerizes to  DaL01UL
	reaction_r1471=global_par_kbDIM*x(95)*x(95);

% Reaction: id = r1472, name = RL11UU extra homodimerizes to  DaL11UU
	reaction_r1472=global_par_kbDIM*x(96)*x(96);

% Reaction: id = r1473, name = RL11CU extra homodimerizes to  DaL11CU
	reaction_r1473=global_par_kbDIM*x(97)*x(97);

% Reaction: id = r1474, name = RL11LU extra homodimerizes to  DaL11LU
	reaction_r1474=global_par_kbDIM*x(98)*x(98);

% Reaction: id = r1475, name = RL11UG extra homodimerizes to  DaL11UG
	reaction_r1475=global_par_kbDIM*x(99)*x(99);

% Reaction: id = r1476, name = RL11UL extra homodimerizes to  DaL11UL
	reaction_r1476=global_par_kbDIM*x(100)*x(100);

% Reaction: id = r1477, name = RL11CG extra homodimerizes to  DaL11CG
	reaction_r1477=global_par_kbDIM*x(101)*x(101);

% Reaction: id = r1478, name = RL11CC extra homodimerizes to  DaL11CC
	reaction_r1478=global_par_kbDIM*x(102)*x(102);

% Reaction: id = r1479, name = RL11LG extra homodimerizes to  DaL11LG
	reaction_r1479=global_par_kbDIM*x(103)*x(103);

% Reaction: id = r1480, name = RL02UU extra homodimerizes to  DaL02UU
	reaction_r1480=global_par_kbDIM*x(104)*x(104);

% Reaction: id = r1481, name = RL02UG extra homodimerizes to  DaL02UG
	reaction_r1481=global_par_kbDIM*x(105)*x(105);

% Reaction: id = r1482, name = RL02UL extra homodimerizes to  DaL02UL
	reaction_r1482=global_par_kbDIM*x(106)*x(106);

% Reaction: id = r1483, name = RL12UU extra homodimerizes to  DaL12UU
	reaction_r1483=global_par_kbDIM*x(107)*x(107);

% Reaction: id = r1484, name = RL12CU extra homodimerizes to  DaL12CU
	reaction_r1484=global_par_kbDIM*x(108)*x(108);

% Reaction: id = r1485, name = RL12LU extra homodimerizes to  DaL12LU
	reaction_r1485=global_par_kbDIM*x(109)*x(109);

% Reaction: id = r1486, name = RL12UG extra homodimerizes to  DaL12UG
	reaction_r1486=global_par_kbDIM*x(110)*x(110);

% Reaction: id = r1487, name = RL12UL extra homodimerizes to  DaL12UL
	reaction_r1487=global_par_kbDIM*x(111)*x(111);

% Reaction: id = r1488, name = RL12CG extra homodimerizes to  DaL12CG
	reaction_r1488=global_par_kbDIM*x(112)*x(112);

% Reaction: id = r1489, name = RL12CC extra homodimerizes to  DaL12CC
	reaction_r1489=global_par_kbDIM*x(113)*x(113);

% Reaction: id = r1490, name = RL12LG extra homodimerizes to  DaL12LG
	reaction_r1490=global_par_kbDIM*x(114)*x(114);

	xdot=zeros(218,1);
	
% Species:   id = L, name = L, affected by kineticLaw
	xdot(1) = (1/(compartment_cell))*((-1.0 * reaction_r919) + (-1.0 * reaction_r920) + (-1.0 * reaction_r921) + (-1.0 * reaction_r922) + (-1.0 * reaction_r923) + (-1.0 * reaction_r924) + (-1.0 * reaction_r925) + (-1.0 * reaction_r926) + (-1.0 * reaction_r927) + (-1.0 * reaction_r928) + (-1.0 * reaction_r929) + (-1.0 * reaction_r930) + (-1.0 * reaction_r931) + (-1.0 * reaction_r932) + (-1.0 * reaction_r933) + (-1.0 * reaction_r934) + (-1.0 * reaction_r935) + (-1.0 * reaction_r936) + (-1.0 * reaction_r937) + (-1.0 * reaction_r938) + (-1.0 * reaction_r939) + (-1.0 * reaction_r940) + (-1.0 * reaction_r941) + (-1.0 * reaction_r942) + (-1.0 * reaction_r943) + (-1.0 * reaction_r944) + (-1.0 * reaction_r945) + (-1.0 * reaction_r946) + (-1.0 * reaction_r947) + (-1.0 * reaction_r948) + (-1.0 * reaction_r949) + (-1.0 * reaction_r950) + (-1.0 * reaction_r951) + (-1.0 * reaction_r952) + (-1.0 * reaction_r953) + (-1.0 * reaction_r954) + (-1.0 * reaction_r955) + (-1.0 * reaction_r956) + (-1.0 * reaction_r957) + (-1.0 * reaction_r958) + (-1.0 * reaction_r959) + (-1.0 * reaction_r960) + (-1.0 * reaction_r961) + (-1.0 * reaction_r962) + (-1.0 * reaction_r963) + (-1.0 * reaction_r964) + (-1.0 * reaction_r965) + (-1.0 * reaction_r966) + (-1.0 * reaction_r967) + (-1.0 * reaction_r968) + (-1.0 * reaction_r969) + (-1.0 * reaction_r970) + (-1.0 * reaction_r997) + (-1.0 * reaction_r998) + (-1.0 * reaction_r999) + (-1.0 * reaction_r1000) + (-1.0 * reaction_r1001) + (-1.0 * reaction_r1002) + (-1.0 * reaction_r1003) + (-1.0 * reaction_r1004) + (-1.0 * reaction_r1005) + (-1.0 * reaction_r1006) + (-1.0 * reaction_r1007) + (-1.0 * reaction_r1008) + (-1.0 * reaction_r1009) + (-1.0 * reaction_r1010) + (-1.0 * reaction_r1011) + (-1.0 * reaction_r1012) + (-1.0 * reaction_r1013) + (-1.0 * reaction_r1014) + (-1.0 * reaction_r1015) + (-1.0 * reaction_r1016) + (-1.0 * reaction_r1017) + (-1.0 * reaction_r1018) + (-1.0 * reaction_r1019) + (-1.0 * reaction_r1020) + (-1.0 * reaction_r1021) + (-1.0 * reaction_r1022) + (-1.0 * reaction_r1023) + (-1.0 * reaction_r1024) + (-1.0 * reaction_r1025) + (-1.0 * reaction_r1026) + (-1.0 * reaction_r1027) + (-1.0 * reaction_r1028) + (-1.0 * reaction_r1029) + (-1.0 * reaction_r1030) + (-1.0 * reaction_r1031) + (-1.0 * reaction_r1032) + (-1.0 * reaction_r1033) + (-1.0 * reaction_r1034) + (-1.0 * reaction_r1035) + (-1.0 * reaction_r1036) + (-1.0 * reaction_r1037) + (-1.0 * reaction_r1038) + (-1.0 * reaction_r1039) + (-1.0 * reaction_r1040) + (-1.0 * reaction_r1041) + (-1.0 * reaction_r1042) + (-1.0 * reaction_r1043) + (-1.0 * reaction_r1044) + (-1.0 * reaction_r1045) + (-1.0 * reaction_r1046) + (-1.0 * reaction_r1047) + (-1.0 * reaction_r1048) + ( 1.0 * reaction_r1075) + ( 1.0 * reaction_r1076) + ( 1.0 * reaction_r1077) + ( 1.0 * reaction_r1078) + ( 1.0 * reaction_r1079) + ( 1.0 * reaction_r1080) + ( 1.0 * reaction_r1081) + ( 1.0 * reaction_r1082) + ( 1.0 * reaction_r1083) + ( 1.0 * reaction_r1084) + ( 1.0 * reaction_r1085) + ( 1.0 * reaction_r1086) + ( 1.0 * reaction_r1087) + ( 1.0 * reaction_r1088) + ( 1.0 * reaction_r1089) + ( 1.0 * reaction_r1090) + ( 1.0 * reaction_r1091) + ( 1.0 * reaction_r1092) + ( 1.0 * reaction_r1093) + ( 1.0 * reaction_r1094) + ( 1.0 * reaction_r1095) + ( 1.0 * reaction_r1096) + ( 1.0 * reaction_r1097) + ( 1.0 * reaction_r1098) + ( 1.0 * reaction_r1099) + ( 1.0 * reaction_r1100) + ( 1.0 * reaction_r1101) + ( 1.0 * reaction_r1102) + ( 1.0 * reaction_r1103) + ( 1.0 * reaction_r1104) + ( 1.0 * reaction_r1105) + ( 1.0 * reaction_r1106) + ( 1.0 * reaction_r1107) + ( 1.0 * reaction_r1108) + ( 1.0 * reaction_r1109) + ( 1.0 * reaction_r1110) + ( 1.0 * reaction_r1111) + ( 1.0 * reaction_r1112) + ( 1.0 * reaction_r1113) + ( 1.0 * reaction_r1114) + ( 1.0 * reaction_r1115) + ( 1.0 * reaction_r1116) + ( 1.0 * reaction_r1117) + ( 1.0 * reaction_r1118) + ( 1.0 * reaction_r1119) + ( 1.0 * reaction_r1120) + ( 1.0 * reaction_r1121) + ( 1.0 * reaction_r1122) + ( 1.0 * reaction_r1123) + ( 1.0 * reaction_r1124) + ( 1.0 * reaction_r1125) + ( 1.0 * reaction_r1126) + ( 1.0 * reaction_r1153) + ( 1.0 * reaction_r1154) + ( 1.0 * reaction_r1155) + ( 1.0 * reaction_r1156) + ( 1.0 * reaction_r1157) + ( 1.0 * reaction_r1158) + ( 1.0 * reaction_r1159) + ( 1.0 * reaction_r1160) + ( 1.0 * reaction_r1161) + ( 1.0 * reaction_r1162) + ( 1.0 * reaction_r1163) + ( 1.0 * reaction_r1164) + ( 1.0 * reaction_r1165) + ( 1.0 * reaction_r1166) + ( 1.0 * reaction_r1167) + ( 1.0 * reaction_r1168) + ( 1.0 * reaction_r1169) + ( 1.0 * reaction_r1170) + ( 1.0 * reaction_r1171) + ( 1.0 * reaction_r1172) + ( 1.0 * reaction_r1173) + ( 1.0 * reaction_r1174) + ( 1.0 * reaction_r1175) + ( 1.0 * reaction_r1176) + ( 1.0 * reaction_r1177) + ( 1.0 * reaction_r1178) + ( 1.0 * reaction_r1205) + ( 1.0 * reaction_r1206) + ( 1.0 * reaction_r1207) + ( 1.0 * reaction_r1208) + ( 1.0 * reaction_r1209) + ( 1.0 * reaction_r1210) + ( 1.0 * reaction_r1211) + ( 1.0 * reaction_r1212) + ( 1.0 * reaction_r1213) + ( 1.0 * reaction_r1214) + ( 1.0 * reaction_r1215) + ( 1.0 * reaction_r1216) + ( 1.0 * reaction_r1217) + ( 1.0 * reaction_r1218) + ( 1.0 * reaction_r1219) + ( 1.0 * reaction_r1220) + ( 1.0 * reaction_r1221) + ( 1.0 * reaction_r1222) + ( 1.0 * reaction_r1223) + ( 1.0 * reaction_r1224) + ( 1.0 * reaction_r1225) + ( 1.0 * reaction_r1226) + ( 1.0 * reaction_r1227) + ( 1.0 * reaction_r1228) + ( 1.0 * reaction_r1229) + ( 1.0 * reaction_r1230));
	
% Species:   id = Cbl, name = Cbl, affected by kineticLaw
	xdot(2) = (1/(compartment_cell))*((-1.0 * reaction_r221) + ( 1.0 * reaction_r222) + (-1.0 * reaction_r223) + ( 1.0 * reaction_r224) + (-1.0 * reaction_r233) + ( 1.0 * reaction_r234) + (-1.0 * reaction_r235) + ( 1.0 * reaction_r236) + (-1.0 * reaction_r251) + ( 1.0 * reaction_r252) + (-1.0 * reaction_r255) + ( 1.0 * reaction_r256) + (-1.0 * reaction_r267) + ( 1.0 * reaction_r268) + (-1.0 * reaction_r269) + ( 1.0 * reaction_r270) + (-1.0 * reaction_r285) + ( 1.0 * reaction_r286) + (-1.0 * reaction_r289) + ( 1.0 * reaction_r290) + (-1.0 * reaction_r297) + ( 1.0 * reaction_r298) + (-1.0 * reaction_r307) + ( 1.0 * reaction_r308) + (-1.0 * reaction_r309) + ( 1.0 * reaction_r310) + (-1.0 * reaction_r325) + ( 1.0 * reaction_r326) + (-1.0 * reaction_r329) + ( 1.0 * reaction_r330) + (-1.0 * reaction_r341) + ( 1.0 * reaction_r342) + (-1.0 * reaction_r343) + ( 1.0 * reaction_r344) + (-1.0 * reaction_r359) + ( 1.0 * reaction_r360) + (-1.0 * reaction_r363) + ( 1.0 * reaction_r364) + (-1.0 * reaction_r371) + ( 1.0 * reaction_r372) + (-1.0 * reaction_r381) + ( 1.0 * reaction_r382) + (-1.0 * reaction_r383) + ( 1.0 * reaction_r384) + (-1.0 * reaction_r399) + ( 1.0 * reaction_r400) + (-1.0 * reaction_r403) + ( 1.0 * reaction_r404) + (-1.0 * reaction_r415) + ( 1.0 * reaction_r416) + (-1.0 * reaction_r417) + ( 1.0 * reaction_r418) + (-1.0 * reaction_r433) + ( 1.0 * reaction_r434) + (-1.0 * reaction_r437) + ( 1.0 * reaction_r438) + (-1.0 * reaction_r445) + ( 1.0 * reaction_r446) + (-1.0 * reaction_r455) + ( 1.0 * reaction_r456) + (-1.0 * reaction_r457) + ( 1.0 * reaction_r458) + (-1.0 * reaction_r473) + ( 1.0 * reaction_r474) + (-1.0 * reaction_r477) + ( 1.0 * reaction_r478) + (-1.0 * reaction_r489) + ( 1.0 * reaction_r490) + (-1.0 * reaction_r491) + ( 1.0 * reaction_r492) + (-1.0 * reaction_r507) + ( 1.0 * reaction_r508) + (-1.0 * reaction_r511) + ( 1.0 * reaction_r512) + (-1.0 * reaction_r519) + ( 1.0 * reaction_r520) + (-1.0 * reaction_r529) + ( 1.0 * reaction_r530) + (-1.0 * reaction_r531) + ( 1.0 * reaction_r532) + (-1.0 * reaction_r547) + ( 1.0 * reaction_r548) + (-1.0 * reaction_r551) + ( 1.0 * reaction_r552) + (-1.0 * reaction_r563) + ( 1.0 * reaction_r564) + (-1.0 * reaction_r565) + ( 1.0 * reaction_r566) + (-1.0 * reaction_r581) + ( 1.0 * reaction_r582) + (-1.0 * reaction_r585) + ( 1.0 * reaction_r586) + (-1.0 * reaction_r593) + ( 1.0 * reaction_r594) + (-1.0 * reaction_r603) + ( 1.0 * reaction_r604) + (-1.0 * reaction_r605) + ( 1.0 * reaction_r606) + (-1.0 * reaction_r621) + ( 1.0 * reaction_r622) + (-1.0 * reaction_r625) + ( 1.0 * reaction_r626) + (-1.0 * reaction_r637) + ( 1.0 * reaction_r638) + (-1.0 * reaction_r639) + ( 1.0 * reaction_r640) + (-1.0 * reaction_r655) + ( 1.0 * reaction_r656) + (-1.0 * reaction_r659) + ( 1.0 * reaction_r660) + (-1.0 * reaction_r667) + ( 1.0 * reaction_r668) + (-1.0 * reaction_r677) + ( 1.0 * reaction_r678) + (-1.0 * reaction_r679) + ( 1.0 * reaction_r680) + (-1.0 * reaction_r695) + ( 1.0 * reaction_r696) + (-1.0 * reaction_r699) + ( 1.0 * reaction_r700) + (-1.0 * reaction_r711) + ( 1.0 * reaction_r712) + (-1.0 * reaction_r713) + ( 1.0 * reaction_r714) + (-1.0 * reaction_r729) + ( 1.0 * reaction_r730) + (-1.0 * reaction_r733) + ( 1.0 * reaction_r734) + (-1.0 * reaction_r741) + ( 1.0 * reaction_r742) + (-1.0 * reaction_r751) + ( 1.0 * reaction_r752) + (-1.0 * reaction_r753) + ( 1.0 * reaction_r754) + (-1.0 * reaction_r769) + ( 1.0 * reaction_r770) + (-1.0 * reaction_r773) + ( 1.0 * reaction_r774) + (-1.0 * reaction_r785) + ( 1.0 * reaction_r786) + (-1.0 * reaction_r787) + ( 1.0 * reaction_r788) + (-1.0 * reaction_r803) + ( 1.0 * reaction_r804) + (-1.0 * reaction_r807) + ( 1.0 * reaction_r808));
	
% Species:   id = Grb2, name = Grb2, affected by kineticLaw
	xdot(3) = (1/(compartment_cell))*((-1.0 * reaction_r221) + ( 1.0 * reaction_r222) + (-1.0 * reaction_r227) + ( 1.0 * reaction_r228) + (-1.0 * reaction_r229) + ( 1.0 * reaction_r230) + (-1.0 * reaction_r239) + ( 1.0 * reaction_r240) + (-1.0 * reaction_r243) + ( 1.0 * reaction_r244) + (-1.0 * reaction_r245) + ( 1.0 * reaction_r246) + (-1.0 * reaction_r247) + ( 1.0 * reaction_r248) + (-1.0 * reaction_r261) + ( 1.0 * reaction_r262) + (-1.0 * reaction_r263) + ( 1.0 * reaction_r264) + (-1.0 * reaction_r273) + ( 1.0 * reaction_r274) + (-1.0 * reaction_r277) + ( 1.0 * reaction_r278) + (-1.0 * reaction_r279) + ( 1.0 * reaction_r280) + (-1.0 * reaction_r281) + ( 1.0 * reaction_r282) + (-1.0 * reaction_r295) + ( 1.0 * reaction_r296) + (-1.0 * reaction_r301) + ( 1.0 * reaction_r302) + (-1.0 * reaction_r303) + ( 1.0 * reaction_r304) + (-1.0 * reaction_r313) + ( 1.0 * reaction_r314) + (-1.0 * reaction_r317) + ( 1.0 * reaction_r318) + (-1.0 * reaction_r319) + ( 1.0 * reaction_r320) + (-1.0 * reaction_r321) + ( 1.0 * reaction_r322) + (-1.0 * reaction_r335) + ( 1.0 * reaction_r336) + (-1.0 * reaction_r337) + ( 1.0 * reaction_r338) + (-1.0 * reaction_r347) + ( 1.0 * reaction_r348) + (-1.0 * reaction_r351) + ( 1.0 * reaction_r352) + (-1.0 * reaction_r353) + ( 1.0 * reaction_r354) + (-1.0 * reaction_r355) + ( 1.0 * reaction_r356) + (-1.0 * reaction_r369) + ( 1.0 * reaction_r370) + (-1.0 * reaction_r375) + ( 1.0 * reaction_r376) + (-1.0 * reaction_r377) + ( 1.0 * reaction_r378) + (-1.0 * reaction_r387) + ( 1.0 * reaction_r388) + (-1.0 * reaction_r391) + ( 1.0 * reaction_r392) + (-1.0 * reaction_r393) + ( 1.0 * reaction_r394) + (-1.0 * reaction_r395) + ( 1.0 * reaction_r396) + (-1.0 * reaction_r409) + ( 1.0 * reaction_r410) + (-1.0 * reaction_r411) + ( 1.0 * reaction_r412) + (-1.0 * reaction_r421) + ( 1.0 * reaction_r422) + (-1.0 * reaction_r425) + ( 1.0 * reaction_r426) + (-1.0 * reaction_r427) + ( 1.0 * reaction_r428) + (-1.0 * reaction_r429) + ( 1.0 * reaction_r430) + (-1.0 * reaction_r443) + ( 1.0 * reaction_r444) + (-1.0 * reaction_r449) + ( 1.0 * reaction_r450) + (-1.0 * reaction_r451) + ( 1.0 * reaction_r452) + (-1.0 * reaction_r461) + ( 1.0 * reaction_r462) + (-1.0 * reaction_r465) + ( 1.0 * reaction_r466) + (-1.0 * reaction_r467) + ( 1.0 * reaction_r468) + (-1.0 * reaction_r469) + ( 1.0 * reaction_r470) + (-1.0 * reaction_r483) + ( 1.0 * reaction_r484) + (-1.0 * reaction_r485) + ( 1.0 * reaction_r486) + (-1.0 * reaction_r495) + ( 1.0 * reaction_r496) + (-1.0 * reaction_r499) + ( 1.0 * reaction_r500) + (-1.0 * reaction_r501) + ( 1.0 * reaction_r502) + (-1.0 * reaction_r503) + ( 1.0 * reaction_r504) + (-1.0 * reaction_r517) + ( 1.0 * reaction_r518) + (-1.0 * reaction_r523) + ( 1.0 * reaction_r524) + (-1.0 * reaction_r525) + ( 1.0 * reaction_r526) + (-1.0 * reaction_r535) + ( 1.0 * reaction_r536) + (-1.0 * reaction_r539) + ( 1.0 * reaction_r540) + (-1.0 * reaction_r541) + ( 1.0 * reaction_r542) + (-1.0 * reaction_r543) + ( 1.0 * reaction_r544) + (-1.0 * reaction_r557) + ( 1.0 * reaction_r558) + (-1.0 * reaction_r559) + ( 1.0 * reaction_r560) + (-1.0 * reaction_r569) + ( 1.0 * reaction_r570) + (-1.0 * reaction_r573) + ( 1.0 * reaction_r574) + (-1.0 * reaction_r575) + ( 1.0 * reaction_r576) + (-1.0 * reaction_r577) + ( 1.0 * reaction_r578) + (-1.0 * reaction_r591) + ( 1.0 * reaction_r592) + (-1.0 * reaction_r597) + ( 1.0 * reaction_r598) + (-1.0 * reaction_r599) + ( 1.0 * reaction_r600) + (-1.0 * reaction_r609) + ( 1.0 * reaction_r610) + (-1.0 * reaction_r613) + ( 1.0 * reaction_r614) + (-1.0 * reaction_r615) + ( 1.0 * reaction_r616) + (-1.0 * reaction_r617) + ( 1.0 * reaction_r618) + (-1.0 * reaction_r631) + ( 1.0 * reaction_r632) + (-1.0 * reaction_r633) + ( 1.0 * reaction_r634) + (-1.0 * reaction_r643) + ( 1.0 * reaction_r644) + (-1.0 * reaction_r647) + ( 1.0 * reaction_r648) + (-1.0 * reaction_r649) + ( 1.0 * reaction_r650) + (-1.0 * reaction_r651) + ( 1.0 * reaction_r652) + (-1.0 * reaction_r665) + ( 1.0 * reaction_r666) + (-1.0 * reaction_r671) + ( 1.0 * reaction_r672) + (-1.0 * reaction_r673) + ( 1.0 * reaction_r674) + (-1.0 * reaction_r683) + ( 1.0 * reaction_r684) + (-1.0 * reaction_r687) + ( 1.0 * reaction_r688) + (-1.0 * reaction_r689) + ( 1.0 * reaction_r690) + (-1.0 * reaction_r691) + ( 1.0 * reaction_r692) + (-1.0 * reaction_r705) + ( 1.0 * reaction_r706) + (-1.0 * reaction_r707) + ( 1.0 * reaction_r708) + (-1.0 * reaction_r717) + ( 1.0 * reaction_r718) + (-1.0 * reaction_r721) + ( 1.0 * reaction_r722) + (-1.0 * reaction_r723) + ( 1.0 * reaction_r724) + (-1.0 * reaction_r725) + ( 1.0 * reaction_r726) + (-1.0 * reaction_r739) + ( 1.0 * reaction_r740) + (-1.0 * reaction_r745) + ( 1.0 * reaction_r746) + (-1.0 * reaction_r747) + ( 1.0 * reaction_r748) + (-1.0 * reaction_r757) + ( 1.0 * reaction_r758) + (-1.0 * reaction_r761) + ( 1.0 * reaction_r762) + (-1.0 * reaction_r763) + ( 1.0 * reaction_r764) + (-1.0 * reaction_r765) + ( 1.0 * reaction_r766) + (-1.0 * reaction_r779) + ( 1.0 * reaction_r780) + (-1.0 * reaction_r781) + ( 1.0 * reaction_r782) + (-1.0 * reaction_r791) + ( 1.0 * reaction_r792) + (-1.0 * reaction_r795) + ( 1.0 * reaction_r796) + (-1.0 * reaction_r797) + ( 1.0 * reaction_r798) + (-1.0 * reaction_r799) + ( 1.0 * reaction_r800) + (-1.0 * reaction_r813) + ( 1.0 * reaction_r814));
	
% Species:   id = CG, name = CG, affected by kineticLaw
	xdot(4) = (1/(compartment_cell))*(( 1.0 * reaction_r221) + (-1.0 * reaction_r222) + (-1.0 * reaction_r225) + ( 1.0 * reaction_r226) + (-1.0 * reaction_r231) + ( 1.0 * reaction_r232) + (-1.0 * reaction_r237) + ( 1.0 * reaction_r238) + (-1.0 * reaction_r241) + ( 1.0 * reaction_r242) + (-1.0 * reaction_r253) + ( 1.0 * reaction_r254) + (-1.0 * reaction_r265) + ( 1.0 * reaction_r266) + (-1.0 * reaction_r271) + ( 1.0 * reaction_r272) + (-1.0 * reaction_r275) + ( 1.0 * reaction_r276) + (-1.0 * reaction_r287) + ( 1.0 * reaction_r288) + (-1.0 * reaction_r299) + ( 1.0 * reaction_r300) + (-1.0 * reaction_r305) + ( 1.0 * reaction_r306) + (-1.0 * reaction_r311) + ( 1.0 * reaction_r312) + (-1.0 * reaction_r315) + ( 1.0 * reaction_r316) + (-1.0 * reaction_r327) + ( 1.0 * reaction_r328) + (-1.0 * reaction_r339) + ( 1.0 * reaction_r340) + (-1.0 * reaction_r345) + ( 1.0 * reaction_r346) + (-1.0 * reaction_r349) + ( 1.0 * reaction_r350) + (-1.0 * reaction_r361) + ( 1.0 * reaction_r362) + (-1.0 * reaction_r373) + ( 1.0 * reaction_r374) + (-1.0 * reaction_r379) + ( 1.0 * reaction_r380) + (-1.0 * reaction_r385) + ( 1.0 * reaction_r386) + (-1.0 * reaction_r389) + ( 1.0 * reaction_r390) + (-1.0 * reaction_r401) + ( 1.0 * reaction_r402) + (-1.0 * reaction_r413) + ( 1.0 * reaction_r414) + (-1.0 * reaction_r419) + ( 1.0 * reaction_r420) + (-1.0 * reaction_r423) + ( 1.0 * reaction_r424) + (-1.0 * reaction_r435) + ( 1.0 * reaction_r436) + (-1.0 * reaction_r447) + ( 1.0 * reaction_r448) + (-1.0 * reaction_r453) + ( 1.0 * reaction_r454) + (-1.0 * reaction_r459) + ( 1.0 * reaction_r460) + (-1.0 * reaction_r463) + ( 1.0 * reaction_r464) + (-1.0 * reaction_r475) + ( 1.0 * reaction_r476) + (-1.0 * reaction_r487) + ( 1.0 * reaction_r488) + (-1.0 * reaction_r493) + ( 1.0 * reaction_r494) + (-1.0 * reaction_r497) + ( 1.0 * reaction_r498) + (-1.0 * reaction_r509) + ( 1.0 * reaction_r510) + (-1.0 * reaction_r521) + ( 1.0 * reaction_r522) + (-1.0 * reaction_r527) + ( 1.0 * reaction_r528) + (-1.0 * reaction_r533) + ( 1.0 * reaction_r534) + (-1.0 * reaction_r537) + ( 1.0 * reaction_r538) + (-1.0 * reaction_r549) + ( 1.0 * reaction_r550) + (-1.0 * reaction_r561) + ( 1.0 * reaction_r562) + (-1.0 * reaction_r567) + ( 1.0 * reaction_r568) + (-1.0 * reaction_r571) + ( 1.0 * reaction_r572) + (-1.0 * reaction_r583) + ( 1.0 * reaction_r584) + (-1.0 * reaction_r595) + ( 1.0 * reaction_r596) + (-1.0 * reaction_r601) + ( 1.0 * reaction_r602) + (-1.0 * reaction_r607) + ( 1.0 * reaction_r608) + (-1.0 * reaction_r611) + ( 1.0 * reaction_r612) + (-1.0 * reaction_r623) + ( 1.0 * reaction_r624) + (-1.0 * reaction_r635) + ( 1.0 * reaction_r636) + (-1.0 * reaction_r641) + ( 1.0 * reaction_r642) + (-1.0 * reaction_r645) + ( 1.0 * reaction_r646) + (-1.0 * reaction_r657) + ( 1.0 * reaction_r658) + (-1.0 * reaction_r669) + ( 1.0 * reaction_r670) + (-1.0 * reaction_r675) + ( 1.0 * reaction_r676) + (-1.0 * reaction_r681) + ( 1.0 * reaction_r682) + (-1.0 * reaction_r685) + ( 1.0 * reaction_r686) + (-1.0 * reaction_r697) + ( 1.0 * reaction_r698) + (-1.0 * reaction_r709) + ( 1.0 * reaction_r710) + (-1.0 * reaction_r715) + ( 1.0 * reaction_r716) + (-1.0 * reaction_r719) + ( 1.0 * reaction_r720) + (-1.0 * reaction_r731) + ( 1.0 * reaction_r732) + (-1.0 * reaction_r743) + ( 1.0 * reaction_r744) + (-1.0 * reaction_r749) + ( 1.0 * reaction_r750) + (-1.0 * reaction_r755) + ( 1.0 * reaction_r756) + (-1.0 * reaction_r759) + ( 1.0 * reaction_r760) + (-1.0 * reaction_r771) + ( 1.0 * reaction_r772) + (-1.0 * reaction_r783) + ( 1.0 * reaction_r784) + (-1.0 * reaction_r789) + ( 1.0 * reaction_r790) + (-1.0 * reaction_r793) + ( 1.0 * reaction_r794) + (-1.0 * reaction_r805) + ( 1.0 * reaction_r806));
	
% Species:   id = PY, name = PY, defined in a rule 	xdot(5) = x(5);
	
% Species:   id = Ub, name = Ub, defined in a rule 	xdot(6) = x(6);
	
% Species:   id = PYNorm, name = PYNorm, defined in a rule 	xdot(7) = x(7);
	
% Species:   id = UbNorm, name = UbNorm, defined in a rule 	xdot(8) = x(8);
	
% Species:   id = SumM, name = SumM, defined in a rule 	xdot(9) = x(9);
	
% Species:   id = SumML, name = SumML, defined in a rule 	xdot(10) = x(10);
	
% Species:   id = Rc00UU, name = Rc00UU, affected by kineticLaw
	xdot(11) = (1/(compartment_cell))*(( 1.0 * reaction_r1) + ( 1.0 * reaction_r2) + ( 1.0 * reaction_r815) + (-1.0 * reaction_r841) + (-1.0 * reaction_r945) + ( 1.0 * reaction_r1101));
	
% Species:   id = Rc10UU, name = Rc10UU, affected by kineticLaw
	xdot(12) = (1/(compartment_cell))*((-1.0 * reaction_r1) + ( 1.0 * reaction_r4) + (-1.0 * reaction_r223) + ( 1.0 * reaction_r224) + (-1.0 * reaction_r225) + ( 1.0 * reaction_r226) + ( 1.0 * reaction_r816) + (-1.0 * reaction_r842) + (-1.0 * reaction_r946) + ( 1.0 * reaction_r1102));
	
% Species:   id = Rc10CU, name = Rc10CU, affected by kineticLaw
	xdot(13) = (1/(compartment_cell))*(( 1.0 * reaction_r5) + ( 1.0 * reaction_r223) + (-1.0 * reaction_r224) + (-1.0 * reaction_r227) + ( 1.0 * reaction_r228) + ( 1.0 * reaction_r817) + (-1.0 * reaction_r843) + (-1.0 * reaction_r947) + ( 1.0 * reaction_r1103));
	
% Species:   id = Rc10LU, name = Rc10LU, affected by kineticLaw
	xdot(14) = (1/(compartment_cell))*(( 1.0 * reaction_r6) + ( 1.0 * reaction_r225) + (-1.0 * reaction_r226) + ( 1.0 * reaction_r227) + (-1.0 * reaction_r228) + ( 1.0 * reaction_r818) + (-1.0 * reaction_r844) + (-1.0 * reaction_r948) + ( 1.0 * reaction_r1104));
	
% Species:   id = Rc01UU, name = Rc01UU, affected by kineticLaw
	xdot(15) = (1/(compartment_cell))*((-1.0 * reaction_r2) + ( 1.0 * reaction_r3) + ( 1.0 * reaction_r9) + (-1.0 * reaction_r229) + ( 1.0 * reaction_r230) + (-1.0 * reaction_r231) + ( 1.0 * reaction_r232) + ( 1.0 * reaction_r819) + (-1.0 * reaction_r845) + (-1.0 * reaction_r949) + ( 1.0 * reaction_r1105));
	
% Species:   id = Rc01UG, name = Rc01UG, affected by kineticLaw
	xdot(16) = (1/(compartment_cell))*(( 1.0 * reaction_r7) + ( 1.0 * reaction_r10) + ( 1.0 * reaction_r229) + (-1.0 * reaction_r230) + (-1.0 * reaction_r233) + ( 1.0 * reaction_r234) + ( 1.0 * reaction_r820) + (-1.0 * reaction_r846) + (-1.0 * reaction_r950) + ( 1.0 * reaction_r1106));
	
% Species:   id = Rc01UL, name = Rc01UL, affected by kineticLaw
	xdot(17) = (1/(compartment_cell))*(( 1.0 * reaction_r8) + ( 1.0 * reaction_r11) + ( 1.0 * reaction_r231) + (-1.0 * reaction_r232) + ( 1.0 * reaction_r233) + (-1.0 * reaction_r234) + ( 1.0 * reaction_r821) + (-1.0 * reaction_r847) + (-1.0 * reaction_r951) + ( 1.0 * reaction_r1107));
	
% Species:   id = Rc11UU, name = Rc11UU, affected by kineticLaw
	xdot(18) = (1/(compartment_cell))*((-1.0 * reaction_r3) + (-1.0 * reaction_r4) + ( 1.0 * reaction_r13) + (-1.0 * reaction_r235) + ( 1.0 * reaction_r236) + (-1.0 * reaction_r237) + ( 1.0 * reaction_r238) + (-1.0 * reaction_r239) + ( 1.0 * reaction_r240) + (-1.0 * reaction_r241) + ( 1.0 * reaction_r242) + ( 1.0 * reaction_r822) + (-1.0 * reaction_r848) + (-1.0 * reaction_r952) + ( 1.0 * reaction_r1108));
	
% Species:   id = Rc11CU, name = Rc11CU, affected by kineticLaw
	xdot(19) = (1/(compartment_cell))*((-1.0 * reaction_r5) + ( 1.0 * reaction_r14) + ( 1.0 * reaction_r235) + (-1.0 * reaction_r236) + (-1.0 * reaction_r243) + ( 1.0 * reaction_r244) + (-1.0 * reaction_r245) + ( 1.0 * reaction_r246) + ( 1.0 * reaction_r823) + (-1.0 * reaction_r849) + (-1.0 * reaction_r953) + ( 1.0 * reaction_r1109));
	
% Species:   id = Rc11LU, name = Rc11LU, affected by kineticLaw
	xdot(20) = (1/(compartment_cell))*((-1.0 * reaction_r6) + ( 1.0 * reaction_r15) + ( 1.0 * reaction_r237) + (-1.0 * reaction_r238) + ( 1.0 * reaction_r243) + (-1.0 * reaction_r244) + (-1.0 * reaction_r247) + ( 1.0 * reaction_r248) + (-1.0 * reaction_r249) + ( 1.0 * reaction_r250) + ( 1.0 * reaction_r824) + (-1.0 * reaction_r850) + (-1.0 * reaction_r954) + ( 1.0 * reaction_r1110));
	
% Species:   id = Rc11UG, name = Rc11UG, affected by kineticLaw
	xdot(21) = (1/(compartment_cell))*((-1.0 * reaction_r7) + ( 1.0 * reaction_r17) + ( 1.0 * reaction_r239) + (-1.0 * reaction_r240) + (-1.0 * reaction_r251) + ( 1.0 * reaction_r252) + (-1.0 * reaction_r253) + ( 1.0 * reaction_r254) + (-1.0 * reaction_r255) + ( 1.0 * reaction_r256) + ( 1.0 * reaction_r825) + (-1.0 * reaction_r851) + (-1.0 * reaction_r955) + ( 1.0 * reaction_r1111));
	
% Species:   id = Rc11UL, name = Rc11UL, affected by kineticLaw
	xdot(22) = (1/(compartment_cell))*((-1.0 * reaction_r8) + ( 1.0 * reaction_r19) + ( 1.0 * reaction_r241) + (-1.0 * reaction_r242) + ( 1.0 * reaction_r255) + (-1.0 * reaction_r256) + (-1.0 * reaction_r257) + ( 1.0 * reaction_r258) + ( 1.0 * reaction_r826) + (-1.0 * reaction_r852) + (-1.0 * reaction_r956) + ( 1.0 * reaction_r1112));
	
% Species:   id = Rc11CG, name = Rc11CG, affected by kineticLaw
	xdot(23) = (1/(compartment_cell))*(( 1.0 * reaction_r20) + ( 1.0 * reaction_r245) + (-1.0 * reaction_r246) + ( 1.0 * reaction_r251) + (-1.0 * reaction_r252) + (-1.0 * reaction_r259) + ( 1.0 * reaction_r260) + (-1.0 * reaction_r261) + ( 1.0 * reaction_r262) + ( 1.0 * reaction_r827) + (-1.0 * reaction_r853) + (-1.0 * reaction_r957) + ( 1.0 * reaction_r1113));
	
% Species:   id = Rc11CC, name = Rc11CC, affected by kineticLaw
	xdot(24) = (1/(compartment_cell))*(( 1.0 * reaction_r21) + ( 1.0 * reaction_r249) + (-1.0 * reaction_r250) + ( 1.0 * reaction_r257) + (-1.0 * reaction_r258) + ( 1.0 * reaction_r259) + (-1.0 * reaction_r260) + ( 1.0 * reaction_r828) + (-1.0 * reaction_r854) + (-1.0 * reaction_r958) + ( 1.0 * reaction_r1114));
	
% Species:   id = Rc11LG, name = Rc11LG, affected by kineticLaw
	xdot(25) = (1/(compartment_cell))*(( 1.0 * reaction_r22) + ( 1.0 * reaction_r247) + (-1.0 * reaction_r248) + ( 1.0 * reaction_r253) + (-1.0 * reaction_r254) + ( 1.0 * reaction_r261) + (-1.0 * reaction_r262) + ( 1.0 * reaction_r829) + (-1.0 * reaction_r855) + (-1.0 * reaction_r959) + ( 1.0 * reaction_r1115));
	
% Species:   id = Rc02UU, name = Rc02UU, affected by kineticLaw
	xdot(26) = (1/(compartment_cell))*((-1.0 * reaction_r9) + ( 1.0 * reaction_r12) + (-1.0 * reaction_r263) + ( 1.0 * reaction_r264) + (-1.0 * reaction_r265) + ( 1.0 * reaction_r266) + ( 1.0 * reaction_r830) + (-1.0 * reaction_r856) + (-1.0 * reaction_r960) + ( 1.0 * reaction_r1116));
	
% Species:   id = Rc02UG, name = Rc02UG, affected by kineticLaw
	xdot(27) = (1/(compartment_cell))*((-1.0 * reaction_r10) + ( 1.0 * reaction_r16) + ( 1.0 * reaction_r263) + (-1.0 * reaction_r264) + (-1.0 * reaction_r267) + ( 1.0 * reaction_r268) + ( 1.0 * reaction_r831) + (-1.0 * reaction_r857) + (-1.0 * reaction_r961) + ( 1.0 * reaction_r1117));
	
% Species:   id = Rc02UL, name = Rc02UL, affected by kineticLaw
	xdot(28) = (1/(compartment_cell))*((-1.0 * reaction_r11) + ( 1.0 * reaction_r18) + ( 1.0 * reaction_r265) + (-1.0 * reaction_r266) + ( 1.0 * reaction_r267) + (-1.0 * reaction_r268) + ( 1.0 * reaction_r832) + (-1.0 * reaction_r858) + (-1.0 * reaction_r962) + ( 1.0 * reaction_r1118));
	
% Species:   id = Rc12UU, name = Rc12UU, affected by kineticLaw
	xdot(29) = (1/(compartment_cell))*((-1.0 * reaction_r12) + (-1.0 * reaction_r13) + (-1.0 * reaction_r269) + ( 1.0 * reaction_r270) + (-1.0 * reaction_r271) + ( 1.0 * reaction_r272) + (-1.0 * reaction_r273) + ( 1.0 * reaction_r274) + (-1.0 * reaction_r275) + ( 1.0 * reaction_r276) + ( 1.0 * reaction_r833) + (-1.0 * reaction_r859) + (-1.0 * reaction_r963) + ( 1.0 * reaction_r1119));
	
% Species:   id = Rc12CU, name = Rc12CU, affected by kineticLaw
	xdot(30) = (1/(compartment_cell))*((-1.0 * reaction_r14) + ( 1.0 * reaction_r269) + (-1.0 * reaction_r270) + (-1.0 * reaction_r277) + ( 1.0 * reaction_r278) + (-1.0 * reaction_r279) + ( 1.0 * reaction_r280) + ( 1.0 * reaction_r834) + (-1.0 * reaction_r860) + (-1.0 * reaction_r964) + ( 1.0 * reaction_r1120));
	
% Species:   id = Rc12LU, name = Rc12LU, affected by kineticLaw
	xdot(31) = (1/(compartment_cell))*((-1.0 * reaction_r15) + ( 1.0 * reaction_r271) + (-1.0 * reaction_r272) + ( 1.0 * reaction_r277) + (-1.0 * reaction_r278) + (-1.0 * reaction_r281) + ( 1.0 * reaction_r282) + (-1.0 * reaction_r283) + ( 1.0 * reaction_r284) + ( 1.0 * reaction_r835) + (-1.0 * reaction_r861) + (-1.0 * reaction_r965) + ( 1.0 * reaction_r1121));
	
% Species:   id = Rc12UG, name = Rc12UG, affected by kineticLaw
	xdot(32) = (1/(compartment_cell))*((-1.0 * reaction_r16) + (-1.0 * reaction_r17) + ( 1.0 * reaction_r273) + (-1.0 * reaction_r274) + (-1.0 * reaction_r285) + ( 1.0 * reaction_r286) + (-1.0 * reaction_r287) + ( 1.0 * reaction_r288) + (-1.0 * reaction_r289) + ( 1.0 * reaction_r290) + ( 1.0 * reaction_r836) + (-1.0 * reaction_r862) + (-1.0 * reaction_r966) + ( 1.0 * reaction_r1122));
	
% Species:   id = Rc12UL, name = Rc12UL, affected by kineticLaw
	xdot(33) = (1/(compartment_cell))*((-1.0 * reaction_r18) + (-1.0 * reaction_r19) + ( 1.0 * reaction_r275) + (-1.0 * reaction_r276) + ( 1.0 * reaction_r289) + (-1.0 * reaction_r290) + (-1.0 * reaction_r291) + ( 1.0 * reaction_r292) + ( 1.0 * reaction_r837) + (-1.0 * reaction_r863) + (-1.0 * reaction_r967) + ( 1.0 * reaction_r1123));
	
% Species:   id = Rc12CG, name = Rc12CG, affected by kineticLaw
	xdot(34) = (1/(compartment_cell))*((-1.0 * reaction_r20) + ( 1.0 * reaction_r279) + (-1.0 * reaction_r280) + ( 1.0 * reaction_r285) + (-1.0 * reaction_r286) + (-1.0 * reaction_r293) + ( 1.0 * reaction_r294) + (-1.0 * reaction_r295) + ( 1.0 * reaction_r296) + ( 1.0 * reaction_r838) + (-1.0 * reaction_r864) + (-1.0 * reaction_r968) + ( 1.0 * reaction_r1124));
	
% Species:   id = Rc12CC, name = Rc12CC, affected by kineticLaw
	xdot(35) = (1/(compartment_cell))*((-1.0 * reaction_r21) + ( 1.0 * reaction_r283) + (-1.0 * reaction_r284) + ( 1.0 * reaction_r291) + (-1.0 * reaction_r292) + ( 1.0 * reaction_r293) + (-1.0 * reaction_r294) + ( 1.0 * reaction_r839) + (-1.0 * reaction_r865) + (-1.0 * reaction_r969) + ( 1.0 * reaction_r1125));
	
% Species:   id = Rc12LG, name = Rc12LG, affected by kineticLaw
	xdot(36) = (1/(compartment_cell))*((-1.0 * reaction_r22) + ( 1.0 * reaction_r281) + (-1.0 * reaction_r282) + ( 1.0 * reaction_r287) + (-1.0 * reaction_r288) + ( 1.0 * reaction_r295) + (-1.0 * reaction_r296) + ( 1.0 * reaction_r840) + (-1.0 * reaction_r866) + (-1.0 * reaction_r970) + ( 1.0 * reaction_r1126));
	
% Species:   id = RcL00UU, name = RcL00UU, affected by kineticLaw
	xdot(37) = (1/(compartment_cell))*(( 1.0 * reaction_r23) + ( 1.0 * reaction_r24) + ( 1.0 * reaction_r867) + (-1.0 * reaction_r893) + ( 1.0 * reaction_r945) + (-1.0 * reaction_r1101));
	
% Species:   id = RcL10UU, name = RcL10UU, affected by kineticLaw
	xdot(38) = (1/(compartment_cell))*((-1.0 * reaction_r23) + ( 1.0 * reaction_r26) + (-1.0 * reaction_r297) + ( 1.0 * reaction_r298) + (-1.0 * reaction_r299) + ( 1.0 * reaction_r300) + ( 1.0 * reaction_r868) + (-1.0 * reaction_r894) + ( 1.0 * reaction_r946) + (-1.0 * reaction_r1102));
	
% Species:   id = RcL10CU, name = RcL10CU, affected by kineticLaw
	xdot(39) = (1/(compartment_cell))*(( 1.0 * reaction_r27) + ( 1.0 * reaction_r297) + (-1.0 * reaction_r298) + (-1.0 * reaction_r301) + ( 1.0 * reaction_r302) + ( 1.0 * reaction_r869) + (-1.0 * reaction_r895) + ( 1.0 * reaction_r947) + (-1.0 * reaction_r1103));
	
% Species:   id = RcL10LU, name = RcL10LU, affected by kineticLaw
	xdot(40) = (1/(compartment_cell))*(( 1.0 * reaction_r28) + ( 1.0 * reaction_r299) + (-1.0 * reaction_r300) + ( 1.0 * reaction_r301) + (-1.0 * reaction_r302) + ( 1.0 * reaction_r870) + (-1.0 * reaction_r896) + ( 1.0 * reaction_r948) + (-1.0 * reaction_r1104));
	
% Species:   id = RcL01UU, name = RcL01UU, affected by kineticLaw
	xdot(41) = (1/(compartment_cell))*((-1.0 * reaction_r24) + ( 1.0 * reaction_r25) + ( 1.0 * reaction_r31) + (-1.0 * reaction_r303) + ( 1.0 * reaction_r304) + (-1.0 * reaction_r305) + ( 1.0 * reaction_r306) + ( 1.0 * reaction_r871) + (-1.0 * reaction_r897) + ( 1.0 * reaction_r949) + (-1.0 * reaction_r1105));
	
% Species:   id = RcL01UG, name = RcL01UG, affected by kineticLaw
	xdot(42) = (1/(compartment_cell))*(( 1.0 * reaction_r29) + ( 1.0 * reaction_r32) + ( 1.0 * reaction_r303) + (-1.0 * reaction_r304) + (-1.0 * reaction_r307) + ( 1.0 * reaction_r308) + ( 1.0 * reaction_r872) + (-1.0 * reaction_r898) + ( 1.0 * reaction_r950) + (-1.0 * reaction_r1106));
	
% Species:   id = RcL01UL, name = RcL01UL, affected by kineticLaw
	xdot(43) = (1/(compartment_cell))*(( 1.0 * reaction_r30) + ( 1.0 * reaction_r33) + ( 1.0 * reaction_r305) + (-1.0 * reaction_r306) + ( 1.0 * reaction_r307) + (-1.0 * reaction_r308) + ( 1.0 * reaction_r873) + (-1.0 * reaction_r899) + ( 1.0 * reaction_r951) + (-1.0 * reaction_r1107));
	
% Species:   id = RcL11UU, name = RcL11UU, affected by kineticLaw
	xdot(44) = (1/(compartment_cell))*((-1.0 * reaction_r25) + (-1.0 * reaction_r26) + ( 1.0 * reaction_r35) + (-1.0 * reaction_r309) + ( 1.0 * reaction_r310) + (-1.0 * reaction_r311) + ( 1.0 * reaction_r312) + (-1.0 * reaction_r313) + ( 1.0 * reaction_r314) + (-1.0 * reaction_r315) + ( 1.0 * reaction_r316) + ( 1.0 * reaction_r874) + (-1.0 * reaction_r900) + ( 1.0 * reaction_r952) + (-1.0 * reaction_r1108));
	
% Species:   id = RcL11CU, name = RcL11CU, affected by kineticLaw
	xdot(45) = (1/(compartment_cell))*((-1.0 * reaction_r27) + ( 1.0 * reaction_r36) + ( 1.0 * reaction_r309) + (-1.0 * reaction_r310) + (-1.0 * reaction_r317) + ( 1.0 * reaction_r318) + (-1.0 * reaction_r319) + ( 1.0 * reaction_r320) + ( 1.0 * reaction_r875) + (-1.0 * reaction_r901) + ( 1.0 * reaction_r953) + (-1.0 * reaction_r1109));
	
% Species:   id = RcL11LU, name = RcL11LU, affected by kineticLaw
	xdot(46) = (1/(compartment_cell))*((-1.0 * reaction_r28) + ( 1.0 * reaction_r37) + ( 1.0 * reaction_r311) + (-1.0 * reaction_r312) + ( 1.0 * reaction_r317) + (-1.0 * reaction_r318) + (-1.0 * reaction_r321) + ( 1.0 * reaction_r322) + (-1.0 * reaction_r323) + ( 1.0 * reaction_r324) + ( 1.0 * reaction_r876) + (-1.0 * reaction_r902) + ( 1.0 * reaction_r954) + (-1.0 * reaction_r1110));
	
% Species:   id = RcL11UG, name = RcL11UG, affected by kineticLaw
	xdot(47) = (1/(compartment_cell))*((-1.0 * reaction_r29) + ( 1.0 * reaction_r39) + ( 1.0 * reaction_r313) + (-1.0 * reaction_r314) + (-1.0 * reaction_r325) + ( 1.0 * reaction_r326) + (-1.0 * reaction_r327) + ( 1.0 * reaction_r328) + (-1.0 * reaction_r329) + ( 1.0 * reaction_r330) + ( 1.0 * reaction_r877) + (-1.0 * reaction_r903) + ( 1.0 * reaction_r955) + (-1.0 * reaction_r1111));
	
% Species:   id = RcL11UL, name = RcL11UL, affected by kineticLaw
	xdot(48) = (1/(compartment_cell))*((-1.0 * reaction_r30) + ( 1.0 * reaction_r41) + ( 1.0 * reaction_r315) + (-1.0 * reaction_r316) + ( 1.0 * reaction_r329) + (-1.0 * reaction_r330) + (-1.0 * reaction_r331) + ( 1.0 * reaction_r332) + ( 1.0 * reaction_r878) + (-1.0 * reaction_r904) + ( 1.0 * reaction_r956) + (-1.0 * reaction_r1112));
	
% Species:   id = RcL11CG, name = RcL11CG, affected by kineticLaw
	xdot(49) = (1/(compartment_cell))*(( 1.0 * reaction_r42) + ( 1.0 * reaction_r319) + (-1.0 * reaction_r320) + ( 1.0 * reaction_r325) + (-1.0 * reaction_r326) + (-1.0 * reaction_r333) + ( 1.0 * reaction_r334) + (-1.0 * reaction_r335) + ( 1.0 * reaction_r336) + ( 1.0 * reaction_r879) + (-1.0 * reaction_r905) + ( 1.0 * reaction_r957) + (-1.0 * reaction_r1113));
	
% Species:   id = RcL11CC, name = RcL11CC, affected by kineticLaw
	xdot(50) = (1/(compartment_cell))*(( 1.0 * reaction_r43) + ( 1.0 * reaction_r323) + (-1.0 * reaction_r324) + ( 1.0 * reaction_r331) + (-1.0 * reaction_r332) + ( 1.0 * reaction_r333) + (-1.0 * reaction_r334) + ( 1.0 * reaction_r880) + (-1.0 * reaction_r906) + ( 1.0 * reaction_r958) + (-1.0 * reaction_r1114));
	
% Species:   id = RcL11LG, name = RcL11LG, affected by kineticLaw
	xdot(51) = (1/(compartment_cell))*(( 1.0 * reaction_r44) + ( 1.0 * reaction_r321) + (-1.0 * reaction_r322) + ( 1.0 * reaction_r327) + (-1.0 * reaction_r328) + ( 1.0 * reaction_r335) + (-1.0 * reaction_r336) + ( 1.0 * reaction_r881) + (-1.0 * reaction_r907) + ( 1.0 * reaction_r959) + (-1.0 * reaction_r1115));
	
% Species:   id = RcL02UU, name = RcL02UU, affected by kineticLaw
	xdot(52) = (1/(compartment_cell))*((-1.0 * reaction_r31) + ( 1.0 * reaction_r34) + (-1.0 * reaction_r337) + ( 1.0 * reaction_r338) + (-1.0 * reaction_r339) + ( 1.0 * reaction_r340) + ( 1.0 * reaction_r882) + (-1.0 * reaction_r908) + ( 1.0 * reaction_r960) + (-1.0 * reaction_r1116));
	
% Species:   id = RcL02UG, name = RcL02UG, affected by kineticLaw
	xdot(53) = (1/(compartment_cell))*((-1.0 * reaction_r32) + ( 1.0 * reaction_r38) + ( 1.0 * reaction_r337) + (-1.0 * reaction_r338) + (-1.0 * reaction_r341) + ( 1.0 * reaction_r342) + ( 1.0 * reaction_r883) + (-1.0 * reaction_r909) + ( 1.0 * reaction_r961) + (-1.0 * reaction_r1117));
	
% Species:   id = RcL02UL, name = RcL02UL, affected by kineticLaw
	xdot(54) = (1/(compartment_cell))*((-1.0 * reaction_r33) + ( 1.0 * reaction_r40) + ( 1.0 * reaction_r339) + (-1.0 * reaction_r340) + ( 1.0 * reaction_r341) + (-1.0 * reaction_r342) + ( 1.0 * reaction_r884) + (-1.0 * reaction_r910) + ( 1.0 * reaction_r962) + (-1.0 * reaction_r1118));
	
% Species:   id = RcL12UU, name = RcL12UU, affected by kineticLaw
	xdot(55) = (1/(compartment_cell))*((-1.0 * reaction_r34) + (-1.0 * reaction_r35) + (-1.0 * reaction_r343) + ( 1.0 * reaction_r344) + (-1.0 * reaction_r345) + ( 1.0 * reaction_r346) + (-1.0 * reaction_r347) + ( 1.0 * reaction_r348) + (-1.0 * reaction_r349) + ( 1.0 * reaction_r350) + ( 1.0 * reaction_r885) + (-1.0 * reaction_r911) + ( 1.0 * reaction_r963) + (-1.0 * reaction_r1119));
	
% Species:   id = RcL12CU, name = RcL12CU, affected by kineticLaw
	xdot(56) = (1/(compartment_cell))*((-1.0 * reaction_r36) + ( 1.0 * reaction_r343) + (-1.0 * reaction_r344) + (-1.0 * reaction_r351) + ( 1.0 * reaction_r352) + (-1.0 * reaction_r353) + ( 1.0 * reaction_r354) + ( 1.0 * reaction_r886) + (-1.0 * reaction_r912) + ( 1.0 * reaction_r964) + (-1.0 * reaction_r1120));
	
% Species:   id = RcL12LU, name = RcL12LU, affected by kineticLaw
	xdot(57) = (1/(compartment_cell))*((-1.0 * reaction_r37) + ( 1.0 * reaction_r345) + (-1.0 * reaction_r346) + ( 1.0 * reaction_r351) + (-1.0 * reaction_r352) + (-1.0 * reaction_r355) + ( 1.0 * reaction_r356) + (-1.0 * reaction_r357) + ( 1.0 * reaction_r358) + ( 1.0 * reaction_r887) + (-1.0 * reaction_r913) + ( 1.0 * reaction_r965) + (-1.0 * reaction_r1121));
	
% Species:   id = RcL12UG, name = RcL12UG, affected by kineticLaw
	xdot(58) = (1/(compartment_cell))*((-1.0 * reaction_r38) + (-1.0 * reaction_r39) + ( 1.0 * reaction_r347) + (-1.0 * reaction_r348) + (-1.0 * reaction_r359) + ( 1.0 * reaction_r360) + (-1.0 * reaction_r361) + ( 1.0 * reaction_r362) + (-1.0 * reaction_r363) + ( 1.0 * reaction_r364) + ( 1.0 * reaction_r888) + (-1.0 * reaction_r914) + ( 1.0 * reaction_r966) + (-1.0 * reaction_r1122));
	
% Species:   id = RcL12UL, name = RcL12UL, affected by kineticLaw
	xdot(59) = (1/(compartment_cell))*((-1.0 * reaction_r40) + (-1.0 * reaction_r41) + ( 1.0 * reaction_r349) + (-1.0 * reaction_r350) + ( 1.0 * reaction_r363) + (-1.0 * reaction_r364) + (-1.0 * reaction_r365) + ( 1.0 * reaction_r366) + ( 1.0 * reaction_r889) + (-1.0 * reaction_r915) + ( 1.0 * reaction_r967) + (-1.0 * reaction_r1123));
	
% Species:   id = RcL12CG, name = RcL12CG, affected by kineticLaw
	xdot(60) = (1/(compartment_cell))*((-1.0 * reaction_r42) + ( 1.0 * reaction_r353) + (-1.0 * reaction_r354) + ( 1.0 * reaction_r359) + (-1.0 * reaction_r360) + (-1.0 * reaction_r367) + ( 1.0 * reaction_r368) + (-1.0 * reaction_r369) + ( 1.0 * reaction_r370) + ( 1.0 * reaction_r890) + (-1.0 * reaction_r916) + ( 1.0 * reaction_r968) + (-1.0 * reaction_r1124));
	
% Species:   id = RcL12CC, name = RcL12CC, affected by kineticLaw
	xdot(61) = (1/(compartment_cell))*((-1.0 * reaction_r43) + ( 1.0 * reaction_r357) + (-1.0 * reaction_r358) + ( 1.0 * reaction_r365) + (-1.0 * reaction_r366) + ( 1.0 * reaction_r367) + (-1.0 * reaction_r368) + ( 1.0 * reaction_r891) + (-1.0 * reaction_r917) + ( 1.0 * reaction_r969) + (-1.0 * reaction_r1125));
	
% Species:   id = RcL12LG, name = RcL12LG, affected by kineticLaw
	xdot(62) = (1/(compartment_cell))*((-1.0 * reaction_r44) + ( 1.0 * reaction_r355) + (-1.0 * reaction_r356) + ( 1.0 * reaction_r361) + (-1.0 * reaction_r362) + ( 1.0 * reaction_r369) + (-1.0 * reaction_r370) + ( 1.0 * reaction_r892) + (-1.0 * reaction_r918) + ( 1.0 * reaction_r970) + (-1.0 * reaction_r1126));
	
% Species:   id = R00UU, name = R00UU, affected by kineticLaw
	xdot(63) = (1/(compartment_cell))*(( 1.0 * reaction_r45) + ( 1.0 * reaction_r46) + (-1.0 * reaction_r815) + ( 1.0 * reaction_r841) + (-1.0 * reaction_r919) + ( 1.0 * reaction_r1075) + (-1.0 * reaction_r1231) + (-1.0 * reaction_r1257) + ( 1.0 * reaction_r1335) + ( 1.0 * reaction_r1387) + (-1.0 * reaction_r1439));
	
% Species:   id = R10UU, name = R10UU, affected by kineticLaw
	xdot(64) = (1/(compartment_cell))*((-1.0 * reaction_r45) + ( 1.0 * reaction_r48) + (-1.0 * reaction_r371) + ( 1.0 * reaction_r372) + (-1.0 * reaction_r373) + ( 1.0 * reaction_r374) + (-1.0 * reaction_r816) + ( 1.0 * reaction_r842) + (-1.0 * reaction_r920) + ( 1.0 * reaction_r1076) + (-1.0 * reaction_r1232) + (-1.0 * reaction_r1258) + ( 1.0 * reaction_r1336) + ( 1.0 * reaction_r1388) + (-1.0 * reaction_r1440));
	
% Species:   id = R10CU, name = R10CU, affected by kineticLaw
	xdot(65) = (1/(compartment_cell))*(( 1.0 * reaction_r49) + ( 1.0 * reaction_r371) + (-1.0 * reaction_r372) + (-1.0 * reaction_r375) + ( 1.0 * reaction_r376) + (-1.0 * reaction_r817) + ( 1.0 * reaction_r843) + (-1.0 * reaction_r921) + ( 1.0 * reaction_r1077) + (-1.0 * reaction_r1233) + (-1.0 * reaction_r1259) + ( 1.0 * reaction_r1337) + ( 1.0 * reaction_r1389) + (-1.0 * reaction_r1441));
	
% Species:   id = R10LU, name = R10LU, affected by kineticLaw
	xdot(66) = (1/(compartment_cell))*(( 1.0 * reaction_r50) + ( 1.0 * reaction_r373) + (-1.0 * reaction_r374) + ( 1.0 * reaction_r375) + (-1.0 * reaction_r376) + (-1.0 * reaction_r818) + ( 1.0 * reaction_r844) + (-1.0 * reaction_r922) + ( 1.0 * reaction_r1078) + (-1.0 * reaction_r1234) + (-1.0 * reaction_r1260) + ( 1.0 * reaction_r1338) + ( 1.0 * reaction_r1390) + (-1.0 * reaction_r1442));
	
% Species:   id = R01UU, name = R01UU, affected by kineticLaw
	xdot(67) = (1/(compartment_cell))*((-1.0 * reaction_r46) + ( 1.0 * reaction_r47) + ( 1.0 * reaction_r53) + (-1.0 * reaction_r377) + ( 1.0 * reaction_r378) + (-1.0 * reaction_r379) + ( 1.0 * reaction_r380) + (-1.0 * reaction_r819) + ( 1.0 * reaction_r845) + (-1.0 * reaction_r923) + ( 1.0 * reaction_r1079) + (-1.0 * reaction_r1235) + (-1.0 * reaction_r1261) + ( 1.0 * reaction_r1339) + ( 1.0 * reaction_r1391) + (-1.0 * reaction_r1443));
	
% Species:   id = R01UG, name = R01UG, affected by kineticLaw
	xdot(68) = (1/(compartment_cell))*(( 1.0 * reaction_r51) + ( 1.0 * reaction_r54) + ( 1.0 * reaction_r377) + (-1.0 * reaction_r378) + (-1.0 * reaction_r381) + ( 1.0 * reaction_r382) + (-1.0 * reaction_r820) + ( 1.0 * reaction_r846) + (-1.0 * reaction_r924) + ( 1.0 * reaction_r1080) + (-1.0 * reaction_r1236) + (-1.0 * reaction_r1262) + ( 1.0 * reaction_r1340) + ( 1.0 * reaction_r1392) + (-1.0 * reaction_r1444));
	
% Species:   id = R01UL, name = R01UL, affected by kineticLaw
	xdot(69) = (1/(compartment_cell))*(( 1.0 * reaction_r52) + ( 1.0 * reaction_r55) + ( 1.0 * reaction_r379) + (-1.0 * reaction_r380) + ( 1.0 * reaction_r381) + (-1.0 * reaction_r382) + (-1.0 * reaction_r821) + ( 1.0 * reaction_r847) + (-1.0 * reaction_r925) + ( 1.0 * reaction_r1081) + (-1.0 * reaction_r1237) + (-1.0 * reaction_r1263) + ( 1.0 * reaction_r1341) + ( 1.0 * reaction_r1393) + (-1.0 * reaction_r1445));
	
% Species:   id = R11UU, name = R11UU, affected by kineticLaw
	xdot(70) = (1/(compartment_cell))*((-1.0 * reaction_r47) + (-1.0 * reaction_r48) + ( 1.0 * reaction_r57) + (-1.0 * reaction_r383) + ( 1.0 * reaction_r384) + (-1.0 * reaction_r385) + ( 1.0 * reaction_r386) + (-1.0 * reaction_r387) + ( 1.0 * reaction_r388) + (-1.0 * reaction_r389) + ( 1.0 * reaction_r390) + (-1.0 * reaction_r822) + ( 1.0 * reaction_r848) + (-1.0 * reaction_r926) + ( 1.0 * reaction_r1082) + (-1.0 * reaction_r1238) + (-1.0 * reaction_r1264) + ( 1.0 * reaction_r1342) + ( 1.0 * reaction_r1394) + (-1.0 * reaction_r1446));
	
% Species:   id = R11CU, name = R11CU, affected by kineticLaw
	xdot(71) = (1/(compartment_cell))*((-1.0 * reaction_r49) + ( 1.0 * reaction_r58) + ( 1.0 * reaction_r383) + (-1.0 * reaction_r384) + (-1.0 * reaction_r391) + ( 1.0 * reaction_r392) + (-1.0 * reaction_r393) + ( 1.0 * reaction_r394) + (-1.0 * reaction_r823) + ( 1.0 * reaction_r849) + (-1.0 * reaction_r927) + ( 1.0 * reaction_r1083) + (-1.0 * reaction_r1239) + (-1.0 * reaction_r1265) + ( 1.0 * reaction_r1343) + ( 1.0 * reaction_r1395) + (-1.0 * reaction_r1447));
	
% Species:   id = R11LU, name = R11LU, affected by kineticLaw
	xdot(72) = (1/(compartment_cell))*((-1.0 * reaction_r50) + ( 1.0 * reaction_r59) + ( 1.0 * reaction_r385) + (-1.0 * reaction_r386) + ( 1.0 * reaction_r391) + (-1.0 * reaction_r392) + (-1.0 * reaction_r395) + ( 1.0 * reaction_r396) + (-1.0 * reaction_r397) + ( 1.0 * reaction_r398) + (-1.0 * reaction_r824) + ( 1.0 * reaction_r850) + (-1.0 * reaction_r928) + ( 1.0 * reaction_r1084) + (-1.0 * reaction_r1240) + (-1.0 * reaction_r1266) + ( 1.0 * reaction_r1344) + ( 1.0 * reaction_r1396) + (-1.0 * reaction_r1448));
	
% Species:   id = R11UG, name = R11UG, affected by kineticLaw
	xdot(73) = (1/(compartment_cell))*((-1.0 * reaction_r51) + ( 1.0 * reaction_r61) + ( 1.0 * reaction_r387) + (-1.0 * reaction_r388) + (-1.0 * reaction_r399) + ( 1.0 * reaction_r400) + (-1.0 * reaction_r401) + ( 1.0 * reaction_r402) + (-1.0 * reaction_r403) + ( 1.0 * reaction_r404) + (-1.0 * reaction_r825) + ( 1.0 * reaction_r851) + (-1.0 * reaction_r929) + ( 1.0 * reaction_r1085) + (-1.0 * reaction_r1241) + (-1.0 * reaction_r1267) + ( 1.0 * reaction_r1345) + ( 1.0 * reaction_r1397) + (-1.0 * reaction_r1449));
	
% Species:   id = R11UL, name = R11UL, affected by kineticLaw
	xdot(74) = (1/(compartment_cell))*((-1.0 * reaction_r52) + ( 1.0 * reaction_r63) + ( 1.0 * reaction_r389) + (-1.0 * reaction_r390) + ( 1.0 * reaction_r403) + (-1.0 * reaction_r404) + (-1.0 * reaction_r405) + ( 1.0 * reaction_r406) + (-1.0 * reaction_r826) + ( 1.0 * reaction_r852) + (-1.0 * reaction_r930) + ( 1.0 * reaction_r1086) + (-1.0 * reaction_r1242) + (-1.0 * reaction_r1268) + ( 1.0 * reaction_r1346) + ( 1.0 * reaction_r1398) + (-1.0 * reaction_r1450));
	
% Species:   id = R11CG, name = R11CG, affected by kineticLaw
	xdot(75) = (1/(compartment_cell))*(( 1.0 * reaction_r64) + ( 1.0 * reaction_r393) + (-1.0 * reaction_r394) + ( 1.0 * reaction_r399) + (-1.0 * reaction_r400) + (-1.0 * reaction_r407) + ( 1.0 * reaction_r408) + (-1.0 * reaction_r409) + ( 1.0 * reaction_r410) + (-1.0 * reaction_r827) + ( 1.0 * reaction_r853) + (-1.0 * reaction_r931) + ( 1.0 * reaction_r1087) + (-1.0 * reaction_r1243) + (-1.0 * reaction_r1269) + ( 1.0 * reaction_r1347) + ( 1.0 * reaction_r1399) + (-1.0 * reaction_r1451));
	
% Species:   id = R11CC, name = R11CC, affected by kineticLaw
	xdot(76) = (1/(compartment_cell))*(( 1.0 * reaction_r65) + ( 1.0 * reaction_r397) + (-1.0 * reaction_r398) + ( 1.0 * reaction_r405) + (-1.0 * reaction_r406) + ( 1.0 * reaction_r407) + (-1.0 * reaction_r408) + (-1.0 * reaction_r828) + ( 1.0 * reaction_r854) + (-1.0 * reaction_r932) + ( 1.0 * reaction_r1088) + (-1.0 * reaction_r1244) + (-1.0 * reaction_r1270) + ( 1.0 * reaction_r1348) + ( 1.0 * reaction_r1400) + (-1.0 * reaction_r1452));
	
% Species:   id = R11LG, name = R11LG, affected by kineticLaw
	xdot(77) = (1/(compartment_cell))*(( 1.0 * reaction_r66) + ( 1.0 * reaction_r395) + (-1.0 * reaction_r396) + ( 1.0 * reaction_r401) + (-1.0 * reaction_r402) + ( 1.0 * reaction_r409) + (-1.0 * reaction_r410) + (-1.0 * reaction_r829) + ( 1.0 * reaction_r855) + (-1.0 * reaction_r933) + ( 1.0 * reaction_r1089) + (-1.0 * reaction_r1245) + (-1.0 * reaction_r1271) + ( 1.0 * reaction_r1349) + ( 1.0 * reaction_r1401) + (-1.0 * reaction_r1453));
	
% Species:   id = R02UU, name = R02UU, affected by kineticLaw
	xdot(78) = (1/(compartment_cell))*((-1.0 * reaction_r53) + ( 1.0 * reaction_r56) + (-1.0 * reaction_r411) + ( 1.0 * reaction_r412) + (-1.0 * reaction_r413) + ( 1.0 * reaction_r414) + (-1.0 * reaction_r830) + ( 1.0 * reaction_r856) + (-1.0 * reaction_r934) + ( 1.0 * reaction_r1090) + (-1.0 * reaction_r1246) + (-1.0 * reaction_r1272) + ( 1.0 * reaction_r1350) + ( 1.0 * reaction_r1402) + (-1.0 * reaction_r1454));
	
% Species:   id = R02UG, name = R02UG, affected by kineticLaw
	xdot(79) = (1/(compartment_cell))*((-1.0 * reaction_r54) + ( 1.0 * reaction_r60) + ( 1.0 * reaction_r411) + (-1.0 * reaction_r412) + (-1.0 * reaction_r415) + ( 1.0 * reaction_r416) + (-1.0 * reaction_r831) + ( 1.0 * reaction_r857) + (-1.0 * reaction_r935) + ( 1.0 * reaction_r1091) + (-1.0 * reaction_r1247) + (-1.0 * reaction_r1273) + ( 1.0 * reaction_r1351) + ( 1.0 * reaction_r1403) + (-1.0 * reaction_r1455));
	
% Species:   id = R02UL, name = R02UL, affected by kineticLaw
	xdot(80) = (1/(compartment_cell))*((-1.0 * reaction_r55) + ( 1.0 * reaction_r62) + ( 1.0 * reaction_r413) + (-1.0 * reaction_r414) + ( 1.0 * reaction_r415) + (-1.0 * reaction_r416) + (-1.0 * reaction_r832) + ( 1.0 * reaction_r858) + (-1.0 * reaction_r936) + ( 1.0 * reaction_r1092) + (-1.0 * reaction_r1248) + (-1.0 * reaction_r1274) + ( 1.0 * reaction_r1352) + ( 1.0 * reaction_r1404) + (-1.0 * reaction_r1456));
	
% Species:   id = R12UU, name = R12UU, affected by kineticLaw
	xdot(81) = (1/(compartment_cell))*((-1.0 * reaction_r56) + (-1.0 * reaction_r57) + (-1.0 * reaction_r417) + ( 1.0 * reaction_r418) + (-1.0 * reaction_r419) + ( 1.0 * reaction_r420) + (-1.0 * reaction_r421) + ( 1.0 * reaction_r422) + (-1.0 * reaction_r423) + ( 1.0 * reaction_r424) + (-1.0 * reaction_r833) + ( 1.0 * reaction_r859) + (-1.0 * reaction_r937) + ( 1.0 * reaction_r1093) + (-1.0 * reaction_r1249) + (-1.0 * reaction_r1275) + ( 1.0 * reaction_r1353) + ( 1.0 * reaction_r1405) + (-1.0 * reaction_r1457));
	
% Species:   id = R12CU, name = R12CU, affected by kineticLaw
	xdot(82) = (1/(compartment_cell))*((-1.0 * reaction_r58) + ( 1.0 * reaction_r417) + (-1.0 * reaction_r418) + (-1.0 * reaction_r425) + ( 1.0 * reaction_r426) + (-1.0 * reaction_r427) + ( 1.0 * reaction_r428) + (-1.0 * reaction_r834) + ( 1.0 * reaction_r860) + (-1.0 * reaction_r938) + ( 1.0 * reaction_r1094) + (-1.0 * reaction_r1250) + (-1.0 * reaction_r1276) + ( 1.0 * reaction_r1354) + ( 1.0 * reaction_r1406) + (-1.0 * reaction_r1458));
	
% Species:   id = R12LU, name = R12LU, affected by kineticLaw
	xdot(83) = (1/(compartment_cell))*((-1.0 * reaction_r59) + ( 1.0 * reaction_r419) + (-1.0 * reaction_r420) + ( 1.0 * reaction_r425) + (-1.0 * reaction_r426) + (-1.0 * reaction_r429) + ( 1.0 * reaction_r430) + (-1.0 * reaction_r431) + ( 1.0 * reaction_r432) + (-1.0 * reaction_r835) + ( 1.0 * reaction_r861) + (-1.0 * reaction_r939) + ( 1.0 * reaction_r1095) + (-1.0 * reaction_r1251) + (-1.0 * reaction_r1277) + ( 1.0 * reaction_r1355) + ( 1.0 * reaction_r1407) + (-1.0 * reaction_r1459));
	
% Species:   id = R12UG, name = R12UG, affected by kineticLaw
	xdot(84) = (1/(compartment_cell))*((-1.0 * reaction_r60) + (-1.0 * reaction_r61) + ( 1.0 * reaction_r421) + (-1.0 * reaction_r422) + (-1.0 * reaction_r433) + ( 1.0 * reaction_r434) + (-1.0 * reaction_r435) + ( 1.0 * reaction_r436) + (-1.0 * reaction_r437) + ( 1.0 * reaction_r438) + (-1.0 * reaction_r836) + ( 1.0 * reaction_r862) + (-1.0 * reaction_r940) + ( 1.0 * reaction_r1096) + (-1.0 * reaction_r1252) + (-1.0 * reaction_r1278) + ( 1.0 * reaction_r1356) + ( 1.0 * reaction_r1408) + (-1.0 * reaction_r1460));
	
% Species:   id = R12UL, name = R12UL, affected by kineticLaw
	xdot(85) = (1/(compartment_cell))*((-1.0 * reaction_r62) + (-1.0 * reaction_r63) + ( 1.0 * reaction_r423) + (-1.0 * reaction_r424) + ( 1.0 * reaction_r437) + (-1.0 * reaction_r438) + (-1.0 * reaction_r439) + ( 1.0 * reaction_r440) + (-1.0 * reaction_r837) + ( 1.0 * reaction_r863) + (-1.0 * reaction_r941) + ( 1.0 * reaction_r1097) + (-1.0 * reaction_r1253) + (-1.0 * reaction_r1279) + ( 1.0 * reaction_r1357) + ( 1.0 * reaction_r1409) + (-1.0 * reaction_r1461));
	
% Species:   id = R12CG, name = R12CG, affected by kineticLaw
	xdot(86) = (1/(compartment_cell))*((-1.0 * reaction_r64) + ( 1.0 * reaction_r427) + (-1.0 * reaction_r428) + ( 1.0 * reaction_r433) + (-1.0 * reaction_r434) + (-1.0 * reaction_r441) + ( 1.0 * reaction_r442) + (-1.0 * reaction_r443) + ( 1.0 * reaction_r444) + (-1.0 * reaction_r838) + ( 1.0 * reaction_r864) + (-1.0 * reaction_r942) + ( 1.0 * reaction_r1098) + (-1.0 * reaction_r1254) + (-1.0 * reaction_r1280) + ( 1.0 * reaction_r1358) + ( 1.0 * reaction_r1410) + (-1.0 * reaction_r1462));
	
% Species:   id = R12CC, name = R12CC, affected by kineticLaw
	xdot(87) = (1/(compartment_cell))*((-1.0 * reaction_r65) + ( 1.0 * reaction_r431) + (-1.0 * reaction_r432) + ( 1.0 * reaction_r439) + (-1.0 * reaction_r440) + ( 1.0 * reaction_r441) + (-1.0 * reaction_r442) + (-1.0 * reaction_r839) + ( 1.0 * reaction_r865) + (-1.0 * reaction_r943) + ( 1.0 * reaction_r1099) + (-1.0 * reaction_r1255) + (-1.0 * reaction_r1281) + ( 1.0 * reaction_r1359) + ( 1.0 * reaction_r1411) + (-1.0 * reaction_r1463));
	
% Species:   id = R12LG, name = R12LG, affected by kineticLaw
	xdot(88) = (1/(compartment_cell))*((-1.0 * reaction_r66) + ( 1.0 * reaction_r429) + (-1.0 * reaction_r430) + ( 1.0 * reaction_r435) + (-1.0 * reaction_r436) + ( 1.0 * reaction_r443) + (-1.0 * reaction_r444) + (-1.0 * reaction_r840) + ( 1.0 * reaction_r866) + (-1.0 * reaction_r944) + ( 1.0 * reaction_r1100) + (-1.0 * reaction_r1256) + (-1.0 * reaction_r1282) + ( 1.0 * reaction_r1360) + ( 1.0 * reaction_r1412) + (-1.0 * reaction_r1464));
	
% Species:   id = RL00UU, name = RL00UU, affected by kineticLaw
	xdot(89) = (1/(compartment_cell))*(( 1.0 * reaction_r67) + ( 1.0 * reaction_r68) + (-1.0 * reaction_r867) + ( 1.0 * reaction_r893) + ( 1.0 * reaction_r919) + (-1.0 * reaction_r1075) + (-1.0 * reaction_r1283) + (-1.0 * reaction_r1309) + ( 1.0 * reaction_r1361) + ( 1.0 * reaction_r1413) + (-1.0 * reaction_r1465));
	
% Species:   id = RL10UU, name = RL10UU, affected by kineticLaw
	xdot(90) = (1/(compartment_cell))*((-1.0 * reaction_r67) + ( 1.0 * reaction_r70) + (-1.0 * reaction_r445) + ( 1.0 * reaction_r446) + (-1.0 * reaction_r447) + ( 1.0 * reaction_r448) + (-1.0 * reaction_r868) + ( 1.0 * reaction_r894) + ( 1.0 * reaction_r920) + (-1.0 * reaction_r1076) + (-1.0 * reaction_r1284) + (-1.0 * reaction_r1310) + ( 1.0 * reaction_r1362) + ( 1.0 * reaction_r1414) + (-1.0 * reaction_r1466));
	
% Species:   id = RL10CU, name = RL10CU, affected by kineticLaw
	xdot(91) = (1/(compartment_cell))*(( 1.0 * reaction_r71) + ( 1.0 * reaction_r445) + (-1.0 * reaction_r446) + (-1.0 * reaction_r449) + ( 1.0 * reaction_r450) + (-1.0 * reaction_r869) + ( 1.0 * reaction_r895) + ( 1.0 * reaction_r921) + (-1.0 * reaction_r1077) + (-1.0 * reaction_r1285) + (-1.0 * reaction_r1311) + ( 1.0 * reaction_r1363) + ( 1.0 * reaction_r1415) + (-1.0 * reaction_r1467));
	
% Species:   id = RL10LU, name = RL10LU, affected by kineticLaw
	xdot(92) = (1/(compartment_cell))*(( 1.0 * reaction_r72) + ( 1.0 * reaction_r447) + (-1.0 * reaction_r448) + ( 1.0 * reaction_r449) + (-1.0 * reaction_r450) + (-1.0 * reaction_r870) + ( 1.0 * reaction_r896) + ( 1.0 * reaction_r922) + (-1.0 * reaction_r1078) + (-1.0 * reaction_r1286) + (-1.0 * reaction_r1312) + ( 1.0 * reaction_r1364) + ( 1.0 * reaction_r1416) + (-1.0 * reaction_r1468));
	
% Species:   id = RL01UU, name = RL01UU, affected by kineticLaw
	xdot(93) = (1/(compartment_cell))*((-1.0 * reaction_r68) + ( 1.0 * reaction_r69) + ( 1.0 * reaction_r75) + (-1.0 * reaction_r451) + ( 1.0 * reaction_r452) + (-1.0 * reaction_r453) + ( 1.0 * reaction_r454) + (-1.0 * reaction_r871) + ( 1.0 * reaction_r897) + ( 1.0 * reaction_r923) + (-1.0 * reaction_r1079) + (-1.0 * reaction_r1287) + (-1.0 * reaction_r1313) + ( 1.0 * reaction_r1365) + ( 1.0 * reaction_r1417) + (-1.0 * reaction_r1469));
	
% Species:   id = RL01UG, name = RL01UG, affected by kineticLaw
	xdot(94) = (1/(compartment_cell))*(( 1.0 * reaction_r73) + ( 1.0 * reaction_r76) + ( 1.0 * reaction_r451) + (-1.0 * reaction_r452) + (-1.0 * reaction_r455) + ( 1.0 * reaction_r456) + (-1.0 * reaction_r872) + ( 1.0 * reaction_r898) + ( 1.0 * reaction_r924) + (-1.0 * reaction_r1080) + (-1.0 * reaction_r1288) + (-1.0 * reaction_r1314) + ( 1.0 * reaction_r1366) + ( 1.0 * reaction_r1418) + (-1.0 * reaction_r1470));
	
% Species:   id = RL01UL, name = RL01UL, affected by kineticLaw
	xdot(95) = (1/(compartment_cell))*(( 1.0 * reaction_r74) + ( 1.0 * reaction_r77) + ( 1.0 * reaction_r453) + (-1.0 * reaction_r454) + ( 1.0 * reaction_r455) + (-1.0 * reaction_r456) + (-1.0 * reaction_r873) + ( 1.0 * reaction_r899) + ( 1.0 * reaction_r925) + (-1.0 * reaction_r1081) + (-1.0 * reaction_r1289) + (-1.0 * reaction_r1315) + ( 1.0 * reaction_r1367) + ( 1.0 * reaction_r1419) + (-1.0 * reaction_r1471));
	
% Species:   id = RL11UU, name = RL11UU, affected by kineticLaw
	xdot(96) = (1/(compartment_cell))*((-1.0 * reaction_r69) + (-1.0 * reaction_r70) + ( 1.0 * reaction_r79) + (-1.0 * reaction_r457) + ( 1.0 * reaction_r458) + (-1.0 * reaction_r459) + ( 1.0 * reaction_r460) + (-1.0 * reaction_r461) + ( 1.0 * reaction_r462) + (-1.0 * reaction_r463) + ( 1.0 * reaction_r464) + (-1.0 * reaction_r874) + ( 1.0 * reaction_r900) + ( 1.0 * reaction_r926) + (-1.0 * reaction_r1082) + (-1.0 * reaction_r1290) + (-1.0 * reaction_r1316) + ( 1.0 * reaction_r1368) + ( 1.0 * reaction_r1420) + (-1.0 * reaction_r1472));
	
% Species:   id = RL11CU, name = RL11CU, affected by kineticLaw
	xdot(97) = (1/(compartment_cell))*((-1.0 * reaction_r71) + ( 1.0 * reaction_r80) + ( 1.0 * reaction_r457) + (-1.0 * reaction_r458) + (-1.0 * reaction_r465) + ( 1.0 * reaction_r466) + (-1.0 * reaction_r467) + ( 1.0 * reaction_r468) + (-1.0 * reaction_r875) + ( 1.0 * reaction_r901) + ( 1.0 * reaction_r927) + (-1.0 * reaction_r1083) + (-1.0 * reaction_r1291) + (-1.0 * reaction_r1317) + ( 1.0 * reaction_r1369) + ( 1.0 * reaction_r1421) + (-1.0 * reaction_r1473));
	
% Species:   id = RL11LU, name = RL11LU, affected by kineticLaw
	xdot(98) = (1/(compartment_cell))*((-1.0 * reaction_r72) + ( 1.0 * reaction_r81) + ( 1.0 * reaction_r459) + (-1.0 * reaction_r460) + ( 1.0 * reaction_r465) + (-1.0 * reaction_r466) + (-1.0 * reaction_r469) + ( 1.0 * reaction_r470) + (-1.0 * reaction_r471) + ( 1.0 * reaction_r472) + (-1.0 * reaction_r876) + ( 1.0 * reaction_r902) + ( 1.0 * reaction_r928) + (-1.0 * reaction_r1084) + (-1.0 * reaction_r1292) + (-1.0 * reaction_r1318) + ( 1.0 * reaction_r1370) + ( 1.0 * reaction_r1422) + (-1.0 * reaction_r1474));
	
% Species:   id = RL11UG, name = RL11UG, affected by kineticLaw
	xdot(99) = (1/(compartment_cell))*((-1.0 * reaction_r73) + ( 1.0 * reaction_r83) + ( 1.0 * reaction_r461) + (-1.0 * reaction_r462) + (-1.0 * reaction_r473) + ( 1.0 * reaction_r474) + (-1.0 * reaction_r475) + ( 1.0 * reaction_r476) + (-1.0 * reaction_r477) + ( 1.0 * reaction_r478) + (-1.0 * reaction_r877) + ( 1.0 * reaction_r903) + ( 1.0 * reaction_r929) + (-1.0 * reaction_r1085) + (-1.0 * reaction_r1293) + (-1.0 * reaction_r1319) + ( 1.0 * reaction_r1371) + ( 1.0 * reaction_r1423) + (-1.0 * reaction_r1475));
	
% Species:   id = RL11UL, name = RL11UL, affected by kineticLaw
	xdot(100) = (1/(compartment_cell))*((-1.0 * reaction_r74) + ( 1.0 * reaction_r85) + ( 1.0 * reaction_r463) + (-1.0 * reaction_r464) + ( 1.0 * reaction_r477) + (-1.0 * reaction_r478) + (-1.0 * reaction_r479) + ( 1.0 * reaction_r480) + (-1.0 * reaction_r878) + ( 1.0 * reaction_r904) + ( 1.0 * reaction_r930) + (-1.0 * reaction_r1086) + (-1.0 * reaction_r1294) + (-1.0 * reaction_r1320) + ( 1.0 * reaction_r1372) + ( 1.0 * reaction_r1424) + (-1.0 * reaction_r1476));
	
% Species:   id = RL11CG, name = RL11CG, affected by kineticLaw
	xdot(101) = (1/(compartment_cell))*(( 1.0 * reaction_r86) + ( 1.0 * reaction_r467) + (-1.0 * reaction_r468) + ( 1.0 * reaction_r473) + (-1.0 * reaction_r474) + (-1.0 * reaction_r481) + ( 1.0 * reaction_r482) + (-1.0 * reaction_r483) + ( 1.0 * reaction_r484) + (-1.0 * reaction_r879) + ( 1.0 * reaction_r905) + ( 1.0 * reaction_r931) + (-1.0 * reaction_r1087) + (-1.0 * reaction_r1295) + (-1.0 * reaction_r1321) + ( 1.0 * reaction_r1373) + ( 1.0 * reaction_r1425) + (-1.0 * reaction_r1477));
	
% Species:   id = RL11CC, name = RL11CC, affected by kineticLaw
	xdot(102) = (1/(compartment_cell))*(( 1.0 * reaction_r87) + ( 1.0 * reaction_r471) + (-1.0 * reaction_r472) + ( 1.0 * reaction_r479) + (-1.0 * reaction_r480) + ( 1.0 * reaction_r481) + (-1.0 * reaction_r482) + (-1.0 * reaction_r880) + ( 1.0 * reaction_r906) + ( 1.0 * reaction_r932) + (-1.0 * reaction_r1088) + (-1.0 * reaction_r1296) + (-1.0 * reaction_r1322) + ( 1.0 * reaction_r1374) + ( 1.0 * reaction_r1426) + (-1.0 * reaction_r1478));
	
% Species:   id = RL11LG, name = RL11LG, affected by kineticLaw
	xdot(103) = (1/(compartment_cell))*(( 1.0 * reaction_r88) + ( 1.0 * reaction_r469) + (-1.0 * reaction_r470) + ( 1.0 * reaction_r475) + (-1.0 * reaction_r476) + ( 1.0 * reaction_r483) + (-1.0 * reaction_r484) + (-1.0 * reaction_r881) + ( 1.0 * reaction_r907) + ( 1.0 * reaction_r933) + (-1.0 * reaction_r1089) + (-1.0 * reaction_r1297) + (-1.0 * reaction_r1323) + ( 1.0 * reaction_r1375) + ( 1.0 * reaction_r1427) + (-1.0 * reaction_r1479));
	
% Species:   id = RL02UU, name = RL02UU, affected by kineticLaw
	xdot(104) = (1/(compartment_cell))*((-1.0 * reaction_r75) + ( 1.0 * reaction_r78) + (-1.0 * reaction_r485) + ( 1.0 * reaction_r486) + (-1.0 * reaction_r487) + ( 1.0 * reaction_r488) + (-1.0 * reaction_r882) + ( 1.0 * reaction_r908) + ( 1.0 * reaction_r934) + (-1.0 * reaction_r1090) + (-1.0 * reaction_r1298) + (-1.0 * reaction_r1324) + ( 1.0 * reaction_r1376) + ( 1.0 * reaction_r1428) + (-1.0 * reaction_r1480));
	
% Species:   id = RL02UG, name = RL02UG, affected by kineticLaw
	xdot(105) = (1/(compartment_cell))*((-1.0 * reaction_r76) + ( 1.0 * reaction_r82) + ( 1.0 * reaction_r485) + (-1.0 * reaction_r486) + (-1.0 * reaction_r489) + ( 1.0 * reaction_r490) + (-1.0 * reaction_r883) + ( 1.0 * reaction_r909) + ( 1.0 * reaction_r935) + (-1.0 * reaction_r1091) + (-1.0 * reaction_r1299) + (-1.0 * reaction_r1325) + ( 1.0 * reaction_r1377) + ( 1.0 * reaction_r1429) + (-1.0 * reaction_r1481));
	
% Species:   id = RL02UL, name = RL02UL, affected by kineticLaw
	xdot(106) = (1/(compartment_cell))*((-1.0 * reaction_r77) + ( 1.0 * reaction_r84) + ( 1.0 * reaction_r487) + (-1.0 * reaction_r488) + ( 1.0 * reaction_r489) + (-1.0 * reaction_r490) + (-1.0 * reaction_r884) + ( 1.0 * reaction_r910) + ( 1.0 * reaction_r936) + (-1.0 * reaction_r1092) + (-1.0 * reaction_r1300) + (-1.0 * reaction_r1326) + ( 1.0 * reaction_r1378) + ( 1.0 * reaction_r1430) + (-1.0 * reaction_r1482));
	
% Species:   id = RL12UU, name = RL12UU, affected by kineticLaw
	xdot(107) = (1/(compartment_cell))*((-1.0 * reaction_r78) + (-1.0 * reaction_r79) + (-1.0 * reaction_r491) + ( 1.0 * reaction_r492) + (-1.0 * reaction_r493) + ( 1.0 * reaction_r494) + (-1.0 * reaction_r495) + ( 1.0 * reaction_r496) + (-1.0 * reaction_r497) + ( 1.0 * reaction_r498) + (-1.0 * reaction_r885) + ( 1.0 * reaction_r911) + ( 1.0 * reaction_r937) + (-1.0 * reaction_r1093) + (-1.0 * reaction_r1301) + (-1.0 * reaction_r1327) + ( 1.0 * reaction_r1379) + ( 1.0 * reaction_r1431) + (-1.0 * reaction_r1483));
	
% Species:   id = RL12CU, name = RL12CU, affected by kineticLaw
	xdot(108) = (1/(compartment_cell))*((-1.0 * reaction_r80) + ( 1.0 * reaction_r491) + (-1.0 * reaction_r492) + (-1.0 * reaction_r499) + ( 1.0 * reaction_r500) + (-1.0 * reaction_r501) + ( 1.0 * reaction_r502) + (-1.0 * reaction_r886) + ( 1.0 * reaction_r912) + ( 1.0 * reaction_r938) + (-1.0 * reaction_r1094) + (-1.0 * reaction_r1302) + (-1.0 * reaction_r1328) + ( 1.0 * reaction_r1380) + ( 1.0 * reaction_r1432) + (-1.0 * reaction_r1484));
	
% Species:   id = RL12LU, name = RL12LU, affected by kineticLaw
	xdot(109) = (1/(compartment_cell))*((-1.0 * reaction_r81) + ( 1.0 * reaction_r493) + (-1.0 * reaction_r494) + ( 1.0 * reaction_r499) + (-1.0 * reaction_r500) + (-1.0 * reaction_r503) + ( 1.0 * reaction_r504) + (-1.0 * reaction_r505) + ( 1.0 * reaction_r506) + (-1.0 * reaction_r887) + ( 1.0 * reaction_r913) + ( 1.0 * reaction_r939) + (-1.0 * reaction_r1095) + (-1.0 * reaction_r1303) + (-1.0 * reaction_r1329) + ( 1.0 * reaction_r1381) + ( 1.0 * reaction_r1433) + (-1.0 * reaction_r1485));
	
% Species:   id = RL12UG, name = RL12UG, affected by kineticLaw
	xdot(110) = (1/(compartment_cell))*((-1.0 * reaction_r82) + (-1.0 * reaction_r83) + ( 1.0 * reaction_r495) + (-1.0 * reaction_r496) + (-1.0 * reaction_r507) + ( 1.0 * reaction_r508) + (-1.0 * reaction_r509) + ( 1.0 * reaction_r510) + (-1.0 * reaction_r511) + ( 1.0 * reaction_r512) + (-1.0 * reaction_r888) + ( 1.0 * reaction_r914) + ( 1.0 * reaction_r940) + (-1.0 * reaction_r1096) + (-1.0 * reaction_r1304) + (-1.0 * reaction_r1330) + ( 1.0 * reaction_r1382) + ( 1.0 * reaction_r1434) + (-1.0 * reaction_r1486));
	
% Species:   id = RL12UL, name = RL12UL, affected by kineticLaw
	xdot(111) = (1/(compartment_cell))*((-1.0 * reaction_r84) + (-1.0 * reaction_r85) + ( 1.0 * reaction_r497) + (-1.0 * reaction_r498) + ( 1.0 * reaction_r511) + (-1.0 * reaction_r512) + (-1.0 * reaction_r513) + ( 1.0 * reaction_r514) + (-1.0 * reaction_r889) + ( 1.0 * reaction_r915) + ( 1.0 * reaction_r941) + (-1.0 * reaction_r1097) + (-1.0 * reaction_r1305) + (-1.0 * reaction_r1331) + ( 1.0 * reaction_r1383) + ( 1.0 * reaction_r1435) + (-1.0 * reaction_r1487));
	
% Species:   id = RL12CG, name = RL12CG, affected by kineticLaw
	xdot(112) = (1/(compartment_cell))*((-1.0 * reaction_r86) + ( 1.0 * reaction_r501) + (-1.0 * reaction_r502) + ( 1.0 * reaction_r507) + (-1.0 * reaction_r508) + (-1.0 * reaction_r515) + ( 1.0 * reaction_r516) + (-1.0 * reaction_r517) + ( 1.0 * reaction_r518) + (-1.0 * reaction_r890) + ( 1.0 * reaction_r916) + ( 1.0 * reaction_r942) + (-1.0 * reaction_r1098) + (-1.0 * reaction_r1306) + (-1.0 * reaction_r1332) + ( 1.0 * reaction_r1384) + ( 1.0 * reaction_r1436) + (-1.0 * reaction_r1488));
	
% Species:   id = RL12CC, name = RL12CC, affected by kineticLaw
	xdot(113) = (1/(compartment_cell))*((-1.0 * reaction_r87) + ( 1.0 * reaction_r505) + (-1.0 * reaction_r506) + ( 1.0 * reaction_r513) + (-1.0 * reaction_r514) + ( 1.0 * reaction_r515) + (-1.0 * reaction_r516) + (-1.0 * reaction_r891) + ( 1.0 * reaction_r917) + ( 1.0 * reaction_r943) + (-1.0 * reaction_r1099) + (-1.0 * reaction_r1307) + (-1.0 * reaction_r1333) + ( 1.0 * reaction_r1385) + ( 1.0 * reaction_r1437) + (-1.0 * reaction_r1489));
	
% Species:   id = RL12LG, name = RL12LG, affected by kineticLaw
	xdot(114) = (1/(compartment_cell))*((-1.0 * reaction_r88) + ( 1.0 * reaction_r503) + (-1.0 * reaction_r504) + ( 1.0 * reaction_r509) + (-1.0 * reaction_r510) + ( 1.0 * reaction_r517) + (-1.0 * reaction_r518) + (-1.0 * reaction_r892) + ( 1.0 * reaction_r918) + ( 1.0 * reaction_r944) + (-1.0 * reaction_r1100) + (-1.0 * reaction_r1308) + (-1.0 * reaction_r1334) + ( 1.0 * reaction_r1386) + ( 1.0 * reaction_r1438) + (-1.0 * reaction_r1490));
	
% Species:   id = Di00UU, name = Di00UU, affected by kineticLaw
	xdot(115) = (1/(compartment_cell))*(( 1.0 * reaction_r89) + ( 1.0 * reaction_r90) + (-1.0 * reaction_r971) + (-1.0 * reaction_r997) + ( 1.0 * reaction_r1179) + ( 1.0 * reaction_r1205) + ( 1.0 * reaction_r1231) + (-1.0 * reaction_r1335) + ( 1.0 * reaction_r1439));
	
% Species:   id = Di10UU, name = Di10UU, affected by kineticLaw
	xdot(116) = (1/(compartment_cell))*((-1.0 * reaction_r89) + ( 1.0 * reaction_r92) + (-1.0 * reaction_r519) + ( 1.0 * reaction_r520) + (-1.0 * reaction_r521) + ( 1.0 * reaction_r522) + (-1.0 * reaction_r972) + (-1.0 * reaction_r998) + ( 1.0 * reaction_r1180) + ( 1.0 * reaction_r1206) + ( 1.0 * reaction_r1232) + (-1.0 * reaction_r1336) + ( 1.0 * reaction_r1440));
	
% Species:   id = Di10CU, name = Di10CU, affected by kineticLaw
	xdot(117) = (1/(compartment_cell))*(( 1.0 * reaction_r93) + ( 1.0 * reaction_r519) + (-1.0 * reaction_r520) + (-1.0 * reaction_r523) + ( 1.0 * reaction_r524) + (-1.0 * reaction_r973) + (-1.0 * reaction_r999) + ( 1.0 * reaction_r1181) + ( 1.0 * reaction_r1207) + ( 1.0 * reaction_r1233) + (-1.0 * reaction_r1337) + ( 1.0 * reaction_r1441));
	
% Species:   id = Di10LU, name = Di10LU, affected by kineticLaw
	xdot(118) = (1/(compartment_cell))*(( 1.0 * reaction_r94) + ( 1.0 * reaction_r521) + (-1.0 * reaction_r522) + ( 1.0 * reaction_r523) + (-1.0 * reaction_r524) + (-1.0 * reaction_r974) + (-1.0 * reaction_r1000) + ( 1.0 * reaction_r1182) + ( 1.0 * reaction_r1208) + ( 1.0 * reaction_r1234) + (-1.0 * reaction_r1338) + ( 1.0 * reaction_r1442));
	
% Species:   id = Di01UU, name = Di01UU, affected by kineticLaw
	xdot(119) = (1/(compartment_cell))*((-1.0 * reaction_r90) + ( 1.0 * reaction_r91) + ( 1.0 * reaction_r97) + (-1.0 * reaction_r525) + ( 1.0 * reaction_r526) + (-1.0 * reaction_r527) + ( 1.0 * reaction_r528) + (-1.0 * reaction_r975) + (-1.0 * reaction_r1001) + ( 1.0 * reaction_r1183) + ( 1.0 * reaction_r1209) + ( 1.0 * reaction_r1235) + (-1.0 * reaction_r1339) + ( 1.0 * reaction_r1443));
	
% Species:   id = Di01UG, name = Di01UG, affected by kineticLaw
	xdot(120) = (1/(compartment_cell))*(( 1.0 * reaction_r95) + ( 1.0 * reaction_r98) + ( 1.0 * reaction_r525) + (-1.0 * reaction_r526) + (-1.0 * reaction_r529) + ( 1.0 * reaction_r530) + (-1.0 * reaction_r976) + (-1.0 * reaction_r1002) + ( 1.0 * reaction_r1184) + ( 1.0 * reaction_r1210) + ( 1.0 * reaction_r1236) + (-1.0 * reaction_r1340) + ( 1.0 * reaction_r1444));
	
% Species:   id = Di01UL, name = Di01UL, affected by kineticLaw
	xdot(121) = (1/(compartment_cell))*(( 1.0 * reaction_r96) + ( 1.0 * reaction_r99) + ( 1.0 * reaction_r527) + (-1.0 * reaction_r528) + ( 1.0 * reaction_r529) + (-1.0 * reaction_r530) + (-1.0 * reaction_r977) + (-1.0 * reaction_r1003) + ( 1.0 * reaction_r1185) + ( 1.0 * reaction_r1211) + ( 1.0 * reaction_r1237) + (-1.0 * reaction_r1341) + ( 1.0 * reaction_r1445));
	
% Species:   id = Di11UU, name = Di11UU, affected by kineticLaw
	xdot(122) = (1/(compartment_cell))*((-1.0 * reaction_r91) + (-1.0 * reaction_r92) + ( 1.0 * reaction_r101) + (-1.0 * reaction_r531) + ( 1.0 * reaction_r532) + (-1.0 * reaction_r533) + ( 1.0 * reaction_r534) + (-1.0 * reaction_r535) + ( 1.0 * reaction_r536) + (-1.0 * reaction_r537) + ( 1.0 * reaction_r538) + (-1.0 * reaction_r978) + (-1.0 * reaction_r1004) + ( 1.0 * reaction_r1186) + ( 1.0 * reaction_r1212) + ( 1.0 * reaction_r1238) + (-1.0 * reaction_r1342) + ( 1.0 * reaction_r1446));
	
% Species:   id = Di11CU, name = Di11CU, affected by kineticLaw
	xdot(123) = (1/(compartment_cell))*((-1.0 * reaction_r93) + ( 1.0 * reaction_r102) + ( 1.0 * reaction_r531) + (-1.0 * reaction_r532) + (-1.0 * reaction_r539) + ( 1.0 * reaction_r540) + (-1.0 * reaction_r541) + ( 1.0 * reaction_r542) + (-1.0 * reaction_r979) + (-1.0 * reaction_r1005) + ( 1.0 * reaction_r1187) + ( 1.0 * reaction_r1213) + ( 1.0 * reaction_r1239) + (-1.0 * reaction_r1343) + ( 1.0 * reaction_r1447));
	
% Species:   id = Di11LU, name = Di11LU, affected by kineticLaw
	xdot(124) = (1/(compartment_cell))*((-1.0 * reaction_r94) + ( 1.0 * reaction_r103) + ( 1.0 * reaction_r533) + (-1.0 * reaction_r534) + ( 1.0 * reaction_r539) + (-1.0 * reaction_r540) + (-1.0 * reaction_r543) + ( 1.0 * reaction_r544) + (-1.0 * reaction_r545) + ( 1.0 * reaction_r546) + (-1.0 * reaction_r980) + (-1.0 * reaction_r1006) + ( 1.0 * reaction_r1188) + ( 1.0 * reaction_r1214) + ( 1.0 * reaction_r1240) + (-1.0 * reaction_r1344) + ( 1.0 * reaction_r1448));
	
% Species:   id = Di11UG, name = Di11UG, affected by kineticLaw
	xdot(125) = (1/(compartment_cell))*((-1.0 * reaction_r95) + ( 1.0 * reaction_r105) + ( 1.0 * reaction_r535) + (-1.0 * reaction_r536) + (-1.0 * reaction_r547) + ( 1.0 * reaction_r548) + (-1.0 * reaction_r549) + ( 1.0 * reaction_r550) + (-1.0 * reaction_r551) + ( 1.0 * reaction_r552) + (-1.0 * reaction_r981) + (-1.0 * reaction_r1007) + ( 1.0 * reaction_r1189) + ( 1.0 * reaction_r1215) + ( 1.0 * reaction_r1241) + (-1.0 * reaction_r1345) + ( 1.0 * reaction_r1449));
	
% Species:   id = Di11UL, name = Di11UL, affected by kineticLaw
	xdot(126) = (1/(compartment_cell))*((-1.0 * reaction_r96) + ( 1.0 * reaction_r107) + ( 1.0 * reaction_r537) + (-1.0 * reaction_r538) + ( 1.0 * reaction_r551) + (-1.0 * reaction_r552) + (-1.0 * reaction_r553) + ( 1.0 * reaction_r554) + (-1.0 * reaction_r982) + (-1.0 * reaction_r1008) + ( 1.0 * reaction_r1190) + ( 1.0 * reaction_r1216) + ( 1.0 * reaction_r1242) + (-1.0 * reaction_r1346) + ( 1.0 * reaction_r1450));
	
% Species:   id = Di11CG, name = Di11CG, affected by kineticLaw
	xdot(127) = (1/(compartment_cell))*(( 1.0 * reaction_r108) + ( 1.0 * reaction_r541) + (-1.0 * reaction_r542) + ( 1.0 * reaction_r547) + (-1.0 * reaction_r548) + (-1.0 * reaction_r555) + ( 1.0 * reaction_r556) + (-1.0 * reaction_r557) + ( 1.0 * reaction_r558) + (-1.0 * reaction_r983) + (-1.0 * reaction_r1009) + ( 1.0 * reaction_r1191) + ( 1.0 * reaction_r1217) + ( 1.0 * reaction_r1243) + (-1.0 * reaction_r1347) + ( 1.0 * reaction_r1451));
	
% Species:   id = Di11CC, name = Di11CC, affected by kineticLaw
	xdot(128) = (1/(compartment_cell))*(( 1.0 * reaction_r109) + ( 1.0 * reaction_r545) + (-1.0 * reaction_r546) + ( 1.0 * reaction_r553) + (-1.0 * reaction_r554) + ( 1.0 * reaction_r555) + (-1.0 * reaction_r556) + (-1.0 * reaction_r984) + (-1.0 * reaction_r1010) + ( 1.0 * reaction_r1192) + ( 1.0 * reaction_r1218) + ( 1.0 * reaction_r1244) + (-1.0 * reaction_r1348) + ( 1.0 * reaction_r1452));
	
% Species:   id = Di11LG, name = Di11LG, affected by kineticLaw
	xdot(129) = (1/(compartment_cell))*(( 1.0 * reaction_r110) + ( 1.0 * reaction_r543) + (-1.0 * reaction_r544) + ( 1.0 * reaction_r549) + (-1.0 * reaction_r550) + ( 1.0 * reaction_r557) + (-1.0 * reaction_r558) + (-1.0 * reaction_r985) + (-1.0 * reaction_r1011) + ( 1.0 * reaction_r1193) + ( 1.0 * reaction_r1219) + ( 1.0 * reaction_r1245) + (-1.0 * reaction_r1349) + ( 1.0 * reaction_r1453));
	
% Species:   id = Di02UU, name = Di02UU, affected by kineticLaw
	xdot(130) = (1/(compartment_cell))*((-1.0 * reaction_r97) + ( 1.0 * reaction_r100) + (-1.0 * reaction_r559) + ( 1.0 * reaction_r560) + (-1.0 * reaction_r561) + ( 1.0 * reaction_r562) + (-1.0 * reaction_r986) + (-1.0 * reaction_r1012) + ( 1.0 * reaction_r1194) + ( 1.0 * reaction_r1220) + ( 1.0 * reaction_r1246) + (-1.0 * reaction_r1350) + ( 1.0 * reaction_r1454));
	
% Species:   id = Di02UG, name = Di02UG, affected by kineticLaw
	xdot(131) = (1/(compartment_cell))*((-1.0 * reaction_r98) + ( 1.0 * reaction_r104) + ( 1.0 * reaction_r559) + (-1.0 * reaction_r560) + (-1.0 * reaction_r563) + ( 1.0 * reaction_r564) + (-1.0 * reaction_r987) + (-1.0 * reaction_r1013) + ( 1.0 * reaction_r1195) + ( 1.0 * reaction_r1221) + ( 1.0 * reaction_r1247) + (-1.0 * reaction_r1351) + ( 1.0 * reaction_r1455));
	
% Species:   id = Di02UL, name = Di02UL, affected by kineticLaw
	xdot(132) = (1/(compartment_cell))*((-1.0 * reaction_r99) + ( 1.0 * reaction_r106) + ( 1.0 * reaction_r561) + (-1.0 * reaction_r562) + ( 1.0 * reaction_r563) + (-1.0 * reaction_r564) + (-1.0 * reaction_r988) + (-1.0 * reaction_r1014) + ( 1.0 * reaction_r1196) + ( 1.0 * reaction_r1222) + ( 1.0 * reaction_r1248) + (-1.0 * reaction_r1352) + ( 1.0 * reaction_r1456));
	
% Species:   id = Di12UU, name = Di12UU, affected by kineticLaw
	xdot(133) = (1/(compartment_cell))*((-1.0 * reaction_r100) + (-1.0 * reaction_r101) + (-1.0 * reaction_r565) + ( 1.0 * reaction_r566) + (-1.0 * reaction_r567) + ( 1.0 * reaction_r568) + (-1.0 * reaction_r569) + ( 1.0 * reaction_r570) + (-1.0 * reaction_r571) + ( 1.0 * reaction_r572) + (-1.0 * reaction_r989) + (-1.0 * reaction_r1015) + ( 1.0 * reaction_r1197) + ( 1.0 * reaction_r1223) + ( 1.0 * reaction_r1249) + (-1.0 * reaction_r1353) + ( 1.0 * reaction_r1457));
	
% Species:   id = Di12CU, name = Di12CU, affected by kineticLaw
	xdot(134) = (1/(compartment_cell))*((-1.0 * reaction_r102) + ( 1.0 * reaction_r565) + (-1.0 * reaction_r566) + (-1.0 * reaction_r573) + ( 1.0 * reaction_r574) + (-1.0 * reaction_r575) + ( 1.0 * reaction_r576) + (-1.0 * reaction_r990) + (-1.0 * reaction_r1016) + ( 1.0 * reaction_r1198) + ( 1.0 * reaction_r1224) + ( 1.0 * reaction_r1250) + (-1.0 * reaction_r1354) + ( 1.0 * reaction_r1458));
	
% Species:   id = Di12LU, name = Di12LU, affected by kineticLaw
	xdot(135) = (1/(compartment_cell))*((-1.0 * reaction_r103) + ( 1.0 * reaction_r567) + (-1.0 * reaction_r568) + ( 1.0 * reaction_r573) + (-1.0 * reaction_r574) + (-1.0 * reaction_r577) + ( 1.0 * reaction_r578) + (-1.0 * reaction_r579) + ( 1.0 * reaction_r580) + (-1.0 * reaction_r991) + (-1.0 * reaction_r1017) + ( 1.0 * reaction_r1199) + ( 1.0 * reaction_r1225) + ( 1.0 * reaction_r1251) + (-1.0 * reaction_r1355) + ( 1.0 * reaction_r1459));
	
% Species:   id = Di12UG, name = Di12UG, affected by kineticLaw
	xdot(136) = (1/(compartment_cell))*((-1.0 * reaction_r104) + (-1.0 * reaction_r105) + ( 1.0 * reaction_r569) + (-1.0 * reaction_r570) + (-1.0 * reaction_r581) + ( 1.0 * reaction_r582) + (-1.0 * reaction_r583) + ( 1.0 * reaction_r584) + (-1.0 * reaction_r585) + ( 1.0 * reaction_r586) + (-1.0 * reaction_r992) + (-1.0 * reaction_r1018) + ( 1.0 * reaction_r1200) + ( 1.0 * reaction_r1226) + ( 1.0 * reaction_r1252) + (-1.0 * reaction_r1356) + ( 1.0 * reaction_r1460));
	
% Species:   id = Di12UL, name = Di12UL, affected by kineticLaw
	xdot(137) = (1/(compartment_cell))*((-1.0 * reaction_r106) + (-1.0 * reaction_r107) + ( 1.0 * reaction_r571) + (-1.0 * reaction_r572) + ( 1.0 * reaction_r585) + (-1.0 * reaction_r586) + (-1.0 * reaction_r587) + ( 1.0 * reaction_r588) + (-1.0 * reaction_r993) + (-1.0 * reaction_r1019) + ( 1.0 * reaction_r1201) + ( 1.0 * reaction_r1227) + ( 1.0 * reaction_r1253) + (-1.0 * reaction_r1357) + ( 1.0 * reaction_r1461));
	
% Species:   id = Di12CG, name = Di12CG, affected by kineticLaw
	xdot(138) = (1/(compartment_cell))*((-1.0 * reaction_r108) + ( 1.0 * reaction_r575) + (-1.0 * reaction_r576) + ( 1.0 * reaction_r581) + (-1.0 * reaction_r582) + (-1.0 * reaction_r589) + ( 1.0 * reaction_r590) + (-1.0 * reaction_r591) + ( 1.0 * reaction_r592) + (-1.0 * reaction_r994) + (-1.0 * reaction_r1020) + ( 1.0 * reaction_r1202) + ( 1.0 * reaction_r1228) + ( 1.0 * reaction_r1254) + (-1.0 * reaction_r1358) + ( 1.0 * reaction_r1462));
	
% Species:   id = Di12CC, name = Di12CC, affected by kineticLaw
	xdot(139) = (1/(compartment_cell))*((-1.0 * reaction_r109) + ( 1.0 * reaction_r579) + (-1.0 * reaction_r580) + ( 1.0 * reaction_r587) + (-1.0 * reaction_r588) + ( 1.0 * reaction_r589) + (-1.0 * reaction_r590) + (-1.0 * reaction_r995) + (-1.0 * reaction_r1021) + ( 1.0 * reaction_r1203) + ( 1.0 * reaction_r1229) + ( 1.0 * reaction_r1255) + (-1.0 * reaction_r1359) + ( 1.0 * reaction_r1463));
	
% Species:   id = Di12LG, name = Di12LG, affected by kineticLaw
	xdot(140) = (1/(compartment_cell))*((-1.0 * reaction_r110) + ( 1.0 * reaction_r577) + (-1.0 * reaction_r578) + ( 1.0 * reaction_r583) + (-1.0 * reaction_r584) + ( 1.0 * reaction_r591) + (-1.0 * reaction_r592) + (-1.0 * reaction_r996) + (-1.0 * reaction_r1022) + ( 1.0 * reaction_r1204) + ( 1.0 * reaction_r1230) + ( 1.0 * reaction_r1256) + (-1.0 * reaction_r1360) + ( 1.0 * reaction_r1464));
	
% Species:   id = Da00UU, name = Da00UU, affected by kineticLaw
	xdot(141) = (1/(compartment_cell))*(( 1.0 * reaction_r111) + ( 1.0 * reaction_r112) + (-1.0 * reaction_r177) + (-1.0 * reaction_r178) + ( 1.0 * reaction_r971) + (-1.0 * reaction_r1023) + ( 1.0 * reaction_r1153) + (-1.0 * reaction_r1179) + ( 1.0 * reaction_r1257) + (-1.0 * reaction_r1387));
	
% Species:   id = Da10UU, name = Da10UU, affected by kineticLaw
	xdot(142) = (1/(compartment_cell))*((-1.0 * reaction_r111) + ( 1.0 * reaction_r114) + ( 1.0 * reaction_r177) + (-1.0 * reaction_r179) + (-1.0 * reaction_r593) + ( 1.0 * reaction_r594) + (-1.0 * reaction_r595) + ( 1.0 * reaction_r596) + ( 1.0 * reaction_r972) + (-1.0 * reaction_r1024) + ( 1.0 * reaction_r1154) + (-1.0 * reaction_r1180) + ( 1.0 * reaction_r1258) + (-1.0 * reaction_r1388));
	
% Species:   id = Da10CU, name = Da10CU, affected by kineticLaw
	xdot(143) = (1/(compartment_cell))*(( 1.0 * reaction_r115) + (-1.0 * reaction_r180) + ( 1.0 * reaction_r593) + (-1.0 * reaction_r594) + (-1.0 * reaction_r597) + ( 1.0 * reaction_r598) + ( 1.0 * reaction_r973) + (-1.0 * reaction_r1025) + ( 1.0 * reaction_r1155) + (-1.0 * reaction_r1181) + ( 1.0 * reaction_r1259) + (-1.0 * reaction_r1389));
	
% Species:   id = Da10LU, name = Da10LU, affected by kineticLaw
	xdot(144) = (1/(compartment_cell))*(( 1.0 * reaction_r116) + (-1.0 * reaction_r181) + ( 1.0 * reaction_r595) + (-1.0 * reaction_r596) + ( 1.0 * reaction_r597) + (-1.0 * reaction_r598) + ( 1.0 * reaction_r974) + (-1.0 * reaction_r1026) + ( 1.0 * reaction_r1156) + (-1.0 * reaction_r1182) + ( 1.0 * reaction_r1260) + (-1.0 * reaction_r1390));
	
% Species:   id = Da01UU, name = Da01UU, affected by kineticLaw
	xdot(145) = (1/(compartment_cell))*((-1.0 * reaction_r112) + ( 1.0 * reaction_r113) + ( 1.0 * reaction_r119) + ( 1.0 * reaction_r178) + (-1.0 * reaction_r182) + (-1.0 * reaction_r183) + (-1.0 * reaction_r599) + ( 1.0 * reaction_r600) + (-1.0 * reaction_r601) + ( 1.0 * reaction_r602) + ( 1.0 * reaction_r975) + (-1.0 * reaction_r1027) + ( 1.0 * reaction_r1157) + (-1.0 * reaction_r1183) + ( 1.0 * reaction_r1261) + (-1.0 * reaction_r1391));
	
% Species:   id = Da01UG, name = Da01UG, affected by kineticLaw
	xdot(146) = (1/(compartment_cell))*(( 1.0 * reaction_r117) + ( 1.0 * reaction_r120) + (-1.0 * reaction_r184) + (-1.0 * reaction_r185) + ( 1.0 * reaction_r599) + (-1.0 * reaction_r600) + (-1.0 * reaction_r603) + ( 1.0 * reaction_r604) + ( 1.0 * reaction_r976) + (-1.0 * reaction_r1028) + ( 1.0 * reaction_r1158) + (-1.0 * reaction_r1184) + ( 1.0 * reaction_r1262) + (-1.0 * reaction_r1392));
	
% Species:   id = Da01UL, name = Da01UL, affected by kineticLaw
	xdot(147) = (1/(compartment_cell))*(( 1.0 * reaction_r118) + ( 1.0 * reaction_r121) + (-1.0 * reaction_r186) + (-1.0 * reaction_r187) + ( 1.0 * reaction_r601) + (-1.0 * reaction_r602) + ( 1.0 * reaction_r603) + (-1.0 * reaction_r604) + ( 1.0 * reaction_r977) + (-1.0 * reaction_r1029) + ( 1.0 * reaction_r1159) + (-1.0 * reaction_r1185) + ( 1.0 * reaction_r1263) + (-1.0 * reaction_r1393));
	
% Species:   id = Da11UU, name = Da11UU, affected by kineticLaw
	xdot(148) = (1/(compartment_cell))*((-1.0 * reaction_r113) + (-1.0 * reaction_r114) + ( 1.0 * reaction_r123) + ( 1.0 * reaction_r179) + ( 1.0 * reaction_r182) + (-1.0 * reaction_r188) + (-1.0 * reaction_r605) + ( 1.0 * reaction_r606) + (-1.0 * reaction_r607) + ( 1.0 * reaction_r608) + (-1.0 * reaction_r609) + ( 1.0 * reaction_r610) + (-1.0 * reaction_r611) + ( 1.0 * reaction_r612) + ( 1.0 * reaction_r978) + (-1.0 * reaction_r1030) + ( 1.0 * reaction_r1160) + (-1.0 * reaction_r1186) + ( 1.0 * reaction_r1264) + (-1.0 * reaction_r1394));
	
% Species:   id = Da11CU, name = Da11CU, affected by kineticLaw
	xdot(149) = (1/(compartment_cell))*((-1.0 * reaction_r115) + ( 1.0 * reaction_r124) + ( 1.0 * reaction_r180) + (-1.0 * reaction_r189) + ( 1.0 * reaction_r605) + (-1.0 * reaction_r606) + (-1.0 * reaction_r613) + ( 1.0 * reaction_r614) + (-1.0 * reaction_r615) + ( 1.0 * reaction_r616) + ( 1.0 * reaction_r979) + (-1.0 * reaction_r1031) + ( 1.0 * reaction_r1161) + (-1.0 * reaction_r1187) + ( 1.0 * reaction_r1265) + (-1.0 * reaction_r1395));
	
% Species:   id = Da11LU, name = Da11LU, affected by kineticLaw
	xdot(150) = (1/(compartment_cell))*((-1.0 * reaction_r116) + ( 1.0 * reaction_r125) + ( 1.0 * reaction_r181) + (-1.0 * reaction_r190) + ( 1.0 * reaction_r607) + (-1.0 * reaction_r608) + ( 1.0 * reaction_r613) + (-1.0 * reaction_r614) + (-1.0 * reaction_r617) + ( 1.0 * reaction_r618) + (-1.0 * reaction_r619) + ( 1.0 * reaction_r620) + ( 1.0 * reaction_r980) + (-1.0 * reaction_r1032) + ( 1.0 * reaction_r1162) + (-1.0 * reaction_r1188) + ( 1.0 * reaction_r1266) + (-1.0 * reaction_r1396));
	
% Species:   id = Da11UG, name = Da11UG, affected by kineticLaw
	xdot(151) = (1/(compartment_cell))*((-1.0 * reaction_r117) + ( 1.0 * reaction_r127) + ( 1.0 * reaction_r184) + (-1.0 * reaction_r191) + ( 1.0 * reaction_r609) + (-1.0 * reaction_r610) + (-1.0 * reaction_r621) + ( 1.0 * reaction_r622) + (-1.0 * reaction_r623) + ( 1.0 * reaction_r624) + (-1.0 * reaction_r625) + ( 1.0 * reaction_r626) + ( 1.0 * reaction_r981) + (-1.0 * reaction_r1033) + ( 1.0 * reaction_r1163) + (-1.0 * reaction_r1189) + ( 1.0 * reaction_r1267) + (-1.0 * reaction_r1397));
	
% Species:   id = Da11UL, name = Da11UL, affected by kineticLaw
	xdot(152) = (1/(compartment_cell))*((-1.0 * reaction_r118) + ( 1.0 * reaction_r129) + ( 1.0 * reaction_r186) + (-1.0 * reaction_r192) + ( 1.0 * reaction_r611) + (-1.0 * reaction_r612) + ( 1.0 * reaction_r625) + (-1.0 * reaction_r626) + (-1.0 * reaction_r627) + ( 1.0 * reaction_r628) + ( 1.0 * reaction_r982) + (-1.0 * reaction_r1034) + ( 1.0 * reaction_r1164) + (-1.0 * reaction_r1190) + ( 1.0 * reaction_r1268) + (-1.0 * reaction_r1398));
	
% Species:   id = Da11CG, name = Da11CG, affected by kineticLaw
	xdot(153) = (1/(compartment_cell))*(( 1.0 * reaction_r130) + (-1.0 * reaction_r193) + ( 1.0 * reaction_r615) + (-1.0 * reaction_r616) + ( 1.0 * reaction_r621) + (-1.0 * reaction_r622) + (-1.0 * reaction_r629) + ( 1.0 * reaction_r630) + (-1.0 * reaction_r631) + ( 1.0 * reaction_r632) + ( 1.0 * reaction_r983) + (-1.0 * reaction_r1035) + ( 1.0 * reaction_r1165) + (-1.0 * reaction_r1191) + ( 1.0 * reaction_r1269) + (-1.0 * reaction_r1399));
	
% Species:   id = Da11CC, name = Da11CC, affected by kineticLaw
	xdot(154) = (1/(compartment_cell))*(( 1.0 * reaction_r131) + (-1.0 * reaction_r194) + ( 1.0 * reaction_r619) + (-1.0 * reaction_r620) + ( 1.0 * reaction_r627) + (-1.0 * reaction_r628) + ( 1.0 * reaction_r629) + (-1.0 * reaction_r630) + ( 1.0 * reaction_r984) + (-1.0 * reaction_r1036) + ( 1.0 * reaction_r1166) + (-1.0 * reaction_r1192) + ( 1.0 * reaction_r1270) + (-1.0 * reaction_r1400));
	
% Species:   id = Da11LG, name = Da11LG, affected by kineticLaw
	xdot(155) = (1/(compartment_cell))*(( 1.0 * reaction_r132) + (-1.0 * reaction_r195) + ( 1.0 * reaction_r617) + (-1.0 * reaction_r618) + ( 1.0 * reaction_r623) + (-1.0 * reaction_r624) + ( 1.0 * reaction_r631) + (-1.0 * reaction_r632) + ( 1.0 * reaction_r985) + (-1.0 * reaction_r1037) + ( 1.0 * reaction_r1167) + (-1.0 * reaction_r1193) + ( 1.0 * reaction_r1271) + (-1.0 * reaction_r1401));
	
% Species:   id = Da02UU, name = Da02UU, affected by kineticLaw
	xdot(156) = (1/(compartment_cell))*((-1.0 * reaction_r119) + ( 1.0 * reaction_r122) + ( 1.0 * reaction_r183) + (-1.0 * reaction_r196) + (-1.0 * reaction_r633) + ( 1.0 * reaction_r634) + (-1.0 * reaction_r635) + ( 1.0 * reaction_r636) + ( 1.0 * reaction_r986) + (-1.0 * reaction_r1038) + ( 1.0 * reaction_r1168) + (-1.0 * reaction_r1194) + ( 1.0 * reaction_r1272) + (-1.0 * reaction_r1402));
	
% Species:   id = Da02UG, name = Da02UG, affected by kineticLaw
	xdot(157) = (1/(compartment_cell))*((-1.0 * reaction_r120) + ( 1.0 * reaction_r126) + ( 1.0 * reaction_r185) + (-1.0 * reaction_r197) + ( 1.0 * reaction_r633) + (-1.0 * reaction_r634) + (-1.0 * reaction_r637) + ( 1.0 * reaction_r638) + ( 1.0 * reaction_r987) + (-1.0 * reaction_r1039) + ( 1.0 * reaction_r1169) + (-1.0 * reaction_r1195) + ( 1.0 * reaction_r1273) + (-1.0 * reaction_r1403));
	
% Species:   id = Da02UL, name = Da02UL, affected by kineticLaw
	xdot(158) = (1/(compartment_cell))*((-1.0 * reaction_r121) + ( 1.0 * reaction_r128) + ( 1.0 * reaction_r187) + (-1.0 * reaction_r198) + ( 1.0 * reaction_r635) + (-1.0 * reaction_r636) + ( 1.0 * reaction_r637) + (-1.0 * reaction_r638) + ( 1.0 * reaction_r988) + (-1.0 * reaction_r1040) + ( 1.0 * reaction_r1170) + (-1.0 * reaction_r1196) + ( 1.0 * reaction_r1274) + (-1.0 * reaction_r1404));
	
% Species:   id = Da12UU, name = Da12UU, affected by kineticLaw
	xdot(159) = (1/(compartment_cell))*((-1.0 * reaction_r122) + (-1.0 * reaction_r123) + ( 1.0 * reaction_r188) + ( 1.0 * reaction_r196) + (-1.0 * reaction_r639) + ( 1.0 * reaction_r640) + (-1.0 * reaction_r641) + ( 1.0 * reaction_r642) + (-1.0 * reaction_r643) + ( 1.0 * reaction_r644) + (-1.0 * reaction_r645) + ( 1.0 * reaction_r646) + ( 1.0 * reaction_r989) + (-1.0 * reaction_r1041) + ( 1.0 * reaction_r1171) + (-1.0 * reaction_r1197) + ( 1.0 * reaction_r1275) + (-1.0 * reaction_r1405));
	
% Species:   id = Da12CU, name = Da12CU, affected by kineticLaw
	xdot(160) = (1/(compartment_cell))*((-1.0 * reaction_r124) + ( 1.0 * reaction_r189) + ( 1.0 * reaction_r639) + (-1.0 * reaction_r640) + (-1.0 * reaction_r647) + ( 1.0 * reaction_r648) + (-1.0 * reaction_r649) + ( 1.0 * reaction_r650) + ( 1.0 * reaction_r990) + (-1.0 * reaction_r1042) + ( 1.0 * reaction_r1172) + (-1.0 * reaction_r1198) + ( 1.0 * reaction_r1276) + (-1.0 * reaction_r1406));
	
% Species:   id = Da12LU, name = Da12LU, affected by kineticLaw
	xdot(161) = (1/(compartment_cell))*((-1.0 * reaction_r125) + ( 1.0 * reaction_r190) + ( 1.0 * reaction_r641) + (-1.0 * reaction_r642) + ( 1.0 * reaction_r647) + (-1.0 * reaction_r648) + (-1.0 * reaction_r651) + ( 1.0 * reaction_r652) + (-1.0 * reaction_r653) + ( 1.0 * reaction_r654) + ( 1.0 * reaction_r991) + (-1.0 * reaction_r1043) + ( 1.0 * reaction_r1173) + (-1.0 * reaction_r1199) + ( 1.0 * reaction_r1277) + (-1.0 * reaction_r1407));
	
% Species:   id = Da12UG, name = Da12UG, affected by kineticLaw
	xdot(162) = (1/(compartment_cell))*((-1.0 * reaction_r126) + (-1.0 * reaction_r127) + ( 1.0 * reaction_r191) + ( 1.0 * reaction_r197) + ( 1.0 * reaction_r643) + (-1.0 * reaction_r644) + (-1.0 * reaction_r655) + ( 1.0 * reaction_r656) + (-1.0 * reaction_r657) + ( 1.0 * reaction_r658) + (-1.0 * reaction_r659) + ( 1.0 * reaction_r660) + ( 1.0 * reaction_r992) + (-1.0 * reaction_r1044) + ( 1.0 * reaction_r1174) + (-1.0 * reaction_r1200) + ( 1.0 * reaction_r1278) + (-1.0 * reaction_r1408));
	
% Species:   id = Da12UL, name = Da12UL, affected by kineticLaw
	xdot(163) = (1/(compartment_cell))*((-1.0 * reaction_r128) + (-1.0 * reaction_r129) + ( 1.0 * reaction_r192) + ( 1.0 * reaction_r198) + ( 1.0 * reaction_r645) + (-1.0 * reaction_r646) + ( 1.0 * reaction_r659) + (-1.0 * reaction_r660) + (-1.0 * reaction_r661) + ( 1.0 * reaction_r662) + ( 1.0 * reaction_r993) + (-1.0 * reaction_r1045) + ( 1.0 * reaction_r1175) + (-1.0 * reaction_r1201) + ( 1.0 * reaction_r1279) + (-1.0 * reaction_r1409));
	
% Species:   id = Da12CG, name = Da12CG, affected by kineticLaw
	xdot(164) = (1/(compartment_cell))*((-1.0 * reaction_r130) + ( 1.0 * reaction_r193) + ( 1.0 * reaction_r649) + (-1.0 * reaction_r650) + ( 1.0 * reaction_r655) + (-1.0 * reaction_r656) + (-1.0 * reaction_r663) + ( 1.0 * reaction_r664) + (-1.0 * reaction_r665) + ( 1.0 * reaction_r666) + ( 1.0 * reaction_r994) + (-1.0 * reaction_r1046) + ( 1.0 * reaction_r1176) + (-1.0 * reaction_r1202) + ( 1.0 * reaction_r1280) + (-1.0 * reaction_r1410));
	
% Species:   id = Da12CC, name = Da12CC, affected by kineticLaw
	xdot(165) = (1/(compartment_cell))*((-1.0 * reaction_r131) + ( 1.0 * reaction_r194) + ( 1.0 * reaction_r653) + (-1.0 * reaction_r654) + ( 1.0 * reaction_r661) + (-1.0 * reaction_r662) + ( 1.0 * reaction_r663) + (-1.0 * reaction_r664) + ( 1.0 * reaction_r995) + (-1.0 * reaction_r1047) + ( 1.0 * reaction_r1177) + (-1.0 * reaction_r1203) + ( 1.0 * reaction_r1281) + (-1.0 * reaction_r1411));
	
% Species:   id = Da12LG, name = Da12LG, affected by kineticLaw
	xdot(166) = (1/(compartment_cell))*((-1.0 * reaction_r132) + ( 1.0 * reaction_r195) + ( 1.0 * reaction_r651) + (-1.0 * reaction_r652) + ( 1.0 * reaction_r657) + (-1.0 * reaction_r658) + ( 1.0 * reaction_r665) + (-1.0 * reaction_r666) + ( 1.0 * reaction_r996) + (-1.0 * reaction_r1048) + ( 1.0 * reaction_r1178) + (-1.0 * reaction_r1204) + ( 1.0 * reaction_r1282) + (-1.0 * reaction_r1412));
	
% Species:   id = DiL00UU, name = DiL00UU, affected by kineticLaw
	xdot(167) = (1/(compartment_cell))*(( 1.0 * reaction_r133) + ( 1.0 * reaction_r134) + ( 1.0 * reaction_r997) + (-1.0 * reaction_r1049) + ( 1.0 * reaction_r1127) + (-1.0 * reaction_r1205) + ( 1.0 * reaction_r1283) + (-1.0 * reaction_r1361));
	
% Species:   id = DiL10UU, name = DiL10UU, affected by kineticLaw
	xdot(168) = (1/(compartment_cell))*((-1.0 * reaction_r133) + ( 1.0 * reaction_r136) + (-1.0 * reaction_r667) + ( 1.0 * reaction_r668) + (-1.0 * reaction_r669) + ( 1.0 * reaction_r670) + ( 1.0 * reaction_r998) + (-1.0 * reaction_r1050) + ( 1.0 * reaction_r1128) + (-1.0 * reaction_r1206) + ( 1.0 * reaction_r1284) + (-1.0 * reaction_r1362));
	
% Species:   id = DiL10CU, name = DiL10CU, affected by kineticLaw
	xdot(169) = (1/(compartment_cell))*(( 1.0 * reaction_r137) + ( 1.0 * reaction_r667) + (-1.0 * reaction_r668) + (-1.0 * reaction_r671) + ( 1.0 * reaction_r672) + ( 1.0 * reaction_r999) + (-1.0 * reaction_r1051) + ( 1.0 * reaction_r1129) + (-1.0 * reaction_r1207) + ( 1.0 * reaction_r1285) + (-1.0 * reaction_r1363));
	
% Species:   id = DiL10LU, name = DiL10LU, affected by kineticLaw
	xdot(170) = (1/(compartment_cell))*(( 1.0 * reaction_r138) + ( 1.0 * reaction_r669) + (-1.0 * reaction_r670) + ( 1.0 * reaction_r671) + (-1.0 * reaction_r672) + ( 1.0 * reaction_r1000) + (-1.0 * reaction_r1052) + ( 1.0 * reaction_r1130) + (-1.0 * reaction_r1208) + ( 1.0 * reaction_r1286) + (-1.0 * reaction_r1364));
	
% Species:   id = DiL01UU, name = DiL01UU, affected by kineticLaw
	xdot(171) = (1/(compartment_cell))*((-1.0 * reaction_r134) + ( 1.0 * reaction_r135) + ( 1.0 * reaction_r141) + (-1.0 * reaction_r673) + ( 1.0 * reaction_r674) + (-1.0 * reaction_r675) + ( 1.0 * reaction_r676) + ( 1.0 * reaction_r1001) + (-1.0 * reaction_r1053) + ( 1.0 * reaction_r1131) + (-1.0 * reaction_r1209) + ( 1.0 * reaction_r1287) + (-1.0 * reaction_r1365));
	
% Species:   id = DiL01UG, name = DiL01UG, affected by kineticLaw
	xdot(172) = (1/(compartment_cell))*(( 1.0 * reaction_r139) + ( 1.0 * reaction_r142) + ( 1.0 * reaction_r673) + (-1.0 * reaction_r674) + (-1.0 * reaction_r677) + ( 1.0 * reaction_r678) + ( 1.0 * reaction_r1002) + (-1.0 * reaction_r1054) + ( 1.0 * reaction_r1132) + (-1.0 * reaction_r1210) + ( 1.0 * reaction_r1288) + (-1.0 * reaction_r1366));
	
% Species:   id = DiL01UL, name = DiL01UL, affected by kineticLaw
	xdot(173) = (1/(compartment_cell))*(( 1.0 * reaction_r140) + ( 1.0 * reaction_r143) + ( 1.0 * reaction_r675) + (-1.0 * reaction_r676) + ( 1.0 * reaction_r677) + (-1.0 * reaction_r678) + ( 1.0 * reaction_r1003) + (-1.0 * reaction_r1055) + ( 1.0 * reaction_r1133) + (-1.0 * reaction_r1211) + ( 1.0 * reaction_r1289) + (-1.0 * reaction_r1367));
	
% Species:   id = DiL11UU, name = DiL11UU, affected by kineticLaw
	xdot(174) = (1/(compartment_cell))*((-1.0 * reaction_r135) + (-1.0 * reaction_r136) + ( 1.0 * reaction_r145) + (-1.0 * reaction_r679) + ( 1.0 * reaction_r680) + (-1.0 * reaction_r681) + ( 1.0 * reaction_r682) + (-1.0 * reaction_r683) + ( 1.0 * reaction_r684) + (-1.0 * reaction_r685) + ( 1.0 * reaction_r686) + ( 1.0 * reaction_r1004) + (-1.0 * reaction_r1056) + ( 1.0 * reaction_r1134) + (-1.0 * reaction_r1212) + ( 1.0 * reaction_r1290) + (-1.0 * reaction_r1368));
	
% Species:   id = DiL11CU, name = DiL11CU, affected by kineticLaw
	xdot(175) = (1/(compartment_cell))*((-1.0 * reaction_r137) + ( 1.0 * reaction_r146) + ( 1.0 * reaction_r679) + (-1.0 * reaction_r680) + (-1.0 * reaction_r687) + ( 1.0 * reaction_r688) + (-1.0 * reaction_r689) + ( 1.0 * reaction_r690) + ( 1.0 * reaction_r1005) + (-1.0 * reaction_r1057) + ( 1.0 * reaction_r1135) + (-1.0 * reaction_r1213) + ( 1.0 * reaction_r1291) + (-1.0 * reaction_r1369));
	
% Species:   id = DiL11LU, name = DiL11LU, affected by kineticLaw
	xdot(176) = (1/(compartment_cell))*((-1.0 * reaction_r138) + ( 1.0 * reaction_r147) + ( 1.0 * reaction_r681) + (-1.0 * reaction_r682) + ( 1.0 * reaction_r687) + (-1.0 * reaction_r688) + (-1.0 * reaction_r691) + ( 1.0 * reaction_r692) + (-1.0 * reaction_r693) + ( 1.0 * reaction_r694) + ( 1.0 * reaction_r1006) + (-1.0 * reaction_r1058) + ( 1.0 * reaction_r1136) + (-1.0 * reaction_r1214) + ( 1.0 * reaction_r1292) + (-1.0 * reaction_r1370));
	
% Species:   id = DiL11UG, name = DiL11UG, affected by kineticLaw
	xdot(177) = (1/(compartment_cell))*((-1.0 * reaction_r139) + ( 1.0 * reaction_r149) + ( 1.0 * reaction_r683) + (-1.0 * reaction_r684) + (-1.0 * reaction_r695) + ( 1.0 * reaction_r696) + (-1.0 * reaction_r697) + ( 1.0 * reaction_r698) + (-1.0 * reaction_r699) + ( 1.0 * reaction_r700) + ( 1.0 * reaction_r1007) + (-1.0 * reaction_r1059) + ( 1.0 * reaction_r1137) + (-1.0 * reaction_r1215) + ( 1.0 * reaction_r1293) + (-1.0 * reaction_r1371));
	
% Species:   id = DiL11UL, name = DiL11UL, affected by kineticLaw
	xdot(178) = (1/(compartment_cell))*((-1.0 * reaction_r140) + ( 1.0 * reaction_r151) + ( 1.0 * reaction_r685) + (-1.0 * reaction_r686) + ( 1.0 * reaction_r699) + (-1.0 * reaction_r700) + (-1.0 * reaction_r701) + ( 1.0 * reaction_r702) + ( 1.0 * reaction_r1008) + (-1.0 * reaction_r1060) + ( 1.0 * reaction_r1138) + (-1.0 * reaction_r1216) + ( 1.0 * reaction_r1294) + (-1.0 * reaction_r1372));
	
% Species:   id = DiL11CG, name = DiL11CG, affected by kineticLaw
	xdot(179) = (1/(compartment_cell))*(( 1.0 * reaction_r152) + ( 1.0 * reaction_r689) + (-1.0 * reaction_r690) + ( 1.0 * reaction_r695) + (-1.0 * reaction_r696) + (-1.0 * reaction_r703) + ( 1.0 * reaction_r704) + (-1.0 * reaction_r705) + ( 1.0 * reaction_r706) + ( 1.0 * reaction_r1009) + (-1.0 * reaction_r1061) + ( 1.0 * reaction_r1139) + (-1.0 * reaction_r1217) + ( 1.0 * reaction_r1295) + (-1.0 * reaction_r1373));
	
% Species:   id = DiL11CC, name = DiL11CC, affected by kineticLaw
	xdot(180) = (1/(compartment_cell))*(( 1.0 * reaction_r153) + ( 1.0 * reaction_r693) + (-1.0 * reaction_r694) + ( 1.0 * reaction_r701) + (-1.0 * reaction_r702) + ( 1.0 * reaction_r703) + (-1.0 * reaction_r704) + ( 1.0 * reaction_r1010) + (-1.0 * reaction_r1062) + ( 1.0 * reaction_r1140) + (-1.0 * reaction_r1218) + ( 1.0 * reaction_r1296) + (-1.0 * reaction_r1374));
	
% Species:   id = DiL11LG, name = DiL11LG, affected by kineticLaw
	xdot(181) = (1/(compartment_cell))*(( 1.0 * reaction_r154) + ( 1.0 * reaction_r691) + (-1.0 * reaction_r692) + ( 1.0 * reaction_r697) + (-1.0 * reaction_r698) + ( 1.0 * reaction_r705) + (-1.0 * reaction_r706) + ( 1.0 * reaction_r1011) + (-1.0 * reaction_r1063) + ( 1.0 * reaction_r1141) + (-1.0 * reaction_r1219) + ( 1.0 * reaction_r1297) + (-1.0 * reaction_r1375));
	
% Species:   id = DiL02UU, name = DiL02UU, affected by kineticLaw
	xdot(182) = (1/(compartment_cell))*((-1.0 * reaction_r141) + ( 1.0 * reaction_r144) + (-1.0 * reaction_r707) + ( 1.0 * reaction_r708) + (-1.0 * reaction_r709) + ( 1.0 * reaction_r710) + ( 1.0 * reaction_r1012) + (-1.0 * reaction_r1064) + ( 1.0 * reaction_r1142) + (-1.0 * reaction_r1220) + ( 1.0 * reaction_r1298) + (-1.0 * reaction_r1376));
	
% Species:   id = DiL02UG, name = DiL02UG, affected by kineticLaw
	xdot(183) = (1/(compartment_cell))*((-1.0 * reaction_r142) + ( 1.0 * reaction_r148) + ( 1.0 * reaction_r707) + (-1.0 * reaction_r708) + (-1.0 * reaction_r711) + ( 1.0 * reaction_r712) + ( 1.0 * reaction_r1013) + (-1.0 * reaction_r1065) + ( 1.0 * reaction_r1143) + (-1.0 * reaction_r1221) + ( 1.0 * reaction_r1299) + (-1.0 * reaction_r1377));
	
% Species:   id = DiL02UL, name = DiL02UL, affected by kineticLaw
	xdot(184) = (1/(compartment_cell))*((-1.0 * reaction_r143) + ( 1.0 * reaction_r150) + ( 1.0 * reaction_r709) + (-1.0 * reaction_r710) + ( 1.0 * reaction_r711) + (-1.0 * reaction_r712) + ( 1.0 * reaction_r1014) + (-1.0 * reaction_r1066) + ( 1.0 * reaction_r1144) + (-1.0 * reaction_r1222) + ( 1.0 * reaction_r1300) + (-1.0 * reaction_r1378));
	
% Species:   id = DiL12UU, name = DiL12UU, affected by kineticLaw
	xdot(185) = (1/(compartment_cell))*((-1.0 * reaction_r144) + (-1.0 * reaction_r145) + (-1.0 * reaction_r713) + ( 1.0 * reaction_r714) + (-1.0 * reaction_r715) + ( 1.0 * reaction_r716) + (-1.0 * reaction_r717) + ( 1.0 * reaction_r718) + (-1.0 * reaction_r719) + ( 1.0 * reaction_r720) + ( 1.0 * reaction_r1015) + (-1.0 * reaction_r1067) + ( 1.0 * reaction_r1145) + (-1.0 * reaction_r1223) + ( 1.0 * reaction_r1301) + (-1.0 * reaction_r1379));
	
% Species:   id = DiL12CU, name = DiL12CU, affected by kineticLaw
	xdot(186) = (1/(compartment_cell))*((-1.0 * reaction_r146) + ( 1.0 * reaction_r713) + (-1.0 * reaction_r714) + (-1.0 * reaction_r721) + ( 1.0 * reaction_r722) + (-1.0 * reaction_r723) + ( 1.0 * reaction_r724) + ( 1.0 * reaction_r1016) + (-1.0 * reaction_r1068) + ( 1.0 * reaction_r1146) + (-1.0 * reaction_r1224) + ( 1.0 * reaction_r1302) + (-1.0 * reaction_r1380));
	
% Species:   id = DiL12LU, name = DiL12LU, affected by kineticLaw
	xdot(187) = (1/(compartment_cell))*((-1.0 * reaction_r147) + ( 1.0 * reaction_r715) + (-1.0 * reaction_r716) + ( 1.0 * reaction_r721) + (-1.0 * reaction_r722) + (-1.0 * reaction_r725) + ( 1.0 * reaction_r726) + (-1.0 * reaction_r727) + ( 1.0 * reaction_r728) + ( 1.0 * reaction_r1017) + (-1.0 * reaction_r1069) + ( 1.0 * reaction_r1147) + (-1.0 * reaction_r1225) + ( 1.0 * reaction_r1303) + (-1.0 * reaction_r1381));
	
% Species:   id = DiL12UG, name = DiL12UG, affected by kineticLaw
	xdot(188) = (1/(compartment_cell))*((-1.0 * reaction_r148) + (-1.0 * reaction_r149) + ( 1.0 * reaction_r717) + (-1.0 * reaction_r718) + (-1.0 * reaction_r729) + ( 1.0 * reaction_r730) + (-1.0 * reaction_r731) + ( 1.0 * reaction_r732) + (-1.0 * reaction_r733) + ( 1.0 * reaction_r734) + ( 1.0 * reaction_r1018) + (-1.0 * reaction_r1070) + ( 1.0 * reaction_r1148) + (-1.0 * reaction_r1226) + ( 1.0 * reaction_r1304) + (-1.0 * reaction_r1382));
	
% Species:   id = DiL12UL, name = DiL12UL, affected by kineticLaw
	xdot(189) = (1/(compartment_cell))*((-1.0 * reaction_r150) + (-1.0 * reaction_r151) + ( 1.0 * reaction_r719) + (-1.0 * reaction_r720) + ( 1.0 * reaction_r733) + (-1.0 * reaction_r734) + (-1.0 * reaction_r735) + ( 1.0 * reaction_r736) + ( 1.0 * reaction_r1019) + (-1.0 * reaction_r1071) + ( 1.0 * reaction_r1149) + (-1.0 * reaction_r1227) + ( 1.0 * reaction_r1305) + (-1.0 * reaction_r1383));
	
% Species:   id = DiL12CG, name = DiL12CG, affected by kineticLaw
	xdot(190) = (1/(compartment_cell))*((-1.0 * reaction_r152) + ( 1.0 * reaction_r723) + (-1.0 * reaction_r724) + ( 1.0 * reaction_r729) + (-1.0 * reaction_r730) + (-1.0 * reaction_r737) + ( 1.0 * reaction_r738) + (-1.0 * reaction_r739) + ( 1.0 * reaction_r740) + ( 1.0 * reaction_r1020) + (-1.0 * reaction_r1072) + ( 1.0 * reaction_r1150) + (-1.0 * reaction_r1228) + ( 1.0 * reaction_r1306) + (-1.0 * reaction_r1384));
	
% Species:   id = DiL12CC, name = DiL12CC, affected by kineticLaw
	xdot(191) = (1/(compartment_cell))*((-1.0 * reaction_r153) + ( 1.0 * reaction_r727) + (-1.0 * reaction_r728) + ( 1.0 * reaction_r735) + (-1.0 * reaction_r736) + ( 1.0 * reaction_r737) + (-1.0 * reaction_r738) + ( 1.0 * reaction_r1021) + (-1.0 * reaction_r1073) + ( 1.0 * reaction_r1151) + (-1.0 * reaction_r1229) + ( 1.0 * reaction_r1307) + (-1.0 * reaction_r1385));
	
% Species:   id = DiL12LG, name = DiL12LG, affected by kineticLaw
	xdot(192) = (1/(compartment_cell))*((-1.0 * reaction_r154) + ( 1.0 * reaction_r725) + (-1.0 * reaction_r726) + ( 1.0 * reaction_r731) + (-1.0 * reaction_r732) + ( 1.0 * reaction_r739) + (-1.0 * reaction_r740) + ( 1.0 * reaction_r1022) + (-1.0 * reaction_r1074) + ( 1.0 * reaction_r1152) + (-1.0 * reaction_r1230) + ( 1.0 * reaction_r1308) + (-1.0 * reaction_r1386));
	
% Species:   id = DaL00UU, name = DaL00UU, affected by kineticLaw
	xdot(193) = (1/(compartment_cell))*(( 1.0 * reaction_r155) + ( 1.0 * reaction_r156) + (-1.0 * reaction_r199) + (-1.0 * reaction_r200) + ( 1.0 * reaction_r1023) + ( 1.0 * reaction_r1049) + (-1.0 * reaction_r1127) + (-1.0 * reaction_r1153) + ( 1.0 * reaction_r1309) + (-1.0 * reaction_r1413) + ( 1.0 * reaction_r1465));
	
% Species:   id = DaL10UU, name = DaL10UU, affected by kineticLaw
	xdot(194) = (1/(compartment_cell))*((-1.0 * reaction_r155) + ( 1.0 * reaction_r158) + ( 1.0 * reaction_r199) + (-1.0 * reaction_r201) + (-1.0 * reaction_r741) + ( 1.0 * reaction_r742) + (-1.0 * reaction_r743) + ( 1.0 * reaction_r744) + ( 1.0 * reaction_r1024) + ( 1.0 * reaction_r1050) + (-1.0 * reaction_r1128) + (-1.0 * reaction_r1154) + ( 1.0 * reaction_r1310) + (-1.0 * reaction_r1414) + ( 1.0 * reaction_r1466));
	
% Species:   id = DaL10CU, name = DaL10CU, affected by kineticLaw
	xdot(195) = (1/(compartment_cell))*(( 1.0 * reaction_r159) + (-1.0 * reaction_r202) + ( 1.0 * reaction_r741) + (-1.0 * reaction_r742) + (-1.0 * reaction_r745) + ( 1.0 * reaction_r746) + ( 1.0 * reaction_r1025) + ( 1.0 * reaction_r1051) + (-1.0 * reaction_r1129) + (-1.0 * reaction_r1155) + ( 1.0 * reaction_r1311) + (-1.0 * reaction_r1415) + ( 1.0 * reaction_r1467));
	
% Species:   id = DaL10LU, name = DaL10LU, affected by kineticLaw
	xdot(196) = (1/(compartment_cell))*(( 1.0 * reaction_r160) + (-1.0 * reaction_r203) + ( 1.0 * reaction_r743) + (-1.0 * reaction_r744) + ( 1.0 * reaction_r745) + (-1.0 * reaction_r746) + ( 1.0 * reaction_r1026) + ( 1.0 * reaction_r1052) + (-1.0 * reaction_r1130) + (-1.0 * reaction_r1156) + ( 1.0 * reaction_r1312) + (-1.0 * reaction_r1416) + ( 1.0 * reaction_r1468));
	
% Species:   id = DaL01UU, name = DaL01UU, affected by kineticLaw
	xdot(197) = (1/(compartment_cell))*((-1.0 * reaction_r156) + ( 1.0 * reaction_r157) + ( 1.0 * reaction_r163) + ( 1.0 * reaction_r200) + (-1.0 * reaction_r204) + (-1.0 * reaction_r205) + (-1.0 * reaction_r747) + ( 1.0 * reaction_r748) + (-1.0 * reaction_r749) + ( 1.0 * reaction_r750) + ( 1.0 * reaction_r1027) + ( 1.0 * reaction_r1053) + (-1.0 * reaction_r1131) + (-1.0 * reaction_r1157) + ( 1.0 * reaction_r1313) + (-1.0 * reaction_r1417) + ( 1.0 * reaction_r1469));
	
% Species:   id = DaL01UG, name = DaL01UG, affected by kineticLaw
	xdot(198) = (1/(compartment_cell))*(( 1.0 * reaction_r161) + ( 1.0 * reaction_r164) + (-1.0 * reaction_r206) + (-1.0 * reaction_r207) + ( 1.0 * reaction_r747) + (-1.0 * reaction_r748) + (-1.0 * reaction_r751) + ( 1.0 * reaction_r752) + ( 1.0 * reaction_r1028) + ( 1.0 * reaction_r1054) + (-1.0 * reaction_r1132) + (-1.0 * reaction_r1158) + ( 1.0 * reaction_r1314) + (-1.0 * reaction_r1418) + ( 1.0 * reaction_r1470));
	
% Species:   id = DaL01UL, name = DaL01UL, affected by kineticLaw
	xdot(199) = (1/(compartment_cell))*(( 1.0 * reaction_r162) + ( 1.0 * reaction_r165) + (-1.0 * reaction_r208) + (-1.0 * reaction_r209) + ( 1.0 * reaction_r749) + (-1.0 * reaction_r750) + ( 1.0 * reaction_r751) + (-1.0 * reaction_r752) + ( 1.0 * reaction_r1029) + ( 1.0 * reaction_r1055) + (-1.0 * reaction_r1133) + (-1.0 * reaction_r1159) + ( 1.0 * reaction_r1315) + (-1.0 * reaction_r1419) + ( 1.0 * reaction_r1471));
	
% Species:   id = DaL11UU, name = DaL11UU, affected by kineticLaw
	xdot(200) = (1/(compartment_cell))*((-1.0 * reaction_r157) + (-1.0 * reaction_r158) + ( 1.0 * reaction_r167) + ( 1.0 * reaction_r201) + ( 1.0 * reaction_r204) + (-1.0 * reaction_r210) + (-1.0 * reaction_r753) + ( 1.0 * reaction_r754) + (-1.0 * reaction_r755) + ( 1.0 * reaction_r756) + (-1.0 * reaction_r757) + ( 1.0 * reaction_r758) + (-1.0 * reaction_r759) + ( 1.0 * reaction_r760) + ( 1.0 * reaction_r1030) + ( 1.0 * reaction_r1056) + (-1.0 * reaction_r1134) + (-1.0 * reaction_r1160) + ( 1.0 * reaction_r1316) + (-1.0 * reaction_r1420) + ( 1.0 * reaction_r1472));
	
% Species:   id = DaL11CU, name = DaL11CU, affected by kineticLaw
	xdot(201) = (1/(compartment_cell))*((-1.0 * reaction_r159) + ( 1.0 * reaction_r168) + ( 1.0 * reaction_r202) + (-1.0 * reaction_r211) + ( 1.0 * reaction_r753) + (-1.0 * reaction_r754) + (-1.0 * reaction_r761) + ( 1.0 * reaction_r762) + (-1.0 * reaction_r763) + ( 1.0 * reaction_r764) + ( 1.0 * reaction_r1031) + ( 1.0 * reaction_r1057) + (-1.0 * reaction_r1135) + (-1.0 * reaction_r1161) + ( 1.0 * reaction_r1317) + (-1.0 * reaction_r1421) + ( 1.0 * reaction_r1473));
	
% Species:   id = DaL11LU, name = DaL11LU, affected by kineticLaw
	xdot(202) = (1/(compartment_cell))*((-1.0 * reaction_r160) + ( 1.0 * reaction_r169) + ( 1.0 * reaction_r203) + (-1.0 * reaction_r212) + ( 1.0 * reaction_r755) + (-1.0 * reaction_r756) + ( 1.0 * reaction_r761) + (-1.0 * reaction_r762) + (-1.0 * reaction_r765) + ( 1.0 * reaction_r766) + (-1.0 * reaction_r767) + ( 1.0 * reaction_r768) + ( 1.0 * reaction_r1032) + ( 1.0 * reaction_r1058) + (-1.0 * reaction_r1136) + (-1.0 * reaction_r1162) + ( 1.0 * reaction_r1318) + (-1.0 * reaction_r1422) + ( 1.0 * reaction_r1474));
	
% Species:   id = DaL11UG, name = DaL11UG, affected by kineticLaw
	xdot(203) = (1/(compartment_cell))*((-1.0 * reaction_r161) + ( 1.0 * reaction_r171) + ( 1.0 * reaction_r206) + (-1.0 * reaction_r213) + ( 1.0 * reaction_r757) + (-1.0 * reaction_r758) + (-1.0 * reaction_r769) + ( 1.0 * reaction_r770) + (-1.0 * reaction_r771) + ( 1.0 * reaction_r772) + (-1.0 * reaction_r773) + ( 1.0 * reaction_r774) + ( 1.0 * reaction_r1033) + ( 1.0 * reaction_r1059) + (-1.0 * reaction_r1137) + (-1.0 * reaction_r1163) + ( 1.0 * reaction_r1319) + (-1.0 * reaction_r1423) + ( 1.0 * reaction_r1475));
	
% Species:   id = DaL11UL, name = DaL11UL, affected by kineticLaw
	xdot(204) = (1/(compartment_cell))*((-1.0 * reaction_r162) + ( 1.0 * reaction_r173) + ( 1.0 * reaction_r208) + (-1.0 * reaction_r214) + ( 1.0 * reaction_r759) + (-1.0 * reaction_r760) + ( 1.0 * reaction_r773) + (-1.0 * reaction_r774) + (-1.0 * reaction_r775) + ( 1.0 * reaction_r776) + ( 1.0 * reaction_r1034) + ( 1.0 * reaction_r1060) + (-1.0 * reaction_r1138) + (-1.0 * reaction_r1164) + ( 1.0 * reaction_r1320) + (-1.0 * reaction_r1424) + ( 1.0 * reaction_r1476));
	
% Species:   id = DaL11CG, name = DaL11CG, affected by kineticLaw
	xdot(205) = (1/(compartment_cell))*(( 1.0 * reaction_r174) + (-1.0 * reaction_r215) + ( 1.0 * reaction_r763) + (-1.0 * reaction_r764) + ( 1.0 * reaction_r769) + (-1.0 * reaction_r770) + (-1.0 * reaction_r777) + ( 1.0 * reaction_r778) + (-1.0 * reaction_r779) + ( 1.0 * reaction_r780) + ( 1.0 * reaction_r1035) + ( 1.0 * reaction_r1061) + (-1.0 * reaction_r1139) + (-1.0 * reaction_r1165) + ( 1.0 * reaction_r1321) + (-1.0 * reaction_r1425) + ( 1.0 * reaction_r1477));
	
% Species:   id = DaL11CC, name = DaL11CC, affected by kineticLaw
	xdot(206) = (1/(compartment_cell))*(( 1.0 * reaction_r175) + (-1.0 * reaction_r216) + ( 1.0 * reaction_r767) + (-1.0 * reaction_r768) + ( 1.0 * reaction_r775) + (-1.0 * reaction_r776) + ( 1.0 * reaction_r777) + (-1.0 * reaction_r778) + ( 1.0 * reaction_r1036) + ( 1.0 * reaction_r1062) + (-1.0 * reaction_r1140) + (-1.0 * reaction_r1166) + ( 1.0 * reaction_r1322) + (-1.0 * reaction_r1426) + ( 1.0 * reaction_r1478));
	
% Species:   id = DaL11LG, name = DaL11LG, affected by kineticLaw
	xdot(207) = (1/(compartment_cell))*(( 1.0 * reaction_r176) + (-1.0 * reaction_r217) + ( 1.0 * reaction_r765) + (-1.0 * reaction_r766) + ( 1.0 * reaction_r771) + (-1.0 * reaction_r772) + ( 1.0 * reaction_r779) + (-1.0 * reaction_r780) + ( 1.0 * reaction_r1037) + ( 1.0 * reaction_r1063) + (-1.0 * reaction_r1141) + (-1.0 * reaction_r1167) + ( 1.0 * reaction_r1323) + (-1.0 * reaction_r1427) + ( 1.0 * reaction_r1479));
	
% Species:   id = DaL02UU, name = DaL02UU, affected by kineticLaw
	xdot(208) = (1/(compartment_cell))*((-1.0 * reaction_r163) + ( 1.0 * reaction_r166) + ( 1.0 * reaction_r205) + (-1.0 * reaction_r218) + (-1.0 * reaction_r781) + ( 1.0 * reaction_r782) + (-1.0 * reaction_r783) + ( 1.0 * reaction_r784) + ( 1.0 * reaction_r1038) + ( 1.0 * reaction_r1064) + (-1.0 * reaction_r1142) + (-1.0 * reaction_r1168) + ( 1.0 * reaction_r1324) + (-1.0 * reaction_r1428) + ( 1.0 * reaction_r1480));
	
% Species:   id = DaL02UG, name = DaL02UG, affected by kineticLaw
	xdot(209) = (1/(compartment_cell))*((-1.0 * reaction_r164) + ( 1.0 * reaction_r170) + ( 1.0 * reaction_r207) + (-1.0 * reaction_r219) + ( 1.0 * reaction_r781) + (-1.0 * reaction_r782) + (-1.0 * reaction_r785) + ( 1.0 * reaction_r786) + ( 1.0 * reaction_r1039) + ( 1.0 * reaction_r1065) + (-1.0 * reaction_r1143) + (-1.0 * reaction_r1169) + ( 1.0 * reaction_r1325) + (-1.0 * reaction_r1429) + ( 1.0 * reaction_r1481));
	
% Species:   id = DaL02UL, name = DaL02UL, affected by kineticLaw
	xdot(210) = (1/(compartment_cell))*((-1.0 * reaction_r165) + ( 1.0 * reaction_r172) + ( 1.0 * reaction_r209) + (-1.0 * reaction_r220) + ( 1.0 * reaction_r783) + (-1.0 * reaction_r784) + ( 1.0 * reaction_r785) + (-1.0 * reaction_r786) + ( 1.0 * reaction_r1040) + ( 1.0 * reaction_r1066) + (-1.0 * reaction_r1144) + (-1.0 * reaction_r1170) + ( 1.0 * reaction_r1326) + (-1.0 * reaction_r1430) + ( 1.0 * reaction_r1482));
	
% Species:   id = DaL12UU, name = DaL12UU, affected by kineticLaw
	xdot(211) = (1/(compartment_cell))*((-1.0 * reaction_r166) + (-1.0 * reaction_r167) + ( 1.0 * reaction_r210) + ( 1.0 * reaction_r218) + (-1.0 * reaction_r787) + ( 1.0 * reaction_r788) + (-1.0 * reaction_r789) + ( 1.0 * reaction_r790) + (-1.0 * reaction_r791) + ( 1.0 * reaction_r792) + (-1.0 * reaction_r793) + ( 1.0 * reaction_r794) + ( 1.0 * reaction_r1041) + ( 1.0 * reaction_r1067) + (-1.0 * reaction_r1145) + (-1.0 * reaction_r1171) + ( 1.0 * reaction_r1327) + (-1.0 * reaction_r1431) + ( 1.0 * reaction_r1483));
	
% Species:   id = DaL12CU, name = DaL12CU, affected by kineticLaw
	xdot(212) = (1/(compartment_cell))*((-1.0 * reaction_r168) + ( 1.0 * reaction_r211) + ( 1.0 * reaction_r787) + (-1.0 * reaction_r788) + (-1.0 * reaction_r795) + ( 1.0 * reaction_r796) + (-1.0 * reaction_r797) + ( 1.0 * reaction_r798) + ( 1.0 * reaction_r1042) + ( 1.0 * reaction_r1068) + (-1.0 * reaction_r1146) + (-1.0 * reaction_r1172) + ( 1.0 * reaction_r1328) + (-1.0 * reaction_r1432) + ( 1.0 * reaction_r1484));
	
% Species:   id = DaL12LU, name = DaL12LU, affected by kineticLaw
	xdot(213) = (1/(compartment_cell))*((-1.0 * reaction_r169) + ( 1.0 * reaction_r212) + ( 1.0 * reaction_r789) + (-1.0 * reaction_r790) + ( 1.0 * reaction_r795) + (-1.0 * reaction_r796) + (-1.0 * reaction_r799) + ( 1.0 * reaction_r800) + (-1.0 * reaction_r801) + ( 1.0 * reaction_r802) + ( 1.0 * reaction_r1043) + ( 1.0 * reaction_r1069) + (-1.0 * reaction_r1147) + (-1.0 * reaction_r1173) + ( 1.0 * reaction_r1329) + (-1.0 * reaction_r1433) + ( 1.0 * reaction_r1485));
	
% Species:   id = DaL12UG, name = DaL12UG, affected by kineticLaw
	xdot(214) = (1/(compartment_cell))*((-1.0 * reaction_r170) + (-1.0 * reaction_r171) + ( 1.0 * reaction_r213) + ( 1.0 * reaction_r219) + ( 1.0 * reaction_r791) + (-1.0 * reaction_r792) + (-1.0 * reaction_r803) + ( 1.0 * reaction_r804) + (-1.0 * reaction_r805) + ( 1.0 * reaction_r806) + (-1.0 * reaction_r807) + ( 1.0 * reaction_r808) + ( 1.0 * reaction_r1044) + ( 1.0 * reaction_r1070) + (-1.0 * reaction_r1148) + (-1.0 * reaction_r1174) + ( 1.0 * reaction_r1330) + (-1.0 * reaction_r1434) + ( 1.0 * reaction_r1486));
	
% Species:   id = DaL12UL, name = DaL12UL, affected by kineticLaw
	xdot(215) = (1/(compartment_cell))*((-1.0 * reaction_r172) + (-1.0 * reaction_r173) + ( 1.0 * reaction_r214) + ( 1.0 * reaction_r220) + ( 1.0 * reaction_r793) + (-1.0 * reaction_r794) + ( 1.0 * reaction_r807) + (-1.0 * reaction_r808) + (-1.0 * reaction_r809) + ( 1.0 * reaction_r810) + ( 1.0 * reaction_r1045) + ( 1.0 * reaction_r1071) + (-1.0 * reaction_r1149) + (-1.0 * reaction_r1175) + ( 1.0 * reaction_r1331) + (-1.0 * reaction_r1435) + ( 1.0 * reaction_r1487));
	
% Species:   id = DaL12CG, name = DaL12CG, affected by kineticLaw
	xdot(216) = (1/(compartment_cell))*((-1.0 * reaction_r174) + ( 1.0 * reaction_r215) + ( 1.0 * reaction_r797) + (-1.0 * reaction_r798) + ( 1.0 * reaction_r803) + (-1.0 * reaction_r804) + (-1.0 * reaction_r811) + ( 1.0 * reaction_r812) + (-1.0 * reaction_r813) + ( 1.0 * reaction_r814) + ( 1.0 * reaction_r1046) + ( 1.0 * reaction_r1072) + (-1.0 * reaction_r1150) + (-1.0 * reaction_r1176) + ( 1.0 * reaction_r1332) + (-1.0 * reaction_r1436) + ( 1.0 * reaction_r1488));
	
% Species:   id = DaL12CC, name = DaL12CC, affected by kineticLaw
	xdot(217) = (1/(compartment_cell))*((-1.0 * reaction_r175) + ( 1.0 * reaction_r216) + ( 1.0 * reaction_r801) + (-1.0 * reaction_r802) + ( 1.0 * reaction_r809) + (-1.0 * reaction_r810) + ( 1.0 * reaction_r811) + (-1.0 * reaction_r812) + ( 1.0 * reaction_r1047) + ( 1.0 * reaction_r1073) + (-1.0 * reaction_r1151) + (-1.0 * reaction_r1177) + ( 1.0 * reaction_r1333) + (-1.0 * reaction_r1437) + ( 1.0 * reaction_r1489));
	
% Species:   id = DaL12LG, name = DaL12LG, affected by kineticLaw
	xdot(218) = (1/(compartment_cell))*((-1.0 * reaction_r176) + ( 1.0 * reaction_r217) + ( 1.0 * reaction_r799) + (-1.0 * reaction_r800) + ( 1.0 * reaction_r805) + (-1.0 * reaction_r806) + ( 1.0 * reaction_r813) + (-1.0 * reaction_r814) + ( 1.0 * reaction_r1048) + ( 1.0 * reaction_r1074) + (-1.0 * reaction_r1152) + (-1.0 * reaction_r1178) + ( 1.0 * reaction_r1334) + (-1.0 * reaction_r1438) + ( 1.0 * reaction_r1490));
end

% adding few functions representing operators used in SBML but not present directly 
% in either matlab or octave. 
function z=pow(x,y),z=x^y;end
function z=root(x,y),z=y^(1/x);end


