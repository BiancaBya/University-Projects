#ifndef MELODII1_GUI_H
#define MELODII1_GUI_H
#include <QtWidgets>
#include "service.h"
#include "tabelview.h"

class GUI : public QWidget{

public:

    explicit GUI(Service& service) : service{service}{
        initGUI();
        loadData();
        initConnect();
    };

private:

    Service& service;

    QTableView* tabel = new QTableView;

    MyTableModel* model = new MyTableModel(service.get_all(), service.get_ranks());

    QLineEdit* txtTitlu = new QLineEdit;

    QSlider* slider = new QSlider(Qt::Horizontal);
    QLabel* label_slider = new QLabel("Valoare slider: 0");

    Melodie melodie_selectata;

    QPushButton* btnUpdate = new QPushButton(" Update ");
    QPushButton* btnDelete = new QPushButton(" Stergere ");


    void initGUI(){

        tabel->setModel(model);

        auto* lyMain = new QHBoxLayout{};
        setLayout(lyMain);

        lyMain->addWidget(tabel);


        slider->setRange(0,10);
        slider->setValue(0);

        auto *ly2 = new QFormLayout;
        ly2->addRow("Titlu selectat: ", txtTitlu);

        auto *ly1 = new QVBoxLayout;

        ly1->addLayout(ly2);
        ly1->addWidget(btnUpdate);
        ly1->addWidget(btnDelete);
        ly1->addWidget(label_slider);
        ly1->addWidget(slider);

        lyMain->addLayout(ly1);

    }

    void loadData(){

        model->setMelodii(service.get_all());
        repaint();

    }

    void initConnect(){

        QObject::connect(tabel->selectionModel(), &QItemSelectionModel::selectionChanged, [this](const QItemSelection& selected){

            if (!selected.indexes().isEmpty()){

                QModelIndex index = selected.indexes().first();
                int linie = index.row();

                auto IDindex = tabel->model()->index(linie, 0);
                QString ID = tabel->model()->data(IDindex, Qt::DisplayRole).toString();

                melodie_selectata = *service.find_melodie(ID.toInt());

                txtTitlu->setText(QString::fromStdString(melodie_selectata.get_titlu()));
                slider->setValue(melodie_selectata.get_rank());

            }

        });

        connect (slider, &QSlider::valueChanged, this, [&](int value){

            label_slider->setText("Valoare Slider: " + QString::number(value));

        });

        connect(btnUpdate, &QPushButton::clicked, [&](){

            if (txtTitlu->text().isEmpty())
                QMessageBox::critical(this, "Eroare", "Date invalide!");

            melodie_selectata.set_titlu(txtTitlu->text().toStdString());
            melodie_selectata.set_rank(slider->value());

            vector<Melodie> melodii = service.get_all();
            for (auto& melodie : melodii)
                if (melodie.get_id() == melodie_selectata.get_id())
                    melodie = melodie_selectata;

            service.actualizare_melodii(melodii);

            loadData();

        });

        connect(btnDelete, &QPushButton::clicked, [&](){

            if (txtTitlu->text().isEmpty())
                QMessageBox::critical(this, "Eroare", "Date invalide!");

            int nr = 0;
            for (auto& melodie : service.get_all()){
                if (melodie.get_artist() == melodie_selectata.get_artist())
                    nr++;
            }

            if (nr == 1)
                QMessageBox::information(this, "Eroare", "Este ultima carte!");

            else
                service.sterge(melodie_selectata.get_id());

            loadData();

        });


    }

    void paintEvent(QPaintEvent* event) override {

        QPainter painter(this);

        QPainterPath path;
        int index = 10;
        for (const auto& rank : service.get_ranks()){

            path.addRect(QRect(index, 1, 20, 50*rank));
            painter.fillPath(path, Qt::white);
            painter.drawPath(path);
            index += 30;

        }

    }


};



#endif //MELODII1_GUI_H
