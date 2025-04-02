(defun adancime (l)
    (cond
    ((NULL (CDR l)) 0)
    ((LISTP l) (+ 1 (APPLY #'MAX (MAPCAR #'adancime (CDR l)))))
    )
)

(defun esteEchilibrat (arb)
    (cond
    ((NULL (CADR arb)) 
        (cond
        ((<= (ABS (- (adancime (CADR arb)) (+ (adancime (CADDR arb)) 1))) 1) T)
        (t NIL)
        )
    )
    ((NULL (CADDR arb)) 
        (cond
        ((<= (ABS (- (+ (adancime (CADR arb)) 1) (adancime (CADDR arb)))) 1) T)
        (t NIL)
        )
    )
    ((<= (ABS (- (adancime (CADR arb)) (adancime (CADDR arb)))) 1) T)
    (t NIL)
    )
)

