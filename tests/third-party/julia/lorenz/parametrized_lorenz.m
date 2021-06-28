function du=parametrized_lorenz(t,u,p)
  du(1) = p(1)*(u(2)-u(1));
  du(2) = u(1)*(p(2)-u(3)) - u(2);
  du(3) = u(1)*u(2) - p(3)*u(3);
  du = du';
end
