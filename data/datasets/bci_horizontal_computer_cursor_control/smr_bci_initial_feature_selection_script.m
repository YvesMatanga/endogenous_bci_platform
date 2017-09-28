%This M-File Will Describe The Feature Selection Approach Has Done by The 
%Wadworth Center for mu-based Brain computer Interfaces
%% clear 
clc
clear
close all
%Let SessionXY = NTrials x NBands x NElectrodes
%% Data Loading and Settings
%eegSession = Csv2BciStruct('db\calibration\bci_users\popoli_a\right_hand\popoli_a_phase1_8_11_2016_20h26.csv');
%eegSession = Csv2BciStruct('db\calibration\bci_users\obaro_az\right_hand\obaro_az_phase1_9_11_2016_11h29.csv');
%eegSession = Csv2BciStruct('db\calibration\bci_users\zanu_sp\right_hand\zanu_sp_phase1_7_11_2016.csv');
%eegSession = Csv2BciStruct('db\calibration\bci_users\dongbaare_p\left_hand_right_hand\dongbaare_p_phase1_8_11_2016_14h32.csv');
%eegSession = Csv2BciStruct('db\calibration\bci_users\mosetlhe_tc\left_right_hand\mosetlhe_tc_phase1_10_11_2016_8h04am.csv');
%eegSession = Csv2BciStruct('db\calibration\bci_users\monzambe_gm\left_hand_righ_hand\monzambe_gm_phase1_10_11_2016_16h17.csv');
eegSession = Csv2BciStruct('db\calibration\bci_users\binini_mg\left_hand_right_hand\binini_mg_phase1_15_11_2016_19h12.csv');
fs = 250;
df = 3;%frquency step
fcs = [5:df:32];%frequency of interest
Nf = 1024;%fft points
Chns = [6:8,10:13,15:17,19:22,24:26];%Central Electrodes
NCh = length(Chns);
p = 16;%16th order AR
magF = zeros(Nf,32);%magnitude fft of eeg with no spatial filer
magFc = zeros(Nf,32);%magnitude fft of eeg with CAR
magFa = zeros(Nf/2,32);%magnitude fft of eeg with AR no spatial filter
magFac = zeros(Nf/2,32);%magnitude fft of eeg with CAR and AR
%% Datasets Forming
[class1,class2] = eegSessionGroupClasses(eegSession);%separate trial in left & right
NTr = eegSession.TrialsCount;
upperBand = length(fcs);

AssociationDataClass1 = zeros(NTr/2,upperBand+1,NCh);
AssociationDataClass2 = zeros(NTr/2,upperBand+1,NCh);
AssociationData  = zeros(NTr,upperBand+1,NCh);

