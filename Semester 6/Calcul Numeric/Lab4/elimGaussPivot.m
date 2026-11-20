function x = elimGaussPivot(A, b)

    [n, m] = size(A);
    if n ~= m
        error('Matricea A trebuie sa fie patratica.');
    end

    if length(b) ~= n
        error('Dimensiuni incompatibile intre A si b.');
    end

    Ab = [A, b(:)];

    for k = 1:n-1
        [pivotMax, p] = max(abs(Ab(k:n, k)));
        p = p + k - 1;

        if pivotMax == 0
            error('Sistem incompatibil sau sistem cu solutie neunica.');
        end

        if p ~= k
            Ab([k p], :) = Ab([p k], :);
        end

        for i = k+1:n
            m_ik = Ab(i, k) / Ab(k, k);
            Ab(i, k:end) = Ab(i, k:end) - m_ik * Ab(k, k:end);
        end
    end

    if Ab(n, n) == 0
        error('Sistem incompatibil sau sistem cu solutie neunica.');
    end

    x = zeros(n, 1);
    x(n) = Ab(n, n+1) / Ab(n, n);

    for i = n-1:-1:1
        x(i) = (Ab(i, n+1) - Ab(i, i+1:n) * x(i+1:n)) / Ab(i, i);
    end
end
