clc;
clear;
close all;

x = [-1.024940 -0.949898 -0.866114 -0.773392 -0.671372 ...
     -0.559524 -0.437067 -0.302909 -0.159493 -0.007464]';

y = [-0.389269 -0.322894 -0.265256 -0.216557 -0.177152 ...
     -0.147582 -0.128618 -0.121353 -0.127348 -0.148895]';

n = length(x);

%  MODEL ELIPSOIDAL
%  x^2 = a*y^2 + b*x*y + c*x + d*y + e

A_ell = [y.^2, x.*y, x, y, ones(n,1)];
b_ell = x.^2;

% Ecuatii normale
coef_ell_normal = (A_ell' * A_ell) \ (A_ell' * b_ell);

% QR
[Qe, Re] = qr(A_ell, 0);
coef_ell_qr = Re \ (Qe' * b_ell);

% Reziduuri
res_ell = A_ell * coef_ell_qr - b_ell;
SSE_ell = norm(res_ell)^2;
sigma2_ell = SSE_ell / (n - 5);

disp('MODEL ELIPSOIDAL');
disp('Coeficienti [a b c d e] prin QR:');
disp(coef_ell_qr);
fprintf('SSE elipsoidal = %.10f\n', SSE_ell);
fprintf('Estimare varianta eroarii = %.10f\n\n', sigma2_ell);

%  MODEL PARABOLIC
%  x^2 = a*y + e

A_par = [y, ones(n,1)];
b_par = x.^2;

% Ecuatii normale
coef_par_normal = (A_par' * A_par) \ (A_par' * b_par);

% QR
[Qp, Rp] = qr(A_par, 0);
coef_par_qr = Rp \ (Qp' * b_par);

% Reziduuri
res_par = A_par * coef_par_qr - b_par;
SSE_par = norm(res_par)^2;
sigma2_par = SSE_par / (n - 2);

disp('MODEL PARABOLIC');
disp('Coeficienti [a e] prin QR:');
disp(coef_par_qr);
fprintf('SSE parabolic = %.10f\n', SSE_par);
fprintf('Estimare varianta eroarii = %.10f\n\n', sigma2_par);

%  COMPARATIE

if SSE_ell < SSE_par
    disp('Modelul elipsoidal pare mai probabil (are eroare mai mica).');
else
    disp('Modelul parabolic pare mai probabil (are eroare mai mica).');
end

figure;
plot(x, y, 'ro', 'MarkerSize', 8, 'LineWidth', 1.5);
hold on;
grid on;
xlabel('x');
ylabel('y');
title('Observatiile asteroidului');

% Curba parabola din modelul x^2 = a*y + e
a_par = coef_par_qr(1);
e_par = coef_par_qr(2);

yy = linspace(min(y)-0.05, max(y)+0.05, 400)';
xx_par = sqrt(max(a_par * yy + e_par, 0));
plot( xx_par, yy, 'b-', 'LineWidth', 2);
plot(-xx_par, yy, 'b-', 'LineWidth', 2);

legend('Date observate', 'Model parabolic', 'Location', 'best');
