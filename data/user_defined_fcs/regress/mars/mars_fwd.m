function [AM,SKM,DIM,TKM] = mars_fwd(X,Y,Mmax)
%MARS_FWD : Forward Pass
%SKM : Mmax x Mmax
%DIM : Mmax x Mmax
%TKM : Mmax x Mmax
SKM = zeros(Mmax+1,Mmax+1);%Storage for all the basis of the algorithm
DIM = zeros(Mmax+1,Mmax+1);
TKM = zeros(Mmax+1,Mmax+1);

SKM_temp = zeros(Mmax+1,Mmax+1);%Storage for all the basis of the algorithm
DIM_temp = zeros(Mmax+1,Mmax+1);
TKM_temp = zeros(Mmax+1,Mmax+1);

dim = size(X,2);
amp = zeros(Mmax+1,1);
%Initially : All Bm = 1
 %B1(x)=1
mp = 1;%m*
vp = 1;%v*
tp = 0;%t*    
M = 2;
while(M <= Mmax)
    lofp = Inf;%goodness of fit
    for m=1:M-1
         %V = findV(X,DIM(m,:));%set of variables not associated with m Bm basis
         V = 1:dim;
         Nv = length(V);
        for j=1:Nv
            v = V(j);
            bm = Bm(X,SKM(m,:),DIM(m,:),TKM(m,:));
            Tt = X(find(bm>0),v);
            Nt = length(Tt);
            for i=1:Nt     
              t = Tt(i);              
              %aMBM           
              [SKM_temp(M,:),DIM_temp(M,:),TKM_temp(M,:)] = add2Bm(SKM_temp(m,:),...
                 DIM_temp(m,:),TKM_temp(m,:),1,v,t);
              %a(M+1)B(M+1)
              [SKM_temp(M+1,:),DIM_temp(M+1,:),TKM_temp(M+1,:)] = add2Bm(SKM_temp(m,:),...
                 DIM_temp(m,:),TKM_temp(m,:),-1,v,t);
              %optimization              
              [am,lof] = fwd_opt(X,Y,M,SKM_temp,DIM_temp,TKM_temp);          
                if(lof < lofp)
                   lofp = lof;%lof = lof*
                   amp(1:length(am)) = am;
                   mp = m;%m*=m
                   vp = v;%v*=v
                   tp = t;%t*=t
                end            
            end
        end
    end
    
    %BM(x)
    [SKM(M,:),DIM(M,:),TKM(M,:)] = add2Bm(SKM(mp,:),...
                 DIM(mp,:),TKM(mp,:),1,vp,tp);
    %Bm*
    [SKM(M+1,:),DIM(M+1,:),TKM(M+1,:)] = add2Bm(SKM(mp,:),...
                 DIM(mp,:),TKM(mp,:),-1,vp,tp);
    %update
    SKM_temp = SKM;
    DIM_temp = DIM;
    TKM_temp = TKM;
    M = M + 2;
end
AM = amp;
end

