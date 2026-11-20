format long g;

t = linspace(-1, 1, 5);
V = fliplr(vander(t));
disp('V_5=');
disp(V);
disp('');

n_arr = 10:15;

for n = n_arr
  fprintf('n = %d\n', n);
  t = linspace(-1, 1, n);

  V = fliplr(vander(t));

  cond_n = cond(V, inf); % norma Cebisev/inf
  fprintf('cond_inf(V_%d) = %.e\n\n', n, cond_n);
end



