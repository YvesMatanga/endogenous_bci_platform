function [snew,A,W] = ica_post(s,z,x,xm)
%Algorithm for postprocessing of ica after unmixing
%Let s (ica sources) be a matrix mxn , m = number of observations  n = sample length
% pre-whitened and centered data ica algorithm input
%xm = 1xm (mean per column)
% W : unixing matrix
% A mixing matrix

 [M,N] = size(x); 
 
 A = z(1:M,1:M)*inv(s(1:M,1:M));%mixing matrix
 W = pinv(A);%unmixing matrix
 y = W*x;%Rtrieve data
 snew = y;
 
%  for i=1:M
%      snew(i,:)  = y(i,:) + xm(i);
%  end
end

