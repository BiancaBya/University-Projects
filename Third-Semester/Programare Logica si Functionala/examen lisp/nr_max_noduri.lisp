(defun stang (arb)
	(CADR arb)
)

(defun drept (arb)
	(CADDR arb)
)

#|
Parcurgere arborele pe nivele, si returneaza lista doar cu nodurile de la nivelul specificat (nivel)
|#
(defun parcurgereNivel (arb nivel nivelCurent rezultat)
	(cond
	((NULL arb) NIL)
	((= nivel nivelCurent) (CONS (CAR arb) rezultat))
	(t
		(APPEND
		  (parcurgereNivel (stang arb) nivel (+ 1 nivelCurent) rezultat)
		  (parcurgereNivel (drept arb) nivel (+ 1 nivelCurent) rezultat)
		)
	)
	)
)

(defun getNivel (arb nivel)
	(parcurgereNivel arb nivel 0 '())
)

(defun maxim (a b)
	(cond
	((> a b) a)
	(t b)
	)
)

(defun nivelMax (arb nivelCurent maxNoduri maxNivel)
	(cond
	((NULL (getNivel arb nivelCurent)) maxNivel)
	((> (LENGTH (getNivel arb nivelCurent)) maxNoduri) (nivelMax arb (+ 1 nivelCurent) (LENGTH (getNivel arb nivelCurent)) nivelCurent))
	(t (nivelMax arb (+ 1 nivelCurent) maxNoduri maxNivel))
	)
)

(defun main (arb)
	(CONS 
		(nivelMax arb 0 0 0)
		(getNivel arb (nivelMax arb 0 0 0))
	)
)
