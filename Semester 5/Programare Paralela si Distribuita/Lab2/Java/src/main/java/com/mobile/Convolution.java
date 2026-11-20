package com.mobile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.*;

public class Convolution {

    static final int N = 1000;
    static final int M = 1000;
    static final int K = 3;
    static int P = 16;
    static CyclicBarrier barrier = new CyclicBarrier(P);

    static int[][] kernel = new int[K][K];

    static void readDynamicMatrix(String path, int[][] matrix) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            StringTokenizer st;
            for (int i = 0; i < N; i++) {
                int j = 0;
                while (j < M) {
                    String line = br.readLine();
                    if (line == null) throw new EOFException("Unexpected EOF at row " + i);
                    st = new StringTokenizer(line);
                    while (st.hasMoreTokens() && j < M) {
                        matrix[i][j++] = Integer.parseInt(st.nextToken());
                    }
                }
            }
        }
    }

    static void writeDynamic(String path, int[][] matrix) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            for (int i = 0; i < N; i++) {
                StringBuilder sb = new StringBuilder(M * 4);
                for (int j = 0; j < M; j++) {
                    sb.append(matrix[i][j]).append(' ');
                }
                bw.write(sb.toString().trim());
                bw.newLine();
            }
        }
    }

    static void readKernel(String path) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            StringTokenizer st;
            for (int i = 0; i < K; i++) {
                int j = 0;
                while (j < K) {
                    String line = br.readLine();
                    if (line == null) throw new EOFException("Unexpected EOF in kernel at row " + i);
                    st = new StringTokenizer(line);
                    while (st.hasMoreTokens() && j < K) {
                        kernel[i][j++] = Integer.parseInt(st.nextToken());
                    }
                }
            }
        }
    }

    static void sequentialRowsDynamic(int[][] matrix) {
        int[] prev = new int[M];
        int[] curr = new int[M];
        int[] next = new int[M];
        int[] out  = new int[M];

        System.arraycopy(matrix[0], 0, prev, 0, M);
        System.arraycopy(matrix[0], 0, curr, 0, M);
        if (N >= 2) System.arraycopy(matrix[1], 0, next, 0, M);
        else System.arraycopy(matrix[0], 0, next, 0, M);

        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < M; ++j) {
                int left = (j == 0) ? curr[j] : curr[j - 1];
                int right = (j == M - 1) ? curr[j] : curr[j + 1];
                int top = prev[j];
                int bottom = next[j];
                int top_left = (j == 0) ? prev[j] : prev[j - 1];
                int top_right = (j == M - 1) ? prev[j] : prev[j + 1];
                int bottom_left = (j == 0) ? next[j] : next[j - 1];
                int bottom_right = (j == M - 1) ? next[j] : next[j + 1];

                out[j] =
                                kernel[0][0] * top_left +
                                kernel[0][1] * top +
                                kernel[0][2] * top_right +
                                kernel[1][0] * left +
                                kernel[1][1] * curr[j] +
                                kernel[1][2] * right +
                                kernel[2][0] * bottom_left +
                                kernel[2][1] * bottom +
                                kernel[2][2] * bottom_right;
            }

            System.arraycopy(out, 0, matrix[i], 0, M);

            if (i + 1 < N) {
                int[] tmp = prev; prev = curr; curr = next; next = tmp;
                if (i + 2 < N) System.arraycopy(matrix[i + 2], 0, next, 0, M);
                else System.arraycopy(matrix[N - 1], 0, next, 0, M);
            }
        }
    }

    static void parallelRowsDynamic(int start, int end, int[][] matrix) throws BrokenBarrierException, InterruptedException {
        int[] topFrontier= new int[M];
        int[] bottomFrontier = new int[M];

        System.arraycopy(matrix[(start != 0) ? (start - 1) : start], 0, topFrontier, 0, M);
        System.arraycopy(matrix[(end != N) ? end : (end - 1)], 0, bottomFrontier, 0, M);

        barrier.await();

        int[] prev = new int[M];
        int[] curr = new int[M];
        int[] next = new int[M];

        System.arraycopy(topFrontier, 0, prev, 0, M);
        System.arraycopy(matrix[start], 0, curr, 0, M);
        if (start + 1 < end)
            System.arraycopy(matrix[start + 1], 0, next, 0, M);
        else
            System.arraycopy(bottomFrontier, 0, next, 0, M);

        for (int i = start; i < end; ++i) {
            for (int j = 0; j < M; ++j) {
                int left = (j == 0) ? curr[j] : curr[j - 1];
                int right = (j == M - 1) ? curr[j] : curr[j + 1];
                int top = prev[j];
                int bottom = next[j];
                int top_left = (j == 0) ? prev[j] : prev[j - 1];
                int top_right = (j == M - 1) ? prev[j] : prev[j + 1];
                int bottom_left = (j == 0) ? next[j] : next[j - 1];
                int bottom_right = (j == M - 1) ? next[j] : next[j + 1];

                matrix[i][j] =
                                kernel[0][0] * top_left +
                                kernel[0][1] * top +
                                kernel[0][2] * top_right +
                                kernel[1][0] * left +
                                kernel[1][1] * curr[j] +
                                kernel[1][2] * right +
                                kernel[2][0] * bottom_left +
                                kernel[2][1] * bottom +
                                kernel[2][2] * bottom_right;
            }

            if (i + 1 < end) {
                int[] tmp = prev; prev = curr; curr = next; next = tmp;
                if (i + 2 < end)
                    System.arraycopy(matrix[i + 2], 0, next, 0, M);
                else
                    System.arraycopy(bottomFrontier, 0, next, 0, M);
            }
        }
    }

    static void sequentialExecutionRows(int[][] matrix) {
        long t0 = System.nanoTime();
        sequentialRowsDynamic(matrix);
        long t1 = System.nanoTime();

        double ms = (t1 - t0) / 1_000_000.0;
        System.out.println(ms);
    }

    static void parallelExecutionRows(int[][] matrix) throws InterruptedException {
        int dimension = N / P;
        int rest = N % P;

        int start = 0;
        int end = dimension;

        long t0 = System.nanoTime();

        Thread[] threads = new Thread[P];
        for (int i = 0; i < P; i++) {
            if (rest > 0) { end++; rest--; }

            final int s = start, e = end;
            threads[i] = new Thread(() -> {
                try {
                    parallelRowsDynamic(s, e, matrix);
                } catch (BrokenBarrierException | InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            });
            threads[i].start();

            start = end;
            end += dimension;
        }

        for (Thread th : threads) th.join();

        long t1 = System.nanoTime();
        double ms = (t1 - t0) / 1_000_000.0;
        System.out.println(ms);
    }

    static void validation(int[][] parallel, int[][] sequential) {
        for (int j = 0; j < M; j++) {
            if (parallel[0][j] != sequential[0][j]) {
                throw new AssertionError("Mismatch at (0," + j + "): "
                        + parallel[0][j] + " != " + sequential[0][j]);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        P = 16;

        int[][] matrixParallel   = new int[N][M];
        int[][] matrixSequential = new int[N][M];

        String matrixPath = "C:/Users/Next/Desktop/PPD/Lab1/C++/Lab1/matrix_1000_1000.txt";
        String kernelPath = "C:/Users/Next/Desktop/PPD/Lab1/C++/Lab1/kernel_3x3.txt";
        String outParallelPath   = "C:/Users/Next/Desktop/PPD/Lab2/C++/output_par_1000_1000.txt";
        String outSequentialPath   = "C:/Users/Next/Desktop/PPD/Lab2/C++/output_seq_1000_1000.txt";

        readDynamicMatrix(matrixPath, matrixParallel);
        readDynamicMatrix(matrixPath, matrixSequential);
        readKernel(kernelPath);

        sequentialExecutionRows(matrixSequential);
        parallelExecutionRows(matrixParallel);

        writeDynamic(outParallelPath, matrixParallel);
        writeDynamic(outSequentialPath, matrixSequential);

        Files.mismatch(Path.of(outParallelPath), Path.of(outSequentialPath));

        //validation(matrixParallel, matrixSequential);
    }
}


