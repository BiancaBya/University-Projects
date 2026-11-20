(defun permute (list)
    (cond 
    ((NOT (NULL list))
        (mapcan #'(lambda (x)
		        (mapcar #'(lambda (y) (cons x y))(permute (remove x list)))
                ) list))
    (t (LIST NIL))
    )
)
