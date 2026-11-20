//2. Se dă un fișier ce conține primele N numere naturale în ordine crescătoare.
//Fie P(i) procesul care elimină toți multiplii lui i cuprinși între 2 și N.
//Folosind un număr adecvat de astfel de procese, să se scrie un program care determină toate numerele prime cuprinse între 1 și N dat.
#include <stdio.h>
#include <stdlib.h>
#include <sys/wait.h>
#include <unistd.h>


int main (int argc, char*argv[])
{

        int n=15;
        for (int i=2; i<=n/2 ;i++)
        {
                int pid = fork();
                if (pid == -1)
                {
                        perror("fork()");
                        exit(EXIT_FAILURE);
                }
                if (pid == 0)
                {
                        // in copil
                        FILE* fd = fopen("numere.txt", "r");
                        if (fd == NULL)
                        {
                                perror("fopen()");
                                exit (1);
                        }

                        int nr = 0;
                        int tmp[100];
                        int index = 0;

                        while(fscanf(fd, "%d", &nr) > 0)
			{
                                if(nr%i != 0 || nr==i)
                                {
                                        tmp[index++] = nr;
                                }
                        }

                        //rewind(fd); // ca sa suprascriem fisierul, daca nu facem asta face append
                        fclose(fd);
                        fd = fopen("numere.txt", "w");
                        for (int j=0; j<index; j++)
                        {
                                fprintf(fd,"%d ",tmp[j]);
                        }
                        fclose(fd);

                        exit(0); // cand iesim din copil
                }
                wait(NULL);

        }

        return 0;

}
