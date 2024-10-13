#ifndef QT1_FILTRARE_H
#define QT1_FILTRARE_H

#include <QtWidgets>
#include "service.h"

class Filtrare : public QWidget{

public:

    explicit Filtrare(CarteService& service):service(service){
        initFiltrare();
        initConnect();
    };

private:

    CarteService& service;

    QListWidget* lista = new QListWidget;

    QPushButton* btnTitlu = new QPushButton(" Filtrare &Titlu ");
    QPushButton* btnAn = new QPushButton(" Filtrare &An ");

    QLineEdit* txtTitlu = new QLineEdit;
    QLineEdit* txtAn = new QLineEdit;


    void initFiltrare(){

        setWindowTitle("Filtrare Carti");

        auto* lyMain = new QHBoxLayout;
        setLayout(lyMain);

        auto* lyButoane = new QHBoxLayout;

        lyButoane->addWidget(btnTitlu);
        lyButoane->addWidget(btnAn);

        auto* lyCampuri = new QFormLayout;

        lyCampuri->addRow("Titlu: ", txtTitlu);
        lyCampuri->addRow("An: ", txtAn);

        auto* lyDreapta = new QVBoxLayout;

        lyDreapta->addLayout(lyCampuri);
        lyDreapta->addLayout(lyButoane);

        lyMain->addWidget(lista);
        lyMain->addLayout(lyDreapta);

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

            if(txtTitlu->text().isEmpty()){
                QMessageBox::critical(this, "Eroare", "Titlul nu poate fi vid!");
                return;
            }

            vector<Carte> copie;
            int lungime = 0;

            string titlu = txtTitlu->text().toStdString();
            service.filtrare_titlu(titlu,copie,lungime);

            loadLista(copie);

            txtTitlu->clear();
            txtAn->clear();

        });

        QObject::connect(btnAn, &QPushButton::clicked, [&](){

            if(txtAn->text().isEmpty()){
                QMessageBox::critical(this, "Eroare", "Anul nu poate fi vid!");
                return;
            }

            vector<Carte> copie;
            int lungime = 0;

            int an = txtAn->text().toInt();
            service.filtrare_an(an,copie,lungime);

            loadLista(copie);

            txtAn->clear();
            txtTitlu->clear();

        });

    }

};


#endif //QT1_FILTRARE_H


