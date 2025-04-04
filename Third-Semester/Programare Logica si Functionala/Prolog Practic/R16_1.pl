suma_1(0,0):-!.

suma_1(N, S):-
    N1 is N-1,
    suma_1(N1, S1),
    S is S1 + N.



suma_2(0, S, S).

suma_2(N, S, Rez):-
    S1 is S+N,
    N1 is N-1,
    suma_2(N1, S1, Rez).

suma(N, Rez):-
    suma_2(N,0, Rez).



suma_lista([], Suma, Suma).

suma_lista([H|T], Suma, Rez):-
    Suma1 is Suma+H,
    suma_lista(T, Suma1, Rez).


validare([], _, []).


validare([H|T], S, [H|Aranj]):-
    suma_lista(H, 0, Rez),
    Rez =:= S, !,
    validare(T, S, Aranj).


validare([_|T], S, Aranj):-
    validare(T, S, Aranj).



arrangement([E|_], 1, [E]).
% Dacă K este 0, aranjamentul este lista goală.

arrangement([_|T], N, R):-
    arrangement(T, N, R).

arrangement([H|T], N, R) :-
    N > 1,
    N1 is N - 1,
    arrangement(T, N1, R1),  % Obținem un aranjament din restul liste
    insert(H, R1, R). % COMBINARI -> fara insert, se adauga H la R sus, fara R1

 % Selectăm H și continuăm recursiv cu restul listei.
     % Inserăm H în toate pozițiile aranjamentului.


insert(X, [], [X]).        
insert(X, [H|T], [X, H|T]). 
insert(X, [H|T], [H|R]) :-
    insert(X, T, R).



main(L, N, S, Rez):-
    findall(A, arrangement(L, N, A), All),
    validare(All, S, Rez).
