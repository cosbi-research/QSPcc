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
% Model name = Winter2017 - Brain Energy Metabolism with PPP
%
% is http://identifiers.org/biomodels.db/MODEL1603240000
% is http://identifiers.org/biomodels.db/BIOMD0000000627
% isDescribedBy http://identifiers.org/doi/10.1177/0271678X17693024
% isDerivedFrom http://identifiers.org/biomodels.db/MODEL1411210000
% isDerivedFrom http://identifiers.org/biomodels.db/BIOMD0000000554
% isDerivedFrom http://identifiers.org/doi/10.1007/978-1-4613-1161-4
%


function Winter2017()
%Initial conditions vector
	tic;
	x0=zeros(61,1);
	x0(61) = 0.0237;
	x0(1) = 0.040323291746644;
	x0(2) = 0.0121467082533562;
	x0(3) = 0.0253903826849856;
	x0(4) = 0.00188912996259375;
	x0(5) = 2.62913971209081E-4;
	x0(6) = 0.513125204430911;
	x0(7) = 0.0506867341754652;
	x0(8) = 1.01756735100076;
	x0(9) = 0.0207718119329183;
	x0(10) = 4.84856903277021E-4;
	x0(11) = 0.00736369051750214;
	x0(12) = 0.00137130155845014;
	x0(13) = 0.05894677979576;
	x0(14) = 0.631465311475557;
	x0(15) = 0.0134379352275963;
	x0(16) = 0.648285185366582;
	x0(17) = 6.98905574867159;
	x0(18) = 1.35000000000001;
	x0(19) = 0.0502819665719672;
	x0(20) = 0.00270068242727579;
	x0(21) = 1.60171481463342;
	x0(22) = 0.0916363094824979;
	x0(23) = 1.35054620762897E-6;
	x0(24) = 0.00129746785673949;
	x0(25) = 3.03470299804797E-4;
	x0(26) = 0.00930686571830458;
	x0(27) = 1.21527766162824E-5;
	x0(28) = 0.519857938505835;
	x0(29) = 0.00293045545188872;
	x0(30) = 0.13105170046902;
	x0(31) = 9.96586007623714E-10;
	x0(32) = 0.28506553827656;
	x0(33) = 0.453875749582273;
	x0(34) = 0.0170326696107673;
	x0(35) = 0.00698016362763041;
	x0(36) = 5.51039449892962E-4;
	x0(37) = 0.014484722086168;
	x0(38) = 2.31197219260613E-4;
	x0(39) = 0.0391626309395164;
	x0(40) = 0.341202758724066;
	x0(41) = 0.0114703177351059;
	x0(42) = 0.0943080249454476;
	x0(43) = 4.00737645868716;
	x0(44) = 0.0;
	x0(45) = 0.111239857216292;
	x0(46) = 0.0296343932014343;
	x0(47) = 1.15569197505455;
	x0(48) = 0.040515277913832;
	x0(49) = 7.49440003798258E-7;
	x0(50) = 4.50905835212361E-4;
	x0(51) = 1.68586812670336E-4;
	x0(52) = 0.00517018155675064;
	x0(53) = 6.5024908937442E-6;
	x0(54) = 0.0691726529321511;
	x0(55) = 0.00142484578792443;
	x0(56) = 6.89248119909569E-10;
	x0(57) = 0.0728065001051474;
	x0(58) = 0.228060016230605;
	x0(59) = 0.269553776630414;
	x0(60) = 0.0;


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


	%plot(t,x);
	disp(toc);
    csvwrite('results.csv',[t,x]);
end



% Depending on whether you are using Octave or Matlab,
% you should comment / uncomment one of the following blocks.
% This should also be done for the definition of the function f below.
% Start Matlab code
function xdot=f(time,x)
% End Matlab code

% Start Octave code
%function xdot=f(x,t)
% End Octave code
const_species_Na__extracellular_space=30.0;

const_species_O2_artery=0.0458700000000001;

const_species_CO2_artery=0.00660000000000004;

const_species_GLC_artery=0.0264000000000002;

const_species_LAC_artery=0.0017215;
compartment_venous_balloon = x(61);

% Compartment: id = compartment_1, name = capillary, constant
	compartment_compartment_1=0.0055;
% Compartment: id = compartment_2, name = neurons, constant
	compartment_compartment_2=0.45;
% Compartment: id = compartment_3, name = astrocytes, constant
	compartment_compartment_3=0.25;
% Compartment: id = compartment_4, name = extracellular_space, constant
	compartment_compartment_4=0.2;
% Compartment: id = venous_balloon, name = venous balloon, defined by a Rule
% Compartment: id = artery, name = artery, constant
	compartment_artery=0.0055;
% Parameter:   id =  parameter_1, name = K_m_GLC
	global_par_parameter_1=0.105;
% Parameter:   id =  parameter_2, name = K_m_G6P
	global_par_parameter_2=0.5;
% Parameter:   id =  parameter_3, name = K_m_F6P_PGI
	global_par_parameter_3=0.06;
% Parameter:   id =  parameter_4, name = K_m_F6P_PFK
	global_par_parameter_4=0.18;
% Parameter:   id =  parameter_5, name = K_I_ATP
	global_par_parameter_5=1.0;
% Parameter:   id =  parameter_6, name = nH
	global_par_parameter_6=4.0;
% Parameter:   id =  parameter_7, name = NADH_total_neurons
	global_par_parameter_7=0.22;
% Parameter:   id =  parameter_8, name = NADH_total_astrocytes
	global_par_parameter_8=0.22;
% Parameter:   id =  parameter_9, name = NAD_neurons
% Parameter:   id =  parameter_10, name = NAD_astrocytes
% Parameter:   id =  parameter_11, name = ANP
	global_par_parameter_11=2.379;
% Parameter:   id =  parameter_12, name = q_AK
	global_par_parameter_12=0.92;
% Parameter:   id =  parameter_14, name = K_m_ATP
	global_par_parameter_14=0.01532;
% Parameter:   id =  parameter_16, name = K_m_ADP
	global_par_parameter_16=0.00107;
% Parameter:   id =  parameter_17, name = K_m_O2
	global_par_parameter_17=0.0029658;
% Parameter:   id =  parameter_18, name = K_m_PYR
	global_par_parameter_18=0.0632;
% Parameter:   id =  parameter_19, name = PCr_total
	global_par_parameter_19=5.0;
% Parameter:   id =  parameter_20, name = Cr_neurons
% Parameter:   id =  parameter_21, name = Cr_astrocytes
% Parameter:   id =  parameter_25, name = Vmax_ATPase_neurons
	global_par_parameter_25=0.04889;
% Parameter:   id =  parameter_26, name = Vmax_ATPase_astrocytes
	global_par_parameter_26=0.035657;
% Parameter:   id =  parameter_43, name = Vmax_ec_LAC (wrt extracellular space)
	global_par_parameter_43=0.0325;
% Parameter:   id =  F_0, name = F_0
	global_par_F_0=0.012;
% Parameter:   id =  delta_F, name = delta_F
	global_par_delta_F=0.42;
% Parameter:   id =  t_0, name = t_0
	global_par_t_0=200.0;
% Parameter:   id =  t_end, name = t_end
	global_par_t_end=40.0;
% Parameter:   id =  t_1, name = t_1
	global_par_t_1=2.0;
% Parameter:   id =  F_in, name = F_in
% Parameter:   id =  F_out, name = F_out
% Parameter:   id =  tau_v, name = tau_v
	global_par_tau_v=35.0;
% Parameter:   id =  Hb_OP, name = Hb.OP
	global_par_Hb_OP=8.6;
% Parameter:   id =  Sm_g, name = Sm_g
	global_par_Sm_g=10500.0;
% Parameter:   id =  Sm_n, name = Sm_n
	global_par_Sm_n=40500.0;
% Parameter:   id =  k_pump, name = k_pump
	global_par_k_pump=3.17E-7;
% Parameter:   id =  K_m_Na_pump, name = K_m,Na-pump
	global_par_K_m_Na_pump=0.4243;
% Parameter:   id =  K_m_G6P_GLYS, name = K_m_G6P_GLYS
	global_par_K_m_G6P_GLYS=0.5;
% Parameter:   id =  delta_GLY, name = delta_GLY
	global_par_delta_GLY=62.0;
% Parameter:   id =  K_m_GLY, name = K_m_GLY
	global_par_K_m_GLY=1.0;
% Parameter:   id =  g_Na_neurons, name = g_Na_neurons
	global_par_g_Na_neurons=0.0039;
% Parameter:   id =  g_Na_astrocytes, name = g_Na_astrocytes
	global_par_g_Na_astrocytes=0.00325;
% Parameter:   id =  Vm, name = Vm
	global_par_Vm=-70.0;
% Parameter:   id =  RT, name = RT
	global_par_RT=2577340.0;
% Parameter:   id =  F, name = F
	global_par_F=96500.0;
% Parameter:   id =  vn_1_tp, name = vn_1
	global_par_vn_1_tp=0.041;
% Parameter:   id =  vn_2_tp, name = vn_2
	global_par_vn_2_tp=1.44;
% Parameter:   id =  t_stim_tp, name = t_stim_tp
	global_par_t_stim_tp=2.0;
% Parameter:   id =  is_stimulated, name = is_stimulated
% Parameter:   id =  v_stim, name = v_stim
% Parameter:   id =  R_Na_GLU, name = R_Na_GLU
	global_par_R_Na_GLU=0.075;
% Parameter:   id =  V_eg_max_GLU, name = V_eg_max_GLU
	global_par_V_eg_max_GLU=0.0208;
% Parameter:   id =  K_m_GLU, name = K_m_GLU
	global_par_K_m_GLU=0.05;
% Parameter:   id =  V_gn_max_GLU, name = V_gn_max_GLU
	global_par_V_gn_max_GLU=0.3;
% Parameter:   id =  delta_HK, name = delta_HK
	global_par_delta_HK=0.6;
% Parameter:   id =  BOLD_signal, name = BOLD signal
% Parameter:   id =  E0, name = E0
% Parameter:   id =  K_m_ATP_ATPase, name = K_m_ATP(ATPase)
	global_par_K_m_ATP_ATPase=0.001;
% Parameter:   id =  NULL, name = NULL
	global_par_NULL=0.0;
% Parameter:   id =  Vmax_ne_LAC__wrt_extracellular_space, name = Vmax_ne_LAC (wrt extracellular space)
	global_par_Vmax_ne_LAC__wrt_extracellular_space=0.44505;
% Parameter:   id =  PS_cap_astrocytes__wrt_capillaries, name = PS_cap_astrocytes (wrt capillaries)
	global_par_PS_cap_astrocytes__wrt_capillaries=11.16181818;
% Parameter:   id =  PS_cap_neuron__wrt_capillaries, name = PS_cap_neuron (wrt capillaries)
	global_par_PS_cap_neuron__wrt_capillaries=18.01636363;
