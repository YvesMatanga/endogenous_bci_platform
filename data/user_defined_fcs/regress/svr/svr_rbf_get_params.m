function [c,gamma,predSpan] = svr_rbf_get_params(X,Y,k)
%SVR_RBF_GET_PARAMS Summary of this function goes here
%   Detailed explanation goes here
n = 0:2:20;
ctest = 2.^(-5+n);
gammatest = 2.^(-15+n);
Nt = length(n);

predSpan = zeros(Nt,Nt);
%P(C,gamma)
mse_min = 0;
for j=1:Nt
    for i=1:Nt
        [r2acc,r2pred,sse_acc,sse_pred] = svr_rbf_crossvalid(X,Y,k,gammatest(j),ctest(i));
        predSpan(i,j) = sum(sse_pred);
        if i==1 && j==1
            mse_min = predSpan(i,j);
        end
        
        if mse_min > predSpan(i,j)
            c = ctest(i);
            gamma = gammatest(j);
            mse_min = predSpan(i,j);
        end
    end
end

end

