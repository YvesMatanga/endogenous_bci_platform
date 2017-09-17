function [y] = mars_regressor2(model,X)
%MARS_REGRESSOR : Evaluate The Output of a MARS Model
%  Given an Input Vector X (NxVars)
%  y (NxVars)

MaxBasis = size(model.knotdims,1);
BasisDepth = size(model.knotdims,3);

K = sizeModel(model,MaxBasis);
[N] = size(X,1);
y = model.coefs(1)*ones(1,N);%first value is the bias
for i=1:K
   mbend = modelBasisEnd(model,i,BasisDepth);
   y = y + model.coefs(i+1)*max(0,model.knotdirs(i,1,1:mbend)*(X(:,model.knotdims(i,1,1:mbend))'-model.knotsites(i,1,1:mbend)));   
end
y = y';
return

function [mbend] = modelBasisEnd(model,ind,BasisDepth)
   mbend = BasisDepth;
   for i=1:BasisDepth
       if model.knotdims(ind,1,i) == 0
           mbend = i-1;
           break;
       end
   end
return

function [col] = sizeModel(model,MaxBasis)
    col = 0;
    for i=1:MaxBasis
        temp=sum(abs(model.knotdims(i,1,:)));
        if temp == 0
            col = i-1;
            break;
        end
    end
return