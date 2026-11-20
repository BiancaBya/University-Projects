#include <mpi.h>
#include <iostream>
#include <fstream>
#include <vector>
#include <stdexcept>
#include <algorithm>
#include <chrono>
#include <iomanip>
#include <cstdint>

using namespace std;

using Digit = uint8_t;

static bool validate(const string& a, const string& b) {
    ifstream f1(a), f2(b);

    if (!f1 || !f2) {
        cerr << "Nu pot deschide unul dintre fisiere.\n";
        return false;
    }

    string t1, t2;
    size_t k = 0;
    for (;;) {
        bool ok1 = (bool)(f1 >> t1);
        bool ok2 = (bool)(f2 >> t2);
        if (!ok1 && !ok2) {
            cout << "Fisierele sunt identice (token-uri).\n";
            return true;
        }
        if (ok1 != ok2 || t1 != t2) {
            cerr << "Diferenta la token-ul #" << k << "  (" << (ok1 ? t1 : "EOF") << " vs " << (ok2 ? t2 : "EOF") << ")\n";
            return false;
        }
        ++k;
    }
}

struct reader {
    ifstream fin;
    long long N = 0, consumed = 0;
    reader() = default;

    explicit reader(const string& path) {
        open(path);
    }

    void open(const string& path) {
        fin = ifstream(path);
        if (!fin) throw runtime_error("Nu pot deschide " + path);
        fin >> N;
        if (!fin) throw runtime_error("Fisier corupt: lipseste N in " + path);
        consumed = 0;
    }

    long long read_k(vector<Digit>& buf, long long k) {
        buf.resize((size_t)k);
        long long got = 0;

        for (; got < k && consumed < N; ++got, ++consumed) {
            long long v;
            fin >> v;
            if (!fin) throw runtime_error("check failed at file!");
            if (v < 0 || v > 9) throw runtime_error("not digit!");
            buf[(size_t)got] = (Digit)v;
        }

        if (got < k) buf.resize((size_t)got);
        return got;
    }
};

static vector<Digit> readFile(const string& path) {
    ifstream fin(path);

    if (!fin) throw runtime_error("Nu pot deschide " + path);

    long long N;
    fin >> N;
    if (!fin) throw runtime_error("Fisier corupt: lipseste N in " + path);

    vector<Digit> v;
    v.reserve((size_t)N);
    for (long long i = 0; i < N; ++i) {
        long long d;
        fin >> d;
        if (!fin) throw runtime_error("Fisier corupt: lipsesc cifre in " + path);
        if (d < 0 || d > 9) throw runtime_error("Cifra invalida in " + path);

        v.push_back((Digit)d);
    }
    return v;
}

static void writeFile(const string& path, const vector<Digit>& digits) {
    ofstream fout(path);
    if (!fout) throw runtime_error("Nu pot scrie " + path);

    fout << digits.size() << "\n";
    for (size_t i = 0; i < digits.size(); ++i) {
        fout << (int)digits[i] << (i + 1 < digits.size() ? ' ' : '\n');
    }
}

