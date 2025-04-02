comb([E|_], 1, [E]).

comb([_|T], N, R):-
   comb(T, N, R).

comb([H|T], N, [H|R]):-
    N > 1,
    N1 is N-1,
    comb(T, N1, R).


suma_lista([], S, S).

suma_lista([H|T], S, Rez):-
    S1 is S + H,
    suma_lista(T, S1, Rez).


lungime_lista([], 0).

lungime_lista([_|T], Lg):-
    lungime_lista(T, Lg1),
    Lg is Lg1 + 1.


my_append([], Rez, Rez).

my_append([H|T], Acc, Rez):-
    my_append(T, [H|Acc], Rez).


get_all(_, N, Ncur, []):-
    Ncur =:= N-1,
    !.

get_all(L, N, Lgcurr, Rez):-
    findall(C, comb(L, Lgcurr, C), AllC),
    Lgcurr1 is Lgcurr - 1,
    get_all(L, N, Lgcurr1, Rez1),
    my_append(AllC, Rez1, Rez).



parc_combinari([], []).

parc_combinari([H|T], [H|Rez]):-
    suma_lista(H, 0, S),
    S mod 3 =:= 0,
    !,
    parc_combinari(T, Rez).


parc_combinari([_|T], Rez):-
    parc_combinari(T, Rez).


main(L, N, Rez):-
   lungime_lista(L, Lg),
   get_all(L, N, Lg, Rez1),
   parc_combinari(Rez1, Rez).


