function [LMP] = ts_segment(ts,fs,Ts)
%TS_SEGMENT Segment timeseries
%ts = NxVars
[N,NVar] = size(ts);
L = floor(Ts*fs);
Points = 1:L:N;
nP = length(Points);%number of points

if(Points(nP)~=N)
    LMP = zeros(nP,NVar);    
else
    LMP = zeros(nP-1,NVar);    
end

for i=1:nP-1
    LMP(i,:) = mean(ts(Points(i):Points(i+1),:));
end

if(Points(nP)~=N)
    LMP(nP,:) = mean(ts(Points(nP):Points(end),:));
end
end

