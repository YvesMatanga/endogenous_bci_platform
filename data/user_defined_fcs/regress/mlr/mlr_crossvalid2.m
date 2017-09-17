function [r2_acc,r2_pred,sse_acc,sse_pred,beta_best] = mlr_crossvalid2(X,Y,k)
%MLR_CROSSVALID Summary of this function goes here
%   Detailed explanation goes here
[N] = size(X,1);
span = floor(N/k);
n = 1:N;

r2_acc = zeros(k,1);
r2_pred = zeros(k,1);
sse_acc = zeros(k,1);
sse_pred = zeros(k,1);
beta_list = zeros(k,size(X,2));

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
    [beta_mlr] = lsmlr2(TrainingX,TrainingY);
    beta_list(i,:) = beta_mlr;
    y_mlr = mlr_regressor2(beta_mlr,TrainingX);
    y_mlrp = mlr_regressor2(beta_mlr,TestX);
    [r2_acc(i),sse_acc(i)] = R2coef(TrainingY,y_mlr);
    [r2_pred(i),sse_pred(i)] = R2coef(TestY,y_mlrp);
end

    [val,ind]=max(r2_pred);
    beta_best = beta_list(ind,:);
end

