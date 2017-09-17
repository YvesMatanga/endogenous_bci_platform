function [perc,succIds,succTime,succTimeAll] = getxSuccessRate(targetBundle,dxyBundle,TrialIds,Mode,view)
%GETXSUCCESSRATE Summary of this function goes here
%   Detailed explanation goes here
FH = 1018;
FW = 1920;
tw = 25;
Dt = 2*sqrt(tw*0.09*(FH^2)*7/(22*16));
fr = 20.833;%frame frequency

if nargin < 5
    view_bool = false;
else
    view_bool = view;
end
N = length(TrialIds);
hit = 0;
succIds=[];
succTime = [];%taking success only in consideration
succTimeAll = [];%taking everything in consideration
for i=TrialIds
    target_coords = double(bci_get_target_coord(targetBundle{i},1));%get corrdinates of 
    c0xy = [0 -target_coords(2)];
    cxyd = getCursorPosition(c0xy,dxyBundle{i});    
    cxy = cxyd(end,:);
    
    Nid = size(cxyd,1);
    nid = (0:Nid-1)/fr;
    
    dx = abs(target_coords(1)-cxy(1));
    dy = abs(target_coords(2)-cxy(2));
    
    if Mode == 1
        if ((dx < (Dt/2)) && (dy < FH*0.045))
            hit = hit+1;
            color = [0 1 0];%success - yellow
        else
            color = [1 0 0];%failure
        end
    elseif Mode == 0
        fixd = 20;
        if ((dx < (double(int16(FH*0.09))+fixd)) && (nid(end) < 15))
            hit = hit+1;
            color = [0 1 0];%success - yellow
            succIds(end+1)=i;%get successful ids
            succTime(end+1) = length(cxyd(:,1));
            succTimeAll(end+1) = length(cxyd(:,1));
        else
            color = [1 0 0];%failure
            succTimeAll(end+1) = length(cxyd(:,1));
        end
    end
    
    if view_bool == true  
        if Mode == 0
    figure
    w = 57/fr;  
    rectangle('Position',[nid(end)-w/2,target_coords(1)-FH*0.045,w,FH*0.09],'FaceColor',color)
    hold on    
    minx = 0;
    maxx = 15+w/2;
    miny = -3*FH/8-FH*0.09;
    maxy = 3*FH/8+FH*0.09;
    wh = FH*0.09*(maxx-minx)/(maxy-miny);
    rectangle('Position',[nid(end)-wh/2,cxyd(end,1)-FH*0.045,wh,FH*0.09],'FaceColor',[0 1 0],'Curvature',[1 1]);
    plot(nid',cxyd(:,1),'b')    
    scatter(nid(end),target_coords(1),'*')
    grid on
    grid minor
    axis([minx maxx miny maxy])    
        elseif Mode == 1
    figure
    rectangle('Position',[target_coords(1)-(Dt/2),target_coords(2)-FH*0.045,Dt,FH*0.09],'FaceColor',color)
    hold on
    plot(cxyd(:,1),cxyd(:,2),'.')    
    scatter(target_coords(1),target_coords(2),'*')
    grid on
    grid minor
    axis([-FW/2 FW/2 -FH/2 FH/2])           
        end
    end
   
end
perc = hit*100/N;
end

