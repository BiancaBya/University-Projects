


#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <limits.h>
#include <time.h>

pthread_barrier_t barr;
int flag = 0;
pthread_mutex_t mtx = PTHREAD_MUTEX_INITIALIZER;

void* dowork(void* arg)
{
	pthread_barrier_wait(&barr); // niciun thread nu executa codul pana cand nu au fost toate threadurile create

	while(1)
	{
		// aici ne trebuie mutex pt ca modificam si testam flag

		pthread_mutex_lock(&mtx);

		if (flag == 0)
		{

			int n = rand()%111112; // a generat numarul
			printf("%d\n", n);

			if (n % 1001 == 0)
			{
				flag = 1;
				break;
			}
		}
		else
			break;

		pthread_mutex_unlock(&mtx);
	}

	pthread_mutex_unlock(&mtx);

	return NULL;

}


int main (int argc, char *argv[])
{

	if (argc != 2)
	{
		printf("Numar invalid de argumente\n");
		exit(EXIT_FAILURE);
	}

	int N = atoi(argv[1]); // daca avem nevoie de numar intreg folosim atoi ca sa il convertim de la char* la intreg

	pthread_t tid[N];
	srand(time(NULL));

	pthread_barrier_init(&barr, NULL, N);

	for (int i = 0; i < N; i++)
	{
		pthread_create(&tid[i], NULL, dowork, NULL);
	}



	for (int i=0; i < N; i++)
	{
		pthread_join(tid[i], NULL);
	}

	pthread_barrier_destroy(&barr);

	return 0;

}
