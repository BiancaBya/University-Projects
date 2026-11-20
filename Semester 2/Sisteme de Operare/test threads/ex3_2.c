// prob 31 de pe site rares

#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <limits.h>
#include <time.h>

typedef struct{

	int* flag;
	pthread_barrier_t* barr;
	pthread_mutex_t* mtx;

}data;

void* dowork(void* arg)
{
        data td = *((data*)arg);
	pthread_barrier_wait(td.barr);

        while(1)
        {
                // aici ne trebuie mutex pt ca modificam si testam flag

                pthread_mutex_lock(td.mtx);

                if (*(td.flag) == 0)
                {

                        int n = rand()%111112; // a generat numarul
                        printf("%d\n", n);

                        if (n % 1001 == 0)
                        {
                                *(td.flag) = 1;
                                break;
                        }
                }
                else
                        break;

                pthread_mutex_unlock(td.mtx);
		 }

        pthread_mutex_unlock(td.mtx);

        return NULL;

}


int main (int argc, char *argv[])
{

        if (argc != 2)
        {
                printf("Numar invalid de argumente\n");
                exit(EXIT_FAILURE);
        }

        int N = atoi(argv[1]);

	// alocam in heap elementele

	int* fl = malloc(sizeof(int));

	pthread_barrier_t* ba = malloc(sizeof(pthread_barrier_t));
	pthread_barrier_init(ba, NULL, N);

	pthread_mutex_t* mx = malloc(sizeof(pthread_mutex_t));
	pthread_mutex_init(mx, NULL);

        pthread_t tid[N];

        srand(time(NULL));


	data *args = malloc(N*sizeof(data));

        for (int i = 0; i < N; i++)
        {
		// populam elementele
		args[i].flag = fl;
		args[i].barr = ba;
		args[i].mtx = mx;
                pthread_create(&tid[i], NULL, dowork, &args[i]);
        }



        for (int i=0; i < N; i++)
        {
                pthread_join(tid[i], NULL);
        }

	pthread_barrier_destroy(ba);
	pthread_mutex_destroy(mx);
	free(args);
	free(fl);
	free(ba);
	free(mx);

	return 0;
}
