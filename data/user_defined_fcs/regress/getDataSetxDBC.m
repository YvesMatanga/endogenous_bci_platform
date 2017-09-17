function [Fx,Dx] = getDataSetxDBC(eeg,dxy_dataset,G_online,fs)
%getDataSetx extract the dataset from the features by performig
%selected data choice by feedback delay

%Inverse Model
bs = electra_source_compute(G_online,eeg);%amplitudes
[N,Ns] = size(bs);%N = number of samples , Ns = Number of sources

%X-coordinates : fps = 48ms (change it to 100 ms)
Dx = dxy_dataset(:,1);

%Estimate Firing Rate per fps block

%Create 1s Window : L = 21 : Tap Data already (Delay Lines)

%Fx : NDx x L*Ns
end