function y=f(a,b)
b= 0.1
for i=1:numel(a)
    if a(i) == 1
	    y = 0;
	    b = b + 0.2;
    elseif a(i) == 2
	    y = 1;
	    b = b + 0.3;
    else
	    y = 0.7
    end
end

end

