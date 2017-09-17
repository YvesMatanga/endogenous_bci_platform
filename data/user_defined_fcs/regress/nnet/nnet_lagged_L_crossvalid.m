function [r2_acc,r2_pred,sse_acc,sse_pred] = nnet_lagged_L_crossvalid(Xin,Y,L,k)
%MLR_CROSSVALID Summary of this function goes here
%   Detailed explanation goes here



NL = length(L);

r2_acc = zeros(NL,k,1);
r2_pred = zeros(NL,k,1);
sse_acc = zeros(NL,k,1);
sse_pred = zeros(NL,k,1);

for v=1:NL

ffnet = feedforwardnet(50);
ffnet.divideParam.trainRatio = 0.85;
ffnet.divideParam.valRatio = 0.15;
ffnet.divideParam.testRatio = 0;

X = x2tap(Xin,L(v));

[N] = size(X,1);
span = floor(N/k);
n = 1:N;
fprintf('--- L = %d ---\n',L(v))
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
    y_nnet = ffnet(TrainingX')';
    y_nnetp = ffnet(TestX')';
    [r2_acc(v,i,1),sse_acc(v,i,1)] = R2coef(TrainingY,y_nnet);
    [r2_pred(v,i,1),sse_pred(v,i,1)] = R2coef(TestY,y_nnetp);
    fprintf('(%d)-- r2acc (%d) = %.2f r2pred(%d) = %.2f\n',v,i,r2_acc(v,i,1),i,r2_pred(v,i,1));    
end
fprintf('\n\n')
end

end

