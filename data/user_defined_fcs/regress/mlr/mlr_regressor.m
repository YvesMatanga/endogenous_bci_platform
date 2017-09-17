function [y] = mlr_regressor(beta,X,bf,af)
%MLR_REGRESSOREvaluate The Output of a Multiple Linear Regressor Model
%  Given Beta (1xNCoefs) 's and aInput Vector X (NxVars) 
%  y (NxVars)
[N,Vars] = size(X);

Xt =[ones(1,N);X']';
yf = Xt*beta;

if nargin > 2
    y=filtfilt(bf,af,yf);
else
    y=yf;
end
end

