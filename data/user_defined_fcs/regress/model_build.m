function [model] = model_build(params)
%MODEL_BUILD Summary of this function goes here
%   Detailed explanation goes here
N = 50;
resultsEval = struct('MSE',0,'R2',0,'GCV',0,'R2GCV',0,'R2test',0,'MSEtest',0);
model = struct('coefs',zeros(N+1,1),'knotdims',zeros(N,1),'knotsites',zeros(N,1),...
    'knotdirs',zeros(N,1),'parents',zeros(N,1),'trainParams',params,'MSE',0,...
    'GCV',0,'t1',zeros(N,N),'t2',zeros(N,N),'minX',zeros(N,1),'maxX',zeros(N,1),...
    'isBinary',zeros(N,1),'X',zeros(N,1),'time',0,'resultsEval',resultsEval);

coder.varsize('model.t1');
coder.varsize('model.t2');
end

