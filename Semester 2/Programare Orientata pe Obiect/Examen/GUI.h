#ifndef EX_GUI_H
#define EX_GUI_H
#include <QtWidgets>
#include "service.h"
#include "Model.h"

class GUI : public QWidget{

public:

    GUI(Service& service) : service(service){
        initGui();
        LoadData();
        initConnect();
    }

private:

    Service& service;


    MyTableModel *model = new MyTableModel(service.get_all());

    QTableView *tabel = new QTableView;

    QPushButton *btnPlus = new QPushButton(" + ");

    QPushButton *btnMinus = new QPushButton(" - ");

    QPushButton *btnDelete = new QPushButton(" Stergere ");

    QPushButton *btnUndo = new QPushButton(" Undo ");

    QPushButton *btnRedo = new QPushButton(" Redo ");


    Masina masina_selectata;

    vector<Masina> masini;


    void initGui(){

        tabel->setModel(model);

        tabel->setSelectionMode(QAbstractItemView::MultiSelection);

        auto *lyMain = new QHBoxLayout;
        setLayout(lyMain);

        lyMain->addWidget(tabel);

        auto *lyBtn = new QVBoxLayout;

        lyBtn->addWidget(btnPlus);
        lyBtn->addWidget(btnMinus);
        lyBtn->addWidget(btnDelete);
        lyBtn->addWidget(btnUndo);
        lyBtn->addWidget(btnRedo);

        lyMain->addLayout(lyBtn);

    }


    void LoadData(){

        model->setTractoare(service.get_all());

    }


    void initConnect(){

        QObject::connect(tabel->selectionModel(), &QItemSelectionModel::selectionChanged, [this](const QItemSelection& selected){

            if (!selected.indexes().isEmpty()){

                QModelIndex index = selected.indexes().first();
                int linie = index.row();

                auto inmatriculare = tabel->model()->index(linie, 0);
                QString inmatriculare_txt = tabel->model()->data(inmatriculare, Qt::DisplayRole).toString();

                masina_selectata = *service.find_masina(inmatriculare_txt.toStdString());

                masini.push_back(masina_selectata);

                LoadData();

            }

        });


        QObject::connect(btnPlus, &QPushButton::clicked, [&](){

            service.plus();
            LoadData();

        });

        QObject::connect(btnMinus, &QPushButton::clicked, [&](){

            service.minus();
            LoadData();

        });

        QObject::connect(btnDelete, &QPushButton::clicked, [&](){

            for (int i=0; i < masini.size(); i++)
                service.stergere(masini[i].get_inmatriculare());

            LoadData();

            masini.clear();
            masina_selectata = Masina("", "", 0, "");

        });

        QObject::connect(btnUndo, &QPushButton::clicked, [&](){

            if (service.Undo() == -1){
                QMessageBox::information(this, "Info", "Nu se poate face undo!");
            }
            else{
                LoadData();
            }

        });

        QObject::connect(btnRedo, &QPushButton::clicked, [&](){

            if (service.Redo() == -1){
                QMessageBox::information(this, "Info", "Nu se poate face redo!");
            }
            else{
                LoadData();
            }

        });


    }



};


#endif //EX_GUI_H
