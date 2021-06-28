% isempty

tic

K= rand(2,2)

disp('K')
disp(K)

disp('K is empty')
disp(isempty(K))

A =[]

result = isempty(A)
disp('matrix A is empty?')
disp(result)

B = 1:5:10
result = isempty(B)
disp('slice B is empty?')
disp(result)

E = 10:5:5
result = isempty(E)
disp('slice E is empty?')
disp(result)


disp(toc)
