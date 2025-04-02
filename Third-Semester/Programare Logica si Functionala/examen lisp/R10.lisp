(defun verif_atom_imp (l este_imp)
    (cond
        ((null l) este_imp)
        ((listp (car l)) (verif_atom_imp (cdr l) (verif_atom_imp (car l) nil)))
        ((numberp (car l))
            (cond
            ((not (evenp (car l))) (verif_atom_imp (cdr l) T))
            (t (verif_atom_imp (cdr l) nil))
        ))
        (t (verif_atom_imp (cdr l) este_imp))
    )
)

(defun nr_subliste (l)
    (cond
        ((listp l) 
            (cond
                ((verif_atom_imp l nil) (+ 1 (apply #'+ (mapcar #'nr_subliste l))))
                (t (apply #'+ (mapcar #'nr_subliste l)))
            )
        )
        (t 0)
    )
)



(defun elem_din_n (lista n poz)
    (cond
        ((null lista) nil)
        ((equal n poz) (append (cons (car lista) (list(car lista))) (elem_din_n (cdr lista) n 1)))
        (t (cons (car lista) (elem_din_n (cdr lista) n (+ poz 1))))
    )
)


(defun principal (lista n)
    (elem_din_n lista n 1)
)

