#include <mpi.h>
#include <iostream>
#include <string>

using namespace std;

void mpi_hello(int rank, int p) {
    const char format[] = "Mesaj de la %10d\n";
    const int STR_LEN = sizeof(format) - 1 + 6; //fara \0 + int de 10 caractere
    if (rank ==0) {
        //main
        char* buf = new char[STR_LEN * (p - 1) + 1];
        buf[STR_LEN * (p - 1)] = '\0';
        for ( int pid = 1; pid < p; pid++ ) {
            MPI_Recv(buf + STR_LEN * (pid - 1), STR_LEN, MPI_CHAR, pid, 0, MPI_COMM_WORLD, MPI_STATUSES_IGNORE);

        }
        printf("%s", buf);
        delete[] buf;
    }
    else {
        //worker
        char* buf = new char[STR_LEN + 1];
        snprintf(buf, STR_LEN + 1, format, rank);
        MPI_Send(buf, STR_LEN, MPI_CHAR, 0, 0, MPI_COMM_WORLD);

        delete[] buf;
    }
}

void mpi_hello_async(int rank, int p) {
    const char format[] = "Mesaj de la %10d\n";
    const int STR_LEN = sizeof(format) - 1 + 6; //fara \0 + int de 10 caractere
    if (rank ==0) {
        //main

        char* buf = new char[STR_LEN * (p - 1) + 1];
        buf[STR_LEN * (p - 1)] = '\0';
        MPI_Request* reqs = new MPI_Request[p-1];
        for ( int pid = 1; pid < p; pid++ ) {
            MPI_Irecv(buf + STR_LEN * (pid - 1), STR_LEN, MPI_CHAR, pid, 0, MPI_COMM_WORLD, &reqs[pid - 1]);

        }

        // bool ok = false;
        // while (!ok) {
        //     ok=true;
        //     for (int pid = 1; pid < p; pid++) {
        //         int flag;
        //         MPI_Test(&reqs[pid - 1],&flag, MPI_STATUS_IGNORE);
        //
        //         if (flag == 0) {
        //             ok=false;
        //         }
        //         else {
        //             printf("%d has finished sending\n", pid);
        //         }
        //     }
        // }

        //MPI_Waitall(p-1, resq, MPI_STATUSES_IGNORE);

        for (int i = 0; i < p -1; i++) {
            int idx;
            MPI_Waitany(p-1,reqs,&idx,MPI_STATUS_IGNORE);
            printf("%d has arrived\n", idx);
        }
        printf("%s", buf);

        delete[] reqs;
        delete[] buf;
    }
    else {
        //worker
        char* buf = new char[STR_LEN + 1];
        MPI_Request req;
        snprintf(buf, STR_LEN + 1, format, rank);
        MPI_Isend(buf, STR_LEN, MPI_CHAR, 0, 0, MPI_COMM_WORLD, &req);
        MPI_Wait(&req, MPI_STATUS_IGNORE);
        delete[] buf;
    }
}

void init_vec(int* vec, int len) {
    for (int i = 0; i < len; i++) {
        vec[i] = rand() % 255;
    }
}

void validate_sum(int* a, int* b, int* c, int len) {
    for (int i = 0; i < len; i++) {
        if (c[i] != a[i] + b[i]) {
            printf("Err at %d != %d + %d\n", c[i], a[i], b[i]);
            return;
        }
    }
    printf("All ok!\n");
}

