function [xh,yh,zh] = fromBLHtoXYZ(B,L,h,a,f)
% f is same as 1./298.257...
% a is the semi-major axis, such as 6378137 (m);
% B,L in radian, H in meter
  ee = 1-(1-f)^2;
  N = a/sqrt(1-ee*(sin(B))^2);
  xh = (N+h)*cos(B)*cos(L);
  yh = (N+h)*cos(B)*sin(L);
  zh = (N*(1-ee)+h)*sin(B);

