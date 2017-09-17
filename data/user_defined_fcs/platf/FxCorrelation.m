function [r,p] = FxCorrelation(targetBundle,FeaturesBundle,TrialIds,Mode)
N = length(TrialIds);

k=1;

NFr = size(FeaturesBundle{1},2);
r = zeros(1,NFr);
p = zeros(1,NFr);

X = zeros(N,NFr);
Y = zeros(N,1);

for i=1:N
    target_coords = double(bci_get_target_coord(targetBundle{TrialIds(i)},1));%get corrdinates of 
    Fx = FeaturesBundle{TrialIds(i)};
    if isempty(find(unique(isnan(Fx))==1)) == false
        disp('found')
    else        
        Y(k) = target_coords(1) > 0;
        if Mode == 0        
        X(k,:) = mean(Fx);
        else
        X(k,:) = sign(mean(Fx));
        end 
        k = k+1;
    end
end
% [rtemp,ptemp] = corrcoef(XY(:,1),XY(:,2));
% r = rtemp(2);
% p = ptemp(2);
for i=1:NFr
    [r(i),h,p(i)] = pointbiserial(Y,X(:,i));
end
end

