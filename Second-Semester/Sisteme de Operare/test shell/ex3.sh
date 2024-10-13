#!/bin/bash
## 3. Să se scrie un script bash care calculează numărul mediu de linii
# ale fișierelor de tip text dintr-un director dat ca argument.
# (comenzi: find, file, wc)

if [ $# -eq 0 ]
then
	echo "Numar insuficient de argumente"
	echo "Utilizare: " $0 " nume_director"
	exit 1
fi

DIR=$1
nr_total_linii=0
nr_fisiere=0
nr_mediu=0

FILES=$(find $DIR -type f) # gaseste in director fisierele

for FILE in $FILES
do
	if file $FILE | grep -q 'ASCII text'
	then
		nr_linii=$(cat $FILE | wc -l)
		nr_total_linii=$(($nr_total_linii+$nr_linii))
		nr_fisiere=$(($nr_fisiere+1)) # trebuie paranteze duble
		echo -e "nr_fisiere $nr_fisiere\nnr_total $nr_total_linii\nnr_linii $nr_linii\n"
	fi
done
nr_mediu=$(($nr_total_linii/$nr_fisiere))
echo "Numarul mediu de linii este $nr_mediu"
exit 0
