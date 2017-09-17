function [y] = filtered_mlr_regressor(beta,X,bf,af)
%MLR_REGRESSOREvaluate The Output of a Multiple Linear Regressor Model
%  Given Beta (1xNCoefs) 's and aInput Vector X (NxVars) 
%  y (NxVars)
[N,Vars] = size(X);

Xt =[ones(1,N);X']';
yt = Xt*beta;
y = filtfilt(bf,af,yt);
end

