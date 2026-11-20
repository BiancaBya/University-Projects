function s = sin_taylor(x, tol)
  if nargin < 2, tol = eps; end

  s = x;
  term = x;
  k = 1;

  while abs(term) > tol * abs(s)
    so = s;
    coef = (-1)/(2*k)/(2*k+1);
    term = term * coef * x * x;
    s = so + term;
    k = k + 1;
  end
end


