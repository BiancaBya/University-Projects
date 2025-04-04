% apelul -> initializeaza lista goala de visited
lista_in_multime(Lista, Multime) :-
    lista_in_multime_aux(Lista, [], Multime).

% caz de bază -> lista goală
lista_in_multime_aux([], _, []).

% cazul cand h nu este in  vizitate: se pune H, primul element din lista
%                                   initiala, la finalul listei rezultat
lista_in_multime_aux([H|T], Vizitate, [H|Rez]) :-
    \+ member(H, Vizitate), !,
    lista_in_multime_aux(T, [H|Vizitate], Rez).

% Cazul cand H este in multimea formata
lista_in_multime_aux([H|T], Vizitate, Rez) :-
    member(H, Vizitate), !,
    lista_in_multime_aux(T, Vizitate, Rez).


% ?- lista_in_multime([1,2,3,1,2],Rezultat).
% Rezultat = [1, 2, 3]

% lista_in_multime([],Rezultat).
% Rezultat = [].
