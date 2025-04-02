; (load '"C:/Users/Next/Desktop/PLF/examen lisp/L1/subiecte/R16_4.lisp")
(defun eliminare (lista element)
    (cond
        ((null lista) (list))
        ((listp (car lista)) (cons (eliminare (car lista) element) (eliminare(cdr lista) element)))
        ((equal (car lista) element) (eliminare (cdr lista) element))
        (T (cons (car lista) (eliminare (cdr lista) element)))
    )
)



(defun suma_niv_pare (lista nivel)
    (cond
        ((null lista) 0)
        ((listp (car lista)) (+ (suma_niv_pare (car lista) (+ nivel 1)) (suma_niv_pare (cdr lista) nivel)))
        ((not (numberp (car lista))) (suma_niv_pare (cdr lista) nivel))
        ((not (evenp nivel)) (+ (car lista) (suma_niv_pare (cdr lista) nivel)))
        (T (suma_niv_pare (cdr lista) nivel)) 
    )
)



(defun nr_liste (L)
    (cond
        ((listp L) 
            (cond
                ((evenp (mod (suma_niv_pare L 1) 2)) (+ 1 (apply #'+ (mapcar #'nr_liste L))))
                (T (apply #'+ (mapcar #'nr_liste L)))
            )
        )
        (T 0)
    )
)







