function g

tic;

function [b,c]=f(a, n)

if isempty(a)
	a = zeros(n);
end

b = a(2,:);
c = a(2,1:n);

end

a(:,:,1) = [1 2 3; 4 5 6; 7 8 9]; 
a(:,:,2) = [1 2 3; 4 5 6; 7 8 9] + 10; 
[x,y] =f(a, 3);
[z,k] = f(a, 4);

%disp(x)
%disp(y)

%disp(z)
%disp(k)

disp(toc);

end
