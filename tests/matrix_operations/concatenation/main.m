tic
% printing i...
i=[[1,2;5,6];[3,4;7,8]]
disp(i)
% printing j...
j=[[1,2;5,6],[1,2;5,6];[3,4;7,8],[3,4;7,8]]
disp(j)

% printing k...
k=[[1,2];[3,4;7,8]]
disp(k)

% printing w...
w=[[1;2],[3;4];[5,6]]
disp(w)

% join vector by column
c1 = [1 ; 2; 3]
c2 = c1
c3 = c2

disp([c1 c2 c3])

% printing A
A = [w k; j]
disp(A)

% mixed
B = [1.1 2 3 4 ones(1,30)]
disp(B)

disp(toc)
