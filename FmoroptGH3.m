function [FR,g,H]=FmoroptGH3(X0)
global r0;
%global Ntop;
%global X;
%global St;
global N9 N100;
Nopt=length(X0);
%Nopt=Nopt/3;
Ntop = Nopt/3;
FR=0;
k=1;
for I=1:Ntop
    %if St(I)==1
        X(I,1)=X0(k);X(I,2)=X0(k+1);X(I,3)=X0(k+2);
        k=k+3;
   % end
end
for J=1:Ntop
    XJ=[X(J,1);X(J,2);X(J,3)];
    for I=J+1:Ntop
        XI=[X(I,1);X(I,2);X(I,3)];
        R=(XJ-XI).^2;  R=(R(1)+R(2)+R(3))^0.5;
        if R<0.99
            N9=N9+1;
        end
        if R>=0.99 && R<1.04
            N100=N100+1;
        end
        F=exp(r0*(1-R))*(exp(r0*(1-R))-2);
        FR=FR+F;
    end
end

% Вычисление градиента
g=zeros(1,Nopt);
for K=1:Ntop
    for I=1:Ntop
        Xk=[X(K,1);X(K,2);X(K,3)];
        if K~=I
            Xi=[X(I,1);X(I,2);X(I,3)];
            Rki=(Xk-Xi).^2;  Rki=(Rki(1)+Rki(2)+Rki(3))^0.5;
           Aki_l=[sign(Xk(1)-Xi(1))*abs(Xk(1)-Xi(1))/Rki;sign(Xk(2)-Xi(2))*abs(Xk(2)-Xi(2))/Rki;sign(Xk(3)-Xi(3))*abs(Xk(3)-Xi(3))/Rki];
            Fki=2*r0*(exp(r0*(1-Rki))-exp(2*r0*(1-Rki)));
            g(3*(K-1)+1)=g(3*(K-1)+1)+Fki*Aki_l(1);
            g(3*(K-1)+2)=g(3*(K-1)+2)+Fki*Aki_l(2);
            g(3*(K-1)+3)=g(3*(K-1)+3)+Fki*Aki_l(3);
        end
    end
end

