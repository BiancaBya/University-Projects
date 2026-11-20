t = 1900:10:2010;
y = [75.995, 91.972, 105.711, 123.203, 131.669, 150.697, ...
     179.323, 203.212, 226.505, 249.633, 281.422, 308.786];

p_1975_all = lagrange_interpol(t, y, 1975);
p_2018_all = lagrange_interpol(t, y, 2018);

fprintf('1975 (toate nodurile): %.3f\n', p_1975_all);
fprintf('2018 (toate nodurile): %.3f\n', p_2018_all);

% --- Estimare 1975 ---
% Sortam nodurile fata de 1975 si folosim un grad rezonabil (ex: m=4, deci 5 noduri)
[t_s75, y_s75] = sort_nodes(t, y, 1975);
pop_1975 = lagrange_interpol(t_s75(1:5), y_s75(1:5), 1975);

% --- Estimare 2018 ---
% Sortam nodurile fata de 2018. Folosim gradul m=2 (3 noduri) pentru stabilitate la extrapolare
[t_s18, y_s18] = sort_nodes(t, y, 2018);
pop_2018 = lagrange_interpol(t_s18(1:3), y_s18(1:3), 2018);

fprintf('Estimare 1975 (m=4): %.3f milioane\n', pop_1975);
fprintf('Estimare 2018 (m=2): %.3f milioane\n', pop_2018);


