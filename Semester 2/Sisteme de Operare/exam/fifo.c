#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <sys/wait.h>

#define FIFO_X "fifo_x"
#define FIFO_Y "fifo_y"
#define FIFO_Z "fifo_z"

void process_A() {
    // Open FIFO X for writing
    int fd_x = open(FIFO_X, O_WRONLY);
    if (fd_x == -1) {
        perror("open fifo_x");
        exit(EXIT_FAILURE);
    }

    // Open FIFO Z for reading
    int fd_z = open(FIFO_Z, O_RDONLY);
    if (fd_z == -1) {
        perror("open fifo_z");
        close(fd_x);
        exit(EXIT_FAILURE);
    }

    // Example write and read (pseudo code)
    // write(fd_x, "message", strlen("message"));
    // char buffer[128];
    // read(fd_z, buffer, sizeof(buffer));

    // Close the FIFOs
    close(fd_x);
    close(fd_z);

    exit(0);
}

void process_B() {
    // Open FIFO X for reading
    int fd_x = open(FIFO_X, O_RDONLY);
    if (fd_x == -1) {
        perror("open fifo_x");
        exit(EXIT_FAILURE);
    }

    // Open FIFO Y for writing
    int fd_y = open(FIFO_Y, O_WRONLY);
    if (fd_y == -1) {
        perror("open fifo_y");
        close(fd_x);
        exit(EXIT_FAILURE);
    }

    // Example read and write (pseudo code)
    // char buffer[128];
    // read(fd_x, buffer, sizeof(buffer));
    // write(fd_y, buffer, strlen(buffer));

    // Close the FIFOs
    close(fd_x);
    close(fd_y);

    exit(0);
}

void process_C() {
    // Open FIFO Y for reading
    int fd_y = open(FIFO_Y, O_RDONLY);
    if (fd_y == -1) {
        perror("open fifo_y");
        exit(EXIT_FAILURE);
    }

    // Open FIFO Z for writing
    int fd_z = open(FIFO_Z, O_WRONLY);
    if (fd_z == -1) {
        perror("open fifo_z");
        close(fd_y);
        exit(EXIT_FAILURE);
    }

    // Example read and write (pseudo code)
    // char buffer[128];
    // read(fd_y, buffer, sizeof(buffer));
    // write(fd_z, buffer, strlen(buffer));

    // Close the FIFOs
    close(fd_y);
    close(fd_z);

    exit(0);
}

int main() {
    // Create FIFOs
    mkfifo(FIFO_X, 0666);
    mkfifo(FIFO_Y, 0666);
    mkfifo(FIFO_Z, 0666);

    pid_t pidA, pidB, pidC;

    // Fork process A
    pidA = fork();
    if (pidA == 0) {
        process_A();
    }

    // Fork process B
    pidB = fork();
    if (pidB == 0) {
        process_B();
    }

    // Fork process C
    pidC = fork();
    if (pidC == 0) {
        process_C();
    }

    // Wait for all child processes to finish
    waitpid(pidA, NULL, 0);
    waitpid(pidB, NULL, 0);
    waitpid(pidC, NULL, 0);

    // Clean up FIFOs
    unlink(FIFO_X);
    unlink(FIFO_Y);
    unlink(FIFO_Z);

    return 0;
}
