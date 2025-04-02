inserare(E, L, [E|L]).
inserare(E, [H|T], [H|Rez]):-
    inserare(E, T, Rez).

perm([],[]).
perm([H|T], P):-
    perm(T, P1),
    inserare(H, P1, P).

