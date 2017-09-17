function [A] = electra_compute_A(L_Loc,N)
%This Function Compute The Matrix A using
% The Lead Filed Matrix Sources Location (x,y,z)
% N = Number of Neighbours (Max = 26)
% L_Loc = [K x 3] , K = number of  sources  3 (x,y,z)

K = length(L_Loc);%get number of sources
A = zeros(K,K);%Iniatialize A

%Create an exaustive search clustering method
    %Let Mdl The Exhaustive Searcher
Mdl = createns(L_Loc,'NSMethod','exhaustive','Distance','euclidean');
Neighbour_Ids = knnsearch(Mdl,L_Loc,'K',N+1);
%N+1 because the algorithm consider the point as its own neighbor
    
    for i=1:K
         %Remove Redundancy
        temp = Neighbour_Ids(i,:);
        Neighbour_Ids(i,:) = temp(temp~=i);%Remove the element as its own neighbour
        Neighbour_Ids(i,:) = electra_vicinity(L_Loc,Neighbour_Ids(i,:));%get true vicinity
        Ni = length(Neighbour_Ids(i,:));%Number of elts in true vicinity
         %find Aii        
            if i==j
        Vi = L_Loc(Neighbour_Ids(i,:));%get all the neighbours to i
        temp = 0;
        for k=1:Ni
            temp = temp + norm([L_Loc(i)' Vi(k)'])^(-2);
        end
        A(i,i) = (N/Ni)*temp;
        %find Aik
            else
                A(i,j) = -norm([L_Loc(i)' L_Loc(j)'])^(-2);
            end
    end    
   
end