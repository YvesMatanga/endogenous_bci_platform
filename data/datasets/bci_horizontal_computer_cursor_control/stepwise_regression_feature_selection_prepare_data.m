N = size(eegSessionBundle,1);%get number of trials

FeaturesOut = [];
TargetOut = [];
Nsd = 13;
all = true;
for i=1:N
    [Fout,Dout] = getDataSetx2(FeaturesBundle{i},targetBundle{i},dxyBundle{i},Nsd,all);
    TargetOut = [TargetOut;Dout];%Add More Data
    FeaturesOut = [FeaturesOut;Fout];
end
FS_Dataset = [FeaturesOut TargetOut];%FeatureSelectionDataset
save('db/RTransfer/FS_Dataset.mat','FS_Dataset')