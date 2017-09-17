function [time_vec] = time()
%TIME : output the current tiem of matlab ,suitable for simulink
   eml.extrinsic('now');
   eml.extrinsic('datevec');
   Y = 0;
   M = 0;
   D = 0;
   H = 0;
   MN = 0;
   S = 0;
   [Y, M, D, H, MN, S] = datevec(now);
   time_vec = [Y, M, D, H, MN, S]; 
end