mStruct.s.x1 = 1; 
mStruct.s.x2 = 2; 
mStruct.p.k1 = 1; 
mStruct.p.k2 = 2; 
mStruct.p.k3 = 3; 
mStruct.u.u1 = 1;
mStruct.c.c1 = 1;

convertToC(mStruct, 'test.m')
compileC( 'odeC');

tic
[t y_odeC] = odeC([0:.1:10], [3,5], [2,3,4], [1], [1e-6, 1e-8, 10]);
toc