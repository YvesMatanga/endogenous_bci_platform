function [dxySession] = calib2getDxyFromFrame(sim_out)
%CALIB2GETDXYFROMFRAME Summary of this function goes here
%   Detailed explanation goes here
sync = sim_out(:,end);
t0Ids = find(sync==1);
t1Ids = find(sync==2);
N0 = length(t0Ids);
N1 = length(t1Ids);
 
 if N0 > N1
     N = N1;
 else
     N = N0;
 end
 
 dxySession = cell(N,1);

for i=1:N 
 dxySession{i,:} = sim_out(t0Ids(i):t1Ids(i),1:end-1);
end

end

