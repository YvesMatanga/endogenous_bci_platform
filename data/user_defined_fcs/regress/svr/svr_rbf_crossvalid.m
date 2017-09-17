function [r2_acc,r2_pred,sse_acc,sse_pred] = svr_rbf_crossvalid(X,Y,k,gamma,c)
%SVR_RBF_CROSSVALID Summary of this function goes here
%   Detailed explanation goes here
[N] = size(X,1);
span = floor(N/k);
n = 1:N;

lib_options = ['-s 3 -t 2 -g ',num2str(gamma),' -c ',num2str(c)];

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
    svm_model = svmtrain(TrainingY,TrainingX,lib_options);
    y_svm = svmpredict(TrainingY,TrainingX,svm_model);
    y_svmp = svmpredict(TestY,TestX,svm_model);
    [r2_acc(i),sse_acc(i)] =  R2coef(TrainingY,y_svm);
    [r2_pred(i),sse_pred(i)] = R2coef(TestY,y_svmp);
end

end



