subliste([],[]).
subliste([_|T],Rez):-subliste(T,Rez).
subliste([H|T],[H|Rez]):-subliste(T,Rez).

exista([Elem | _], Elem):-!.
exista([_ | T], Elem):-
    exista(T, Elem).

getRest([], _, []):-!.
getRest([H | T], Pereche, Rez):-
    exista(Pereche, H),
    !,
    getRest(T, Pereche, Rez).
getRest([H | T], Pereche, [H | Rez]):-
    getRest(T, Pereche, Rez).


% L : lista de subliste
partitii([], _, []):-!.
partitii([H1 | T], LIinit, [[H1, H2] | Rez]):-
    getRest(LIinit, H1, H2),
    partitii(T, LIinit, Rez).

sumaCifre(0, 0):-!.
sumaCifre(N, S):-
    Cif is N mod 10,
    NewN is N div 10,
    sumaCifre(NewN, S1),
    S is S1 + Cif.

sumaCifreElemente([], 0):-!.
sumaCifreElemente([H | T], S):-
    sumaCifreElemente(T, S1),
    sumaCifre(H, SumCif),
    S is S1 + SumCif.

partitiiBune([], []):-!.
partitiiBune([[H1, H2] | T], [[H1, H2] | Rez]):-
    sumaCifreElemente(H1, S1),
    sumaCifreElemente(H2, S2),
    S1 =:= S2,
    !,
    partitiiBune(T, Rez).

partitiiBune([_ | T], Rez):-
    partitiiBune(T, Rez).


principal(L, Rez):-
    findall(S, subliste(L, S), AllS),
    partitii(AllS, L, AllPart),
    partitiiBune(AllPart, Rez).
