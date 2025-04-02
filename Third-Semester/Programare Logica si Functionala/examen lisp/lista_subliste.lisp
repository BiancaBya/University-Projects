(defun subliste (lista)
    (cond
        ((null lista) (list))
        ((listp lista) (cons lista (mapcan #'subliste (cdr lista))))
        ((atom lista) nil)
    )
)

