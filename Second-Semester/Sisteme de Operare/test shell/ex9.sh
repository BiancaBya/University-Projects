#!/bin/bash
#9. Să se scrie un script bash care afişează,
#pentru fiecare fişier dat ca argument,
#linia care apare de cele mai multe ori.
#Afişarea se va face în ordinea descrescătoare a numărului de apariţii.
#(comenzi: sort, uniq, head)

if [ $# -eq 0 ]
then
	echo "Numar insuficient de argumente"
	echo "Utilizare" $0 "nume_fisier"
	exit 1
fi

while ! [ $# -eq 0 ]
do
	fisier=$1

	# uniq -c = pune in fata liniei numarul de aparitii
	# sort -rn = sorteaza DESCRESCATOR dupa valorile NUMERICE, nu dupa caracterele din linie
	#head -n 1 = specifica ca se ia doar prima linie, obligatori -n!!

	linie=$(cat $fisier | sort | uniq -c | sort -rn | head -n 1)
	echo "Linia cu cele mai multe aparitii: " $linie
	echo # enter
	shift 1
done

exit 0