% Parameter:   id =  Vmax_eg_GLU__wrt_extracellular_space, name = Vmax_eg_GLU (wrt extracellular space)
	global_par_Vmax_eg_GLU__wrt_extracellular_space=0.026;
% Parameter:   id =  K_T_GLC_ce__Aubert, name = K_T_GLC_ce (Aubert)
	global_par_K_T_GLC_ce__Aubert=9.0;
% Parameter:   id =  Vmax_ce_GLC__wrt_capillaries___Aubert, name = Vmax_ce_GLC (wrt capillaries) (Aubert)
% Parameter:   id =  Vmax_eg_GLC__wrt_astrocytes___Aubert_, name = Vmax_eg_GLC (wrt astrocytes) (Aubert) 
% Parameter:   id =  K_T_GLC_eg__Aubert, name = K_T_GLC_eg (Aubert)
	global_par_K_T_GLC_eg__Aubert=9.0;
% Parameter:   id =  K_T_GLC_en__Aubert, name = K_T_GLC_en (Aubert)
	global_par_K_T_GLC_en__Aubert=9.0;
% Parameter:   id =  Vmax_en_GLC__wrt_neurons___Aubert, name = Vmax_en_GLC (wrt neurons) (Aubert)
% Parameter:   id =  K_T_GLC_cg__Aubert, name = K_T_GLC_cg (Aubert)
	global_par_K_T_GLC_cg__Aubert=9.0;
% Parameter:   id =  Vmax_cg_GLC__wrt_capillaries___Aubert, name = Vmax_cg_GLC (wrt capillaries) (Aubert)
% Parameter:   id =  Vmax_ec_LAC__wrt_extracellular_space___Aubert, name = Vmax_ec_LAC (wrt extracellular space) (Aubert)
% Parameter:   id =  Vmax_gc_LAC__wrt_astrocytes___Aubert, name = Vmax_gc_LAC (wrt astrocytes) (Aubert)
% Parameter:   id =  Vmax_ge_LAC__wrt_astrocytes___Aubert, name = Vmax_ge_LAC (wrt astrocytes) (Aubert)
% Parameter:   id =  Vmax_ne_LAC__wrt_neurons___Aubert, name = Vmax_ne_LAC (wrt neurons) (Aubert)
% Parameter:   id =  K_T_LAC_ne__Aubert, name = K_T_LAC_ne (Aubert)
	global_par_K_T_LAC_ne__Aubert=0.5;
% Parameter:   id =  K_T_LAC_ge__Aubert, name = K_T_LAC_ge (Aubert)
	global_par_K_T_LAC_ge__Aubert=0.5;
% Parameter:   id =  K_T_LAC_gc__Aubert, name = K_T_LAC_gc (Aubert)
	global_par_K_T_LAC_gc__Aubert=0.5;
% Parameter:   id =  K_T_LAC_ec__Aubert, name = K_T_LAC_ec (Aubert)
	global_par_K_T_LAC_ec__Aubert=0.5;
% Parameter:   id =  PS_cap_astrocytes__wrt_capillaries___Aubert, name = PS_cap_astrocytes (wrt capillaries) (Aubert)
% Parameter:   id =  PS_cap_neuron__wrt_capillaries___Aubert, name = PS_cap_neuron (wrt capillaries) (Aubert)
% Parameter:   id =  K_O2__Aubert, name = K_O2 (Aubert)
	global_par_K_O2__Aubert=0.0361;
% Parameter:   id =  nh_O2__Aubert, name = nh_O2 (Aubert)
	global_par_nh_O2__Aubert=2.73;
% Parameter:   id =  Vmax_f_PGI__Cloutier, name = Vmax_f_PGI (Cloutier)
	global_par_Vmax_f_PGI__Cloutier=0.5;
% Parameter:   id =  Vmax_r_PGI__Cloutier, name = Vmax_r_PGI (Cloutier)
	global_par_Vmax_r_PGI__Cloutier=0.45;
% Parameter:   id =  Vmax_ce_GLC__Aubert, name = Vmax_ce_GLC (Aubert)
	global_par_Vmax_ce_GLC__Aubert=0.118;
% Parameter:   id =  Vmax_cg_GLC___Aubert, name = Vmax_cg_GLC  (Aubert)
	global_par_Vmax_cg_GLC___Aubert=0.0093;
% Parameter:   id =  Vmax_eg_GLC__Aubert_, name = Vmax_eg_GLC (Aubert) 
	global_par_Vmax_eg_GLC__Aubert_=1020.0;
% Parameter:   id =  Vmax_en_GLC___Aubert, name = Vmax_en_GLC  (Aubert)
	global_par_Vmax_en_GLC___Aubert=5230.0;
% Parameter:   id =  _sf, name = sf
	global_par__sf=0.75;
% Parameter:   id =  _PScap, name = _PScap
	global_par__PScap=1.1;
% Parameter:   id =  f_CBF_dyn, name = f_CBF_dyn
% Parameter:   id =  stimulus, name = stimulus
% Parameter:   id =  Metabolite_123, name = Initial for O2
	global_par_Metabolite_123=8.34000000000002;
% Parameter:   id =  Metabolite_1, name = Initial for O2
	global_par_Metabolite_1=7.33150759029891;
% Parameter:   id =  Metabolite_9, name = Initial for dHb
	global_par_Metabolite_9=0.0478025402198328;
% Parameter:   id =  Compartment_9, name = Initial for venous balloon
	global_par_Compartment_9=0.0237;
% Parameter:   id =  ModelValue_54, name = Initial for E0
	global_par_ModelValue_54=0.241844702566212;
% Parameter:   id =  ModelValue_7, name = Initial for NADH_total_astrocytes
	global_par_ModelValue_7=0.22;
% Parameter:   id =  ModelValue_6, name = Initial for NADH_total_neurons
	global_par_ModelValue_6=0.22;
% Parameter:   id =  ModelValue_16, name = Initial for PCr_total
	global_par_ModelValue_16=5.0;
% Parameter:   id =  ModelValue_83, name = Initial for Vmax_ce_GLC (Aubert)
	global_par_ModelValue_83=0.118;
% Parameter:   id =  ModelValue_84, name = Initial for Vmax_cg_GLC  (Aubert)
	global_par_ModelValue_84=0.0093;
% Parameter:   id =  ModelValue_86, name = Initial for Vmax_en_GLC  (Aubert)
	global_par_ModelValue_86=5230.0;
% Parameter:   id =  ModelValue_43, name = Initial for vn_1
	global_par_ModelValue_43=0.041;
% assignmentRule: variable = E0
	global_par_E0=1-(2*global_par_Metabolite_1-global_par_Metabolite_123)/(const_species_O2_artery/compartment_artery);
% assignmentRule: variable = BOLD_signal
	global_par_BOLD_signal=global_par_Compartment_9*7*(1-x(5)/compartment_compartment_1/global_par_Metabolite_9)+2*(1-x(5)/compartment_compartment_1/global_par_Metabolite_9)/(compartment_venous_balloon/global_par_Compartment_9)+(2*global_par_ModelValue_54-0.2)*(1-compartment_venous_balloon/global_par_Compartment_9);
% assignmentRule: variable = Vmax_ce_GLC__wrt_capillaries___Aubert
	global_par_Vmax_ce_GLC__wrt_capillaries___Aubert=global_par_ModelValue_83*compartment_compartment_4/compartment_compartment_1;
% assignmentRule: variable = Vmax_eg_GLC__wrt_astrocytes___Aubert_
	global_par_Vmax_eg_GLC__wrt_astrocytes___Aubert_=global_par_Vmax_eg_GLC__Aubert_*compartment_compartment_3/compartment_compartment_4;
% assignmentRule: variable = Vmax_en_GLC__wrt_neurons___Aubert
	global_par_Vmax_en_GLC__wrt_neurons___Aubert=global_par_ModelValue_86*compartment_compartment_2/compartment_compartment_4;
% assignmentRule: variable = Vmax_cg_GLC__wrt_capillaries___Aubert
	global_par_Vmax_cg_GLC__wrt_capillaries___Aubert=global_par_ModelValue_84*compartment_compartment_3/compartment_compartment_1;
% assignmentRule: variable = Vmax_ec_LAC__wrt_extracellular_space___Aubert
	global_par_Vmax_ec_LAC__wrt_extracellular_space___Aubert=0.00783*global_par__sf;
% assignmentRule: variable = Vmax_gc_LAC__wrt_astrocytes___Aubert
	global_par_Vmax_gc_LAC__wrt_astrocytes___Aubert=0.0058*global_par__sf;
% assignmentRule: variable = Vmax_ge_LAC__wrt_astrocytes___Aubert
	global_par_Vmax_ge_LAC__wrt_astrocytes___Aubert=0.076*global_par__sf;
% assignmentRule: variable = Vmax_ne_LAC__wrt_neurons___Aubert
	global_par_Vmax_ne_LAC__wrt_neurons___Aubert=0.29*global_par__sf;
% assignmentRule: variable = PS_cap_astrocytes__wrt_capillaries___Aubert
	global_par_PS_cap_astrocytes__wrt_capillaries___Aubert=0.414*0.25*compartment_compartment_3/compartment_compartment_1;
% assignmentRule: variable = PS_cap_neuron__wrt_capillaries___Aubert
	global_par_PS_cap_neuron__wrt_capillaries___Aubert=global_par__PScap*0.45*compartment_compartment_2/compartment_compartment_1;
% assignmentRule: variable = f_CBF_dyn
	global_par_f_CBF_dyn=1+global_par_delta_F*(1/(1+exp((-4.59186)*(time-((global_par_t_0+global_par_t_1)-3))))-1/(1+exp((-4.59186)*(time-(global_par_t_0+global_par_t_1+global_par_t_end+3)))));
% assignmentRule: variable = stimulus
	global_par_stimulus=global_par_ModelValue_43+global_par_vn_2_tp*(time-global_par_t_0)/global_par_t_stim_tp*exp((-(time-global_par_t_0))/global_par_t_stim_tp);
% assignmentRule: variable = parameter_9
	global_par_parameter_9=global_par_ModelValue_6-x(11)/compartment_compartment_2;
% assignmentRule: variable = parameter_10
	global_par_parameter_10=global_par_ModelValue_7-x(37)/compartment_compartment_3;
% assignmentRule: variable = parameter_20
	global_par_parameter_20=global_par_ModelValue_16-x(16)/compartment_compartment_2;
% assignmentRule: variable = parameter_21
	global_par_parameter_21=global_par_ModelValue_16-x(42)/compartment_compartment_3;
% assignmentRule: variable = F_in
	global_par_F_in=global_par_F_0*global_par_f_CBF_dyn;
% assignmentRule: variable = F_out
	global_par_F_out=global_par_F_0*((compartment_venous_balloon/global_par_Compartment_9)^(1/0.5)+(compartment_venous_balloon/global_par_Compartment_9)^((-1)/2)*global_par_tau_v/global_par_Compartment_9*global_par_F_in)/(1+global_par_F_0*(compartment_venous_balloon/global_par_Compartment_9)^((-1)/2)*global_par_tau_v/global_par_Compartment_9);
