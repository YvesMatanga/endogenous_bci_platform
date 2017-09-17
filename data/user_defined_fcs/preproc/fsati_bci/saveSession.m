function [] = saveSession(eeg_frame,sim_out,phase_id,description,filename)
%SAVESESSION Summary of this function goes here
%   Detailed explanation goes here
eegSessionBundle = getSessionFromFrame(eeg_frame);
if phase_id==1
save(filename,'eegSessionBundle','phase_id');
else

[modelParametersBundle,statsBundle,dxyBundle,dxyRawBundle,...
FeaturesBundle,targetBundle] =...
getDataFromFrame(sim_out);

save(filename,'eegSessionBundle','modelParametersBundle','dxyBundle',...
    'dxyRawBundle','statsBundle','targetBundle','FeaturesBundle','description','phase_id');
end

end

