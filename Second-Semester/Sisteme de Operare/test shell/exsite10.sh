#!/bin/bash

CURRENT_MONTH=$(date +"%b")

CURRENT_MONTH_SESSION=$(last | awk '$4 ~ /^'$CURRENT_MONTH'$/ {print $0}')

SESSION_COUNTS=$(echo "$CURRENT_MONTH_SESSION" | awk '{print $1}' | sort | uniq -c | sort -nr)

echo "$SESSION_COUNTS" | while read -r COUNT USER; do
	echo -e "$COUNT \t $USER"
done

exit 0
