tic;

A=[1.1, 2; 3, 4]
B=[5, 6; 7, 8]
C=[1; 2; 3; 4; 5; 6]
K(1,:,:) = A
K(2,:,:) = B

disp([K K K; K K K])

disp('repmat(K,3, 1)')
disp(repmat(K,3, 1))

disp('repmat(A,1,3)')
disp(repmat(A,1,3))

disp('repmat(C,1,3)')
disp(repmat(C,1,3))

disp('repmat(A,2,3)')
disp(repmat(A,2,3))

disp('repmat(1:6, 1, 7)');
disp(repmat(1:6, 1, 7));

disp(toc);
