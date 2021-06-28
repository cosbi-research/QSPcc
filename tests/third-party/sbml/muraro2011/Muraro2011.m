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
% Model name = Muraro2011_Cytokinin-Auxin_CrossRegulation
%
% is http://identifiers.org/biomodels.db/MODEL1203080000
% is http://identifiers.org/biomodels.db/BIOMD0000000416
% isDescribedBy http://identifiers.org/pubmed/21640126
% isDerivedFrom http://identifiers.org/pubmed/20135237
%


function Muraro2011()
tic;
%Initial conditions vector
	x0=zeros(32,1);
	x0(1) = 0.0;
	x0(2) = 0.0;
	x0(3) = 0.0;
	x0(4) = 0.0;
	x0(5) = 0.0;
	x0(6) = 0.0;
	x0(7) = 0.0;
	x0(8) = 1.0;
	x0(9) = 0.0;
	x0(10) = 0.0;
	x0(11) = 0.0;
	x0(12) = 0.0;
	x0(13) = 0.0;
	x0(14) = 0.0;
	x0(15) = 0.0;
	x0(16) = 0.0;
	x0(17) = 1.0;
	x0(18) = 1.0;
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


% Depending on whether you are using Octave or Matlab,
% you should comment / uncomment one of the following blocks.
% This should also be done for the definition of the function f below.
% Start Matlab code
	tspan=[0:0.01:1000];
	opts = odeset('AbsTol',1e-6,'RelTol',1e-3);
	[t,x]=ode23(@f,tspan,x0,opts);
disp(toc);
	% End Matlab code

% Start Octave code
	%t=linspace(0,100,100);
	%x=lsode('f',x0,t);
% End Octave code


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
% Parameter:   id =  eps, name = eps
	global_par_eps=0.01;
% Parameter:   id =  lambda1, name = lambda1
	global_par_lambda1=0.1;
% Parameter:   id =  lambda3, name = lambda3
	global_par_lambda3=0.02;
% Parameter:   id =  alphaAux, name = alphaAux
	global_par_alphaAux=1.0;
% Parameter:   id =  alphaTIR1, name = alphaTIR1
	global_par_alphaTIR1=1.0;
% Parameter:   id =  alphaARF, name = alphaARF
	global_par_alphaARF=1.0;
% Parameter:   id =  phiIAAp, name = phiIAAp
	global_par_phiIAAp=100.0;
% Parameter:   id =  phiARp, name = phiARp
	global_par_phiARp=2.0;
% Parameter:   id =  phiPINp, name = phiPINp
	global_par_phiPINp=100.0;
% Parameter:   id =  deltaIAAp, name = deltaIAAp
	global_par_deltaIAAp=1.0;
% Parameter:   id =  deltaARp, name = deltaARp
	global_par_deltaARp=1.0;
% Parameter:   id =  deltaPINp, name = deltaPINp
	global_par_deltaPINp=1.0;
% Parameter:   id =  muAux, name = muAux
	global_par_muAux=0.1;
% Parameter:   id =  muIAAs, name = muIAAs
	global_par_muIAAs=1.0;
% Parameter:   id =  etaAuxTIR1, name = etaAuxTIR1
	global_par_etaAuxTIR1=10.0;
% Parameter:   id =  etaARFIAA, name = etaARFIAA
	global_par_etaARFIAA=1.0;
% Parameter:   id =  la, name = la
	global_par_la=0.5;
% Parameter:   id =  ld, name = ld
	global_par_ld=0.1;
% Parameter:   id =  pa, name = pa
	global_par_pa=10.0;
% Parameter:   id =  pd, name = pd
	global_par_pd=10.0;
% Parameter:   id =  ka, name = ka
	global_par_ka=100.0;
% Parameter:   id =  kd, name = kd
	global_par_kd=1.0;
% Parameter:   id =  qa, name = qa
	global_par_qa=1.0;
% Parameter:   id =  qd, name = qd
	global_par_qd=1.0;
% Parameter:   id =  thetaARF, name = thetaARF
	global_par_thetaARF=0.1;
% Parameter:   id =  thetaARF2, name = thetaARF2
	global_par_thetaARF2=0.01;
% Parameter:   id =  thARFIAA, name = thARFIAA
	global_par_thARFIAA=0.1;
