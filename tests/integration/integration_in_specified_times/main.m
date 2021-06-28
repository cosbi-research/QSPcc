tic
options = odeset('RelTol',1e-5,'AbsTol',1e-6);

tspan = [0, 0.1, 0.3, 1, 1.6, 2.75, 3, 3.1, 4, 5];
y0 = [3];

ode15s(@(t,y) -10*y, tspan, y0, options);


disp(toc)
