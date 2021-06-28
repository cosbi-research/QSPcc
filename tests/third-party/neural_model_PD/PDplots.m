function y = PDplots(t0,tf,t,y,rates,om)

NumFig = 10;
om = om(1);

figure(NumFig)
clf
set(NumFig,'Color',[1 1 1])
subplot(3,2,1)
plot(t,rates(:,1))
title('GO signal')
v = axis;
% axis([0 40 v(3) v(4)])
axis([0 v(2) v(3) v(4)])
axis tight
subplot(3,2,2)
plot(t,y(:,1))
hold on
plot(t,max(y(:,2),0),'--')
v = axis;
% axis([0 20 v(3) v(4)])
axis([0 v(2) v(3) v(4)])
axis tight
title('DV')
subplot(3,2,3)
plot(t,y(:,3))
hold on
plot(t,max(y(:,4),0),'--')
v = axis;
% axis([0 20 v(3) v(4)])
axis([0 tf v(3) v(4)])
% axis tight
title('PPV')
subplot(3,2,4)
plot(t,rates(:,10))
hold on
plot(t,rates(:,11),'--')
v = axis;
% axis([0 20 0 0.08])
axis([0 tf 0.006 v(4)])
% axis tight
title('DVV')
xlabel('Time (ms)')
subplot(3,2,5)
plot(t,rates(:,12))
v = axis;
% axis([0 20 0.03 0.07])
axis([0 v(2) 0.006 v(4)])
axis tight
title('P')
xlabel('Time (ms)')

NumFig = NumFig + 1;

figure(NumFig)
clf;
set(NumFig,'Color',[1 1 1])
subplot(3,2,1)
plot(t,y(:,11))
hold on
plot(t,y(:,12), 'r--')
v = axis;
% axis([0 60 v(3) v(4)])
axis([t0 tf 0 v(4)])
plot([om om],[0 v(4)],'r--')
title('alpha-MN')
subplot(3,2,2)
plot(t,y(:,21))
hold on
plot(t,y(:,22),'--')
v = axis;
% axis([0 60 v(3) v(4)])
axis([t0 tf v(3) v(4)])
plot([om om],[v(3) v(4)],'r--')
title('IaIN')
subplot(3,2,3)
plot(t,y(:,9))
hold on
plot(t,y(:,10), '--')
v = axis;
% axis([0 60 v(3) v(4)])
axis([t0 tf v(3) v(4)])
plot([om om],[v(3) v(4)],'r--')
title('Renshaw cell')
subplot(3,2,4)
plot(t,y(:,25))
hold on
plot(t,y(:,26),'--')
v = axis;
% axis([0 60 -0.01 v(4)])
axis([t0 tf v(3) v(4)])
plot([om om],[v(3) v(4)],'r--')
title('IbIN')
subplot(3,2,5)
plot(t,rates(:,13))
hold on
v = axis;
% axis([0 60 v(3) v(4)])
axis([t0 tf v(3) v(4)])
plot(t,rates(:,14),'--')
title('Renshaw recruitment rate')
subplot(3,2,6)
plot(t,y(:,23))
hold on
plot(t,y(:,24),'--')
v = axis;
% axis([0 60 0 v(4)])
axis([t0 tf v(3) 0.7])
plot([om om],[v(3) v(4)],'r--')
title('Spindle response')


NumFig = NumFig + 1;

figure(NumFig)
clf;
set(NumFig,'Color', [1 1 1])
subplot(2,2,1)
plot(t,y(:,13))
hold on
plot(t,y(:,14),'--')
axis tight
title('Static gamma-MN')
subplot(2,2,2)
plot(t,y(:,15))
hold on
plot(t,y(:,16),'--')
axis tight
title('Intrafusal static')
subplot(2,2,3)
plot(t,y(:,17))
hold on
plot(t,y(:,18),'--')
v = axis;
axis([0 tf 0 v(4)])
% axis tight
title('Dynamic gamma-MN')
subplot(2,2,4)
plot(t,y(:,19))
hold on
plot(t,y(:,20),'--')
v = axis;
axis([0 tf 0 v(4)])
% axis tight
title('Intrafusal dynamic')

NumFig = NumFig + 1;

figure(NumFig)
clf;
set(NumFig,'Color', [1 1 1])
subplot(3,1,1)
plot(t,y(:,5))
hold on
plot(t,y(:,6),'--')
axis tight
title('Contractile muscle state')
subplot(3,1,2)
plot(t,rates(:,6))
hold on
plot(t,rates(:,7), '--')
axis tight
title('Contraction rate (beta)')
subplot(3,1,3)
plot(t,rates(:,8))
hold on
plot(t,rates(:,9), '--')
axis tight
title('Number of contractile fibers (B)')
xlabel('Time (ms)')

NumFig = NumFig + 1;

figure(NumFig)
clf
set(NumFig,'Color',[1 1 1])
subplot(2,2,1)
plot(t,y(:,7))
hold on
v = axis;
% axis([0 60 -0.01 v(4)])
axis([t0 tf 0. v(4)])
plot([om om],[v(3) v(4)],'r--')
title('Position')
subplot(2,2,2)
plot(t,y(:,8))
hold on
v = axis;
% axis([0 60 -0.03 v(4)])
axis([t0 tf v(3) v(4)])
plot([om om],[v(3) v(4)],'r--')
title('Velocity')
subplot(2,2,3)
plot(t,rates(:,2))
hold on
plot(t,rates(:,3), '--')
axis tight
title('Length')
subplot(2,2,4)
plot(t,rates(:,4))
hold on
plot(t,rates(:,5), '--')
v = axis;
axis([t0 tf 0 v(4)])
plot([om om],[v(3) v(4)],'r--')
title('Force')

NumFig = NumFig + 1;

figure(NumFig)
clf
set(NumFig,'Color',[1 1 1])
subplot(2,2,1)
plot(t,rates(:,10))
hold on
v = axis;
axis([0 60 0.01 v(4)])
plot([om om],[0.0 v(4)],'r--')
title('DVV flexion')
subplot(2,2,3)
hold on
plot(t,rates(:,11))
v = axis;
axis([0 60 0.005 0.03])
plot([om om],[0.0 0.03],'r--')
title('DVV extension')
xlabel('Time (ms)')
subplot(2,2,2)
plot(t,rates(:,12))
hold on
v = axis;
axis([0 60 0.04 v(4)])
plot([om om],[0.04 v(4)],'r--')
title('P flexion')
subplot(2,2,4)
plot(t,rates(:,12))
hold on
v = axis;
axis([0 60 0.04 v(4)])
plot([om om],[0.04 v(4)],'r--')
title('P extension')
xlabel('Time (ms)')

NumFig = NumFig + 1;
y = NumFig;





