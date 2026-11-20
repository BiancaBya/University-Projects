; atomul numeric maxim de pe orice nivel impar, este par


(defun max_niv_impar (lista nivel)
    (cond
        ((null lista) -100001)
        ((listp (car lista)) (max (max_niv_impar (car lista) (+ nivel 1)) (max_niv_impar (cdr lista) nivel)))
        ((and (numberp (car lista)) (oddp nivel)) (max (car lista) (max_niv_impar (cdr lista) nivel)))
        (T (max_niv_impar (cdr lista) nivel))
    )
)


(defun verif_max (lista)
    (cond
        ((evenp (max_niv_impar lista 1)) 1)
        (T 0)    
    )
)


(defun lista_mapcar (lista)
    (cond
        ((listp lista) (+ (verif_max lista) (apply #'+ (mapcar #'lista_mapcar lista))))
        (T 0)
    )
)


(defun main (lista)
    (cond
        ((= (verif_max lista) 1) (- (lista_mapcar lista) 1))
        (T (lista_mapcar lista))
    )
)


;mapcon #'list(1 2) -> ((1 2) (2))
;mapcon #'list(2) -> ((2))
;append -> (1 2 2 2)


