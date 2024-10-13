#!/bin/bash
# 8. Să se scrie un script bash care are ca argumente triplete formate dintr-un nume de fisier, un cuvant si un numar k.
# Pentru fiecare astfel de triplet, se vor afişa toate liniile fişierului care contin cuvăntul respectiv exact de k ori.

if [ $# -eq 0 ]
then
	echo "Numar insuficient de argumente"
	echo "Utilizare" $0 "nume_fisier cuvant numar"
	exit 1
fi

while ! [ $# -eq 0 ]
do
	fisier=$1
	cuvant=$2
	k=$3
	if ! [ -f $fisier ]
	then
		echo "Argumentul" $fisier "nu este un fisier"
		exit 2
	fi

	#nr de linii ale fisierului
	nr_linii=$(wc -l < $fisier)
	echo "Nr de linii ale fisierului:" $nr_linii

	#parcurgem fiecare linie a fisierului
	for L in $(seq 1 $nr_linii)
	do
		# facem o copie a liniei
		linie=$(sed -n ''$L'p' $fisier)
		aparitii=$(echo $linie | grep -E '\<'$cuvant'\>' | wc -l)
		if [ $aparitii -eq $k ]
		then
			echo "nr_linie:" $L
			echo "Linia:" $linie
		fi
	done

	shift 3 # eliminam tripletul care a fost prelucrat
done

exit 0
