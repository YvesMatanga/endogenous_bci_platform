function [mars_model] = ares2mars(ares_model,MaxBasis)
%pass ARES_model data to SIMULINK fixed size Mars
mars_model = mars_build_model(MaxBasis,ares_model.trainParams);
ActualMaxBasis = length(ares_model.knotdims);
Nx = length(ares_model.minX);

for i=1:ActualMaxBasis
    mars_model.knotdims(i) = ares_model.knotdims{i};
    mars_model.knotdirs(i) = ares_model.knotdirs{i};
    mars_model.knotsites(i) = ares_model.knotsites{i};      
end
mars_model.coefs(1:(ActualMaxBasis+1)) = ares_model.coefs;
mars_model.parents(1:ActualMaxBasis) = ares_model.parents;
mars_model.MSE = ares_model.MSE;
mars_model.GCV = ares_model.GCV;
mars_model.minX(1:Nx) = ares_model.minX;
mars_model.maxX(1:Nx) = ares_model.maxX;
mars_model.isBinary(1:Nx) = ares_model.isBinary;
end

