% operations with matrices

tic

disp('********************DOUBLE MATRIX*******************')
K=[1.0,3.1;4.5,5.7]

disp('K')
disp(K)

disp('log')
E=log(K)

disp(E)

E=exp(K)

disp('exp')
disp(E)

F=ceil(K)

disp('ceil')
disp(F)

F=floor(K)

disp('floor')
disp(F)


A = [1.0, 3.0]
B = [2.1, 2.1]
P= power(A,B)
disp('power')
disp(P)

disp('********************INT MATRIX*******************')

H=[1,3;4,5]
disp('H')
disp(H)


E=log(H)

disp('log')
disp(E)

E=exp(H)

disp('exp')
disp(E)

L= [1, 3]
M = [2, 2]
R= power(L,M)

disp('power')
disp(R)

disp(toc)

disp('**********complex matrix-scalar-matrix operations****************')

a=[1 2 3 4 10.0]
b=[5 6 7 8 9.0]'
c=[1 2 3 4.0]

disp(-((3*a+b).*(a+b/2)))

disp((2*3/4+b).*(c+4))

disp(toc);
