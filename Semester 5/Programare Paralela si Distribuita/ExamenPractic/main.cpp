#include <mpi.h>
#include <fstream>
#include <vector>
#include <stdexcept>
#include <iostream>

using namespace std;

bool readPolinom(const string& path, int &N, double* &coef) {
    ifstream in(path);
    if (!in) {
        throw runtime_error("Cannot open file " + path);
    }

    in >> N;
    coef = new double[N + 1];

    for (int i = 0; i <= N; i++) {
        in >> coef[i];
    }
    return true;
}

vector<double> readInput(const string& path, int &count) {
    ifstream in(path);
    if (!in) {
        throw runtime_error("Cannot open file " + path);
    }

    vector<double> res;
    double x;
    while (in >> x) {
        res.push_back(x);
    }
    count = (int)res.size();
    return res;
}

int main(int argc, char *argv[]) {
    MPI_Init(&argc, &argv);

    const int TAG_COEF = 10;
    const int TAG_V = 11;

    int rank, p;
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &p);

    string polinomFile = "polinom3.txt";
    string inputFile = "input3.txt";
    string outputFile  = "output3.txt";

    int N = 0;
    double *allCoef = nullptr;
    double myCoef = 0;

    int nrX = 0;
    vector<double> xs;

    if (rank == 0) {
        readPolinom(polinomFile, N, allCoef);

        if (p != N + 1) {
            cerr << "Trebe " << N + 1
                 << " procese MPI (mpirun -np " << N + 1 << " ...)\n";
            MPI_Abort(MPI_COMM_WORLD, 1);
        }

        myCoef = allCoef[0];

        for (int pid = 1; pid <= N; ++pid) {
            MPI_Send(&allCoef[pid], 1, MPI_DOUBLE, pid, TAG_COEF, MPI_COMM_WORLD);
        }

        xs = readInput(inputFile, nrX);
    }

    MPI_Bcast(&N,1, MPI_INT, 0, MPI_COMM_WORLD);
    MPI_Bcast(&nrX, 1, MPI_INT, 0, MPI_COMM_WORLD);

    if (rank != 0) {
        MPI_Status status;
        MPI_Recv(&myCoef, 1, MPI_DOUBLE, 0, TAG_COEF, MPI_COMM_WORLD, &status);
    }

    ofstream fout;
    if (rank == N) {
        fout.open(outputFile);
        if (!fout) {
            cerr << "Cannot open output file " << outputFile << '\n';
            MPI_Abort(MPI_COMM_WORLD, 2);
        }
    }

    for (int idx = 0; idx < nrX; ++idx) {
        double x;

        if (rank == 0) {
            x = xs[idx];
        }

        MPI_Bcast(&x, 1, MPI_DOUBLE, 0, MPI_COMM_WORLD);

        double v;
        MPI_Status status;

        if (rank == 0) {
            v = 0.0;
        } else {
            MPI_Recv(&v, 1, MPI_DOUBLE, rank - 1, TAG_V, MPI_COMM_WORLD, &status);
        }

        v = v * x + myCoef;

        if (rank == N) {
            fout << v << '\n';
        } else {
            MPI_Send(&v, 1, MPI_DOUBLE, rank + 1, TAG_V, MPI_COMM_WORLD);
        }
    }

    if (rank == N) {
        fout.close();
    }

    if (allCoef) {
        delete[] allCoef;
    }

    MPI_Finalize();
    return 0;
}
