function [Xd] = x2tap(X,L)
%X2TAP converts a matrix X(NxM) to a tapped matrix X(NxML)
[N,M] = size(X);
if(N < L)
    X = [zeros(L-N,M);X];
    N = L;
end
Xd = zeros(N,M*L);
for i=1:L-1
    Xd(i,:) = reshape(flipud([zeros(L-i,M);X(1:i,:)]),1,M*L);
end

for i=L:N
    Xd(i,:) = reshape(flipud(X(i-L+1:i,:)),1,M*L);
end
end

