#!/bin/awk

BEGIN{print "Nume\t Prenume\t Data\t nasterii\t Telefon"}
{
	printf("%s   \t%s    \t%s\t%s\t\n", $1, $2, $3, $4)
}
END{print "Nr angajati " NR}
