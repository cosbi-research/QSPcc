% load data array
tic
z = csvread('treatment_virtual.csv', 1, 0)

e = csvread('treatment_virtual.csv', 2, 0)

g = csvread('treatment_virtual.csv', 3, 0)

disp(z);

disp(toc)
