#!/bin/bash

if [ $# -ne 1]; then
	echo "ERROR: invalid number of arguments..."
	echo "USAGE: $1 <directory>"
	exit 1
fi

if ! [ -d "$1" ]; then
	echo "ERROR: directory '$1' does not exist..."
	exit 1
fi

DIR="$1"
CHECKSUMS=()

find "$DIR" -type f | while read -r FILE; do
	CHECKSUM=$(md5 -q "$FILE" | awk '{print $1}')

	if [[ "${CHECKSUMS[@]}" =~ "${CHECKSUM}" ]]; then
		echo "Duplicate file found: $FILE"
	else
		CHECKSUMS+=("$CHECKSUM")
	fi
done

exit 0
