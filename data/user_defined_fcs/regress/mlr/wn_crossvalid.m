function [r2_acc,r2_pred,sse_acc,sse_pred] = wn_crossvalid(X,Y,L,k)
%MLR_CROSSVALID Summary of this function goes here
%   Detailed explanation goes here
[N] = size(X,1);
span = floor(N/k);
n = 1:N;

r2_acc = zeros(k,1);
r2_pred = zeros(k,1);
sse_acc = zeros(k,1);
sse_pred = zeros(k,1);

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
    [W_wn] = wn_build(TrainingX,TrainingY,L);
    y_wn = wn_regressor(TrainingX,W_wn,L);    
    y_wn = y_wn/std(y_wn);%standardization
    Nt = length(y_wn);
    
    y_wnp = wn_regressor(TestX,W_wn,L);
    y_wnp = y_wnp/std(y_wnp);%standardization
    Np = length(y_wnp);
    
    [r2_acc(i),sse_acc(i)] = R2coef(TrainingY(1:Nt),y_wn);
    [r2_pred(i),sse_pred(i)] = R2coef(TestY(1:Np),y_wnp);
end

end

