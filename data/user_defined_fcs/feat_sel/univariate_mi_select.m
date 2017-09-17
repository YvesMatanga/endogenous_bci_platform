function [passedIndices,MIs,miTemp] = univariate_mi_select(F,Y,eps)
%UNIVARIATE_MI_SELECT : This Function selection Features based 
%Their Mutual Information with the target.
%Features are delat with as univariates features
%Y : N x 1 (Target Values)
%Features(F) : N x Number of Features
%passedIndices : X x 1 (return the number of features that has paaed)
%MI  : X x 1 will return the mutual information of feature that has passed
%eps : passing threshold
[N,NFr] = size(F);%get number of trials and number of features
miTemp = zeros(NFr,1);%return mutual information per features
j=1;
passedIndices = [];
MIs = [];

if nargin < 3%if epsilon is not defined
    eps = 0.3;
end

for i=1:NFr %evaluate mutual information per features    
    miTemp(i,:) = mi(F(:,i),Y);  
     if miTemp(i,:) >= eps
         MIs(j,:) = miTemp(i,:);%save the passes MIs
         passedIndices(j,:)=i;
         j = j+1;
     end
end

end

