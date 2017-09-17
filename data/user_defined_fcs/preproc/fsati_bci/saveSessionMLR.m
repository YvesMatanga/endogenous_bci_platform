function [] = saveSessionMLR(eeg_frame,sim_out,phase_id,description,channel_matrix,feature_selection_char_array,fs,filename,dim)
%SAVESESSION Summary of this function goes here
%   Detailed explanation goes here
eegSessionBundle = getSessionFromFrame(eeg_frame);

if dim==1

[modelParametersBundle,statsBundle,dxyBundle,dxyRawBundle,...
FeaturesBundle,targetBundle] = getDataFromFrameMlr(sim_out,dim);

save(filename,'eegSessionBundle','modelParametersBundle','dxyBundle',...
    'dxyRawBundle','statsBundle','targetBundle','FeaturesBundle','description',...
    'channel_matrix','feature_selection_char_array','fs','phase_id');

elseif dim==2

[modelParametersBundle_x,statsBundle_x,dxyBundle,dxyRawBundle,...
 FeaturesBundle_x,targetBundle,modelParametersBundle_y...
 ,statsBundle_y,FeaturesBundle_y] = getDataFromFrameMlr(sim_out,dim);

save(filename,'eegSessionBundle','modelParametersBundle_x','modelParametersBundle_y','dxyBundle',...
    'dxyRawBundle','statsBundle_x','statsBundle_y','targetBundle','FeaturesBundle_x','FeaturesBundle_y',...
    'description','channel_matrix','feature_selection_char_array','phase_id','fs');
else
    
end    
end

