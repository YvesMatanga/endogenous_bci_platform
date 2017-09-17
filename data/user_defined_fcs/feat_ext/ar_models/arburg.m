function [a] = arburg(xn,p)
%p = order of filter
%a = coefficient by burg 's algorithm (p+1 x 1) : colum vector
%initialy
Ak = [1;zeros(p,1)];
Akp = zeros(p+1,1);
fkn=xn;%k=0
bkn=xn;%k=0
N = length(xn);
fknp = zeros(N,1);
bknp = zeros(N,1);

  for k=0:p-1       
       num = -2*sum( fkn((k+2):N).*bkn(1:(N-k-1)) );
       den = sum(fkn((k+2):N).^2) + sum(bkn(1:(N-k-1)).^2);
       u = num/den;
              
       fknp((k+2):N) = fkn((k+2):N) + u*bkn(1:(N-k-1));
       bknp(1:(N-k-1)) = bkn(1:(N-k-1)) + u*fkn((k+2):N);
       Akp(1:(k+2)) = [Ak(1:(k+1));0] + u*[0;flipud(Ak(1:(k+1)))];
              
       bkn(1:(N-k-1))  = bknp(1:(N-k-1));%load next value
       fkn((k+2):N) = fknp((k+2):N);  
       Ak(1:(k+2)) =   Akp(1:(k+2));   
  end
 a = Ak;
end