% Matrice patratica 3x3 densa, folosita pentru verificarea initiala a algoritmilor.
% Este un sistem simplu, cu valori introduse manual, pentru a vedea daca
% metodele Jacobi, Gauss-Seidel si SOR converg catre aceeasi solutie.
A = [ 3, 1, -1;
      1, -4, 1;
      2, 5, -4 ];
B = [ 10;
      -2;
      0 ];

[x,ni] = Jacobi(A, B);
[gauss, ni0] = GaussSeidel(A, B);
omega=find_omega(A);
[x1, ni1] = SOR(A,B, omega);

fprintf('Jacobi a terminat in %d iteratii.\n', ni);
fprintf('Gauss-Seidel a terminat in %d iteratii.\n', ni0);
fprintf('SOR a terminat in %d iteratii cu omega = %f.\n\n', ni1, omega);

% Matrice tridiagonala (are valori doar pe diagonala principala si cele doua adiacente).
% Acest tip de matrice este specific discretizarii ecuatiilor diferentiale (metoda diferentelor finite).
% Este dominant diagonala (5 pe diagonala vs -1 pe laterale), ceea ce garanteaza convergenta rapida.
n = 7;
A = diag(5 * ones(n, 1)) + diag(-1 * ones(n - 1, 1), -1) + diag(-1 * ones(n - 1, 1), 1);
b = 3 * ones(n, 1) + [1; zeros(n - 2, 1); 1];

[jac, ni] = Jacobi(A, b);
[gauss, ni0] = GaussSeidel(A, b);
omg = find_omega(A);
[my_sor, ni1] = SOR(A, b, omg);

fprintf('Jacobi a terminat in %d iteratii.\n', ni);
fprintf('Gauss-Seidel a terminat in %d iteratii.\n', ni0);
fprintf('SOR a terminat in %d iteratii cu omega = %f.\n\n', ni1, omg);

% Matrice bandata mai complexa, cu elemente pe diagonala principala si pe diagonalele 1 si 3.
% Structura aceasta apare in problemele de inginerie mai avansate (de exemplu, incovoierea grinzilor).
% Desi are mai multe elemente nenule per rand, ramane o matrice rara (sparse).
ons = ones(n, 1);
A = diag(5 * ons) + diag(-1 * ons(2:end), -1) + diag(-1 * ons(2:end), 1) + diag(-1 * ons(4:end), -3) + diag(-1 * ons(4:end), 3);
b = ones(n, 1) + [2; 1; 1; zeros(n-6, 1); 1; 1;2];

[jac, ni] = Jacobi(A, b);
[gauss, ni0] = GaussSeidel(A, b);
omg = find_omega(A);
[my_sor, ni1] = SOR(A, b, omg);

fprintf('Jacobi a terminat in %d iteratii.\n', ni);
fprintf('Gauss-Seidel a terminat in %d iteratii.\n', ni0);
fprintf('SOR a terminat in %d iteratii cu omega = %f.\n\n', ni1, omg);

% Generare automata a unui sistem de dimensiune 100x100 (sau mai mare).
% Acest test evalueaza performanta si eficienta algoritmilor pe sisteme realiste.
% Aici se observa cel mai bine avantajul metodei SOR fata de Jacobi in ceea ce priveste numarul de iteratii.
[A, b] = generare_matrici(100);

[jac, ni] = Jacobi(A, b);
[gauss, ni0] = GaussSeidel(A, b);
omg = find_omega(A);
[my_sor, ni1] = SOR(A, b, omg);

fprintf('Jacobi a terminat in %d iteratii.\n', ni);
fprintf('Gauss-Seidel a terminat in %d iteratii.\n', ni0);
fprintf('SOR a terminat in %d iteratii cu omega = %f.\n\n', ni1, omg);
