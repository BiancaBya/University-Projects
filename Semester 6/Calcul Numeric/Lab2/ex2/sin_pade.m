function s = sin_pade(x, m, k)
    x = red_arg(x);
    n = m + k + 1;

    c = zeros(1, 2*n + 2);
    c(2) = 1;      % coeficientul lui x
    term = 1;      % coeficientul curent

    for i = 1:n
        term = term * (-1) / ((2*i) * (2*i+1));
        c(2*i + 2) = term;
    end

    s = pade_core(x, c, m, k);
end
