#!/bin/bash
# 6. Să se scrie un script bash care primește ca argumente oricâte nume de fișiere.
# Scriptul va citi de la tastatură un cuvânt, va determina și afișa numărul de apariţii
# ale cuvântului în fiecare fișier furnizat ca argument.

if [ $# -eq 0 ]
then
	echo "Numar insuficient de argumente"
	echo "Utilizare:" $0 "nume_fisiere"
	exit 1
fi

# -p -> afiseaza un mesaj, mesajul e ce e intre ghilimele
# in variabila cuvant se salveaza input-ul
read -p "Cuvant:" cuvant

for nume in $*
do
	if [ -f $nume ]
	then
		numar=$(cat $nume | grep -E '\<'$cuvant'\>' | wc -l)
		echo $numar
	else
		echo "Argumentul" $nume "nu este un fisier"
		exit 2
	fi
done

exit 0
