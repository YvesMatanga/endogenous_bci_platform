function [Am,r2] = rp_fwd_opt(X,Y,M,SKM,DIM,TKM)
%FWD_OPT Summary of this function goes here
%   Detailed explanation goes here
N = size(X,1);
% y = aiBi(x) ,i=1,...N
%M = number of basis functions
Bx = zeros(N,M);
Mc = 1:M;
 for m=1:M
    Bx(:,m) = rpBm(X,SKM(m,:),DIM(m,:),TKM(m,:)); 
    b = unique(Bx(:,m));%select non constant basis functions
    if(length(b) > 1)
        Mc(m) = m;
    else
        Mc(m) = 0;
    end
 end
[am] = inv(Bx'*Bx)*Bx'*Y;%regression analysiss : mlr
ymodel = Bx*am;%mlr_regressor(am,Bx);
B = (Bx(Mc(Mc~=0),:))';%McxN
CM = trace(B*inv(B'*B)*B')+1;
M = length(Mc(Mc~=0))%number of non constant basis functions
d = 3;%cost of optimization
CMt = CM + d*M;
k = N/((N-CMt)^2);
SSE = sum((ymodel-Y).^2);
GCVM = k*SSE;
r2 = R2coef(X,Y,ymodel);
lof = GCVM
Am = am;
end


