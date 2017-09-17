function [fout] = mean_score(mean_f,fin)
%MEAN_SCORE Summary of this function goes here
fout = bsxfun(@minus,fin,mean_f);%matrix - vector
end

