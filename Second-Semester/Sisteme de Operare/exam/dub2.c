#include <stdio.h>
#include <unistd.h>
#include <fcntl.h>
#include <stdlib.h>

int main() {

	printf("Inainte de dub2\n");

    // Deschide un fișier pentru a scrie
    int fd = open("output.txt", O_WRONLY);
    if (fd == -1) {
        perror("open");
        exit(EXIT_FAILURE);
    }

	printf("Tot inainte\n");

    // Redirecționează stdout către descriptorul fișierului deschis
    if (dup2(fd, 1) == -1) {
        perror("dup2");
        exit(EXIT_FAILURE);
    }
    // Redirectionam descriptorul de scriere catre fisier, adica 1


	// STDIN = 0
	// STDOUT = 1
	// STDERR = 2


    // Închide descriptorul duplicat (nu mai este necesar)
    close(fd);

    // De acum încolo, orice este scris pe stdout va fi redirecționat către "output.txt"
    printf("Această linie va fi scrisă în fișierul output.txt\n");

    return 0;
}
