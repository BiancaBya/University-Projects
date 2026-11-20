#include <iostream>
#include <thread>
#include <cassert>
#include <chrono>
#include <cmath>
using namespace std;
using namespace std::chrono;

const int N = 1000000;
const int P = 4;
const int MAX_VAL = 100;
int a[N], b[N], c[N], c_secv[N];

void randomise(int *v, int len){
    for (int i = 0; i < len; i++){
        v[i] = rand() % MAX_VAL;
    }
}

void sum(const int *a, const int *b, int *c, int len){
    for (int i = 0; i < len; i++){
        c[i] = a[i] + b[i];
    }
}

void sum_par(const int *a, const int *b, int *c, int start, int end){
    for (int i = start; i < end; i++){
        c[i] = a[i] + b[i];
//        c[i] = sqrt((a[i] + b[i]) + a[i] * a[i] * b[i]); // mai mult de procesat ca sa fie varianta paralela mai rapida
    }
}

void create_workers(const int *a, const int *b, int *c, int *c_secv) {

    auto start_sum_par = high_resolution_clock::now();
    int dim_thr = N / P;
    int r = N % P;

    int start_idx = 0;
    int end_idx = dim_thr;

    thread t[P];

    for (int thr_idx = 0; thr_idx < P; thr_idx++){
        if (r > 0){
            end_idx++;
            r--;
        }

//        cout << "t: " << thr_idx
//        << " start: " << start_idx
//        << " end: " << end_idx << endl;

        t[thr_idx] = thread(sum_par, a, b, c, start_idx, end_idx);

        start_idx = end_idx;
        end_idx += dim_thr;
    }

    for (int thr_idx = 0; thr_idx < P; thr_idx++){
        t[thr_idx].join();
    }

    auto end_sum_par = high_resolution_clock::now();
    duration<double, milli> par_time = end_sum_par - start_sum_par;

    cout << "Parallel: " << par_time.count() << " ms" << endl;

    for (int i = 0; i < N; i++){
        assert(c[i] == c_secv[i]);
    }
}

int main() {

    randomise(a, N);
    randomise(b, N);

    auto start_sum_secv = high_resolution_clock::now();
    sum(a, b, c_secv, N);
    auto end_sum_secv = high_resolution_clock::now();
    duration<double, milli> secv_time = end_sum_secv - start_sum_secv;

    cout << "Secv: " << secv_time.count() << " ms" << endl;

    cout << "a[0]: " << a[0] << " b[0]: " << b[0]
    << " c[0]: " << c_secv[0] << endl;

    create_workers(a, b, c, c_secv);

    int *a_dyn = new int[N];
    int *b_dyn = new int[N];
    int *c_dyn = new int[N];

    int *arr = new int[N * 3];

    for (int i = 0; i < N; i++){
        a_dyn[i] = a[i];
        b_dyn[i] = b[i];
        c_dyn[i] = c[i];

        arr[i] = a[i];
        arr[i + N] = b[i];
    }

    create_workers(a_dyn, b_dyn, c_dyn, c_secv);

    create_workers(arr, arr + N, arr + N * 2, c_secv);

    delete[] a_dyn;
    delete[] b_dyn;
    delete[] c_dyn;
    delete[] arr;

}

