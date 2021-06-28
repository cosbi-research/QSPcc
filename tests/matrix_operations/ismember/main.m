tic

A = 1:20

disp('A')
disp(A)

c=21
disp('c')
disp(c)

result = ismember(c,A)
disp('ismember(c,A)')
disp(result)

B = 1.0:0.5:20.1

disp('B')
disp(B)

d=19.4
disp('d')
disp(d)

result = ismember(d,B)
disp('ismember(d,B)')
disp(result)

E = [1,2,3,4,5,6]

disp('E')
disp(E)

f=2
disp('f')
disp(f)

result = ismember(f,E)
disp('ismember(f,E)')
disp(result)

G = [1.0,2.2,3.3,4.4,5.,6.]

disp('G')
disp(G)

h=2.2
disp('h')
disp(h)

result = ismember(h,G)
disp('ismember(h,G)')
disp(result)


X = [1.0,2.2,3.3,4.4,5.,6.]

disp('X')
disp(X)

Y = [1.0, 6.0, 4.4]
disp('Y')
disp(Y)

resultVect = ismember(Y,X)
disp('ismember(Y,X)')
disp(resultVect)

XI = [1.0,2.0,3.0,4.0,5.0,6.0]

disp('XI')
disp(XI)

YI = [1.0, 6.0, -20.0]
disp('YI')
disp(YI)

resultVect = ismember(YI,XI)
disp('ismember(YI,XI)')
disp(resultVect)

X2 = 1.0:1.0:6.0

disp('X2')
disp(X2)

Y2 = [1.0, 6.0, -20.0]
disp('Y2')
disp(Y2)

resultVect = ismember(Y2,X2)
disp('ismember(Y2,X2)')
disp(resultVect)

X3 = 1.0:1.0:6.0

disp('X3')
disp(X3)

Y3 = 1.0:0.5:2.5
disp('Y3')
disp(Y3)

resultVect = ismember(Y3,X3)
disp('ismember(Y3,X3)')
disp(resultVect)

X4 = [1,2,3,4]

disp('X4')
disp(X4)

Y4 = [1,4,8,10]
disp('Y4')
disp(Y4)

resultVect = ismember(Y4,X4)
disp('ismember(Y4,X4)')
disp(resultVect)

X5 = 1:4

disp('X5')
disp(X5)

Y5 = [1,4,8,10]
disp('Y5')
disp(Y5)

resultVect = ismember(Y5,X5)
disp('ismember(Y5,X5)')
disp(resultVect)

X6 = 1:4

disp('X6')
disp(X6)

Y6 = 1:20
disp('Y6')
disp(Y6)

resultVect = ismember(Y6,X6)
disp('ismember(Y6,X6)')
disp(resultVect)

disp(toc)
