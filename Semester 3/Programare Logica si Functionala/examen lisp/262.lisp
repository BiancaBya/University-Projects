(defun inlocuire (lista nivel k)
    (cond
        ((listp lista) (apply #'list (mapcar #'(lambda(x) (inlocuire x (+ 1 nivel) k)) lista)))
        ((and (not (evenp nivel)) (numberp lista) (> lista k)) (- lista 1))
        (T lista)
    )
)


(defun principal (lista k)
    (inlocuire lista 0 k) ; se scade nivelu cu 1
)




