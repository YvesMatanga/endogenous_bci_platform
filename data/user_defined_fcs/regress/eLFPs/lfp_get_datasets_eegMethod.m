function [F,D] = lfp_get_datasets_eegMethod(eegSessionBundle,G_online,FList,fs,fps,targetBundle)
%LFP_GET_DATASETS_EEGMETHOD Summary of this function goes here
%   Detailed explanation goes here
Nt = length(eegSessionBundle);
F = [];
D = [];
for i=1:Nt
eeg = eegSessionBundle{i};
tcoords = bci_get_target_coord(targetBundle{i},1);
Ftemp = eeg2FeatsSM(eeg,G_online,FList',fs,fps); 
Dtemp = tcoords(1)*ones(size(Ftemp,1),1);
F = [F;Ftemp];
D =[D;Dtemp];
end
end

