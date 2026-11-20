//Să se scrie un program C care:
//  - va citi de la tastatură un număr natural N = {50000, 80000, 100000}.
//  - va citi N numere întregi (pe 1 OCTET) din fișierul binar "/tmp/212-file"
//    și le va stoca într-o variabilă globală;
//  - va crea, apoi, un număr potrivit de thread-uri care vor procesa numerele
//    întregi citite anterior din fișier și care:
//      - vor determina numărul de apariții a2, a3, a5, ..., a97 ale fiecărui
//        număr întreg PRIM din intervalul (0, 100];
//      - vor calcula M = media aritmetică(a2, a3, ..., a97);
//      - vor determina și afișa TOATE numerele PRIME ale căror număr de apariții
//        este CEL MAI APROPIAT de media aritmetică M calculată anterior.
//
//Execuția thread-urilor va fi sincronizată folosind cele mai potrivite
//mecanisme studiate.

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <fcntl.h>
#include <math.h>


#define MAX_THR 100
#define MAX_SIZE 100000

int N, numere [MAX_SIZE];
double medie;
int suma;
int vec_numarare[101];

pthread_barrier_t barr;
pthread_mutex_t mtx = PTHREAD_MUTEX_INITIALIZER;

int prim (int n)
{
	if (n == 0 || n == 1)
		return 0;
	if (n == 2)
		return 1;
	if (n%2 ==0)
		return 0;
	for (int d = 2; d <= n/2; d++)
		if (n%d == 0)
			return 0;
	return 1;
}


void* do_work (void* arg) // ultimul argument de la crearea threadului este argumentul primit de functia data si ea ca argument, do_work
{
	int tid = *(int *) arg;
	free(arg);

	for (int i=0; i < N; i++)
		if(numere[i] == tid)
			vec_numarare[tid]++;

	pthread_mutex_lock (&mtx); // cu &mtx

	suma += tid;
	printf("Nr de aparitii ale %d este de %d\n", tid, vec_numarare[tid]);

	pthread_mutex_unlock (&mtx);

	// asteptam ca toate threadurile sa adauge la suma
	pthread_barrier_wait (&barr);

	if (tid == 2) // doar o data afisam media
	{
		medie = (double)(suma/25); // de numarat nr prime
		printf("Medie:%f\n", medie);
	}

//	pthread_barrier_wait (&barr);

//	pthread_barrier_wait(&barr);

	return NULL; // obligatoriu return NULL;

}


int main (int argc, char* argv[])

{
	printf("N=");
	scanf("%d", &N);

	if (N > MAX_SIZE)
	{
		printf("Numar invalid!");
		exit(EXIT_FAILURE);
	}

	int fd = open("random-file", O_RDONLY);
	if (fd == -1)
	{
		perror("open()");
		exit(EXIT_FAILURE);
	}

	for (int i=0; i < N; i++)
		read(fd, &numere[i], 1); // citeste un octet


	// creez 100 threaduri

	pthread_t tid [MAX_THR];

	pthread_barrier_init(&barr, NULL, MAX_THR); // cu &barr si nr_mar de threaduri

	for (int i=0; i < MAX_THR; i++)
	{
		if (prim(i) == 1)
		{
			int* id = malloc(sizeof(int));
                	*id = i;
			pthread_create(&tid[i], NULL, do_work, id); // cu & la &tid[i]
		}
	}

	// facem join

	printf("aici");

	for (int i=0; i < MAX_THR; i++)
	{
		pthread_join(tid[i], NULL); // fara &
	}


	pthread_barrier_destroy(&barr); // din nou cu &barr

	close(fd);
	return 0;
}





