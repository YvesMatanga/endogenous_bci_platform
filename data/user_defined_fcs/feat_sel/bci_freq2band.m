function [bands] = bci_freq2band(fp,f,df,fs)
%fp = Nx1 (number of frequency points)
%df = step per band
%f = center frequencies
[Nf] = length(fp);
NBand = length(f);
bands = zeros(1,NBand);
dn2 = ceil(Nf*df/(2*fs));%number of point for half df
fcn = floor(Nf*f/fs);%number of points per center frequencies
fcnl = fcn-dn2;%lower freq npint pe r fc
fcnu = fcn+dn2;%upper freq npoint per fc
for i=1:NBand
    bands(i) = (fs/Nf)*sum(fp(fcnl(i):fcnu(i)));
end
end

