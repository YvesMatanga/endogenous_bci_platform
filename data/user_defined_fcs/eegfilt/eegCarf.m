function [y,av] = eegCarf(eeg)
%eeg is a matrix of MxN where 
% N is the number of channels and M the number 
% of observations per channels
%N = number of channels
[M,N] = size(eeg);
%eeg signal
car = sum(eeg,2)/N;%average per row
temp = zeros(M,N);
    for i = 1:N
        temp(:,i) = eeg(:,i) - car;
    end
y = temp;%remove average from signal
av = car;
end