for i=1:(NTr/2)
 eegArrayClass1 = eegCarf(uint24ToFloat(eegStruct2Array(class1(i))));
 %eegArrayClass1 = uint24ToFloat(eegStruct2Array(class1(i)));
 eegArrayClass1 = eegArrayClass1(751:end,:);%To Remove first 3s
 class1fft = abs(fft(eegArrayClass1,Nf));
 
 eegArrayClass2 = eegCarf(uint24ToFloat(eegStruct2Array(class2(i))));
 %eegArrayClass2 = uint24ToFloat(eegStruct2Array(class2(i)));
 eegArrayClass2 = eegArrayClass2(751:end,:);%To Remove first 3s
 class2fft = abs(fft(eegArrayClass2,Nf));
 
 eegArrayClass1F = zeros(upperBand,NCh);
 eegArrayClass2F = zeros(upperBand,NCh);
 
 for k=1:NCh
  eegArrayClass1F(:,k) = bci_freq2band(class1fft(:,k),fcs,df,fs);
  eegArrayClass2F(:,k) = bci_freq2band(class2fft(:,k),fcs,df,fs);
 end 
    for j=1:NCh
       AssociationDataClass1(i,:,j)= [abs(eegArrayClass1F(1:upperBand,j))' 1];
       AssociationDataClass2(i,:,j)= [abs(eegArrayClass2F(1:upperBand,j))' -1];
       AssociationData(2*(i-1)+1,:,j) = AssociationDataClass1(i,:,j);
       AssociationData(2*(i-1)+2,:,j) = AssociationDataClass2(i,:,j);
    end
end
%save to matfile and semd to R 
SessionXY = AssociationData;
disp('... done ...')
%% Feature Selection Study
figure
[r2XY,p_XY] = bci_r2_map(SessionXY);
Ax = gca;
set(Ax,'XTick',1:NCh);
set(Ax,'YTick',1:upperBand);
strBands = num2str(fcs');
cellChns = cell(1,NCh);
cellBands = cell(1,upperBand);
for i=1:NCh
    cellChns{i} = gtec_electrode2char(Chns(i));%strChns(i,:);
end
for i=1:upperBand
    cellBands{i} = strBands(i,:);
end
set(Ax,'XTickLabel',cellChns);
set(Ax,'YTickLabel',cellBands);
xlabel('Electrodes')
ylabel('Center Frequencies (Hz)')
figure
[rXY,pr_XY] = bci_r_map(SessionXY);
Ax = gca;
set(Ax,'XTick',1:NCh);
set(Ax,'YTick',1:upperBand);
strChns = num2str(Chns');
strBands = num2str(fcs');
cellChns = cell(1,NCh);
cellBands = cell(1,upperBand);
for i=1:NCh
    cellChns{i} = gtec_electrode2char(Chns(i));%strChns(i,:);
end
for i=1:upperBand
    cellBands{i} = strBands(i,:);
end
set(Ax,'XTickLabel',cellChns);
set(Ax,'YTickLabel',cellBands);
xlabel('Electrodes')
ylabel('Center Frequencies (Hz)')
%% Statistics of Selected Channels
clc
%disp('....Please Select Channel to Display...')
%pause
Chns_out = [15 16 21 22];
class = [class1,class2];
for id=1:NTr
eeg = uint24ToFloat(eegStruct2Array(class(id)));%eeg no filtering
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
    for i=1:32
        eegAf(:,i) = freqz(1,ArpChns(i,:),Nf/2,fs);
        eegAfc(:,i) = freqz(1,ArpChnsc(i,:),Nf/2,fs);
    end
magFa = abs(eegAf);
magFac = abs(eegAfc);
end
magF = magF/NTr;
magFc = magFc/NTr;
magFa = magFa/NTr;
magFac = magFac/NTr;
%spectral avearge
f = 0:fs/Nf:fs/2;
%spectral average - plots
legendText = gtec_electrode2char(Chns_out);
figure
subplot(4,1,1)
plot(f,magFc(1:length(f),Chns_out))
grid on
grid minor
title('Common Average FFT')
legend(legendText)
subplot(4,1,2)
plot(f,magF(1:length(f),Chns_out))
title('No Spatial Filter FFT')
grid on
grid minor
legend(legendText)
subplot(4,1,3)
plot(f(1:(length(f)-1)),magFac(:,Chns_out))
title('Common Average - Autoregressive Model(16)')
grid on
grid minor
legend(legendText)
subplot(4,1,4)
plot(f(1:(length(f)-1)),magFa(:,Chns_out))
title('No Spatial Filter - Autoregressive Model(16)')
grid on
grid minor
legend(legendText)

%% r2 per electrodes
NChOut = length(Chns_out);
for i=1:NChOut
    r2ch = r2XY(:,find(Chns==Chns_out(i)));
    figure
    plot(1:upperBand,r2ch)
    xlabel('Frequency (Hz)')
    ylabel('r2')
    Ax = gca;
    set(Ax,'XTick',1:upperBand);    
    strBands = num2str(fcs');    
    cellBands = cell(1,upperBand);    
    for k=1:upperBand
        cellBands{k} = strBands(k,:);
    end
    set(Ax,'XTickLabel',cellBands);    
    xlabel('Bands')
    ylabel('r2')
    grid minor
    legend(gtec_electrode2char(Chns_out(i)))
end
