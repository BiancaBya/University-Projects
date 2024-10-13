#ifndef QT1_SORTARE_H
#define QT1_SORTARE_H

#include <QtWidgets>
#include "service.h"

class Sortare : public QWidget{

public:

    explicit Sortare(CarteService& service): service(service){
        initSortare();
        initConnect();
    };

private:

    CarteService& service;

    QListWidget* lista = new QListWidget;

    QPushButton* btnTitlu = new QPushButton(" Sortare &Titlu ");
    QPushButton* btnAutor = new QPushButton(" Sortare &Autor ");
    QPushButton* btnAn = new QPushButton(" Sortare &An ");

    void initSortare(){

        setWindowTitle("Sortare Carti");

        auto* lyMain = new QHBoxLayout{};
        setLayout(lyMain);

        auto* lyButoane = new QVBoxLayout{};

        lyButoane->addWidget(btnTitlu);
        lyButoane->addWidget(btnAutor);
        lyButoane->addWidget(btnAn);

        lyMain->addWidget(lista);
        lyMain->addLayout(lyButoane);

    }

    void loadLista(const vector<Carte>& carti){

        lista->clear();
        QString titlu, autor, gen, an;

        for (const auto& carte : carti){

            QString element;

            titlu = (QString::fromStdString(carte.get_titlu()));
            autor = (QString::fromStdString(carte.get_autor()));
            gen = (QString::fromStdString(carte.get_gen()));
            an = (QString::number(carte.get_an()));

            element = titlu + " | " + autor + " | " + gen + " | " + an;
            lista->addItem(element);
        }

    }

    void initConnect(){

        QObject::connect(btnTitlu, &QPushButton::clicked, [&](){

            vector<Carte> copie;
            int lungime = 0;

            service.sortare(CarteService::cmp_titlu, copie, lungime);
            loadLista(copie);

        });

        QObject::connect(btnAutor, &QPushButton::clicked, [&](){

            vector<Carte> copie;
            int lungime = 0;

            service.sortare(CarteService::cmp_autor, copie, lungime);
            loadLista(copie);

        });

        QObject::connect(btnAn, &QPushButton::clicked, [&](){

            vector<Carte> copie;
            int lungime = 0;

            service.sortare(CarteService::cmp_an, copie, lungime);
            loadLista(copie);

        });

    }

};

#endif //QT1_SORTARE_H

