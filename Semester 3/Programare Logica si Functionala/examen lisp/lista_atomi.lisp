(defun atomi (lista)
    (cond
        ((null lista) (list))
        ((atom (car lista)) (append (list(car lista)) (atomi (cdr lista))))
        ((listp (car lista)) (append (atomi (car lista)) (atomi (cdr lista))))
    )
)

(defun atomi_invers (lista)
    (cond
        ((null lista) (list))
        ((atom (car lista)) (append (atomi_invers (cdr lista)) (list(car lista))))
        ((listp (car lista)) (append (atomi_invers (car lista)) (atomi_invers (cdr lista))))
    )
)

