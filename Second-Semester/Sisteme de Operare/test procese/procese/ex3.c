//3. Se dă un fișier care conține un text (șiruri de caractere separate prin spații).
// Fie P(i) procesul care determină frecvența de apariție a literei i într-un text dat.
// Folosind un număr adecvat de astfel de procese, să se scrie un program care afișează
// frecvența de apariție a fiecarei litere.

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/wait.h>

int main(int argc, char* argv[])

{
	char litere[] = "abcdefghijklmnopqrstuvwxyz";
	FILE* fd = fopen("cuvinte.txt","r");
	// testam daca e deschis

	for (int i=0; i<strlen(litere); i++)
	{
		int pid = fork();
		if (pid == -1)
		{
			perror("fork()");
			exit(EXIT_FAILURE);
		}
		if (pid == 0)
		{
			char c;
			int contor = 0;
			rewind(fd);
			while((c = fgetc(fd)) > 0)
			{
				if (c == litere[i])
					contor++;
			}
			printf("Litera %c apare de %d ori\n", litere[i], contor); // trebuie \n
			exit(0);
		}
		wait(NULL); // parintele asteapta terminarea procesului copilului
	}
	fclose(fd);

	return 0;

}
