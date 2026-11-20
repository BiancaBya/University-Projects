clc;
clear;

A = [1e-20  1;
     1      1];

A = [1 7 -1;
     2 3 1;
     -1 -1 10];

x_exact = [1; 1];

x_exact = [7; 6; 8];

b = A * x_exact;

disp('A = ');
disp(A);

disp('b = ');
disp(b);

disp('Solutia exacta = ');
disp(x_exact);

x_fara = elimGaussFaraPivot(A,b);
x_pivot = elimGaussPivot(A, b);
x_lup = rezolvareLUP(A, b);

disp('Solutia fara pivotare:');
disp(x_fara);

disp('Solutia cu pivotare partiala:');
disp(x_pivot);

disp('Solutia cu LUP:');
disp(x_lup);

disp('Eroare fara pivotare:');
disp(norm(x_fara - x_exact));

disp('Eroare cu pivotare:');
disp(norm(x_pivot - x_exact));

disp('Eroare cu LUP:');
disp(norm(x_lup - x_exact));
