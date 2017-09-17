function [dn] = nlin_threshold(d,screen_width)
%LIN_THRESHOLD Summary of this function goes here
%   Detailed explanation goes here
H = screen_width*3/4;
dn_max = 0.998;
p = H/(2*atanh(dn_max));
dn = tanh(d/p);
end

