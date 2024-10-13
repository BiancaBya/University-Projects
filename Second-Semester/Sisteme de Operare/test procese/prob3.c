#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/wait.h>
#include <ctype.h>

// 3. Se dă un fișier care conține un text (șiruri de caractere separate prin spații).
// Fie P(i) procesul care determină frecvența de apariție a literei i într-un text dat.
// Folosind un număr adecvat de astfel de procese, să se scrie un program care afișează frecvența de apariție a fiecarei litere.

int main(int argc, char** argv)
{
	char litere[] = "abcdefghijklmnopqrstuvwxyz";

	for (int i=0; i < strlen(litere); i++)
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
			close(pd[0]); // inchidem capatul de citire
			FILE* fd = fopen("text", "r");

			int frecv = 0;
			char litera;
			while (fscanf(fd, "%c", &litera) == 1) // trebuie & ca sa dam adresa variabilei litera, fscanf are nev de o zona de memorie
				if (litera == litere[i])
					frecv++;

			write(pd[1], &frecv, sizeof(int));
			close(pd[1]);
			exit(0);
		}
		// in parinte citim de la copil frecventa de aparitie a literei si o scriem pe ecran
		close (pd[1]);  // inchidem capatul de scriere

		int frecv = 0;
		read(pd[0], &frecv, sizeof(int));
		printf("Litera %c apare de %d ori\n", litere[i], frecv);

		close(pd[0]);
		wait(NULL);
	}
	return 0;

}
