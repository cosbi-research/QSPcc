function dx = test(t, x, p, u, v)
dx(1) = u(1) - p(1) * x(1) + p(2) * x(2); 
dx(2) = p(1) * x(1) - ((p(3)/4) + p(2)) * x(2); 
dx = dx(:); 