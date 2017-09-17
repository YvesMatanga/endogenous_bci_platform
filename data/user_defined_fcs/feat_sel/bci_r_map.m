function [r2XY,pXY] = bci_r_map(SessionXY)
%BCI_R2_MAP Summary of this function goes here
%   Detailed explanation goes here
[NTr,NBands,NCh] = size(SessionXY);
r2XY = zeros(NBands-1,NCh);
pXY = zeros(NBands-1,NCh);

for i=1:NCh
    for j=1:NBands-1
        X = SessionXY(:,j,i);
        Y = SessionXY(:,end,i);
        r2XY(j,i) = rp(X,Y);
        pXY(j,i) = 0;
    end
end
image(r2XY,'CDataMapping','scaled');
title('EEG Band vs Channel r')
colorbar
return 

function [r]=rp(X,Y)
%   covr = cov(X,Y)^2;
%   varx = var(X);
%   vary = var(Y);
%   r2temp = covr/(varx*vary);
  rtemp = corrcoef(X,Y);
  r = rtemp(2);
return

function [r2,t]=r2p2(X,Y)
%   covr = cov(X,Y)^2;
%   varx = var(X);
%   vary = var(Y);
%   r2temp = covr/(varx*vary);
  id1 = find(Y == 1);
  id2 = find(Y == -1);
  X1 = X(id1,:);
  X2 = X(id2,:);
  
  s1 = sum(X1);
  s2 = sum(X2);
  
  q1 = sum(X1.^2);
  q2 = sum(X2.^2);
  
  n1 = length(X1);
  n2 = length(X2);
  
  G = (s1 + s2)^2/(n1+n2);
  
  r2 = ((s1^2/n1) + (s2^2/n2) - G)/(q1 + q2 - G);
  t = sqrt((n1 + n2 - 2)*r2/(1-r2));
return


