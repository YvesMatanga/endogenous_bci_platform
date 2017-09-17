function [svs,sv_coefs,b,gamma] = svr_rbf_parameters_init(NFr)
%UNTITLED4 Summary of this function goes here
%   Detailed explanation goes here
  svs = [zeros(1,NFr)];
  sv_coefs = 1;
  b = 0;
  gamma = -1;
end

