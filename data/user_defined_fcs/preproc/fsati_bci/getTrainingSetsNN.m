
function [F1,D1,F2,D2,FeaturesSession_x,FeaturesSession_y,targetSession] = getTrainingSetsNN(sim_out,BufferLength,phase,dim,Nsd_in,all)
%GETTRAININGSETS ,Wanted Training Sets From Simulation Data
%Get BufferLength Latest Data

[statsSession_x,dxySession,...
    dxyRawSession,FeaturesSession_x,...
    targetSession,statsSession_y,FeaturesSession_y] = getDataFromFrameNn(sim_out,dim);

    F1=[];
    D1=[];
    F2=[];
    D2=[];

    if ~isempty(targetSession)

        if(nargin < 6)
          Nsd = 13;
        else
          Nsd = Nsd_in;
        end

        NSets = length(FeaturesSession_x);
        if NSets < BufferLength%Determine starting index
            Start = 1;
        else
            Start = (NSets-BufferLength+1);
        end
        %find duplicate and replace it
        NTr = 2;%Number of Targets
        if phase==3
            NTr = 8;
        else
            NTr = 2;
        end
        
        if Start > 1
        [id,sid] = getDuplicateAndReplaceTarget(targetSession,BufferLength,NTr,Start);
        end
        
        for i=Start:NSets
            if Start > 1
                if i==id
                    a = sid;
                else
                    a = i;
                end
            else
                a = i;
            end
            if(phase==1)%horizontal
                 [Fx,Dx] = getDataSetx2(FeaturesSession_x{a},targetSession{a},dxySession{a},Nsd,all);
                 NFr_dataset = size(Fx,1);
                 F1(end+1:end+NFr_dataset,:) = Fx;
                 D1(end+1:end+NFr_dataset,:) = Dx;
            elseif(phase==2)%vertical
                 [Fy,Dy] = getDataSety(FeaturesSession_x{a},targetSession{a},dxySession{a},Nsd,all);
                 NFr_dataset = size(Fy,1);
                 F1(end+1:end+NFr_dataset,:) = Fy;
                 D1(end+1:end+NFr_dataset,:) = Dy;
            elseif(phase==3)
                [Fx,Dx] = getDataSetx(FeaturesSession_x{a},targetSession{a},dxySession{a},Nsd,all);
                [Fy,Dy] = getDataSety(FeaturesSession_y{a},targetSession{a},dxySession{a},Nsd,all);
                NFr_dataset_x = size(Fx,1);
                NFr_dataset_y = size(Fy,1);
                F1(end+1:end+NFr_dataset_x,:) = Fx;
                D1(end+1:end+NFr_dataset_x,:) = Dx;   
                F2(end+1:end+NFr_dataset_y,:) = Fy;
                D2(end+1:end+NFr_dataset_y,:) = Dy;   
            else%default : horizontal
                 [Fx,Dx] = getDataSetx(FeaturesSession_x{a},targetSession{a},dxySession{a},Nsd,all);
                 NFr_dataset = size(Fx,1);
                 F1(end+1:end+NFr_dataset,:) = Fx;  
                 D1(end+1:end+NFr_dataset,:) = Dx;
            end
        end

    end
    
end





