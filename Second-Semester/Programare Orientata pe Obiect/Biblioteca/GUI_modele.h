#ifndef QT1_GUI_MODELE_H
#define QT1_GUI_MODELE_H
#include <QtWidgets>
#include <qdebug.h>
#include <qmessagebox.h>
#include <vector>
#include <string>
#include "service.h"
#include "sortare.h"
#include "filtrare.h"
#include "cosul.h"
#include "raport.h"
#include "validator.h"
#include "exceptions.h"
#include "CosCRUDGUI.h"
#include "CosReadOnlyGUI.h"
#include "Model.h"

using std::vector;
using std::string;

class GUIModele: public QWidget{


public:

    explicit GUIModele (CarteService& service) : service(service) {
        initGUI();
        loadData();
        initConnect();
    }


private:

    CarteService& service;

    Sortare sortare {service};

    Filtrare filtrare {service};

    Raport raport {service};


    QListView* lista = new QListView;

    QTableView* tabel = new QTableView;

    MyTableModel* model_tabel = new MyTableModel(service.get_all());

    MyListModel* model_lista = new MyListModel(service.get_all());


    Carte carte_selectata;

    QPushButton* btnExit = new QPushButton(" &Exit ");
    QPushButton* btnCos = new QPushButton(" &Cos ");
    QPushButton* btnCosCrud = new QPushButton(" Cos Crud ");
    QPushButton* btnCosRead = new QPushButton (" Cos Read Only ");

    QPushButton* btnAdd = new QPushButton(" &Adauga ");
    QPushButton* btnModify = new QPushButton(" &Modifica ");
    QPushButton* btnDelete = new QPushButton(" &Sterge ");
    QPushButton* btnUndo = new QPushButton(" &Undo ");

    QPushButton* btnCautare = new QPushButton(" &Cautare ");
    QPushButton* btnFiltrare = new QPushButton(" &Filtrare ");
    QPushButton* btnSortare = new QPushButton(" &Sortare ");
    QPushButton* btnRaport = new QPushButton( " &Raport " );

    QPushButton* btnAddCos = new QPushButton(" Adauga in Cos ");
    QPushButton* btnGolireCos = new QPushButton(" Golire Cos ");
    QPushButton* btnRandomCos = new QPushButton(" Random Cos ");

    QLineEdit* txtTitlu = new QLineEdit;
    QLineEdit* txtAutor = new QLineEdit;
    QLineEdit* txtGen = new QLineEdit;
    QLineEdit* txtAn = new QLineEdit;
    QLineEdit* nrCarti = new QLineEdit;

    Cos cos;


    void initGUI() {

        tabel->setModel(model_tabel);

        lista->setModel(model_lista);


        setWindowTitle("Biblioteca");

        auto* lyMain = new QHBoxLayout{};
        setLayout(lyMain);

        // Layout-ul pt tabel

        lyMain->addWidget(tabel);

        // Layout-ul pentru Lista

        auto layout = new QVBoxLayout;

        auto label = new QLabel{ "Lista" };

        auto hlayout = new QHBoxLayout;
        hlayout->addStretch();
        hlayout->addWidget(label);
        hlayout->addStretch();

        layout->addLayout(hlayout);
        layout->addWidget(lista);

        lyMain->addLayout(layout);


        // Layout-ul pentru butoanele de adaugare, stergere si modificare

        auto layout_butoane = new QVBoxLayout;

        layout_butoane->addWidget(btnAdd);
        layout_butoane->addWidget(btnModify);
        layout_butoane->addWidget(btnDelete);
        layout_butoane->addWidget(btnUndo);

        lyMain->addLayout(layout_butoane);


        // Layout-ul pentru campurile de titlu, autor, gen si an

        auto layout_dreapta = new QVBoxLayout;


        auto layout_campuri = new QFormLayout;

        layout_campuri->addRow("Titlu", txtTitlu);
        layout_campuri->addRow("Autor", txtAutor);
        layout_campuri->addRow("Gen", txtGen);
        layout_campuri->addRow("An", txtAn);
        layout_campuri->addRow("Nr Carti Random", nrCarti);

        layout_dreapta->addLayout(layout_campuri);

        auto aux = new QHBoxLayout;

        aux->addWidget(btnCautare);
        aux->addWidget(btnFiltrare);
        aux->addWidget(btnSortare);
        aux->addWidget(btnRaport);

        layout_dreapta->addLayout(aux);

        auto ly_btn_cos = new QHBoxLayout;

        ly_btn_cos->addWidget(btnAddCos);
        ly_btn_cos->addWidget(btnGolireCos);
        ly_btn_cos->addWidget(btnRandomCos);

        layout_dreapta->addLayout(ly_btn_cos);

        layout_dreapta->addWidget(btnCos);
        layout_dreapta->addWidget(btnCosCrud);
        layout_dreapta->addWidget(btnCosRead);
        layout_dreapta->addWidget(btnExit);

        lyMain->addLayout(layout_dreapta);

    }

