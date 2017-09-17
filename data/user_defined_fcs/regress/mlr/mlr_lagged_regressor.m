function [Y] = mlr_lagged_regressor(W,X,L)
%MLR_LAGGED_REGRESSOR 
Xd = x2tap(X,L);
N = size(X,1);
Xt = [ones(1,N);Xd']';%Let Xt be The Matrix Such that Y = Xt*B
Y = Xt*W;
end

