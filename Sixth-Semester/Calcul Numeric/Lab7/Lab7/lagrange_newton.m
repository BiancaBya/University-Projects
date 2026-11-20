function [Lf, table] = lagrange_newton(nodes, values, x_eval)
    m = length(nodes);
    table = zeros(m, m);
    table(:, 1) = values(:); % prima coloana - valorile f(x_i)

    % constructie tabelul diferente divizate
    for j = 2:m
        for i = 1:(m - j + 1)
            table(i, j) = (table(i+1, j-1) - table(i, j-1)) / (nodes(i+j-1) - nodes(i));
        end
    end

    % evaluare polinom
    coefs = table(1, :);
    Lf = coefs(1) * ones(size(x_eval));
    prod_term = ones(size(x_eval));

    for k = 2:m
        prod_term = prod_term .* (x_eval - nodes(k-1));
        Lf = Lf + coefs(k) * prod_term;
    end
end
