function dydt = odefcn(t,y,c)
dydt = zeros(2,1);
dydt(1) = y(2);
dydt(2) = t.*y(1);
