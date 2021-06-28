tic;

A=[1, -2; -1, 0]
B=[4, -3; 2, 3]

disp(kron(A,B))

disp('kron([1,2; 3, 4], ones(8,1))')
disp(kron([1,2; 3, 4], ones(8,1)))

disp(toc);
