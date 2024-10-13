#!bin/awk

BEGIN{

print "Argumentele din linia de comanda sunt\n "
for (i=0; i < ARGC; i++)
	printf "ARGV[%d] = %s \n", i, ARGV[i]

}




