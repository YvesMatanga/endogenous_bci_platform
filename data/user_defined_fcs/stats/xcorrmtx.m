function [R] = xcorrmtx(X,Y)
%UNTITLED2 Summary of this function goes here
%   Detailed explanation goes here
%   X = Nx1 ,Y = Nx1
N = length(X);
Rntemp = xcorr(X,Y,N-1);
Rn = Rntemp(1:N);
R = zeros(N,N);
R(1,:) = Rn';
Rshift = Rn';
for i=1:(N-1)
  Rshift = [Rn(i+1),Rshift(1:end-1)];
  R(i+1,:) = Rshift;
end
end

