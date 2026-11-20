function y = pade_core(x, c, m, k)
    C = zeros(k, k);
    d = zeros(k, 1);

    for i = 1:k    % coeficientii numitorului
        for j = 1:k
            C(i, j) = c(m + i - j + 1);
        end
        d(i) = -c(m + i + 1);
    end

    b = C \ d;  %rezolva sistemul liniar
    b = [1; b]; % adauga coeficientul constant 1
    a = zeros(m + 1, 1); % coeficientii numaratorului

    for j = 0:m
        for l = 0:min(j, k)
            a(j + 1) = a(j + 1) + c(j - l + 1) * b(l + 1);
        end
    end

    num = polyval(flip(a), x);
    den = polyval(flip(b), x);
    y = num / den;
end