static void run_v1(const string& in1, const string& in2, const string& out) {
    int rank = 0, p = 1;

    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &p);

    const int TAG_LEN = 100, TAG_A = 101, TAG_B = 102, TAG_CARRY = 103, TAG_RLEN = 104, TAG_R = 105;

    auto t0 = chrono::high_resolution_clock::now();

    if (rank == 0) {

        reader r1(in1), r2(in2);
        long long N = max(r1.N, r2.N);

        int workers = p - 1;
        long long base = (workers > 0 ? N / workers : 0);
        long long rem  = (workers > 0 ? N % workers : 0);

        fstream fout(out, ios::in | ios::out | ios::trunc);

        if (!fout) {
            ofstream init(out);
            init.close();
            fout.open(out, ios::in | ios::out);
        }

        if (!fout) {
            cerr << "Nu pot deschide fisierul de iesire " << out << "\n";
            MPI_Abort(MPI_COMM_WORLD, 2);
        }

        const size_t HDR = 64;
        string header(HDR - 1, ' ');

        fout << header << "\n";
        fout.flush();

        for (int pid = 1; pid <= workers; ++pid) {
            long long longSegLength = base + ((pid == workers) ? rem : 0);
            int segLength = (int)longSegLength;

            vector<Digit> A, B;
            r1.read_k(A, longSegLength);
            r2.read_k(B, longSegLength);

            A.resize((size_t)segLength, 0);
            B.resize((size_t)segLength, 0);

            //send length to workers
            MPI_Ssend(&segLength, 1, MPI_INT, pid, TAG_LEN, MPI_COMM_WORLD);

            //send data to workers
            if (segLength > 0) {
                MPI_Ssend(A.data(), segLength, MPI_UNSIGNED_CHAR, pid, TAG_A, MPI_COMM_WORLD);
                MPI_Ssend(B.data(), segLength, MPI_UNSIGNED_CHAR, pid, TAG_B, MPI_COMM_WORLD);
            }
        }

        long long total_digits = 0;

        for (int pid = 1; pid <= workers; ++pid) {
            int rlen = 0;

            // receive lenght
            MPI_Recv(&rlen, 1, MPI_INT, pid, TAG_RLEN, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

            if (rlen > 0) {
                vector<Digit> Rseg((size_t)rlen);

                //receive result
                MPI_Recv(Rseg.data(), rlen, MPI_UNSIGNED_CHAR, pid, TAG_R, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

                for (int i = 0; i < rlen; ++i) {
                    fout << (int)Rseg[(size_t)i] << ' ';
                }

                total_digits += rlen;
            }
        }

        int carry_final = 0;

        if (workers >= 1) {
            // receive carry
            MPI_Recv(&carry_final, 1, MPI_INT, workers, TAG_CARRY, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

            if (carry_final > 0) {
                fout << carry_final << ' ';
                total_digits += 1;
            }
        }

        fout << "\n";
        fout.flush();

        fout.seekp(0, ios::beg);
        fout << total_digits;
        fout.flush();
        fout.close();

        bool ok = validate("third.txt", out);

        if (!ok) {
            cerr << "check failed!\n";
        } else {
            cerr << "OK\n";
        }

        auto t1 = chrono::high_resolution_clock::now();
        cerr << fixed << setprecision(3)
             << "[MPI v1] Total execution time: "
             << chrono::duration<double, milli>(t1 - t0).count() << " ms\n";

    } else {
        int len = 0;
        MPI_Recv(&len, 1, MPI_INT, 0, 100, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
        vector<Digit> A, B, R;

        if (len > 0) {
            A.resize((size_t)len);
            B.resize((size_t)len);

            MPI_Recv(A.data(), len, MPI_UNSIGNED_CHAR, 0, 101, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
            MPI_Recv(B.data(), len, MPI_UNSIGNED_CHAR, 0, 102, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
        }

        int carry_in = 0;
        if (rank > 1)
            MPI_Recv(&carry_in, 1, MPI_INT, rank - 1, 103, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

        R.resize((size_t)len);
        int carry = carry_in;
        for (int i = 0; i < len; ++i) {
            int s = (int)A[(size_t)i] + (int)B[(size_t)i] + carry;
            R[(size_t)i] = (Digit)(s % 10);
            carry = s / 10;
        }

        if (rank < p - 1)
            MPI_Ssend(&carry, 1, MPI_INT, rank + 1, 103, MPI_COMM_WORLD);

        if (rank == p - 1)
            MPI_Ssend(&carry, 1, MPI_INT, 0, 103, MPI_COMM_WORLD);

        int rlen = len;
            MPI_Ssend(&rlen, 1, MPI_INT, 0, 104, MPI_COMM_WORLD);

        if (rlen > 0)
            MPI_Ssend(R.data(), rlen, MPI_UNSIGNED_CHAR, 0, 105, MPI_COMM_WORLD);

    }
}

static void run_v2(const string& in1, const string& in2, const string& out) {
    int rank = 0, p = 1;

    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &p);

    const int TAG_CARRY = 200;

    auto t0 = chrono::high_resolution_clock::now();

    vector<Digit> A_all, B_all;
    long long N_orig = 0, N_pad = 0;

    if (rank == 0) {
        vector<Digit> A = readFile(in1);
        vector<Digit> B = readFile(in2);

        N_orig = (long long)max(A.size(), B.size());

        A.resize((size_t)N_orig, 0);
        B.resize((size_t)N_orig, 0);

        long long r = (p > 0 ? (N_orig % p) : 0);
        N_pad = r ? (N_orig + (p - r)) : N_orig;

        A.resize((size_t)N_pad, 0);
        B.resize((size_t)N_pad, 0);

        A_all.swap(A);
        B_all.swap(B);
    }

    MPI_Bcast(&N_pad, 1, MPI_LONG_LONG, 0, MPI_COMM_WORLD);

    const int chunk = (int)(N_pad / p);

    vector<Digit> A_loc(chunk), B_loc(chunk), R_loc(chunk);

    MPI_Scatter(A_all.data(), chunk, MPI_UNSIGNED_CHAR,
                A_loc.data(), chunk, MPI_UNSIGNED_CHAR, 0, MPI_COMM_WORLD);

    MPI_Scatter(B_all.data(), chunk, MPI_UNSIGNED_CHAR,
                B_loc.data(), chunk, MPI_UNSIGNED_CHAR, 0, MPI_COMM_WORLD);

    int carry_in = 0;

    if (rank == 0) carry_in = 0;
    else MPI_Recv(&carry_in, 1, MPI_INT, rank - 1, TAG_CARRY, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

    int carry = carry_in;

    for (int i = 0; i < chunk; ++i) {
        int s = (int)A_loc[i] + (int)B_loc[i] + carry;
        R_loc[i] = (Digit)(s % 10);
        carry = s / 10;
    }

    int carry_out = carry;

    if (rank < p - 1)
        MPI_Send(&carry_out, 1, MPI_INT, rank + 1, TAG_CARRY, MPI_COMM_WORLD);

    if (rank == p - 1)
        MPI_Send(&carry_out, 1, MPI_INT, 0, TAG_CARRY, MPI_COMM_WORLD);

    vector<Digit> R_all;
    if (rank == 0) R_all.resize((size_t)N_pad);

    MPI_Gather(
            R_loc.data(), chunk, MPI_UNSIGNED_CHAR,       // from each process
            R_all.data(), chunk, MPI_UNSIGNED_CHAR,  // receive buffer
            0, MPI_COMM_WORLD                             // destination
    );

    int carry_final = 0;

    if (rank == 0) {
        MPI_Recv(&carry_final, 1, MPI_INT, p - 1, TAG_CARRY, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

        // resize, remove padding
        if (N_pad > N_orig) {
            while ((long long)R_all.size() > N_orig && R_all.back() == 0)
                R_all.pop_back();
        }

        if (carry_final > 0)
            R_all.push_back((Digit)carry_final);

        if (R_all.empty())
            R_all.push_back(0);

        while (R_all.size() > 1 && R_all.back() == 0) R_all.pop_back();

        writeFile(out, R_all);

        bool ok = validate("third.txt", out);

        if (!ok) {
            cerr << "check failed!\n";
        } else {
            cerr << "ok\n";
        }

        auto t1 = chrono::high_resolution_clock::now();

        cerr << fixed << setprecision(3)
             << "[MPI v2] Total execution time: "
             << chrono::duration<double, milli>(t1 - t0).count() << " ms\n";
    }
}

static void run_v3(const string& in1, const string& in2, const string& out) {
    int rank = 0, p = 1;

    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &p);

    const int TAG_LEN = 300, TAG_A = 301, TAG_B = 302, TAG_R = 303, TAG_CARRY = 304;

    auto t0 = chrono::high_resolution_clock::now();

    if (rank == 0) {

        reader r1(in1), r2(in2);
        long long N = max(r1.N, r2.N);

        int workers = p - 1;
        long long base = N / workers, rem = N % workers;

        vector<long long> offs(workers, 0);

        long long s = 0;

        for (int i = 1; i <= workers; ++i) {
            long long seg = base + ((i == workers) ? rem : 0);
            offs[i - 1] = s;
            s += seg;
        }


        vector<Digit> result((size_t)N + 1, 0);

        vector<MPI_Request> send_reqs;
        send_reqs.reserve(workers * 3 + 1);

        vector<MPI_Request> recv_reqs(workers, MPI_REQUEST_NULL);

        static vector<vector<Digit>> stashA, stashB;

        for (int pid = 1; pid <= workers; ++pid) {
            long long longSegLength = base + ((pid == workers) ? rem : 0);
            int segLength = (int)longSegLength;

            vector<Digit> A(segLength, 0), B(segLength, 0);

            (void)r1.read_k(A, longSegLength);
            (void)r2.read_k(B, longSegLength);

            // Send Length
            MPI_Request rq_len;
            MPI_Isend(&segLength, 1, MPI_INT, pid, TAG_LEN, MPI_COMM_WORLD, &rq_len);
            send_reqs.push_back(rq_len);

            // Keep A and B
            if ((int)stashA.size() < workers) {
                stashA.resize(workers);
                stashB.resize(workers);
            }

            stashA[pid - 1] = std::move(A);
            stashB[pid - 1] = std::move(B);

            // Send A and B
            MPI_Request rq_A, rq_B;
            MPI_Isend(stashA[pid - 1].data(), segLength, MPI_UNSIGNED_CHAR, pid, TAG_A, MPI_COMM_WORLD, &rq_A);
            MPI_Isend(stashB[pid - 1].data(), segLength, MPI_UNSIGNED_CHAR, pid, TAG_B, MPI_COMM_WORLD, &rq_B);

            send_reqs.push_back(rq_A);
            send_reqs.push_back(rq_B);

            // write result
            if (segLength > 0)
                MPI_Irecv(result.data() + offs[pid - 1], segLength, MPI_UNSIGNED_CHAR, pid, TAG_R, MPI_COMM_WORLD, &recv_reqs[pid - 1]);
        }

        // Get final carry from last worker
        int carry_final = 0;
        MPI_Request rq_carry = MPI_REQUEST_NULL;
        MPI_Irecv(&carry_final, 1, MPI_INT, p - 1, TAG_CARRY, MPI_COMM_WORLD, &rq_carry);

        int remaining = workers;

        // wait for carry and workers
        while (remaining > 0) {
            int idx = MPI_UNDEFINED;
            MPI_Waitany(workers, recv_reqs.data(), &idx, MPI_STATUS_IGNORE);

            if (idx != MPI_UNDEFINED) remaining--;
        }

        MPI_Wait(&rq_carry, MPI_STATUS_IGNORE);

        if (!send_reqs.empty())
            MPI_Waitall((int)send_reqs.size(), send_reqs.data(), MPI_STATUSES_IGNORE);

        // create result with final carry
        long long finalN = N;
        if (carry_final > 0) {
            result[(size_t)finalN] = (Digit)carry_final;
            finalN++;
        }

        while (finalN > 1 && result[(size_t)finalN - 1] == 0) finalN--;
        vector<Digit> out_digits(result.begin(), result.begin() + finalN);
        writeFile(out, out_digits);

        bool ok = validate("third.txt", out);

        if (!ok) {
            cerr << "check failed!\n";
        } else {
            cerr << "OK\n";
        }

        auto t1 = chrono::high_resolution_clock::now();
        cerr << fixed << setprecision(3)
             << "[MPI v3] Total execution time: "
             << chrono::duration<double, milli>(t1 - t0).count() << " ms\n";

    } else {
        int len = 0;

        MPI_Request rq_len;
        MPI_Irecv(&len, 1, MPI_INT, 0, TAG_LEN, MPI_COMM_WORLD, &rq_len);

        int carry_in = 0;
        MPI_Request rq_cin = MPI_REQUEST_NULL;

        if (rank > 1)
            MPI_Irecv(&carry_in, 1, MPI_INT, rank - 1, TAG_CARRY, MPI_COMM_WORLD, &rq_cin);

        MPI_Wait(&rq_len, MPI_STATUS_IGNORE);

        vector<Digit> A(len), B(len), R(len);

        MPI_Request rq_A, rq_B;

        if (len > 0) {
            MPI_Irecv(A.data(), len, MPI_UNSIGNED_CHAR, 0, TAG_A, MPI_COMM_WORLD, &rq_A);
            MPI_Irecv(B.data(), len, MPI_UNSIGNED_CHAR, 0, TAG_B, MPI_COMM_WORLD, &rq_B);

            MPI_Wait(&rq_A, MPI_STATUS_IGNORE);
            MPI_Wait(&rq_B, MPI_STATUS_IGNORE);
        }

        if (rank > 1)
            MPI_Wait(&rq_cin, MPI_STATUS_IGNORE);

        int carry = carry_in;

        for (int i = 0; i < len; ++i) {
            int s = (int)A[i] + (int)B[i] + carry;
            R[i] = (Digit)(s % 10);
            carry = s / 10;
        }

        int carry_out = carry;
        MPI_Request rq_cout;

        if (rank < p - 1)
            MPI_Isend(&carry_out, 1, MPI_INT, rank + 1, TAG_CARRY, MPI_COMM_WORLD, &rq_cout);
        else
            MPI_Isend(&carry_out, 1, MPI_INT, 0, TAG_CARRY, MPI_COMM_WORLD, &rq_cout);

        MPI_Request rq_r;

        if (len > 0)
            MPI_Isend(R.data(), len, MPI_UNSIGNED_CHAR, 0, TAG_R, MPI_COMM_WORLD, &rq_r);
        else
            rq_r = MPI_REQUEST_NULL;

        if (rq_r != MPI_REQUEST_NULL)
            MPI_Wait(&rq_r, MPI_STATUS_IGNORE);

        MPI_Wait(&rq_cout, MPI_STATUS_IGNORE);
    }
}

int main(int argc, char** argv) {
    MPI_Init(&argc, &argv);

    int rank = 0;
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);

    string mode = "v1";
    string in1  = "Numar1.txt";
    string in2  = "Numar2.txt";
    string out  = "Numar3.txt";

    if (argc >= 2) mode = argv[1];

    if (argc >= 4) {
        in1 = argv[2];
        in2 = argv[3];
    }

    if (argc >= 5) {
        out = argv[4];
    }

    if (mode == "v1")       run_v1(in1, in2, out);
    else if (mode == "v2")  run_v2(in1, in2, out);
    else if (mode == "v3")  run_v3(in1, in2, out);
    else if (rank == 0)     cerr << "Invalid mode. Use v1 | v2 | v3\n";

    MPI_Finalize();
    return 0;
}
