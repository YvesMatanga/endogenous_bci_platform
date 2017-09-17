function [p_values,tscores] = mlr_ttest(ymlr,ytarget,x,beta)
% perform t_test to check if a given variable is statiscally significant or
% not :
% ymlr : predicted target by model (Nx1)
% y    : actual target from data (Nx1)
% x    : data (NxVar) ,Var = Number of Variables
%Requirements : stixbox statistics toolbox (for student distribution cdf)
[N NVar]=size(x);
MSE = sum(((ymlr-ytarget).^2)')/N;
X = [ones(1,N);x']';%put the data in mlr direct regression solution format
C = MSE*inv(X'*X);%variance-covariance matrix : diagonal represent variance 
%estimates of betaj's
diag_C = diag(C);
Sebj = sqrt(diag_C(2:(NVar+1)));%Get Sebj per Bjs
tscores = beta(2:(NVar+1))./Sebj; 
p_values = 2*(1-pt(abs(tscores),N-2));%p-value
end

