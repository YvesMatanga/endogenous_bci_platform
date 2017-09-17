function [class1,class2] = eegGroupClasses(trials)
  c1 = 1;
  c2 = 1;
  Nt = length(trials);
  
  %get classes Id (unique id's)
  register = zeros(Nt+1);
  for i=1:((Nt/2)+1)
      register(i) = trials(i).taskId;
  end
  %order classes smallest first
  classes = unique(register);
  classes(find(classes==0)) = [];
  
  if classes(1) > classes(2)
      classId1 = classes(2);
      classId2 = classes(1);
  else
      classId1 = classes(1);
      classId2 = classes(2);
  end
  
  for i=1:Nt
      if(trials(i).taskId == classId1)
          class1(:,c1) = trials(i).eeg;
          c1 = c1+1;
      else
          class2(:,c2) = trials(i).eeg;
          c2 = c2+1;
      end
  end
end

