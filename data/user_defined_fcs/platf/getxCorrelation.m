function [r,p] = getxCorrelation(targetBundle,dxyBundle,TrialIds,Mode)
N = length(TrialIds);
X = zeros(N,1);
Y = zeros(N,1);
k=1;
for i=1:N
    target_coords = double(bci_get_target_coord(targetBundle{TrialIds(i)},1));%get corrdinates of 
    Dx = dxyBundle{TrialIds(i)}(:,1);
    if isempty(find(unique(isnan(Dx))==1)) == false
        disp('found')
    else        
        Y(k) = target_coords(1) > 0;
        if Mode == 0        
        X(k) = mean(Dx);
        else
        X(k) = sign(mean(Dx));
        end 
        k = k+1;
    end
end
% [rtemp,ptemp] = corrcoef(XY(:,1),XY(:,2));
% r = rtemp(2);
% p = ptemp(2);
[r,h,p] = pointbiserial(Y,X);
end

