function [coefs,dims,dirs,sites] = mars_params_init(Nx,MaxBasis)
%MARS_PARAMS_INIT Summary of this function goes here
%   Detailed explanation goes here
coefs = zeros(1,MaxBasis+1);
dims = zeros(1,MaxBasis);
dirs = zeros(1,MaxBasis);
sites = zeros(1,MaxBasis);

for i=1:2*Nx
   if(mod(i,2) == 0)%even
    coefs(i+1) = 1;
    dirs(i) = 1;
   else%odd
    coefs(i+1) = -1;
    dirs(i) = -1;
   end
   dims(i) = ceil(i/2);
end

end

