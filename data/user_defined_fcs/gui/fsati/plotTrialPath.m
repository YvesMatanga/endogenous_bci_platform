function [DXY] = plotTrialPath(dxy,txy,c0xy)
%PLOTTRIALPATH get the path of the cursor to reach the target
DXY = getCursorPosition(c0xy,dxy);  
plot(DXY(:,1),DXY(:,2),'r','LineWidth',2)
hold on
scatter(txy(1),txy(2),500,'s','fill','LineWidth',2 ...
,'MarkerFaceColor','g')%target
scatter(c0xy(1),c0xy(2),'fill','LineWidth',2 ...
,'MarkerFaceColor','b')%initial cursor
hold off
grid on
grid minor
end

