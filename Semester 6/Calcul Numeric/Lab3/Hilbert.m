format long g;
n_arr = 10:15;

disp('H_5=');
    disp(hilb(5));
    disp('');

for n = n_arr
  fprintf('n = %d\n', n);

  H = hilb(n);

  cond_n = cond(H, 2); % norma euclidiana/2
  fprintf('cond_2(H_%d) = %.e\n\n', n, cond_n);
end



