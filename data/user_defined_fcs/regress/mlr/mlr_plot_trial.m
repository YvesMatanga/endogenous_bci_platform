function [cxy] = mlr_plot_trial(F,tid,dy,b,a,W,c)
%MLR_PLOT_TRIAL Summary of this function goes here
%   Detailed explanation goes here
 if nargin < 7
     d='b';
 else
     d=c;
 end
 [N] = size(F,1);
 cxy = zeros(N,2);
 
 FH = 1018;
 FW = 1920;
 tw = 14.7;
 Dt = 2*sqrt(tw*0.09*(FH^2)*7/(22*16)); 
 target_coords = double(bci_get_target_coord(tid,1));
     
 for i=1:N
     Oo = -i*b*a;
     In = ones(i,1);
     cxy(i,1) = Oo + b*(F(1:i,:)*W)'*In;
     cxy(i,2) = dy*i-381;
 end

 dx = abs(target_coords(1)-cxy(end,1));
 dy = abs(target_coords(2)-cxy(end,2));

 dxTest = (dx < (Dt/2));
 dyTest = (dy < FH*0.045);
 
 if (dxTest && dyTest)    
    color = [0 1 0];%success - yellow
 else
    color = [1 0 0];%failure
 end
    
 %figure
 rectangle('Position',[target_coords(1)-(Dt/2),target_coords(2)-FH*0.045,Dt,FH*0.09],'FaceColor',color)
 hold on
 scatter(cxy(:,1),cxy(:,2),[d,'.'])    
 scatter(target_coords(1),target_coords(2),'b*')
 grid on
 axis([-FW/2 FW/2 -FH/2 FH/2]) 
end

