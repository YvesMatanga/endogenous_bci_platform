function [c] = make_svr_rbf_parameters(svs,sv_coefs,b,gamma,MaxSvs)
%MAKE_SVR_PARAMETERS Summary of this function goes here
%   Detailed explanation goes here
[N,NFr] = size(svs);
svs_temp = [svs;zeros((MaxSvs-N),NFr)];
c = [reshape(svs_temp,[MaxSvs*NFr,1])' sv_coefs' zeros(1,(MaxSvs-N)) b gamma];
end

