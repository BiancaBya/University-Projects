//1. Se dă un șir de litere.
//Folosind câte un proces pentru fiecare vocală, să se elimine toate vocalele din acest șir.
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/wait.h>
#define MAX_SIZE 100

int main(int argc, char* argv[])
{

        char sir[]="aeiouBaeiouC";
        char voc[]="aeiouAEIOU";
        for (int i=0; i<strlen(voc); i++)
        {
                int pd[2];
                int res = pipe(pd);
                if (res == -1)
                {
                        perror("pipe()");
                        exit(EXIT_FAILURE);
                }

                int pid = fork();
                if (pid == -1)
                {
                        perror("fork()");
                        exit(EXIT_FAILURE);
                }

                if (pid == 0)
                {
                        // in copil
                        close(pd[0]); //inchidem capatul se read
                        char *tmp = malloc(MAX_SIZE*sizeof(char));
			int k = 0;
                        for (int j=0; j<strlen(sir); j++)
                                if (sir[j] != voc[i])
                                        tmp[k++] = sir[j];
                        tmp[k] = '\0'; // punem terminatorul de sir
                        write(pd[1], tmp, MAX_SIZE);
                        close(pd[1]);
                        free(tmp);

                        exit(0);
                }
                close(pd[1]); // doar citim in parinte, inchidem capatul de write
                char *tmp = malloc(MAX_SIZE*sizeof(char));
                read(pd[0], tmp, MAX_SIZE);
                strcpy(sir, tmp);
                close(pd[0]);
                free(tmp);
                wait (NULL);
        }
        printf("Sir final: %s\n", sir); // trebuie \n ca sa se vada mesajele
        return 0;
}
