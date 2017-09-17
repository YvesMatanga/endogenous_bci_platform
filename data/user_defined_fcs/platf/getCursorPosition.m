function [cxy] = getCursorPosition(c0xy,dxy)
%get cursor position calculates the cursor position from relative
%cusor movement given an initial cursor location
%dxy = Nx2 (N = number of samples)
%c0xy = 1x2
%cxy = Nx2
N = length(dxy);
cxy = zeros(N,2);
temp = c0xy;

for i=1:N
 cxy(i,:) = temp + double(int16(dxy(i,:)));%to avoid error
 temp = cxy(i,:);
end

end