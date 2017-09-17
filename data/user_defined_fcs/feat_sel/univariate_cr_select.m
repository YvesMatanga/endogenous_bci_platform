function [passedIndices,CRs,crTemp] = univariate_cr_select(F,Y,eps)
%UNIVARIATE_MI_SELECT : This Function selection Features based 
%Their Correlation with the target.
%Features are delat with as univariates features
%Y : N x 1 (Target Values)
%Features(F) : N x Number of Features
%passedIndices : X x 1 (return the number of features that has paaed)
%MI  : X x 1 will return the mutual information of feature that has passed
%eps : passing threshold
[N,NFr] = size(F);%get number of trials and number of features
crTemp = zeros(NFr,1);%return mutual information per features
j=1;
passedIndices = [];
CRs = [];

if nargin < 3%if epsilon is not defined
    eps = 0.15;
end

for i=1:NFr %evaluate mutual information per features
    cor = corrcoef(F(:,i),Y);    
    crTemp(i,:) = cor(2,1);
     if abs(crTemp(i,:)) >= eps
         CRs(j,:) = crTemp(i,:);%save the passes MIs
         passedIndices(j,:)=i;
         j = j+1;
     end
end

end

