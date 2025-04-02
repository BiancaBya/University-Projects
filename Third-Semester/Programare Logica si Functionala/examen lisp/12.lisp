; (load '"C:/Users/Next/Desktop/PLF/examen lisp/L1/12.lisp")
; a) Definiti o functie care intoarce produsul scalar a doi vectori. 



(defun produs (v1 v2)
    (cond
        ((or (null v1) (null v2)) 0)
        (t (+ (* (car v1) (car v2)) (produs (cdr v1) (cdr v2))))
    )
)



(print (produs '(1 2 3) '(1 2 3)))








;b) Sa se construiasca o functie care intoarce maximul atomilor numerici dintr-o lista, de la orice nivel. 


(defun maxim (lista max)
    (cond 
        ((null lista) max)
        ((null max) (maxim (cdr lista) (car lista)))
        ((listp (car lista)) (maxim (cdr lista) (maxim (car lista) max)))
        ((and (numberp(car lista)) (> (car lista) max)) (maxim (cdr lista) (car lista)))
        (t (maxim (cdr lista) max))
    )
)


(defun maxim_principal (lista)
    (maxim lista nil)
)



(print (maxim_principal '(1 2 3 (4 5 (3 2 (6)))) ))







;c) Sa se scrie o functie care intoarce lista permutarilor unei liste date. 

(defun permute (list)
    (cond 
    ((NOT (NULL list))
        (mapcan #'(lambda (x)
		        (mapcar #'(lambda (y) (cons x y))(permute (remove x list)))
                ) list))
    (t (LIST NIL))
    )
)











;d) Sa se scrie o functie care intoarce T daca o lista are numar par de 
;elemente pe primul nivel si NIL in caz contrar, fara sa se numere elementele listei. 




(defun numarare (lista ok)
    (cond 
        ((and (null lista) (equal ok 1)) T)
        ((and (null lista) (equal ok 0)) NIl)
        (t (numarare (cdr lista) (- 1 ok)))
    )
)


(defun apel (lista)
    (numarare lista 1)
)


(print (apel '(1 2 3 4)))
(print (apel '(1 2 3 4 5)))


