clc;
clear;

n = 4;

x_exact = ones(n,1);

A = randi(10, n, n);

while det(A) == 0
    A = randi(10, n, n);
end

b = A * x_exact;

x1 = elimGaussPivot(A, b);

x2 = rezolvareLUP(A, b);

disp('Matricea A este:');
disp(A);

disp('Vectorul b este:');
disp(b);

disp('Solutia exacta este:');
disp(x_exact);

disp('Solutia obtinuta cu eliminare gaussiana este:');
disp(x1);

disp('Solutia obtinuta cu LUP este:');
disp(x2);
