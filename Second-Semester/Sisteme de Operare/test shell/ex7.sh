#!/bin/bash
# 7. Să se scrie un script bash care primește ca argumente oricâte nume de fișiere.
# Scriptul va citi de la tastatură două cuvinte și va înlocui TOATE aparițiile primului cuvânt
# cu cel de-al doilea în fiecare fișier furnizat ca argument.

if [ $# -eq 0 ]
then
	echo "Numar insuficient de argumente"
	echo "Utilizare" $0 "nume fisiere"
	exit 1
fi

read -p "cuvant 1:" cuvant1
read -p "cuvant 2:" cuvant2

for nume in $*
do
	if [ -f $nume ]
	then
		cat $nume | sed 's/\<'$cuvant1'\>/'$cuvant2'/g'
	else
		echo "Argumentul" $nume "nu este un fisier"
		exit 2
	fi
done
exit 0
