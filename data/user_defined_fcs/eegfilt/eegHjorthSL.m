function [y] = eegHjorthSL(eeg)

L = ...
[ 1 -0.333 zeros(1,2) -0.333 -0.1665 -0.1665 zeros(1,25);%fp1 (f7,fp2,u(f3,fz))
 -0.333 1 zeros(1,4) -0.1665 -0.1665 zeros(1,24) ;%fp2 (f8,fp1,u(f4,fz))
 zeros(1,2) 1 -0.333 -0.333 zeros(1,5) -0.333 zeros(1,21);%af3 (af4,fc1,f7)
 zeros(1,2) -0.333 1 zeros(1,4) -0.333 zeros(1,2) -0.333 zeros(1,20);%af4 (fc2,af3,f8)
 -0.333 zeros(1,3) 1 -0.333 zeros(1,7) -0.333 zeros(1,18);%f7 (t7,f3,fp1)
  -0.25 zeros(1,3) -0.25 1 -0.25 zeros(1,7) -0.25 zeros(1,17);%f3 (fp1,f7,fz,c3)
  -0.125 -0.125 zeros(1,3) -0.25 1 -0.25 zeros(1,7) -0.25 zeros(1,16);%fz (cz,f3,f4,u(fp1,fp2))
  0 -0.25 zeros(1,4) -0.25 1 -0.25 zeros(1,7) -0.25 zeros(1,15);%f4(c4,fp2,fz,f8)
 0 -0.333 zeros(1,5) -0.333 1 zeros(1,8) -0.333 zeros(1,14);%f8 (fp2,t8,f4)
 -0.111 zeros(1,3) -0.111 -0.111 zeros(1,3) 1 -0.333 zeros(1,7) -0.333 zeros(1,13);%fc5 (cp5,fc1,u(fp1,f7,af3))
  0 0 -0.25 zeros(1,6) -0.25 1 -0.25 zeros(1,7) -0.25 zeros(1,12) ;%fc1 (cp1,fc2,af3,fc5)
  0 0 0 -0.25 zeros(1,6) -0.25 1 -0.25 zeros(1,7) -0.25 zeros(1,11) ;%fc2(cp2.fc1,fc6,af4)
  0 -0.111 zeros(1,5) -0.111 -0.111 zeros(1,2) -0.333 zeros(1,9) -0.333 zeros(1,10);%fc6 ( cp6,fc2,u(fp2,f4,f8))
  zeros(1,4) -0.333 zeros(1,8) 1 -0.333 zeros(1,7) -0.333 zeros(1,9);%t7 (p7,c3,f7)
  zeros(1,5) -0.25 zeros(1,7) -0.25 1 -0.25 zeros(1,7) -0.25 zeros(1,8);%c3(f3,p3,cz,t7)
  zeros(1,6) -0.25 zeros(1,7) -0.25 1 -0.25 zeros(1,7) -0.25 zeros(1,7);%cz(pz,fz,c4,c3)
  zeros(1,7) -0.25 zeros(1,7) -0.25 1 -0.25 zeros(1,7) -0.25 zeros(1,6);%c4(p4,f4,cz,t8)
  zeros(1,8) -0.333 zeros(1,7) -0.333 1 zeros(1,8) -0.333 zeros(1,5) ;%t8 (f8,c4,p8)
  zeros(1,9) -0.333 zeros(1,8) 1 -0.333 zeros(1,7) -0.333 zeros(1,4);%cp5 (po7,cp1,fc5)
  zeros(1,10) -0.25 zeros(1,7) -0.25 1 -0.25 zeros(1,7) -0.25 zeros(1,3);%cp1(po3,fc1,cp5,cp2)
  zeros(1,11) -0.25 zeros(1,7) -0.25 1 -0.25 zeros(1,7) -0.25 zeros(1,2);%cp2(po4,fc2,cp1,cp6)
  zeros(1,12) -0.333 zeros(1,7) -0.333 1 zeros(1,8) -0.333 0;%cp6 (po8,cp2,fc6)
  zeros(1,13) -0.333 zeros(1,8) 1 -0.333 zeros(1,3) -0.111 -0.111 zeros(1,2) -0.111;%p7 (p3,u(oz,po4,po8),t7)
  zeros(1,14) -0.25 zeros(1,7) -0.25 1 -0.25 zeros(1,2) -0.0833 -0.0833 zeros(1,2) -0.0833;%p3(p7,pz,c3,u(po3,oz,po7))
  zeros(1,15) -0.25 zeros(1,7) -0.25 1 -0.25 zeros(1,5) -0.25;%pz(p3,p4,cz,oz)
  zeros(1,16) -0.25 zeros(1,7) -0.25 1 -0.25 zeros(1,2) -0.0833 -0.0833 -0.0833;%p4(c4,pz,p8,u(po4,po8,oz))
  zeros(1,17) -0.333 zeros(1,7) -0.333 1 zeros(1,2) -0.111 -0.111 -0.111;%p8 (t8,p4,u(oz,po4,po8))
  zeros(1,13) -0.111 zeros(1,4) -0.111 zeros(1,3) -0.111 -0.333 zeros(1,3) 1 zeros(1,3) -0.333;%po7 (oz,p3,u(t7,cp5,p7))
  zeros(1,19) -0.333 zeros(1,2) -0.333 zeros(1,5) 1 -0.333 zeros(1,2);%po3 (cp1,p7,po4)
  zeros(1,20) -0.333 zeros(1,5) -0.333 0 -0.333 1 zeros(1,2);%po4 (cp2,po3,p8)
  zeros(1,17) -0.111  zeros(1,3) -0.111 zeros(1,4) -0.111 zeros(1,3) 1 0;%po8 (oz,p4,u(t9,cp6,p8))
  zeros(1,24) -0.333  zeros(1,2) -0.333 zeros(1,2) -0.333 1%oz (po7,pz,po8)
  ];

y = transpose(L*transpose(eeg));%remove average from signal
end

