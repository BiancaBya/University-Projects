(defun remove_div3 (lista rez)
    (cond
        ((and (numberp lista) (equal 0 (mod lista 3))) nil)
        ((atom lista) (cons lista rez))
        ((listp lista) (list (mapcan #'(lambda(l)(remove_div3 l rez)) lista)))
    )
)



(defun wrapper (lista)
    (car (remove_div3 lista nil))
)



(defun G (L)
    (list (car L)(car L))
)

