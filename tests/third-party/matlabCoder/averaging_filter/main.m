tic
% generate noisy data
v = 0:0.00614:2*pi;

x = sin(v) + 0.3*rand(1,numel(v));

% pass them to filter
y = afilter(x);
disp(toc);

%csvwrite('out.tsv', [x,y])
