tic
options = odeset('RelTol',1e-5,'AbsTol',1e-6);

tspan = 0:0.1:2;
y0 = [3];

[t,y] = ode15s(@(t,y) -10*y, tspan, y0, options);

disp(t)
disp(y)

disp(toc)
