function [scales] = frq2scal(frq,wavelet,fs)
%FRQ2SCAL Summary of this function goes here
%   This Function compute the scales of frequencies given the wavelet name
%   and sampling frequency fs
Fcfs = scal2frq(1,wavelet,1/fs);%scale for frequency of 1Hz
scales = Fcfs./frq;%get scales of frq
end

