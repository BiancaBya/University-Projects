#!/bin/bash

# 5. Să se scrie un script bash care pentru fiecare argument din linia de comandă:
# - dacă e fișier, va afișa numele, numărul de caractere și de linii (în această ordine)
# - dacă e director, va afișa numele și câte fișiere conține (inclusiv în subdirectoare).
# (comenzi: test, wc, awk, find)

if [ $# -eq 0 ]
then
	echo "Numar insuficient de argumente"
	echo "Utilizare" $0 "argumente"
	exit 1
fi

for nume in $*
do
	if [ -d $nume ]
	then
		echo "Numele directorului este: " $nume
		numar_fisiere=$(find $nume -type f | wc -l)
		echo "Numar fisiere:" $numar_fisiere

	elif [ -f $nume ]
	then
		echo "Numele fisierului este: " $nume
		numar_caractere=$(wc -m < $nume)
		echo "Numar de caractere:" $numar_caractere
		numar_linii=$(wc -l < $nume)
		echo "Numar de linii:" $numar_linii
	else
		echo "$nume nu este nici director nici fisier"
	fi
done

exit 0
