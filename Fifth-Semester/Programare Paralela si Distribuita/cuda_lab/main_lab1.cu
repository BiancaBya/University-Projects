#include <cuda_runtime.h>

#include <algorithm>
#include <chrono>
#include <fstream>
#include <iostream>
#include <string>
#include <vector>

using namespace std;
using namespace chrono;

static void cudaCheck(cudaError_t err, const char* where) {
    if (err != cudaSuccess) {
        cerr << "CUDA error at " << where << ": " << cudaGetErrorString(err)
             << " (" << (int)err << ")\n";
        exit(1);
    }
}

static bool readAllInts(const string& path, vector<int>& out) {
    ifstream in(path);
    if (!in.is_open()) return false;
    out.clear();
    int x;
    while (in >> x) out.push_back(x);
    return true;
}

static bool loadMatrixFlexible(const string& path, int& N, int& M, vector<int>& mat) {
    vector<int> data;
    if (!readAllInts(path, data)) return false;
    if (data.empty()) return false;

    if (data.size() >= 3) {
        int n0 = data[0], m0 = data[1];
        long long need = 1LL * n0 * m0;
        if (n0 > 0 && m0 > 0 && (long long)data.size() == 2 + need) {
            N = n0; M = m0;
            mat.assign(data.begin() + 2, data.end());
            return true;
        }
    }

    long long need = 1LL * N * M;
    if ((long long)data.size() != need) return false;
    mat = std::move(data);
    return true;
}

static bool loadKernelKxK(const string& path, int K, vector<int>& kernel) {
    ifstream in(path);
    if (!in.is_open()) return false;
    kernel.assign(K * K, 0);
    for (int i = 0; i < K * K; i++) {
        if (!(in >> kernel[i])) return false;
    }
    return true;
}

static bool writeMatrix(const string& path, int N, int M, const vector<int>& mat) {
    ofstream out(path);
    if (!out.is_open()) return false;
    for (int i = 0; i < N; i++) {
        const int* row = &mat[i * M];
        for (int j = 0; j < M; j++) out << row[j] << ' ';
        out << '\n';
    }
    return true;
}

static inline int clampi(int x, int lo, int hi) {
    if (x < lo) return lo;
    if (x > hi) return hi;
    return x;
}

static void conv_cpu(int N, int M, const vector<int>& in, vector<int>& out, const vector<int>& k, int K) {
    out.assign((size_t)N * M, 0);
    int r = K / 2;

    for (int i = 0; i < N; i++) {
        for (int j = 0; j < M; j++) {
            long long sum = 0;
            for (int di = 0; di < K; di++) {
                int ii = clampi(i + (di - r), 0, N - 1);
                for (int dj = 0; dj < K; dj++) {
                    int jj = clampi(j + (dj - r), 0, M - 1);
                    sum += 1LL * k[di * K + dj] * in[(size_t)ii * M + jj];
                }
            }
            out[(size_t)i * M + j] = (int)sum;
        }
    }
}

static bool equalMatrices(const vector<int>& a, const vector<int>& b) {
    if (a.size() != b.size()) return false;
    for (size_t i = 0; i < a.size(); i++) if (a[i] != b[i]) return false;
    return true;
}

__constant__ int d_kernel[25];

__global__ void conv2d_rows_kernel(const int* __restrict__ in, int* __restrict__ out, int N, int M, int K) {
    int j = (int)(blockIdx.x * blockDim.x + threadIdx.x);
    int i = (int)(blockIdx.y * blockDim.y + threadIdx.y);
    if (i >= N || j >= M) return;

    int r = K / 2;
    long long sum = 0;

    for (int di = 0; di < K; di++) {
        int ii = i + (di - r);
        if (ii < 0) ii = 0;
        if (ii >= N) ii = N - 1;

        for (int dj = 0; dj < K; dj++) {
            int jj = j + (dj - r);
            if (jj < 0) jj = 0;
            if (jj >= M) jj = M - 1;

            sum += 1LL * d_kernel[di * K + dj] * in[(size_t)ii * M + jj];
        }
    }

    out[(size_t)i * M + j] = (int)sum;
}

__global__ void conv2d_cols_kernel(const int* __restrict__ in, int* __restrict__ out, int N, int M, int K) {
    int i = (int)(blockIdx.x * blockDim.x + threadIdx.x);
    int j = (int)(blockIdx.y * blockDim.y + threadIdx.y);
    if (i >= N || j >= M) return;

    int r = K / 2;
    long long sum = 0;

    for (int di = 0; di < K; di++) {
        int ii = i + (di - r);
        if (ii < 0) ii = 0;
        if (ii >= N) ii = N - 1;

        for (int dj = 0; dj < K; dj++) {
            int jj = j + (dj - r);
            if (jj < 0) jj = 0;
            if (jj >= M) jj = M - 1;

            sum += 1LL * d_kernel[di * K + dj] * in[(size_t)ii * M + jj];
        }
    }

    out[(size_t)i * M + j] = (int)sum;
}

static void chooseBlock2D(int blockSize, int& bx, int& by) {
    if (blockSize <= 0) blockSize = 256;
    bx = 32;
    by = blockSize / bx;
    if (by <= 0) { bx = 16; by = blockSize / bx; }
    if (by <= 0) { bx = 8;  by = blockSize / bx; }
    if (by <= 0) { bx = 1;  by = 1; }
    if (by > 16) by = 16;
    if (bx * by > 1024) by = 1024 / bx;
    if (by <= 0) { bx = 16; by = 16; }
}

