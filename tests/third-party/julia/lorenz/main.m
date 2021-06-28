u0 = [1.0, 0.0, 0.0];
tspan = [0.0, 1.0];
p = [10.0, 28.0, 8/3];
tic
[t,y] = ode15s(@(t, u) parametrized_lorenz(t, u, p), tspan, u0);
disp(toc)
%csvwrite('juliaout.csv', [t,y])
