function [y] = mars_regressor4(model_parameters,X)
%MARS_REGRESSOR : Evaluate The Output of a MARS Model
%  Given an Input Vector X (NxVars)
%  y (NxVars)
MaxBasis = (length(model_parameters)-1)/4;
coefs = model_parameters(1:(MaxBasis+1));
knotdims = model_parameters((MaxBasis+2):(2*MaxBasis+1));
knotdirs = model_parameters((2*MaxBasis+2):(3*MaxBasis+1));
knotsites = model_parameters((3*MaxBasis+2):(4*MaxBasis+1));

K = sizeModel(knotdims,MaxBasis);
[N] = size(X,1);
y = coefs(1)*ones(1,N);%first value is the bias
for i=1:K   
   y = y +coefs(i+1)*max(0,knotdirs(i)*(X(:,knotdims(i))'-knotsites(i)));   
end
y = y';
return


function [col] = sizeModel(knotdims,MaxBasis)
    col = MaxBasis;
    for i=1:MaxBasis        
        if knotdims(i) == 0
            col = i-1;
            break;
        end
    end
return