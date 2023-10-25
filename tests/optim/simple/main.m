d = linspace(0,3);
y = exp(-1.3*d) + 0.05*randn(size(d));

x0 = [4.0];
lb = [0.5];
ub = [10.0];
% Create an anonymous function that takes a value of the exponential decay rate r 
% and returns a vector of differences from the model with that decay rate and the data.
options = optimoptions(@lsqnonlin,'Algorithm','cmaes','MaxIterations',1500, 'InitialStandardDeviations', [0.17])
tic;
x = lsqnonlin(@(r)exp(-d*r)-y, x0, lb, ub, options);
disp(toc);

csvwrite('results.tsv', [d,x])

