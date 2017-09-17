function [Y] = wn_regressor(X,A,L)
%WN_REGRESSOR Summary of this function goes here
%   X = NxM ,N = number of taps ,M = Number of sensors
%   A = NxM ,coefficient matrix
%  [N,M] = size(X);
N = size(X,1)-L+1;
Y = zeros(N,1);
for i=1:N
    Y(i) = sum(diag(A'*X(i:L+i-1,:)));
end
end

