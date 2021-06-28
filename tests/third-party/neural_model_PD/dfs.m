function dy = dfs(t,y,par)

dy = zeros(26,1);

%% Global variables
global count cout Spindle1 Spindle2

%% Parameters
g0 = par(1,1);
k = par(2,1);
eta = par(3,1);
Im = par(4,1);
lambda = par(5,1);
T1 = par(6,1);
T2 = par(7,1);
G1 = par(8,1);
G2 = par(9,1);
Gv = par(10,1);
Gs = par(11,1);
Gf = par(12,1);
beta = par(13,1);
gamma = par(14,1);
delta = par(15,1);
phi = par(16,1);
chi = par(17,1);
rho = par(18,1);
omega = par(19,1);
tau = par(20,1);
Fe = par(21,1);
DA1 = par(22,1);
DA2 = par(23,1);
DA3 = par(24,1);
DA4 = par(25,1);
DA5 = par(26,1);
DA6 = par(27,1);
DA7 = par(28,1);
DA8 = par(29,1);
ae = par(30,1);
tau2 = par(31,1);
Bu = par(32,1);
Bp = par(33,1);
DA9 = par(34,1);
DA10 = par(35,1);
DA11 = par(36,1);

%% States
V1 = y(1);  % DV1
V2 = y(2);  % DV2
A1 = y(3);  % PPV1
A2 = y(4);  % PPV2
C1 = y(5);  % contractile 1  
C2 = y(6);  % contractile 2
theta = y(7);   % position
dtheta = y(8);  % velocity
R1 = y(9);     % Renshaw 1
R2 = y(10);     % Renshaw 2
M1 = y(11); % alpha-MN1
M2 = y(12); % alpha-MN2
S1 = y(13); % gamma-static MN 1
S2 = y(14); % gamma-static MN 2
U1 = y(15); % Intrafusal static gamma muscle contraction 1
U2 = y(16); % Intrafusal static gamma muscle contraction 2
D1 = y(17); % gamma-dynamic MN 1
D2 = y(18); % gamma-dynamic MN 2
N1 = y(19); % Intrafusal dynamic gamma muscle contraction 1
N2 = y(20); % Intrafusal dynamic gamma muscle contraction 2
IaIN1 = y(21);  % IaIN 1
IaIN2 = y(22);  % IaIN 2
W1 = y(23);     % Spindle response 1
W2 = y(24);     % Spindle response 2
IbIN1 = y(25);   % IbIN 1
IbIN2 = y(26);  % IbIN 2

%% GPi output signal
if (t < tau)
   GO = 0;
else
   GO = g0 * (((t - tau) ^ 2) / (gamma * ((t - tau) ^ 2) + beta));     %% u = 1 at t > 10
end

%%%% VITE equations %%%%
%% Stretch feedback
E1 = DA11 * ae * W1;
E2 = DA11 * ae * W2;

%% Difference Vector (DV) w spindle feedback w/o delays
dy(1) = 30 * (-V1 + T1 - DA1 * A1 + DA1 * 0.061 * (W1 - W2));        % DV1
dy(2) = 30 * (-V2 + T2 - DA1 * A2 + DA1 * 0.061 * (W2 - W1));        % DV2

%% Difference Vector (DV) w spindle feedback w delays
% count = count + 1;
% Spindle1(count) = W1;
% Spindle2(count) = W2;
% if (t >= tau2 & t <= tau2 + 0.0007)
% %     disp('Ok')
%    cout = count-1;
% end

% if (t < tau2)
%   dy(1) = 30 * (-V1 + T1 - DA1 * A1);        % DV1
%   dy(2) = 30 * (-V2 + T2 - DA1 * A2);        % DV2
% elseif (t >= tau2)  
%     dy(1) = 30 * (-V1 + T1 - DA1 * A1 + ...
%          10*(Spindle1(count-cout) - Spindle2(count-cout)));        % DV1
%     dy(2) = 30 * (-V2 + T2 - DA1 * A2 + ...
%         10*(Spindle2(count-cout) - Spindle1(count-cout)));        % DV2
% end

%% Difference Vector (DV) w/o spindle feedback
% dy(1) = 30 * (-V1 + T1 - DA1 * A1);        % DV1
% dy(2) = 30 * (-V2 + T2 - DA1 * A2);        % DV2

%% Desired Velocity Vector (DVV)
DVV1 = max((GO * (DA2 * V1 - DA3 * V2) + Bu / DA4),0);
DVV2 = max((GO * (DA3 * V2 - DA2 * V1) + Bu / DA4),0);

%% Present Position Vector (PPV)
dy(3) = (1 - A1) * (DVV1 - DVV2) - A1 * (DVV2 - DVV1); % PPV1 (A1) 
dy(4) = (1 - A2) * (DVV2 - DVV1) - A2 * (DVV1 - DVV2); % PPV2 (A2) 

%% Co-contraction vector
P = max((GO * (DA2 * V1 - DA3 * V2) + Bp / DA4),0);

