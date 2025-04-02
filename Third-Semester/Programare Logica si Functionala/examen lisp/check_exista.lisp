; exista (l elem) = 
; = 1, l = elem si l = atom
; = exista(l1 elem) + exista(l2 elem) + ... + exista(ln elem), l = list, l = l1...ln
; = 0 , altfel


; main (l elem) =
; F, exista(l elem) = 0
; T, altfel 


(defun exista (lista element)
    (cond
        ((and (atom lista) (equal lista element)) 1)
        ((listp lista) (apply #'+ (mapcar #'(lambda(x) (exista x element)) lista)))
        (T 0)
    )
)


(defun main (lista element)
    (cond
        ((equal (exista lista element) 0) NIL)
        (T T)
    )
)



(print (main '(1 2 3 4 5) 3))

(print (main '(1 2 3 4 5) 6))

(print (main '(1 (2 3) 4 5) 3))

(print (main '((1 2 (3) 4) 5) 3))

(print (main '(1 (2 3) 4 5) 6))

(print (main '((1 2 (3) 4) 5) 6))

