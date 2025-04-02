(defun cmmdc (a b)
    (cond
        ((= a 0) b)
        ((= b 0) a)
        ((> a b) (cmmdc (- a b) b))
        (t (cmmdc a (- b a)))
    )
)


(defun primul_nr (lista nivel)
    (cond
        ((null lista) nil)
        ((and (evenp nivel) (and (numberp (car lista)) (not (evenp (car lista))))) (car lista))
        ((listp (car lista)) (cond 
                ((not (null (primul_nr (car lista) (+ nivel 1)))) (primul_nr (car lista) (+ nivel 1)))
                (T (primul_nr (cdr lista) nivel))
            ))
        (T (primul_nr (cdr lista) nivel))
    )
)


(defun cmmdc_lista (lista nivel linit)
    (cond
        ((null lista) (primul_nr linit 1))
        ((and (evenp nivel) (and (numberp (car lista)) (not (evenp (car lista))))) (cmmdc (car lista) (cmmdc_lista (cdr lista) nivel linit)))
        ((listp (car lista)) (cmmdc (cmmdc_lista (car lista) (+ 1 nivel) linit) (cmmdc_lista (cdr lista) nivel linit)))
        (T (cmmdc_lista (cdr lista) nivel linit))
    )
)



(defun main (lista)
    (cmmdc_lista lista 1 lista)
)







(defun adancime(a nivel)
    (cond
        ((LISTP a) (APPLY #'MAX (CONS (adancime (CAR a) nivel) (MAPCAR #'(LAMBDA (X)(adancime X (+ 1 nivel))) (CDR a)))))
        (t nivel)
    )
)

(defun inaltime (a nod)
    (cond
        ((LISTP a)
            (cond
            ((EQUAL (CAR a) nod) (adancime a 0))
            (t (APPLY #'MAX (CONS (inaltime (CAR a) nod) (MAPCAR #'(LAMBDA (X)(inaltime X nod)) (CDR a)))))
            )
        )
        (t -1)
    )
)



 (a (b (g)) (c (d (e)) (f)))
 c
 max(
 inaltime(a) => -1
inaltime((b (g)))
 =>
 max(
    inaltime(b) => -1
    inaltime((g)) => -1
 )
inaltime((c (d (e)) (f)))
=> adancime((c (d (e)) (f)) 0)
max(
    adancime(c 0) => 0
    adancime((d (e)) 1) => 
    max
    => 1
    => 2
    adancime((f) 1) => 1
) => 2
)=> 2


