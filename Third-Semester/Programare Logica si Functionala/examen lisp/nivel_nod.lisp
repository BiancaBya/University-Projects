(defun nivelNod (arb nivel nod)
    (cond
        ((and (atom arb) (equal arb nod)) nivel)
        ((listp arb) (+ (nivelNod (car arb) nivel nod)(apply #'+(mapcar #'(lambda(x) (nivelNod x (+ nivel 1) nod))(cdr arb)))))
        (T 0)
    )
)

(defun main (arb nod)
    (nivelNod arb 1 nod)
)

