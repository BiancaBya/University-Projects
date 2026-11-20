#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <fcntl.h>
#include <math.h>

int n = 0;
pthread_mutex_t mtx[3];

void *f (void* p)
{
	int id = (int)p;
	pthread_mutex_lock (&mtx[id]);

	n += id;
	printf("%d ", n);

	pthread_mutex_unlock (&mtx[(id+1)%3]);
	return NULL;
}

int main ()
{
	int i;
	pthread_t t[3];
	for (i=0; i<3; i++)
		pthread_mutex_init(&mtx[i], NULL);

	for (i=0; i<3; i++)
		pthread_create(&t[i], NULL, f, (void*)i);

	for (i=0; i<3; i++)
		pthread_join(t[i], NULL);

	for (i=0; i<3; i++)
		pthread_mutex_destroy(&mtx[i]);

	return 0;
}

