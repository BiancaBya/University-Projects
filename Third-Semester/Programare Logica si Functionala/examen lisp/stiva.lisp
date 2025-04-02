(defun pushElement (element stack)
	(APPEND stack (LIST element))
)

;; returneaza primul element din stiva
(defun popElement (stack)
	(cond
	((NULL stack) NIL)
	((NULL (CDR stack)) (CAR stack))
	(t (popElement (CDR stack)))
	)
)

;; returneaza stiva dupa stergerea primului element
(defun popStack (stack)
	(cond
	((NULL stack) NIL)
	((NULL (CDR stack)) NIL)
	(t (CONS (CAR stack) (popStack (CDR stack))))
	)
)

(defun isOperator (x)
	(cond
	((EQUAL x '+) t)
	((EQUAL x '-) t)
	((EQUAL x '*) t)
	((EQUAL x '/) t)
	)
)

(defun computeOp (operator op1 op2)
	(cond
	((EQUAL operator '+) (+ op1 op2))
	((EQUAL operator '-) (- op1 op2))
	((EQUAL operator ') ( op1 op2))
	((EQUAL operator '/) (/ op1 op2))
	)
)

(defun computeExpr (expr stack)
	(cond
	((NULL expr) (CAR stack))
	((isOperator (popElement expr)) 
		(computeExpr 
		(popStack expr)
		(pushElement (computeOp (popElement expr) (popElement stack) (popElement (popStack stack))) (popStack (popStack stack)) )
		)
	)
	(t 
		(computeExpr 
		(popStack expr)
		(pushElement (popElement expr) stack)
		)
	)
	)
)