void mpi_sum(int rank, int p) {
    const int N = 100;
    if (rank == 0) {
        int* a = new int[N];
        int* b = new int[N];
        int* c = new int[N];

        init_vec(a, N);
        init_vec(b, N);

        int dim = N / (p-1);
        int r = N % (p-1);
        int start = 0;
        int end = dim;

        MPI_Request* reqs = new MPI_Request[p-1];

        for (int pid = 1; pid < p; pid++) {
            if (r > 0) {
                end += 1;
            }
            int len = end - start;

            MPI_Send(&len, 1, MPI_INT, pid, 0, MPI_COMM_WORLD);
            MPI_Send(a + start, end - start, MPI_INT, pid, 0, MPI_COMM_WORLD);
            MPI_Send(b + start, end - start, MPI_INT, pid, 0, MPI_COMM_WORLD);
            //async
            //MPI_Irecv(c + start, end - start, MPI_INT, pid, 0, MPI_COMM_WORLD, &reqs[pid - 1]);

            start = end;
            end += dim;
        }

        //async
        //MPI_Waitall(p-1, reqs, MPI_STATUS_IGNORE);

        //sync
        r = N % (p-1);
        start = 0;
        end = dim;
        for (int pid = 1; pid < p; pid++) {
            if (r > 0) {
                end += 1;
            }
            int len = end - start;

            MPI_Recv(c+start, end - start, MPI_INT, pid, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
            start = end;
            end += dim;
        }


        validate_sum(a, b, c, N);

        delete[] a;
        delete[] b;
        delete[] c;
        delete[] reqs;
    }
    else {
        int len;
        MPI_Recv(&len, 1, MPI_INT, 0, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
        int* a_recv = new int[len];
        int* b_recv = new int[len];
        int* c_recv = new int[len];

        MPI_Recv(a_recv, len, MPI_INT, 0, 0, MPI_COMM_WORLD,MPI_STATUS_IGNORE);
        MPI_Recv(b_recv, len, MPI_INT, 0, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

        for (int i = 0; i < len; i++) {
            c_recv[i] = a_recv[i] + b_recv[i];
        }
        MPI_Send(c_recv, len, MPI_INT, 0, 0, MPI_COMM_WORLD);

        delete[] a_recv;
        delete[] b_recv;
        delete[] c_recv;

    }
}

void mpi_sum_scatter(int rank, int p) {
    int* a = nullptr;
    int* b = nullptr;
    int* c = nullptr;

    const int N = 1000;

    if (rank == 0) {
        a = new int[N];
        b = new int[N];
        c = new int[N];

        init_vec(a, N);
        init_vec(b, N);
    }
    //calc
    int len = N / p;
    int* a_recv = new int[len];
    int* b_recv = new int[len];
    int* c_recv = new int[len];

    MPI_Scatter(a, len, MPI_INT, a_recv, len, MPI_INT, 0, MPI_COMM_WORLD);
    MPI_Scatter(b, len, MPI_INT, b_recv, len, MPI_INT, 0, MPI_COMM_WORLD);

    for (int i = 0; i < len; i++) {
        c_recv[i] = a_recv[i] + b_recv[i];
    }

    MPI_Gather(c_recv, len, MPI_INT, c, len, MPI_INT, 0, MPI_COMM_WORLD);

    if (rank == 0) {
        validate_sum(a, b, c, len * p);
    }

    delete[] a_recv;
    delete[] b_recv;
    delete[] c_recv;


    if (rank == 0) {
        delete[] a;
        delete[] b;
        delete[] c;
    }

}

int main(int argc, char* argv[]) {
    printf("Hello!\n");

    if (argc < 2) {
        printf("Usage: %s [param]\n", argv[0]);
        exit(1);
    }

    MPI_Init(&argc,&argv);
    MPI_Barrier(MPI_COMM_WORLD);

    int rank, p;
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &p);

    printf("Hello from proc %d of %d!\n", rank, p);

    int param = stoi(argv[1]);

    switch (param) {
        case 1:
            mpi_hello(rank, p);
            break;
        case 2:
            mpi_hello_async(rank, p);
            break;
        case 3:
            mpi_sum(rank, p);
            break;
        case 4:
            mpi_sum_scatter(rank, p);
            break;
        default:
            printf("Undefined %d!\n", param);
            break;
    }

    MPI_Finalize();
}
