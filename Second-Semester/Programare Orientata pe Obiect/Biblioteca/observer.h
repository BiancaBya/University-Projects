#ifndef QT1_OBSERVER_H
#define QT1_OBSERVER_H
#include <algorithm>
#include <vector>
using std::vector;

class Observer{

public:

    virtual void update() = 0;

};


class Observable{

private:

    vector<Observer*> observers_list;


public:

    void addObserver(Observer* observer){
        observers_list.push_back(observer);
    }

    void removeObserver(Observer* observer){
        observers_list.erase(remove(observers_list.begin(), observers_list.end(), observer), observers_list.end());
    }

    void notify(){
        for (auto observer : observers_list){
            observer->update();
        }
    }

};



#endif //QT1_OBSERVER_H




