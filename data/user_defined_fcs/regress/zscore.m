function [fout] = zscore(f,mean_f,std_f)
%ZSCORE evaluate teh standard vaue of f given meanf and std
fout = bsxfun(@rdivide,bsxfun(@minus,f,mean_f),std_f);
end

