;9. Sa se converteasca un arbore de tipul (1) la un arbore de tipul (2).

(defun parcurgere_stanga (l nrNoduri nrMuchii)
  (cond
    ((null l) nil)
    ((= nrNoduri ( + 1 nrMuchii)) nil) ; cand parcurge tot arborele se opreste
    (t (cons (car l) (cons (cadr l) (parcurgere_stanga (cddr l) (+ 1 nrNoduri) (+ (cadr l) nrMuchii)))))
    ; nodul curent, nr de subarbori, apel cu restul listei 
  )
)


(defun parcurgere_dreapta (l nrNoduri nrMuchii)
  (cond
    ((null l) nil)
    ((= nrNoduri (+ 1 nrMuchii)) l)
    (t (parcurgere_dreapta (cddr l) (+ 1 nrNoduri) (+ (cadr l) nrMuchii)))
  )
)


(defun stang(l)
  (parcurgere_stanga (cddr l) 0 0)
)


(defun drept(l)
  (parcurgere_dreapta (cddr l) 0 0)
)


(defun convert(l)
  (cond
    ((null l) nil)
    ; daca are doi subarbori, apeleaza functiile de drept si stang pentru ambii subarbori
    ((equal (cadr l) 2) (append (list (car l)) (cons (convert(stang l)) (list (convert(drept l))))))
    ; daca are doar un subarbore, apeleaza doar stanga
    ((equal (cadr l) 1) (append (list (car l)) (list (convert(stang l)))))
    ; daca nu are subarbori, returneaza doar nodul
    (t (list (car l)))
  )
)


(print(convert '(A 2 B 0 C 2 D 0 E 0)))

(print(convert '(a 2 b 2 c 1 i 0 f 1 g 0 d 2 e 0 h 0)))

(print(convert '(A 2 B 2 D 2 F 1 K 0 G 0 E 0 C 1 H 1 I 1 J 0)))

