function [ord] = arord(N)
%This function calculates eveluate the most appropriate
%AR model order given the number of samples for short data segments
ord_min = uint16((N/3)-1);
ord_max = uint16((N/2)-1);
%ord = floor(mean([ord_min ord_max]));
ord = double(ord_min);
end

