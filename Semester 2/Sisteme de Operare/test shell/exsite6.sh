#!/bin/bash

if [ $# -ne 1 ]; then
	echo "ERROR: invalid number of arguments..."
	exit 1
fi

if ! [ -d "$1" ]; then
	echo "ERROR: '$1' does not exist or is not a directory..."
	exit 1
fi

DIR=$1

find "$DIR" -type f -perm -o=w | while read -r FILE; do
	ls -l "$FILE"
	chmod o-w "$FILE"
	ls -l "$FILE"
done

exit 0 
