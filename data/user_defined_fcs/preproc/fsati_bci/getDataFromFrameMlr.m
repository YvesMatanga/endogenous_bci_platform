function [betaSession_x,statsSession_x,dxySession,...
    dxyRawSession,FeaturesSession_x,...
    targetSession,betaSession_y,statsSession_y,FeaturesSession_y] = getDataFromFrameMlr(sim_out,dim)
%GET_SESSION_RETRIEVAL Summary of this function goes here
%   Detailed explanation goes here
sync = sim_out(:,end);
t0Ids = find(sync==1);
t1Ids = find(sync==2);
N0 = length(t0Ids);
N1 = length(t1Ids);
NCs = size(sim_out,2)-1; 

 if N0 > N1
     N = N1;
 else
     N = N0;
 end

statsSession_x = cell(N,1);
betaSession_x = cell(N,1);
dxySession = cell(N,1);
dxyRawSession = cell(N,1);
FeaturesSession_x = cell(N,1);
targetSession = cell(N,1);
statsSession_y = cell(N,1);
betaSession_y = cell(N,1);
FeaturesSession_y = cell(N,1);

 if dim == 1

NFr = floor((NCs - 7)/2);
NMLrCoeffs = NCs - NFr - 7; 


for i=1:N 
betaSession_x{i,:} = unique(sim_out(t0Ids(i):t1Ids(i),1:NMLrCoeffs),'rows');
statsSession_x{i,:} = unique(sim_out(t0Ids(i):t1Ids(i),NMLrCoeffs+1:NMLrCoeffs+2),'rows');
dxySession{i,:} = sim_out(t0Ids(i):t1Ids(i),NMLrCoeffs+3:NMLrCoeffs+4);
dxyRawSession{i,:} = sim_out(t0Ids(i):t1Ids(i),NMLrCoeffs+5:NMLrCoeffs+6);
FeaturesSession_x{i,:} = sim_out(t0Ids(i):t1Ids(i),NMLrCoeffs+7:NMLrCoeffs+7+NFr-1);
targetSession{i,:} = unique(sim_out(t0Ids(i):t1Ids(i),NMLrCoeffs+7+NFr),'rows');
end
 
 else % dim == 2
sim_out_sample = sim_out(1,:);
InfIds = find(sim_out_sample==Inf);
Nbeta_x = InfIds(1)-3;
Nbeta_y = InfIds(2)-InfIds(1)-1;
NF_x = InfIds(3)-InfIds(2)-1;
NF_y = NCs-InfIds(3)-1;

for i=1:N 
betaSession_x{i,:} = unique(sim_out(t0Ids(i):t1Ids(i),1:Nbeta_x),'rows');
statsSession_x{i,:} = unique(sim_out(t0Ids(i):t1Ids(i),Nbeta_x+1:Nbeta_x+2),'rows');
betaSession_y{i,:} = unique(sim_out(t0Ids(i):t1Ids(i),Nbeta_x+4:Nbeta_x+Nbeta_y+3),'rows');
statsSession_y{i,:} = unique(sim_out(t0Ids(i):t1Ids(i),Nbeta_x+Nbeta_y+4:Nbeta_x+Nbeta_y+5),'rows');

dxySession{i,:} = sim_out(t0Ids(i):t1Ids(i),Nbeta_x+Nbeta_y+6:Nbeta_x+Nbeta_y+7);
dxyRawSession{i,:} = sim_out(t0Ids(i):t1Ids(i),Nbeta_x+Nbeta_y+8:Nbeta_x+Nbeta_y+9);
FeaturesSession_x{i,:} = sim_out(t0Ids(i):t1Ids(i),Nbeta_x+Nbeta_y+11:Nbeta_x+Nbeta_y+NF_x+10);
FeaturesSession_y{i,:} = sim_out(t0Ids(i):t1Ids(i),Nbeta_x+Nbeta_y+NF_x+12:Nbeta_x+Nbeta_y+NF_x+NF_y+11);
targetSession{i,:} = unique(sim_out(t0Ids(i):t1Ids(i),end-1),'rows');
end

 end

end