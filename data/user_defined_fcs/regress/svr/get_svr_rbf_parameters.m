function[svs,sv_coefs,b,gamma] =  get_svr_rbf_parameters(c,MaxSvs)
   %get_svr_parameters extract the svm co-oordinates from the constant c
   N = length(c);
   NFr = (N-2)/(MaxSvs)-1;
   svs_temp = reshape(c(1:NFr*MaxSvs),[MaxSvs,NFr]);
   sv_coefs_temp = c(NFr*MaxSvs+1:end-2)';
   %pruning
   nzeros = 0;
   for i=1:MaxSvs
       if sv_coefs_temp(MaxSvs+1-i) ~= 0
           break;
       end
       nzeros = nzeros + 1;
   end
   svs = svs_temp(1:MaxSvs-nzeros,:);
   sv_coefs = sv_coefs_temp(1:MaxSvs-nzeros);
   b = c(end-1);
   gamma = c(end);
end