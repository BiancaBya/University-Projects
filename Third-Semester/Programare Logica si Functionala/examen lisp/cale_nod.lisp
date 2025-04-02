(defun cale_nod (arb nod rez)
    (cond
        ((equal (car arb) nod) (cons nod rez))
        ((listp arb) (mapcan #'(lambda(x) (cale_nod x nod (cons (car arb) rez))) (cdr arb)))
    )
)


(defun invers (lista rez)
    (cond
        ((null lista) rez)
        (T (invers (cdr lista) (cons (car lista) rez)))
    )
)
