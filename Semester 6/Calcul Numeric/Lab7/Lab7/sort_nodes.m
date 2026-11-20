function [nodes_sorted, values_sorted] = sort_nodes(nodes, values, x_eval)
    distante = abs(nodes - x_eval);

    [~, indici] = sort(distante);

    nodes_sorted = nodes(indici);
    values_sorted = values(indici);
end
