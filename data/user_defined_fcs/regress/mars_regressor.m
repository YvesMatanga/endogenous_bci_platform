function [y] = mars_regressor(model,X)
%MARS_REGRESSOR : Evaluate The Output of a MARS Model
%  Given an Input Vector X (NxVars)
%  y (NxVars)
K = length(model.coefs)-1;%number of basis functions
[N,dim] = size(X);
y = model.coefs(1)*ones(1,N);%first value is the bias
for i=1:K
   y = y + model.coefs(i+1)*max(0,model.knotdirs{i}*(X(:,model.knotdims{i})'-model.knotsites{i}));   
end
y = y';
end

