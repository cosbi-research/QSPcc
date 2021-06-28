function dydt = odefcn(t,y)
dydt = zeros(2,1);
dydt(1) = y(2);
dydt(2) = (1/2)*t.*y(1);