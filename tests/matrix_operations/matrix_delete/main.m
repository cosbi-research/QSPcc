tic
D = [ 3 4 5;
      1 3 1;
      3 5 9 ];

E = D;

D(1,:)=[];
% equivalent
E = E(:,2:3);

disp(toc);
