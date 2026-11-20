%f = @(x) exp(x); noduri = [0, 0.5, 1.2, 2, 3.25, 5];

% f = @(x) sin(x); noduri = [-pi/2, 0, pi/2, pi]; a = -5; b = 5;

% f = @(x) 1./(1+x.^2); a=-5; b=5; noduri=linspace(a,b,11); % Runge

f = @(x) abs(x); a=-1; b=1; noduri=linspace(a,b,7); % Bernstein

valorile = f(noduri);

% --- EVALUARE ---
x_plot = linspace(a, b, 100);
y_real = f(x_plot);
y_interp = lagrange_interpol(noduri, valorile, x_plot);

% --- GRAFIC ---
figure;
plot(x_plot, y_real, 'g--', 'LineWidth', 2); hold on; % Functia reala
plot(x_plot, y_interp, 'r-', 'LineWidth', 1.5);      % Polinomul
plot(noduri, valorile, 'ko', 'MarkerFaceColor', 'k'); % Nodurile date de tine

grid on;
legend('Functia f(x)', 'Polinomul Lagrange (Clasic)', 'Nodurile alese');
title('Reprezentare Grafica: Lagrange Clasic');
