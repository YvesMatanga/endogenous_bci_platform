function [Fy,Dy] = getDataSety(Features,target_id,dxy_dataset,Nsd)
%getDataSety extract the dataset from the features by performig
%selected data choice by feedback delay
    NFl = size(Features,1);
    target_coords = double(bci_get_target_coord(target_id,2));%get corrdinates of 
    c0xy = [-target_coords(1) 0];
    cur_dataset = getCursorPosition(c0xy,dxy_dataset);
    dy_dataset = target_coords(2)*ones(NFl,1) - cur_dataset(:,2);%get all x's    
    %flip upside down
    Features_dataset = flipud(Features);
    dy_dataset = flipud(dy_dataset); 
    Fy = Features_dataset((1:Nsd:end),:);
    Dy = [dy_dataset((Nsd+1):Nsd:end,:);target_coords(2)]; 
end