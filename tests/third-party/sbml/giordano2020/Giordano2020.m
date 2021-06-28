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
% Model name = Giordano2020 - SIDARTHE model of COVID-19 spread in Italy
%
% isDescribedBy http://identifiers.org/pubmed/32322102
% is http://identifiers.org/biomodels.db/MODEL2007280002
% is http://identifiers.org/biomodels.db/BIOMD0000000955
%

function Giordano2020()
%Initial conditions vector
	tic;

	x0=zeros(8,1);
	x0(1) = 0.9999963;
	x0(2) = 3.33333333E-6;
	x0(3) = 3.33333333E-7;
	x0(4) = 1.66666666E-8;
	x0(5) = 3.33333333E-8;
	x0(6) = 0.0;
	x0(7) = 0.0;
	x0(8) = 0.0;


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

	%csvwrite('results.csv', [t,x]);
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

% Compartment: id = Italy, name = Italy, constant
	compartment_Italy=1.0;
% Parameter:   id =  alpha, name = alpha
	global_par_alpha=0.57;
% Parameter:   id =  beta, name = beta
	global_par_beta=0.011;
% Parameter:   id =  gamma, name = gamma
	global_par_gamma=0.456;
% Parameter:   id =  delta, name = delta
	global_par_delta=0.011;
% Parameter:   id =  epsilon, name = epsilon
	global_par_epsilon=0.171;
% Parameter:   id =  theta, name = theta
	global_par_theta=0.371;
% Parameter:   id =  zeta, name = zeta
	global_par_zeta=0.125;
% Parameter:   id =  eta, name = eta
	global_par_eta=0.125;
% Parameter:   id =  mu, name = mu
	global_par_mu=0.017;
% Parameter:   id =  nu, name = nu
	global_par_nu=0.027;
% Parameter:   id =  tau, name = tau
	global_par_tau=0.01;
% Parameter:   id =  lambda, name = lambda
	global_par_lambda=0.034;
% Parameter:   id =  kappa, name = kappa
	global_par_kappa=0.017;
% Parameter:   id =  rho, name = rho
	global_par_rho=0.034;
% Parameter:   id =  sigma, name = sigma
	global_par_sigma=0.017;
% Parameter:   id =  xi, name = xi
	global_par_xi=0.017;
% Parameter:   id =  Event_trigger_Fig3b, name = Event_trigger_Fig3b
	global_par_Event_trigger_Fig3b=0.0;
% Parameter:   id =  Event_trigger_Fig3d, name = Event_trigger_Fig3d
	global_par_Event_trigger_Fig3d=0.0;
% Parameter:   id =  Event_trigger_Fig4b, name = Event_trigger_Fig4b
	global_par_Event_trigger_Fig4b=0.0;
% Parameter:   id =  Event_trigger_Fig4d, name = Event_trigger_Fig4d
	global_par_Event_trigger_Fig4d=0.0;
% Parameter:   id =  epsilon_modifier, name = epsilon_modifier
	global_par_epsilon_modifier=1.0;
% Parameter:   id =  alpha_modifier, name = alpha_modifier
	global_par_alpha_modifier=1.0;
% Parameter:   id =  ModelValue_16, name = Initial for Event_trigger_Fig3b
	global_par_ModelValue_16=0.0;
% Parameter:   id =  ModelValue_17, name = Initial for Event_trigger_Fig3d
	global_par_ModelValue_17=0.0;
% Parameter:   id =  ModelValue_18, name = Initial for Event_trigger_Fig4b
	global_par_ModelValue_18=0.0;
% Parameter:   id =  ModelValue_19, name = Initial for Event_trigger_Fig4d
	global_par_ModelValue_19=0.0;
% Parameter:   id =  ModelValue_21, name = Initial for alpha_modifier
	global_par_ModelValue_21=1.0;
% Parameter:   id =  ModelValue_20, name = Initial for epsilon_modifier
	global_par_ModelValue_20=1.0;

% Reaction: id = Susceptible_to_Infected, name = Susceptible_to_Infected
	reaction_Susceptible_to_Infected=compartment_Italy*Rate_Law_for_Susceptible_to_Infected(x(1), global_par_alpha, x(2), global_par_beta, x(3), global_par_gamma, x(4), global_par_delta, x(5));

% Reaction: id = Infected_to_Diagnosed, name = Infected_to_Diagnosed
	reaction_Infected_to_Diagnosed=compartment_Italy*global_par_epsilon*x(2);

% Reaction: id = Infected_to_Ailing, name = Infected_to_Ailing
	reaction_Infected_to_Ailing=compartment_Italy*global_par_zeta*x(2);

% Reaction: id = Infected_to_Healed, name = Infected_to_Healed
	reaction_Infected_to_Healed=compartment_Italy*global_par_lambda*x(2);

% Reaction: id = Diagnosed_to_Recognized, name = Diagnosed_to_Recognized
	reaction_Diagnosed_to_Recognized=compartment_Italy*global_par_eta*x(3);

% Reaction: id = Diagnosed_to_Healed, name = Diagnosed_to_Healed
	reaction_Diagnosed_to_Healed=compartment_Italy*global_par_rho*x(3);

% Reaction: id = Ailing_to_Recognised, name = Ailing_to_Recognised
	reaction_Ailing_to_Recognised=compartment_Italy*global_par_theta*x(4);

% Reaction: id = Ailing_to_Healed, name = Ailing_to_Healed
	reaction_Ailing_to_Healed=compartment_Italy*global_par_kappa*x(4);

% Reaction: id = Ailing_to_Threatened, name = Ailing_to_Threatened
	reaction_Ailing_to_Threatened=compartment_Italy*global_par_mu*x(4);

