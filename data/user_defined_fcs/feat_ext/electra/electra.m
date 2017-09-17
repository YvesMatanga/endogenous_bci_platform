function [G] = electra_compute_G(L,L_Loc,x)
% Preambule : function to compute contrained lead field matrix
% Gain_constrained = bst_gain_orient(HeadModel.Gain, HeadModel.GridOrient);
%---------------------------------------------
%Implementation of the Electra Algorithm 
%
%To Estimate Inner Localfiled Potentials
% L = [SxK] : S = number of Sensors/Electrodes
%             K = number of dipoles/voxels
% J = [KxN] : N = number of samples
% X = [SxN] :( x (NxS) must be transposed)
% L_Loc = [Kx3] : Dipoles Locations
%---------------------------------------------
X = x';
[S,K] = size(L);
N = length(X);%number of samples
%Computing Wd : Data Covariance Matrix
Wd = eye(N,N);
%Computing A 
A = electra_compute_A(L_Loc,N);
%Computing Wj
M = A;
Wj = inv(M'*M);
%Computing LWj-1L^T
LWL = L*inv(Wj)*L';
%Comptuing G
G = zeros(S,K);
    if det(LWL) ~= 0
        G = inv(Wj)*L'*inv(LWL);
    else
        G = inv(Wj)*L'*Wd*L*inv(L'*Wd*LWL*Wd*L)*L'*Wd;
    end
end

