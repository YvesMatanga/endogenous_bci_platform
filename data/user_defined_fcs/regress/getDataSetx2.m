function [Fx,Dx] = getDataSetx2(Features,target_id,dxy_dataset,Nsd,all)
%getDataSetx extract the dataset from the features by performig
%selected data choice by feedback delay
if(nargin < 5)
    allp = false;
else
    allp = all;
end
 if allp == false
    NFl = size(Features,1);
    target_coords = double(bci_get_target_coord(target_id,1));%get corrdinates of 
    c0xy = [0 -target_coords(2)];
    cur_dataset = getCursorPosition(c0xy,dxy_dataset);
    dx_dataset = target_coords(1)*ones(NFl,1) - cur_dataset(:,1);%get all x's    
    %flip upside down
    Features_dataset = flipud(Features);
    dx_dataset = flipud(dx_dataset); 
    Fx = Features_dataset((1:Nsd:end),:);
    Dx = [dx_dataset((Nsd+1):Nsd:end,:);target_coords(1)]; 
 else
    NFl = size(Features,1);
    target_coords = double(bci_get_target_coord(target_id,1));%get corrdinates of 
    %c0xy = [0 -target_coords(2)]; 
    %cur_dataset = getCursorPosition(c0xy,dxy_dataset);
    dx_dataset = target_coords(1)*ones(NFl,1); %- cur_dataset(:,1);%get all x's    
    %flip upside down
    %Features_dataset = flipud(Features);
    %dx_dataset = flipud(dx_dataset); 
    %ds = 1;
    Fx = Features;%Features_dataset(1:ds:end,:);
    %NFx = size(Fx,1);
    %if NFx < Nsd
     %   Nin = NFx;
    %else
       % Nin = Nsd;
    %end
    Dx = dx_dataset;%[dx_dataset((Nsd+1):ds:end,:);target_coords(1)*ones(Nin,1)];
 end
end