% Вычисление гессиана
H=zeros(Nopt,Nopt);
Hb=zeros(Nopt,Nopt);h=0.000001;
for i1=1:Nopt
    k=ceil(i1/3);  L=i1-(k-1)*3;
    for j1=i1:Nopt
        p=ceil(j1/3);  r=j1-(p-1)*3;
        Xk=[X(k,1);X(k,2);X(k,3)];
        Xp=[X(p,1);X(p,2);X(p,3)];
        Rkp=(Xk-Xp).^2;  Rkp=(Rkp(1)+Rkp(2)+Rkp(3))^0.5;
        Fkp=2*r0*(exp(r0*(1-Rkp))-exp(2*r0*(1-Rkp)));
        Pkp=2*(r0^2)*(2*exp(2*r0*(1-Rkp))-exp(r0*(1-Rkp)));
        %проверка
        OTL=0;
        if OTL==1
        if k==p && L==r
            X3=X; X3(k,L)=X3(k,L)+h; [FR3,aFR1,aFs,aNt]=Fmoros(X3);
            X2=X;                    [FR2,aFR1,aFs,aNt]=Fmoros(X2);
            X1=X; X1(k,L)=X1(k,L)-h; [FR1,aFR1,aFs,aNt]=Fmoros(X1);
            Hb(i1,j1)=(FR3-2*FR2+FR1)/h^2;
        elseif k~=p && L==r
            X4=X; X4(k,L)=X4(k,L)+h;X4(p,L)=X4(p,L)+h;
            [FR4,aFR1,aFs,aNt]=Fmoros(X4);
            X3=X; X3(k,L)=X3(k,L)+h;X3(p,L)=X3(p,L)-h;
            [FR3,aFR1,aFs,aNt]=Fmoros(X3);
            X2=X; X2(k,L)=X2(k,L)-h;X2(p,L)=X2(p,L)+h;
            [FR2,aFR1,aFs,aNt]=Fmoros(X2);
            X1=X; X1(k,L)=X1(k,L)-h;X1(p,L)=X1(p,L)-h;
            [FR1,aFR1,aFs,aNt]=Fmoros(X1);
            Hb(i1,j1)=(FR4-FR3-FR2+FR1)/(4*h^2);
        elseif k==p && L~=r
            X4=X; X4(k,L)=X4(k,L)+h;X4(k,r)=X4(k,r)+h;
            [FR4,aFR1,aFs,aNt]=Fmoros(X4);
            X3=X; X3(k,L)=X3(k,L)+h;X3(k,r)=X3(k,r)-h;
            [FR3,aFR1,aFs,aNt]=Fmoros(X3);
            X2=X; X2(k,L)=X2(k,L)-h;X2(k,r)=X2(k,r)+h;
            [FR2,aFR1,aFs,aNt]=Fmoros(X2);
            X1=X; X1(k,L)=X1(k,L)-h;X1(k,r)=X1(k,r)-h;
            [FR1,aFR1,aFs,aNt]=Fmoros(X1);
            Hb(i1,j1)=(FR4-FR3-FR2+FR1)/(4*h^2);
        elseif k~=p && L~=r
            X4=X; X4(k,L)=X4(k,L)+h;X4(p,r)=X4(p,r)+h;
            [FR4,aFR1,aFs,aNt]=Fmoros(X4);
            X3=X; X3(k,L)=X3(k,L)+h;X3(p,r)=X3(p,r)-h;
            [FR3,aFR1,aFs,aNt]=Fmoros(X3);
            X2=X; X2(k,L)=X2(k,L)-h;X2(p,r)=X2(p,r)+h;
            [FR2,aFR1,aFs,aNt]=Fmoros(X2);
            X1=X; X1(k,L)=X1(k,L)-h;X1(p,r)=X1(p,r)-h;
            [FR1,aFR1,aFs,aNt]=Fmoros(X1);
            Hb(i1,j1)=(FR4-FR3-FR2+FR1)/(4*h^2);
        end
        end
        if k==p && L==r
            for I=1:Ntop
                if I~=k
                    Xi=[X(I,1);X(I,2);X(I,3)];
                    Rki=(Xk-Xi).^2;  Rki=(Rki(1)+Rki(2)+Rki(3))^0.5;
                    Fki=2*r0*(exp(r0*(1-Rki))-exp(2*r0*(1-Rki)));
                    Pki=2*(r0^2)*(2*exp(2*r0*(1-Rki))-exp(r0*(1-Rki)));
                    A=(Xk(L)-Xi(L))/Rki;
                    B=1/Rki-((Xk(L)-Xi(L))^2)/Rki^3;
                    H(i1,j1)=H(i1,j1)+Pki*A^2+Fki*B;
                end
            end
        elseif k~=p && L==r
            Ak=sign(Xk(L)-Xp(L))*abs(Xk(L)-Xp(L))/Rkp;
            Ap=sign(Xp(L)-Xk(L))*abs(Xp(L)-Xk(L))/Rkp;
            %B=1/Rkp-((Xk(L)-Xp(L))^2)/Rkp^3;
            B=sign(Xk(L)-Xp(L))/Rkp-((Xk(L)-Xp(L))^2)/Rkp^3;
            H(i1,j1)=H(i1,j1)+Pkp*Ak*Ap+Fkp*B;
        elseif k==p && L~=r
            for I=1:Ntop
                if I~=k
                    Xi=[X(I,1);X(I,2);X(I,3)];
                    Rki=(Xk-Xi).^2;  Rki=(Rki(1)+Rki(2)+Rki(3))^0.5;
                    Fki=2*r0*(exp(r0*(1-Rki))-exp(2*r0*(1-Rki)));
                    Pki=2*(r0^2)*(2*exp(2*r0*(1-Rki))-exp(r0*(1-Rki)));
                    A=(sign(Xk(L)-Xi(L))*abs(Xk(L)-Xi(L))*sign(Xk(r)-Xi(r))*abs(Xk(r)-Xi(r)))/Rki^2;
                    B=-(sign(Xk(L)-Xi(L))*abs(Xk(L)-Xi(L))*sign(Xk(r)-Xi(r))*abs(Xk(r)-Xi(r)))/Rki^3;
                    H(i1,j1)=H(i1,j1)+Pki*A+Fki*B;
                end
            end
        elseif k~=p && L~=r
            A=(sign(Xk(L)-Xp(L))*abs(Xk(L)-Xp(L))*sign(Xp(r)-Xk(r))*abs(Xp(r)-Xk(r)))/Rkp^2;
            B=-(sign(Xk(L)-Xp(L))*abs(Xk(L)-Xp(L))*sign(Xp(r)-Xk(r))*abs(Xp(r)-Xk(r)))/Rkp^3;
            H(i1,j1)=H(i1,j1)+Pkp*A+Fkp*B;
        else
            error('Ошибка вычисления Гессиана');
        end
        
        if i1~=j1
            H(j1,i1)=H(i1,j1);
            Hb(j1,i1)=Hb(i1,j1);
        end 
    end
    
end
%H=Hb;
return
