%a=[1 2 3; 4 5 6]

%b=a(1,:)
tic;

s=sparse(5,5);

w=sparse(1, 2, 2);

h = 2*3/1;
m=sparse(1, 1, h);

x=sparse([1 2 2], [1 1 2], [5 6 7])


z=sparse([1 2 2], [1 1 2], [5 6 7], 3, 3);

a=(x+w)/3

disp(toc);
