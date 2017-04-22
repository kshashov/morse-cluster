function [xsup,xinf]=inf_sup(x, Natom, Mcl)

%[Cs,Xs,xs]=CONVERT_2016(x);
Cs=bitget(x,Mcl:-1:1);
N = sum(Cs);
M = Natom;

if N == M
    Cinf = Cs;
    Csup = Cinf;
elseif N > M
    NM = N - M;
    
    %Cinf
    k = 0;
    for i=Mcl:-1:1
        if (Cs(i) == 1) && (not(k == NM))
            Cinf(i) = 0;
            k = k + 1;
        else
            Cinf(i) = Cs(i);
        end
    end
    
    %Csup
    k = N - M + 1; %#1
    j = 0;
    Csup = Cs;
    for i=Mcl:-1:1 %#2
        if (Csup(i) == 1)
            j = j + 1; 
        else
            if (j >= k)
                break;
            end
        end
    end
    Csup(i) = 1; %#4
    j = 0;
    for z=i+1:1:Mcl 
        if j == k 
            break
        end
        if Csup(z) == 1
            Csup(z) = 0;
            j = j + 1;
        end
    end
    Csup = toRight(Csup, Mcl, i, 1); %#5 
elseif N < M
    %Csup
    MN = M - N;
    k = 0;
    for i=Mcl:-1:1
        if (Cs(i) == 0) && (not(k == MN))
            Csup(i) = 1;
            k = k + 1;
        else
            Csup(i) = Cs(i);
        end
    end
    
    %Cinf
    k = M - N + 1; %#1
    j = 0;
    Cinf = Cs;
    np = Mcl;
    for I=Mcl:-1:1
        if Cinf(I)==1 
            np=I;  %позиция в векторе
        end 
    end
    for i=Mcl:-1:np %#2
        if (Cinf(i) == 0)
            j = j + 1; 
        else
            if (j >= k)
                break;
            end
        end
    end
    
    if (j < k)
        Cinf = Csup;%zeros(1, Mcl); %EDIT THIS
    else    
        Cinf(i) = 0; %#4
        j = 0;
        for z=i+1:1:Mcl 
            if j == k 
                break
            end
            if Cinf(z) == 0
                Cinf(z) = 1;
                j = j + 1;
            end
        end
        Cinf = toRight(Cinf, Mcl, i, 0); %#5
    end
end

%xsup xinf
xsup=0;
for I=Mcl:-1:1
    xsup=xsup+Csup(I)*2^(Mcl-I);
end
xinf=0;
for I=Mcl:-1:1
    xinf=xinf+Cinf(I)*2^(Mcl-I);
end
return

function [V] = toRight(Src, Mcl, index, number)
    V = Src;
    j = 0;
    for i=index+1:1:Mcl
        if not(V(i) == number)
            j = j + 1;
        end
    end
    
    for i=index+1:1:Mcl
        if (j > 0)
            V(i) = 1 - number;
            j = j - 1;
        else
            V(i) = number;
        end
    end
return