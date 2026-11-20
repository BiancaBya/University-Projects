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

static void filter_inplace_cpu_rows(int N, int M, vector<int>& mat, const vector<int>& k, int K) {
    int r = K / 2;
    vector<int> out(M);
    vector<vector<int>> ring(K, vector<int>(M));

    auto loadRow = [&](int srcRow, vector<int>& dst) {
        srcRow = max(0, min(N - 1, srcRow));
        const int* src = &mat[srcRow * M];
        copy(src, src + M, dst.begin());
    };

    for (int t = 0; t < K; t++) {
        loadRow(t - r, ring[t]);
    }

    for (int i = 0; i < N; i++) {
        for (int j = 0; j < M; j++) {
            long long sum = 0;
            for (int di = 0; di < K; di++) {
                int jj = j;
                for (int dj = 0; dj < K; dj++) {
                    int col = jj + (dj - r);
                    if (col < 0) col = 0;
                    if (col >= M) col = M - 1;
                    sum += 1LL * k[di * K + dj] * ring[di][col];
                }
            }
            out[j] = (int)sum;
        }

        copy(out.begin(), out.end(), &mat[i * M]);

        if (i + 1 < N) {
            for (int t = 0; t < K - 1; t++) ring[t].swap(ring[t + 1]);
            loadRow(i + r + 1, ring[K - 1]);
        }
    }
}

__constant__ int d_kernel[25];

__device__ __forceinline__ int clampi(int v, int lo, int hi) {
    return v < lo ? lo : (v > hi ? hi : v);
}

__global__ void convRow_inplace_ring_shared(int* d_mat,
                                            const int* d_ring,
                                            int* d_ring_rw,
                                            int* d_head,
                                            int i, int N, int M, int K) {

    int j = blockIdx.x * blockDim.x + threadIdx.x;
    int r = K / 2;

    int head = *d_head;

    int tileW = blockDim.x + 2 * r;
    extern __shared__ int sh[];
    int blockStart = blockIdx.x * blockDim.x;

    for (int di = 0; di < K; di++) {
        int ringIdx = (head + di) % K;
        const int* ringRow = d_ring + ringIdx * M;

        for (int lx = threadIdx.x; lx < tileW; lx += blockDim.x) {
            int globalCol = blockStart + (lx - r);
            globalCol = clampi(globalCol, 0, M - 1);
            sh[di * tileW + lx] = ringRow[globalCol];
        }
    }

    __syncthreads();

    if (j < M) {
        long long sum = 0;

        int localX = threadIdx.x + r;

        for (int di = 0; di < K; di++) {
            int krow = di * K;
            int shOff = di * tileW;

            for (int dj = 0; dj < K; dj++) {
                int sx = localX + (dj - r);
                sum += 1LL * d_kernel[krow + dj] * sh[shOff + sx];
            }
        }

        d_mat[i * M + j] = (int)sum;
    }

    if (i + 1 < N && j < M) {
        int src = i + r + 1;
        if (src >= N) src = N - 1;

        int* dstRow = d_ring_rw + head * M;
        dstRow[j] = d_mat[src * M + j];
    }

    if (blockIdx.x == 0 && threadIdx.x == 0) {
        if (i + 1 < N) {
            *d_head = (head + 1) % K;
        }
    }
}

__global__ void init_ring_kernel(int* d_ring, int* d_head, const int* d_mat,
                                 int N, int M, int K) {
    int j = blockIdx.x * blockDim.x + threadIdx.x;
    if (j >= M) return;

    int r = K / 2;
    for (int t = 0; t < K; t++) {
        int src = t - r;
        if (src < 0) src = 0;
        if (src >= N) src = N - 1;
        d_ring[t * M + j] = d_mat[src * M + j];
    }

    if (blockIdx.x == 0 && threadIdx.x == 0) {
        *d_head = 0;
    }
}

