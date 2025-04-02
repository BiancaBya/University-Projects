my_member(X, [X|_]).

my_member(X, [_|T]) :-
    my_member(X,T).


%apelul initial
principal(Lista1, Lista2, Rezultat) :-
    interclasare(Lista1, Lista2, [], Rezultat).

%caz de baza -> liste goale
interclasare([], [], _, []).


%cazurile in care o lista e goala
interclasare([], [H2|T2], Vizitate, Rez) :-
    my_member(H2, Vizitate), !,
    interclasare([], T2, Vizitate, Rez).

interclasare([H1|T1], [], Vizitate, Rez) :-
    my_member(H1, Vizitate), !,
    interclasare(T1, [], Vizitate, Rez).



interclasare([H1|T1], [], Vizitate, [H1|Rez]) :-
    \+ my_member(H1, Vizitate), !,
    interclasare(T1, [], [H1|Vizitate], Rez).

interclasare([], [H2|T2], Vizitate, [H2|Rez]) :-
    \+ my_member(H2, Vizitate), !,
    interclasare([], T2, [H2|Vizitate], Rez).



%cazul in care fl1 este in Vizitate
interclasare([H1|T1], [H2|T2], Vizitate, Rez) :-
    my_member(H1, Vizitate), !,
    interclasare(T1, [H2|T2], Vizitate, Rez).

%cazul in care sl2 este in Vizitate
interclasare([H1|T1], [H2|T2], Vizitate, Rez) :-
    my_member(H2, Vizitate), !,
    interclasare([H1|T1], T2, Vizitate, Rez).

%cazul in care fl1 <= sl1 si fl1 nu este in Vizitate
interclasare([H1|T1], [H2|T2], Vizitate, [H1|Rez]) :-
    \+ my_member(H1, Vizitate),
    H1 =< H2,
    !,
    interclasare(T1, [H2|T2], [H1|Vizitate], Rez).

%cazul in care sl1 < fl1 si sl1 nu este in Vizitate
interclasare([H1|T1], [H2|T2], Vizitate, [H2|Rez]) :-
    \+ my_member(H2, Vizitate),
    H2 < H1,
    !,
    interclasare([H1|T1], T2, [H2|Vizitate], Rez).


% apelul initial
apel(ListaEterogena, Rezultat) :-
    functie(ListaEterogena, [], Rezultat).

% Cazul de baza: lista goala
% se pune rezultatul acumulat in variabila Rezultat
functie([], Rez, Rez).

% H = lista, interclasam cu rezultatul acumulat
functie([H|T], Acc, Rez) :-
    is_list(H), !,
    principal(H, Acc, TempRez),  % Interclasam lista H cu acumularea
    functie(T, TempRez, Rez).

% H nu este o lista
functie([H|T], Acc, Rez) :-
    \+ is_list(H), !,
    functie(T, Acc, Rez).


%my_member(X, [X|T]) -> true
%my_member(X, [Y|T]) -> my_member(X,T)



% ?- apel([1,2,[3,4,4,5],6,[5,7,8],9,[8,8,10],[11],12], Rez).
% Rez = [3, 4, 5, 7, 8, 10, 11] .

% ?- apel([1,2,3,4], Rez).
% Rez = [].

% ?- apel([], Rez).
% Rez = [].

