function [electrodeNames] = gtec_electrode2char(Nbr)
%GTEC_ELECTRODE2CHAR displays the name of the electrode specified
%channel lookup table
% 1 --  FP1                      % 17 --  C4
% 2 --  FP2                      % 18 --  T8
% 3 --  AF3                      % 19 --  CP5
% 4 --  AF4                      % 20 --  CP1
% 5 --  F7                       % 21 --  CP2
% 6 --  F3                       % 22 --  CP6
% 7 --  FZ                       % 23 --  P7 
% 8 --  F4                       % 24 --  P3
% 9 --  F8                       % 25 --  PZ
% 10 -- FC5                      % 26 --  P4
% 11 -- FC1                      % 27 --  P8
% 12 -- FC2                      % 28 --  PO7
% 13 -- FC6                      % 29 --  PO3
% 14 -- T7                       % 30 --  PO4
% 15 -- C3                       % 31 --  PO8
% 16 -- CZ                       % 32 --  OZ
electrodeLabels = ...
    ['FP1';'FP2';'AF3';'AF4';'F7 ';'F3 ';'FZ ';'F4 ';'F8 ';'FC5';'FC1';...
    'FC2';'FC6';'T7 ';'C3 ';'CZ ';'C4 ';'T8 ';'CP5';'CP1';'CP2';'CP6';...
    'P7 ';'P3 ';'PZ ';'P4 ';'P8 ';'PO7';'PO3';'PO4';'PO8';'OZ '];
electrodeNames = electrodeLabels(Nbr,:);
end

