#include <cstdint>
#include <vector>
#include <string>
#include <fstream>
#include <iostream>
#include <stdexcept>
#include <iomanip>
#include <chrono>

using Digit = std::uint8_t;

static std::vector<Digit> read(const std::string& path) {
    std::ifstream fin(path);
    if (!fin) {
        throw std::runtime_error("Cannot open file: " + path);
    }

    long long N = -1;
    if (!(fin >> N) || N < 0) {
        throw std::runtime_error("Invalid N value in: " + path);
    }

    std::vector<Digit> a;
    a.reserve(static_cast<size_t>(N));

    for (long long i = 0; i < N; ++i) {
        long long x = -1;
        if (!(fin >> x)) {
            throw std::runtime_error("Missing digits in: " + path);
        }
        if (x < 0 || x > 9) {
            throw std::runtime_error("Digit out of range [0,9] in: " + path);
        }
        a.push_back(static_cast<Digit>(x));
    }

    return a;
}

static void write(const std::string& path, const std::vector<Digit>& a) {
    std::ofstream fout(path);
    if (!fout) {
        throw std::runtime_error("Cannot write to file: " + path);
    }

    fout << a.size() << "\n";
    for (size_t i = 0; i < a.size(); ++i) {
        fout << static_cast<int>(a[i]) << (i + 1 < a.size() ? ' ' : '\n');
    }
}

static void deleteZeros(std::vector<Digit>& a) {
    while (a.size() > 1 && a.back() == 0) {
        a.pop_back();
    }
}

static std::vector<Digit> addNumbers(const std::vector<Digit>& A, const std::vector<Digit>& B) {
    const size_t n = (A.size() > B.size() ? A.size() : B.size());
    std::vector<Digit> C;
    C.reserve(n + 1);

    int carry = 0;
    for (size_t i = 0; i < n; ++i) {
        const int ai = (i < A.size() ? A[i] : 0);
        const int bi = (i < B.size() ? B[i] : 0);
        const int s  = ai + bi + carry;

        C.push_back(static_cast<Digit>(s % 10));
        carry = s / 10;
    }
    if (carry) C.push_back(static_cast<Digit>(carry));

    deleteZeros(C);
    return C;
}

int main() {
    std::ios::sync_with_stdio(false);
    std::cin.tie(nullptr);

    try {
        const std::string in1 = "Random2Numar1.txt";
        const std::string in2 = "Random2Numar2.txt";
        const std::string out = "Random2Numar3.txt";

        const auto t0 = std::chrono::high_resolution_clock::now();
        auto A = read(in1);
        auto B = read(in2);

        auto C = addNumbers(A, B);

        write(out, C);

        const auto t1 = std::chrono::high_resolution_clock::now();
        const double ms = std::chrono::duration<double, std::milli>(t1 - t0).count();

        std::cerr << std::fixed << std::setprecision(3)
                  << "[Sequential] Total time (read + compute + write): "
                  << ms << " ms\n";
    } catch (const std::exception& e) {
        std::cerr << "Error: " << e.what() << '\n';
        return 1;
    }

    return 0;
}
