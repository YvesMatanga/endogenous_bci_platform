function [amp] = arband2(arp,bandN)
%ARBAND Summary of this function goes here
%   Detailed explanation goes here
%fres = fs/N;
%range = (bandN-1)*bandSize:fres:bandN*bandSize;
N = 1024;
bandSize = 4;
fs = 125;
switch bandN
    %band-pass filters
    case 1
amp = sum(abs(freqz(1,arp,0:fs/N:bandSize,fs)));
    case 2
amp = sum(abs(freqz(1,arp,bandSize:fs/N:2*bandSize,fs)));
    case 3
amp = sum(abs(freqz(1,arp,2*bandSize:fs/N:3*bandSize,fs)));
    case 4
amp = sum(abs(freqz(1,arp,3*bandSize:fs/N:4*bandSize,fs)));
    case 5
amp = sum(abs(freqz(1,arp,4*bandSize:fs/N:5*bandSize,fs)));
    case 6
amp = sum(abs(freqz(1,arp,5*bandSize:fs/N:6*bandSize,fs)));
    case 7
amp = sum(abs(freqz(1,arp,6*bandSize:fs/N:7*bandSize,fs)));
    case 8
amp = sum(abs(freqz(1,arp,7*bandSize:fs/N:8*bandSize,fs)));
    case 9
amp = sum(abs(freqz(1,arp,8*bandSize:fs/N:9*bandSize,fs)));
    case 10
amp = sum(abs(freqz(1,arp,9*bandSize:fs/N:10*bandSize,fs)));
    case 11
amp = sum(abs(freqz(1,arp,10*bandSize:fs/N:11*bandSize,fs)));
    case 12
amp = sum(abs(freqz(1,arp,11*bandSize:fs/N:11*bandSize,fs)));
    case 13
amp = sum(abs(freqz(1,arp,12*bandSize:fs/N:11*bandSize,fs)));
    case 14
amp = sum(abs(freqz(1,arp,13*bandSize:fs/N:11*bandSize,fs)));
    case 15
amp = sum(abs(freqz(1,arp,14*bandSize:fs/N:11*bandSize,fs)));
    case 16
amp = sum(abs(freqz(1,arp,15*bandSize:fs/N:11*bandSize,fs)));
    case 17
amp = sum(abs(freqz(1,arp,16*bandSize:fs/N:11*bandSize,fs)));
    case 18
amp = sum(abs(freqz(1,arp,17*bandSize:fs/N:11*bandSize,fs)));
    case 19
amp = sum(abs(freqz(1,arp,18*bandSize:fs/N:11*bandSize,fs)));
    case 20
amp = sum(abs(freqz(1,arp,19*bandSize:fs/N:11*bandSize,fs)));
    case 21
amp = sum(abs(freqz(1,arp,20*bandSize:fs/N:11*bandSize,fs)));
    case 22
amp = sum(abs(freqz(1,arp,10*bandSize:fs/N:11*bandSize,fs)));
    otherwise                
amp = sum(abs(freqz(1,arp,0:fs/N:bandSize,fs)));
end
amp = amp*(fs/N);
end

