#!/bin/bash
DIRS=$(find -type d)
nr_dir=0
nr_file=0
for DIR in $DIRS
do
	nr_dir=$(($nr_dir + 1))
	FILES=$(find -type f)
	for FILE in $FILES
	do
		nr=$(echo $FILE | grep -E ".txt" | wc -l)
		nr_file=$(($nr_file + $nr))
	done
done
echo $nr_file
echo $nr_dir
echo "Media de fisiere txt este " $(($nr_file/$nr_dir))
