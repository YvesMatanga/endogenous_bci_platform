function [AMp,SKMp,DIMp,TKMp] = mars_bwd(X,Y,AM,SKM,DIM,TKM)
%MARS_BWD Summary of this function goes here
%   Detailed explanation goes here
Mmax = size(SKM,1);
Jp = 1:Mmax;
Kp = Jp;
[am,lofp] = bwd_opt(X,Y,Mmax,SKM,DIM,TKM);
M = Mmax;
AMp = AM;%initial one is default prior to trimming
while(M <= 2)
 b = Inf;
 L = Kp;
 for m=2:M
     K = L(L ~= m);
     [am,lof] = bwd_opt(X,Y,length(K),SKM(K,:),DIM(K,:),TKM(K,:));
     if(lof < b)
         b = lof;
         Kp = K;
     end
     if(lof < lofp)
         lofp = lof;
         Jp = K;
         Amp = zeros(Mmax,1);
         AMp(K) = am
         disp('done')
     end
 end
    M = M+1;
end

SKMp = zeros(Mmax,Mmax);
DIMp = zeros(Mmax,Mmax);
TKMp = zeros(Mmax,Mmax);
SKMp(Jp,:) = SKM(Jp,:);
DIMp(Jp,:) = DIM(Jp,:);
TKMp(Jp,:) = TKM(Jp,:);
end

