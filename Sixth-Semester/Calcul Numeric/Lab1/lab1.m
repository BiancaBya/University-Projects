F= @(x) [log(1+x)];

syms x
y=taylor_coef(F(x),3)
%taylor_draw(F(x),-1.2,3,6)

pade_test(F(x),x,2,2,a=0,b=2)
