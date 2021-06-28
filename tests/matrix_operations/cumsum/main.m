% isempty

tic

E = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
F = cumsum(E);

disp('E')
disp(E)

disp('cumsum(E)')
disp(F)

A = [1, 2; 3, 4; 5, 6; 7, 8; 9, 10]
B = cumsum(A);

disp('A')
disp(A)

disp('cumsum(A)')
disp(B)

G = 1:20;
H = cumsum(G);

disp('G')
disp(G)

disp('cumsum(G)')
disp(H)

I = 1.0:0.5:10.0;
L = cumsum(I);

disp('I')
disp(I)

disp('cumsum(I)')
disp(L)

disp(toc)
