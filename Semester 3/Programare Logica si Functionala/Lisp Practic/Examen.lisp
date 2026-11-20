;(load '"C:/Users/Next/Desktop/PLF/examen lisp/Practic/Examen.lisp")


; nr_aparitii (l1,l2...ln, elem) = 
; 0, l = []
; nr_aparitii(l1) + nr_aparitii(l2...ln), l1 = lista
; nr_aparitii(l2...ln) + 1, l1 = atom si l1 = elem
; nr_aparitii(l2...ln), altfel

(defun nr_aparitii (lista elem)
    (cond
        ((null lista) 0)
        ((listp (car lista)) (+ (nr_aparitii (car lista) elem) (nr_aparitii (cdr lista) elem)))
        ((equal (car lista) elem) (+ 1 (nr_aparitii (cdr lista) elem)))
        (T (nr_aparitii (cdr lista) elem))
    )
)


; elimina(l1,l2...ln LInit) =
; [], l = []
; elimina (l2...ln, LInit), l1 = atom si nr_aparitii(LInit, l1) > 1
; l1 + elimina (l2...ln, LInit), l1 = atom si nr_aparitii(LInit, l1) = 1
; elimina (l1, LInit) + elimina (l2...ln, LInit), l1 = list

(defun elimina (lista lstinit)
    (cond
        ((null lista) (list))
        ((and (atom (car lista)) (> (nr_aparitii lstinit (car lista)) 1)) (elimina (cdr lista) lstinit))
        ((atom (car lista)) (cons (car lista) (elimina (cdr lista) lstinit)))
        ((listp (car lista)) (cons (elimina (car lista) lstinit) (elimina (cdr lista) lstinit)))
    )
)


; main (l)=
; elimina (l,l)

(defun main (lista)
    (elimina lista lista)
)



