
t_end = 100000;
t_step = 100;
tspan = 0:t_step:t_end;

opts = odeset('RelTol',1e-5,'AbsTol',1e-6);
tic

[t,x] = ode15s(@case00001, tspan, case00001, opts);
%csvwrite('00001_x.csv',[t,x])

[t,x] = ode15s(@case00002, tspan, case00002, opts);
%csvwrite('00002_x.csv',[t,x])

[t,x] = ode15s(@case00004, tspan, case00004, opts);
%csvwrite('00004_x.csv',[t,x])

[t,x] = ode15s(@case00019, tspan, case00019, opts);
%csvwrite('00019_x.csv',[t,x])

[t,x] = ode15s(@case00029, tspan, case00029, opts);
%csvwrite('00029_x.csv',[t,x])

[t,x] = ode15s(@case00035, tspan, case00035, opts);
%csvwrite('00035_x.csv',[t,x])

disp(toc)

