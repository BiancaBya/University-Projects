clc;
clear;
close all;

% Datele din tabel (in milioane)
t = [1900; 1910; 1920; 1930; 1940; 1950; 1960; 1970; 1980; 1990; 2000; 2010; 2020];
y = [75.995; 91.972; 105.710; 123.200; 131.670; 150.700; 179.320; 203.210; 226.510; 249.630; 281.420; 308.790; 350.686];

% Scalare pentru stabilitate numerica
s = (t - 1950) / 50;

% puncte pentru desen
tt = (1900:1:2020)';
ss = (tt - 1950) / 50;

%% MODEL POLINOMIAL DE GRAD 3
A_poly = [ones(size(s)), s, s.^2, s.^3];

% ecuatii normale
coef_poly_normal = (A_poly' * A_poly) \ (A_poly' * y);

% QR
[Qp, Rp] = qr(A_poly, 0);
coef_poly_qr = Rp \ (Qp' * y);

% evaluare pe grafic
A_poly_plot = [ones(size(ss)), ss, ss.^2, ss.^3];
y_poly_plot = A_poly_plot * coef_poly_qr;

% estimari
s1975 = (1975 - 1950) / 50;
s2015 = (2015 - 1950) / 50;

y1975_poly = [1, s1975, s1975^2, s1975^3] * coef_poly_qr;
y2015_poly = [1, s2015, s2015^2, s2015^3] * coef_poly_qr;

% reziduuri
res_poly = A_poly * coef_poly_qr - y;
SSE_poly = norm(res_poly)^2;

%% MODEL EXPONENTIAL
% y = K * e^(lambda * s)
% ln(y) = ln(K) + lambda*s

z = log(y);
A_exp = [ones(size(s)), s];

% ecuatii normale
coef_exp_normal = (A_exp' * A_exp) \ (A_exp' * z);

% QR
[Qe, Re] = qr(A_exp, 0);
coef_exp_qr = Re \ (Qe' * z);

lnK = coef_exp_qr(1);
lambda = coef_exp_qr(2);
K = exp(lnK);

A_exp_plot = [ones(size(ss)), ss];
y_exp_plot = exp(A_exp_plot * coef_exp_qr);

y1975_exp = exp([1, s1975] * coef_exp_qr);
y2015_exp = exp([1, s2015] * coef_exp_qr);

% reziduuri in scala originala
res_exp = exp(A_exp * coef_exp_qr) - y;
SSE_exp = norm(res_exp)^2;

%% Afisare rezultate
disp('MODEL POLINOMIAL GRAD 3');
disp('Coeficienti [c0 c1 c2 c3] prin QR:');
disp(coef_poly_qr);
fprintf('Estimare pentru 1975 = %.6f milioane\n', y1975_poly);
fprintf('Estimare pentru 2015 = %.6f milioane\n', y2015_poly);
fprintf('SSE polinomial = %.10f\n', SSE_poly);
fprintf('\n');

disp('MODEL EXPONENTIAL');
disp('Coeficienti pentru ln(y) = ln(K) + lambda*s:');
disp(coef_exp_qr);
fprintf('K = %.6f\n', K);
fprintf('lambda = %.6f\n', lambda);
fprintf('Estimare pentru 1975 = %.6f milioane\n', y1975_exp);
fprintf('Estimare pentru 2015 = %.6f milioane\n', y2015_exp);
fprintf('SSE exponential = %.10f\n', SSE_exp);
fprintf('\n');

if SSE_poly < SSE_exp
    disp('Modelul polinomial se potriveste mai bine datelor.');
else
    disp('Modelul exponential se potriveste mai bine datelor.');
end

%% Plot
figure;
plot(t, y, 'ro', 'MarkerSize', 8, 'LineWidth', 1.5);
hold on;
plot(tt, y_poly_plot, 'b-', 'LineWidth', 2);
plot(tt, y_exp_plot, 'g--', 'LineWidth', 2);

plot(1975, y1975_poly, 'ks', 'MarkerSize', 8, 'LineWidth', 1.5);
plot(2015, y2015_poly, 'ks', 'MarkerSize', 8, 'LineWidth', 1.5);

plot(1975, y1975_exp, 'md', 'MarkerSize', 8, 'LineWidth', 1.5);
plot(2015, y2015_exp, 'md', 'MarkerSize', 8, 'LineWidth', 1.5);

grid on;
xlabel('Anul');
ylabel('Populatia (milioane)');
title('Modelarea populatiei SUA');

legend('Date observate', ...
       'Model polinomial grad 3', ...
       'Model exponential', ...
       'Estimari polinomiale', ...
       'Estimari exponentiale', ...
       'Location', 'northeast');