% assignmentRule: variable = is_stimulated
	global_par_is_stimulated=piecewise(0, (time <= 200) || (time >= (global_par_t_0+global_par_t_end)), 1);
% assignmentRule: variable = v_stim
	global_par_v_stim=global_par_is_stimulated*global_par_stimulus;
% rateRule: variable = venous_balloon
compartment_venous_balloon = x(61);

% Reaction: id = reaction_2, name = HK_astrocytes (R01786)  (HeinrichSchuster)	% Local Parameter:   id =  k_HK, name = k_HK
	reaction_reaction_2_k_HK=0.01;
	% Local Parameter:   id =  K_I_G6P, name = K_I_G6P
	reaction_reaction_2_K_I_G6P=0.02;

	reaction_reaction_2=compartment_compartment_3*vHK__HS(reaction_reaction_2_k_HK, x(33)/compartment_compartment_3, x(34)/compartment_compartment_3, reaction_reaction_2_K_I_G6P);

% Reaction: id = reaction_5, name = PFK_neurons (R04779, R01070, R01015)	% Local Parameter:   id =  k_PFK, name = k_PFK
	reaction_reaction_5_k_PFK=0.44;
	% Local Parameter:   id =  K_m_F6P, name = K_m_F6P
	reaction_reaction_5_K_m_F6P=0.18;

	reaction_reaction_5=compartment_compartment_2*vPFK(reaction_reaction_5_k_PFK, x(8)/compartment_compartment_2, global_par_parameter_5, global_par_parameter_6, x(9)/compartment_compartment_2, reaction_reaction_5_K_m_F6P);

% Reaction: id = reaction_6, name = PFK_astrocytes (R04779, R01070, R01015)	% Local Parameter:   id =  k_PFK, name = k_PFK
	reaction_reaction_6_k_PFK=0.2;
	% Local Parameter:   id =  K_m_F6P, name = K_m_F6P
	reaction_reaction_6_K_m_F6P=0.18;

	reaction_reaction_6=compartment_compartment_3*vPFK(reaction_reaction_6_k_PFK, x(33)/compartment_compartment_3, global_par_parameter_5, global_par_parameter_6, x(35)/compartment_compartment_3, reaction_reaction_6_K_m_F6P);

% Reaction: id = reaction_7, name = PGK_neurons (R01061, R01512, R01518, R00658)	% Local Parameter:   id =  k_PGK, name = k_PGK
	reaction_reaction_7_k_PGK=10.0;

	reaction_reaction_7=compartment_compartment_2*vPGK(reaction_reaction_7_k_PGK, x(10)/compartment_compartment_2, x(19)/compartment_compartment_2, x(22)/compartment_compartment_2, x(11)/compartment_compartment_2);

% Reaction: id = reaction_8, name = PGK_astrocytes (R01061, R01512, R01518, R00658)	% Local Parameter:   id =  k_PGK, name = k_PGK
	reaction_reaction_8_k_PGK=3.0;

	reaction_reaction_8=compartment_compartment_3*vPGK(reaction_reaction_8_k_PGK, x(36)/compartment_compartment_3, x(45)/compartment_compartment_3, x(48)/compartment_compartment_3, x(37)/compartment_compartment_3);

% Reaction: id = reaction_9, name = PK_neurons (R00200)	% Local Parameter:   id =  k_PK, name = k_PK
	reaction_reaction_9_k_PK=44.0;

	reaction_reaction_9=compartment_compartment_2*vPK(reaction_reaction_9_k_PK, x(12)/compartment_compartment_2, x(19)/compartment_compartment_2);

% Reaction: id = reaction_10, name = PK_astrocytes (R00200)	% Local Parameter:   id =  k_PK, name = k_PK
	reaction_reaction_10_k_PK=20.0;

	reaction_reaction_10=compartment_compartment_3*vPK(reaction_reaction_10_k_PK, x(38)/compartment_compartment_3, x(45)/compartment_compartment_3);

% Reaction: id = reaction_13, name = mitochondrial_respiration_neurons (n.a.)	% Local Parameter:   id =  v_max_mito, name = v_max_mito
	reaction_reaction_13_v_max_mito=0.1;
	% Local Parameter:   id =  alpha, name = alpha
	reaction_reaction_13_alpha=5.0;
	% Local Parameter:   id =  beta, name = beta
	reaction_reaction_13_beta=20.0;

	reaction_reaction_13=vMITO2__inkl__Volumes(reaction_reaction_13_v_max_mito, x(13)/compartment_compartment_2, global_par_parameter_18, x(19)/compartment_compartment_2, global_par_parameter_16, x(15)/compartment_compartment_2, global_par_parameter_17, reaction_reaction_13_alpha, x(8)/compartment_compartment_2, reaction_reaction_13_beta, compartment_compartment_2);

% Reaction: id = reaction_14, name = mitochondrial_respiration_astrocytes (n.a.)	% Local Parameter:   id =  v_max_mito, name = v_max_mito
	reaction_reaction_14_v_max_mito=0.01;
	% Local Parameter:   id =  alpha, name = alpha
	reaction_reaction_14_alpha=5.0;
	% Local Parameter:   id =  beta, name = beta
	reaction_reaction_14_beta=20.0;

	reaction_reaction_14=vMITO2__inkl__Volumes(reaction_reaction_14_v_max_mito, x(39)/compartment_compartment_3, global_par_parameter_18, x(45)/compartment_compartment_3, global_par_parameter_16, x(41)/compartment_compartment_3, global_par_parameter_17, reaction_reaction_14_alpha, x(33)/compartment_compartment_3, reaction_reaction_14_beta, compartment_compartment_3);

% Reaction: id = reaction_17, name = GLC_exchange_extracellular_space_neurons (Aubert)
	reaction_reaction_17=facilitated_transport__inkl__Volume____scaled(global_par_Vmax_en_GLC__wrt_neurons___Aubert, global_par__sf, x(58)/compartment_compartment_4, global_par_K_T_GLC_en__Aubert, x(6)/compartment_compartment_2, compartment_compartment_4);

% Reaction: id = reaction_18, name = GLC_exchange_extracellular_space_astrocytes (Aubert)
	reaction_reaction_18=facilitated_transport__inkl__Volume____scaled(global_par_Vmax_eg_GLC__wrt_astrocytes___Aubert_, global_par__sf, x(58)/compartment_compartment_4, global_par_K_T_GLC_eg__Aubert, x(32)/compartment_compartment_3, compartment_compartment_4);

% Reaction: id = reaction_19, name = GLC_exchange_capillary_ec (Aubert)
	reaction_reaction_19=facilitated_transport__inkl__Volume____scaled(global_par_Vmax_ce_GLC__wrt_capillaries___Aubert, global_par__sf, x(3)/compartment_compartment_1, global_par_K_T_GLC_ce__Aubert, x(58)/compartment_compartment_4, compartment_compartment_1);

% Reaction: id = reaction_20, name = GLC_exchange_capillary_astrocytes (Aubert)
	reaction_reaction_20=facilitated_transport__inkl__Volume____scaled(global_par_Vmax_cg_GLC__wrt_capillaries___Aubert, global_par__sf, x(3)/compartment_compartment_1, global_par_K_T_GLC_cg__Aubert, x(32)/compartment_compartment_3, compartment_compartment_1);

% Reaction: id = reaction_21, name = LAC_exchange_ec_capillary
	reaction_reaction_21=facilitated_transport__inkl__Volume(global_par_Vmax_ec_LAC__wrt_extracellular_space___Aubert, x(59)/compartment_compartment_4, global_par_K_T_LAC_ec__Aubert, x(4)/compartment_compartment_1, compartment_compartment_4);

% Reaction: id = reaction_22, name = LAC_exchange_neurons_ec
	reaction_reaction_22=facilitated_transport__inkl__Volume(global_par_Vmax_ne_LAC__wrt_neurons___Aubert, x(59)/compartment_compartment_4, global_par_K_T_LAC_ne__Aubert, x(14)/compartment_compartment_2, compartment_compartment_4);

% Reaction: id = reaction_23, name = LAC_exchange_astrocytes_ec
	reaction_reaction_23=facilitated_transport__inkl__Volume(global_par_Vmax_ge_LAC__wrt_astrocytes___Aubert, x(40)/compartment_compartment_3, global_par_K_T_LAC_ge__Aubert, x(59)/compartment_compartment_4, compartment_compartment_3);

% Reaction: id = reaction_24, name = LAC_exchange_astrocytes_capillary
	reaction_reaction_24=facilitated_transport__inkl__Volume(global_par_Vmax_gc_LAC__wrt_astrocytes___Aubert, x(40)/compartment_compartment_3, global_par_K_T_LAC_gc__Aubert, x(4)/compartment_compartment_1, compartment_compartment_3);

% Reaction: id = O2_exchange_capillary_neurons, name = O2_exchange_capillary_neurons
	reaction_O2_exchange_capillary_neurons=O2_transport_function_inkl__volume(global_par_PS_cap_neuron__wrt_capillaries___Aubert, compartment_compartment_2, global_par_K_O2__Aubert, global_par_Hb_OP, x(1)/compartment_compartment_1, global_par_nh_O2__Aubert, x(15)/compartment_compartment_2, compartment_compartment_1);

% Reaction: id = O2_exchange_capillary_astrocytes, name = O2_exchange_capillary_astrocytes	% Local Parameter:   id =  PScap, name = PScap
	reaction_O2_exchange_capillary_astrocytes_PScap=10.0;

	reaction_O2_exchange_capillary_astrocytes=O2_transport_function_inkl__volume(reaction_O2_exchange_capillary_astrocytes_PScap, compartment_compartment_3, global_par_K_O2__Aubert, global_par_Hb_OP, x(1)/compartment_compartment_1, global_par_nh_O2__Aubert, x(41)/compartment_compartment_3, compartment_compartment_1);

% Reaction: id = Blood_flow_contribution_to_capillary_O2, name = O2_exchange_artery_capillary
	reaction_Blood_flow_contribution_to_capillary_O2=Blood_flow_contribution_inkl__volume(global_par_F_in, compartment_compartment_1, compartment_artery, const_species_O2_artery/compartment_artery, x(1)/compartment_compartment_1);

% Reaction: id = Blood_flow_contribution_to_capillary_GLC, name = GLC_exchange_artery_capillary
	reaction_Blood_flow_contribution_to_capillary_GLC=Blood_flow_contribution_inkl__volume(global_par_F_in, compartment_compartment_1, compartment_artery, const_species_GLC_artery/compartment_artery, x(3)/compartment_compartment_1);

% Reaction: id = Blood_flow_contribution_to_capillary_LAC, name = LAC_exchange_capillary_artery
	reaction_Blood_flow_contribution_to_capillary_LAC=Blood_flow_contribution_inkl__volume(global_par_F_in, compartment_compartment_1, compartment_compartment_1, x(4)/compartment_compartment_1, const_species_LAC_artery/compartment_artery);

