tic
x = [0.0, 1.0, 3.0, 4.0];
y = [0.3, 0.45, 0.30, 0.4];
pp = interp1(x,y, 0.5, 'nearest');

disp(pp);

disp(toc)
