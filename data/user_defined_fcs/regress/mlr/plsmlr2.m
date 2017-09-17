function [B] = plsmlr2(X,Y)
%Multiple Linear Regression
%
%Let X be the matrix of observation (NxV) 
%N = number of datasets and V = number of predictors
%Let Y be target vector (Nx1)
%
% [B,Xt,e] = lsmlr(X,Y)
%  B = [bo b1 ...bv] parameters estimates using SSE
%  Xt = rearranges datasets  such that Y = Xt*B
%  e = residual error vector
%  R2 = coeeficient of determination for goodness of fit test

N = length(Y);
Xt = X;%Let Xt be The Matrix Such that Y = Xt*B 
%B = [bo b1 ....bv]
B = pinv(Xt'*Xt)*Xt'*Y;
% e = Y - Xt*B;%residual
% SSE = (Y-Xt*B)'*(Y-Xt*B);
% SST =  sum((Y - mean(Y)).^2);
% R2 = (SST - SSE)/SST;
end

