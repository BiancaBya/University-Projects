#include <iostream>
#include <mutex>
#include <thread>
#include <vector>
#include <barrier>

using namespace std;
using namespace std::chrono;
using namespace std::literals::chrono_literals;

class BankAcc {
    private:
        static int global_counter;
        static mutex global_mtx;
        int id;
        int funds;
        mutex mtx;

    public:
        BankAcc(int funds):
            funds(funds),
            mtx ()
            {
                scoped_lock<mutex> lock(global_mtx);
                id = global_counter;
                global_counter += 1;
            }

        int get_funds(){
            // V1
            // mtx.lock()
            // int cpy = funds;
            // mtx.unlock()
            // return cpy

            // V2
            {
                // mtx = blocat
                scoped_lock<mutex> lock(mtx);
                return funds;
            }

            // mtx nu mai e blocat
        }

        bool add_funds(int val){
            unique_lock<mutex> lock(mtx);
            funds += val;
            return true;
        }

        bool remove_funds(int val) {
            unique_lock<mutex> lock(mtx);
            if (funds < val){
                return false;
            }
            funds -= val;
            return true;
        }

        bool transfer_funds(int val, BankAcc& acc){
            //unique_lock<mutex> lock1(this->mtx);

            //unique_lock<mutex> lock1.1(this->mtx); -> deadlock btw

            //this_thread::sleep_for(200ms); // -> deadlock

            // V1
//            if (funds < val){
//                return false;
//            }
//            unique_lock<mutex> lock2(acc.mtx);
//            funds -= val;
//            acc.funds += val;
//            return true;


            // V2
//            this_thread::sleep_for(200ms); // -> deadlock
//            scoped_lock<mutex, mutex> lock (this->mtx, acc.mtx); // solutie pt time_sleep  ca sa le lock pe amandoua deodata
//
//            if (funds < val){
//                return false;
//            }
//            funds -= val;
//            acc.funds += val;
//            return true;

            // V3
//                scoped_lock<mutex, mutex> lock(this->mtx, acc.mtx);
//                if (funds < val){
//                    return false;
//                }
//                funds -= val;
//                acc.funds += val;
//                return true;

            // V4
                if (this->id == acc.id){
                    return true;
                }

                mutex* m1 = &this->mtx;
                mutex* m2 = &acc.mtx;

                if (acc.id < this->id){
                    m1 = &acc.mtx;
                    m2 = &this->mtx;
                }

                unique_lock<mutex> lock1 (*m1);

                if (funds < val){
                    return false;
                }
                funds -= val;
                acc.funds += val;
                return true;

        }
};

int BankAcc::global_counter = 0;
mutex BankAcc::global_mtx = mutex();

int main() {
    vector<thread> thrs;
    BankAcc b1(10);
    BankAcc b2(10);
    barrier barr(3); // asteptam sa se creeze toate threadurile si dupa se executa codul -> ca sa se amestece

    thrs.emplace_back([&](){
        barr.arrive_and_wait();
       b1.add_funds(100);
    });

    thrs.emplace_back([&](){
        barr.arrive_and_wait();
       b1.transfer_funds(100, b2);
    });

    thrs.emplace_back([&](){
        barr.arrive_and_wait();
        b2.transfer_funds(10, b1);
    });

    for (auto& t: thrs){
        t.join();
    }

    cout << "B1: " << b1.get_funds() << endl;
    cout << "B2: " << b2.get_funds() << endl;

    return 0;
}
