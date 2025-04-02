; a) Sa se scrie o functie care intoarce diferenta a doua multimi. 

(defun diferenta_multimi (multime1 multime2)
  (cond
    ((null multime1) nil) 
    ((membru (car multime1) multime2) 
     (diferenta_multimi (cdr multime1) multime2)) ; dacă primul element din m1 este în m2, îl ignorăm
    (t
     (cons (car multime1) (diferenta_multimi (cdr multime1) multime2))))) ; altfel, îl adăugăm în rez

(defun membru (elem multime)
  ;my_member
  (cond
    ((null multime) nil) 
    ((equal elem (car multime)) t) 
    (t (membru elem (cdr multime))))) 


(print (diferenta_multimi '(1 2 3 4) '(3 4 5 6)))







; b) Definiti o functie care inverseaza o lista impreuna cu toate sublistele sale de pe orice nivel.

(defun inverseaza_ajutor (lst acc)
  (if (null lst)
      acc  
      (inverseaza_ajutor (cdr lst) 
                         (cons (if (listp (car lst)) 
                                  (inverseaza_ajutor (car lst) nil)  ; dacă este sublistă, inversare
                                  (car lst))  ; dacă nu, îl adăugăm
                                acc))))  ; add elem la începutul listei acc


(defun inverseaza (lst)
  (inverseaza_ajutor lst nil))  ; listă acc goală



(print (inverseaza '(1 2 (3 4) (5 (6 7)) 8)))






; c) Dandu-se o lista, sa se construiasca lista primelor elemente ale tuturor 
; elementelor lista ce au un numar impar de elemente la nivel superficial. 
; Exemplu: (1 2 (3 (4 5) (6 7)) 8 (9 10 11)) => (1 3 9).


(defun procesare (lst adaugat-primul-element)
  (cond
    ((null lst) nil)  
    ((and (not adaugat-primul-element) (oddp (length lst)))  ; lungime ipară si nu s-a procesat primul el
     (cons (car lst) (procesare (cdr lst) t)))  ; add primul element și se proceseaza restul
    ((and (listp (car lst)) (oddp (length (car lst))))  ; sublista are lungime impară
     (append (procesare (car lst) nil)  ; adaugam la rez procesarea sublistei
             (procesare (cdr lst) adaugat-primul-element)))  ; procesare restul listei
    (t (procesare (cdr lst) t))))  


(defun principal (lst)
  (procesare lst nil))

(print (principal '(1 2 (3 (4 5 6) (6 7)) 8 (9 10 11))))


(print ( principal '(1 2 (3 (4 5) (6 7)) 8 (9 10 11))))





; d) Sa se construiasca o functie care intoarce suma atomilor numerici
; dintr-o lista, de la nivelul superficial.

(defun suma (lst)
    (cond
        ((null lst) 0) ;daca lista e vida, incepe suma cu 0
        ((numberp (car lst)) (+ (car lst) (suma (cdr lst)))) ;daca primul element e numar, il aduman la rez
        (t (suma (cdr lst))) ;altfel, il ignoram
    )
)

(print(suma '(1 2 3 4)))














