function s = cos_taylor(x, tol)
  if nargin < 2, tol = eps; end

  s = 1;
  term = 1;
  k = 1;

  while abs(term) > tol * abs(s)
    so = s;
    coef = (-1)/(2*k-1)/(2*k);
    term = term * coef * x * x;
    s = so + term;
    k = k + 1;
  end
end


