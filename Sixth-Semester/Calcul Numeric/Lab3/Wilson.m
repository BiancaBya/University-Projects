A = [10 7 8 7; 7 5 6 5; 8 6 10 9; 7 5 9 10];
B = [32; 23; 33; 31];

X_0 = linsolve(A, B);
disp('X_0=');
disp(X_0);
disp('');

t=1; norm_type=inf;

delta_B = [0.1; -0.1; 0.1; -0.1];

B_t = B + t*delta_B;
disp('B_t=');
disp(B_t);
disp('');

X_bt = linsolve(A, B_t);
disp('X_bt=');
disp(X_bt);
disp('');

err_in = norm(abs(t)*delta_B, norm_type) / norm(B, norm_type);
err_out = norm(X_bt - X_0, norm_type) / norm(X_0, norm_type); % delta_X
raport = err_out/err_in;
fprintf('err_in = %f\n', err_in);
fprintf('err_out = %f\n', err_out);
fprintf('raport = %f\n\n\n', raport);

delta_A = [0 0 0.1 0.2; 0.08 0.04 0 0; 0 -0.02 -0.11 0; -0.01 -0.01 0 -0.02];

A_t = A + t*delta_A;
disp('A_t=');
disp(A_t);
disp('');

X_at = linsolve(A_t, B);
disp('X_at=')
disp(X_at);
disp('');

err_in = norm(abs(t)*delta_A, norm_type) / norm(A, norm_type);
err_out = norm(X_at - X_0, norm_type) / norm(X_0, norm_type);
raport = err_out/err_in;
fprintf('err_in = %f\n', err_in);
fprintf('err_out = %f\n', err_out);
fprintf('raport = %f\n\n\n', raport);




