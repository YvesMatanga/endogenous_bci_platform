function [dcor] = dcorr2(X,Y)
%DCORR2 Summary of this function goes here
%   Detailed explanation goes here

N = size(X,1);
aij = zeros(N,N);
Aij = zeros(N,N);
Bij = zeros(N,N);
bij = zeros(N,N);
ai = zeros(N,1);
bi = zeros(N,1);
aj = zeros(N,1);
bj = zeros(N,1);

for i=1:N
    for j=1:N
        aij(i,j)= abs(X(i) - X(j));
        bij(i,j)= abs(Y(i) - Y(j));
    end
end

for i=1:N    
    ai(i) = mean(aij(i,:));
    bi(i) = mean(bij(i,:));
    aj(i) = mean(aij(:,i));
    bj(i) = mean(bij(:,i));    
end
% aij
% bij
% ai
% bi
% aj
% bj
mean_a = mean(aij(:));
mean_b = mean(bij(:));

dcovxy = 0;
dcovx = 0;
dcovy = 0;

for i=1:N
    for j=1:N
        Aij(i,j) = aij(i,j) - ai(i) - aj(i) + mean_a;
        Bij(i,j) = bij(i,j) - bi(i) - bj(i) + mean_b;
        dcovxy = dcovxy + Aij(i,j)*Bij(i,j);
        dcovx = dcovx + Aij(i,j)*Aij(i,j);
        dcovy = dcovy + Bij(i,j)*Bij(i,j);
    end
end

dcovxy = dcovxy/(N^2);
dcovx = dcovx/(N^2);
dcovy = dcovy/(N^2);

dcor = sqrt(dcovxy/sqrt(dcovx*dcovy));
end

