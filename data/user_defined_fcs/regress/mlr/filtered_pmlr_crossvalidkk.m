function [r2_acc,r2_pred,sse_acc,sse_pred,beta_best,Rn,r2best] = filtered_pmlr_crossvalidkk(Xin,Yin,k,L,bf,af)
%MLR_CROSSVALID Summary of this function goes here
%   Detailed explanation goes here
[N] = size(Xin,1);
span = floor(N/k);
n = 1:N;

r2_acc = zeros(k,1);
r2_pred = zeros(k,1);
sse_acc = zeros(k,1);
sse_pred = zeros(k,1);

r2best = 0;
beta_best = zeros(size(Xin,2)+1,1);

for q=1:k
    Ids = randperm(N);
    X = Xin(Ids,:);
    Y = Yin(Ids,:);
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
    [beta_mlr] = plsmlr(TrainingX,TrainingY);
    y_mlr = mlr_regressor(beta_mlr,TrainingX);
    y_mlrf = y_mlr;%filtfilt(bf,af,y_mlr);    
    y_mlrp = mlr_regressor(beta_mlr,TestX);
    y_mlrpf = y_mlrp;%filtfilt(bf,af,y_mlrp);    
    [r2acc,sseacc] = R2coef(TrainingY,y_mlrf);
    
    r2_acc(i) = r2_acc(i) + r2acc/k;
    sse_acc(i) = sse_acc(i) + sseacc/k;
    
    [r2pred,ssepred] = R2coef(TestY,y_mlrpf);
    
    r2_pred(i) = r2_pred(i) + r2pred/k;
    sse_pred(i) = sse_pred(i) + ssepred/k;
    
    if r2pred > r2best
        r2best = r2pred;
        beta_best = beta_mlr;  
    end
end
    NFr = (length(beta_best)-1)/L;
    Rn = zeros(NFr,1);
    
    i=1;
    for q=1:NFr
        Rn(q) = sum(abs(beta_best(i+1:i+L)));
        i=i+L;
    end
    Rn = Rn/L;
end
end