%%%% FLETE equations %%%%
%% Origin-to-insertion point
L1 = sqrt((cos(theta)) ^ 2 + (20 - sin(theta)) ^ 2);
L2 = sqrt((cos(theta)) ^ 2 + (20 + sin(theta)) ^ 2);
dL1 = -20 * dtheta / sqrt(1 + ((20 - sin(theta)) / cos(theta)) ^ 2);
dL2 = 20 * dtheta / sqrt(1 + ((20 + sin(theta)) / cos(theta)) ^ 2);

%% Muscle force
F1 = k * (max(L1 - G1 + C1,0)) ^ 2;
F2 = k * (max(L2 - G2 + C2,0)) ^ 2;

%% Contraction rate
beta1 = 0.05 + 0.01 * (A1 + P + E1); 
beta2 = 0.05 + 0.01 * (A2 + P + E2); 

%% Number of contractile fibers
B1 = .2 + 2 * (A1 + P + E1); 
B2 = .2 + 2 * (A2 + P + E2); 

%% Contractile state of muscle
dy(5) = beta1 * ((B1 - C1) * max(M1,0) - delta * C1) - max(F1 - Gf,0);  
dy(6) = beta2 * ((B2 - C2) * max(M2,0) - delta * C2) - max(F2 - Gf,0);  

%% Limb dynamics 1
dy(7) = dtheta;   % d(theta)
dy(8) = (F1 - F2 + Fe - eta * dtheta) / Im; % theta

%% Renshaw recruitment rate
z1 = .02 * (1 + max(M1,0)); 
z2 = .02 * (1 + max(M2,0)); 

%% Renshaw cell activity
dy(9) = phi * (lambda * B1 - R1) * DA5 * z1 * max(M1,0) - R1 * DA6 * (1.5 + max(R2,0));
dy(10) = phi * (lambda * B2 - R2) * DA6 * z2 * max(M2,0) - R2 * DA5 * (1.5 + max(R1,0));

%% Alpha-MN activity
dy(11) = phi * ((lambda * B1 - M1) * DA7 * (A1 + P + chi * E1) - ...
    (M1 + 2) * DA8 * (1 + omega * max(R1,0) + rho * max(IbIN1,0) + max(IaIN2,0))); 
dy(12) = phi * DA8 * ((lambda * B2 - M2) * (A2 + P + chi * E2) - ...
    (M2 + 2) * DA7 * (1 + omega * max(R2,0) + rho * max(IbIN2,0) + max(IaIN1,0))); 

%% Gamma static MN activity
dy(13) = phi * (10 - S1) * (A1 + P) - S1 * (1.8 + 0.2 * (max(R1,0) / (0.3 + max(R1,0)))); 
dy(14) = phi * (10 - S2) * (A2 + P) - S2 * (1.8 + 0.2 * (max(R2,0) / (0.3 + max(R2,0)))); 

%% Intrafusal static activity 
dy(15) = 4 * S1 - U1;  % U1
dy(16) = 4 * S2 - U2;  % U2

%% Gamma dynamic MN activity
dy(17) = (8 - D1) * (100 * GO * max(V1,0) + P) - ...
    (1.2 + D1) * (1 + 100 * GO * max(V2,0) + 0.5 * (max(R1,0) / (0.3 + max(R1,0)))); 
dy(18) = (8 - D2) * (100 * GO * max(V2,0) + P) - ...
   (1.2 + D2) * (1 + 100 * GO * max(V1,0) + 0.5 * (max(R2,0) / (0.3 + max(R2,0)))); 

%% Intrafusal dynamic activity
dy(19) = 4 * D1 - N1;  % N1
dy(20) = 4 * D2 - N2;  % N2

%% Inhibitory interneuron type Ia
dy(21) = phi * (15 - IaIN1) * DA9 * (A1 + chi * E1 + P) - ... 
    IaIN1 * DA10 * (1 + omega * max(R1,0) + max(IaIN2,0)); % IaIN1
dy(22) = phi * DA10 * (15 - IaIN2) * (A2 + chi * E2 + P) - ...
    IaIN2 * DA9 * (1 + omega * max(R2,0) + max(IaIN1,0)); % IaIN2

%% Spindle response
dy(23) = (2 - W1) * (Gs * max(U1 + L1 - G1,0) + Gv * (max((N1 + dL1),0) ^ 0.3)) - 10 * W1;
dy(24) = (2 - W2) * (Gs * max(U2 + L2 - G2,0) + Gv * (max((N2 + dL2),0) ^ 0.3)) - 10 * W2;

%% Inhibitory interneuron type Ib
dy(25) = phi * DA11 * ((15 - IbIN1) * F1) - IbIN1 * DA11 * (0.8 + 2.2 * max(IbIN2,0));
dy(26) = phi * DA11 * ((15 - IbIN2) * F2) - IbIN2 * DA11 * (0.8 + 2.2 * max(IbIN1,0));





