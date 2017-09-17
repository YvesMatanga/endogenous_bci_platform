function [Y] = mars_rgr(X,Am,SKM,DIM,TKM)
%MARS_RGR : Perform regressio using mars
N = size(X,1);
Y = zeros(N,1);
M = length(Am);
% y = aiBi(x) ,i=1,...N
%  for m=2:M
%     Y = Y + Am(m)*Bm(X,SKM(m,:),DIM(m,:),TKM(m,:));   
%  end
 
Bx = zeros(N,M);
 for m=1:M
    Bx(:,m) = Bm(X,SKM(m,:),DIM(m,:),TKM(m,:));   
 end
Y = Bx*Am;%mlr_regressor(Am,Bx);
end

