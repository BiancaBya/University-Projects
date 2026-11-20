function cval = cos_pade(x, m, k)
    x = red_arg(x);
    n = m + k + 1;  % nr de coeficienti ai seriei Taylor
    c = zeros(1, 2*n + 1);
    c(1) = 1; % coeficientul lui x^0 este 1
    term = 1;

    for i = 1:n
        term = term * (-1) / ((2*i - 1) * (2*i));
        c(2*i + 1) = term; % generare coeficienti
    end

    cval = pade_core(x, c, m, k); % apel pade cu coeficientii Taylor
end


