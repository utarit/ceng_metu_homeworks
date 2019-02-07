:- module(hw5, [configuration / 3]).
:- [hardware].

areTheyClose(A, B) :- adjacent(A,B) ; adjacent(B,A).

isOut(S) :- sections(L), loop(S, L, 0).

check(put(_, A), [put(_, X)]) :- A \== X.
check(put(_ , A), [put(_, X)|T]) :- A \== X, check(put(_ , A), T).

configuration([], _, []).
configuration(A, B, C) :- conf(A, C), checkForRest(B, C).


conf([A],  [put(A, X)]) :- sections(L), member(X, L).
conf([H|T],  [put(H, X)|E]) :- sections(L), member(X, L), conf(T, E), check(put(H, X), E).

checkForRest([], _).
checkForRest([outer_edge(A) |T], P) :- member(put(A, S), P), isOut(S), checkForRest(T, P).
checkForRest([close_to(A, B) |T], P) :- member(put(A, S1), P), member(put(B, S2), P), areTheyClose(S1,S2), checkForRest(T, P).

loop(_, [], X) :- X < 3. 
loop(S, [H|T], X) :- areTheyClose(S, H) -> Y is X + 1, loop(S, T, Y) ; loop(S, T, X).


