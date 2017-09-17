function [Ar_eeg] = eegARmodel3(eegArray,p)
%Compute ArModels Bands for eegArray depending on order(p) and bandsize
NCh = size(eegArray,2);%Number of Channels
% N = 1024;%N of complex points for the frequencies
% fres = fs/N;%frequency resolution
% scale = floor(bandSize/fres);
% UpperBand = ceil(uFreq/(scale*fres));
% H_eeg = zeros(UpperBand,NCh);
Ar_eeg = zeros(NCh,p+1);
for i=1:NCh
    Ar_eeg(i,:) = arburg(eegArray(:,i),p);   
    %freqs = abs(freqz(1,Ar_eeg',N/2,fs));
    %H_eeg(:,i) = fillBands(freqs,fres,UpperBand,scale);%N points Complex Frequency
end
%MagHeeg = H_eeg(1:UpperBand,:)/(N^2); %get Magnitude per Channels : NxNCh
end

% function [H_eeg] = fillBands(freqs,fres,UpperBand,scale) 
%    H_eeg = zeros(UpperBand,1);
%    for i=1:UpperBand
%        H_eeg(i) = fres*sum(freqs(1+scale*(i-1):scale*i));
%    end
% return

