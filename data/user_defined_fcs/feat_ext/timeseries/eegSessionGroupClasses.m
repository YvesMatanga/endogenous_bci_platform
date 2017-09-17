function [class1,class2] = eegSessionGroupClasses(eegSession)
%EEGSESSIONGROUPCLASSES group data of the session in two classes
%left hand and right hand
% eegSession is structure as described in fsati bci platform format (2016)
%yves matanga(matangangomayves@gmail.com)

 for i=1:eegSession.RunsCount
     trials = eegSession.runs(i).trials;     
     [class1Temp,class2Temp] = eegGroupClasses(trials);    
     NTr1 = length(class1Temp);
     NTr2 = length(class2Temp);
     class1((NTr1*(i-1)+1):NTr1*i) = class1Temp;
     class2((NTr2*(i-1)+1):NTr2*i) = class2Temp;
 end 
end

