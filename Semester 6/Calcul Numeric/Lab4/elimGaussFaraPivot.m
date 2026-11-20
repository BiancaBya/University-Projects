function x = elimGaussFaraPivot(A, b)
    [n, m] = size(A);

    if n ~= m
        error('Matricea A trebuie sa fie patratica.');
    end

    Ab = [A, b(:)];

    for k = 1:n-1
        if Ab(k,k) == 0
            error('Pivot nul. Fara pivotare nu se poate continua.');
        end

        for i = k+1:n
            m_ik = Ab(i,k) / Ab(k,k);
            Ab(i, k:end) = Ab(i, k:end) - m_ik * Ab(k, k:end);
        end
    end

    x = zeros(n,1);
    x(n) = Ab(n,n+1) / Ab(n,n);

    for i = n-1:-1:1
        x(i) = (Ab(i,n+1) - Ab(i,i+1:n) * x(i+1:n)) / Ab(i,i);
    end
end
