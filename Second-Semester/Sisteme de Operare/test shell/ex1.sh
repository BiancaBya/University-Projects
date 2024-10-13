#!/bin/bash
#1. Să se scrie un script bash care primeşte ca argument un număr natural N şi generează N fişiere de tip text, astfel:
#- numele fişierelor vor fi de forma: file_X.txt, unde X = {1, 2, ..., N}
#- fiecare fişier generat va conţine doar liniile de la X la X + 5 ale fişierului passwd.fake

# ./ex1.sh N (cum se apeleaza)

#verificam numarul de argumente
if [ $# -eq 0 ]
then
	echo "Numar insuficient de argumente!"
	echo 'Utilizare: ' $0 'numar_natural'
	exit 1
fi

n=$1  # nr furnizat in linia de comanda

# seq -> secventa de numere de la 1 la $n = valoarea variabilei n
# trebuie $ inainte de paranteze ca sa ia valorile de la 1 la n
for x in $(seq 1 $n)
do
	# $x -> se inlocuieste cu valoarea lui x
	touch "file_$x.txt"
	# > file -> output-ul merge in file
	sed -n ''$x',+5p' passwd.fake > "file_$x.txt"
done



exit 0
