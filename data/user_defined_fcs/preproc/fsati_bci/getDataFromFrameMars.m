function [modelParametersSession_x,statsSession_x,dxySession,...
    dxyRawSession,FeaturesSession_x,...
    targetSession,modelParametersSession_y,statsSession_y,FeaturesSession_y] = getDataFromFrameMars(sim_out,MaxBasis,dim)
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

dxySession = cell(N,1);
dxyRawSession = cell(N,1);
targetSession = cell(N,1);
statsSession_x = cell(N,1);
modelParametersSession_x = cell(N,1);
FeaturesSession_x = cell(N,1);
statsSession_y = cell(N,1);
modelParametersSession_y = cell(N,1);
FeaturesSession_y = cell(N,1);

 if dim == 1
NMarsCoeffs = MaxBasis*4 +1;
NFr = NCs - NMarsCoeffs - 7;

for i=1:N 
modelParametersSession_x{i,:} = unique(sim_out(t0Ids(i):t1Ids(i),1:NMarsCoeffs),'rows');
statsSession_x{i,:} = unique(sim_out(t0Ids(i):t1Ids(i),NMarsCoeffs+1:NMarsCoeffs+2),'rows');
dxySession{i,:} = sim_out(t0Ids(i):t1Ids(i),NMarsCoeffs+3:NMarsCoeffs+4);
dxyRawSession{i,:} = sim_out(t0Ids(i):t1Ids(i),NMarsCoeffs+5:NMarsCoeffs+6);
FeaturesSession_x{i,:} = sim_out(t0Ids(i):t1Ids(i),NMarsCoeffs+7:NMarsCoeffs+7+NFr-1);
targetSession{i,:} = unique(sim_out(t0Ids(i):t1Ids(i),NMarsCoeffs+7+NFr),'rows');
end

 else %dim == 2

sim_out_sample = sim_out(1,:);
InfIds = find(sim_out_sample==Inf);
NModel_x = InfIds(1)-3;
NModel_y = InfIds(2)-InfIds(1)-1;
NF_x = InfIds(3)-InfIds(2)-1;
NF_y = NCs-InfIds(3)-1;

for i=1:N 
modelParametersSession_x{i,:} = unique(sim_out(t0Ids(i):t1Ids(i),1:NModel_x),'rows');
statsSession_x{i,:} = unique(sim_out(t0Ids(i):t1Ids(i),NModel_x+1:NModel_x+2),'rows');
modelParametersSession_y{i,:} = unique(sim_out(t0Ids(i):t1Ids(i),NModel_x+4:NModel_x+NModel_y+3),'rows');
statsSession_y{i,:} = unique(sim_out(t0Ids(i):t1Ids(i),NModel_x+NModel_y+4:NModel_x+NModel_y+5),'rows');

dxySession{i,:} = sim_out(t0Ids(i):t1Ids(i),NModel_x+NModel_y+6:NModel_x+NModel_y+7);
dxyRawSession{i,:} = sim_out(t0Ids(i):t1Ids(i),NModel_x+NModel_y+8:NModel_x+NModel_y+9);
FeaturesSession_x{i,:} = sim_out(t0Ids(i):t1Ids(i),NModel_x+NModel_y+11:NModel_x+NModel_y+NF_x+10);
FeaturesSession_y{i,:} = sim_out(t0Ids(i):t1Ids(i),NModel_x+NModel_y+NF_x+12:NModel_x+NModel_y+NF_x+NF_y+11);
targetSession{i,:} = unique(sim_out(t0Ids(i):t1Ids(i),end-1),'rows');
end

end

end