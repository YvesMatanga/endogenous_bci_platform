function [Fx,Dx] = getDataSetxElectra(eeg,target_id,dxy_dataset,Nsd,G_online,fs,all)
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
    Nd = 6;%50ms
    N = size(eeg,1);
    nv = 1:Nd:N;
    %fs = 125;
    [b,a] = butter(6,[8/(fs/2) 30/(fs/2)],'bandpass');
    eegf = filter(b,a,eeg);
    %eegf = eegCarf(eegf);
    bs = electra_source_compute(G_online,eegf).^2;
    
    Fx = [];
    j=1;
    for i=nv
        if i~=nv(end)
            Fx(j,:) = sum(bs(i:i+Nd-1,:));
        else
            Fx(j,:) = sum(bs(i:end,:));
        end
        j=j+1;
    end
    NFl = size(Fx,1);
    target_coords = double(bci_get_target_coord(target_id,1));%get corrdinates of 
    %c0xy = [0 -target_coords(2)]; 
    %cur_dataset = getCursorPosition(c0xy,dxy_dataset);
    dx_dataset = target_coords(1)*ones(NFl,1); %- cur_dataset(:,1);%get all x's    
    %flip upside down
    %Features_dataset = flipud(Features);
    %dx_dataset = flipud(dx_dataset); 
    %ds = 1;
    %Fx = Features;%Features_dataset(1:ds:end,:);
    %NFx = size(Fx,1);
    %if NFx < Nsd
     %   Nin = NFx;
    %else
       % Nin = Nsd;
    %end
    Dx = dx_dataset;%[dx_dataset((Nsd+1):ds:end,:);target_coords(1)*ones(Nin,1)];
 end
end