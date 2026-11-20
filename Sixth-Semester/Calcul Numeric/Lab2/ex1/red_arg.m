function a = red_arg(x)
  % reducere la [0, 2pi)
  % a = mod(x, 2*pi);
  k = floor(x / (2*pi)); % de cate ori incape 2pi in x
  a = x - 2 * k * pi;  % scade numarul de 2pi din x ca sa ramana in (0, 2pi)
end



