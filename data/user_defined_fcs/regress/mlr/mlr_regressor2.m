function [y] = mlr_regressor2(beta,X)
%MLR_REGRESSOREvaluate The Output of a Multiple Linear Regressor Model
%  Given Beta (1xNCoefs) 's and aInput Vector X (NxVars) 
%  y (NxVars)
[N,Vars] = size(X);
Xt = X;
y = Xt*beta;
end

