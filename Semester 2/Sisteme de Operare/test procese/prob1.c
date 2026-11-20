//1. Se dă un șir de litere.
//Folosind câte un proces pentru fiecare vocală,
//să se elimine toate vocalele din acest șir.
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>
#include <ctype.h> // functii pt manipularea caracterelor

#define MAX_SIZE 100

int main(int argc, char* argv[])
{

	char sir[] = "aeioubAEIOUCafgeiOUOArt";
	char vocale[] = "aeiouAEIOU";

	// facem cate un proces pentru fiecare vocala din alfabet
	for(int i = 0; i < strlen(vocale); i++)
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
			// in copil facem un sir nou care este sirul initial fara vocala actuala si il trimitem prin pipe
			close (pd[0]); // inchidem capatul de citire

			int k = 0;
			char* aux = malloc(MAX_SIZE * sizeof(char));

			for (int j=0; j < strlen(sir); j++)
			{
				if (sir[j] != vocale[i])
					aux[k++] = sir[j];
			}

			aux[k] = '\0';  // punem NULL la final

			write(pd[1], aux, MAX_SIZE); // obligatoriu MAX_SIZE

			close(pd[1]);
			free(aux);

			exit(0);
		}

		// in parinte

		close(pd[1]); // inchidem capatul de scriere

		char* aux = malloc(MAX_SIZE * sizeof(char));

		read(pd[0], aux, MAX_SIZE);
		strcpy(sir, aux);

		close(pd[0]);
		free(aux);

		wait(NULL); // se asteapta terminarea procesului

	}

	printf("Sirul final: %s\n", sir);
	return 0;

}
