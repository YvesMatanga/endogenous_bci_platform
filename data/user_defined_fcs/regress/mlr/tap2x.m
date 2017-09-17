function [X] = tap2x(tapX,L)
%TAP2X convers taps data to their original lines
[N,Nts] = size(tapX);%N=number of samples,Nts = number of tapped column
Ns = Nts/L;%Number of sources
X = zeros(N+L-1,Ns);%Number of Data with current sources

for j=1:Ns%get previous values of first instance
    X(1:L,j) = tapX(1,(j-1)*L+1:j*L);
end
    X(1:L,:) = flipud(X(1:L,:));
    
for i=2:N
    X(L+i-1,:) = tapX(i,1:L:end);
end
end

