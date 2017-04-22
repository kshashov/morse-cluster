function [XR]=LO(r, X0) 
    global r0;
    r0=r;
    [XR,FR22,exitflag1,output1] = fminunc(@FmoroptGH3,X0,optimset('GradObj','on','Hessian','on','TolX',2.9e-6,'MaxFunEvals',6000,'MaxIter',3000));
       
    %Xopt=[];k=1;
    %for I=1:NX
    %    XRR=[XR(k),XR(k+1),XR(k+2)];
    %    k=k+3;
    %    Xopt=[Xopt; XRR];
    %end
return;