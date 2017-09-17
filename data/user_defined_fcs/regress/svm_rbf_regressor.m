function y = svm_rbf_regressor(svs,sv_coefs,b,gamma,x)
 Ksv = length(sv_coefs);%number of support vectors coefficients
 [N,NFr] = size(x);
 temp = zeros(N,1);
 y = zeros(N,1);
    for k=1:N
     for i=1:Ksv
         temp(k) = temp(k) + sv_coefs(i)*rbfk(svs(i,:),x(k,:),gamma);
     end
         y(k) = temp(k,:) + b;%output
    end
return 

function y = rbfk(x1,x2,gamma)
 y = exp(-gamma*(norm(x1-x2))^2);%rbf kernel
return