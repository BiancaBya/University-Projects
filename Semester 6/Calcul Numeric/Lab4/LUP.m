function [L,U,P]=LUP(A)

[m,n] = size(A);
P = zeros(m,n);
pivot = (1:m)';

for i=1:m-1
    [maxim, ppoz] = max(abs(A(i:m,i)));
    ppoz = ppoz+i-1;

    if i~=ppoz
        A([i, ppoz],:) = A([ppoz,i],:);
        pivot([i,ppoz]) = pivot([ppoz,i]);
    end

    row = i+1:m;
    A(row,i) = A(row,i)/A(i,i);
    A(row,row) = A(row,row)-A(row,i)*A(i,row);
end

for i=1:m
    P(i,pivot(i)) = 1;
end

U = triu(A);
L = tril(A,-1);
L = L+eye(m);
end
