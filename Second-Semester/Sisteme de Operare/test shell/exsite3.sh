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
FILES=$(find "$DIR" -type f -name "*.log")

for FILE in $FILES; do
	sort -o "$FILE" "$FILE"
	echo "'$FILE' sorted..."
done

exit 0
