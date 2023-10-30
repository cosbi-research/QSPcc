d = linspace(0,3, 10);
y = exp(-1.3*d) + 0.05*randn(size(d));

x0 = [4.0];
lb = [1e-6];
ub = [10.0];
% Create an anonymous function that takes a value of the exponential decay rate r 
% and returns a vector of differences from the model with that decay rate and the data.
options = optimoptions(@lsqnonlin, 'InitialStandardDeviations', [0.57])
tic;
x = lsqnonlin(@(r)exp(-d*r)-y, x0, lb, ub, options);

disp(sprintf('Best solution: %f', x(1)))
disp(toc);

