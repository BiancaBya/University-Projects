#include <iostream>
#include <fstream>
#include <vector>
#include <thread>
#include <mutex>
#include <queue>
#include <condition_variable>
#include <random>
#include <chrono>
#include <string>
#include <climits>
#include <functional>
#include <atomic>
#include <map>

using namespace std;

constexpr int NUM_STUDENTS = 500;
constexpr int NUM_PROJECTS = 10;

constexpr size_t QUEUE_CAPACITY = 100;

static void generateRandomInput(const vector<string>& files) {
    mt19937 rng(
            (unsigned)chrono::high_resolution_clock::now().time_since_epoch().count()
    );
    uniform_int_distribution<int> idDist(1, NUM_STUDENTS);
    uniform_int_distribution<int> gradeDist(0, 10);
    uniform_int_distribution<int> countDist(80, NUM_STUDENTS);

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

static bool parseLine(const string& line, int& id, int& grade) {
    auto p1 = line.find('(');
    auto p2 = line.find(',', p1);
    auto p3 = line.find(')', p2);
    if (p1 == string::npos || p2 == string::npos || p3 == string::npos) return false;

    try {
        id = stoi(line.substr(p1 + 1, p2 - p1 - 1));
        grade = stoi(line.substr(p2 + 1, p3 - p2 - 1));
    } catch (...) {
        return false;
    }
    return true;
}

struct GradePair {
    int id;
    int grade;
};

class BoundedQueue {
private:
    queue<GradePair> q;
    mutable mutex mtx;
    condition_variable cvNotEmpty; // readerii așteapta daca e goala
    condition_variable cvNotFull; // writerii așteapta daca e plina
    const size_t cap;

public:
    explicit BoundedQueue(size_t capacity) : cap(capacity) {}

    void push(const GradePair& gp) {
        unique_lock<mutex> lock(mtx);
        cvNotFull.wait(lock, [&]() { return q.size() < cap; });
        q.push(gp);
        lock.unlock();
        cvNotEmpty.notify_one(); // notify pe cvNotEmpty (trezeste un worker care așteapta)
    }

    GradePair pop() {
        unique_lock<mutex> lock(mtx);
        cvNotEmpty.wait(lock, [&]() { return !q.empty(); });
        GradePair gp = q.front();
        q.pop();
        lock.unlock();
        cvNotFull.notify_one(); // notify pe cvNotFull (trezeste un producer)
        return gp;
    }
};

// thread pool cu max p_r thread-uri
class ThreadPool {
private:
    vector<thread> workers;
    queue<function<void()>> tasks;
    mutex mtx;
    condition_variable cv;
    bool stop;

public:
    explicit ThreadPool(int threads) : stop(false) {
        workers.reserve(threads);
        for (int i = 0; i < threads; ++i) {
            workers.emplace_back([this]() {
                while (true) {
                    function<void()> task;
                    {
                        unique_lock<mutex> lock(mtx);
                        cv.wait(lock, [&]() { return stop || !tasks.empty(); });
                        if (stop && tasks.empty()) return;
                        task = std::move(tasks.front());
                        tasks.pop();
                    }
                    task();
                }
            });
        }
    }

    void submit(function<void()> f) {
        {
            unique_lock<mutex> lock(mtx);
            tasks.push(std::move(f));
        }
        cv.notify_one();
    }

    void shutdown() {
        {
            unique_lock<mutex> lock(mtx);
            stop = true;
        }
        cv.notify_all();
        for (auto& t : workers) {
            if (t.joinable()) t.join();
        }
    }

    ~ThreadPool() {
        if (!stop) shutdown();
    }
};


struct Node {
    int id;
    int total;
    Node* next;
    mutable mutex mtx;

    Node(int i, int t) : id(i), total(t), next(nullptr) {}
};

class FineGrainSortedList {
private:
    Node* head; // sentinel: (total=INT_MAX, id=INT_MIN)
    Node* tail; // sentinel: (total=INT_MIN, id=INT_MAX)
    static constexpr int ID_LOCKS = 1024;
    mutex idLocks[ID_LOCKS];

    static bool before(int idA, int totalA, int idB, int totalB) {
        if (totalA != totalB) return totalA > totalB;
        return idA < idB;
    }

    void insertSortedUnlocked(int id, int total) {
        Node* pred = head;
        unique_lock<mutex> lockPred(pred->mtx);

        Node* curr = pred->next;
        unique_lock<mutex> lockCurr(curr->mtx);

        while (curr != tail && before(curr->id, curr->total, id, total)) {
            lockPred.unlock();
            pred = curr;
            lockPred = std::move(lockCurr);

            curr = curr->next;
            lockCurr = unique_lock<mutex>(curr->mtx);
        }

        Node* n = new Node(id, total);
        n->next = curr;
        pred->next = n;
    }

    bool removeByIdUnlocked(int id, int& oldTotal) {
        Node* pred = head;
        unique_lock<mutex> lockPred(pred->mtx);

        Node* curr = pred->next;
        unique_lock<mutex> lockCurr(curr->mtx);

        while (curr != tail) {
            if (curr->id == id) {
                pred->next = curr->next;
                oldTotal = curr->total;
                lockCurr.unlock();
                delete curr;
                return true;
            }

            lockPred.unlock();
            pred = curr;
            lockPred = std::move(lockCurr);

            curr = curr->next;
            lockCurr = unique_lock<mutex>(curr->mtx);
        }

        return false;
    }

public:
    FineGrainSortedList() {
        head = new Node(INT_MIN, INT_MAX);
        tail = new Node(INT_MAX, INT_MIN);
        head->next = tail;
    }

    ~FineGrainSortedList() {
        clearAll();
        delete head;
        delete tail;
    }

    void clearAll() {
        Node* curr = head->next;
        while (curr != tail) {
            Node* tmp = curr->next;
            delete curr;
            curr = tmp;
        }
        head->next = tail;
    }

    void addOrUpdate(int id, int grade) {
        unique_lock<mutex> idLock(idLocks[id % ID_LOCKS]);

        int oldTotal = 0;
        bool existed = removeByIdUnlocked(id, oldTotal);

        int newTotal = existed ? (oldTotal + grade) : grade;

        insertSortedUnlocked(id, newTotal);
    }

    void toFile(const string& filename) const {
        ofstream out(filename);
        if (!out.is_open()) {
            cerr << "cannot open file: " << filename << "\n";
            return;
        }

        Node* pred = head;
        unique_lock<mutex> lockPred(pred->mtx);

        Node* curr = pred->next;
        unique_lock<mutex> lockCurr(curr->mtx);

        while (curr != tail) {
            out << curr->id << " " << curr->total << "\n";

            lockPred.unlock();
            pred = curr;
            lockPred = std::move(lockCurr);

            curr = curr->next;
            lockCurr = unique_lock<mutex>(curr->mtx);
        }
    }
};

static double runSequential(const vector<string>& files, const string& outFile) {
    FineGrainSortedList list;

    auto start = chrono::high_resolution_clock::now();

    for (const auto& fname : files) {
        ifstream in(fname);
        if (!in.is_open()) {
            cerr << "cannot open file: " << fname << "\n";
            continue;
        }
        string line;
        while (getline(in, line)) {
            int id, grade;
            if (parseLine(line, id, grade)) {
                list.addOrUpdate(id, grade);
            }
        }
    }

    list.toFile(outFile);

    auto end = chrono::high_resolution_clock::now();
    chrono::duration<double, milli> ms = end - start;
    return ms.count();
}


static double runParallel(const vector<string>& files,
                          int p_r, int p_w,
                          const string& outFile) {

    FineGrainSortedList list;
    BoundedQueue queue(QUEUE_CAPACITY);

    auto start = chrono::high_resolution_clock::now();

    ThreadPool pool(p_r);

    for (const auto& fname : files) {
        pool.submit([&queue, fname]() {
            ifstream in(fname);
            if (!in.is_open()) {
                cerr << "cannot open file (task): " << fname << "\n";
                return;
            }
            string line;
            while (getline(in, line)) {
                int id, grade;
                if (parseLine(line, id, grade)) {
                    queue.push(GradePair{id, grade});
                }
            }
        });
    }

    vector<thread> workers;
    workers.reserve(p_w);

    for (int i = 0; i < p_w; ++i) {
        workers.emplace_back([&]() {
            while (true) {
                GradePair gp = queue.pop();
                if (gp.id == -1) break;
                list.addOrUpdate(gp.id, gp.grade);
            }
        });
    }

    pool.shutdown();

    for (int i = 0; i < p_w; ++i) {
        queue.push(GradePair{-1, 0});
    }

    for (auto& t : workers) {
        if (t.joinable()) t.join();
    }

    list.toFile(outFile);

    auto end = chrono::high_resolution_clock::now();
    chrono::duration<double, milli> ms = end - start;
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
    std::string base = "rezultate_secv_lab5.txt";

    std::vector<std::string> files = {
            "rezultate_pw_2.txt",
            "rezultate_pw_4.txt",
            "rezultate_pw_8.txt",
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
    vector<string> files;
    files.reserve(NUM_PROJECTS);
    for (int i = 1; i <= NUM_PROJECTS; ++i) {
        files.push_back("proiect" + to_string(i) + ".txt");
    }

    generateRandomInput(files);

    const int p_r = 4;
    vector<int> pws = {2, 4, 8};

    double t_secv = runSequential(files, "rezultate_secv_lab5.txt");
    cout << "Timp secvential: " << t_secv << " ms\n";

    for (int p_w : pws) {
        string outName = "rezultate_pw_" + to_string(p_w) + ".txt";
        double t_par = runParallel(files, p_r, p_w, outName);
        double raport = t_secv / t_par;
        cout << "p_r=" << p_r << ", p_w=" << p_w
             << " -> T_par=" << t_par << " ms"
             << ", Tsecv/Tpar = " << raport << "\n";
    }

    runParallel(files, p_r, 8, "rezultate.txt");

    testAllResults();

    return 0;
}
