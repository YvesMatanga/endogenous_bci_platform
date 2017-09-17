function [F] = eeg2LMP(eeg,G,fs,fps)
%EEG2LMPS Summary of this function goes here
%   Detailed explanation goes here
Bs = electra_source_compute(G,eeg);%BrainSources
[N,Ns] = size(Bs);
Nfps = floor(fs/fps);%frame length
%Feature Vector
Nht = ceil(N/Nfps);
F = zeros(Nht,Ns);
qs = 1;
qf = qs + Nfps+1;
Bs = [zeros(Nfps+1,Ns);Bs];
    for k=1:Nht
Bsw = Bs(qs:qf,:);%Time series Non-Overlapping Window
    %rectification mlp
F(k,:) = mean(Bsw);%LMPs
qs  = qs + Nfps;
qf  = qs + Nfps+1;
    end
end

