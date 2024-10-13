#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>

int main(int argc, char* argv[])
{

	int a[] = {1,2,3,4};

	int pd[2];    // pd[0] = descriptor de citire, pd[1] = descriptor de scriere

	int rez = pipe(pd);   // lansam pipe (0 ok, -1 nu e ok)

	if(rez == -1)   // nu s-a putut crea pipe-ul
	{
		perror("pipe()");
		exit(EXIT_FAILURE);
	}

	int pid = fork();    // creezi copilul
	if (pid == -1)  // nu s-a putut crea copilul
	{
		perror("fork()");
		exit(EXIT_FAILURE);
	}

	// SENS  Copil -> Parinte

	if (pid == 0) // Procesul din Copil
	{
		close(pd[0]); //inchidem capatul de citire, pt ca copilul scrie in parinte

		a[0] += a[1];
		printf("[CHILD] Suma: %d\n", a[0]);

		// folosim cdescriptorul de scriere ca sa scriem in pipe
		write(pd[1], &a[0], sizeof(int)); // scriem in pipe suma partiala

		close(pd[1]); // inchidem si capatul de scriere al copilului
		exit(0);      // iesim din copil
	}

	// Procesul din Parinte

	close(pd[1]); // inchidem capatul de scriere, pt ca parintele citeste din copil

	a[2] += a[3];

	// folosim descriptorul de citire ca sa citim din pipe
	read(pd[0], &a[0], sizeof(int));  // citim suma partiala din pipe

	int status;
	wait(&status);
	printf("[PARENT] Exit status copil: %d\n", status);

	a[0] += a[2];
	// in a[0] este suma partiala citita de la copil
	// in a[2] este suma calculata mai sus in parinte
	// adunarea e suma totala

	printf("[PARENT] Suma: %d\n", a[0]);

	close(pd[0]); // inchidem capatul de citire al parintelui
	return 0;

}
