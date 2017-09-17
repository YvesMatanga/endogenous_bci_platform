function [eegSession] = getSessionFromFrame(eeg_frame)
%GET_SESSION_RETRIEVAL Summary of this function goes here
%   Detailed explanation goes here
sync = eeg_frame(:,33);
t0Ids = find(sync==1);
t1Ids = find(sync==2);
N0 = length(t0Ids);
N1 = length(t1Ids);
 
 if N0 > N1
     N = N1;
 else
     N = N0;
 end
 
 eegSession = cell(N,1);

for i=1:N 
 eegSession{i,:} = eeg_frame(t0Ids(i):t1Ids(i),1:32);
end

end

