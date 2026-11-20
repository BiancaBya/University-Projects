#!/bin/bash
#11. Să se scrie un script bash care pentru fiecare fişier cu drepturile 755 dintr-un director
# (şi subdirectoarele sale) dat ca argument îi va modifica drepturile de acces în 744.
# Înainte de a modifica drepturile de acces, script-ul va cere confirmarea
# din partea utilizatorului (pentru fiecare fişier în parte).
#(comenzi: find, chmod, read)

if [ $# -eq 0 ]
then
	echo "ARG insuficiente"
	exit 1
fi

DIR=$(find ~ -name $1 -type d)

if ! [ -d $DIR ]
then
	echo "nu e director"
	exit 2
fi

# gasim fisierele cu drepturile cautate

FILES=$(find $DIR -type f -perm 755)
for FILE in $FILES
do
	#nume=$(echo $FILE | awk -F'/' '{print $NF}')
	echo
	nume=$(echo $FILE | sed 's@.*/@@')
	read -p "Doriti sa schimbati drepturile fisierului $nume [y/n]" conditie
	case $conditie in
	[yY])
		chmod 744 $FILE;
		echo "Drepturi de acces schimbate";;
	[nN])
		:;;
	*)
		echo "Alegere invalida";;
	esac

done

exit 0
