function [R2,SSE] = R2coef(ydata,ymodel)
%R2CEOF : Given X,Y compute the coefficient of determination
% X (NxVar) Y(Nx1)
SSE = sum((ydata-ymodel).^2);
SST =  sum((ydata - mean(ydata)).^2);
R2 = (SST - SSE)/SST;
end

