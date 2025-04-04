% apel initial
descompune_lista(Lista, [Pare, Impare], NrP, NrI) :-
    descompune_lista_aux(Lista, [], [], 0, 0, Pare, Impare, NrP, NrI).

% daca lista e goala -> se pun in variabilele Pare Impare NrP si NrI
%                       valorile calculate in apelul recursiv
descompune_lista_aux([], PareAcc, ImpareAcc, NrPareAcc, NrImpareAcc, PareAcc, ImpareAcc, NrPareAcc, NrImpareAcc).

% dacă H este par îl adăugăm la lista de pare si crestem contorul de
% nr_pare
descompune_lista_aux([H|T], PareAcc, ImpareAcc, NrPareAcc, NrImpareAcc, Pare, Impare, NrPare, NrImpare) :-
    H mod 2 =:= 0,
    NrPareAcc1 is NrPareAcc + 1, !,
    descompune_lista_aux(T, [H|PareAcc], ImpareAcc, NrPareAcc1, NrImpareAcc, Pare, Impare, NrPare, NrImpare).

% dacă H este impar îl adăugăm la lista de impare si crestem contorul de
% nr_impare
descompune_lista_aux([H|T], PareAcc, ImpareAcc, NrPareAcc, NrImpareAcc, Pare, Impare, NrPare, NrImpare) :-
    H mod 2 =:= 1,
    NrImpareAcc1 is NrImpareAcc + 1, !,
    descompune_lista_aux(T, PareAcc, [H|ImpareAcc], NrPareAcc, NrImpareAcc1, Pare, Impare, NrPare, NrImpare).

% descompune_lista([1, 2, 3, 4, 5, 6, 7], Rezultat, NrPare, NrImpare).
% Rezultat = [[6, 4, 2], [7, 5, 3, 1]],
% NrPare = 3,
% NrImpare = 4 .

% ?- descompune_lista([1, 3, 5], Rezultat, NrPare, NrImpare).
% Rezultat = [[], [5, 3, 1]],
% NrPare = 0,
% Nrimpare = 3.

% descompune_lista([2, 4, 8], Rezultat, NrPare, NrImpare).
% Rezultat = [[8, 4, 2], []],
% NrPare = 3,
% Nrimpare = 0.

% descompune_lista([], Rezultat, NrPare, NrImpare).
% Rezultat = [[], []],
% NrPare = NrImpare, NrImpare = 0.
