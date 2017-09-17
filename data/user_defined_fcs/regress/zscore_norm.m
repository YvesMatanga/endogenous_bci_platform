function [mean_f,std_f] = zscore_norm(f_dataset)
%ZCORE_NORM normlizae a feature vector [1 x nVar]
%given the dataset
%f_datasets = NxNVar
mean_f = mean(f_dataset);
std_f = std(f_dataset);
if std_f(1) == 0
  std_f = ones(1,size(f_dataset,2));%do nothing
end
%data standardized to std = 1 mean = 0
end

