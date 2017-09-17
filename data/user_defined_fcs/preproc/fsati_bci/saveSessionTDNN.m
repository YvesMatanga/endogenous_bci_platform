function [] = saveSessionTDNN(eeg_frame,sim_out,phase_id,description,channel_matrix,feature_selection_char_array,filename,dim)
%SAVESESSION Summary of this function goes here
%   Detailed explanation goes here
eegSessionBundle = getSessionFromFrame(eeg_frame);

if dim==1

[statsBundle,dxyBundle,dxyRawBundle,...
FeaturesBundle,targetBundle] = getDataFromFrameTdnn(sim_out,dim);

save(filename,'eegSessionBundle','dxyBundle',...
    'dxyRawBundle','statsBundle','targetBundle','FeaturesBundle','description',...
    'channel_matrix','feature_selection_char_array','phase_id');

elseif dim==2

[statsBundle_x,dxyBundle,dxyRawBundle,...
 FeaturesBundle_x,targetBundle...
 ,statsBundle_y,FeaturesBundle_y] = getDataFromFrameTdnn(sim_out,dim);

save(filename,'eegSessionBundle','dxyBundle',...
    'dxyRawBundle','statsBundle_x','statsBundle_y','targetBundle','FeaturesBundle_x','FeaturesBundle_y',...
    'description','channel_matrix','feature_selection_char_array','phase_id');
else
    
end    
end

