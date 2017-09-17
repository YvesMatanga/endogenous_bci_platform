function [score,speed] = mlr_csm_score(beta,FeaturesBundle,dxyBundle,targetBundle,P)
%CSM_SCORE : Cumilitative Success Model Score
N = length(targetBundle);
score_temp=0;
speed = 0;
Nsd = 13;
all = true;
success_count = 0;

X=[];
for i=1:N
    %get features
    [Xt] = getDataSetx2(FeaturesBundle{i},targetBundle{i},dxyBundle{i},Nsd,all);
    X = [X;Xt];
end
Yp = mlr_regressor(beta,X);
[m,sd] = zscore_norm(Yp);

for i=1:N
    %get features
    X = getDataSetx2(FeaturesBundle{i},targetBundle{i},dxyBundle{i},Nsd,all);
    target_coords = double(bci_get_target_coord(targetBundle{i},1));
    t_t = target_coords(1);%get target position    
    %get strength of success
    Nx = length(X);
    Np = floor(Nx*P);
    t_i = sum(mlr_regressor(beta,X(1:Np,:))-m)/(sd*Nx);
    %check if cursor is moving in the right direction
    if(sign(t_i) == sign(t_t))
        score_temp = score_temp + 1;
        speed = speed + abs(t_i);
        success_count = success_count + 1;
    end
    
end
score = score_temp*100/N;
speed = speed/success_count;
end