% Reaction: id = Flow_of_CO2_between_capillary_and_vessel__artery_, name = CO2_exchange_capillary_artery
	reaction_Flow_of_CO2_between_capillary_and_vessel__artery_=Blood_flow_contribution_inkl__volume(global_par_F_in, compartment_compartment_1, compartment_compartment_1, x(2)/compartment_compartment_1, const_species_CO2_artery/compartment_artery);

% Reaction: id = vPUMP_neurons, name = Na+_exchange_neurons_extracellular_space (n.a.)
	reaction_vPUMP_neurons=compartment_compartment_2*vPUMP_volume_dependent(global_par_Sm_n, compartment_compartment_2, global_par_k_pump, x(8)/compartment_compartment_2, x(17)/compartment_compartment_2, global_par_K_m_Na_pump);

% Reaction: id = vPUMP_astrocytes, name = Na+_exchange_astrocytes_extracellular_space (n.a.)
	reaction_vPUMP_astrocytes=compartment_compartment_3*vPUMP_volume_dependent(global_par_Sm_g, compartment_compartment_3, global_par_k_pump, x(33)/compartment_compartment_3, x(43)/compartment_compartment_3, global_par_K_m_Na_pump);

% Reaction: id = vLEAK_Na_neurons, name = LEAK_Na_neurons (n.a.)
	reaction_vLEAK_Na_neurons=vLEAK_Na_inkl__Volume(global_par_Sm_n, global_par_g_Na_neurons, compartment_compartment_2, global_par_F, global_par_RT, const_species_Na__extracellular_space/compartment_compartment_4, x(17)/compartment_compartment_2, global_par_Vm, compartment_compartment_2);

% Reaction: id = vLEAK_Na_astrocytes, name = LEAK_Na_astrocytes (n.a)	% Local Parameter:   id =  gNA, name = gNA
	reaction_vLEAK_Na_astrocytes_gNA=0.0039;

	reaction_vLEAK_Na_astrocytes=vLEAK_Na_inkl__Volume(global_par_Sm_g, reaction_vLEAK_Na_astrocytes_gNA, compartment_compartment_3, global_par_F, global_par_RT, const_species_Na__extracellular_space/compartment_compartment_4, x(43)/compartment_compartment_3, global_par_Vm, compartment_compartment_3);

% Reaction: id = vSTIM, name = Na+_exchange_extracellular_space_neurons  (stimulation)
	reaction_vSTIM=vStim__with_volume(global_par_v_stim, compartment_compartment_2);

% Reaction: id = vGLU_ne, name = GLU_exchange_neurons_extracellular_space
	reaction_vGLU_ne=vGLU_ne__inkl__Volume(global_par_v_stim, global_par_R_Na_GLU, x(18)/compartment_compartment_2, global_par_K_m_GLU, compartment_compartment_2);

% Reaction: id = vGLU_eg, name = GLU_exchange_extracellular_space_astrocytes
	reaction_vGLU_eg=vGLU_eg__inkl__Volumes(global_par_Vmax_eg_GLU__wrt_extracellular_space, x(60)/compartment_compartment_4, global_par_K_m_GLU, compartment_compartment_4);

% Reaction: id = vGLU_gn, name = GLU_exchange_astrocytes_neurons
	reaction_vGLU_gn=vGLU_gn__inkl__Volume(global_par_V_gn_max_GLU, x(44)/compartment_compartment_3, global_par_K_m_GLU, x(33)/compartment_compartment_3, global_par_parameter_14, compartment_compartment_3);

% Reaction: id = inflow_of_dHb, name = inflow of dHb
	reaction_inflow_of_dHb=compartment_compartment_1*vdHb_in(global_par_F_in, const_species_O2_artery/compartment_artery, x(1)/compartment_compartment_1);

% Reaction: id = outflow_of_dHb, name = outflow of dHb
	reaction_outflow_of_dHb=compartment_compartment_1*vdHb_out(global_par_F_out, x(5)/compartment_compartment_1, compartment_venous_balloon);

% Reaction: id = ATPase_neurons, name = ATPase_neurons (n.a.)	% Local Parameter:   id =  VmaxATPase, name = VmaxATPase
	reaction_ATPase_neurons_VmaxATPase=0.07;

	reaction_ATPase_neurons=compartment_compartment_2*vATPase(reaction_ATPase_neurons_VmaxATPase, x(8)/compartment_compartment_2, global_par_K_m_ATP_ATPase);

% Reaction: id = ATPase_astrocytes, name = ATPase_astrocytes (n.a.)	% Local Parameter:   id =  VmaxATPase, name = VmaxATPase
	reaction_ATPase_astrocytes_VmaxATPase=0.035;

	reaction_ATPase_astrocytes=compartment_compartment_3*vATPase(reaction_ATPase_astrocytes_VmaxATPase, x(33)/compartment_compartment_3, global_par_K_m_ATP_ATPase);

% Reaction: id = AK_neurons, name = AK_neurons (R00127)	% Local Parameter:   id =  k1, name = k1
	reaction_AK_neurons_k1=1000.0;
	% Local Parameter:   id =  k2, name = k2
	reaction_AK_neurons_k2=920.0;

	reaction_AK_neurons=compartment_compartment_2*(reaction_AK_neurons_k1*(x(19)/compartment_compartment_2)^2-reaction_AK_neurons_k2*x(8)/compartment_compartment_2*x(20)/compartment_compartment_2);

% Reaction: id = AK_astrocytes, name = AK_astrocytes (R00127)	% Local Parameter:   id =  k1, name = k1
	reaction_AK_astrocytes_k1=1000.0;
	% Local Parameter:   id =  k2, name = k2
	reaction_AK_astrocytes_k2=920.0;

	reaction_AK_astrocytes=compartment_compartment_3*(reaction_AK_astrocytes_k1*(x(45)/compartment_compartment_3)^2-reaction_AK_astrocytes_k2*x(33)/compartment_compartment_3*x(46)/compartment_compartment_3);

% Reaction: id = CK_astrocytes_forward__R01881, name = CK_astrocytes (R01881)	% Local Parameter:   id =  k1, name = k1
	reaction_CK_astrocytes_forward__R01881_k1=0.5;
	% Local Parameter:   id =  k2, name = k2
	reaction_CK_astrocytes_forward__R01881_k2=0.01;

	reaction_CK_astrocytes_forward__R01881=compartment_compartment_3*(reaction_CK_astrocytes_forward__R01881_k1*x(45)/compartment_compartment_3*x(42)/compartment_compartment_3-reaction_CK_astrocytes_forward__R01881_k2*x(33)/compartment_compartment_3*x(47)/compartment_compartment_3);

% Reaction: id = CK_neurons_forward__R01881, name = CK_neurons (R01881)	% Local Parameter:   id =  k1, name = k1
	reaction_CK_neurons_forward__R01881_k1=0.5;
	% Local Parameter:   id =  k2, name = k2
	reaction_CK_neurons_forward__R01881_k2=0.01;

	reaction_CK_neurons_forward__R01881=compartment_compartment_2*(reaction_CK_neurons_forward__R01881_k1*x(16)/compartment_compartment_2*x(19)/compartment_compartment_2-reaction_CK_neurons_forward__R01881_k2*x(8)/compartment_compartment_2*x(21)/compartment_compartment_2);

% Reaction: id = LDH_astrocytes_forward__R00703, name = LDH_astrocytes (R00703)	% Local Parameter:   id =  k1, name = k1
	reaction_LDH_astrocytes_forward__R00703_k1=780.0;
	% Local Parameter:   id =  k2, name = k2
	reaction_LDH_astrocytes_forward__R00703_k2=32.0;

	reaction_LDH_astrocytes_forward__R00703=compartment_compartment_3*(reaction_LDH_astrocytes_forward__R00703_k1*x(39)/compartment_compartment_3*x(37)/compartment_compartment_3-reaction_LDH_astrocytes_forward__R00703_k2*x(40)/compartment_compartment_3*x(48)/compartment_compartment_3);

% Reaction: id = LDH_neurons_forward__R00703, name = LDH_neurons (R00703)	% Local Parameter:   id =  k1, name = k1
	reaction_LDH_neurons_forward__R00703_k1=2000.0;
	% Local Parameter:   id =  k2, name = k2
	reaction_LDH_neurons_forward__R00703_k2=15.0;

	reaction_LDH_neurons_forward__R00703=compartment_compartment_2*(reaction_LDH_neurons_forward__R00703_k1*x(13)/compartment_compartment_2*x(11)/compartment_compartment_2-reaction_LDH_neurons_forward__R00703_k2*x(14)/compartment_compartment_2*x(22)/compartment_compartment_2);

% Reaction: id = ZWF_astrocytes__R02736, name = ZWF_astrocytes (R02736)	% Local Parameter:   id =  Vmax, name = Vmax
	reaction_ZWF_astrocytes__R02736_Vmax=0.29057;
	% Local Parameter:   id =  K_S1, name = K_S1
	reaction_ZWF_astrocytes__R02736_K_S1=6.91392E-5;
	% Local Parameter:   id =  K_S2, name = K_S2
	reaction_ZWF_astrocytes__R02736_K_S2=1.31616E-5;
	% Local Parameter:   id =  Keq, name = Keq
	reaction_ZWF_astrocytes__R02736_Keq=22906.4;
	% Local Parameter:   id =  K_P1, name = K_P1
	reaction_ZWF_astrocytes__R02736_K_P1=0.0180932;
	% Local Parameter:   id =  K_P2, name = K_P2
	reaction_ZWF_astrocytes__R02736_K_P2=5.0314E-4;

	reaction_ZWF_astrocytes__R02736=compartment_compartment_3*modular_rate_law_for_two_substrates__two_products(reaction_ZWF_astrocytes__R02736_Vmax, reaction_ZWF_astrocytes__R02736_K_S1, reaction_ZWF_astrocytes__R02736_K_S2, x(34)/compartment_compartment_3, x(56)/compartment_compartment_3, x(49)/compartment_compartment_3, x(57)/compartment_compartment_3, reaction_ZWF_astrocytes__R02736_Keq, reaction_ZWF_astrocytes__R02736_K_P1, reaction_ZWF_astrocytes__R02736_K_P2);

