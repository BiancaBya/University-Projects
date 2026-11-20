f_runge = @(x) 1 ./ (1 + x.^2);
m = 12; % Grad ridicat

% Noduri Echidistante
x_eq = linspace(-5, 5, m+1);
y_eq = f_runge(x_eq);

% Noduri Cebîșev (scalate la intervalul [-5, 5])
j = 0:m;
x_cheb = 5 * cos(j * pi / m);
y_cheb = f_runge(x_cheb);

% Grafic
xx = linspace(-5, 5, 500);
figure;
plot(xx, f_runge(xx), 'k--', 'LineWidth', 2); hold on;
plot(xx, lagrange_interpol(x_eq, y_eq, xx), 'r');   % Problema Runge
plot(xx, lagrange_interpol(x_cheb, y_cheb, xx), 'b'); % Solutia Cebisev
legend('Original', 'Echidistante (Eroare mare!)', 'Cebisev (Stabil)');
title('Fenomenul Runge - Comparatie Noduri');
