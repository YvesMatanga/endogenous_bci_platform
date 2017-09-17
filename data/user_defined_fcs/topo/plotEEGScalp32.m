function [Levels] = plotEEGScalp32(Levels,varargin)
%plotEEGScalp32 display the scalp topography for 32 electrodes
minL = min(Levels);
maxL = max(Levels);

if length(varargin)==2
 minL = varargin{1};
 maxL = varargin{2}; 
end

% Levels Must be 1x32 matrix
ScalpLevels = zeros(1,64);%%zeros every where
%ScalpLevels(1:2:64) = Levels;

ScalpLevels(22) = Levels(1);%fp1
ScalpLevels(24) = Levels(2);%fp2
ScalpLevels(26) = Levels(3);%af3
ScalpLevels(28) = Levels(4);%af4
ScalpLevels(30) = Levels(5);%f7
ScalpLevels(32) = Levels(6);%f3
ScalpLevels(34) = Levels(7);%fz
ScalpLevels(36) = Levels(8);%f4
ScalpLevels(38) = Levels(9);%f8
ScalpLevels(1) = Levels(10);%fc5
ScalpLevels(3) = Levels(11);%fc1
ScalpLevels(5) = Levels(12);%fc2
ScalpLevels(7) = Levels(13);%fc6
ScalpLevels(41) = Levels(14);%t7
ScalpLevels(9) = Levels(15);%c3
ScalpLevels(11) = Levels(16);%cz

ScalpLevels(13) = Levels(17);%c4
ScalpLevels(42) = Levels(18);%t8
ScalpLevels(15) = Levels(19);%cp5
ScalpLevels(17) = Levels(20);%cp1
ScalpLevels(19) = Levels(21);%cp2
ScalpLevels(21) = Levels(22);%cp6
ScalpLevels(47) = Levels(23);%p7
ScalpLevels(49) = Levels(24);%p3
ScalpLevels(51) = Levels(25);%pz
ScalpLevels(53) = Levels(26);%p4
ScalpLevels(55) = Levels(27);%p8
ScalpLevels(56) = Levels(28);%po7
ScalpLevels(57) = Levels(29);%po3
ScalpLevels(59) = Levels(30);%po4
ScalpLevels(60) = Levels(31);%po8
ScalpLevels(62) = Levels(32);%oz

topoplotEEG(ScalpLevels,'eloc64.txt','gridscale',150);

caxis([minL maxL])
colorbar
set(gca,'LineWidth',2,'TickLength',[0.025 0.025], 'fontsize',18)
end

