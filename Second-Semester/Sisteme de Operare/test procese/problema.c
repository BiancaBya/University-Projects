#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/wait.h>

int main(int argc, char* argv[])
{

	FILE* fd = fopen("fisier","r");

	int pd[2];
	if (pipe(pd) == -1)
	{
		perror("pipe()");
		exit(EXIT_FAILURE);
	}

	int pid = fork();
	if (pid == -1)
	{
		perror("fork()");
		exit(EXIT_FAILURE);
	}

	if (pid == 0) // in copil
	{
		// citeste linie cu linie fisierul
		// trimite operatia aritmetica si numerele citite
		close (pd[0]);

		char caracter;
		int a, b;

		while (fscanf(fd, " %c", &caracter) == 1) // se pune spatiu ca sa ignore caracterele albe
		{
	        	fscanf(fd, " %d %d", &a, &b);
        		write(pd[1], &caracter, sizeof(char));
        		write(pd[1], &a, sizeof(int));
        		write(pd[1], &b, sizeof(int));
		}

		close(pd[1]);
		exit(0);
	}

	// in parinte
	// citeste operatia si numerele din pipe
	// calculeaza rezultatul si il afiseaza in consola cu formatul dat
	close(pd[1]);

	char caracter;
	int a, b, rezultat;
	int ok = 1;
	while (ok == 1)
	{
		int result = read(pd[0], &caracter, sizeof(char));
    		if (result == 0) {
        		break; // daca capatul s-a inchis, break
    		}

		read (pd[0], &a, sizeof(int));
		read (pd[0], &b, sizeof(int));

		if (caracter == '+')
	    		rezultat = a+b;

		else if (caracter == '-')
	    		rezultat = a-b;

		else if (caracter == '*')
	    		rezultat = a*b;

		else if (caracter == ':')
	    		rezultat = a/b;
		else{
	    		ok = 0;
			break;
		}
		printf("%d %c %d = %d\n", a, caracter, b, rezultat);
	}

	close(pd[0]);

	wait(NULL);

	fclose(fd);
	return 0;

}