% Parameter:   id =  thetaIAAp, name = thetaIAAp
	global_par_thetaIAAp=0.1;
% Parameter:   id =  thetaARp, name = thetaARp
	global_par_thetaARp=0.1;
% Parameter:   id =  psiARFIAA, name = psiARFIAA
	global_par_psiARFIAA=0.1;
% Parameter:   id =  psiARF, name = psiARF
	global_par_psiARF=0.1;
% Parameter:   id =  alphaCk, name = alphaCk
	global_par_alphaCk=1.0;
% Parameter:   id =  alphaARRB, name = alphaARRB
	global_par_alphaARRB=2.0;
% Parameter:   id =  alphaAHK, name = alphaAHK
	global_par_alphaAHK=1.0;
% Parameter:   id =  alphaPH, name = alphaPH
	global_par_alphaPH=1.0;
% Parameter:   id =  phiCRp, name = phiCRp
	global_par_phiCRp=2.0;
% Parameter:   id =  phiARRAp, name = phiARRAp
	global_par_phiARRAp=100.0;
% Parameter:   id =  deltaCRp, name = deltaCRp
	global_par_deltaCRp=1.0;
% Parameter:   id =  deltaARRAp, name = deltaARRAp
	global_par_deltaARRAp=1.0;
% Parameter:   id =  muCk, name = muCk
	global_par_muCk=0.1;
% Parameter:   id =  etaAHKph, name = etaAHKph
	global_par_etaAHKph=1.0;
% Parameter:   id =  etaCkPh, name = etaCkPh
	global_par_etaCkPh=1.0;
% Parameter:   id =  ra, name = ra
	global_par_ra=1.0;
% Parameter:   id =  rd, name = rd
	global_par_rd=1.0;
% Parameter:   id =  ua, name = ua
	global_par_ua=1.0;
% Parameter:   id =  ud, name = ud
	global_par_ud=1.0;
% Parameter:   id =  sa, name = sa
	global_par_sa=1.0;
% Parameter:   id =  sd, name = sd
	global_par_sd=1.0;
% Parameter:   id =  thARRAph, name = thARRAph
	global_par_thARRAph=0.1;
% Parameter:   id =  thARRBph, name = thARRBph
	global_par_thARRBph=0.1;
% assignmentRule: variable = TIR1
	x(13)=global_par_alphaTIR1-x(3)-x(4);
% assignmentRule: variable = ARF
	x(14)=global_par_alphaARF-2*x(7)-x(6);
% assignmentRule: variable = ARRBp
	x(23)=global_par_alphaARRB-global_par_etaAHKph*x(19);
% assignmentRule: variable = CkAHKph
	x(24)=global_par_alphaPH-x(17)-x(20)-x(19);
% assignmentRule: variable = CkAHK
	x(25)=global_par_alphaAHK-global_par_etaAHKph*(x(17)+x(24));
% assignmentRule: variable = F1
	x(26)=x(14)/global_par_thetaARF/(1+x(14)/global_par_thetaARF+x(7)/global_par_thetaARF2+x(6)/global_par_thARFIAA+x(14)*x(2)/global_par_psiARFIAA+x(14)^2/global_par_psiARF+x(19)/global_par_thARRBph);
% assignmentRule: variable = F2
	x(27)=(x(7)/global_par_thetaARF2+x(14)^2/global_par_psiARF)/(1+x(14)/global_par_thetaARF+x(7)/global_par_thetaARF2+x(6)/global_par_thARFIAA+x(14)*x(2)/global_par_psiARFIAA+x(14)^2/global_par_psiARF+x(19)/global_par_thARRBph);
% assignmentRule: variable = F3
	x(28)=x(19)/global_par_thARRBph/(1+x(14)/global_par_thetaARF+x(7)/global_par_thetaARF2+x(6)/global_par_thARFIAA+x(14)*x(2)/global_par_psiARFIAA+x(14)^2/global_par_psiARF+x(19)/global_par_thARRBph);
% assignmentRule: variable = F4
	x(29)=x(19)/global_par_thARRBph/(1+x(20)/global_par_thARRAph+x(19)/global_par_thARRBph);
% assignmentRule: variable = F5a
	x(30)=x(14)/global_par_thetaARF/(1+x(14)/global_par_thetaARF+x(7)/global_par_thetaARF2+x(6)/global_par_thARFIAA+x(14)*x(2)/global_par_psiARFIAA+x(14)^2/global_par_psiARF);
