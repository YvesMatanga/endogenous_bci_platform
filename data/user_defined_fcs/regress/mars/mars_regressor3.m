function [y] = mars_regressor4(model,X)
%MARS_REGRESSOR : Evaluate The Output of a MARS Model
%  Given an Input Vector X (NxVars)
%  y (NxVars)
MaxBasis = (length(model)-1)/4;
K = sizeModel(model,MaxBasis);
[N] = size(X,1);
y = model.coefs(1)*ones(1,N);%first value is the bias
for i=1:K   
   y = y + model.coefs(i+1)*max(0,model.knotdirs(i)*(X(:,model.knotdims(i))'-model.knotsites(i)));   
end
y = y';
return

function [col] = sizeModel(model,MaxBasis)
    col = MaxBasis;
    for i=1:MaxBasis        
        if model.knotdims(i) == 0
            col = i-1;
            break;
        end
    end
return