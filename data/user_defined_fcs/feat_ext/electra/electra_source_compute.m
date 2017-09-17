function [BrainSources] = electra_source_compute(G,eeg)
%ELECTRA_SOURCE_COMPUTE Summary of this function goes here
%eeg = NxNelc
%G = NsxNelc
%BrainSources = (G*eeg')' = NxNs
BrainSources = (G*eeg')';
end

