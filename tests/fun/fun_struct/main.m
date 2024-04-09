tic;

disp('miscellaneus user-defined structures')
a.k.x=[];
a.x='asd';
a.y=[1 2 3; 1 2 4];
a.z=2.0;
a.k.x=[1 2];
a.k.w=2.1;
x =3.1;
b = a;


[z,k]= f(b, x);

disp(z)
disp(k)

disp(toc);
