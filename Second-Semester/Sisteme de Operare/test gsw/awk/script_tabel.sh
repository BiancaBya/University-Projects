#!bin/awk

BEGIN{print "NUME    PRENUME     DATA NASTERII    TELEFON     SALARIU"}
{
printf "%s\t %s\t %s\t %s\t %s\n", $1, $2, $3, $4, $5
}
END{print "Numarul total de angajati este", NR}
