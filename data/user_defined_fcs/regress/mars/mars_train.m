function [AM,SKM,DIM,TKM] = mars_train(X,Y,Mmax)
%MARS_BUILD Summary of this function goes here
%   Detailed explanation goes here
[AMf,SKMf,DIMf,TKMf] = mars_fwd(X,Y,Mmax);%forward pass
[AM,SKM,DIM,TKM] = mars_bwd(X,Y,AMf,SKMf,DIMf,TKMf);%backward pass
end

