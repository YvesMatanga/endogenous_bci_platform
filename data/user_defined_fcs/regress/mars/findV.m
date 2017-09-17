function [V] = findV(X,Dim)
%FINDV Summary of this function goes here
%   Detailed explanation goes here
UniqDim = unique(Dim);
Nv = length(UniqDim);
NVar = size(X,2);
V = 1:NVar;
for j=1:Nv
  V = V(V~=UniqDim(j));%Remove all variables in the UniqueDim
end
end

