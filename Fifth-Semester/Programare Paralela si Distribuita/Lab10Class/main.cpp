//#include <iostream>
//#include <omp.h>
//
//using namespace std;
//
//const int N = 10000;
//const int M = 1000;
//int a[N], b[N], c[N];
//int m1[M][M], m2[M][M], m3[M][M];
//
//void initMatrix(){
//    for (int i = 0; i < M; i++){
//        for (int j = 0; j < M; j++) {
//            m1[i][j] = rand() % 10;
//            m2[i][j] = rand() % 10;
//        }
//    }
//}
//
//void valid_sum_Matrix(){
//    for (int i = 0; i < M; i++){
//        for (int j = 0; j < M; j++) {
//            if (m3[i][j] != m1[i][j] + m2[i][j]) {
//                printf("BAD SUM %d != %d + %d", m3[i][j], m1[i][j], m2[i][j]);
//                exit(1);
//            }
//        }
//    }
//    printf("ALL GOOD\n");
//}
//
//void sum_Matrix(){
//#pragma omp parallel for collapse(2)
//    for (int i = 0; i < M; i++){
//        for (int j = 0; j < M; j++) {
//            m3[i][j] = m1[i][j] + m2[i][j];
//        }
//    }
//}
//
//void init(){
//    for (int i = 0; i < N; i++){
//        a[i] = rand() % 10;
//        b[i] = rand() % 10;
//    }
//}
//
//void valid_sum(){
//    for (int i = 0; i < N; i++){
//        if (c[i] != a[i] + b[i]){
//            printf("BAD SUM %d != %d + %d", c[i], a[i], b[i]);
//            exit(1);
//        }
//    }
//    printf("ALL GOOD\n");
//}
//
//void sum(){
//    for (int i = 0; i < N; i++){
//        c[i] = a[i] + b[i];
//    }
//}
//
//int dotProd() {
//    int res = 0;
//    int aux = 0;
////#pragma omp parallel for
////    {
////        for (int i = 0; i < N; i++){
////#pragma omp critical
////            res += a[i] * b[i];
////        }
////    }
////    return res;
//
//#pragma omp parallel shared(res) firstprivate(aux)
//    {
//#pragma omp for nowait
//        for (int i = 0; i < N; i++) {
//            aux += a[i] * b[i];
//        }
//#pragma omp atomic
//        res += aux;
//    }
//    return res;
//}
//
//void validateDot(int val){
//    for (int i = 0; i < N; i++){
//        val -= a[i] * b[i];
//    }
//    if (val == 0) {
//        printf("GOOD\n");
//    }
//    else {
//        printf("BAD = %d", val);
//    }
//}
//
//int newDot(){
//    int res = 0;
//
//#pragma omp parallel for reduction(+:res)
//    for (int i = 0; i < N; i++){
//        res += a[i] * b[i];
//    }
//
//    return res;
//}
//
//void omp_sections(){
//#pragma omp parallel num_threads(8)
//    {
//#pragma omp sections
//        {
//#pragma omp section
//            {
//#pragma omp critical
//                cout << "Hello from section 1 " << omp_get_thread_num()
//                     << "/" << omp_get_num_threads()
//                     << "/" << omp_get_max_threads() << endl;
//            }
//#pragma omp section
//            {
//#pragma omp critical
//                {
//                    cout << omp_get_thread_num() << endl;
//                    cout << "I am computing a sum " << endl;
//                }
//                for (int i = 0; i < N; i++)
//                    c[i] = a[i] + b[i];
//                valid_sum();
//            }
//#pragma omp section
//            {
//#pragma omp critical
//                {
//                    cout << omp_get_thread_num() << endl;
//                    cout << "I am computing a product " << endl;
//                }
//                int val = dotProd();
//#pragma omp critical
//                cout << "result: " << val << endl;
//                validateDot(val);
//            }
//        }
//    }
//}
//
//int main() {
//
//    cout << "Hello world " << omp_get_thread_num()
//        << "/" << omp_get_num_threads()
//        << "/" << omp_get_max_threads() << endl;
//
//    init();
//
//    omp_set_num_threads(2);
//
//#pragma omp parallel num_threads(2)
//    { // fork
//#pragma omp critical
//        {
//            cout << "Hello " << omp_get_thread_num()
//                    << "/" << omp_get_num_threads()
//                    << "/" << omp_get_max_threads() << endl;
//        }
//    } // join
//
////    double start = omp_get_wtime();
////    sum();
////    double end = omp_get_wtime();
////    double duration = end - start;
////    printf("Time: %f\n", duration);
////    valid_sum();
////
////    start = omp_get_wtime();
////    int val = dotProd();
////    end = omp_get_wtime();
////    duration = end - start;
////    printf("Time: %f\n", duration);
////    validateDot(val);
//
////    double start = omp_get_wtime();
////    int val = newDot();
////    double end = omp_get_wtime();
////    double duration = end - start;
////    printf("Time: %f\n", duration);
////    validateDot(val);
//
////    initMatrix();
////    double start = omp_get_wtime();
////    sum_Matrix();
////    double end = omp_get_wtime();
////    double duration = end - start;
////    printf("Time: %f\n", duration);
////    valid_sum_Matrix();
//
//    omp_sections();
//
//
//    return 0;
//}
//
//
#include <iostream>
#include <omp.h>
#include <math.h>

void functie(int n, float *a, float *b, float *y, float *z) {
    int i;

    // S-5: Incepe regiunea paralela cu 3 thread-uri
#pragma omp parallel default(shared) private(i)
    {
        // S-7: Impartirea lucrului pe sectiuni
#pragma omp sections
        {
#pragma omp section
            {
                // Sectiunea 1: Media (care rezulta in aceeasi valoare)
                for (i = 0; i < n; i++) {
                    b[i] = (a[i] + a[i]) / 2.0;
                }
            }

#pragma omp section
            {
                // Sectiunea 2: Radacina patrata
                for (i = 0; i < n; i++) {
                    y[i] = sqrt(a[i]);
                }
            }

#pragma omp section
            {
                // Sectiunea 3: Dublul valorii
                for (i = 0; i < n; i++) {
                    z[i] = a[i] * 2;
                }
            }
        }

        n++;
    }

    printf("Valoarea lui n dupa regiunea paralela: %d\n", n);
}

int main() {
    // S-23: Setam numarul de thread-uri la 3
    omp_set_num_threads(3);
    int n = 1000;

    // Alocam tablourile (pe stack conform imaginii, sau static)
    float a[1000], b[1000], c[1000], d[1000];

    // S-25: Initializare vector a cu patrate perfecte
    for (int i = 0; i < n; i++) {
        a[i] = (i + 1) * (i + 1);
    }

    // S-26: Apelul functiei
    // Nota: Am mapat y -> c si z -> d conform ordinii parametrilor
    functie(n, a, b, c, d);

    // Test afisare primele elemente pentru verificare
    printf("a[0]=%.1f, b[0]=%.1f, c[0]=%.1f, d[0]=%.1f\n", a[0], b[0], c[0], d[0]);
    printf("a[9]=%.1f, b[9]=%.1f, c[9]=%.1f, d[9]=%.1f\n", a[9], b[9], c[9], d[9]);

    return 0;
}


