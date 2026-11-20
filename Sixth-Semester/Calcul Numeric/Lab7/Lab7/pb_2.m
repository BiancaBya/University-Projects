f_exp = @(x) exp(x.^2 - 1);
noduri_exp = [1, 1.1, 1.2, 1.3, 1.4];
valori_exp = f_exp(noduri_exp);

x_tinta = 1.25;
f_125_estimat = lagrange_interpol(noduri_exp, valori_exp, x_tinta);

% Calculul u_m(1.25) pentru eroare
u_m = prod(x_tinta - noduri_exp);
fprintf('f(1.25) aproximat: %.6f\n', f_125_estimat);
fprintf('Valoarea exacta f(1.25): %.6f\n', f_exp(x_tinta));
