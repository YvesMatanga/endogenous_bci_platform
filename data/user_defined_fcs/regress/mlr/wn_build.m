function [W] = wn_build(X,Y,L)
%WN_BUILD Summary of this function goes here
%   Detailed explanation goes here
%   X = NxM ,N = number of samples , M = number of sources
[M] = size(X,2);
[C] = size(Y,2);% C = control dimensions

%R : correlation matrix
R = zeros(L*M,L*M);
for i=1:M
    for j=1:M
        temp = xcorrmtx(X(:,i),X(:,j));
        R(L*(i-1)+1:L*i,L*(j-1)+1:L*j) = temp(1:L,1:L);%cross-correlation
    end
end

%P Matrix
P = zeros(L*M,C);
for i=1:C
    for j=1:M
        temp = xcorr(X(:,j),Y(:,i),L-1);
        P(L*(j-1)+1:L*j,i) = temp(1:L);
    end
end
W = reshape(R^(-1)*P,L,M);
end

