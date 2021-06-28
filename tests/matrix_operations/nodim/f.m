function k=f(K)
a = K(end,:);
b = K(end,end);
c = K(:,:);
d = [K,K;K,K];
w = d * d';
k = w * w;
end