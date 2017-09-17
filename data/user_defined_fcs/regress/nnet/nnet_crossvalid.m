function [r2_acc,r2_pred,sse_acc,sse_pred,ffnet_out] = nnet_crossvalid(hdn,X,Y,k)
%MLR_CROSSVALID Summary of this function goes here
%   Detailed explanation goes here

ffnet = feedforwardnet(hdn);
ffnet.divideParam.trainRatio = 0.9;
ffnet.divideParam.valRatio = 0.1;
ffnet.divideParam.testRatio = 0;

[N] = size(X,1);
span = floor(N/k);
n = 1:N;

r2_acc = zeros(k,1);
r2_pred = zeros(k,1);
sse_acc = zeros(k,1);
sse_pred = zeros(k,1);
ffnetbank = {};
for i=1:k
   if i ~= k
    ids = (i-1)*span+1:i*span;
   else
    ids = (i-1)*span+1:N;%up to the end in case dataset is not muliplt of k
   end
    TestX = X(ids,:);%data for validation
    TestY = Y(ids,:);
    nids = n;%copy indexes
    nids(ids) = [];%delete unwanted
    TrainingX = X(nids,:);%data for training
    TrainingY = Y(nids,:);
    
    %Training    
    ffnet = train(ffnet,TrainingX',TrainingY');
    ffnetbank{i} = ffnet;
    y_nnet = ffnet(TrainingX')';
    y_nnetp = ffnet(TestX')';
    [r2_acc(i),sse_acc(i)] = R2coef(TrainingY,y_nnet);
    [r2_pred(i),sse_pred(i)] = R2coef(TestY,y_nnetp);
end
[val,ind]=max(r2_pred);
ffnet_out = ffnetbank{ind};
end

