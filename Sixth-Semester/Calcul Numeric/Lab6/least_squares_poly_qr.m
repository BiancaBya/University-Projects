function [coef, values] = least_squares_poly_qr(x, y, degree, points)
    % Rezolvare prin factorizare QR:
    % A*c ~= y  ==>  Q*R*c = y  ==>  R*c = Q'*y

    x = x(:);
    y = y(:);
    points = points(:);

    m = length(x);

    % Matricea de design A
    A = zeros(m, degree + 1);
    for j = 0:degree
        A(:, j + 1) = x .^ j;
    end

    [Q, R] = qr(A, 0);
    coef = R \ (Q' * y);

    % Evaluare in punctele cerute
    P = zeros(length(points), degree + 1);
    for j = 0:degree
        P(:, j + 1) = points .^ j;
    end

    values = P * coef;
end
