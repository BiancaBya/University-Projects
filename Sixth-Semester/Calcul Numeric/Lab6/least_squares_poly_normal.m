function [coef, values] = least_squares_poly_normal(x, y, degree, points)
    % x, y - date discrete
    % degree - gradul polinomului
    % points - punctele in care evaluam aproximanta
    %
    % Rezolvare prin ecuatiile normale:
    % (A' * A) * c = A' * y

    x = x(:);
    y = y(:);
    points = points(:);

    m = length(x);

    % Matricea de design A
    A = zeros(m, degree + 1);
    for j = 0:degree
        A(:, j + 1) = x .^ j;
    end

    % Ecuatiile normale
    N = A' * A;
    b = A' * y;

    coef = N \ b;

    % Evaluare in punctele cerute
    P = zeros(length(points), degree + 1);
    for j = 0:degree
        P(:, j + 1) = points .^ j;
    end

    values = P * coef;
end
