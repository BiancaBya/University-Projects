#include <iostream>
#include <fstream>
#include <vector>
#include <thread>
#include <mutex>
#include <queue>
#include <atomic>
#include <random>
#include <chrono>
#include <string>
#include <semaphore>
#include <climits>
#include <map>

using namespace std;

constexpr int NUM_STUDENTS = 200;
constexpr int NUM_PROJECTS = 10;

struct Node {
    int id;
    int total;
    Node* next;
    Node(int i, int t) : id(i), total(t), next(nullptr) {}
};

class ThreadSafeList {
private:
    Node* head;
    mutable std::mutex mtx;

public:
    ThreadSafeList() : head(nullptr) {}

    ~ThreadSafeList() {
        clear();
    }

    void clear() {
        std::unique_lock<std::mutex> lock(mtx);
        Node* curr = head;
        while (curr) {
            Node* tmp = curr->next;
            delete curr;
            curr = tmp;
        }
        head = nullptr;
    }

    // add grade for id or create new node
    void addOrUpdate(int id, int grade) {
        std::unique_lock<std::mutex> lock(mtx);
        Node* curr = head;
        while (curr) {
            if (curr->id == id) {
                curr->total += grade;
                return;
            }
            curr = curr->next;
        }

        Node* n = new Node(id, grade);
        n->next = head;
        head = n;
    }

    void toFile(const std::string& filename) const {
        std::unique_lock<std::mutex> lock(mtx);
        ofstream out(filename);
        if (!out.is_open()) {
            cerr << "cannot open file: " << filename << "\n";
            return;
        }

        Node* curr = head;
        while (curr) {
            out << curr->id << " " << curr->total << "\n";
            curr = curr->next;
        }
    }
};


struct GradePair {
    int id;
    int grade;
};

class ThreadSafeQueue {
private:
    std::queue<GradePair> q;
    mutable std::mutex mtx;
    std::counting_semaphore<INT_MAX> items{0};

public:
    void push(const GradePair& gp) {
        std::unique_lock<std::mutex> lock(mtx);
        q.push(gp);
        lock.unlock();
        items.release(); // the reader increases the number of items in the queue
    }

    void pop(GradePair& out) {
        items.acquire(); //if there are no elements, a worker waits until one appears
        std::unique_lock<std::mutex> lock(mtx);
        out = q.front();
        q.pop();
    }
};


void generateRandomInput(const std::vector<std::string>& files) {
    std::mt19937 rng(
            (unsigned)std::chrono::high_resolution_clock::now().time_since_epoch().count()
    );
    std::uniform_int_distribution<int> idDist(1, NUM_STUDENTS);
    std::uniform_int_distribution<int> gradeDist(0, 10);
    std::uniform_int_distribution<int> countDist(80, NUM_STUDENTS);

    for (const auto& fname : files) {
        ofstream out(fname);
        if (!out.is_open()) {
            cerr << "cannot create file: " << fname << "\n";
            continue;
        }

        int cnt = countDist(rng);
        for (int i = 0; i < cnt; ++i) {
            int id = idDist(rng);
            int grade = gradeDist(rng);
            out << "(" << id << ", " << grade << ")\n";
        }
    }
}


bool parseLine(const std::string& line, int& id, int& grade) {
    auto p1 = line.find('(');
    auto p2 = line.find(',', p1);
    auto p3 = line.find(')', p2);
    if (p1 == std::string::npos || p2 == std::string::npos || p3 == std::string::npos)
        return false;

    try {
        id = std::stoi(line.substr(p1 + 1, p2 - p1 - 1));
        grade = std::stoi(line.substr(p2 + 1, p3 - p2 - 1));
    } catch (...) {
        return false;
    }
    return true;
}


double runSequential(const std::vector<std::string>& files, const std::string& outFile) {
    ThreadSafeList list;

    auto start = std::chrono::high_resolution_clock::now();

    for (const auto& fname : files) {
        ifstream in(fname);
        if (!in.is_open()) {
            cerr << "cannot open file: " << fname << "\n";
            continue;
        }
        std::string line;
        while (std::getline(in, line)) {
            int id, grade;
            if (parseLine(line, id, grade)) {
                list.addOrUpdate(id, grade);
            }
        }
    }

    // write results
    list.toFile(outFile);

    auto end = std::chrono::high_resolution_clock::now();
    std::chrono::duration<double, std::milli> ms = end - start;
    return ms.count();
}



