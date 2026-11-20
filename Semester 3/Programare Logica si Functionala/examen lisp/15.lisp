;(load '"C:/Users/Next/Desktop/PLF/examen lisp/L1/15.lisp")

;a) Sa se scrie o functie care intoarce reuniunea a doua multimi. 


(defun reuniune (m1 m2)
    (cond 
        ((null m1) m2)
        ((member (car m1) m2) (reuniune (cdr m1) m2))
        (t (reuniune (cdr m1) (cons (car m1) m2)))
    )
)



(print (reuniune '(1 2 3 4) '(3 4 5 6)))

















;b) Sa se construiasca o functie care intoarce produsul atomilor numerici dintr-o lista, de la orice nivel. 




(defun produs (lista)
    (cond
        ((null lista) 1)
        ((listp (car lista)) (* (produs (car lista)) (produs (cdr lista))))
        ((numberp (car lista)) (* (car lista) (produs (cdr lista))))
        (t (produs (cdr lista)))
    )
)

 
(print (produs '(1 2 a 3 (2 2 (3 b) (2 c 2 (2 d 3)) f) g))) 













;c) Definiti o functie care sorteaza cu pastrarea dublurilor o lista liniara. 


; cu pastrarea dublurilor

(defun inserare_sortare (elem lista)
    (cond
        ((null lista) (list elem))
        ((< (car lista) elem) (cons (car lista) (inserare_sortare elem (cdr lista))))
        (t (cons elem lista))
    )
)


(defun sortare_cu_dubluri (lista)
    (cond
        ((null (cdr lista)) (list (car lista)))
        (t (inserare_sortare (car lista) (sortare_cu_dubluri (cdr lista))))
    )
)


(print (sortare_cu_dubluri '(3 1 4 5 2)))

(print (sortare_cu_dubluri '(3 1 4 4 5 1 2)))




; fara pastrarea dublurilor


(defun inserare_fara_dubluri (elem lista)
    (cond
        ((null lista) (list elem))
        ((< (car lista) elem) (cons (car lista) (inserare_fara_dubluri elem (cdr lista))))
        ((equal (car lista) elem) (inserare_fara_dubluri elem (cdr lista)))
        (t (cons elem lista))
    )
)



(defun sortare_fara_dubluri (lista)
    (cond
        ((null (cdr lista)) (list (car lista)))
        (t (inserare_fara_dubluri (car lista) (sortare_fara_dubluri (cdr lista))))
    )
)


(print (sortare_fara_dubluri '(3 1 4 5 2)))

(print (sortare_fara_dubluri '(3 1 4 4 5 1 2)))




;d) Definiti o functie care construiește o listă cu pozițiile elementului 
;minim dintr-o listă liniară numerică.




(defun minim (lista minimul)
    (cond
        ((null lista) minimul)
        ((and (null minimul) (numberp (car lista))) (minim (cdr lista) (car lista)))
        ((and (null minimul) (listp (car lista))) (min (minim (car lista) minimul) (minim (cdr lista) minimul)))
        ((listp (car lista)) (minim (cdr lista) (minim (car lista) minimul)))
        ((and (numberp (car lista)) (< (car lista) minimul)) (minim (cdr lista) (car lista)))
        (t (minim (cdr lista) minimul))
    )
)


(print (minim '((4 a 3 5) 2 b 3 (6 5 c (7 8 (1 b 2) 3) 6) (4 e 5) 3) nil))

(print (minim '(4 3 2 1 5) nil))



(defun pozitii (lista elem poz)
    (cond
        ((null lista) (list))
        ((equal (car lista) elem) (cons poz (pozitii (cdr lista) elem (+ poz 1))))
        (t (pozitii (cdr lista) elem (+ poz 1)))
    )
)



(defun principal (lista)
    (pozitii lista (minim lista nil) 1)
)



(print (principal '(4 1 3 2 1 5 1)))