% assignmentRule: variable = F5b
	x(31)=(x(7)/global_par_thetaARF2+x(14)^2/global_par_psiARF)/(1+x(14)/global_par_thetaARF+x(7)/global_par_thetaARF2+x(6)/global_par_thARFIAA+x(14)*x(2)/global_par_psiARFIAA+x(14)^2/global_par_psiARF);
% assignmentRule: variable = F6
	x(32)=x(12)/global_par_thetaARp/(1+x(12)/global_par_thetaARp);

% Reaction: id = r1
	reaction_r1=global_par_phiIAAp*(global_par_lambda1*x(26)+x(27)+global_par_lambda3*x(28))-x(1);

% Reaction: id = r2
	reaction_r2=1/global_par_eps*(global_par_deltaIAAp*x(1)-global_par_la*x(2)*x(3)+global_par_ld*x(4))+global_par_etaARFIAA*(global_par_pd*x(6)-global_par_pa*x(2)*x(14));

% Reaction: id = r3
	reaction_r3=1/global_par_eps*(global_par_ka*x(8)*x(13)-global_par_kd*x(3)+(global_par_ld+1)*x(4)-global_par_la*x(3)*x(2));

% Reaction: id = r4
	reaction_r4=1/global_par_eps*(global_par_la*x(2)*x(3)-(global_par_ld+1)*x(4));

% Reaction: id = r5
	reaction_r5=1/global_par_eps*(x(4)-global_par_muIAAs*x(5));

% Reaction: id = r6
	reaction_r6=global_par_pa*x(14)*x(2)-global_par_pd*x(6);

% Reaction: id = r7
	reaction_r7=global_par_qa*x(14)^2-global_par_qd*x(7);

% Reaction: id = r8
	reaction_r8=global_par_muAux*(global_par_alphaAux-x(8))-1/global_par_eps*global_par_etaAuxTIR1*(global_par_ka*x(8)*x(13)-global_par_kd*x(3));

% Reaction: id = r9
	reaction_r9=global_par_phiPINp*(global_par_lambda1*x(30)+x(31))-x(9);

% Reaction: id = r10
	reaction_r10=1/global_par_eps*(global_par_deltaPINp*x(9)-x(10));

% Reaction: id = r11
	reaction_r11=global_par_phiARp*(global_par_lambda1*x(30)+x(31))-x(11);

% Reaction: id = r12
	reaction_r12=1/global_par_eps*(global_par_deltaARp*x(11)-x(12));

% Reaction: id = r13
	reaction_r13=global_par_phiCRp*x(29)-x(15);

% Reaction: id = r14
	reaction_r14=1/global_par_eps*(global_par_deltaCRp*x(15)-x(16));

% Reaction: id = r15
	reaction_r15=1/global_par_eps*(global_par_rd*x(24)-global_par_ra*x(17)*x(18));

% Reaction: id = r16
	reaction_r16=global_par_muCk*(global_par_alphaCk-x(18))-global_par_etaCkPh/global_par_eps*(global_par_ra*x(17)*x(18)-global_par_rd*x(24));

% Reaction: id = r17
	reaction_r17=1/global_par_eps*(global_par_ua*x(24)*x(23)-global_par_ud*x(25)*x(19));

% Reaction: id = r18
	reaction_r18=1/global_par_eps*(global_par_sa*x(24)*x(22)-global_par_sd*x(25)*x(20));

% Reaction: id = r19
	reaction_r19=global_par_phiARRAp*x(32)-x(21);

% Reaction: id = r20
	reaction_r20=1/global_par_eps*(global_par_deltaARRAp*x(21)-x(22)+global_par_etaAHKph*(global_par_sd*x(25)*x(20)-global_par_sa*x(24)*x(22)));

	xdot=zeros(32,1);
	
% Species:   id = IAAm, name = IAAm, affected by kineticLaw
	xdot(1) = (1/(compartment_cell))*(( 1.0 * reaction_r1));
	
% Species:   id = IAAp, name = IAAp, affected by kineticLaw
	xdot(2) = (1/(compartment_cell))*(( 1.0 * reaction_r2));
	
% Species:   id = AuxTIR1, name = AuxTIR1, affected by kineticLaw
	xdot(3) = (1/(compartment_cell))*(( 1.0 * reaction_r3));
	
