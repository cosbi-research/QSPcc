tic
a = [1,2]
b = 0.5

for i = 1:numel(a)
	b = f(a,b+i)
end

disp(b)
disp(toc)
