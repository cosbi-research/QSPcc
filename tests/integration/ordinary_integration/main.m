tic
options = odeset('RelTol',1e-5,'AbsTol',1e-6);

tspan = [0 5];
y0 = [0 0.01];
[t1,y1] = ode15s(@(t,y) odefcn(t,y), tspan, y0, options);

disp(t1+1)
disp(y1)
disp(toc)