% Reaction: id = Recognised_to_Threatened, name = Recognised_to_Threatened
	reaction_Recognised_to_Threatened=compartment_Italy*global_par_nu*x(5);

% Reaction: id = Recognised_to_Healed, name = Recognised_to_Healed
	reaction_Recognised_to_Healed=compartment_Italy*global_par_xi*x(5);

% Reaction: id = Threatened_to_Extinct, name = Threatened_to_Extinct
	reaction_Threatened_to_Extinct=compartment_Italy*global_par_tau*x(6);

% Reaction: id = Threatened_to_Healed, name = Threatened_to_Healed
	reaction_Threatened_to_Healed=compartment_Italy*global_par_sigma*x(6);

%Event: id=Day_4
	event_Day_4=time > 4;

	if(event_Day_4) 
		global_par_alpha=0.422;
		global_par_beta=0.0057;
		global_par_gamma=0.285;
		global_par_delta=0.0057;
	end

%Event: id=Day_12
	event_Day_12=time > 12;

	if(event_Day_12) 
		global_par_epsilon=0.143;
	end

%Event: id=Day_22
	event_Day_22=time > 22;

	if(event_Day_22) 
		global_par_alpha=0.36;
		global_par_beta=0.005;
		global_par_gamma=0.2;
		global_par_delta=0.005;
		global_par_mu=0.008;
		global_par_nu=0.015;
		global_par_lambda=0.08;
		global_par_rho=0.017;
		global_par_kappa=0.017;
		global_par_xi=0.017;
		global_par_sigma=0.017;
		global_par_zeta=0.034;
		global_par_eta=0.034;
	end

%Event: id=Day_28
	event_Day_28=time > 28;

	if(event_Day_28) 
		global_par_alpha=0.21;
		global_par_gamma=0.11;
	end

%Event: id=Day_38
	event_Day_38=time > 38;

	if(event_Day_38) 
		global_par_rho=0.02;
		global_par_xi=0.02;
		global_par_sigma=0.01;
		global_par_zeta=0.025;
		global_par_epsilon=0.2;
		global_par_eta=0.025;
		global_par_kappa=0.02;
	end

%Event: id=Day_50
	event_Day_50=time > 50;

	if(event_Day_50) 
		global_par_alpha=0.209*global_par_ModelValue_21+0.252*global_par_ModelValue_16+0.105*global_par_ModelValue_17+0.42*global_par_ModelValue_19;
		global_par_epsilon=0.2*global_par_ModelValue_20+0.4*global_par_ModelValue_18+0.6*global_par_ModelValue_19;
	end



	xdot=zeros(8,1);
	
% Species:   id = Susceptible, name = Susceptible, affected by kineticLaw
	xdot(1) = (1/(compartment_Italy))*((-1.0 * reaction_Susceptible_to_Infected));
	
% Species:   id = Infected, name = Infected, affected by kineticLaw
	xdot(2) = (1/(compartment_Italy))*(( 1.0 * reaction_Susceptible_to_Infected) + (-1.0 * reaction_Infected_to_Diagnosed) + (-1.0 * reaction_Infected_to_Ailing) + (-1.0 * reaction_Infected_to_Healed));
	
% Species:   id = Diagnosed, name = Diagnosed, affected by kineticLaw
	xdot(3) = (1/(compartment_Italy))*(( 1.0 * reaction_Infected_to_Diagnosed) + (-1.0 * reaction_Diagnosed_to_Recognized) + (-1.0 * reaction_Diagnosed_to_Healed));
	
% Species:   id = Ailing, name = Ailing, affected by kineticLaw
	xdot(4) = (1/(compartment_Italy))*(( 1.0 * reaction_Infected_to_Ailing) + (-1.0 * reaction_Ailing_to_Recognised) + (-1.0 * reaction_Ailing_to_Healed) + (-1.0 * reaction_Ailing_to_Threatened));
	
% Species:   id = Recognized, name = Recognized, affected by kineticLaw
	xdot(5) = (1/(compartment_Italy))*(( 1.0 * reaction_Diagnosed_to_Recognized) + ( 1.0 * reaction_Ailing_to_Recognised) + (-1.0 * reaction_Recognised_to_Threatened) + (-1.0 * reaction_Recognised_to_Healed));
	
% Species:   id = Threatened, name = Threatened, affected by kineticLaw
	xdot(6) = (1/(compartment_Italy))*(( 1.0 * reaction_Ailing_to_Threatened) + ( 1.0 * reaction_Recognised_to_Threatened) + (-1.0 * reaction_Threatened_to_Extinct) + (-1.0 * reaction_Threatened_to_Healed));
	
% Species:   id = Healed, name = Healed, affected by kineticLaw
	xdot(7) = (1/(compartment_Italy))*(( 1.0 * reaction_Infected_to_Healed) + ( 1.0 * reaction_Diagnosed_to_Healed) + ( 1.0 * reaction_Ailing_to_Healed) + ( 1.0 * reaction_Recognised_to_Healed) + ( 1.0 * reaction_Threatened_to_Healed));
	
% Species:   id = Extinct, name = Extinct, affected by kineticLaw
	xdot(8) = (1/(compartment_Italy))*(( 1.0 * reaction_Threatened_to_Extinct));
end


function z=Rate_Law_for_Susceptible_to_Infected(S,alpha,I,beta,D,gamma,A,delta,R), z=(S*(alpha*I+beta*D+gamma*A+delta*R));end

% adding few functions representing operators used in SBML but not present directly 
% in either matlab or octave. 
function z=pow(x,y),z=x^y;end
function z=root(x,y),z=y^(1/x);end



