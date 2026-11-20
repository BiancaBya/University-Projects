//Să se scrie un program C care:

//Va citi de la tastatură un număr natural N = {40000, 60000, 80000}.

// 1 -- Va crea UN SINGUR thread care va citi N numere întregi (pe 1 OCTET) din fișierul binar "/tmp/212-file" și le va stoca într-o variabilă globală.

// -- Va crea, apoi, un număr potrivit de thread-uri care vor procesa numerele întregi citite anterior din fișier în primul thread creat și care:

// 2 vor calcula și vor afișa diferența ABSOLUTĂ dintre suma numerelor PARE și suma numerelor IMPARE din fiecare interval de 2000 numere întregi;

// 3 vor determina, la final, și vor stoca într-o variabilă globală comună, cea mai MICĂ diferență absolută dintre cele calculate anterior.

//Rezultatul final va fi afișat DOAR în programul principal.

//Cele 2 tipuri de thread-uri vor fi create în ordinea indicată, iar execuția lor va fi sincronizată folosind cele mai potrivite mecanisme studiate.

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <fcntl.h>
#include <math.h>

#define MAX_SIZE 80000
#define MAX_NR 2000
// aici nu avem un nr de threaduri stiut de la inceput

int N;
int numere[MAX_SIZE];
int flag;              // flag = 0 GOL, flag = 1 PLIN
int dif_globala = MAX_SIZE;

pthread_mutex_t mtx = PTHREAD_MUTEX_INITIALIZER;
pthread_cond_t cond = PTHREAD_COND_INITIALIZER;
pthread_barrier_t barr;

void * citire(void* arg)
{
	int fd = open("random-file", O_RDONLY);

	if (fd == -1)
	{
		perror("open()");
		exit(1);
	}

	for (int i=0; i<N; i++)
		read( fd, &numere[i], 1); // citim un octet din fisier si il punem in sirul de numere

	printf("Done reading\n");

	flag = 1; // punem 1 sa semnalam ca bufferul este plin

	pthread_cond_broadcast ( &cond ); // trezim restul threadurilor

	close(fd);

	return NULL;
}

void * do_work(void* arg)
{
	int tid = *((int *)arg);
	free(arg);

	// ne trebuie mutex ca sa folosim variabila de conditie
	pthread_mutex_lock (&mtx);

	while(flag == 0) // asteptam pana cand threadul de citire termina
		pthread_cond_wait(&cond, &mtx);

	pthread_mutex_unlock (&mtx);

	// 1 diferenta locala dintre nr pare si cele impare din interval
	int inceput = tid*2000;
	int dif_locala, suma_pare = 0, suma_impare = 0;

	for (int i = inceput; i < inceput + 2000; i++)
	{
		if (numere[i] % 2 == 0)
			suma_pare += numere[i];
		else
			suma_impare += numere[i];
	}
	dif_locala = abs(suma_pare - suma_impare);
	printf ("TID[%d] Diferenta este %d\n", tid, dif_locala);

	pthread_barrier_wait (&barr); // asteptam ca toate threadurile sa isi calculeze diferenta , nu neaparat

	pthread_mutex_lock(&mtx);

	if (dif_locala < dif_globala)
		dif_globala = dif_locala;

	pthread_mutex_unlock(&mtx);

	return NULL;
}


int main (int argc, char *argv[])
{
	printf("N=");
	scanf("%d", &N);

	if (N > MAX_SIZE)
	{
		printf("N invalid!\n");
		exit(1);
	}

	int nr_thr = N / MAX_NR; // numarul de intervale = nr de threaduri

	pthread_t tid [nr_thr+1]; // unul pentru citire si restul sunt cele pe intervale

	pthread_barrier_init(&barr, NULL, nr_thr);

	pthread_create(&tid[nr_thr], NULL, citire, NULL);

	for (int i=0; i < nr_thr; i++)
	{
		int* p = malloc(sizeof(int));
		*p = i;
		pthread_create(&tid[i], NULL, do_work, p);
	}

	for (int i=0 ; i <= nr_thr; i++)
		pthread_join(tid[i], NULL);

	printf("Diferenta minima este %d\n", dif_globala);

	return 0;
}
