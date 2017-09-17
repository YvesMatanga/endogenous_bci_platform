function [r2_acc,r2_pred,sse_acc,sse_pred,beta_best,Rn] = filtered_mlr_crossvalid(X,Y,k,L,bf,af)
%MLR_CROSSVALID Summary of this function goes here
%   Detailed explanation goes here
[N] = size(X,1);
span = floor(N/k);
n = 1:N;

r2_acc = zeros(k,1);
r2_pred = zeros(k,1);
sse_acc = zeros(k,1);
sse_pred = zeros(k,1);
beta_list = zeros(k,1+size(X,2));
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
    [beta_mlr] = lsmlr(TrainingX,TrainingY);
    beta_list(i,:) = beta_mlr;
    y_mlr = mlr_regressor(beta_mlr,TrainingX);
    y_mlrf = filtfilt(bf,af,y_mlr);    
    y_mlrp = mlr_regressor(beta_mlr,TestX);
    y_mlrpf = filtfilt(bf,af,y_mlrp);    
    [r2_acc(i),sse_acc(i)] = R2coef(TrainingY,y_mlrf);
    [r2_pred(i),sse_pred(i)] = R2coef(TestY,y_mlrpf);
end
    [val,ind]=max(r2_pred);
    beta_best = beta_list(ind,:);
    NFr = (length(beta_best)-1)/L;
    Rn = zeros(NFr,1);
    
    i=1;
    for q=1:NFr
        Rn(q) = sum(abs(beta_best(i+1:i+L)));
        i=i+L;
    end
    Rn = Rn/L;
end