static float filter_inplace_cuda_ring_optimized(int N, int M, int* d_mat,
                                                const vector<int>& k, int K,
                                                int blockSize, bool printLaunch) {
    if (K != 3 && K != 5) {
        cerr << "K must be 3 or 5\n";
        exit(1);
    }
    if (blockSize <= 0) blockSize = 256;

    cudaCheck(cudaMemcpyToSymbol(d_kernel, k.data(), (size_t)K * K * sizeof(int)),
              "cudaMemcpyToSymbol(d_kernel)");

    int* d_ring = nullptr;
    cudaCheck(cudaMalloc(&d_ring, (size_t)K * M * sizeof(int)), "cudaMalloc(d_ring)");

    int* d_head = nullptr;
    cudaCheck(cudaMalloc(&d_head, sizeof(int)), "cudaMalloc(d_head)");

    dim3 block(blockSize);
    dim3 grid((M + block.x - 1) / block.x);

    init_ring_kernel<<<grid, block>>>(d_ring, d_head, d_mat, N, M, K);
    cudaCheck(cudaGetLastError(), "init_ring_kernel launch");

    int r = K / 2;
    size_t shBytes = (size_t)K * (size_t)(blockSize + 2 * r) * sizeof(int);

    cudaEvent_t evStart, evStop;
    cudaCheck(cudaEventCreate(&evStart), "cudaEventCreate start");
    cudaCheck(cudaEventCreate(&evStop), "cudaEventCreate stop");
    cudaCheck(cudaEventRecord(evStart), "cudaEventRecord start");

    for (int i = 0; i < N; i++) {
        convRow_inplace_ring_shared<<<grid, block, shBytes>>>(
                d_mat, d_ring, d_ring, d_head, i, N, M, K
        );
        cudaCheck(cudaGetLastError(), "convRow_inplace_ring_shared launch");
    }

    cudaCheck(cudaEventRecord(evStop), "cudaEventRecord stop");
    cudaCheck(cudaEventSynchronize(evStop), "cudaEventSync stop");

    float ms = 0.0f;
    cudaCheck(cudaEventElapsedTime(&ms, evStart, evStop), "cudaEventElapsedTime");

    cudaEventDestroy(evStart);
    cudaEventDestroy(evStop);

    cudaFree(d_ring);
    cudaFree(d_head);

    return ms;
}


static bool equalMatrices(const vector<int>& a, const vector<int>& b) {
    if (a.size() != b.size()) return false;
    for (size_t i = 0; i < a.size(); i++) if (a[i] != b[i]) return false;
    return true;
}

static string baseNameNoExt(const string& path) {
    size_t p = path.find_last_of("/\\");
    string name = (p == string::npos) ? path : path.substr(p + 1);
    size_t dot = name.find_last_of('.');
    if (dot != string::npos) name = name.substr(0, dot);
    return name;
}

int main(int argc, char** argv) {
    if (argc < 7) {
        cerr << "Usage: cuda_lab.exe <matrixFile> <N> <M> <K> <P> <blockSize>\n";
        return 1;
    }

    string matrixPath = argv[1];
    int N = stoi(argv[2]);
    int M = stoi(argv[3]);
    int K = stoi(argv[4]);
    int P = stoi(argv[5]);
    int blockSize = stoi(argv[6]);

    (void)P;

    if (K != 3 && K != 5) {
        cerr << "K must be 3 or 5\n";
        return 1;
    }
    if (blockSize <= 0) blockSize = 256;

    bool printLaunch = false;

    string kernelPath = "kernel_" + to_string(K) + "x" + to_string(K) + ".txt";
    string outSeqPath  = "output_seq_"  + to_string(N) + "_" + to_string(M) + ".txt";
    string outCudaPath = "output_cuda_" + to_string(N) + "_" + to_string(M) + ".txt";

    vector<int> kernel;
    if (!loadKernelKxK(kernelPath, K, kernel)) {
        cerr << "Failed to read kernel " << K << "x" << K << " from: " << kernelPath << "\n";
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

    vector<int> h_seq = h_in;
    vector<int> h_cuda = h_in;

    auto t0 = high_resolution_clock::now();
    filter_inplace_cpu_rows(N, M, h_seq, kernel, K);
    auto t1 = high_resolution_clock::now();
    duration<double, milli> cpuMs = t1 - t0;

    int* d_mat = nullptr;
    cudaCheck(cudaMalloc(&d_mat, (size_t)N * M * sizeof(int)), "cudaMalloc(d_mat)");
    cudaCheck(cudaMemcpy(d_mat, h_in.data(), (size_t)N * M * sizeof(int),
                         cudaMemcpyHostToDevice),
              "H2D memcpy matrix");

    float gpuMs = filter_inplace_cuda_ring_optimized(N, M, d_mat, kernel, K, blockSize, printLaunch);

    cudaCheck(cudaMemcpy(h_cuda.data(), d_mat, (size_t)N * M * sizeof(int),
                         cudaMemcpyDeviceToHost),
              "D2H memcpy matrix");
    cudaFree(d_mat);

    bool ok = equalMatrices(h_seq, h_cuda);

    cout << "CPU_MS=" << cpuMs.count() << "\n";
    cout << "GPU_MS=" << gpuMs << "\n";
    cout << "OK=" << (ok ? 1 : 0) << "\n";

    writeMatrix(outSeqPath, N, M, h_seq);
    writeMatrix(outCudaPath, N, M, h_cuda);

    return ok ? 0 : 2;
}

