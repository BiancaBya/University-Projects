//
// pipe_2.c
//
// Sa se implementeze un proces care creeaza un proces copil cu care comunica
// prin pipe. Procesul parinte trimite prin pipe procesului copil doua numere
// intregi, iar procesul copil returneaza prin pipe suma lor.

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>


int main(int argc, char* argv[])
{

	int c2p[2]; // copil -> parinte file_descriptor
	int rez = pipe(c2p);
	if(rez == -1)   // nu s-a creat pipe-ul
	{
		perror("pipe(c2p)");
		exit(EXIT_FAILURE);
	}

	int p2c[2]; // parinte -> copil file_descriptor
	rez = pipe(p2c);
	if (rez == -1)  // nu s-a creat pipe-ul
	{
		perror("pipe(p2c)");
		exit(EXIT_FAILURE);
	}

	int pid = fork(); // facem copilul
	if (pid == -1)  // nu s-a putut crea copilul
	{
		perror("pipe(c2p)");
		exit(EXIT_FAILURE);
	}

	if (pid == 0)  // suntem in copil
	{
		//inchidem capetele de care nu avem nevoie
		close(c2p[0]);
		close(p2c[1]);
		// daca suntem in copil, trebuie sa citim din parinte, deci inchidem
		// capatul de scriere al parintelui -> copil pentru ca nu il folosim
		// iar copilul va scrie inapoi in pipe parintelui
		// deci inchidem capatul de citire al copilului -> parinte

		while(1)
		{

			// citim primul intregul
			int a;
			read(p2c[0], &a, sizeof(int));
			printf("[CHILD] a: %d\n", a);

			if(a == 0) // stop
			    break;

			// citim al doilea intreg
			int b;
			read(p2c[0], &b, sizeof(int));
			printf("[CHILD] b: %d\n", b);

			// trimitem suma parintelui
			int suma = a+b;
			write(c2p[1], &suma, sizeof(int));

			printf("[CHILD] Suma: %d\n", suma);

		}

		// inchidem si ceilalti descriptori
		close(c2p[1]);
		close(p2c[0]);
		exit(0); // exit cu succes

	}
	// suntem in parinte
	// inchidem descriptorii pe care nu ii folosim
	close(c2p[1]); // citim din copil -> parinte => inchidem capatul de scriere
	close(p2c[0]); // scriem in parinte -> copil => inchidem capatul de citire

	while(1)
	{

		// citim primul intreg
		int a;
		printf("[PARENT] a: ");
		scanf("%d", &a);

		// trimitel intregul la copil
		write(p2c[1], &a, sizeof(int));

		if (a == 0)
		    break;

		sleep(2); // sa avem timp sa introducem datele

		// citim al doilea intreg
		int b;
		printf("[PARENT] b: ");
		scanf("%d", &b);

		// trimitel intregul la copil
		write(p2c[1], &b, sizeof(int));

		//citim suma de la copil
		int suma;
		read(c2p[0], &suma, sizeof(int));

		printf("[PARENT] Suma: %d\n", suma);

	}

	// wait for child
	int status;
	wait(&status);
	printf("[PARENT] Exit status copil: %d\n", status);

	// inchidem si ceilalti descriptori
	close(c2p[0]);
	close(p2c[1]);

	return 0;

}
