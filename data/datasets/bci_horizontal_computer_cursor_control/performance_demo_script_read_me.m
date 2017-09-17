%---- << Description >>
%---- This Script runs subject performances for the five subjects that participated
%---- in horizontal computer cursor control experiment in November and December 2016 
%---- at The Tshwane University of Technology for Sensorimotor rythms based BCIs.
%---- This performance has been published in the document :
%---- "Analysis of User Control Attainment in SMR-based BCIs",
%---- Yves Matanga, Djouani Karim , Anish Kurien,IEEE Transactions on Neural Systems and Reh-
%---- Engineering, May 2017 (under review)


%--- << Prerequisites >>
%--- (1) IN MATLAB SET PATH TO THE "USER DEFINED FUNCTION FOLDER" TO MAKE USE OF CUSTOM BUILD FUNCTIONS
%--- MANDATORY FOR THE OPERATION OF THIS SCRIPT
%--- (2) SET PATH TO THE DIFFERENT DATASETS FOLDER FOR THE FIVE SUBJECTS

%--- <<This script will help fellow researchers to find out the correct trials used in the experiment, the 
%---- The subsequent features selected throughout the experiment(i.e periodic feature selection) 
%---- and finally it will better to understand the datasets structure
%----- For a Better interpretation, it should be used hand in hand with the paper mentioned above
%----- With Hope it Helps
%% clear
clc
clear
close all
%% criteria
% Success Rate per Run
% Success Rate per Session
% Target Acquisition Time

%subject list
%1.Subject 1
%2.Subject 2
%3.Subject 3
%4.Subject 4
%5.Subject 5
%6.xxxxx
%% data
subjsData = {};
s=1;
%SUBJECT 1
subjectNbr = 1;
load_files = {...
    'subject1_online_session1_date_14_11_2016_11h25',...
    'subject1_online_session2_date_15_11_2016_14h44',...
    'subject1_online_session3_date_16_11_2016_15h56',...
    'subject1_online_session4_date_18_11_2016_19h46',...
    'subject1_online_session5_date_21_11_2016_18h35',...
    'subject1_online_session6_date__24_11_2016_18h21'...
    };    
electrodes = [20];%channels
featSelSeq = {[1:4],[2,4],[2,4],[2,4],[4],[4]};%touched
trial_ids = {...
    [49:240],...
    [49:240],...
    [49:240],...
    [49:240],...
    [49:240],...
    [49:240]...
    };
subjData = {subjectNbr,load_files,trial_ids,electrodes,featSelSeq};
subjsData{s} = subjData;
s = s+1;
%SUBJECT 2
subjectNbr = 2;
load_files = {...
    'subject2_online_session1_date_8_11_2016_16h33',...
    'subject2_online_session2_date_10_11_2016_14h21',...
    'subject2_online_session3_date_14_11_2016_13h52',...
    'subject2_online_session4_date_25_11_2016_16h34',...
    'subject2_online_session5_date_28_11_2016_16h35',...
    'subject2_online_session6_date_2_12_2016_13h25'...
    };
electrodes = [15];%channels
featSelSeq = {[1:4],[2,3],[2,3],[2,3],[2,3],[2,3]};

trial_ids = {...
    [49:83,97:240],...%due to corrupted data
    [49:240],...
    [49:240],...
    [49:240],...
    [49:240],...
    [49:240]...
    };
subjData = {subjectNbr,load_files,trial_ids,electrodes,featSelSeq};
subjsData{s} = subjData;
s = s+1;
%SUBJECT 3
subjectNbr = 3;
load_files = {...
    'subject3_online_session1_date_10_11_2016_10h11',...
    'subject3_online_session2_date_15_11_2016_12h32',...
    'subject3_online_session3_date_21_11_2016_10h35',...
    'subject3_online_session4_date_22_11_2016_11h22',...
    'subject3_online_session5_date_29_11_2016_10h18',...
    'subject3_online_session6_date_2_12_2016_10h15'...
    };    
