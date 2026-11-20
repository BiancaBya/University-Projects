permutareBuna([_]):-!.
permutareBuna([H1, H2 | T]):-
    abs(H1 - H2) =< 3,
    permutareBuna([H2 | T]).



permutariBune([],[]):-!.
permutariBune([H | T], [H | Rez]):-
    permutareBuna(H),
    !,
    permutariBune(T, Rez).
permutariBune([_ | T], Rez):-
    permutariBune(T, Rez).



insereaza(E, [], [E]).
insereaza(E, [H | T], [E, H | T]).
insereaza(E, [H|T], [H|Rez]) :-
    insereaza(E, T, Rez).


perm([], []).
perm([E|T], P) :-
    perm(T, L),
    insereaza(E, L, P).

% main
allPerm(L, AllPermBune):-
    findall(P, perm(L, P), AllPerm),
    permutariBune(AllPerm, AllPermBune).
