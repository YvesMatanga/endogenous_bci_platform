function [xcw,xm] = ica_prep(x)
%Algorithm was preprocessing of ica observations prior
% to unmixing
%Let x be a matrix mxn , m = number of observations  n = sample length
 % Centering 
xt = x';%nxm
xm = mean(xt); %mean per row
[M,N] = size(xt);
xc = zeros(M,N);%Initialize y with zeros
for i = 1:N
    xc(:,i) = xt(:,i) - xm(i);
end
 % Whitening
% ExxT = cov(xc);
% [E,D] = eig(ExxT);%eget eigen vector and digaonal martix
% xcw = E*(D.^(-0.5))*(E')*xc;%whitened and centered data

xw =  whiten(xc);
xcw = xw';
xm = xm';%mean per row
end

