s1 = 'Yes';
s2 = 'No';
tf = strcmp(s1,s2)
disp(tf);
% 0

s1 = 'yes';
s2 = 'yes';
disp(strcmp(s1,s2));
% 1

s1 = 'yes';
s2 = 'Yes';
disp(strcmp(s1,s2));
% 0
