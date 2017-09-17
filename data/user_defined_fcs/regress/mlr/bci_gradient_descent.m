function [Wt] = bci_gradient_descent(FeaturesBundle,targetBundle,statsBundle,W,alpha)
%BCI_GRADIENT_DESCENT Summary of this function goes here
%   Detailed explanation goes here
[Nt] = size(FeaturesBundle,1)
NFr = size(FeaturesBundle{1},2);
g = 15;
Wt = zeros(NFr,1);
Wt_1 = W;
NIter = 1000;
for j=1:NIter
    range = randperm(Nt);
    for i=range
         cxyt = bci_get_target_coord(targetBundle{i},1);
         cx0 = 0;
         b = g/statsBundle{i}(2);
         a = statsBundle{i}(1);
         pt = cxyt(1);
         
         F = FeaturesBundle{i};
         N = size(F,1);
         In = ones(N,1);
         
         Oo = cx0 - N*b*a;
         Ot = Oo + b*W'*F'*In;
         et = double(Ot-pt);         
         Wt = Wt_1 - alpha*2*b*et*F'*In;
         Wt_1 = Wt;
    end
end

end

