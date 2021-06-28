function ym = algebraic(t,y,par)

%% Parameters
g0 = par(1,1);
k = par(2,1);
Gi = par(8,1);
beta = par(13,1);
gamma = par(14,1);
tau = par(20,1);
DA2 = par(23,1);
DA3 = par(24,1);
DA4 = par(25,1);
Bu = par(32,1);
Bp = par(33,1);
ae = par(30,1);
DA11 = par(36,1);

%% Single GO signal
GO = zeros(length(t), 1);

for i = 1:length(t)
    if (t(i) < tau)
        GO(i) = 0;     
    else
        GO(i) = g0 .* (((t(i) - tau) .^ 2) ./ (gamma * ((t(i) - tau) .^ 2) + beta));     %% u = 1 at t =0
   end
end

yt = y';
y23 = yt(23,:)';
% y23_1 = yt(:,23);
y24 = yt(24,:)';
y7 = yt(7,:)';
y6 = yt(6,:)';
y5 = yt(5,:)';
y4 = yt(4,:)';
y3 = yt(3,:)';
y2 = yt(2,:)';
y1 = yt(1,:)';

%% Stretch feedback
E1 = DA11 * ae * y23;
E2 = DA11 * ae * y24;

%% Muscle lengths
L1 = sqrt((cos(y7)) .^ 2 + (20 - sin(y7)) .^ 2);
L2 = sqrt((cos(y7)) .^ 2 + (20 + sin(y7)) .^ 2);

%% Muscle forces (torques)
F1 = k * (max(L1 - Gi + y5,0)) .^ 2;
F2 = k * (max(L2 - Gi + y6,0)) .^ 2;

%% Co-contractive signal
P = max((GO .* (DA2 * y1 - DA3 * y2) + Bp / DA4),0);

%% Contraction rate
beta1 = 0.05 + 0.01 * (y3 + P + E1); % (0.5, 5)
beta2 = 0.05 + 0.01 * (y4 + P + E2); % (0.5, 5)

%% Number of contractile fibers
B1 = .2 + 2 * (y3 + P + E1); % (0.2, 2)
B2 = .2 + 2 * (y4 + P + E2); % (0.2, 2)

%% Desired velocity vector (DVV)
DVV1 = max((GO .* (DA2 * y1 - DA3 * y2) + Bu / DA4),0);
DVV2 = max((GO .* (DA3 * y2 - DA2 * y1) + Bu / DA4),0);

%% Renshaw recruitment rate
z1 = .02 * (1 + max(y3,0)); % (0.2, 1)
z2 = .02 * (1 + max(y4,0)); % (0.2, 1)


ym = [GO L1 L2 F1 F2 beta1 beta2 B1 B2 DVV1 DVV2 P z1 z2];
%    1  2  3  4  5  6      7    8  9  10    11 12 13 14                                            





