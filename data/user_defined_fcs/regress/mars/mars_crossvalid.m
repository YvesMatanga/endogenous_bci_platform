function [r2_acc,r2_pred,sse_acc,sse_pred,best_model] = mars_crossvalid(X,Y,k)
%MARS_CROSSVALID Summary of this function goes here
%   Detailed explanation goes here
[N] = size(X,1);
span = floor(N/k);
n = 1:N;

r2_acc = zeros(k,1);
r2_pred = zeros(k,1);
sse_acc = zeros(k,1);
sse_pred = zeros(k,1);

trainParams = aresparams2('cubic',false,'maxInteractions',0);
models = {};

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
    mars_model = aresbuild(TrainingX,TrainingY,trainParams);
    models{i} = mars_model;
    y_mars = mars_regressor(mars_model,TrainingX);
    y_marsp = mars_regressor(mars_model,TestX);
    [r2_acc(i),sse_acc(i)] = R2coef(TrainingY,y_mars);
    [r2_acc(i),sse_pred(i)] = R2coef(TestY,y_marsp);
end
    [val,ind]=max(r2_pred);
    best_model = models{ind};
end

