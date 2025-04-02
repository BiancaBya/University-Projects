; 4 V1
; mai trebuie inversata
(defun cale (a nod rez)
	(cond
        ((EQUAL (CAR a) nod) (CONS nod rez))
        ((LISTP a) (MAPCAN #'(LAMBDA (arb)(cale arb nod (CONS (CAR a) rez)))(CDR a)))
	)
)

; 4 V2
(defun apartine_arbore(l elem)
    (cond
        ((null l) nil)
        ((listp (car l))  (OR (apartine_arbore (car l) elem) (apartine_arbore (cdr l) elem)))
        ((equal (car l) elem) t)
        (t (apartine_arbore (cdr l) elem))
    )
)

(defun path(l elem)
    (cond
        ((and (listp l) (apartine_arbore l elem))
            (append (list (car l)) (mapcar #'(lambda (x)
                                            (path x elem)
                                      ) l
                            )
            )
        )
    )
)

