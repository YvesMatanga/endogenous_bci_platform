function [scalpLevels] = eegTopoView(eegArray,mentalId)
%EEGTOPOVIEW will display amplitude level at each electrode
%mentalId  musb be a charx3
%eegArray = Nx32

mag_eegFftArray = abs(fft(eegArray));
scalpLevels = sum(mag_eegFftArray);

description = mentalId;
main_title = sprintf('IMAGERY : %s',upper(description));

plotEEGScalp32(scalpLevels);
title(main_title)
end

