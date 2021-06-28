% isempty

tic

dimension=10

disp('10x10 matrix from slice')
disp(reshape(1:dimension^2,dimension,dimension))

A = [1, 2, 3, 4, 5; 6, 7, 8, 9, 10]
B = reshape(A, 5, 2, 1, 1);

disp('A')
disp(A)

disp('reshape(A, 5, 2, 1, 1)')
disp(B)

Z = 1:20
Q = reshape(Z, 4, 5);

disp('Z')
disp(Z)

disp('reshape(Q, 4, 5)')
disp(Q)

X = 1.0:0.5:10.5
Y = reshape(X, 4, 5);

disp('X')
disp(X)

disp('reshape(X, 4, 5)')
disp(Y)

C = [1, 2; 3, 4; 5 6; 7, 8; 9, 10]
D = reshape(C, 5, 2, 1, 1)

disp('C')
disp(C)

disp('reshape(C, 5, 2, 1, 1)')
disp(D)

F = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
disp('F')
disp(F)

G = reshape(F, 2, 5)

disp('reshape(F, 2, 5)')
disp(G)

H = [1, 2, 3, 4, 5; 6, 7, 8, 9, 10]
disp('H')
disp(H)

[x,y] = size(H)

O = reshape(H, y, x)

disp('reshape(H, 5, 2)')
disp(O)


disp(toc)
