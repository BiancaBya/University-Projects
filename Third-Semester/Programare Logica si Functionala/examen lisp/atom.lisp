; nr de subliste la care ultimul atom numeric de pe orice nivel par


(defun subliste_last_at_par (lista flag)
    (cond
        ((null lista) flag)
        ((and (numberp (car lista)) (evenp (car lista))) (subliste_last_at_par (cdr lista) T))
        ((and (numberp (car lista)) (oddp (car lista))) (subliste_last_at_par (cdr lista) NIL))
        ((listp (car lista)) (subliste_last_at_par (cdr lista) (subliste_last_at_par (car lista) NIL)))
        (T (subliste_last_at_par (cdr lista) flag))
    )
)



(defun nr_subliste (lista)
    (cond
        ((listp lista) 
            (cond
                ((subliste_last_at_par lista nil) (+ 1 (apply #'+ (mapcar #'nr_subliste lista))))
                (T (apply #'+ (mapcar #'nr_subliste lista)))
            )
        )
        (T 0)
    )
)


(defun principal_1 (lista) 
    (nr_subliste lista)
)



(defun subliste_orice (lista flag)
    (cond
        ((null lista) flag)
        ((and (numberp (car lista)) (evenp (car lista))) (subliste_orice (cdr lista) T))
        ((and (numberp (car lista)) (oddp (car lista))) (subliste_orice (cdr lista) NIL))
        ((listp (car lista)) (and (subliste_orice (car lista) NIL)(subliste_orice (cdr lista) NIL)))
        (T (subliste_orice (cdr lista) flag))
    )
)



(defun nr_subliste_orice (lista)
    (cond
        ((listp lista)
            (cond
                ()
            )
        )
        (T 0)
    )
)


