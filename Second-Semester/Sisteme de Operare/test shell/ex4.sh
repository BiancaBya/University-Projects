#!/bin/bash

# 4. Să se scrie un script bash care afișează numele fișierelor
# dintr-un director dat ca argument care conțin numere cu peste 5 cifre.

if [ $# -eq 0 ]
then
	echo "Numar insuficient de argumente"
	echo "Utilizare " $0 " nume_director"
	exit 1
fi

if ! [ -d $1 ]
then
	echo "Directorul nu exista"
	exit 2
fi

DIR=$1
FILES=$(find $DIR -type f)

for FILE in $FILES
do
	if cat $FILE | grep -Eq '[0-9]{5,}'
	then
		nume=$(echo $FILE | sed 's@.*/@@')
		echo "Numele fisierului: " $nume
	fi
done
exit 0
