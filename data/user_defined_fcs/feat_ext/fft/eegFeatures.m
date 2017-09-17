function [features] = eegFeatures(x,lf,uf,fs,varargin)
%Let x be an row vector of eeg trials
%features : Number of Trials x Number of features(Bands)

Nf = 2048;%Number of Samples
N = length(x);%number of trials
features = zeros(N,uf-lf+1);
df = fs/Nf;%band size
bandSize = 1;

if nargin == 5
    ch = varargin{1};
else
    ch = 15;
end
    
for i=1:N
trialEEG = x(:,i);
eegArray = eegStruct2Array(trialEEG);
eegArray = eegHjorthSL(eegArray);
%eegArray = eegCarf(eegArray);
xsig = eegArray(:,ch);
xnew = real(ifft(fft(uint24ToFloat(xsig),Nf),Nf));%Rearrange Trial
magX = abs(xnew);
 band = 1;
 for j=lf:uf
     k = 0;
     Nl = uint16(j*Nf/fs);
     if(Nl == 0)
         Nl = 1;
     end
     while((j+(k*df))<=(j))
      features(i,band) = features(i,band) + magX(Nl,:);
      Nl = Nl+1;
      k = k+1;
%       j+(k*df)
     end
     band=band+1;
 end
end

end

