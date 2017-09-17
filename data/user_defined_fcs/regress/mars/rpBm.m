function [bm] = rpBm(X,Skm,Dim,Tkm)
%Basis Function Definition
%   Detailed explanation goes here
N = size(X,1);
bm = ones(N,1);
NSkm = length(find(abs(Skm)>0));%find number of non zeros Skm

%describe basis function
if NSkm > 0 
K = length(Skm);
 for i=1:K
     if(Skm(i) ~= 0)%for nonzeros terms only
     bm = bm.*(Skm(i)*(X(:,Dim(i))-Tkm(i))>0);
     end
 end
end

end

