tic
K = [1,2,3.1;4,5,6;7,8,9];
disp('Last row')
disp(K(end,:))
% 7 8 9
disp('Last element')
disp(K(end,end))
% 6
disp('Last two column')
disp(K(:,2:3))
% 2 4 6
disp('Whole matrix')
disp(K(:,:))
% all K
disp('First two elements of second column')
disp(K(1:2,2))
% 2 4


MW_ASM                      = 78000; % molecular weight of rhASM in daltons 
t = 0.1;
vp_weight               = 0.5*randn(1,43) + 70;                                               % subject weight (pre-infusion) over trial, in kg
vp_dose_schedule        = [0.1     0.3    0.3    0.6    1.0    2.0    3.0    3.0*ones(1,36)]; % dose administered, in mg/kg, following dose-escalation and maintenance protocol
vp_infusion_duration    = [0.8     1.0    1.0    1.25   1.75   2.5    3.5    3.5*ones(1,36)]; % time in hours of infusion duration
tmp = ones(K(end,end))
vp_dose_times           = 0:(7*24*2):(7*24*2*42);                                             % biweekly dosing for 43 doses
% convert mg/kg to total moles administered
vp_dose_administered  = vp_weight .* vp_dose_schedule * (1/1000) * (1/MW_ASM);                % dose administered, in moles
% calculate infusion rate
vp_infusion_rate      = vp_dose_administered ./ vp_infusion_duration;                         % infusion rate in moles/hour

k_inf = sum((t >= vp_dose_times & t <= (vp_dose_times + vp_infusion_duration)).*vp_infusion_rate);
disp(k_inf)
% 1.1054e-07

% boolean slice
a=[1 2; 3 4.1];
b(1,:,:)=a;
b(2,:,:)=a/10;
l0(1,:,:)=[1 4; 1 1];
l0(2,:,:)=[2 4; 2 2];

l02(:,:,1)=[1 1; 2 2];
l02(:,:,2)=[4 1; 4 2];

disp('zero')
disp([b(1), b(2), b(3), b(4)])
disp(b([1 4 1 1 2 4 2 2]));
bf=b(l0);
disp([bf(1,2,1), bf(1,2,2), bf(2,2,1), bf(1,1,2), bf(2,2,2), bf(2,1,1)]);
 
l1=b <= 1 | b >=3;
 
disp('one');
disp(b(l1));

disp('two');
disp(b(:,[1 4]));
 
l2 = false(2,2);
l2(1)=true;
l2(4)=true;
 
disp('three');
disp(b(:, l2));
 
d=ones(2,3,4)*0.9;
d(1,2,1)=2;
 
k=ones(2,2,2);
 
disp('four');
disp(d(:,k))
disp(d(:,d>1,:))

disp('five');
disp(a(:,k))

disp('six')
a = 4 * a(a>1);
disp(a);

disp('slice assign test');
r=[1 2 3 4; 5 6 7 8; 9 10 11 12];
z=[1.1 2.1 3.1 4.1; 6.1 7.1 8.1 9.1];

disp(r(1:2,:));

disp(r(1:2:3,:));

z=r(1:2:3,:);
disp(z);


disp(toc)
