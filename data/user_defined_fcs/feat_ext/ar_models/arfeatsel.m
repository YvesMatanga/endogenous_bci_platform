function [feat_vec] = arfeatsel(ArpChns,chnsBandsMatrix,NFr)
%ARFEATSEL Summary of this function goes here
%   Detailed explanation goes here   
[NCh] = size(ArpChns,1);
feat_vec= zeros(1,NFr);
v=1;
for i=1:NCh
    bandFs = unique(chnsBandsMatrix(i,:));
    bands = bandFs(bandFs~=0);
    NBands = length(bands);
 for j=1:NBands
     %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
     % PLEASE SET SAMPLING FREQ OF arband in the function
     %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    feat_vec(v)=arband(ArpChns(i,:),bands(j));
    v = v+1;
 end
end
end
