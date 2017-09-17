function [SkmOut,DimOut,TkmOut] = add2Bm(Skm,Dim,Tkm,skm,dim,tkm)
%Basis Function Definition
%   Detailed explanation goes here
M = length(Skm);
m = 0;
for i=1:M
    if(Skm(i) ~= 0)
        m = i;
        break;
    end
end

SkmOut = Skm;
DimOut = Dim;
TkmOut = Tkm;

SkmOut(m+1) = skm;
DimOut(m+1) = dim;
TkmOut(m+1) = tkm;
end