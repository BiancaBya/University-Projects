#include <iostream>
#include <fstream>
#include <thread>
#include <vector>
#include <cassert>
using namespace std;
using namespace std::chrono;

const int N = 1000;
const int M = 1000;
const int K = 5;
int P = 2;

int matrix[N][M];
int kernel[K][K];

int result_sequential[N][M];
int result_parallel[N][M];


void read_matrix(string path) {
    ifstream in(path);

    for (int i = 0; i < N; i++) {
        for (int j = 0; j < M; j++) {
            in >> matrix[i][j];
        }
    }

    in.close();
}

void read_dynamic_matrix(string path, int** dynamic_matrix){
    ifstream in(path);

    for (int i = 0; i < N; i++){
        for (int j = 0; j < M; j++){
            in >> dynamic_matrix[i][j];
        }
    }

    in.close();
}

void write_dynamic(string path, int** dynamic_result) {
    ofstream out(path);

    for (int i = 0; i < N; i++) {
        for (int j = 0; j < M; j++) {
            out << dynamic_result[i][j] << " ";
        }
        out << endl;
    }

    out.close();
}

void read_kernel(string path){
    ifstream in(path);

    for (int i = 0; i < K; i++) {
        for (int j = 0; j < K; j++) {
            in >> kernel[i][j];
        }
    }

    in.close();
}

void write(string path) {
    ofstream out(path);

    for (int i = 0; i < N; i++) {
        for (int j = 0; j < M; j++) {
            out << result_sequential[i][j] << " ";
        }
        out << endl;
    }

    out.close();
}

int convolution(int x, int y, int kernelCenter) {
    int output = 0;

    for (int i = 0; i < K; ++i) {
        for (int j = 0; j < K; ++j) {

            int iFromMatrix = x - kernelCenter + i;
            int jFromMatrix = y - kernelCenter + j;
            if (iFromMatrix < 0) iFromMatrix = 0;
            if (iFromMatrix >= N) iFromMatrix = N - 1;
            if (jFromMatrix < 0) jFromMatrix = 0;
            if (jFromMatrix >= M) jFromMatrix = M - 1;

            output += matrix[iFromMatrix][jFromMatrix] * kernel[i][j];
        }
    }
    return output;
}

int convolution_dynamic(int x, int y, int kernelCenter, int** dynamic_matrix) {
    int output = 0;

    for (int i = 0; i < K; ++i) {
        for (int j = 0; j < K; ++j) {

            int iFromMatrix = x - kernelCenter + i;
            int jFromMatrix = y - kernelCenter + j;
            if (iFromMatrix < 0) iFromMatrix = 0;
            if (iFromMatrix >= N) iFromMatrix = N - 1;
            if (jFromMatrix < 0) jFromMatrix = 0;
            if (jFromMatrix >= M) jFromMatrix = M - 1;

            output += dynamic_matrix[iFromMatrix][jFromMatrix] * kernel[i][j];
        }
    }
    return output;
}

void sequential_rows(int kernelCenter) {
    for (int i = 0; i < N; i++) {
        for (int j = 0; j < M; j++) {
            result_sequential[i][j] = convolution(i, j, kernelCenter);
        }
    }
}

void parallel_rows(int kernelCenter, int start, int end) {
    for (int i = start; i < end; i++) {
        for (int j = 0; j < M; j++) {
            result_parallel[i][j] = convolution(i, j, kernelCenter);
        }
    }
}

void sequential_columns(int kernelCenter) {
    for (int j = 0; j < M; j++) {
        for (int i = 0; i < N; i++) {
            result_sequential[i][j] = convolution(i, j, kernelCenter);
        }
    }
}

void parallel_columns(int kernelCenter, int start, int end) {
    for (int j = start; j < end; j++) {
        for (int i = 0; i < N; i++) {
            result_parallel[i][j] = convolution(i, j, kernelCenter);
        }
    }
}



void sequential_rows_dynamic(int kernelCenter, int** dynamic_matrix, int** dynamic_result) {
    for (int i = 0; i < N; i++) {
        for (int j = 0; j < M; j++) {
            dynamic_result[i][j] = convolution_dynamic(i, j, kernelCenter, dynamic_matrix);
        }
    }
}

void parallel_rows_dynamic(int kernelCenter, int start, int end, int** dynamic_matrix, int** dynamic_result) {
    for (int i = start; i < end; i++) {
        for (int j = 0; j < M; j++) {
            dynamic_result[i][j] = convolution_dynamic(i, j, kernelCenter, dynamic_matrix);
        }
    }
}

void sequential_columns_dynamic(int kernelCenter, int** dynamic_matrix, int** dynamic_result) {
    for (int j = 0; j < M; j++) {
        for (int i = 0; i < N; i++) {
            dynamic_result[i][j] = convolution_dynamic(i, j, kernelCenter, dynamic_matrix);
        }
    }
}

void parallel_columns_dynamic(int kernelCenter, int start, int end, int** dynamic_matrix, int** dynamic_result) {
    for (int j = start; j < end; j++) {
        for (int i = 0; i < N; i++) {
            dynamic_result[i][j] = convolution_dynamic(i, j, kernelCenter, dynamic_matrix);
        }
    }
}



