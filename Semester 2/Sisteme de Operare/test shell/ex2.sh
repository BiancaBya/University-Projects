#!/bin/bash
# 2. Să se scrie un script bash care afișează primele 3 linii și ultimele 3 linii
# ale fiecărui fișier de tip text din dintr-un director dat ca argument
# în linia de comandă. Dacă un fișier text are mai puțin de 6 linii,
# atunci conţinutul acestuia va fi afişat în întregime.
# (comenzi: find, file, grep, wc, head, tail)
# nume director = dir
if [ $# -eq 0 ]
then
	echo "Numar insuficient de argumente!"
	echo "Utilizare: " $0 " nume_director"
	exit 1
fi

DIR=$1
# find $DIR -type f -> cauta in director toate fisierele (type -f de la file)
for FILE in $(find $DIR -type f)
do
	if file $FILE | grep -q 'ASCII text$' # -q = sa nu afiseze outputul pe ecran
	then
		nr=$(cat $FILE | wc -l)
		if [ $nr -le 6 ]
		then
			cat -n $FILE # -n ca sa puna numarul liniei in fata fiecarei linii
		else
			echo -e "____Primele 3 linii____\n"
			# -n ca sa  nu afiseze si fluxul de intrare
			sed -n '1,3p' $FILE # se poate si cu head
			echo -e "____Ultimele 3 linii____\n"
			tail -3 $FILE
		fi
	fi
done
exit 0
