function [y,mx] = center(x)
%UNTITLED4 Summary of this function goes here
%   Detailed explanation goes here
mx = mean(x);%mean value of series
y = x - mx;
end

