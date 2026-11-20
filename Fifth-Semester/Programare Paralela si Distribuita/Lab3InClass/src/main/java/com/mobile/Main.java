package com.mobile;

import java.util.Arrays;
import java.util.Random;

public class Main {

    private static final int N = 10000000;
    private static final int P = 3;
    private static int[] a = new int[N];
    private static int[] b = new int[N];
    private static int[] c = new int[N];
    private static int[] c_secv = new int[N];
    private static final Random rand = new Random();



    private static void init_rand(int[] arr){
        for (int i = 0; i < arr.length; i++){
            arr[i] = rand.nextInt(500);
        }
    }
    private static void print_arr(int[] arr, int len){
        for (int i = 0; i < len && i < arr.length; i++){
            System.out.printf("%5d ", arr[i]);
        }
        System.out.println();
    }

    private static void sum_secv(int[] a, int[] b, int[] c){
        for(int i = 0; i < a.length; i++){
            c[i] = a[i] + b[i];
            c[i] = (int)Math.sqrt(Math.pow(a[i], 3.4) - Math.pow(b[i], 0.3));
        }
    }

    public static void main(String[] args) {

        init_rand(a);
        init_rand(b);

        long before_sum_secv = System.nanoTime();

        sum_secv(a, b, c_secv);

        long after_sum_secv = System.nanoTime();

        MyThread[] threads = new MyThread[P];
        int dim = N/P;
        int rest = N%P;
        int start = 0;
        int end = dim;

        long before_sum_part = System.nanoTime();

        for (int i = 0; i < P; i++){
            if (rest > 0){
                end++;
                rest--;
            }
            threads[i] = new MyThread(a, b, c, start, end);
            threads[i].start();

            start = end;
            end += dim;
        }
        try {
            for (MyThread thread : threads){
                thread.join();
            }
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        long after_sum_part = System.nanoTime();


        if (Arrays.equals(c, c_secv)){
            System.out.println("OK!");
        }
        else {
            System.out.println("ERROR!");
        }

        print_arr(a, 5);
        print_arr(b, 5);
        print_arr(c, 5);
        print_arr(c_secv, 5);

        System.out.println();
        long delta_secv = after_sum_secv - before_sum_secv;
        long delta_part = after_sum_part - before_sum_part;

        System.out.printf("Secv time: %10dns\n ", delta_secv);
        System.out.printf("Secv par: %10dns\n ", delta_part);

        double ns_in_a_ms = 1e6;
        double delta_secv_ms = delta_secv / ns_in_a_ms;
        double delta_part_ms = delta_part / ns_in_a_ms;

        System.out.printf("Secv time: %10.3fns\n ", delta_secv_ms);
        System.out.printf("Secv par: %10.3fns\n ", delta_part_ms);

    }
}



