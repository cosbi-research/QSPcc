tic
options = odeset('RelTol',1e-5,'AbsTol',1e-6);

c = 1;
tspan = 0:0.01:3;
y0 = [0 0.01];

[t,y] = ode15s(@(t,y) odefcn(t,y,c), tspan, y0, options);

disp(t)
disp(y)
disp(toc)
