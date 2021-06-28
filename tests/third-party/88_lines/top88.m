%%% 88 line topology optimization code
function top88(nelx, nely, volfrac, penal, rmin, ft)
% material properties
EO = 1;
Emin = 1e-9;
nu = 0.3;
% prepare finite element analysis
A11 = [12, 3, -6, -3; 3, 12, 3, 0; -6, 3, 12, -3; -3, 0, -3, 12];
A12 = [-6, -3, 0, 3; -3, -6, -3, -6; 0, -3, -6, 3; 3, -6, 3, -6];
B11 = [-4, 3, -2, 9; 3, -4, -9, 4; -2, -9, -4, -3; 9, 4, -3, -4];
B12 = [2, -3, 4, -9; -3, 2, 9, -2; 4, 9, 2, 3; -9, -2, 3, 2];
KE = 1/(1-nu^2)/24*([A11 A12; A12' A11]+nu*[B11 B12; B12' B11]);
nodenrs = reshape(1:(1+nelx)*(1+nely), 1+nely, 1+nelx);
edofVec = reshape(2*nodenrs(1:end-1,1:end-1)+1, nelx*nely, 1);
edofMat = repmat(edofVec, 1, 8)+repmat([0, 1, 2*nely+[2 3 0 1], -2, -1],nelx*nely,1);
iK = reshape(kron(edofMat, ones(8,1))', 64*nelx*nely, 1);
jK = reshape(kron(edofMat, ones(1,8))', 64*nelx*nely, 1);
% define loads and supports (half mbb-beam)
F = sparse(2,1,-1.0,2*(nely+1)*(nelx+1),1);
U = zeros(2*(nely+1)*(nelx+1),1);
fixeddofs = union([1:2:2*(nely+1)], [2*(nelx+1)*(nely+1)]);
alldofs = [1:2*(nely+1)*(nelx+1)];
freedofs = setdiff(alldofs, fixeddofs);
% prepare filter
iH = ones(nelx*nely*(2*(ceil(rmin)-1)+1)^2,1);
jH = ones(size(iH));
sH = zeros(size(iH));
k = 0;
for i1 = 1:nelx
  for j1 = 1:nely
    e1 = (i1-1)*nely+j1;
    for i2 = max(i1-(ceil(rmin)-1),1):min(i1+(ceil(rmin)-1),nelx)
      for j2 = max(j1-(ceil(rmin)-1),1):min(j1+(ceil(rmin)-1),nely)
        e2 = (i2-1)*nely+j2;
        k = k + 1;
        iH(k) = e1;
        jH(k) = e2;
        sH(k) = max(0, rmin-sqrt((i1-i2)^2+(j1-j2)^2));
      end
    end
  end
end
H = sparse(iH, jH, sH);
Hs = sum(H,2);
% initialize iteration
x = repmat([volfrac], nely, nelx);
xPhys = x;
loop = 0;
change = 1;
% results
results = [];
% start iteration
while change > 0.01
  loop = loop + 1;
  %disp(sprintf('loop %d change %f', loop, change));
  % FE-analysis
  sK = reshape(KE(:)*(Emin+xPhys(:)'.^penal*(EO-Emin)),64*nelx*nely,1);
  K = sparse(iK,jK,sK); 
  K = (K+K')/2;
  U(freedofs) = K(freedofs, freedofs)\F(freedofs);
  % objective function an sensitivity analysis
  ce = reshape(sum((U(edofMat)*KE).*U(edofMat),2),nely,nelx);
  c = sum(sum((Emin+xPhys.^penal*(EO-Emin)).*ce));
  dc = -penal*(EO-Emin)*xPhys.^(penal-1).*ce;
  dv = ones(nely, nelx);
  % filtering/modification of sensitivities
  %csvwrite(sprintf('Hs_%d.csv', loop), Hs);
  %csvwrite(sprintf('dc_%d.csv', loop), dc);
  if ft == 1
    dc(:) = H*(x(:).*dc(:))./Hs./max(1e-3,x(:));
  elseif ft == 2
    dc(:) = H*(dc(:)./Hs);
    dv(:) = H*(dv(:)./Hs);
  end
  % optimality criteria update of design variables and physical densities
  l1 = 0; l2 = 1e9; move = 0.2;
  
  while (l2-l1)/(l1+l2) > 1e-3
    lmid = 0.5*(l2+l1);
    xnew = max(0,max(x-move,min(1,min(x+move,x.*sqrt(-dc./dv/lmid)))));
    if ft == 1
      xPhys = xnew;
    elseif ft == 2
      xPhys(:) = (H*xnew(:))./Hs;
    end
    if sum(xPhys(:)) > volfrac*nelx*nely 
      l1 = lmid; 
    else 
      l2 = lmid; 
    end
  end
  change = max(abs(xnew(:)-x(:)));
  x = xnew;
  % print results
  results = [results; [loop,c, mean(xPhys(:)), change]];
  %colormap(gray); imagesc(1-xPhys); caxis([0 1]); axis equal; axis off; drawnow;
end

csvwrite('results.csv', results);

