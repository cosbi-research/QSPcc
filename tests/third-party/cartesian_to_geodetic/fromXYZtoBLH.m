function [B,L,h]=fromXYZtoBLH(xh,yh,zh,a,f)
% f is same as 1./298.257...
% a is the semi-major axis, such as 6378137 (m);
% B,L in radian, H in meter
  
  % initialize
  b = a*(1.-f);
  ee = 1 - (1-f)^2;
  
  % Eq.(9);
  alf = (xh^2+yh^2)/a^2+zh^2/b^2;
  beta =(xh^2+yh^2)/b^2+zh^2/a^2;
  gama = b/a+a/b;
  
  % Eq.(11);
  p = 2-beta-gama^2/2;
  q = beta*gama-(2/gama)*(alf+beta);
  r = 1+beta-(gama/2)^2*(2+beta)+(gama/2)^4;
  
  %---------------------------------------------------------------------------
  %Algebla prediction begain
  %Eq.(20)
  G = (4+beta-gama^2)/12;
  es = -((alf+beta)^2/gama^2-alf*beta)/32;
  %Eq.(19)
  E = - G^2;
  F = G^3+es;
  %Eq.(21)
  E3F2 = es*(es+2*G^3);
  % theoretically, E3F2>=0; but sometimes E3F2 numerical value is negative 
% in multivalue region.
  E3F2 = abs(E3F2);
  % theoretically, F+sqrt(E3F2)>=0; but sometimes F+sqrt(E3F2) numerical value is negative
  m = ( abs(F+sqrt(E3F2)) )^(1/3);
  % theoretically, F-sqrt(E3F2)>=0; but sometimes F-sqrt(E3F2) numerical value is negative
  m = m - E/m;
  % theoretically, -m-p/6>=0; but sometimes -m-p/6 numerical value is negative
  s0 = abs(-m-p/6);
  % theoretically, u+v=sqrt((p/2+2*s0)^2-r)-p/2-s0 >=0;
  uv = sqrt(abs(sqrt(abs((p/2+2*s0)*(p/2+2*s0)-r))-p/2-s0)); 
  % theoretically, (p/2+2*s0)^2-r>=0;
  t0 = -sign(q)*sqrt(s0)+uv;
  %Algebla prediction end
  %---------------------------------------------------------------------------
  
  % sometimes the Algebla prediction t exists numerical value error, but we
  % can see this t as a good initial value and get a new solution from the
  % basic quartic equation untill the process converges satisfactorily.
  % The author find that only once iteration, the new solution is precision
  % and satisfy our application to 1 mm over geodetic height range from
  % -6x10^6 to 10^10 m.
  
  %Algebla correction begain
  t0 = sqrt(-p/2+sqrt(p*p-4*(q*t0+r))/2);
  %Algebla correction end
  
  %eigenvalue or eigen-root that we publish
  lmt = t0 - gama/2;
  
  x = xh*(a/(a+b*lmt));
  y = yh*(a/(a+b*lmt));
  z = zh*(b/(b+a*lmt));
  
  B = atan2( z, sqrt(x*x+y*y)*(1-ee) ); %[-M_PI/2,+M_PI/2]
  if( x == 0 & y == 0 )
    L = 0.; % Longitude is non define.
  else
    L = atan2( y, x ); %[-M_PI,+M_PI]
  end
  h = sign(lmt)*sqrt( (xh-x)*(xh-x)+(yh-y)*(yh-y)+(zh-z)*(zh-z) );

