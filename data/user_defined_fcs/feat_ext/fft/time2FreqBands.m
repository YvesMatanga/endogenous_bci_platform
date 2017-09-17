function [xf] = time2FreqBands(eegArray,bandSize,uFreq,fs)
%TIME2FREQBANDS return frequency bands of xt timeseries
%xt = NxNt ,Nt of timeseries,N number of columns
xf=eegfft(eegArray,bandSize,uFreq,fs);
end

