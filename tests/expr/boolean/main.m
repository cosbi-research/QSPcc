tic
a = 3;
b = 1;
c = a  > b;

disp(c > b)

% matrix of boolean

X = [1,2,3,4;5,6,7,8];
Z = X > a;

disp(X)
disp(Z)

K = X == Z;

disp(K)
disp(isinf(K))
disp(toc)
