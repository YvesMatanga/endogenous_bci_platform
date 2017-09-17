function [Y] = mlr_lagged_preprocessed_regressor(W,X)
%MLR_LAGGED_REGRESSOR 
N = size(X,1);
Xt = [ones(1,N);X']';%Let Xt be The Matrix Such that Y = Xt*B
Y = Xt*W;
end

