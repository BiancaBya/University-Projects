#!/bin/bash
#10. Să se scrie un script bash care va afişa toate numele de fişiere
#dintr-un director dat ca argument şi din subdirectoarele sale,
#care au numele mai scurte de 8 caractere.
#Pentru acestea, dacă sunt fişiere text, li se vor afişa primele 10 linii.

if [ $# -eq 0 ]
then
	echo "Numar insuficient de argumente"
	echo "Utilizare" $0 "nume_director"
	exit 1
fi

DIR=$1

if ! [ -d $DIR ]
then
	echo "Argumentul" $DIR "nu este un director"
	exit 2
fi

FILES=$(find $DIR -type f)
for FILE in $FILES
do
	nume=$(basename "$FILE")
	nr=${#nume}
	if [ $nr -lt 8 ]
	then
		echo "Numele fisierului:" $nume
		# se pune comanda file ca sa dea tipul fisierului
		if file $FILE | grep -q 'ASCII text'
		then
			echo "Primele 10 linii sunt:"
			head -n 10 $FILE
		fi
	fi
	echo
done

exit 0