double runParallel(const std::vector<std::string>& files,
                   int p, int p_r,
                   const std::string& outFile) {
    if (p_r <= 0 || p_r > p) {
        throw std::runtime_error("p_r invalid");
    }
    int p_w = p - p_r;

    ThreadSafeList list;
    ThreadSafeQueue queue;

    auto start = std::chrono::high_resolution_clock::now();

    std::vector<std::thread> readerThreads;
    std::vector<std::thread> workerThreads;

    auto readerFunc = [&](int index) {
        for (size_t i = index; i < files.size(); i += p_r) {
            ifstream in(files[i]);
            if (!in.is_open()) {
                cerr << "cannot open file (reader): " << files[i] << "\n";
                continue;
            }
            std::string line;
            while (std::getline(in, line)) {
                int id, grade;
                if (parseLine(line, id, grade)) {
                    queue.push(GradePair{id, grade});
                }
            }
        }
    };

    auto workerFunc = [&]() {
        while (true) {
            GradePair gp;
            queue.pop(gp);
            if (gp.id == -1) {
                break;
            }
            list.addOrUpdate(gp.id, gp.grade);
        }
    };

    // reader threads
    for (int i = 0; i < p_r; ++i) {
        readerThreads.emplace_back(readerFunc, i);
    }
    // worker threads
    for (int i = 0; i < p_w; ++i) {
        workerThreads.emplace_back(workerFunc);
    }

    for (auto& t : readerThreads) {
        if (t.joinable()) t.join();
    }

    // the pairs that will stop the workers after they processed all the pairs read by the readers
    for (int i = 0; i < p_w; ++i) {
        queue.push(GradePair{-1, 0});
    }

    for (auto& t : workerThreads) {
        if (t.joinable()) t.join();
    }

    // main-thread writes result
    list.toFile(outFile);

    auto end = std::chrono::high_resolution_clock::now();
    std::chrono::duration<double, std::milli> ms = end - start;
    return ms.count();
}

bool loadResults(const std::string& filename, std::map<int, int>& resultMap) {
    ifstream in(filename);
    if (!in.is_open()) {
        cerr << "Cannot open " << filename << "\n";
        return false;
    }

    int id, total;
    while (in >> id >> total) {
        resultMap[id] = total;
    }
    return true;
}

bool compareFiles(const std::string& f1, const std::string& f2) {
    std::map<int, int> A, B;

    if (!loadResults(f1, A)) return false;
    if (!loadResults(f2, B)) return false;

    return A == B;
}

void testAllResults() {
    std::string base = "rezultate_secv.txt";

    std::vector<std::string> files = {
            "rezultate_p_4_pr_1.txt",
            "rezultate_p_4_pr_2.txt",
            "rezultate_p_8_pr_1.txt",
            "rezultate_p_8_pr_2.txt",
            "rezultate_p_16_pr_1.txt",
            "rezultate_p_16_pr_2.txt"
    };

    for (auto& f : files) {
        bool ok = compareFiles(base, f);
        if (ok)
            cout << "[OK]";
        else
            cout << "[ERROR]";
    }
}



int main() {
    std::vector<std::string> files;
    files.reserve(NUM_PROJECTS);
    for (int i = 1; i <= NUM_PROJECTS; ++i) {
        files.push_back("proiect" + std::to_string(i) + ".txt");
    }

    generateRandomInput(files);

    double t_secv = runSequential(files, "rezultate_secv.txt");
    std::cout << "Timp secvential: " << t_secv << " ms\n";

    std::vector<std::pair<int, int>> configs = {
            {4, 1}, {8, 1}, {16, 1},
            {4, 2}, {8, 2}, {16, 2}
    };

    for (auto cfg : configs) {
        int p = cfg.first;
        int p_r = cfg.second;
        std::string outName = "rezultate_p_" + std::to_string(p) +
                              "_pr_" + std::to_string(p_r) + ".txt";

        double t_par = runParallel(files, p, p_r, outName);
        double raport = t_secv / t_par;

        std::cout << "p=" << p << ", p_r=" << p_r
                  << " -> T_par=" << t_par << " ms"
                  << ", Tsecv/Tpar = " << raport << "\n";
    }

    testAllResults();

    return 0;
}


