function [coord] = bci_get_target_coord(target_id,paradigm)
%BCI_GET_TARGET_COORD returns the coordinates of the target in the Appli
%cation software based on the paradigm in consideration
Height = 1018;%1018pixels
coord = zeros(1,2);
switch paradigm 
    case 1%horizontal 1d
         switch target_id
          case 1
              coord(1) = int16(-3*Height/8);
              coord(2) = int16(3*Height/8);
          case 2
              coord(1) = int16(-Height/8);
              coord(2) = int16(3*Height/8);
          case 3
              coord(1) = int16(Height/8);
              coord(2) = int16(3*Height/8);
          case 4
              coord(1) = int16(3*Height/8);
              coord(2) = int16(3*Height/8);
          otherwise
        end
    case 2%vertical 1d
        switch target_id
          case 1
              coord(1) = int16(3*Height/8);
              coord(2) = int16(-3*Height/8);              
          case 2
              coord(1) = int16(3*Height/8);
              coord(2) = int16(-Height/8);              
          case 3
              coord(1) = int16(3*Height/8);
              coord(2) = int16(Height/8);              
          case 4
              coord(1) = int16(3*Height/8);
              coord(2) = int16(3*Height/8);                      
          otherwise
        end
    case 3%control 2d
        switch target_id
          case 1
              coord(1) = 0;
              coord(2) = int16(3*Height/8);
          case 2
              coord(1) = int16(3*sqrt(2)*0.5*Height/8);
              coord(2) = int16(3*sqrt(2)*0.5*Height/8);
          case 3
              coord(1) = int16(3*sqrt(2)*0.5*Height/8);
              coord(2) = 0;
          case 4
              coord(1) = int16(3*sqrt(2)*0.5*Height/8);
              coord(2) = int16(-3*sqrt(2)*0.5*Height/8);
          case 5
              coord(1) = 0;
              coord(2) = int16(-3*sqrt(2)*0.5*Height/8);
          case 6      
              coord(1) = int16(-3*sqrt(2)*0.5*Height/8);
              coord(2) = int16(-3*sqrt(2)*0.5*Height/8);
          case 7      
              coord(1) = int16(-3*sqrt(2)*0.5*Height/8);
              coord(2) = 0;
          case 8
              coord(1) = int16(-3*sqrt(2)*0.5*Height/8);
              coord(2) = int16(3*sqrt(2)*0.5*Height/8);
            otherwise
         end
    otherwise
end

end

