#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>
#include <sys/wait.h>

// 2. Se dă un fișier ce conține primele N numere naturale în ordine crescătoare.
// Fie P(i) procesul care elimină toți multiplii lui i cuprinși între 2 și N.
// Folosind un număr adecvat de astfel de procese, să se scrie un program care determină toate numerele prime cuprinse între 1 și N dat.

int main(int argc, char *argv[])
{
	int n = 10;

	for(int i=2; i < n; i++)
	{
		int pd[2];
		int rez = pipe(pd);
		if (rez == -1)
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
			FILE* fd = fopen("numere","r"); // deschidem fisierul de citire

			if (fd == NULL)
			{
				perror("fopen()");
				exit(EXIT_FAILURE);
			}

			int k = 0;
			int num;
			int* nr = malloc(n * sizeof(int));

			while(fscanf(fd, "%d", &num) > 0)  // verificam daca mai putem citi
			{
				if((num % i != 0) || num == i)
					nr[k++] = num;

			}

			fclose(fd);
			write(pd[1], &k, sizeof(int)); // scriem k
			write(pd[1], nr, n * sizeof(int)); // scriem pentru parinte
			free(nr);

			exit(0);
		}
		// in parinte
		int k = 0;
		int* nr = malloc(n * sizeof(int));
		read(pd[0], &k, sizeof(int));

		read(pd[0], nr, n * sizeof(int));

		FILE* fd = fopen("numere", "w"); // deschidem fisierul pentru scriere
		if (fd == NULL)
		{
			perror("fopen()");
			exit(EXIT_FAILURE);
		}

		for (int j = 0; j < k; j++)
			fprintf(fd, "%d ", nr[j]);

		fclose(fd);
		free(nr);

		wait(NULL);
	}
	return 0;
}
