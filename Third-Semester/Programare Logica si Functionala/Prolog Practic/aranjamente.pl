insereaza(E, L, [E|L]).
insereaza(E, [H|T], [H|Rez]):-
    insereaza(E, T, Rez).


aranj([H|_], 1, [H]).
aranj([_|T], K, A):-
    aranj(T, K, A).
aranj([H|T], K, A):-
    K > 1,
    K1 is K-1,
    aranj(T, K1, A1),
    insereaza(H, A1, A).


% aranjamentele cu suma S

suma_lista([], 0).
suma_lista([H|T], S):-
    suma_lista(T, S1),
    S is S1+H.


aranjBune([], _, []).
aranjBune([H|T], S, [H|Aranj]):-
    suma_lista(H, Suma),
    Suma =:= S,
    !,
    aranjBune(T, S, Aranj).
aranjBune([_|T], S, Aranj):-
    aranjBune(T, S, Aranj).



main(L, K, S, Rez):-
    findall(A, aranj(L, K, A), AllA),
    aranjBune(AllA, S, Rez).
