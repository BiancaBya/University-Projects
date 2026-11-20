function x = rezolvareLUP(A, b)

[L,U,P] = LUP(A);
y = L\(P*b);
x = U\y;
end
