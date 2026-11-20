clc;
clear;
close all;

% Necunoscute:
% x1 = AB, x2 = BC, x3 = CD

A = [1 1 1;
     1 1 0;
     0 1 1;
     1 0 0;
     0 0 1];

b = [89;
     67;
     53;
     35;
     20];

%% Rezolvare prin ecuatiile normale
sol_normal = (A' * A) \ (A' * b);

%% Rezolvare prin QR
[Q, R] = qr(A, 0);
sol_qr = R \ (Q' * b);

AB = sol_qr(1);
BC = sol_qr(2);
CD = sol_qr(3);

disp('Solutia prin ecuatiile normale:');
disp(sol_normal);

disp('Solutia prin QR:');
disp(sol_qr);

fprintf('AB = %.6f\n', AB);
fprintf('BC = %.6f\n', BC);
fprintf('CD = %.6f\n', CD);

% Reziduu
res = A * sol_qr - b;
fprintf('Norma reziduului = %.10f\n', norm(res));
fprintf('Suma patratelor reziduurilor = %.10f\n', norm(res)^2);
