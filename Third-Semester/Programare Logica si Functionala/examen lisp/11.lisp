; (load '"C:/Users/Next/Desktop/PLF/examen lisp/L1/11.lisp")
;a) Sa se determine cel mai mic multiplu comun al valorilor numerice dintr-o lista neliniara. 


(defun cmmdc (a b)
    (cond
        ((= a 0) b) 
        ((= b 0) a)
        ((< a b) (cmmdc a (- b a)))
        (t  (cmmdc (- a b) b))
    )
)


(defun cmmdc_lista (lista)
    (cond
        ((null lista) 0)
        ((numberp (car lista)) (cmmdc (car lista) (cmmdc_lista(cdr lista))))
        (t (cmmdc_lista(cdr lista))) 
    )
)


(print (cmmdc_lista '(6 48 a 5 12 b 24)))



(defun cmmmc (a b)
    (/ (* a b) (cmmdc a b))
)


(defun cmmmc_lista (lista)
    (cond
        ((null lista) 1)
        ((numberp (car lista)) (cmmmc (car lista) (cmmmc_lista(cdr lista))))
        (t (cmmmc_lista(cdr lista)))
    )
)


(print (cmmmc_lista '(6 A 18 B 5 C)))





;b) Sa se scrie o functie care sa testeze daca o lista liniara formata din 
;numere intregi are aspect de "munte"(o secvență se spune ca are aspect de 
;"munte" daca elementele cresc pana la un moment dat, apoi descresc. De 
;ex. 10 18 29 17 11 10). 



(defun verificare_primul (lista)
    (cond
        ((< (car lista) (cadr lista)) t)
        (t nil)
    )
)


; ok = 1 -> crescator
; ok = 2 -> descrescator

(defun munte_verificare (lista ok)
    (cond 
        ((null lista) nil)
        ((and (null (cdr lista)) (= ok 1)) nil)
        ((and (null (cdr lista)) (= ok 2)) t)
        ((and (< (car lista) (cadr lista)) (= ok 1)) (munte_verificare (cdr lista) 1))
        ((and (> (car lista) (cadr lista)) (= ok 1)) (munte_verificare (cdr lista) 2))
        ((and (> (car lista) (cadr lista)) (= ok 2)) (munte_verificare (cdr lista) 2))
        (t nil)
    )
)


(defun munte (lista)
    (cond
        ((null (verificare_primul lista)) nil)
        (t (munte_verificare lista 1))
    )
)


(print (munte '(1 2 3 4 3 2 1)))

(print (munte '(1 2 3 4)))

(print (munte '(4 3 2 1)))

(print (munte '(1 2 4 3 4 3 2 1)))

(print (munte '(1 2 3 4 3 4 2 1)))






;c) Sa se elimine toate aparitiile elementului numeric maxim  dintr-o lista neliniara. 


(defun elimina (lista elem)
    (cond
        ((null lista) nil)
        ((listp (car lista)) (cons (elimina (car lista) elem) (elimina (cdr lista) elem)))
        ((equal (car lista) elem) (elimina (cdr lista) elem))
        (t (cons (car lista) (elimina (cdr lista) elem)))
    )
)



(defun maxim (lista max)
    (cond 
        ((null lista) max)
        ((null max) (maxim (cdr lista) (car lista)))
        ((listp (car lista)) (maxim (cdr lista) (maxim (car lista) max)))
        ((and (numberp(car lista)) (> (car lista) max)) (maxim (cdr lista) (car lista)))
        (t (maxim (cdr lista) max))
    )
)



(defun eliminare_maxim (lista)
    (elimina lista (maxim_bun lista nil))
)

(print (eliminare_maxim '(1 2 (1 2 5 4) 4 (5 4) (1 2 (4 5)))))







(defun maxim_bun (lista maximul)
    (cond
        ((null lista) maximul)
        ((and (null maximul) (numberp (car lista)) (maxim_bun (cdr lista) (car lista))))
        ((and (null maximul) (listp (car lista))) (max (maxim_bun (car lista) maximul) (maxim_bun (cdr lista) maximul)))
        ((listp (car lista)) (maxim_bun (cdr lista) (maxim_bun (car lista) maximul)))
        ((and (numberp (car lista)) (> (car lista) maximul)) (maxim_bun (cdr lista) (car lista)))
        (t (maxim_bun (cdr lista) maximul))
    )
)


(print (maxim_bun '((e 1 2 a 3) 4 c 5 (6 d 7 (8 9 (100) 4 e)) 6 7 f) nil))






;d) Sa se construiasca o functie care intoarce produsul atomilor numerici pari 
;dintr-o lista, de la orice nivel. 



(defun produs (lista)
    (cond
        ((null lista) 1)
        ((listp (car lista)) (* (produs (car lista)) (produs (cdr lista))))
        ((and (numberp (car lista)) (equal 0 (mod (car lista) 2))) (* (car lista) (produs (cdr lista))))
        (t (produs (cdr lista)))
    )
)

(print (produs '(f 1 a (2 b (2 (a 2) 3 f) 3 d) 2 c)))






