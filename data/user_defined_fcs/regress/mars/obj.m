function [lof] = obj(X,Y,Am,SKM,DIM,TKM)
%OBJ : Objective Function
Ye = mars_rgr(X,Am,SKM,DIM,TKM);
SSE = sum((Y-Ye).^2);
SST = sum((Y - mean(Y)).^2);
lof = SST/(SST-SSE);
end