% Reaction: id = ZWF_neurons__R02736, name = ZWF_neurons (R02736)	% Local Parameter:   id =  Vmax, name = Vmax
	reaction_ZWF_neurons__R02736_Vmax=0.586458;
	% Local Parameter:   id =  K_S1, name = K_S1
	reaction_ZWF_neurons__R02736_K_S1=6.91392E-5;
	% Local Parameter:   id =  K_S2, name = K_S2
	reaction_ZWF_neurons__R02736_K_S2=1.31616E-5;
	% Local Parameter:   id =  Keq, name = Keq
	reaction_ZWF_neurons__R02736_Keq=22906.4;
	% Local Parameter:   id =  K_P1, name = K_P1
	reaction_ZWF_neurons__R02736_K_P1=0.0180932;
	% Local Parameter:   id =  K_P2, name = K_P2
	reaction_ZWF_neurons__R02736_K_P2=5.0314E-4;

	reaction_ZWF_neurons__R02736=compartment_compartment_2*modular_rate_law_for_two_substrates__two_products(reaction_ZWF_neurons__R02736_Vmax, reaction_ZWF_neurons__R02736_K_S1, reaction_ZWF_neurons__R02736_K_S2, x(7)/compartment_compartment_2, x(31)/compartment_compartment_2, x(23)/compartment_compartment_2, x(30)/compartment_compartment_2, reaction_ZWF_neurons__R02736_Keq, reaction_ZWF_neurons__R02736_K_P1, reaction_ZWF_neurons__R02736_K_P2);

% Reaction: id = SOL_neurons__R02035, name = SOL_neurons (R02035)	% Local Parameter:   id =  Vmax, name = Vmax
	reaction_SOL_neurons__R02035_Vmax=0.372782;
	% Local Parameter:   id =  K_S1, name = K_S1
	reaction_SOL_neurons__R02035_K_S1=0.0180932;
	% Local Parameter:   id =  Keq, name = Keq
	reaction_SOL_neurons__R02035_Keq=531174.0;
	% Local Parameter:   id =  K_P1, name = K_P1
	reaction_SOL_neurons__R02035_K_P1=2.28618;

	reaction_SOL_neurons__R02035=compartment_compartment_2*modular_rate_law_for_one_substrate__one_product(reaction_SOL_neurons__R02035_Vmax, reaction_SOL_neurons__R02035_K_S1, x(23)/compartment_compartment_2, x(24)/compartment_compartment_2, reaction_SOL_neurons__R02035_Keq, reaction_SOL_neurons__R02035_K_P1);

% Reaction: id = SOL_astrocytes__R02035, name = SOL_astrocytes (R02035)	% Local Parameter:   id =  Vmax, name = Vmax
	reaction_SOL_astrocytes__R02035_Vmax=0.184701;
	% Local Parameter:   id =  K_S1, name = K_S1
	reaction_SOL_astrocytes__R02035_K_S1=0.0180932;
	% Local Parameter:   id =  Keq, name = Keq
	reaction_SOL_astrocytes__R02035_Keq=531174.0;
	% Local Parameter:   id =  K_P1, name = K_P1
	reaction_SOL_astrocytes__R02035_K_P1=2.28618;

	reaction_SOL_astrocytes__R02035=compartment_compartment_3*modular_rate_law_for_one_substrate__one_product(reaction_SOL_astrocytes__R02035_Vmax, reaction_SOL_astrocytes__R02035_K_S1, x(49)/compartment_compartment_3, x(50)/compartment_compartment_3, reaction_SOL_astrocytes__R02035_Keq, reaction_SOL_astrocytes__R02035_K_P1);

% Reaction: id = GND_neurons___R01528, name = GND_neurons  (R01528)	% Local Parameter:   id =  Vmax, name = Vmax
	reaction_GND_neurons___R01528_Vmax=2.65764;
	% Local Parameter:   id =  K_S1, name = K_S1
	reaction_GND_neurons___R01528_K_S1=3.23421E-5;
	% Local Parameter:   id =  K_S2, name = K_S2
	reaction_GND_neurons___R01528_K_S2=3.11043E-6;
	% Local Parameter:   id =  Keq, name = Keq
	reaction_GND_neurons___R01528_Keq=4.0852E7;
	% Local Parameter:   id =  K_P1, name = K_P1
	reaction_GND_neurons___R01528_K_P1=0.0537179;
	% Local Parameter:   id =  K_P2, name = K_P2
	reaction_GND_neurons___R01528_K_P2=5.0314E-4;

	reaction_GND_neurons___R01528=compartment_compartment_2*modular_rate_law_for_two_substrates__two_products(reaction_GND_neurons___R01528_Vmax, reaction_GND_neurons___R01528_K_S1, reaction_GND_neurons___R01528_K_S2, x(24)/compartment_compartment_2, x(31)/compartment_compartment_2, x(25)/compartment_compartment_2, x(30)/compartment_compartment_2, reaction_GND_neurons___R01528_Keq, reaction_GND_neurons___R01528_K_P1, reaction_GND_neurons___R01528_K_P2);

% Reaction: id = GND_astrocytes__R01528, name = GND_astrocytes (R01528)	% Local Parameter:   id =  Vmax, name = Vmax
	reaction_GND_astrocytes__R01528_Vmax=1.31677;
	% Local Parameter:   id =  K_S1, name = K_S1
	reaction_GND_astrocytes__R01528_K_S1=3.23421E-5;
	% Local Parameter:   id =  K_S2, name = K_S2
	reaction_GND_astrocytes__R01528_K_S2=3.11043E-6;
	% Local Parameter:   id =  Keq, name = Keq
	reaction_GND_astrocytes__R01528_Keq=4.0852E7;
	% Local Parameter:   id =  K_P1, name = K_P1
	reaction_GND_astrocytes__R01528_K_P1=5.0314E-4;
	% Local Parameter:   id =  K_P2, name = K_P2
	reaction_GND_astrocytes__R01528_K_P2=0.0537179;

	reaction_GND_astrocytes__R01528=compartment_compartment_3*modular_rate_law_for_two_substrates__two_products(reaction_GND_astrocytes__R01528_Vmax, reaction_GND_astrocytes__R01528_K_S1, reaction_GND_astrocytes__R01528_K_S2, x(50)/compartment_compartment_3, x(56)/compartment_compartment_3, x(51)/compartment_compartment_3, x(57)/compartment_compartment_3, reaction_GND_astrocytes__R01528_Keq, reaction_GND_astrocytes__R01528_K_P1, reaction_GND_astrocytes__R01528_K_P2);

% Reaction: id = RPE_neurons__R01529, name = RPE_neurons (R01529)	% Local Parameter:   id =  Vmax, name = Vmax
	reaction_RPE_neurons__R01529_Vmax=0.0156605;
	% Local Parameter:   id =  K_S1, name = K_S1
	reaction_RPE_neurons__R01529_K_S1=0.0537179;
	% Local Parameter:   id =  Keq, name = Keq
	reaction_RPE_neurons__R01529_Keq=39.2574;
	% Local Parameter:   id =  K_P1, name = K_P1
	reaction_RPE_neurons__R01529_K_P1=0.603002;

	reaction_RPE_neurons__R01529=compartment_compartment_2*modular_rate_law_for_one_substrate__one_product(reaction_RPE_neurons__R01529_Vmax, reaction_RPE_neurons__R01529_K_S1, x(25)/compartment_compartment_2, x(26)/compartment_compartment_2, reaction_RPE_neurons__R01529_Keq, reaction_RPE_neurons__R01529_K_P1);

% Reaction: id = RPE_astrocytes__R01529, name = RPE_astrocytes (R01529)	% Local Parameter:   id =  Vmax, name = Vmax
	reaction_RPE_astrocytes__R01529_Vmax=0.00775925;
	% Local Parameter:   id =  K_S1, name = K_S1
	reaction_RPE_astrocytes__R01529_K_S1=0.0537179;
	% Local Parameter:   id =  Keq, name = Keq
	reaction_RPE_astrocytes__R01529_Keq=39.2574;
	% Local Parameter:   id =  K_P1, name = K_P1
	reaction_RPE_astrocytes__R01529_K_P1=0.603002;

	reaction_RPE_astrocytes__R01529=compartment_compartment_3*modular_rate_law_for_one_substrate__one_product(reaction_RPE_astrocytes__R01529_Vmax, reaction_RPE_astrocytes__R01529_K_S1, x(51)/compartment_compartment_3, x(52)/compartment_compartment_3, reaction_RPE_astrocytes__R01529_Keq, reaction_RPE_astrocytes__R01529_K_P1);

% Reaction: id = RKI_astrocytes__R01056, name = RKI_astrocytes (R01056)	% Local Parameter:   id =  Vmax, name = Vmax
	reaction_RKI_astrocytes__R01056_Vmax=8.21984E-4;
	% Local Parameter:   id =  K_S1, name = K_S1
	reaction_RKI_astrocytes__R01056_K_S1=0.0537179;
	% Local Parameter:   id =  Keq, name = Keq
	reaction_RKI_astrocytes__R01056_Keq=35.4534;
	% Local Parameter:   id =  K_P1, name = K_P1
	reaction_RKI_astrocytes__R01056_K_P1=0.778461;

	reaction_RKI_astrocytes__R01056=compartment_compartment_3*modular_rate_law_for_one_substrate__one_product(reaction_RKI_astrocytes__R01056_Vmax, reaction_RKI_astrocytes__R01056_K_S1, x(51)/compartment_compartment_3, x(53)/compartment_compartment_3, reaction_RKI_astrocytes__R01056_Keq, reaction_RKI_astrocytes__R01056_K_P1);

% Reaction: id = RKI_neurons__R01056, name = RKI_neurons (R01056)	% Local Parameter:   id =  Vmax, name = Vmax
	reaction_RKI_neurons__R01056_Vmax=0.00165901;
	% Local Parameter:   id =  K_S1, name = K_S1
	reaction_RKI_neurons__R01056_K_S1=0.0537179;
	% Local Parameter:   id =  Keq, name = Keq
	reaction_RKI_neurons__R01056_Keq=35.4534;
	% Local Parameter:   id =  K_P1, name = K_P1
	reaction_RKI_neurons__R01056_K_P1=0.778461;

	reaction_RKI_neurons__R01056=compartment_compartment_2*modular_rate_law_for_one_substrate__one_product(reaction_RKI_neurons__R01056_Vmax, reaction_RKI_neurons__R01056_K_S1, x(25)/compartment_compartment_2, x(27)/compartment_compartment_2, reaction_RKI_neurons__R01056_Keq, reaction_RKI_neurons__R01056_K_P1);

% Reaction: id = TKL_1_astrocytes__R01641, name = TKL-1_astrocytes (R01641)	% Local Parameter:   id =  Vmax, name = Vmax
	reaction_TKL_1_astrocytes__R01641_Vmax=2.44278E-4;
	% Local Parameter:   id =  K_S1, name = K_S1
	reaction_TKL_1_astrocytes__R01641_K_S1=1.73625E-4;
	% Local Parameter:   id =  K_S2, name = K_S2
	reaction_TKL_1_astrocytes__R01641_K_S2=5.85387E-4;
	% Local Parameter:   id =  Keq, name = Keq
	reaction_TKL_1_astrocytes__R01641_Keq=1652870.0;
	% Local Parameter:   id =  K_P1, name = K_P1
	reaction_TKL_1_astrocytes__R01641_K_P1=0.168333;
	% Local Parameter:   id =  K_P2, name = K_P2
	reaction_TKL_1_astrocytes__R01641_K_P2=0.192807;

	reaction_TKL_1_astrocytes__R01641=compartment_compartment_3*modular_rate_law_for_two_substrates__two_products(reaction_TKL_1_astrocytes__R01641_Vmax, reaction_TKL_1_astrocytes__R01641_K_S1, reaction_TKL_1_astrocytes__R01641_K_S2, x(52)/compartment_compartment_3, x(53)/compartment_compartment_3, x(36)/compartment_compartment_3, x(54)/compartment_compartment_3, reaction_TKL_1_astrocytes__R01641_Keq, reaction_TKL_1_astrocytes__R01641_K_P1, reaction_TKL_1_astrocytes__R01641_K_P2);

