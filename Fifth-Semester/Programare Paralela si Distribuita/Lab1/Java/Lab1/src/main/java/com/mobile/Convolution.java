package com.mobile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Convolution {

    static final int N = 1000;
    static final int M = 1000;
    static final int K = 5;
    static int P = 2;

    static int[][] matrix = new int[N][M];
    static int[][] kernel = new int[K][K];

    static int[][] result_sequential = new int[N][M];
    static int[][] result_parallel   = new int[N][M];


    static void read_matrix(String path) {
        try (Scanner sc = new Scanner(new File(path), StandardCharsets.UTF_8.name())) {
            for (int i = 0; i < N; i++) {
                int[] row = matrix[i];
                for (int j = 0; j < M; j++) {
                    row[j] = sc.nextInt();
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found: " + path, e);
        }
    }

    static void read_kernel(String path) {
        try (Scanner sc = new Scanner(new File(path), StandardCharsets.UTF_8.name())) {
            for (int i = 0; i < K; i++) {
                for (int j = 0; j < K; j++) {
                    kernel[i][j] = sc.nextInt();
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found: " + path, e);
        }
    }

    static void write(String path) {
        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(path), StandardCharsets.UTF_8), 1 << 20)) {
            for (int i = 0; i < N; i++) {
                int[] row = result_sequential[i];
                for (int j = 0; j < M; j++) {
                    out.write(Integer.toString(row[j]));
                    out.write(' ');
                }
                out.write('\n');
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to write results to " + path, e);
        }
    }

    static int convolution(int x, int y, int kernelCenter) {
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

    static void sequential_rows(int kernelCenter) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                result_sequential[i][j] = convolution(i, j, kernelCenter);
            }
        }
    }

    static void parallel_rows(int kernelCenter, int start, int end) {
        for (int i = start; i < end; i++) {
            for (int j = 0; j < M; j++) {
                result_parallel[i][j] = convolution(i, j, kernelCenter);
            }
        }
    }

    static void sequential_columns(int kernelCenter) {
        for (int j = 0; j < M; j++) {
            for (int i = 0; i < N; i++) {
                result_sequential[i][j] = convolution(i, j, kernelCenter);
            }
        }
    }

    static void parallel_columns(int kernelCenter, int start, int end) {
        for (int j = start; j < end; j++) {
            for (int i = 0; i < N; i++) {
                result_parallel[i][j] = convolution(i, j, kernelCenter);
            }
        }
    }

    static void sequential_execution_rows(int kernelCenter) {
        long startTime = System.nanoTime();
        sequential_rows(kernelCenter);
        long endTime = System.nanoTime();
        double ms = (endTime - startTime) / 1_000_000.0;
        System.out.println(ms);
    }

    static void sequential_execution_columns(int kernelCenter) {
        long startTime = System.nanoTime();
        sequential_columns(kernelCenter);
        long endTime = System.nanoTime();
        double ms = (endTime - startTime) / 1_000_000.0;
        System.out.println(ms);
    }

    static void parallel_execution_rows(int kernelCenter) {
        int dimension = N / P;
        int rest = N % P;

        int start = 0;
        int end = dimension;

        long startTime = System.nanoTime();
        Thread[] threadsArr = new Thread[P];

        for (int i = 0; i < P; i++) {
            if (rest > 0) {
                end++;
                rest--;
            }
            final int s = start, e = end;
            threadsArr[i] = new Thread(() -> parallel_rows(kernelCenter, s, e));
            threadsArr[i].start();

            start = end;
            end += dimension;
        }

        for (int i = 0; i < P; i++) {
            try {
                threadsArr[i].join();
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Thread interrupted", ie);
            }
        }

        long endTime = System.nanoTime();
        double ms = (endTime - startTime) / 1_000_000.0;
        System.out.println(ms);
    }

    static void parallel_execution_columns(int kernelCenter) {
        int dimension = M / P;
        int rest = M % P;

        int start = 0;
        int end = dimension;

        long startTime = System.nanoTime();
        Thread[] threadsArr = new Thread[P];

        for (int i = 0; i < P; i++) {
            if (rest > 0) {
                end++;
                rest--;
            }
            final int s = start, e = end;
            threadsArr[i] = new Thread(() -> parallel_columns(kernelCenter, s, e));
            threadsArr[i].start();

            start = end;
            end += dimension;
        }

        for (int i = 0; i < P; i++) {
            try {
                threadsArr[i].join();
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Thread interrupted", ie);
            }
        }

        long endTime = System.nanoTime();
        double ms = (endTime - startTime) / 1_000_000.0;
        System.out.println(ms);
    }

    static void validation() {
        for (int j = 0; j < M; j++) {
            if (result_sequential[0][j] != result_parallel[0][j]) {
                throw new AssertionError("Validation failed at (0," + j + "): "
                        + result_sequential[0][j] + " != " + result_parallel[0][j]);
            }
        }
    }

    public static void main(String[] args) {
        // if (args.length >= 1) P = Integer.parseInt(args[0]);

        P = 16;

        read_matrix("C:\\Users\\Next\\Desktop\\PPD\\Lab1\\C++\\Lab1\\matrix_1000_1000.txt");
        read_kernel("C:/Users/Next/Desktop/PPD/Lab1/C++/Lab1/kernel_5x5.txt");

        int kernelCenter = (K - 1) / 2;

        sequential_execution_rows(kernelCenter);
        sequential_execution_columns(kernelCenter);

        parallel_execution_rows(kernelCenter);
        parallel_execution_columns(kernelCenter);

        validation();

        // write("C:/Users/Next/Desktop/PPD/Lab1/C++/Lab1/result_4_4txt");

    }
}
