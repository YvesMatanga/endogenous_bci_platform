function [id,sid,tgs] = getDuplicateAndReplaceTarget(targetBundle,bufferLength,NTr,index)
%GETTRAININGSETSFROMMAT Summary of this function goes here
%   Detailed explanation goes here
%index = first index 
%number of trials is 4 
%finding duplicate
id = 0;
for i=index:(bufferLength+index-1)  
  nbr=0;%count number of duplicate    
  for j=(i+1):(bufferLength+index-1)
      if targetBundle{i} == targetBundle{j}
          nbr = nbr+1;
      end
  end
  
  if nbr == ceil(bufferLength/NTr)
      id = i;
     break;
  end
end
%swap duplicate
sid = 0;

start = index-NTr;
if start <= 0
    start = 1;
end

range = start:(index-1);
range = (flipud(range'))';

for i=range%from the duplication location backward
    nbr = 0;
    for j=(index):(bufferLength+index-1)
        if targetBundle{i} == targetBundle{j}
            nbr = nbr + 1;
        end
    end
    if nbr < ceil(bufferLength/NTr)%found no duplicate
        sid=i;
        break;
    end
end
%target list
j = 1;
range =  start:(bufferLength+index-1);
tgs = zeros(1,length(range));
for i=start:(bufferLength+index-1)
    tgs(j) = targetBundle{i};
    j = j+1;
end
end