void sequential_execution_rows(int kernelCenter, int** dynamic_matrix, int** dynamic_result){

    auto startTimeSeq = high_resolution_clock::now();

    sequential_rows(kernelCenter);
    sequential_rows_dynamic(kernelCenter, dynamic_matrix, dynamic_result);

    auto endTimeSeq = high_resolution_clock::now();

    duration<double, milli> difference = endTimeSeq - startTimeSeq;

    cout << difference.count();
}

void sequential_execution_columns(int kernelCenter, int** dynamic_matrix, int** dynamic_result){

    auto startTimeSeq = high_resolution_clock::now();

    sequential_columns(kernelCenter);
    sequential_columns_dynamic(kernelCenter, dynamic_matrix, dynamic_result);

    auto endTimeSeq = high_resolution_clock::now();

    duration<double, milli> difference = endTimeSeq - startTimeSeq;

    cout << difference.count();
}

void parallel_execution_rows(int kernelCenter, int** dynamic_matrix, int** dynamic_result) {

    int dimension = N / P;
    int rest = N % P;

    int start = 0;
    int end = dimension;

    auto startTime = high_resolution_clock::now();
    thread threadsArr[P];

    for (int i = 0; i < P; i++) {
        if (rest > 0){
            end++;
            rest--;
        }

        threadsArr[i] = thread(parallel_rows, kernelCenter, start, end);
        threadsArr[i] = thread(parallel_rows_dynamic, kernelCenter, start, end, dynamic_matrix, dynamic_result);

        start = end;
        end += dimension;
    }

    for (int i = 0; i < P; i++){
        threadsArr[i].join();
    }

    auto endTime = high_resolution_clock::now();

    duration<double, milli> difference = endTime- startTime;

    cout << difference.count();
}

void parallel_execution_columns(int kernelCenter, int** dynamic_matrix, int** dynamic_result) {

    int dimension = M / P;
    int rest = M % P;

    int start = 0;
    int end = dimension;

    auto startTime = high_resolution_clock::now();
    thread threadsArr[P];

    for (int i = 0; i < P; i++) {
        if (rest > 0){
            end++;
            rest--;
        }

        threadsArr[i] = thread(parallel_columns, kernelCenter, start, end);
        threadsArr[i] = thread(parallel_columns_dynamic, kernelCenter, start, end, dynamic_matrix, dynamic_result);

        start = end;
        end += dimension;
    }

    for (int i = 0; i < P; i++){
        threadsArr[i].join();
    }

    auto endTime = high_resolution_clock::now();

    duration<double, milli> difference = endTime- startTime;

    cout << difference.count();
}

void validation(int** dynamic_result_sequential, int** dynamic_result_parallel){
    for (int j = 0; j < M; j++)
        assert(result_sequential[0][j] == result_parallel[0][j]);
    for (int j = 0; j < M; j++)
        assert(dynamic_result_sequential[0][j] == dynamic_result_parallel[0][j]);
}


int main(int argc, char** argv) {

    P = 2;

    int** matrix_dynamic = new int*[N];
    int** result_dynamic_sequential = new int*[N];
    int** result_dynamic_parallel = new int*[N];

    for (int i = 0; i < N; i++) {
        matrix_dynamic[i] = new int[M];
    }

    for (int i = 0; i < N; i++){
        result_dynamic_sequential[i] = new int[M];
    }

    for (int i = 0; i < N; i++){
        result_dynamic_parallel[i] = new int[M];
    }

    read_dynamic_matrix(R"(C:\Users\Next\Desktop\PPD\Lab1\C++\Lab1\matrix_1000_1000.txt)", matrix_dynamic);
    read_matrix(R"(C:\Users\Next\Desktop\PPD\Lab1\C++\Lab1\matrix_10000_10000.txt)");
    read_kernel("C:/Users/Next/Desktop/PPD/Lab1/C++/Lab1/kernel_5x5.txt");

    int kernelCenter = (K - 1) / 2;

    sequential_execution_rows(kernelCenter, matrix_dynamic, result_dynamic_sequential);
    sequential_execution_columns(kernelCenter, matrix_dynamic, result_dynamic_sequential);

    parallel_execution_rows(kernelCenter, matrix_dynamic, result_dynamic_parallel);
    parallel_execution_columns(kernelCenter, matrix_dynamic, result_dynamic_parallel);

    validation(result_dynamic_sequential, result_dynamic_parallel);

    write("C:/Users/Next/Desktop/PPD/Lab1/C++/Lab1/result_4_4txt");

    for (int i = 0; i < N; i++) {
        delete[] matrix_dynamic[i];
    }

    for (int i = 0; i < N; i++) {
        delete[] result_dynamic_sequential[i];
    }

    for (int i = 0; i < N; i++) {
        delete[] result_dynamic_parallel[i];
    }

    delete[] matrix_dynamic;
    delete[] result_dynamic_sequential;
    delete[] result_dynamic_parallel;

    return 0;
}



