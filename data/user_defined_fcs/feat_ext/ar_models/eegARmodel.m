function [MagHeeg] = eegARmodel(eegArray,p,bandSize,uFreq,fs)
%Compute ArModels Bands for eegArray depending on order(p) and bandsize
NCh = size(eegArray,2);%Number of Channels
N = 2^nextpow2(fs/bandSize);%N of complex points for the frequencies
UpperBand = ceil(uFreq/(fs/(N)));
H_eeg = zeros(N,NCh);
for i=1:NCh
    Ar_eeg = arburg(eegArray(:,i),p);
    tmp = abs(freqz(1,Ar_eeg',N,fs));
    H_eeg(:,i) = tmp';%N points Complex Frequency
end
MagHeeg = H_eeg(1:UpperBand,:); %get Magnitude per Channels : NxNCh
end