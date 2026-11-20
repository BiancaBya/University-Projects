clc;
clear;
close all;

% Exemplu de test
x = (1:10)';
y = sin(x);

degree = 4;
points = linspace(x(1), x(end), 200)';

[coef_normal, y_normal] = least_squares_poly_normal(x, y, degree, points);
[coef_qr, y_qr] = least_squares_poly_qr(x, y, degree, points);

disp('Coeficienti prin ecuatiile normale:');
disp(coef_normal);

disp('Coeficienti prin QR:');
disp(coef_qr);

figure;
plot(x, y, 'ro', 'MarkerSize', 8, 'LineWidth', 1.5);
hold on;
plot(points, y_normal, 'b-', 'LineWidth', 2);
plot(points, y_qr, 'g--', 'LineWidth', 2);
grid on;
legend('Date discrete', 'Aproximare LS - ec. normale', 'Aproximare LS - QR', 'Location', 'best');
title('Aproximanta discreta prin metoda celor mai mici patrate');
xlabel('x');
ylabel('y');
