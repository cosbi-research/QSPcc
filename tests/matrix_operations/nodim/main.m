tic
% column vector
vector = csvread('bigvector.csv', 1, 0);
% matrix
K = [vector,vector,vector];

f(K);

disp(toc)
