#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <fcntl.h>
#include <math.h>

#define MAX_SIZE 80000
#define NR_TH 50

int N, buff[MAX_SIZE];
double M;
int current, evenFr[101];
int sum;

double closestDif = MAX_SIZE + 1;

pthread_mutex_t mtx = PTHREAD_MUTEX_INITIALIZER;
pthread_barrier_t barr;

void* doFr(void* arg)
{
    int tid = *((int*)arg);
    free(arg);

    for(int i = 0; i < N; ++i)
        if(buff[i] == tid)
            evenFr[tid]++;

    pthread_mutex_lock(&mtx);
    sum += evenFr[tid];
    //printf("Numarul %3d, aparitii: %d\n", tid, evenFr[tid]);
    pthread_mutex_unlock(&mtx);

    pthread_barrier_wait(&barr);
    if(tid == 2){
        M = (double)(sum/50);
        printf("MA: %f\n", M);
    }

    pthread_barrier_wait(&barr);

    pthread_mutex_lock(&mtx);

    if(fabs((double)(M - evenFr[tid])) < closestDif)
        closestDif = fabs((double)(M - evenFr[tid]));

    pthread_mutex_unlock(&mtx);

    pthread_barrier_wait(&barr);

    if(evenFr[tid] == (int)(M + closestDif) || evenFr[tid] == (int)(M - closestDif))
        printf("%3d\n", tid);

    return NULL;
}

int main(int argc, char* argv[])
{
    printf("N = ");
    scanf("%d", &N);

    if(N > MAX_SIZE)
    {
        printf("Numar invalid. 5000 <= N <= 80000\n");
        exit(EXIT_FAILURE);
    }

    int fd = open("random-file", O_RDONLY);
    if(fd == -1)
    {
        perror("open()");
        exit(EXIT_FAILURE);
    }

    for(int i = 0; i < N; ++i)
        read(fd, &buff[i], 1);

    pthread_t tid[NR_TH];

    pthread_barrier_init(&barr, NULL, NR_TH);

    for(int i = 0; i < NR_TH; ++i)
    {
        int* p = malloc(sizeof(int));
        *p = (i+1)*2;
        pthread_create(&tid[i], NULL, doFr, p);
    }

    for(int i = 0; i < NR_TH; ++i)
        pthread_join(tid[i], NULL);

    pthread_barrier_destroy(&barr);

    close(fd);
    return 0;
}