% Reaction: id = TKL_1_neurons__R01641, name = TKL-1_neurons (R01641)	% Local Parameter:   id =  Vmax, name = Vmax
	reaction_TKL_1_neurons__R01641_Vmax=4.93027E-4;
	% Local Parameter:   id =  K_S1, name = K_S1
	reaction_TKL_1_neurons__R01641_K_S1=1.73625E-4;
	% Local Parameter:   id =  K_S2, name = K_S2
	reaction_TKL_1_neurons__R01641_K_S2=5.85387E-4;
	% Local Parameter:   id =  Keq, name = Keq
	reaction_TKL_1_neurons__R01641_Keq=1652870.0;
	% Local Parameter:   id =  K_P1, name = K_P1
	reaction_TKL_1_neurons__R01641_K_P1=0.168333;
	% Local Parameter:   id =  K_P2, name = K_P2
	reaction_TKL_1_neurons__R01641_K_P2=0.192807;

	reaction_TKL_1_neurons__R01641=compartment_compartment_2*modular_rate_law_for_two_substrates__two_products(reaction_TKL_1_neurons__R01641_Vmax, reaction_TKL_1_neurons__R01641_K_S1, reaction_TKL_1_neurons__R01641_K_S2, x(26)/compartment_compartment_2, x(27)/compartment_compartment_2, x(10)/compartment_compartment_2, x(28)/compartment_compartment_2, reaction_TKL_1_neurons__R01641_Keq, reaction_TKL_1_neurons__R01641_K_P1, reaction_TKL_1_neurons__R01641_K_P2);

% Reaction: id = TAL_astrocytes__R01827, name = TAL_astrocytes (R01827)	% Local Parameter:   id =  Vmax, name = Vmax
	reaction_TAL_astrocytes__R01827_Vmax=0.0080394;
	% Local Parameter:   id =  K_S1, name = K_S1
	reaction_TAL_astrocytes__R01827_K_S1=0.168333;
	% Local Parameter:   id =  K_S2, name = K_S2
	reaction_TAL_astrocytes__R01827_K_S2=0.192807;
	% Local Parameter:   id =  Keq, name = Keq
	reaction_TAL_astrocytes__R01827_Keq=0.323922;
	% Local Parameter:   id =  K_P1, name = K_P1
	reaction_TAL_astrocytes__R01827_K_P1=0.0799745;
	% Local Parameter:   id =  K_P2, name = K_P2
	reaction_TAL_astrocytes__R01827_K_P2=0.109681;

	reaction_TAL_astrocytes__R01827=compartment_compartment_3*modular_rate_law_for_two_substrates__two_products(reaction_TAL_astrocytes__R01827_Vmax, reaction_TAL_astrocytes__R01827_K_S1, reaction_TAL_astrocytes__R01827_K_S2, x(36)/compartment_compartment_3, x(54)/compartment_compartment_3, x(35)/compartment_compartment_3, x(55)/compartment_compartment_3, reaction_TAL_astrocytes__R01827_Keq, reaction_TAL_astrocytes__R01827_K_P1, reaction_TAL_astrocytes__R01827_K_P2);

% Reaction: id = TAL_neurons__R01827, name = TAL_neurons (R01827)	% Local Parameter:   id =  Vmax, name = Vmax
	reaction_TAL_neurons__R01827_Vmax=0.0162259;
	% Local Parameter:   id =  K_S1, name = K_S1
	reaction_TAL_neurons__R01827_K_S1=0.168333;
	% Local Parameter:   id =  K_S2, name = K_S2
	reaction_TAL_neurons__R01827_K_S2=0.192807;
	% Local Parameter:   id =  Keq, name = Keq
	reaction_TAL_neurons__R01827_Keq=0.323922;
	% Local Parameter:   id =  K_P1, name = K_P1
	reaction_TAL_neurons__R01827_K_P1=0.0799745;
	% Local Parameter:   id =  K_P2, name = K_P2
	reaction_TAL_neurons__R01827_K_P2=0.109681;

	reaction_TAL_neurons__R01827=compartment_compartment_2*modular_rate_law_for_two_substrates__two_products(reaction_TAL_neurons__R01827_Vmax, reaction_TAL_neurons__R01827_K_S1, reaction_TAL_neurons__R01827_K_S2, x(10)/compartment_compartment_2, x(28)/compartment_compartment_2, x(9)/compartment_compartment_2, x(29)/compartment_compartment_2, reaction_TAL_neurons__R01827_Keq, reaction_TAL_neurons__R01827_K_P1, reaction_TAL_neurons__R01827_K_P2);

% Reaction: id = TKL_2_astrocytes__R01830, name = TKL-2_astrocytes (R01830)	% Local Parameter:   id =  Vmax, name = Vmax
	reaction_TKL_2_astrocytes__R01830_Vmax=1.37124E-4;
	% Local Parameter:   id =  K_S1, name = K_S1
	reaction_TKL_2_astrocytes__R01830_K_S1=0.0799745;
	% Local Parameter:   id =  K_S2, name = K_S2
	reaction_TKL_2_astrocytes__R01830_K_S2=0.168333;
	% Local Parameter:   id =  Keq, name = Keq
	reaction_TKL_2_astrocytes__R01830_Keq=0.0777764;
	% Local Parameter:   id =  K_P1, name = K_P1
	reaction_TKL_2_astrocytes__R01830_K_P1=0.603002;
	% Local Parameter:   id =  K_P2, name = K_P2
	reaction_TKL_2_astrocytes__R01830_K_P2=0.109681;

	reaction_TKL_2_astrocytes__R01830=compartment_compartment_3*modular_rate_law_for_two_substrates__two_products(reaction_TKL_2_astrocytes__R01830_Vmax, reaction_TKL_2_astrocytes__R01830_K_S1, reaction_TKL_2_astrocytes__R01830_K_S2, x(35)/compartment_compartment_3, x(36)/compartment_compartment_3, x(52)/compartment_compartment_3, x(55)/compartment_compartment_3, reaction_TKL_2_astrocytes__R01830_Keq, reaction_TKL_2_astrocytes__R01830_K_P1, reaction_TKL_2_astrocytes__R01830_K_P2);

% Reaction: id = TKL_2_neurons__R01830, name = TKL-2_neurons (R01830)	% Local Parameter:   id =  Vmax, name = Vmax
	reaction_TKL_2_neurons__R01830_Vmax=2.76758E-4;
	% Local Parameter:   id =  K_S1, name = K_S1
	reaction_TKL_2_neurons__R01830_K_S1=0.0799745;
	% Local Parameter:   id =  K_S2, name = K_S2
	reaction_TKL_2_neurons__R01830_K_S2=0.168333;
	% Local Parameter:   id =  Keq, name = Keq
	reaction_TKL_2_neurons__R01830_Keq=0.0777764;
	% Local Parameter:   id =  K_P1, name = K_P1
	reaction_TKL_2_neurons__R01830_K_P1=0.603002;
	% Local Parameter:   id =  K_P2, name = K_P2
	reaction_TKL_2_neurons__R01830_K_P2=0.109681;

	reaction_TKL_2_neurons__R01830=compartment_compartment_2*modular_rate_law_for_two_substrates__two_products(reaction_TKL_2_neurons__R01830_Vmax, reaction_TKL_2_neurons__R01830_K_S1, reaction_TKL_2_neurons__R01830_K_S2, x(9)/compartment_compartment_2, x(10)/compartment_compartment_2, x(26)/compartment_compartment_2, x(29)/compartment_compartment_2, reaction_TKL_2_neurons__R01830_Keq, reaction_TKL_2_neurons__R01830_K_P1, reaction_TKL_2_neurons__R01830_K_P2);

% Reaction: id = NADPH_oxidase_neurons__R07172, name = NADPH oxidase neurons (R07172)	% Local Parameter:   id =  k1, name = k1
	reaction_NADPH_oxidase_neurons__R07172_k1=4.23283E-4;

	reaction_NADPH_oxidase_neurons__R07172=reaction_NADPH_oxidase_neurons__R07172_k1*x(30);

% Reaction: id = NADPH_oxidase_astrocytes__R07172, name = NADPH oxidase astrocytes (R07172)	% Local Parameter:   id =  k1, name = k1
	reaction_NADPH_oxidase_astrocytes__R07172_k1=2.09722E-4;

	reaction_NADPH_oxidase_astrocytes__R07172=reaction_NADPH_oxidase_astrocytes__R07172_k1*x(57);

% Reaction: id = R5P_sink_astrocytes__n_a_, name = R5P sink_astrocytes (n.a.)
	reaction_R5P_sink_astrocytes__n_a_=global_par_NULL*x(53);

% Reaction: id = R5P_sink_neurons__n_a_, name = R5P sink_neurons (n.a.)	% Local Parameter:   id =  k1, name = k1
	reaction_R5P_sink_neurons__n_a__k1=0.0;

	reaction_R5P_sink_neurons__n_a_=reaction_R5P_sink_neurons__n_a__k1*x(27);

% Reaction: id = PGI_astrocytes__R02740___HS, name = PGI_astrocytes (R02740) (HS)	% Local Parameter:   id =  k1, name = k1
	reaction_PGI_astrocytes__R02740___HS_k1=931.69;
	% Local Parameter:   id =  k2, name = k2
	reaction_PGI_astrocytes__R02740___HS_k2=2273.32;

	reaction_PGI_astrocytes__R02740___HS=compartment_compartment_3*(reaction_PGI_astrocytes__R02740___HS_k1*x(34)/compartment_compartment_3-reaction_PGI_astrocytes__R02740___HS_k2*x(35)/compartment_compartment_3);

% Reaction: id = HK_neurons__R01786___HeinrichSchuster, name = HK_neurons (R01786) (HeinrichSchuster)	% Local Parameter:   id =  k_HK, name = k_HK
	reaction_HK_neurons__R01786___HeinrichSchuster_k_HK=0.022;
	% Local Parameter:   id =  K_I_G6P, name = K_I_G6P
	reaction_HK_neurons__R01786___HeinrichSchuster_K_I_G6P=0.02;

	reaction_HK_neurons__R01786___HeinrichSchuster=compartment_compartment_2*vHK__HS(reaction_HK_neurons__R01786___HeinrichSchuster_k_HK, x(8)/compartment_compartment_2, x(7)/compartment_compartment_2, reaction_HK_neurons__R01786___HeinrichSchuster_K_I_G6P);

