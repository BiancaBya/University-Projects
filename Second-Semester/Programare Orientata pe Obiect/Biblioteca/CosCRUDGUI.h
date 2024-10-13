#ifndef QT1_COSCRUDGUI_H
#define QT1_COSCRUDGUI_H
#include <QtWidgets>
#include "service.h"
#include "cos.h"
#include "observer.h"

class CosCrudGUI : public QWidget, public Observer{

public:

    CosCrudGUI(CarteService& service, Cos& cos) : service(service), cos(cos){
        initGUI();
        initConnect();
        loadData();
        cos.addObserver(this);
    };


private:

    CarteService& service;

    Cos& cos;

    QListWidget* lista = new QListWidget;

    QPushButton* btnAdd = new QPushButton(" &Adauga ");
    QPushButton* btnGoleste = new QPushButton(" &Golire ");
    QPushButton* btnRandom = new QPushButton(" Generare &Random ");
    QPushButton* btnExport = new QPushButton(" &Export HTML ");

    QLineEdit* txtTitlu = new QLineEdit;
    QLineEdit* txtAutor = new QLineEdit;
    QLineEdit* txtGen = new QLineEdit;
    QLineEdit* txtAn = new QLineEdit;
    QLineEdit* nrCarti = new QLineEdit;


    void initGUI(){

        setWindowTitle("Cos Carti CRUD");

        auto* lyMain = new QHBoxLayout;
        setLayout(lyMain);

        auto* lyButoane = new QHBoxLayout;

        lyButoane->addWidget(btnAdd);
        lyButoane->addWidget(btnGoleste);
        lyButoane->addWidget(btnRandom);

        auto layout_campuri = new QFormLayout;

        layout_campuri->addRow("Titlu", txtTitlu);
        layout_campuri->addRow("Autor", txtAutor);
        layout_campuri->addRow("Gen", txtGen);
        layout_campuri->addRow("An", txtAn);
        layout_campuri->addRow("Nr carti Random: ", nrCarti);

        auto* lyDreapta = new QVBoxLayout;

        lyDreapta->addLayout(layout_campuri);
        lyDreapta->addLayout(lyButoane);
        lyDreapta->addWidget(btnExport);

        lyMain->addWidget(lista);
        lyMain->addLayout(lyDreapta);

    }


    void loadData(){

        vector<Carte> carti = cos.get_cos();

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

        QObject::connect(btnAdd, &QPushButton::clicked, [&](){

            auto titlu = txtTitlu->text().toStdString();
            auto autor = txtAutor->text().toStdString();
            auto gen = txtGen->text().toStdString();
            auto an = txtAn->text().toInt();

            if(service.gasire_carte(titlu, autor, gen, an)){

                Carte carte(titlu, autor, gen, an);
                cos.adaugare_in_cos(carte);

                vector<Carte> carti = cos.get_cos();
                loadData();

            }

            else{
                QMessageBox::critical(this, "Eroare", "Cartea nu exista!");
            }

            txtTitlu->clear();
            txtAutor->clear();
            txtGen->clear();
            txtAn->clear();

        });

        QObject::connect(btnGoleste, &QPushButton::clicked, [&](){

            cos.golire_cos();
            lista->clear();

        });

        QObject::connect(btnRandom, &QPushButton::clicked, [&](){

            if (nrCarti->text().isEmpty()){
                QMessageBox::critical(this, "Eroare", "Numarul de carti nu poate fi vid!");
                return;
            }

            cos.golire_cos();
            int nr = nrCarti->text().toInt();
            cos.random_carti(service.get_all(), nr);

            vector<Carte> carti = cos.get_cos();
            loadData();

            nrCarti->clear();

        });

        QObject::connect(btnExport, &QPushButton::clicked, [&](){

            string file_name = "C:/Users/Next/CLionProjects/qt1/exportat.html";
            cos.export_html(file_name);

            system(("start " + file_name).c_str());

        });

    }

    void update() override{
        loadData();
    }


};


#endif //QT1_COSCRUDGUI_H


