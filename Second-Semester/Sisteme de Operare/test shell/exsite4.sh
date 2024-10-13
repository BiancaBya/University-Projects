#!/bin/bash

if [ $# -ne 1 ]; then
	echo "Error: invalid number of arguments..."
	echo "Usage: $0 <directory>"
	exit 1
fi

if ! [ -d "$1" ]; then
	echo "Error: directory '$1' does not exist..."
	exit 1
fi

DIR=$1

find "$DIR" -type l | while read LINK; do
	if [ ! -e "$LINK" ]; then
		echo "Broken symbolic link: $LINK"
	fi
done

exit 0