% Reaction: id = PGI_neurons__R02740___HS, name = PGI_neurons (R02740) (HS)	% Local Parameter:   id =  k1, name = k1
	reaction_PGI_neurons__R02740___HS_k1=931.69;
	% Local Parameter:   id =  k2, name = k2
	reaction_PGI_neurons__R02740___HS_k2=2273.32;

	reaction_PGI_neurons__R02740___HS=compartment_compartment_2*(reaction_PGI_neurons__R02740___HS_k1*x(7)/compartment_compartment_2-reaction_PGI_neurons__R02740___HS_k2*x(9)/compartment_compartment_2);

const_species_Na__extracellular_space=30.0;

const_species_O2_artery=0.0458700000000001;

const_species_CO2_artery=0.00660000000000004;

const_species_GLC_artery=0.0264000000000002;

const_species_LAC_artery=0.0017215;



	xdot=zeros(61,1);
	% rateRule: variable = venous_balloon
	xdot(61) = global_par_F_in-global_par_F_out;
	
% Species:   id = species_23, name = O2, affected by kineticLaw
	xdot(1) = (-1.0 * reaction_O2_exchange_capillary_neurons) + (-1.0 * reaction_O2_exchange_capillary_astrocytes) + ( 1.0 * reaction_Blood_flow_contribution_to_capillary_O2);
	
% Species:   id = species_24, name = CO2, affected by kineticLaw
	xdot(2) = ( 3.0 * reaction_reaction_13) + ( 3.0 * reaction_reaction_14) + (-1.0 * reaction_Flow_of_CO2_between_capillary_and_vessel__artery_);
	
% Species:   id = species_25, name = GLC, affected by kineticLaw
	xdot(3) = (-1.0 * reaction_reaction_19) + (-1.0 * reaction_reaction_20) + ( 1.0 * reaction_Blood_flow_contribution_to_capillary_GLC);
	
% Species:   id = species_26, name = LAC, affected by kineticLaw
	xdot(4) = ( 1.0 * reaction_reaction_21) + ( 1.0 * reaction_reaction_24) + (-1.0 * reaction_Blood_flow_contribution_to_capillary_LAC);
	
% Species:   id = dHb, name = dHb, affected by kineticLaw
	xdot(5) = ( 1.0 * reaction_inflow_of_dHb) + (-1.0 * reaction_outflow_of_dHb);
	
% Species:   id = species_1, name = GLC, affected by kineticLaw
	xdot(6) = ( 1.0 * reaction_reaction_17) + (-1.0 * reaction_HK_neurons__R01786___HeinrichSchuster);
	
% Species:   id = species_2, name = G6P, affected by kineticLaw
	xdot(7) = (-1.0 * reaction_ZWF_neurons__R02736) + ( 1.0 * reaction_HK_neurons__R01786___HeinrichSchuster) + (-1.0 * reaction_PGI_neurons__R02740___HS);
	
% Species:   id = species_3, name = ATP, affected by kineticLaw
	xdot(8) = (-1.0 * reaction_reaction_5) + ( 1.0 * reaction_reaction_7) + ( 1.0 * reaction_reaction_9) + ( 15.0 * reaction_reaction_13) + (-1.0 * reaction_vPUMP_neurons) + (-1.0 * reaction_ATPase_neurons) + ( 1.0 * reaction_AK_neurons) + ( 1.0 * reaction_CK_neurons_forward__R01881) + (-1.0 * reaction_HK_neurons__R01786___HeinrichSchuster);
	
% Species:   id = species_7, name = F6P, affected by kineticLaw
	xdot(9) = (-1.0 * reaction_reaction_5) + ( 1.0 * reaction_TAL_neurons__R01827) + (-1.0 * reaction_TKL_2_neurons__R01830) + ( 1.0 * reaction_PGI_neurons__R02740___HS);
	
% Species:   id = species_9, name = GAP, affected by kineticLaw
	xdot(10) = ( 2.0 * reaction_reaction_5) + (-1.0 * reaction_reaction_7) + ( 1.0 * reaction_TKL_1_neurons__R01641) + (-1.0 * reaction_TAL_neurons__R01827) + (-1.0 * reaction_TKL_2_neurons__R01830);
	
% Species:   id = species_11, name = NADH, affected by kineticLaw
	xdot(11) = ( 1.0 * reaction_reaction_7) + (-1.0 * reaction_reaction_13) + (-1.0 * reaction_LDH_neurons_forward__R00703);
	
% Species:   id = species_12, name = PEP, affected by kineticLaw
	xdot(12) = ( 1.0 * reaction_reaction_7) + (-1.0 * reaction_reaction_9);
	
% Species:   id = species_15, name = PYR, affected by kineticLaw
	xdot(13) = ( 1.0 * reaction_reaction_9) + (-1.0 * reaction_reaction_13) + (-1.0 * reaction_LDH_neurons_forward__R00703);
	
% Species:   id = species_18, name = LAC, affected by kineticLaw
	xdot(14) = ( 1.0 * reaction_reaction_22) + ( 1.0 * reaction_LDH_neurons_forward__R00703);
	
% Species:   id = species_16, name = O2, affected by kineticLaw
	xdot(15) = (-3.0 * reaction_reaction_13) + ( 1.0 * reaction_O2_exchange_capillary_neurons);
	
% Species:   id = species_21, name = PCr, affected by kineticLaw
	xdot(16) = (-1.0 * reaction_CK_neurons_forward__R01881);
	
% Species:   id = Na__neurons, name = Na+, affected by kineticLaw
	xdot(17) = (-3.0 * reaction_vPUMP_neurons) + ( 1.0 * reaction_vLEAK_Na_neurons) + ( 1.0 * reaction_vSTIM);
	
% Species:   id = GLU_neurons, name = GLU, affected by kineticLaw
	xdot(18) = (-1.0 * reaction_vGLU_ne) + ( 1.0 * reaction_vGLU_gn);
	
% Species:   id = ADP_neurons, name = ADP, affected by kineticLaw
	xdot(19) = ( 1.0 * reaction_reaction_5) + (-1.0 * reaction_reaction_7) + (-1.0 * reaction_reaction_9) + (-15.0 * reaction_reaction_13) + ( 1.0 * reaction_vPUMP_neurons) + ( 1.0 * reaction_ATPase_neurons) + (-2.0 * reaction_AK_neurons) + (-1.0 * reaction_CK_neurons_forward__R01881) + ( 1.0 * reaction_HK_neurons__R01786___HeinrichSchuster);
	
% Species:   id = AMP_neurons, name = AMP, affected by kineticLaw
	xdot(20) = ( 1.0 * reaction_AK_neurons);
	
% Species:   id = Cr_neurons, name = Cr, affected by kineticLaw
	xdot(21) = ( 1.0 * reaction_CK_neurons_forward__R01881);
	
% Species:   id = NAD_neurons, name = NAD, affected by kineticLaw
	xdot(22) = (-1.0 * reaction_reaction_7) + ( 1.0 * reaction_reaction_13) + ( 1.0 * reaction_LDH_neurons_forward__R00703);
	
% Species:   id = G6L_neurons, name = G6L, affected by kineticLaw
	xdot(23) = ( 1.0 * reaction_ZWF_neurons__R02736) + (-1.0 * reaction_SOL_neurons__R02035);
	
% Species:   id = P6G_neurons, name = P6G, affected by kineticLaw
	xdot(24) = ( 1.0 * reaction_SOL_neurons__R02035) + (-1.0 * reaction_GND_neurons___R01528);
	
% Species:   id = Ru5P_neurons, name = Ru5P, affected by kineticLaw
	xdot(25) = ( 1.0 * reaction_GND_neurons___R01528) + (-1.0 * reaction_RPE_neurons__R01529) + (-1.0 * reaction_RKI_neurons__R01056);
	
% Species:   id = X5P_neurons, name = X5P, affected by kineticLaw
	xdot(26) = ( 1.0 * reaction_RPE_neurons__R01529) + (-1.0 * reaction_TKL_1_neurons__R01641) + ( 1.0 * reaction_TKL_2_neurons__R01830);
	
% Species:   id = R5P_neurons, name = R5P, affected by kineticLaw
	xdot(27) = ( 1.0 * reaction_RKI_neurons__R01056) + (-1.0 * reaction_TKL_1_neurons__R01641) + (-1.0 * reaction_R5P_sink_neurons__n_a_);
	
% Species:   id = S7P_neurons, name = S7P, affected by kineticLaw
	xdot(28) = ( 1.0 * reaction_TKL_1_neurons__R01641) + (-1.0 * reaction_TAL_neurons__R01827);
	
% Species:   id = E4P_neurons, name = E4P, affected by kineticLaw
	xdot(29) = ( 1.0 * reaction_TAL_neurons__R01827) + ( 1.0 * reaction_TKL_2_neurons__R01830);
	
% Species:   id = NADPH_neurons, name = NADPH, affected by kineticLaw
	xdot(30) = ( 1.0 * reaction_ZWF_neurons__R02736) + ( 1.0 * reaction_GND_neurons___R01528) + (-1.0 * reaction_NADPH_oxidase_neurons__R07172);
	
% Species:   id = NADP_neurons, name = NADP, affected by kineticLaw
	xdot(31) = (-1.0 * reaction_ZWF_neurons__R02736) + (-1.0 * reaction_GND_neurons___R01528) + ( 1.0 * reaction_NADPH_oxidase_neurons__R07172);
	
% Species:   id = species_4, name = GLC, affected by kineticLaw
	xdot(32) = (-1.0 * reaction_reaction_2) + ( 1.0 * reaction_reaction_18) + ( 1.0 * reaction_reaction_20);
	
% Species:   id = species_5, name = ATP, affected by kineticLaw
	xdot(33) = (-1.0 * reaction_reaction_2) + (-1.0 * reaction_reaction_6) + ( 1.0 * reaction_reaction_8) + ( 1.0 * reaction_reaction_10) + ( 15.0 * reaction_reaction_14) + (-1.0 * reaction_vPUMP_astrocytes) + (-1.0 * reaction_vGLU_gn) + (-1.0 * reaction_ATPase_astrocytes) + ( 1.0 * reaction_AK_astrocytes) + ( 1.0 * reaction_CK_astrocytes_forward__R01881);
	
% Species:   id = species_6, name = G6P, affected by kineticLaw
	xdot(34) = ( 1.0 * reaction_reaction_2) + (-1.0 * reaction_ZWF_astrocytes__R02736) + (-1.0 * reaction_PGI_astrocytes__R02740___HS);
	
% Species:   id = species_8, name = F6P, affected by kineticLaw
	xdot(35) = (-1.0 * reaction_reaction_6) + ( 1.0 * reaction_TAL_astrocytes__R01827) + (-1.0 * reaction_TKL_2_astrocytes__R01830) + ( 1.0 * reaction_PGI_astrocytes__R02740___HS);
	
