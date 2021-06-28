function dudt=AmariTCimpBS(t,u)
%
    %connectivity parameters
    w1 =1.8;%PY  -> PY
    w2 = 4;   %PY -> I
    w3 = 1.5; % I -| PY
    % w4= 0;
    w5 = 10.5; %  TC -> RTN
    w6 = 0.6; % RTN -| TC
    w7 = 3; % PY -> TC
    w8 = 3; %   PY -> RTN
    w9 = 1; %  TC -> PY
    w10= 0.2;% RTN -| RTN

   h_p = -.35; %   
   h_i = -3.4; % 
   h_t = -2; %    -2 for bistable for ode45; -2.2 for excitable for ode45.
   h_r = -5; %   
   s=2.8;
    a=1;
   % Time scale parameters
   tau1=1*26;    %
   tau2=1.25*26;    %
   tau3=.1*a*26;    %
   tau4=.1*a*26;    %


   
   sig_py = (1./(1+250000.^(-u(1))));
   sig_in = (1./(1+250000.^(-u(2))));
   sig_tc = (1./(1+250000.^(-u(3))));
   sig_re = (1./(1+250000.^(-u(4))));
   
   dudt=zeros(4,1);

   dudt(1) = (+h_p -u(1) +w1*sig_py -w3*sig_in + w9*sig_tc )*tau1;
   dudt(2) = (+h_i -u(2) +w2*sig_py                            )*tau2;
   dudt(3) = (+h_t -u(3)  +w7.*sig_py - w6*(s*u(4)+.5)       )*tau3;
   dudt(4)= (+h_r -u(4) +w8.*sig_py + w5*(s*u(3)+.5) -w10*(s*u(4)+.5))*tau4;



end