electrodes = [15];%channels
featSelSeq = {[1:4],[1:4],[1:3],[1],[1],[1]};%untouched

trial_ids = {...
    [49:216],...
    [49:240],...
    [49:168,217:240],...
    [49:240],...
    [49:240],...
    [49:240]...
    };
subjData = {subjectNbr,load_files,trial_ids,electrodes,featSelSeq};
subjsData{s} = subjData;
s = s+1;
%SUBJECT 4
subjectNbr = 4;
load_files = {...
    'subject4_online_session1_date_11_11_2016_14h17',...
    'subject4_online_session2_date_11_11_2016_17h32',...
    'subject4_online_session3_date_14_11_2016_18h53',...
    'subject4_online_session4_date_18_11_2016_12h8',...
    'subject4_online_session5_date_24_11_2016_13h57',...
    'subject4_online_session6_date_25_11_2016_13h28'...
    };
electrodes = [20];%channels
featSelSeq = {[1:4],[1,2],[2],[2],[2],[2]};%untouched

trial_ids = {...
    [49:192],...
    [49:168],...
    [49:240],...
    [49:240],...
    [49:216],...  
    [49:240]...
    };
subjData = {subjectNbr,load_files,trial_ids,electrodes,featSelSeq};
subjsData{s} = subjData;
s = s+1;
%SUBJECT 5
subjectNbr = 5;
load_files = {...
    'subject5_online_session1_date_15_11_2016_22h9',...
    'subject5_online_session2_date_18_11_2016_10h8',...
    'subject5_online_session3_date_22_11_2016_18h45',...
    'subject5_online_session4_date_24_11_2016_20h16',...
    'subject5_online_session5_date_28_11_2016_18h58',...
    'subject5_online_session6_date_3_12_2016_15h12'...
    };
electrodes = [16 22];%channels
featSelSeq = {[1:4],[1:4],[1,2,4],[1,4],[1,4],[1,4]};%untouched

trial_ids = {...
    [49:240],...    
    [49:240],...
    [49:144,169:216],...
    [49:240],...
    [49:216],...
    [49:192]...
    };
subjData = {subjectNbr,load_files,trial_ids,electrodes,featSelSeq};
subjsData{s} = subjData;
s = s+1;
%% compute success rate + target acquisition time + Spectrum
Nsb = size(subjsData,2);%Number of Subjects
subjsResults = cell(1,Nsb);
NsMax = 0;
NrMax = 0;
%------------------------------------ spectrum variables -----------------
Nf = 512;%fft Npoints
fs = 125;
p = 16;
%----------------------------------- computation -------------------------
for i=1:Nsb
    %get subject
    subjData = subjsData{i};
    load_files = subjData{2};%load files
    subjNbr = subjData{1};%subject Number  
    trial_ids = subjData{3};
    electrodes = subjData{4};%electrodes of last session
    featSelSeq = subjData{5};
    %compute
    Ns = size(load_files,2);%Number of Sessions
    Sr = zeros(Ns,1);    
    Scor = zeros(Ns,1);
    
    SFcor = cell(Ns,1);
    SFpcor= cell(Ns,1);
    
    Mode = 0;%task type
    View = false;  
    
    Srr = cell(1,Ns);%Session Run Success Rate
    SuccTimes = cell(1,Ns);
    SuccTimefs = cell(1,Ns);
    Nr = 0;%Number of Runs per Subject
    for j=1:Ns                
    load(load_files{j});%load session 
%----------------------------------------- Success Rate -------------------
    [Sr(j),SuccIds,SuccTimef,SuccTime] = getxSuccessRate(targetBundle,...
        dxyBundle,trial_ids{j},Mode,View);
    SuccTimes{j} = SuccTime;%success histogram
    SuccTimefs{j} = SuccTimef;%success histogram
