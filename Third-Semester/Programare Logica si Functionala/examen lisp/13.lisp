;a) Sa se intercaleze un element pe pozitia a n-a a unei liste liniare. 



(defun inserare (elem poz lista)
    (cond
        ((equal poz 1) (cons elem lista))
        (t (cons (car lista) (inserare elem (- poz 1) (cdr lista))))
    )
)


(print (inserare 4 4 '(1 2 3 5 6)))





; interclasare liste

(defun interclasare (l1 l2)
    (cond
        ((null l1) l2)
        ((null l2) l1)
        ((< (car l1) (car l2)) (cons (car l1) (interclasare (cdr l1) l2)))
        (t (cons (car l2) (interclasare l1 (cdr l2))))
    )
)






;b) Sa se construiasca o functie care intoarce suma atomilor numerici dintr-o lista, de la orice nivel. 



(defun suma (lista)
    (cond
        ((null lista) 0)
        ((listp (car lista)) (+ (suma (car lista)) (suma (cdr lista))))
        ((numberp (car lista)) (+ (car lista) (suma (cdr lista))))
        (t (suma (cdr lista)))
    )
)



(print (suma '(1 2 a 3 (4 5 b (6 c) (7 d (8 g 9) f)) g)))










;c) Sa se scrie o functie care intoarce multimea tuturor sublistelor unei 
;liste date. Ex: Ptr. lista ((1 2 3) ((4 5) 6)) => ((1 2 3) (4 5) ((4 5) 6)) 



(defun subliste (lista)
    (cond
        ((null lista) (list))
        ((listp (car lista)) (append (cons (car lista) (subliste (car lista))) (subliste (cdr lista))))
        (t (subliste (cdr lista)))
    )
)



(print (subliste '((1 2 3) ((4 5) 6))))


(print (subliste '((1 2 3) 4 (5 6 (7 8 (9))) (2 3 (4 5)))))











;d) Sa se scrie o functie care testeaza egalitatea a doua multimi, fara sa se 
;faca apel la diferenta a doua multimi.


(defun my_member(elem lista)
    (cond
        ((null lista) nil)
        ((equal (car lista) elem) T)
        (t (my_member elem (cdr lista)))
    )
)


(defun verificare_apartine (m1 m2)
    (cond
        ((null m1) T)
        ((member (car m1) m2) (verificare_apartine (cdr m1) m2))
        (t NIL)
    )
)

(defun verificare (m1 m2)
    (cond
        ((and (verificare_apartine m1 m2) (verificare_apartine m2 m1)) T)
        (T NIL)
    )
)

(print (verificare '(4 3 2 1 5) '(1 2 5 4 3)))

(print (verificare '(4 3 2 1 6 5) '(1 2 5 4 3)))



