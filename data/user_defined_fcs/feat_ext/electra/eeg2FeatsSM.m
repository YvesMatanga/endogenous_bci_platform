function [F] = eeg2FeatsSM(eeg,G_online,FGrid,fs,fps)
%EEG2FEATSSM 
Bs = electra_source_compute(G_online,eeg);%BrainSources
[N,Ns] = size(Bs);
%AR Filtering
p = 16;
df = 3;
Nf = 1024;
Tw = 0.4;%400ms
Ts = 1/fs;%8ms
Nw = floor(Tw/Ts);%window length
Nfps = floor(fs/fps);%frame length
Bs = [zeros(Nw,Ns);Bs];
%Calculate Number of Feartures
NFr = 0;
for i=1:Ns
    temp = unique(FGrid(i,:));
    temp = temp(temp~=0);
    NFr = NFr + length(temp);
end
%Feature Vector
Nht = ceil(N/Nfps);
F = zeros(Nht,NFr);
qs = 1;
qf = qs + Nw-1;
    for k=1:Nht
Bsw = Bs(qs:qf,:);%Time series Sliding Window
qs  = qs + Nfps;
qf  = qs + Nw-1;
%Ar filtering
ArpChns = eegARmodel3(Bsw,p);
q = 1;
for i=1:Ns
    temp = unique(FGrid(i,:));
    fcs = temp(temp~=0);%Features of Source i
    Arp = ArpChns(i,:);
    fp = freqz(1,Arp,Nf,fs);
    bands = bci_freq2band(abs(fp),fcs,df,fs);
    F(k,q:q+length(bands)-1) = bands;
    q = q + length(bands);
end
    end
    F = F(2:end,:);
end

