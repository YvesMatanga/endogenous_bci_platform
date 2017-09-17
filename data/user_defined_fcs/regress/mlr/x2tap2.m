function [Xout] = x2tap2(X,L)
%X2TAP2 converts a matrix X(NxM) to a tapped matrix X(NxML)
%remove uincomplete lags w9ith zeros)
[N,M] = size(X);
Xd = zeros(N,M*L);
for i=1:L-1
    Xd(i,:) = reshape(flipud([zeros(L-i,M);X(1:i,:)]),1,M*L);
end

for i=L:N
    Xd(i,:) = reshape(flipud(X(i-L+1:i,:)),1,M*L);
end
Xout = Xd(L:end,:);
end

