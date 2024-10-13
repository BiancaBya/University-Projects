#!/bin/bash

if [ $# -eq 0 ]; then
	echo "ERROR: invalid number of arguments..."
	echo "USAGE: [$0 <dangerous_name> $1 <dangerous_name> ...]"
	exit 1
fi

DANGEROUS_NAMES=("$@")

while true; do
	for PROGRAM in "${DANGEROUS_NAMES[@]}"; do
		if pgrep -x "$PROGRAM" > /dev/null; then
			kill "$PROGRAM"
			echo "WARNING: dangerous program '$PROGRAM' detected and terminated"
		fi 
	done

	sleep 2
done

exit 0 
