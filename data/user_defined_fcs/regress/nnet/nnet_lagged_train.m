function [ffnet] = nnet_lagged_train(Xin,Y,L,hdn,tr)
%NNET_LAGGED_TRAIN Summary of this function goes here
%   Detailed explanation goes here
ffnet = feedforwardnet(hdn);
ffnet.divideParam.trainRatio = tr;
ffnet.divideParam.valRatio = 1-tr;
ffnet.divideParam.testRatio = 0;
X = x2tap(Xin,L);
ffnet = train(ffnet,X',Y');
end

