format long g;

t = 1./(1:5);
V = fliplr(vander(t));
disp('V_5=');
disp(V);
disp('');

n_arr = 10:15;

for n = n_arr
  fprintf('n = %d\n', n);
  t = 1./(1:n);

  V = fliplr(vander(t));

  cond_n = cond(V, inf); % norma Cebisev/inf
  fprintf('cond_inf(V_%d) = %.e\n\n', n, cond_n);
end


% pentru valori mai mari ale lui n (13, 14, 15)
% avem crestere uriasa a numarului de conditionare
% din punct de vedere numeric matricea e considerata aproape singulara
% rcond = 1/cond(A,1) (relativ la norma 1)
% => pentru rcond aproape de 0, matrice aproape singulara numeric



