(defun noduriNivel (arb nivel k)
    (cond
        ((listp arb) (append (noduriNivel (car arb) nivel k)
                        (mapcan #'(lambda(x) (nodurinivel x (+ nivel 1) k))(cdr arb))))
        ((equal nivel k) (list arb))                
        (T NIL)                
    )
)


(defun principal (arb k)
    (nodurinivel arb 0 k)
)


