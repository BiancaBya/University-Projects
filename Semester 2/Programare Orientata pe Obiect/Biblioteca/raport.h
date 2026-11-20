#ifndef QT1_RAPORT_H
#define QT1_RAPORT_H

#include <QtWidgets>
#include "service.h"

class Raport : public QWidget {

public:

    explicit Raport(CarteService& service) : service(service){
        initRaport();
    };

private:

    CarteService& service;

    QListWidget* lista = new QListWidget;

    void initRaport(){

        setWindowTitle("Raport autori");

        auto* lyMain = new QHBoxLayout;
        setLayout(lyMain);

        lyMain->addWidget(lista);

        map<string, Autorcount> raport = service.report();

        for(const auto& pereche : raport){

            auto autor = (QString::fromStdString(pereche.first));
            auto nr = (QString::number(pereche.second.get_nr()));
            QString element = "Autorul " + autor + " are un numar de " + nr + " carti";

            lista->addItem(element);

        }

    }

};

#endif //QT1_RAPORT_H