    void loadData() {

        model_lista->setCarti(service.get_all());
        model_tabel->setCarti(service.get_all());

    }


    void initConnect() {


        QObject::connect(lista->selectionModel(), &QItemSelectionModel::selectionChanged, [this](const QItemSelection& selected) {

            if (!selected.indexes().isEmpty()) {

                QModelIndex index = selected.indexes().first();
                QString text = index.data(Qt::DisplayRole).toString();
                QStringList elemente = text.split(" | ");

                if (elemente.size() == 4) {
                    txtTitlu->setText(elemente[0]);
                    txtAutor->setText(elemente[1]);
                    txtGen->setText(elemente[2]);
                    txtAn->setText(elemente[3]);

                    auto titlu = elemente[0].toStdString();
                    auto autor = elemente[1].toStdString();
                    auto gen = elemente[2].toStdString();
                    auto an = elemente[3].toInt();

                    carte_selectata = Carte(titlu, autor, gen, an);
                }

            }

        });



        QObject::connect(tabel->selectionModel(), &QItemSelectionModel::selectionChanged, [this](const QItemSelection& selected) {

             if (!selected.indexes().isEmpty()){

                 QModelIndex index = selected.indexes().first();
                 int linie = index.row();

                 auto titluIndex = tabel->model()->index(linie,0);
                 auto autorIndex = tabel->model()->index(linie,1);
                 auto genIndex = tabel->model()->index(linie,2);
                 auto anIndex = tabel->model()->index(linie,3);

                 QString titlu = tabel->model()->data(titluIndex, Qt::DisplayRole).toString();
                 QString autor = tabel->model()->data(autorIndex, Qt::DisplayRole).toString();
                 QString gen = tabel->model()->data(genIndex, Qt::DisplayRole).toString();
                 QString an = tabel->model()->data(anIndex, Qt::DisplayRole).toString();

                 txtTitlu->setText(titlu);
                 txtAutor->setText(autor);
                 txtGen->setText(gen);
                 txtAn->setText(an);

                 carte_selectata = Carte(titlu.toStdString(), autor.toStdString(), gen.toStdString(), an.toInt());

             }

        });



        QObject::connect(btnExit, &QPushButton::clicked, [&]() {   // se capteaza prin referinta elementul this ca sa se poata apela metode, ex close
            close();
        });


        QObject::connect(btnAdd, &QPushButton::clicked, [&]() {

            auto titlu = txtTitlu->text();
            auto autor = txtAutor->text();
            auto gen = txtGen->text();
            auto an = txtAn->text();

            auto titlu_carte = titlu.toStdString();
            auto autor_carte = autor.toStdString();
            auto gen_carte = gen.toStdString();
            auto an_carte = an.toInt();

            Carte carte(titlu_carte, autor_carte, gen_carte, an_carte);

            try {
                service.adaugare(carte);
            }catch (Exceptions& exceptie){

                QString mesaj = QString::fromStdString(exceptie.get_message());
                QMessageBox::critical(this, "Eroare", mesaj);
            }

            loadData();

            txtTitlu->clear();
            txtAutor->clear();
            txtGen->clear();
            txtAn->clear();

        });


        QObject::connect(btnModify, &QPushButton::clicked, [&](){

            if (txtTitlu->text().isEmpty() or txtAutor->text().isEmpty() or txtGen->text().isEmpty() or txtAn->text().isEmpty()){
                QMessageBox::critical(this, "Eroare", "Date invalide!");
                return;
            }

            else{

                auto titlu = txtTitlu->text().toStdString();
                auto autor = txtAutor->text().toStdString();
                auto gen = txtGen->text().toStdString();
                auto an = txtAn->text().toInt();

                Carte carte_noua(titlu, autor, gen, an);

                try {
                    service.modificare(carte_selectata, carte_noua);
                }catch (Exceptions& exceptie){

                    QString mesaj = QString::fromStdString(exceptie.get_message());
                    QMessageBox::critical(this, "Eroare", mesaj);
                }

                loadData();

                txtTitlu->clear();
                txtAutor->clear();
                txtGen->clear();
                txtAn->clear();

                carte_selectata = Carte();

            }

        });


        QObject::connect(btnDelete, &QPushButton::clicked, [&](){

            if (txtTitlu->text().isEmpty() or txtAutor->text().isEmpty() or txtGen->text().isEmpty() or txtAn->text().isEmpty()){
                QMessageBox::critical(this, "Eroare", "Date invalide!");
                return;
            }

            else{

                try {
                    service.stergere(carte_selectata);
                }catch (Exceptions& exceptie){

                    QString mesaj = QString::fromStdString(exceptie.get_message());
                    QMessageBox::critical(this, "Eroare", mesaj);
                }

                loadData();

                txtTitlu->clear();
                txtAutor->clear();
                txtGen->clear();
                txtAn->clear();

                carte_selectata = Carte();
            }

        });


        QObject::connect(btnUndo, &QPushButton::clicked, [&](){

            if (service.Undo() == -1){
                QMessageBox::information(this, "Info", "Nu se poate face undo!");
            }
            else{
                loadData();
            }

        });


        QObject::connect(btnCautare, &QPushButton::clicked, [&](){

            if (txtTitlu->text().isEmpty() or txtAutor->text().isEmpty() or txtGen->text().isEmpty() or txtAn->text().isEmpty()){
                QMessageBox::critical(this, "Eroare", "Date invalide!");
                return;
            }

            else{

                auto titlu_cautat = txtTitlu->text().toStdString();
                auto autor_cautat = txtAutor->text().toStdString();
                auto gen_cautat = txtGen->text().toStdString();
                auto an_cautat = txtAn->text().toInt();

                if(service.gasire_carte(titlu_cautat, autor_cautat, gen_cautat, an_cautat)){

                    QString titlu = QString::fromStdString(titlu_cautat);
                    QString autor = QString::fromStdString(autor_cautat);
                    QString gen = QString::fromStdString(gen_cautat);
                    QString an = QString::number(an_cautat);

                    QString mesaj = "Titlu: " + titlu + '\n' + "Autor: " + autor + '\n' + "Gen: " + gen + '\n' + "An: " + an;

                    QMessageBox::information(this, "Carte Gasita", mesaj);
                }
                else{
                    QMessageBox::information(this, "Info", "Cartea nu exista!");
                }

            }

            txtTitlu->clear();
            txtAutor->clear();
            txtGen->clear();
            txtAn->clear();

        });


        QObject::connect(btnSortare, &QPushButton::clicked, [&](){

            sortare.show();

        });


        QObject::connect(btnFiltrare, &QPushButton::clicked, [&](){

            filtrare.show();

        });


        QObject::connect(btnCos, &QPushButton::clicked, [&](){

            auto* cos_main = new Cosul(service, cos);
            cos.addObserver(cos_main);
            cos_main->show();

        });


        QObject::connect(btnCosCrud, &QPushButton::clicked, [&](){

            auto* cos_crud = new CosCrudGUI(service, cos);
            cos.addObserver(cos_crud);
            cos_crud->show();

        });


        QObject::connect(btnCosRead, &QPushButton::clicked, [&](){

            auto* cos_read = new CosReadOnly(service, cos);
            cos.addObserver(cos_read);
            cos_read->show();

        });


        QObject::connect(btnAddCos, &QPushButton::clicked, [&](){

            auto titlu = txtTitlu->text().toStdString();
            auto autor = txtAutor->text().toStdString();
            auto gen = txtGen->text().toStdString();
            auto an = txtAn->text().toInt();

            if(service.gasire_carte(titlu, autor, gen, an)){

                Carte carte(titlu, autor, gen, an);
                cos.adaugare_in_cos(carte);

                vector<Carte> carti = cos.get_cos();

            }

            else{
                QMessageBox::critical(this, "Eroare", "Cartea nu exista!");
            }

            txtTitlu->clear();
            txtAutor->clear();
            txtGen->clear();
            txtAn->clear();


        });


        QObject::connect(btnGolireCos, &QPushButton::clicked, [&](){

            cos.golire_cos();

        });


        QObject::connect(btnRandomCos, &QPushButton::clicked, [&](){

            if (nrCarti->text().isEmpty()){
                QMessageBox::critical(this, "Eroare", "Numarul de carti nu poate fi vid!");
                return;
            }

            cos.golire_cos();
            int nr = nrCarti->text().toInt();
            cos.random_carti(service.get_all(), nr);

            vector<Carte> carti = cos.get_cos();

            nrCarti->clear();

        });


        QObject::connect(btnRaport, &QPushButton::clicked, [&](){

            raport.show();

        });

    }

};



#endif //QT1_GUI_MODELE_H






