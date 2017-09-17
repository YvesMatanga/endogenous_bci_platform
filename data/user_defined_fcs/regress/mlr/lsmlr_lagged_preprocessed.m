function [W] = lsmlr_lagged_preprocessed(X,Y)
%LSMLR_LAGGED   
  N = length(Y);
  Xt = [ones(1,N);X']';%Let Xt be The Matrix Such that Y = Xt*B
  W = inv(Xt'*Xt)*Xt'*Y;  
end

