function [AM,SKM,DIM,TKM] = rp_fwd(X,Y,Mmax)
%MARS_FWD : Forward Pass
%SKM : Mmax x Mmax
%DIM : Mmax x Mmax
%TKM : Mmax x Mmax
SKM = zeros(Mmax,Mmax);%Storage for all the basis of the algorithm
DIM = zeros(Mmax,Mmax);
TKM = zeros(Mmax,Mmax);

SKM_temp = zeros(Mmax,Mmax);%Storage for all the basis of the algorithm
DIM_temp = zeros(Mmax,Mmax);
TKM_temp = zeros(Mmax,Mmax);

dim = size(X,2);
amp = zeros(Mmax,1);
%Initially : All Bm = 1
 %B1(x)=1
mp = 1;%m*
vp = 1;%v*
tp = 0;%t*    
for M=2:Mmax
    %lofp = Inf;%goodness of fit
    gofp = 0;
    for m=1:M-1
        for v=1:dim            
            bm = rpBm(X,SKM(m,:),DIM(m,:),TKM(m,:));
            Tt = X(find(bm>0),v);
            Nt = length(Tt);
            for i=1:Nt     
              t = Tt(i);              
              %aMBM           
              [SKM_temp(M,:),DIM_temp(M,:),TKM_temp(M,:)] = add2Bm(SKM_temp(m,:),...
                 DIM_temp(m,:),TKM_temp(m,:),-1,v,t);
              %amBm
              [SKM_temp(m,:),DIM_temp(m,:),TKM_temp(m,:)] = add2Bm(SKM_temp(m,:),...
                 DIM_temp(m,:),TKM_temp(m,:),1,v,t);
              %optimization              
              [am,gof] = rp_fwd_opt(X,Y,M,SKM_temp,DIM_temp,TKM_temp);          
                if(gof > gofp)
                   gofp = gof;%lof = lof*
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
                 DIM(mp,:),TKM(mp,:),-1,vp,tp);
    %Bm*
    [SKM(mp,:),DIM(mp,:),TKM(mp,:)] = add2Bm(SKM(mp,:),...
                 DIM(mp,:),TKM(mp,:),1,vp,tp);
    %update
    SKM_temp = SKM;
    DIM_temp = DIM;
    TKM_temp = TKM;
end
AM = amp;
end

