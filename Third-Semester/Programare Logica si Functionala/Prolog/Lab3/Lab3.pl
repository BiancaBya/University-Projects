% Predicatul principal
principal(N, Rez) :-
    Lg is 2 * N,
    subsir(1, Lg, [0], 0, Rez). % Începe cu [0] și construiește restul

% Cazul de bază: dacă am ajuns la lungimea buna, punem 0 la final
subsir(Lg, Lg, Acc, _, Rez) :-
    Rez = [0|Acc]. % Adaugă un 0


%Cazuri pentru last = 0

subsir(Index, Lg, Acc, 0, Rez) :-
    Index < Lg,
    NextIndex is Index + 1,
    subsir(NextIndex, Lg, [1 | Acc], 1, Rez).

subsir(Index, Lg, Acc, 0, Rez) :-
    Index < Lg,
    NextIndex is Index + 1,
    subsir(NextIndex, Lg, [-1 | Acc], -1, Rez).



%Cazuri pentru last = 1

subsir(Index, Lg, Acc, 1, Rez) :-
    Index < Lg,
    NextIndex is Index + 1,
    subsir(NextIndex, Lg, [0 | Acc], 0, Rez).

subsir(Index, Lg, Acc, 1, Rez) :-
    Index < Lg,
    NextIndex is Index + 1,
    subsir(NextIndex, Lg, [-1 | Acc], -1, Rez).


%Cazuri pentru care last = -1

subsir(Index, Lg, Acc, -1, Rez) :-
    Index < Lg,
    NextIndex is Index + 1,
    subsir(NextIndex, Lg, [0 | Acc], 0, Rez).

subsir(Index, Lg, Acc, -1, Rez) :-
    Index < Lg,
    NextIndex is Index + 1,
    subsir(NextIndex, Lg, [1 | Acc], 1, Rez).
