function [Y] = simple_obj(X)
%SIMPLE_OBJ Summary of this function goes here
%   Detailed eXplanation goes here
Y = (4 - 2.1*X(1)^2 + X(1)^4/3)*X(1)^2 + X(1)*X(2) + ...
       (-4 + 4*X(2)^2)*X(2)^2;
end

