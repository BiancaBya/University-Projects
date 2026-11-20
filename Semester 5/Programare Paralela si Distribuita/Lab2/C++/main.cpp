#include <iostream>
#include <fstream>
#include <thread>
#include <vector>
#include <cassert>
#include <barrier>
#include <string>

using namespace std;
using namespace chrono;

const int N = 1000;
const int M = 1000;
const int K = 3;
int P = 2;
barrier barr(P);

int kernel[K][K];


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


void sequential_rows_dynamic(int** dynamic_matrix) {
    vector<int> prev_line(M), current_line(M), next_line(M), out(M);

    copy(dynamic_matrix[0], dynamic_matrix[0] + M, prev_line.begin());
    copy(dynamic_matrix[0], dynamic_matrix[0] + M, current_line.begin());
    copy(dynamic_matrix[1], dynamic_matrix[1] + M, next_line.begin());

    for (int i = 0; i < N; ++i) {
        for (int j = 0; j < M; ++j) {

            int left = (j == 0) ? current_line[j] : current_line[j - 1];
            int right = (j == M - 1) ? current_line[j] : current_line[j + 1];
            int top = prev_line[j];
            int bottom = next_line[j];
            int top_left = (j == 0) ? prev_line[j] : prev_line[j - 1];
            int top_right = (j == M - 1) ? prev_line[j] : prev_line[j + 1];
            int bottom_left = (j == 0) ? next_line[j] : next_line[j - 1];
            int bottom_right = (j == M - 1) ? next_line[j] : next_line[j + 1];

            out[j] =
                    kernel[0][0] * top_left +
                    kernel[0][1] * top +
                    kernel[0][2] * top_right +
                    kernel[1][0] * left +
                    kernel[1][1] * current_line[j] +
                    kernel[1][2] * right +
                    kernel[2][0] * bottom_left +
                    kernel[2][1] * bottom +
                    kernel[2][2] * bottom_right;
        }

        copy(out.begin(), out.end(), dynamic_matrix[i]);

        if (i + 1 < N) {
            prev_line.swap(current_line);
            current_line.swap(next_line);

            if (i + 2 < N)
                copy(dynamic_matrix[i + 2], dynamic_matrix[i + 2] + M, next_line.begin());
            else
                copy(dynamic_matrix[N - 1], dynamic_matrix[N - 1] + M, next_line.begin());

        }
    }
}



void parallel_rows_dynamic(int start, int end, int** dynamic_matrix) {
    vector<int> top_frontier(M), bottom_frontier(M);

    for (int j = 0; j < M; ++j) {
        top_frontier[j] = (start != 0) ? dynamic_matrix[start - 1][j] : dynamic_matrix[start][j];
        bottom_frontier[j] = (end != N) ? dynamic_matrix[end][j] : dynamic_matrix[end - 1][j];
    }

    barr.arrive_and_wait();

    vector<int> prev_line(M), current_line(M), next_line(M);

    copy(top_frontier.begin(), top_frontier.end(), prev_line.begin());
    copy(dynamic_matrix[start], dynamic_matrix[start] + M, current_line.begin());

    if (start + 1 < end)
        copy(dynamic_matrix[start + 1], dynamic_matrix[start + 1] + M, next_line.begin());
    else
        copy(bottom_frontier.begin(), bottom_frontier.end(), next_line.begin());

    for (int i = start; i < end; i++) {

        for (int j = 0; j < M; j++) {

            int left = (j == 0) ? current_line[j] : current_line[j - 1];
            int right = (j == M - 1) ? current_line[j] : current_line[j + 1];
            int top = prev_line[j];
            int bottom = next_line[j];
            int top_left = (j == 0) ? prev_line[j] : prev_line[j - 1];
            int top_right = (j == M - 1) ? prev_line[j] : prev_line[j + 1];
            int bottom_left = (j == 0) ? next_line[j] : next_line[j - 1];
            int bottom_right = (j == M - 1) ? next_line[j] : next_line[j + 1];

            dynamic_matrix[i][j] =
                    kernel[0][0] * top_left +
                    kernel[0][1] * top +
                    kernel[0][2] * top_right +
                    kernel[1][0] * left +
                    kernel[1][1] * current_line[j] +
                    kernel[1][2] * right +
                    kernel[2][0] * bottom_left +
                    kernel[2][1] * bottom +
                    kernel[2][2] * bottom_right;
        }

        if (i + 1 < end) {
            prev_line.swap(current_line);
            current_line.swap(next_line);
            if (i + 2 < end)
                copy(dynamic_matrix[i + 2], dynamic_matrix[i + 2] + M, next_line.begin());
            else
                copy(bottom_frontier.begin(), bottom_frontier.end(), next_line.begin());
        }
    }
}



