function [dn] = lin_threshold(d,screen_width)
%LIN_THRESHOLD Summary of this function goes here
%   Detailed explanation goes here
H = screen_width*3/4;
dn = (d > (H/2)) + (-1)*((d < (-H/2))) + (d*2/H).*((d < (H/2))&(d > (-H/2)));
end

