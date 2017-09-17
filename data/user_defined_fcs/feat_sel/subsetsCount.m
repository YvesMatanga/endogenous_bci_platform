function [Nsb] = subsetsCount(N)
%SUBSETSCOUNT Count the number of possible subsets given the number 
%of elements N
Nsb=0;
for i=1:N
    Nsb=Nsb+nchoosek(N,i);
end
end