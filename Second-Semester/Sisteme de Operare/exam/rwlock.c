#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>

pthread_rwlock_t rwlock;
int shared_data = 0;

void *reader(void *arg) {
    pthread_rwlock_rdlock(&rwlock);
    printf("Reader %ld: shared_data = %d\n", (long)arg, shared_data);
    pthread_rwlock_unlock(&rwlock);
    return NULL;
}

void *writer(void *arg) {
    pthread_rwlock_wrlock(&rwlock);
    shared_data++;
    printf("Writer %ld: incremented shared_data to %d\n", (long)arg, shared_data);
    pthread_rwlock_unlock(&rwlock);
    return NULL;
}

int main() {
    pthread_t readers[5], writers[2];
    pthread_rwlock_init(&rwlock, NULL);

    for (long i = 0; i < 5; i++) {
        pthread_create(&readers[i], NULL, reader, (void *)i);
    }

    for (long i = 0; i < 2; i++) {
        pthread_create(&writers[i], NULL, writer, (void *)i);
    }

    for (long i = 5; i < 7; i++) {
        pthread_create(&readers[i], NULL, reader, (void *)i);
    }

    for (int i = 0; i < 5; i++) {
        pthread_join(readers[i], NULL);
    }

    for (int i = 0; i < 2; i++) {
        pthread_join(writers[i], NULL);
    }

    pthread_rwlock_destroy(&rwlock);
    return 0;
}
