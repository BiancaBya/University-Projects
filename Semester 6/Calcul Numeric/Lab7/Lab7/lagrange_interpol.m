function Lf = lagrange_interpol(nodes, values, x_eval)
    % nodes: x_i, values: f(x_i), x_eval: punctele unde evaluam
    m = length(nodes) - 1;
    Lf = zeros(size(x_eval));

    for k = 0:m
        % calcul polinomul fundamental l_k
        lk = ones(size(x_eval));
        for j = 0:m
            if j ~= k
                lk = lk .* (x_eval - nodes(j+1)) / (nodes(k+1) - nodes(j+1));
            end
        end
        Lf = Lf + lk * values(k+1);
    end
end
