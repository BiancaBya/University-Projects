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
FILES=$(find "$DIR" -type f -name "*.c")

for FILE in $FILES; do
	LINES=$(cat "$FILE" | wc -l)

	if [ "$LINES" -ge 500 ]; then
		echo "$FILE"
	fi
done

exit 0
