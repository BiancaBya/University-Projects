#!/bin/bash

LOGGED_USERS=$(awk '{print $1}' who.fake);

for USER in $LOGGED_USERS; do
	PROCESSES=$(awk '$1 ~ /^'$USER'$/ {print $1}' ps.fake | wc -l);
	echo "$USER $PROCESSES"
done

exit 0