%----------------------------------------- Correlation -------------------
    Scor(j) = getxCorrelation(targetBundle,dxyBundle,trial_ids{j},Mode);
    [SFcor{j},SFpcor{j}] = FxCorrelation(targetBundle,FeaturesBundle,trial_ids{j},Mode);
%--------------------------------------------------------------------------
        k = 2;
        last = trial_ids(1);
        RunSx = [];%Session Runs success Rate
        
        while trial_ids{j}(end)>=(24*k+1)                         
            IdsStart = find(trial_ids{j}<=(24*(k+1)) & trial_ids{j}>24*k );
            IdsStop = find(trial_ids{j}<=(24*(k+1)));
            startId=trial_ids{j}(min(IdsStart));
            endId =trial_ids{j}(max(IdsStop));             
            RunSx(k-1) = getxSuccessRate(targetBundle,dxyBundle,...
                startId:endId,Mode,View);%l
            %count number of runs in total           
            Nr = Nr+1;
            %update k
            k=k+1;
        end
        Srr{j} = RunSx;%Save Success Rate of Session j runs            
%----------------------------Spectrum (AR)---------------------------------
        if j==Ns
magF = zeros(Nf,32);%magnitude fft of eeg with no spatial filer
magFc = zeros(Nf,32);%magnitude fft of eeg with CAR
magFa = zeros(Nf/2,32);%magnitude fft of eeg with AR no spatial filter
magFac = zeros(Nf/2,32);%magnitude fft of eeg with CAR and AR

Nt = length(trial_ids{j});
            for id=trial_ids{j}
eeg = eegSessionBundle{id};%eeg no filtering
eegc = eegCarf(eeg);%Apply CAR
eegf = fft(eeg,Nf);
eegfc = fft(eegc,Nf);
magf = abs(eegf);
magfc = abs(eegfc);
magF = magF + magf;
magFc = magFc + magfc;
ArpChns = eegARmodel3(eeg,p);
ArpChnsc = eegARmodel3(eegc,p);
eegAf = zeros(Nf/2,32);
eegAfc = zeros(Nf/2,32);
    for m=1:32
        eegAf(:,m) = freqz(1,ArpChns(m,:),Nf/2,fs);
        eegAfc(:,m) = freqz(1,ArpChnsc(m,:),Nf/2,fs);
    end
magFa = magFa + abs(eegAf);
magFac = magFac + abs(eegAfc);
            end
magF = magF/Nt;
magFc = magFc/Nt;
magFa = magFa/Nt;
magFac = magFac/Nt;
        end        
%--------------------------------------------------------------------------    
    end
    %save subject data
    subjResults = {subjNbr,Sr,SuccTime,Srr,{magF,magFc,magFa,magFac},electrodes,...
        Scor,SuccTimes,SuccTimefs,{SFcor,SFpcor},featSelSeq};
    %Subject Nbr ,Sessions Success Rate,Runs of Session Sucess Rate
    subjsResults{i} = subjResults;%save subject to bigger list
    %standards
    if Ns > NsMax
        NsMax = Ns;
    end
    
    if Nr > NrMax
        NrMax = Nr;
    end
