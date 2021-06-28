clear all;
tic 
   [t, y_MATLAB] = ode15s(@test, [0:.1:10], [3,5], [], [2,3,4], [1], [6]); 
disp(toc );

% csvwrite('matlab.csv', [t,y_MATLAB]);
   
   % plot(t, y_MATLAB) 
   
