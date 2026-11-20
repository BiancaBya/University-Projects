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


my_append([], Rez, Rez).

my_append([H|T], Acc, Rez):-
    my_append(T, [H|Acc], Rez).



generare_int(A, B, []):-
    A > B, !.

generare_int(A, B, [A|Rez]):-
    A =< B, !,
    A1 is A + 1,
    generare_int(A1, B, Rez).



get_all(_, N, Ncurr, []):-
    Ncurr > N, !.

get_all(L, N, Ncurr, Rez):-
    findall(C, comb(L, Ncurr, C), AllC),
    Ncurr1 is Ncurr + 1,
    get_all(L, N, Ncurr1, Rez1),
    my_append(AllC, Rez1, Rez).


parcurgere([], []).

parcurgere([H|T], [H|Rez]):-
    suma_lista(H, 0, S),
    S mod 2 =:= 1, !,
    parcurgere(T, Rez).

parcurgere([_|T], Rez):-
    parcurgere(T, Rez).



main(A, B, Rez):-
    generare_int(A, B, Int),
    Lg is B-A+1,
    get_all(Int, Lg, 1, Rez1),
    parcurgere(Rez1, Rez).
