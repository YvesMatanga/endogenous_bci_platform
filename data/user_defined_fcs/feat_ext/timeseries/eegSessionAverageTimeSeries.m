function [eegArray] = eegSessionAverageTimeSeries(class)
%EEGSESSIONAVERAGETIMESERIES return average time series of 
%eeg class ,A class is an array of eeg struct 
%This perform through fft accumulation
%Number of channels : 32
Nc = length(class);
N = 2^nextpow2(length(class(1).fp1));%get next power of 2 one trisl length
eegArrayF = zeros(N,32);%Number of channels 32
for i=1:Nc
  eegArrayTemp = uint24ToFloat(eegStruct2Array(class(i)));
  eegArrayF = eegArrayF + fft(eegArrayTemp,N);%Acumulation
end
  eegArray = real(ifft(eegArrayF/Nc,Nc));%level amplitude by Nc
end

