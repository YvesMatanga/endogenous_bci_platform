% This function converts from the bci sql format to float(uV)
function [y] = uint24ToFloat(x)
y = x*4.5e6/(2^24-1) - 2.25e6;
end

