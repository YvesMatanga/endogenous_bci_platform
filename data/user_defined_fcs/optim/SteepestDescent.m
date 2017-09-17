% 	
% Ch 6: Numerical Techniques for Unconstrained Optimization
% Optimzation with MATLAB, Section 6.3.1
% Steepest Descent Method
% copyright (code) Dr. P.Venkataraman
%	
% An m-file for the Steepest Descent Method
%************************************
% requires:     	UpperBound_nVar.m
%						GoldSection_nVar.m
%	and the problem m-file: Example6_1.m
% 
%***************************************
%
% the following information are passed to the function

% the name of the function 			'functname'
% functname.m : returns scalar for vector input
%
% the gradient calculation is in  gradfunction.m
% gradfunction.m:  returns vector for vector input
%
%	initial design vector					dvar0
%  number of iterations                niter

%------for golden section
%  the tolerance (for golden section)	tol 
%
%-------for upper bound calculation
% the initial value of stepsize			lowbound
% the incremental value 					intvl
% the number of scanning steps	    	ntrials
%
% the function returns the final design and the objective function

%	sample callng statement

% SteepDescent'Example6_1',[0.5 0.5],20, 0.0001, 0,1 ,20)
%
function ReturnValue = SteepestDescent(functname, ...
   	dvar0,niter,tol,lowbound,intvl,ntrials)
   
clf % clear figure
e3 = 1.0e-08;   
nvar = length(dvar0); % length of design vector or number of variables
% obtained from start vector 
if (nvar == 2)
%*******************
%  plotting contours
%*******************

% for 2 var problems the contour plot can be drawn
	x1 = 0:0.1:5;
	x2 = 0:0.1:5;
	x1len = length(x1);
	x2len = length(x2);
	for i = 1:x1len;
   	for j = 1:x2len;
      	x1x2 =[x1(i) x2(j)];
      	fun(j,i) = feval(functname,x1x2);
   	end
	end

	c1 = contour(x1,x2,fun, ...
   	[3.1 3.25 3.5 4 6 10 15 20 25],'k');
	%clabel(c1); % remove labelling to mark iteration
	grid
	xlabel('x_1')
	ylabel('x_2')
	% replacing _ by - in the function nane
    funname = strrep(functname,'_','-');
    
    % adding file name to the title
    title(strcat('Steepest Descent Using :',funname));
    
%*************************
% finished plotting contour
%*************************

% note that contour values are problem dependent
% the range is problem dependent
end

%*********************
%  Numerical Procedure
%*********************
% design vector, alpha , and function value is stored
xs(1,:) = dvar0;
x = dvar0;
Lc = 'r';
fs(1) = feval(functname,x); % value of function at start
as(1)=0;
s = -(gradfunction(functname,x)); % steepest descent

convg(1)=s*s';
for i = 1:niter-1
   % determine search direction
  
   output = GoldSection_nVar(functname,tol,x, ...
      s,lowbound,intvl,ntrials);
   
   as(i+1) = output(1);
   fs(i+1) = output(2);
   for k = 1:nvar
      xs(i+1,k)=output(2+k);
      x(k)=output(2+k);
   end
   s = -(gradfunction(functname,x)); % steepest descent
   convg(i+1)=s*s';
   %***********
   % draw lines
   %************
   
   if (nvar == 2)
      line([xs(i,1) xs(i+1,1)],[xs(i,2) xs(i+1,2)],'LineWidth',2, ...
         'Color',Lc)
      itr = int2str(i);
      x1loc = 0.5*(xs(i,1)+xs(i+1,1));
      x2loc = 0.5*(xs(i,2)+xs(i+1,2));
      %text(x1loc,x2loc,itr); 
      % writes iteration number on the line
   if strcmp(Lc,'r') 
         Lc = 'k';
   else
         Lc = 'r';
   end
        
    pause(1)  
   %***********************
   % finished drawing lines
   %***********************
   end
   if(convg(i+1)<= e3) break; end; % convergence criteria  
   
   %***************************************
   %  complete the other stopping criteria
   %****************************************
end
len=length(as);
%for kk = 1:nvar
designvar=xs(length(as),:);

fprintf('The problem:  '),disp(functname)
fprintf('\n - The design vector,function value and KT value \nduring the iterations\n')
disp([xs fs' convg'])
ReturnValue = [designvar fs(len)];