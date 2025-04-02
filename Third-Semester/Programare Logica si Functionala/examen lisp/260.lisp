
(defun inlocuire (lista nivel)
    (cond
        ((listp lista) (apply #'list (mapcar #'(lambda(x) (inlocuire x (+ 1 nivel))) lista)))
        ((and (evenp nivel) (numberp lista) (not (evenp lista))) (+ 1 lista))
        (T lista)
    )
)


(defun principal (lista)
    (inlocuire lista 0) ; se pune nivelu din cerinta
)