end
%% Display Results : Success Rate per Session + av Fit 
%clc
%----------- Polynom for best fit -----------------------------------------
SX = [];
polyOrd = 2;
%------------- Legend Settings --------------------------------------------
colors={'g--o','b--o','k--o','r-o','c--o'};
%--------------------------------------------------------------------------
figure
hold on
title('Hit Percentage per Session')
xlabel('Session Number')
ylabel('Number of Hits')
plot(1:NsMax,50*ones(1,NsMax),'r--','DisplayName','50%')
set(gca,'xtick',1:NsMax)
barData = zeros(Nsb,NsMax);
box on
for i=1:Nsb
%load subject data
subjResults = subjsResults{i};
Sr = subjResults{2};%success rate per session
Ns = size(Sr,1);%number of sessions
subjNbr = subjResults{1};%subject number
%plot
plot(1:Ns,Sr,colors{i},'DisplayName',['S',num2str(subjNbr)],'Linewidth',2)
%prepare for bar plot
barData(1:Ns,i) = Sr;
% save session data
SX(end+1:end+Ns,1:2) = [(1:Ns)',Sr];%save X-Y Part
end
%--------- get best fit average--------------------------------------------
P = polyfit(SX(:,1),SX(:,2),polyOrd);
Py = polyval(P,1:NsMax);
%--------------------------------------------------------------------------
pdata = plot(1:NsMax,Py,'r','DisplayName',['Av Growth'],'Linewidth',5);
legend('show','Location','Best','Orientation','vertical')

figure
bar(barData)
title('Hit Percentage per Session')
xlabel('Session Number')
ylabel('Percentage of Hits')
hold on
hline = refline([0 50]);
set(hline,'Color','b')
legend('show','Location','northwest','Orientation','horizontal')
box on
%% Display Results : R Correlation
%clc
%----------- Polynom for best fit -----------------------------------------
SX = [];
AvScor = zeros(Nsb,1);
ScorLast = zeros(Nsb,1);
polyOrd = 2;
%------------- Legend Settings --------------------------------------------
colors={'g--o','b--o','k--o','r-o','c--o'};
%--------------------------------------------------------------------------
figure
hold on
title('Correlation (r^2) per Session')
xlabel('Session Number')
ylabel('Correlation factor (r^2)')
set(gca,'xtick',1:NsMax)
barData = zeros(Nsb,NsMax);
box on
for i=1:Nsb
%load subject data
subjResults = subjsResults{i};
Scor = subjResults{7};%correlation rate per session
AvScor(i) = mean(abs(Scor));
ScorLast(i) = Scor(end);
Ns = size(Scor,1);%number of sessions
subjNbr = subjResults{1};%subject number
%plot
plot(1:Ns,Scor.^2,colors{i},'DisplayName',['S',num2str(subjNbr)],'Linewidth',2)
%prepare for bar plot
barData(1:Ns,i) = Scor.^2;
% save session data
SX(end+1:end+Ns,1:2) = [(1:Ns)',Scor.^2];%save X-Y Part
end
%--------- get best fit average--------------------------------------------
P = polyfit(SX(:,1),SX(:,2),polyOrd);
Py = polyval(P,1:NsMax);
%--------------------------------------------------------------------------
pdata = plot(1:NsMax,Py,'r','DisplayName',['Av Curve'],'Linewidth',5);
legend('show','Location','Best','Orientation','vertical')

figure
bar(barData)
title('Correlation (r^2) per Session')
xlabel('Session Number')
ylabel('Correlation')
hold on
legend('show','Location','northwest','Orientation','horizontal')
box on

%% Display Results : Success Rate per Runs
colors={'g--o','b--o','k--o','r-o','c--o'};
figure
hold on
title('Hit Percentage over Runs')
xlabel('Session Number')
ylabel('Percentage of Hits')
plot(1:NrMax,50*ones(1,NrMax),'r--','DisplayName','50%')
set(gca,'xtick',1:NrMax)
box on
for i=1:Nsb
%load subject data
subjResults = subjsResults{i};
Sr = subjResults{2};%success rate per session
Srr = subjResults{4};%success rate per runs
Ns = size(Sr,1);%number of sessions
subjNbr = subjResults{1};%subject number
    Runs = [];
    for k=1:Ns
        Runs(end+1:end+length(Srr{k}))= Srr{k};
    end
    Nr = length(Runs);
%plot
plot(1:Nr,Runs,colors{i},'DisplayName',['S',num2str(subjNbr)],'Linewidth',2)
end
legend('show','Location','northwest','Orientation','horizontal')

figure
hold on
title('Hit Percentage over Runs')
xlabel('Session Number')
ylabel('Number of Hits')
plot(1:NrMax,50*ones(1,NrMax),'r','DisplayName','Thresh')
set(gca,'xtick',1:NrMax)
box on
for i=1:Nsb
    %load subject data
    subjResults = subjsResults{i};
    Sr = subjResults{2};%success rate per session    
    Srr = subjResults{4};%success rate per runs
    Ns = size(Sr,1);%number of sessions
    subjNbr = subjResults{1};%subject number
    %load barData
    barData = zeros(6,8);
    for k=1:Ns
        barData(k,1:length(Srr{k}))=Srr{k};
    end
subplot(Nsb,1,i)
bar(barData,'hist')
hold on
hline = refline([0 50]);
set(hline,'Color','r')
title(['Subject ',num2str(subjNbr)])
axis([0 7 0 100])
%xlabel('Session Numbers')
%ylabel('Percentage of Hits')
end
%% Display Results : SMR AR
%clc
colors={'g','b','k','r','y'};
f = 0:fs/Nf:fs/2;
figure
hold on
title('Last session SMR Spectrum- AR(16)')
xlabel('Frequencies (Hz)')
ylabel('Amplitude')
box on
for i=1:Nsb
%load subject data
subjResults = subjsResults{i};
magSpec = subjResults{5};%subject spectrum
subjNbr = subjResults{1};%subject number
magFac = magSpec{4};%CAR + AR(16)
electrodes = subjResults{6};
%plot
elec = [];
for j=1:size(electrodes,2)
    elec(1,end+1:end+3) = gtec_electrode2char(electrodes(j));
    if j~= size(electrodes,2)
        elec(1,end+1)=',';
    end
end
%plot
if i~=5
subplot(2,Nsb,[i,i+5])
plot(f(1:Nf/2),magFac(:,electrodes),colors{i},'DisplayName',['S',num2str(subjNbr),'-',elec],'Linewidth',3)
axis([0 62.5 0 max(magFac(:,electrodes))+0.5])
legend('show','Location','Best','Orientation','vertical')
else
subplot(2,Nsb,i)
plot(f(1:Nf/2),magFac(:,electrodes(1)),colors{i},'DisplayName',['S',num2str(subjNbr),'-',elec(1,1:3)],'Linewidth',3)
axis([0 62.5 0 max(magFac(:,electrodes(1)))+0.5])
legend('show','Location','Best','Orientation','vertical')
subplot(2,Nsb,i+5)
plot(f(1:Nf/2),magFac(:,electrodes(2)),colors{i},'DisplayName',['S',num2str(subjNbr),'-',elec(1,5:7)],'Linewidth',3)
axis([0 62.5 0 max(magFac(:,electrodes(2)))+0.5])
legend('show','Location','Best','Orientation','vertical')
end

end
%--------------------------------------------------------------------------
colors={'g','b','k','r','c'};
f = 0:fs/Nf:fs/2;
for i=1:Nsb
%load subject data
subjResults = subjsResults{i};
magSpec = subjResults{5};%subject spectrum
subjNbr = subjResults{1};%subject number
magFac = magSpec{4};%CAR + AR(16)
electrodes = subjResults{6};
%plot
elec = [];
for j=1:size(electrodes,2)
    elec(1,end+1:end+3) = gtec_electrode2char(electrodes(j));
    if j~= size(electrodes,2)
        elec(1,end+1)=',';
    end
end
%plot
if i~=5
figure
hold on
title('Last session SMR Spectrum- AR(16)')
xlabel('Frequencies (Hz)')
ylabel('Amplitude')
box on
plot(f(1:Nf/2),magFac(:,electrodes),colors{i},'DisplayName',['S',num2str(subjNbr),'-',elec],'Linewidth',1.8)
axis([0 62.5 0 max(magFac(:,electrodes))+0.5])
legend('show','Location','Best','Orientation','vertical')
else
figure
hold on
title('Last session SMR Spectrum- AR(16)')
xlabel('Frequencies (Hz)')
ylabel('Amplitude')
box on
plot(f(1:Nf/2),magFac(:,electrodes(1)),colors{i},'DisplayName',['S',num2str(subjNbr),'-',elec(1,1:3)],'Linewidth',1.8)
axis([0 62.5 0 max(magFac(:,electrodes(1)))+0.5])
legend('show','Location','Best','Orientation','vertical')

figure
hold on
title('Last session SMR Spectrum- AR(16)')
xlabel('Frequencies (Hz)')
ylabel('Amplitude')
box on
plot(f(1:Nf/2),magFac(:,electrodes(2)),[colors{i},'--'],'DisplayName',['S',num2str(subjNbr),'-',elec(1,5:7)],'Linewidth',1.8)
axis([0 62.5 0 max(magFac(:,electrodes(2)))+0.5])
legend('show','Location','Best','Orientation','vertical')
end

end
%% Display Results : Target Acquisition Time
%clc
colors={'g','b','k','r','c'};
dt = 0.048;
nt = 0:0.048:16;%timespan
%-------------------------------------------------------------------------
figure
title('Last session Target Aquisition time')
xlabel('Time(s)')
ylabel('Amplitude')
box on
for i=1:Nsb
%load subject data
subjResults = subjsResults{i};
SuccTime = subjResults{3};%Target Acquisition Histogram
pd = fitdist(dt*SuccTime','Kernel','Kernel','epanechnikov');
pdfSucc  = pdf(pd,nt);
subjNbr = subjResults{1};%subject number
SuccTimes = subjResults{8};%distrubtion of target acquisition per session
Ns = length(SuccTimes);%Number of sessions
%------------ plot --------------------------------------------------------
subplot(Nsb,1,i)
plot(nt,pdfSucc,colors{i},'DisplayName',['S',num2str(subjNbr)],'Linewidth',2)
legend('show','Location','Best','Orientation','vertical')
end
%-------------------------------------------------------------------------
for i=1:Nsb
figure
title('Last session Target Aquisition time')
xlabel('Time(s)')
ylabel('Amplitude')
box on
%load subject data
subjResults = subjsResults{i};
SuccTime = subjResults{3};%Target Acquisition Histogram
Sr = subjResults{2};%success rate per session    
pd = fitdist(dt*SuccTime','Kernel','Kernel','epanechnikov');
pdfSucc  = pdf(pd,nt);
subjNbr = subjResults{1};%subject number
SuccTimes = subjResults{8};%distrubtion of target acquisition per session
Ns = length(SuccTimes);%Number of sessions
%--------------------------------------------------------------------------
mc = 1 + 0.15*randn(255,1);
pd = fitdist(mc,'Normal');%fitdist(mc,'Kernel','Kernel','epanechnikov');
pdfSuccMc  = pdf(pd,nt);
%------------ plot --------------------------------------------------------
plot(nt,pdfSucc,colors{i},'DisplayName',['S',num2str(subjNbr)],'Linewidth',2)
hold 
if i~= 4
    k=0.08;
else
    k=1.0;
end
plot(nt,k*pdfSuccMc,'k--','DisplayName','MC')
legend('show','Location','Best','Orientation','vertical')
title(sprintf('Success Rate (%%) : %.2f',Sr(end)))
end
%% Display Results : ANOVA
%clc
colors={'g','b','k','r','c'};
dt = 0.048;
nt = 0:0.048:16;%timespan
p = zeros(1,Nsb);%subjects - pvalue's ANOVA
F = zeros(1,Nsb);%subjects - pvalue's ANOVA
Fpf = zeros(1,Nsb);%subjects - pvalue's ANOVA
%-------------------------------------------------------------------------
% figure
% title('Last session Target Aquisition time')
% xlabel('Time(s)')
% ylabel('Amplitude')
% box on
for i=1:Nsb
%load subject data
subjResults = subjsResults{i};
SuccTime = subjResults{3};%Target Acquisition Histogram
% pd = fitdist(dt*SuccTime','Kernel','Kernel','epanechnikov');
% pdfSucc  = pdf(pd,nt);
subjNbr = subjResults{1};%subject number
SuccTimes = subjResults{9};%distrubtion of target acquisition per session
Ns = length(SuccTimes);%Number of sessions
%--- ANOVA Study ---------------------------------------------------------
Xgr = {};
Xd = [];
t = 1;
for k=1:Ns
    SuccT = dt*SuccTimes{k};
    Nd = length(SuccT);
    Xd(end+1:end+Nd) = SuccT;
    for q=1:Nd
        Xgr{t} = num2str(k);
        t=t+1;
    end
end
[pt,tbl] = anova1(Xd,Xgr,'on');
p(i) = tbl{2,6};
F(i) = tbl{2,5};
title(['ANOVA : Subject ',num2str(subjNbr),', F = ',num2str(F(i)),', p = ',num2str(p(i))])
xlabel('Session Number')
ylabel('Target Acquistion in Successful Trials')
%------------ plot --------------------------------------------------------
% subplot(Nsb,1,i)
% plot(nt,pdfSucc,colors{i},'DisplayName',['S',num2str(subjNbr)],'Linewidth',2)
% legend('show','Location','Best','Orientation','vertical')
end
%% Features vs Target Correlation
%clc
colors={'g','b','b--','k','r','c','c--'};
%--------------------------------------------------------------------------
polyOrd = 2;
SX = [];
rData = {};
figure
title('Features vs Target Correlation over Sessions')
xlabel('Session')
ylabel('r2')
hold on
box on
p=1;
CORP = zeros(7,6);
for i=1:Nsb
%load subject data
subjResults = subjsResults{i};
subjNbr = subjResults{1};%subject number
FT32sbj = subjResults{11};%Datasets
freqs = subjResults{10};%bands - electrodes
electrodes = subjResults{6};
Ns = size(FT32sbj,2);%Number of sessions
Nft = size(freqs,2);
r32sb = zeros(Ns,32,Nft);%correlation per subj - per elec - per features 
rp32sb = zeros(Ns,32,Nft);%correlation per subj - per elec - per features 
%--- r2 -------------------------------------------------------------------
for k=1:Ns
   FT32 = FT32sbj{k};   
   for l=1:Nft
       for m=1:32
            [rt,pt] = corrcoef(FT32(:,m,l),FT32(:,end,l));%correlation electr-tar
%           [rt,ht,pt] = pointbiserial(FT32(:,end,l)>0,FT32(:,m,l));%correlation electr-tar
           %get per feature    
           r32sb(k,m,l)=rt(2);                   
           rp32sb(k,m,l)=pt(2);                   
       end
   end
end
%--------------- plot -----------------------------------------------------
for o=1:size(freqs,2)
  ft = freqs{o};
  r = r32sb(:,ft(1),o);
  rp = rp32sb(:,ft(1),o);
  CORP(p,:) = rp;
  rData{p} = r32sb(:,:,o);
  SX(end+1:end+Ns,1:2) = [(1:Ns)',r.^2];%save X-Y Part
  plot(1:Ns,r.^2,colors{p},'DisplayName',['S',num2str(subjNbr),'--',...
      gtec_electrode2char(ft(1)),'(',num2str(ft(2)),' Hz)'],'Linewidth',2)  
  p=p+1;
end
end
%--------- get best fit average--------------------------------------------
P = polyfit(SX(:,1),SX(:,2),polyOrd);
Py = polyval(P,1:NsMax);
%--------------------------------------------------------------------------
pdata = plot(1:NsMax,Py,'r','DisplayName',['Av Curve'],'Linewidth',5);
legend('show','Location','Best','Orientation','vertical')
%-------------------------------------------------------------------------
for q=1:length(rData)
map = rData{q};
save(['topo',num2str(q),'.mat'],'map');
end
symCorp = sym(CORP);
latexCorp = latex(vpa(symCorp,5))