subliste([], []).
subliste([_|T], Rez):-
    subliste(T, Rez).
subliste([H|T], [H|Rez]):-
    subliste(T, Rez).



% sublistele cu elemente pare, cu cel mult n elemente

elem_pare([]).
elem_pare([H|T]):-
    H mod 2 =:= 0,
    elem_pare(T).


cel_mult_n([], N, NrEl):-
    NrEl =< N.
cel_mult_n([_|T], N, NrEl):-
    NrEl1 is NrEl + 1,
    cel_mult_n(T, N, NrEl1).



subBune([], [], _).
subBune([H|T], [H|Rez], N):-
    elem_pare(H),
    cel_mult_n(H, N, 0),
    !,
    subBune(T, Rez, N).
subBune([_|T], Rez, N):-
    subBune(T, Rez, N).


my_append([], Rez, Rez).
my_append([H|T],L, Rez):-
    my_append(T, [H|L], Rez).


get_all(L, N, Rez):-
    findall(S, subliste(L, S), AllS),
    get_all(L, N, Rez1),
    my_append(AllS, Rez1, Rez).


main(L, N, Rez):-
    get_all(L, N, Rez1),
    subBune(Rez1, Rez, N).

