function [eegFftArray,description] = eegFftView(eegArray,fs,mentalId)
%EEGVIEW display trial eeg from eegArray (Nx32)
%will  output eegArray (Nx32)
%mentalId  musb be a charx3
%eegArray = Nx32

N = length(eegArray(:,1));%get size of data
f = 0:fs/N:fs/2;

eegFftArray = fft(eegArray);
magFftArray = abs(eegFftArray);
description = mentalId;
%display eeg of all electrodes
main_title = sprintf('IMAGERY : %s',upper(description));
    for i=1:32
title_message = sprintf('%s',gtec_electrode2char(i));
subplot(4,8,i)
plot(f,magFftArray(1:length(f),i))
title(title_message)
grid on
    end
annotation('textbox', [0 0.9 1 0.1], ...
    'String', main_title, ...
    'EdgeColor', 'none', ...
    'HorizontalAlignment', 'center')
end

