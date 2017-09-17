function [F,D] = lfp_get_datasets_invMethod(eegSessionBundle,G_online,dxyBundle,fs,fps,L)
%LFP_GET_DATASETS_INVMETHOD Summary of this function goes here
%   Detailed explanation goes here
Nt = length(eegSessionBundle);
F = [];
D = [];
for i=1:Nt
eeg = eegSessionBundle{i};
Ftemp = x2tap(eeg2LMP(eeg,G_online,fs,fps),L); 
Dtemp = dxyBundle{i};
F = [F;Ftemp];
D =[D;[0;diff(Dtemp(:,1))]];
end
end

 