#!/bin/bash

if [ $# -ne 1 ]; then
	echo "ERROR: invalid number of arguments..."
	exit 1
fi

if ! [ -f "$1" ]; then
	echo "ERROR: '$1' does not exist or is not a file..."
	exit 1
fi

FILE="$1"
EMAILS=""

while IFS= read -r NAME; do
	EMAIL="$NAME@scs.ubbcluj.ro"
	EMAILS+="$EMAIL"

	if ! [ -z "$EMAILS" ]; then
		EMAILS+=","
	fi
done < "$FILE"

EMAILS="${EMAILS%,}"

if ! [ -z "$EMAILS" ]; then
	echo "$EMAILS"
else
	echo "No emails available..."
fi

exit 0
