d = linspace(0,3, 10);
y = exp(-1.3*d) + 0.05*randn(size(d));
y0 = [exp(-1.3*0) + 0.05*randn];

odeoptions = odeset('RelTol',1e-5,'AbsTol',1e-6);
options = optimoptions(@lsqnonlin);

x0 = [4.0];
lb = [1e-6];
ub = [10.0];
% Create an anonymous function that takes a value of the exponential decay rate r 
% and returns a vector of differences from the model with that decay rate and the data.
tic;
x = lsqnonlin(@(r) odesystem(d,r,y0,odeoptions) - y, x0, lb, ub, options);

disp(sprintf('Best solution: %f', x(1)))
% MATLAB OUTPUT (2023): Best solution: 0.228230
% QSPCC  OUTPUT       : Best solution: 0.231170
disp(toc);
% MATLAB OUTPUT (2023): 0.5081
% QSPCC  OUTPUT       : 6.69e-2

function y=odesystem(d, r, y0, odeoptions)
  [t,y] = ode15s(@(t,y) -r*t, d, y0, odeoptions);
end
