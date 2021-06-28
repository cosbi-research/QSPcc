tic
a = [1,2];

parfor i = 1:numel(a)
	disp(f(a,0.5+i));
end

disp(toc)
