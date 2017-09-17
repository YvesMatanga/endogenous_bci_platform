function [eegArray,description] = eegView(eegArray,fs,mentalId)
%EEGVIEW display trial eeg from eeg Trial struct in the fsati lab
%will  output eegArray (Nx32)
%mentalId  musb be a charx3
%eegArray = Nx32

N = length(eegArray(:,1));%get size of data
n = (0:N-1)/fs;

description = mentalId;
%display eeg of all electrodes
main_title = sprintf('IMAGERY : %s',upper(description));
figure
    for i=1:32
title_message = sprintf('%s',gtec_electrode2char(i));
subplot(4,8,i)
plot(n,eegArray(:,i))
title(title_message)
grid on
    end
annotation('textbox', [0 0.9 1 0.1], ...
    'String', main_title, ...
    'EdgeColor', 'none', ...
    'HorizontalAlignment', 'center')
end

