function [MagHeeg] = eegfft(eegArray,bandSize,uFreq,fs)
%Compute ArModels Bands for eegArray depending on order(p) and bandsize
NCh = size(eegArray,2);%Number of Channels
N = 1024;%N of complex points for the frequencies
fres = fs/N;%frequency resolution
scale = floor(bandSize/fres);
UpperBand = ceil(uFreq/(scale*fres));
H_eeg = zeros(UpperBand,NCh);
for i=1:NCh
    Ar_eeg = fft(eegArray(:,i),N);
    %Ar_eeg = fft(eegArray(:,i),N);
    freqs = abs(Ar_eeg);
    H_eeg(:,i) = fillBands(freqs,bandSize,fres,UpperBand);%N points Complex Frequency
end
MagHeeg = H_eeg(1:UpperBand,:); %get Magnitude per Channels : NxNCh
return

function [H_eeg] = fillBands(freqs,bandSize,fres,UpperBand) 
   scale = floor(bandSize/fres);   
   H_eeg = zeros(UpperBand,1);
   for i=1:UpperBand
       H_eeg(i) = fres*sum(freqs(1+scale*(i-1):scale*i));
   end
return