function [W] = lsmlr_lagged(X,Y,L)
%LSMLR_LAGGED 
  Xd = x2tap(X,L);%create delay lines of L tap long
  N = length(Y);
  Xt = [ones(1,N);Xd']';%Let Xt be The Matrix Such that Y = Xt*B
  W = inv(Xt'*Xt)*Xt'*Y;  
end