% Species:   id = AuxTIAA, name = AuxTIAA, affected by kineticLaw
	xdot(4) = (1/(compartment_cell))*(( 1.0 * reaction_r4));
	
% Species:   id = IAAs, name = IAAs, affected by kineticLaw
	xdot(5) = (1/(compartment_cell))*(( 1.0 * reaction_r5));
	
% Species:   id = ARFIAA, name = ARFIAA, affected by kineticLaw
	xdot(6) = (1/(compartment_cell))*(( 1.0 * reaction_r6));
	
% Species:   id = ARF2, name = ARF2, affected by kineticLaw
	xdot(7) = (1/(compartment_cell))*(( 1.0 * reaction_r7));
	
% Species:   id = Aux, name = Aux, affected by kineticLaw
	xdot(8) = (1/(compartment_cell))*(( 1.0 * reaction_r8));
	
% Species:   id = PINm, name = PINm, affected by kineticLaw
	xdot(9) = (1/(compartment_cell))*(( 1.0 * reaction_r9));
	
% Species:   id = PINp, name = PINp, affected by kineticLaw
	xdot(10) = (1/(compartment_cell))*(( 1.0 * reaction_r10));
	
% Species:   id = ARm, name = ARm, affected by kineticLaw
	xdot(11) = (1/(compartment_cell))*(( 1.0 * reaction_r11));
	
% Species:   id = ARp, name = ARp, affected by kineticLaw
	xdot(12) = (1/(compartment_cell))*(( 1.0 * reaction_r12));
	
% Species:   id = TIR1, name = TIR1, defined in a rule 	xdot(13) = x(13);
	
% Species:   id = ARF, name = ARF, defined in a rule 	xdot(14) = x(14);
	
% Species:   id = CRm, name = CRm, affected by kineticLaw
	xdot(15) = (1/(compartment_cell))*(( 1.0 * reaction_r13));
	
% Species:   id = CRp, name = CRp, affected by kineticLaw
	xdot(16) = (1/(compartment_cell))*(( 1.0 * reaction_r14));
	
% Species:   id = AHKph, name = AHKph, affected by kineticLaw
	xdot(17) = (1/(compartment_cell))*(( 1.0 * reaction_r15));
	
% Species:   id = Ck, name = Ck, affected by kineticLaw
	xdot(18) = (1/(compartment_cell))*(( 1.0 * reaction_r16));
	
% Species:   id = ARRBph, name = ARRBph, affected by kineticLaw
	xdot(19) = (1/(compartment_cell))*(( 1.0 * reaction_r17));
	
% Species:   id = ARRAph, name = ARRAph, affected by kineticLaw
	xdot(20) = (1/(compartment_cell))*(( 1.0 * reaction_r18));
	
% Species:   id = ARRAm, name = ARRAm, affected by kineticLaw
	xdot(21) = (1/(compartment_cell))*(( 1.0 * reaction_r19));
	
% Species:   id = ARRAp, name = ARRAp, affected by kineticLaw
	xdot(22) = (1/(compartment_cell))*(( 1.0 * reaction_r20));
	
% Species:   id = ARRBp, name = ARRBp, defined in a rule 	xdot(23) = x(23);
	
% Species:   id = CkAHKph, name = CkAHKph, defined in a rule 	xdot(24) = x(24);
	
% Species:   id = CkAHK, name = CkAHK, defined in a rule 	xdot(25) = x(25);
	
% Species:   id = F1, name = F1, defined in a rule 	xdot(26) = x(26);
	
% Species:   id = F2, name = F2, defined in a rule 	xdot(27) = x(27);
	
% Species:   id = F3, name = F3, defined in a rule 	xdot(28) = x(28);
	
% Species:   id = F4, name = F4, defined in a rule 	xdot(29) = x(29);
	
% Species:   id = F5a, name = F5a, defined in a rule 	xdot(30) = x(30);
	
% Species:   id = F5b, name = F5b, defined in a rule 	xdot(31) = x(31);
	
% Species:   id = F6, name = F6, defined in a rule 	xdot(32) = x(32);
end

% adding few functions representing operators used in SBML but not present directly 
% in either matlab or octave. 
function z=pow(x,y),z=x^y;end
function z=root(x,y),z=y^(1/x);end


