#include <stdio.h>
#include <unistd.h>
#include <fcntl.h>
#include <stdlib.h>

int main ()
{
	int desc, fisier;

	fisier = open ("output", O_CREAT | O_WRONLY, 0755);

	if (fisier == -1)
	{
		perror("open");
		exit(1);
	}

	// inchide fisierul la care refera descriptorul 1, adica fisierul stdout, si noul fisier de iesiri in consola este fisierul "output"

	desc = dup2 (fisier, 1); // valoarea noului descriptor creat care acceseaza fisierul output

	if (desc != 1)
	{
		printf("eroare");
		perror("dub2");
		exit(1);
	}

	printf("In fisier");


	return 0;
}