void sequential_execution_rows(int** dynamic_matrix){

    auto startTimeSeq = high_resolution_clock::now();

    sequential_rows_dynamic(dynamic_matrix);

    auto endTimeSeq = high_resolution_clock::now();

    duration<double, milli> difference = endTimeSeq - startTimeSeq;

    cout << difference.count();
}


void parallel_execution_rows(int** dynamic_matrix) {

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

        threadsArr[i] = thread(parallel_rows_dynamic, start, end, dynamic_matrix);

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


void validation(int** dynamic_result_parallel, int** dynamic_result_sequential){
    for (int j = 0; j < M; j++)
        assert(dynamic_result_parallel[0][j] == dynamic_result_sequential[0][j]);
}


bool filesAreEqual(const string& path1, const string& path2) {
    ifstream file1(path1);
    ifstream file2(path2);

    if (!file1.is_open() || !file2.is_open()) {
        return false;
    }

    string line1, line2;
    int lineNr = 1;

    while (true) {
        bool eof1 = !getline(file1, line1);
        bool eof2 = !getline(file2, line2);

        if (eof1 && eof2)
            return true;
        if (eof1 != eof2) {
            return false;
        }

        if (line1 != line2) {
            return false;
        }

        ++lineNr;
    }
}



int main(int argc, char** argv) {

    P = 4;

    int** matrix_dynamic_parallel = new int*[N];
    for (int i = 0; i < N; i++) {
        matrix_dynamic_parallel[i] = new int[M];
    }

    int** matrix_dynamic_sequential = new int*[N];
    for (int i = 0; i < N; i++) {
        matrix_dynamic_sequential[i] = new int[M];
    }

    read_dynamic_matrix(R"(C:\Users\Next\Desktop\PPD\Lab1\C++\Lab1\matrix_1000_1000.txt)", matrix_dynamic_parallel);
    read_dynamic_matrix(R"(C:\Users\Next\Desktop\PPD\Lab1\C++\Lab1\matrix_1000_1000.txt)", matrix_dynamic_sequential);
    read_kernel("C:/Users/Next/Desktop/PPD/Lab1/C++/Lab1/kernel_3x3.txt");


    sequential_execution_rows(matrix_dynamic_sequential);

    cout << endl;

    parallel_execution_rows(matrix_dynamic_parallel);

    //validation(matrix_dynamic_parallel, matrix_dynamic_sequential);

    write_dynamic("C:/Users/Next/Desktop/PPD/Lab2/C++/output_par_1000_1000.txt", matrix_dynamic_parallel);
    write_dynamic("C:/Users/Next/Desktop/PPD/Lab2/C++/output_seq_1000_1000.txt", matrix_dynamic_sequential);
    filesAreEqual("C:/Users/Next/Desktop/PPD/Lab2/C++/output_par_1000_1000.txt", "C:/Users/Next/Desktop/PPD/Lab2/C++/output_seq_10000_10000.txt");

    for (int i = 0; i < N; i++) {
        delete[] matrix_dynamic_parallel[i];
    }
    delete[] matrix_dynamic_parallel;

    for (int i = 0; i < N; i++) {
        delete[] matrix_dynamic_sequential[i];
    }
    delete[] matrix_dynamic_sequential;

    return 0;
}



