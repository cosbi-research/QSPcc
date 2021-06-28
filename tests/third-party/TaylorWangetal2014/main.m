tic;
%initial condition near the fixed point
fp_approx = [0.1724,    0.1787,   -0.0818,    0.2775];


[t1,u]=ode45(@AmariTCimpBS, 0:0.01:10, fp_approx);%background state
[t2,v]=ode45(@AmariTCimpBS, 10:0.01:15, u(end,:)-[0.3 0.3 0 0]);%seizure state
[t3,w]=ode45(@AmariTCimpBS, 15:0.01:30, v(end,:)-[0.3 0.3 0 0]);%background state
%%


PY=[u(:,1); v(2:end,1); w(2:end,1)];
IN=[u(:,2); v(2:end,2); w(2:end,2)];

TC=[u(:,3); v(2:end,3); w(2:end,3)];
RE=[u(:,4); v(2:end,4); w(2:end,4)];

t=[t1;t2(2:end);t3(2:end)];

disp(toc);

csvwrite('results.csv', [t mean([PY, IN], 2)]);

%figure(1)
%plot(t,mean([PY,IN],2),'k')
%hold on
%m=mean(fp_approx(1:2));
%plot([10 10],[m m-.3],'r','LineWidth',5)
%plot([15 15],[m m-.3],'b','LineWidth',5)
%hold off
%xlabel('Time (sec)','FontSize',20)
%ylabel('Simulated EEG','FontSize',20)
%set(gca,'FontSize',15)
%legend('Simulated EEG','Stimulus pulse to induce SWD','Stimulus pulse to terminate SWD')
