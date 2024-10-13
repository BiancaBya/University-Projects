// prob 27

#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <limits.h>
#include <time.h>

#define SIZE 128

int count;
int poz;
char buffer[SIZE+1];
pthread_cond_t cg = PTHREAD_COND_INITIALIZER; // variabila de conditie pt threadul generator
pthread_cond_t cp = PTHREAD_COND_INITIALIZER;
pthread_mutex_t mtx = PTHREAD_MUTEX_INITIALIZER;

void * generat (void* arg)
{
	while(1)
	{
		char ch = 'a'+rand()%('z'-'a'+1);

		pthread_mutex_lock(&mtx);

		if (poz == SIZE)
		{
			pthread_cond_signal(&cp); // semnalam threadul de print

			while(poz == SIZE) // ne punem in asteptare
			{
				pthread_cond_wait(&cg, &mtx); // conditie generatori
			}
		}

		if (count == 0)
			break;

		buffer[poz] = ch; // stocam litera in buffer
		poz++;

		pthread_mutex_unlock(&mtx);

	}
	pthread_mutex_unlock(&mtx);

	return NULL; // terminam executia threadului
}

void* print (void* arg)
{
	while(1)
	{
		pthread_mutex_lock(&mtx);

		if (poz != SIZE)
		{
			pthread_cond_signal(&cg);

			while(poz != SIZE)
			{
				pthread_cond_wait(&cp, &mtx);
			}
		}

		printf("%s\n", buffer);
		count--;
		poz = 0; // trecem la un sir nou
		memset(buffer, 0 , SIZE); // zero-ul final nu il modifica nimeni si punem 0 numai pana la SIZE

		pthread_cond_broadcast(&cg);

		if (count == 0)
			break;

		pthread_mutex_unlock(&mtx);

	}

	pthread_mutex_unlock(&mtx);
	return NULL;

}


int main (int argc, char *argv[])
{

	if (argc != 3)
	{
		printf("Numar insuficient de argumente\n");
		exit(EXIT_FAILURE);
	}

	int N = atoi (argv[1]); // luam elementele din linia de comanda
	int M = atoi (argv[2]);
	count = M;
	poz = 0;
	memset(buffer, 0, SIZE+1); // initializam buffer si punem 0 pe toate pozitiile

	srand(time(NULL));

	pthread_t tid [N+1]; // threaduri generatori

	for (int i = 0; i < N; i++)
	{
		pthread_create(&tid[i], NULL, generat, NULL);
	}

	pthread_create(&tid[N], NULL, print, NULL);

	for (int i = 0; i <= N; i++)
	{
		pthread_join(tid[i], NULL);
	}


	return 0;
}
