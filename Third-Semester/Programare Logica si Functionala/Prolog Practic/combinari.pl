comb([H|_], 1, [H]).
comb([_|T], K, C):-
    comb(T, K, C).
comb([H|T], K, [H|C]):-
    K > 1,
    K1 is K-1,
    comb(T, K1, C).


generare_lista(0, []):-!.
generare_lista(N, [N|Rez]):-
    N1 is N-1,
    generare_lista(N1, Rez).


dif_para([_]):- !.
dif_para([H1,H2|T]):-
    Dif is H2-H1,
    Dif mod 2 =:= 0,
    !,
    dif_para([H2|T]).



validare([],[]).
validare([H|T], [H|Rez]):-
    dif_para(H),
    !,
    validare(T, Rez).
validare([_|T], Rez):-
    validare(T, Rez).




main(N, K, Rez):-
    generare_lista(N, L),
    findall(C, comb(L, K, C), AllC),
    validare(AllC, Rez).