static float conv_cuda(int N, int M, int* d_in, int* d_out, const vector<int>& kernel, int K,
                       const string& mode, int blockSize) {
    if (K != 3 && K != 5) {
        cerr << "K must be 3 or 5\n";
        exit(1);
    }

    cudaCheck(cudaMemcpyToSymbol(d_kernel, kernel.data(), (size_t)K * K * sizeof(int)),
              "cudaMemcpyToSymbol(d_kernel)");

    int bx, by;
    chooseBlock2D(blockSize, bx, by);

    dim3 block((unsigned)bx, (unsigned)by);

    cudaEvent_t evStart, evStop;
    cudaCheck(cudaEventCreate(&evStart), "cudaEventCreate start");
    cudaCheck(cudaEventCreate(&evStop), "cudaEventCreate stop");
    cudaCheck(cudaEventRecord(evStart), "cudaEventRecord start");

    if (mode == "rows") {
        dim3 grid((unsigned)((M + bx - 1) / bx), (unsigned)((N + by - 1) / by));
        conv2d_rows_kernel<<<grid, block>>>(d_in, d_out, N, M, K);
        cudaCheck(cudaGetLastError(), "conv2d_rows_kernel launch");
    } else if (mode == "cols") {
        dim3 grid((unsigned)((N + bx - 1) / bx), (unsigned)((M + by - 1) / by));
        conv2d_cols_kernel<<<grid, block>>>(d_in, d_out, N, M, K);
        cudaCheck(cudaGetLastError(), "conv2d_cols_kernel launch");
    } else {
        cerr << "mode must be 'rows' or 'cols'\n";
        exit(1);
    }

    cudaCheck(cudaEventRecord(evStop), "cudaEventRecord stop");
    cudaCheck(cudaEventSynchronize(evStop), "cudaEventSync stop");

    float ms = 0.0f;
    cudaCheck(cudaEventElapsedTime(&ms, evStart, evStop), "cudaEventElapsedTime");

    cudaEventDestroy(evStart);
    cudaEventDestroy(evStop);

    return ms;
}

int main(int argc, char** argv) {
    if (argc < 8) {
        cerr << "Usage: lab1_cuda.exe <matrixFile> <N> <M> <K> <P> <blockSize> <mode(rows|cols)>\n";
        return 1;
    }

    string matrixPath = argv[1];
    int N = stoi(argv[2]);
    int M = stoi(argv[3]);
    int K = stoi(argv[4]);
    int P = stoi(argv[5]); (void)P;
    int blockSize = stoi(argv[6]);
    string mode = argv[7];

    if (K != 3 && K != 5) {
        cerr << "K must be 3 or 5\n";
        return 1;
    }
    if (blockSize <= 0) blockSize = 256;
    if (mode != "rows" && mode != "cols") {
        cerr << "mode must be 'rows' or 'cols'\n";
        return 1;
    }

    string kernelPath = "kernel_" + to_string(K) + "x" + to_string(K) + ".txt";
    string outSeqPath  = "output_seq_"  + to_string(N) + "_" + to_string(M) + "_" + mode + ".txt";
    string outCudaPath = "output_cuda_" + to_string(N) + "_" + to_string(M) + "_" + mode + ".txt";

    vector<int> kernel;
    if (!loadKernelKxK(kernelPath, K, kernel)) {
        cerr << "Failed to read kernel from: " << kernelPath << "\n";
        return 1;
    }

    vector<int> h_in;
    if (!loadMatrixFlexible(matrixPath, N, M, h_in)) {
        cerr << "Failed to read matrix from: " << matrixPath << "\n";
        return 1;
    }
    if ((int)h_in.size() != N * M) {
        cerr << "Matrix size mismatch\n";
        return 1;
    }

    vector<int> h_seq;
    auto t0 = high_resolution_clock::now();
    conv_cpu(N, M, h_in, h_seq, kernel, K);
    auto t1 = high_resolution_clock::now();
    duration<double, milli> cpuMs = t1 - t0;

    size_t bytes = (size_t)N * (size_t)M * sizeof(int);

    int* d_in = nullptr;
    int* d_out = nullptr;

    cudaCheck(cudaMalloc(&d_in, bytes), "cudaMalloc(d_in)");
    cudaCheck(cudaMalloc(&d_out, bytes), "cudaMalloc(d_out)");
    cudaCheck(cudaMemcpy(d_in, h_in.data(), bytes, cudaMemcpyHostToDevice), "H2D matrix");

    float gpuMs = conv_cuda(N, M, d_in, d_out, kernel, K, mode, blockSize);

    vector<int> h_cuda((size_t)N * M);
    cudaCheck(cudaMemcpy(h_cuda.data(), d_out, bytes, cudaMemcpyDeviceToHost), "D2H result");

    cudaFree(d_in);
    cudaFree(d_out);

    bool ok = equalMatrices(h_seq, h_cuda);

    cout << "CPU_MS=" << cpuMs.count() << "\n";
    cout << "GPU_MS=" << gpuMs << "\n";
    cout << "OK=" << (ok ? 1 : 0) << "\n";

    writeMatrix(outSeqPath, N, M, h_seq);
    writeMatrix(outCudaPath, N, M, h_cuda);

    return ok ? 0 : 2;
}