% Species:   id = species_10, name = GAP, affected by kineticLaw
	xdot(36) = ( 2.0 * reaction_reaction_6) + (-1.0 * reaction_reaction_8) + ( 1.0 * reaction_TKL_1_astrocytes__R01641) + (-1.0 * reaction_TAL_astrocytes__R01827) + (-1.0 * reaction_TKL_2_astrocytes__R01830);
	
% Species:   id = species_13, name = NADH, affected by kineticLaw
	xdot(37) = ( 1.0 * reaction_reaction_8) + (-1.0 * reaction_reaction_14) + (-1.0 * reaction_LDH_astrocytes_forward__R00703);
	
% Species:   id = species_14, name = PEP, affected by kineticLaw
	xdot(38) = ( 1.0 * reaction_reaction_8) + (-1.0 * reaction_reaction_10);
	
% Species:   id = species_17, name = PYR, affected by kineticLaw
	xdot(39) = ( 1.0 * reaction_reaction_10) + (-1.0 * reaction_reaction_14) + (-1.0 * reaction_LDH_astrocytes_forward__R00703);
	
% Species:   id = species_19, name = LAC, affected by kineticLaw
	xdot(40) = (-1.0 * reaction_reaction_23) + (-1.0 * reaction_reaction_24) + ( 1.0 * reaction_LDH_astrocytes_forward__R00703);
	
% Species:   id = species_20, name = O2, affected by kineticLaw
	xdot(41) = (-3.0 * reaction_reaction_14) + ( 1.0 * reaction_O2_exchange_capillary_astrocytes);
	
% Species:   id = species_22, name = PCr, affected by kineticLaw
	xdot(42) = (-1.0 * reaction_CK_astrocytes_forward__R01881);
	
% Species:   id = Na__astrocytes, name = Na+, affected by kineticLaw
	xdot(43) = (-3.0 * reaction_vPUMP_astrocytes) + ( 1.0 * reaction_vLEAK_Na_astrocytes) + ( 1.0 * reaction_vGLU_eg);
	
% Species:   id = GLU_astrocytes, name = GLU, affected by kineticLaw
	xdot(44) = ( 1.0 * reaction_vGLU_eg) + (-1.0 * reaction_vGLU_gn);
	
% Species:   id = ADP_astrocytes, name = ADP, affected by kineticLaw
	xdot(45) = ( 1.0 * reaction_reaction_2) + ( 1.0 * reaction_reaction_6) + (-1.0 * reaction_reaction_8) + (-1.0 * reaction_reaction_10) + (-15.0 * reaction_reaction_14) + ( 1.0 * reaction_vPUMP_astrocytes) + ( 1.0 * reaction_vGLU_gn) + ( 1.0 * reaction_ATPase_astrocytes) + (-2.0 * reaction_AK_astrocytes) + (-1.0 * reaction_CK_astrocytes_forward__R01881);
	
% Species:   id = AMP_astrocytes, name = AMP, affected by kineticLaw
	xdot(46) = ( 1.0 * reaction_AK_astrocytes);
	
% Species:   id = Cr_astrocytes, name = Cr, affected by kineticLaw
	xdot(47) = ( 1.0 * reaction_CK_astrocytes_forward__R01881);
	
% Species:   id = NAD_astrocytes, name = NAD, affected by kineticLaw
	xdot(48) = (-1.0 * reaction_reaction_8) + ( 1.0 * reaction_reaction_14) + ( 1.0 * reaction_LDH_astrocytes_forward__R00703);
	
% Species:   id = G6L_astrocytes, name = G6L, affected by kineticLaw
	xdot(49) = ( 1.0 * reaction_ZWF_astrocytes__R02736) + (-1.0 * reaction_SOL_astrocytes__R02035);
	
% Species:   id = P6G_astrocytes, name = P6G, affected by kineticLaw
	xdot(50) = ( 1.0 * reaction_SOL_astrocytes__R02035) + (-1.0 * reaction_GND_astrocytes__R01528);
	
% Species:   id = Ru5P_astrocytes, name = Ru5P, affected by kineticLaw
	xdot(51) = ( 1.0 * reaction_GND_astrocytes__R01528) + (-1.0 * reaction_RPE_astrocytes__R01529) + (-1.0 * reaction_RKI_astrocytes__R01056);
	
% Species:   id = X5P_astrocytes, name = X5P, affected by kineticLaw
	xdot(52) = ( 1.0 * reaction_RPE_astrocytes__R01529) + (-1.0 * reaction_TKL_1_astrocytes__R01641) + ( 1.0 * reaction_TKL_2_astrocytes__R01830);
	
% Species:   id = R5P_astrocytes, name = R5P, affected by kineticLaw
	xdot(53) = ( 1.0 * reaction_RKI_astrocytes__R01056) + (-1.0 * reaction_TKL_1_astrocytes__R01641) + (-1.0 * reaction_R5P_sink_astrocytes__n_a_);
	
% Species:   id = S7P_astrocytes, name = S7P, affected by kineticLaw
	xdot(54) = ( 1.0 * reaction_TKL_1_astrocytes__R01641) + (-1.0 * reaction_TAL_astrocytes__R01827);
	
% Species:   id = E4P_astrocytes, name = E4P, affected by kineticLaw
	xdot(55) = ( 1.0 * reaction_TAL_astrocytes__R01827) + ( 1.0 * reaction_TKL_2_astrocytes__R01830);
	
% Species:   id = NADP_astrocytes, name = NADP, affected by kineticLaw
	xdot(56) = (-1.0 * reaction_ZWF_astrocytes__R02736) + (-1.0 * reaction_GND_astrocytes__R01528) + ( 1.0 * reaction_NADPH_oxidase_astrocytes__R07172);
	
% Species:   id = NADPH_astrocytes, name = NADPH, affected by kineticLaw
	xdot(57) = ( 1.0 * reaction_ZWF_astrocytes__R02736) + ( 1.0 * reaction_GND_astrocytes__R01528) + (-1.0 * reaction_NADPH_oxidase_astrocytes__R07172);
	
% Species:   id = species_27, name = GLC, affected by kineticLaw
	xdot(58) = (-1.0 * reaction_reaction_17) + (-1.0 * reaction_reaction_18) + ( 1.0 * reaction_reaction_19);
	
% Species:   id = species_28, name = LAC, affected by kineticLaw
	xdot(59) = (-1.0 * reaction_reaction_21) + (-1.0 * reaction_reaction_22) + ( 1.0 * reaction_reaction_23);
	
% Species:   id = GLU_extracellular_space, name = GLU, affected by kineticLaw
	xdot(60) = ( 1.0 * reaction_vGLU_ne) + (-1.0 * reaction_vGLU_eg);
end

function z=vdHb_in(F_in,O2_a,O2_c), z=(F_in*(O2_a-(2*O2_c-O2_a)));end

function z=vdHb_out(F_out,dHb,V_v), z=(F_out*dHb/V_v);end

function z=vATPase(VmaxATPase,ATP,Km_ATP), z=(VmaxATPase*ATP/(ATP+Km_ATP));end

function z=vPK(k_PK,PEP,ADP), z=(k_PK*PEP*ADP);end

function z=vPGK(k_PGK,GAP,ADP,NAD,NADH), z=(k_PGK*GAP*ADP*NAD/NADH);end

function z=vPFK(k_PFK,ATP,K_I_ATP,nH,F6P,K_m_F6P), z=(k_PFK*ATP*(1+(ATP/K_I_ATP)^nH)^(-1)*F6P/(F6P+K_m_F6P));end

function z=facilitated_transport__inkl__Volume(Vmax,S,K,P,Volume), z=(Vmax*(S/(S+K)-P/(P+K))*Volume);end

function z=vGLU_eg__inkl__Volumes(Vmax_GLU,GLU_e,K_m_GLU,Volume), z=(Vmax_GLU*GLU_e/(GLU_e+K_m_GLU)*Volume);end

function z=vGLU_gn__inkl__Volume(Vmax_GLU,GLU_g,K_m_GLU,ATP_g,K_m_ATP,Volume), z=(Vmax_GLU*GLU_g/(GLU_g+K_m_GLU)*ATP_g/(ATP_g+K_m_ATP)*Volume);end

function z=vGLU_ne__inkl__Volume(vSTIM,ratio_Na_GLU,GLU_n,Km_GLU,Volume), z=(vSTIM*ratio_Na_GLU*GLU_n/(GLU_n+Km_GLU)*Volume);end

function z=vStim__with_volume(vstim,Volume), z=(vstim*Volume);end

function z=modular_rate_law_for_two_substrates__two_products(Vmax,K_S1,K_S2,S1,S2,P1,P2,Keq,K_P1,K_P2), z=(Vmax*1/(K_S1*K_S2)*(S1*S2-P1*P2/Keq)/(((1+S1/K_S1)*(1+S2/K_S2)+(1+P1/K_P1)*(1+P2/K_P2))-1));end

function z=modular_rate_law_for_one_substrate__one_product(Vmax,K_S1,S1,P1,Keq,K_P1), z=(Vmax*1/K_S1*(S1-P1/Keq)/((1+S1/K_S1+1+P1/K_P1)-1));end

function z=Blood_flow_contribution_inkl__volume(F_in,V_c,Volume,Substrate,Product), z=(2*F_in/V_c*(Substrate-Product)*Volume);end

function z=O2_transport_function_inkl__volume(PScap,Volume,KO2,HbOP,O2_source,nh,O2_destination,Volume1), z=(PScap/Volume*(KO2*(HbOP/O2_source-1)^((-1)/nh)-O2_destination)*Volume1);end

function z=vLEAK_Na_inkl__Volume(Sm,gNA,Volume,F,RT,Na_e,Na,Vm,Volume1), z=(Sm*gNA/(Volume*F)*(RT/F*log(Na_e/Na)-Vm)*Volume1);end

function z=vPUMP_volume_dependent(Sm,Volume,k_pump,ATP,Na,Km_pump), z=(Sm/Volume*k_pump*ATP*Na*(1+ATP/Km_pump)^(-1));end

function z=vHK__HS(k_HK,ATP,G6P,K_I_G6P), z=(k_HK*ATP*(1+G6P/K_I_G6P)^(-1));end

function z=facilitated_transport__inkl__Volume____scaled(Vmax,sf,S,K,P,Volume), z=(Vmax*sf*(S/(S+K)-P/(P+K))*Volume);end

function z=vMITO2__inkl__Volumes(v_max_mito,PYR,K_m_PYR,ADP,K_m_ADP,O2,K_m_O2,alpha,ATP,beta,Volume), z=(v_max_mito*PYR/(PYR+K_m_PYR)*ADP/(ADP+K_m_ADP)*O2/(O2+K_m_O2)*(1-1/(1+exp((-alpha)*(ATP/ADP-beta))))*Volume);end

% adding few functions representing operators used in SBML but not present directly 
% in either matlab or octave. 
function z=pow(x,y),z=x^y;end
function z=root(x,y),z=y^(1/x);end
function z = piecewise(then_val, condition, else_val)
	if(condition)
		z = then_val;
	else
		z = else_val;
	end
end



