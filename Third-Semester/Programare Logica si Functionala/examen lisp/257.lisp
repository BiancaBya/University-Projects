(defun inaltime_nod(a e gasit)
    (cond
    ((atom a) -1)
    ((AND (listp a) (equal gasit NIL) (not (equal e (car a)))) (apply #'max(mapcar #'(lambda (x)(inaltime_nod x e NIL)) a)))
    (t (+ 1 (apply #'max(mapcar #'(lambda (x)(inaltime_nod x e T)) a))) )
    )
)

; main(a, e) = inaltime_nod(a, e, NIL)
; main(a:list, e:element)
(defun main(l e)
(inaltime_nod l e NIL)
)




(defun F (x &rest y)
    (cond
        ((null y) x)
        (t (append x (mapcar #'car y)))
    )
)   
