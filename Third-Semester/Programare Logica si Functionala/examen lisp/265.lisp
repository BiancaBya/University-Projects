(defun lista_noduri (arb nivel k)
    (cond
        ((listp arb) (append (lista_noduri (car arb) nivel k) (mapcan #'(lambda(x) (lista_noduri x (+ nivel 1 ) k))(cdr arb))))
        ((and (atom arb) (= nivel k)) (list arb))
        (T nil)
    )
)



(defun principal (lista k)
    (lista_noduri lista 0 k)
)

