//2. Se dă un tablou care conține 100.000 de numere întregi. Folosind un număr potrivit de thread-uri
//și cele mai potrivite mecanisme de sincronizare, să se calculeze diferența absolută dintre valoarea minimă
//determinată în fiecare interval de 2000 de numere consecutive și media aritmetică a tuturor valorilor minime.


#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <limits.h>
#include <time.h>

#define MAX_THR 100
#define MAX_NUM 100000

int suma_mediilor = 0;
int numere[MAX_NUM];

pthread_barrier_t barr;
pthread_mutex_t mtx = PTHREAD_MUTEX_INITIALIZER;

void *do_work(void* arg)
{
        int id = *(int *)arg;
        free(arg); // se elibereaza id-ul in thread dupa ce am obtinut valoarea lui, threadurile cu mainul au in comun heap-ul

        int min = INT_MAX;
        int st = id * MAX_NUM/MAX_THR; // se obtine partea din stanga a intervalului de numere adunate
        int dr = st + MAX_NUM/MAX_THR;
        for (int i = st; i < dr; i++)
        {
                if (min > numere[i])
                        min = numere[i];
        }

        pthread_mutex_lock (&mtx);

        suma_mediilor += min;

        pthread_mutex_unlock (&mtx); // ca sa nu se piarda valoarea lui minim_global
        // avem nevoie de mutex, obiect de sincronizare pt ca minim_global este partajat intre threaduri

	// pun bariera, ne asiguram ca tot codul inainte de linie s-a terminat pentru toate threadurile care asteapta la ea

	pthread_barrier_wait(&barr);

	float medie = suma_mediilor / MAX_THR; // media aritmetica
	printf("[Thread %2d] Media aritmetica: %f\n", id, medie);

	float diff = abs(min - medie);
	printf("[Thread %2d] Diferenta: %f\n", id, diff);

	return NULL; // terminarea unui thread ori cu null ori cu pthread_exit
}


int main(int argc, char** argv)
{
        // stabilesc seed
        srand(time(NULL));

        // generez 100.000 nr aleatoare
        for (int i=0; i < MAX_NUM; i++)
                numere[i] = rand();

	// creez bariera
	pthread_barrier_init(&barr, NULL, MAX_THR);

        //creez 100 threaduri

        pthread_t tid[MAX_THR];

        for (int i=0; i < MAX_THR; i++)
        {
                int* id = malloc(sizeof(int));
                *id = i;
                pthread_create(&tid[i], NULL, do_work, id);
        }

        //astept terminarea threadurilor

        for (int i=0; i < MAX_THR; i++)
        {
                pthread_join(tid[i], NULL);
        }

	// distrug bariera

	pthread_barrier_destroy(&barr);

        return 0;
}
