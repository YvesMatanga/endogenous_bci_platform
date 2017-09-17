function [F,D,Fo,Do] = lfpGetDatasets(eegBundle,G,dxyBundle,L,fs,fps)
%LFPGETDATASETS Summary of this function goes here
%   Detailed explanation goes here
    Nt = length(eegBundle);
    F = [];
    D = [];
    Fo = [];
    Do = [];
    for i=1:Nt
        eeg = eegBundle{i};
        LMPs = eeg2LMP(eeg,G,fs,fps);
        Ftemp = x2tap(LMPs,L); 
        Dtemp = dxyBundle{i};
        Fo = [Fo;LMPs];
        F = [F;Ftemp];
        D =[D;Dtemp(:,1)];
    end
    Do = D;
end

