#ifndef TRACTOR_GUI_H
#define TRACTOR_GUI_H
#include <QtWidgets>
#include "service.h"
#include "model.h"

class GUI : public QWidget{

Q_OBJECT

signals:
    void circleClicked(int index);

public:

    GUI(Service& service) : service(service){
        initGui();
        LoadData();
        initConnect();
    }

private:

    Service& service;

    string tipul = service.get_all()[0].get_tip();

    MyTableModel *model = new MyTableModel(service.get_all(), service.get_tipuri(), tipul);

    QTableView *tabel = new QTableView;

    QLineEdit *txtID = new QLineEdit;
    QLineEdit *txtDenumire = new QLineEdit;
    QLineEdit *txtTip = new QLineEdit;
    QLineEdit *txtNrRoti = new QLineEdit;

    QPushButton *btnAdd = new QPushButton(" Adauga ");

    QComboBox *comboBox = new QComboBox;

    Tractor tractor_selectat;


    void initGui(){

        setFixedSize(1000, 700);

        tabel->setModel(model);

        auto *lyMain = new QHBoxLayout;
        setLayout(lyMain);

        lyMain->addWidget(tabel);

        auto *ly_aux = new QVBoxLayout;

        auto *ly_campuri = new QFormLayout;

        ly_campuri->addRow("ID: ", txtID);
        ly_campuri->addRow("Denumire: ", txtDenumire);
        ly_campuri->addRow("Tip: ", txtTip);
        ly_campuri->addRow("Nr roti: ", txtNrRoti);

        ly_aux->addLayout(ly_campuri);

        comboBox->setEditText(QString::fromStdString(tipul));

        ly_aux->addWidget(comboBox);

        ly_aux->addWidget(btnAdd);

        lyMain->addLayout(ly_aux);


        vector<pair<int,string>> tipuri = service.get_tipuri();
        for (const auto& pereche : tipuri){
            comboBox->addItem(QString::fromStdString(pereche.second));
        }


    }


    void LoadData(){

        model->setTractoare(service.get_all(), service.get_tipuri(),tipul);
        repaint();

    }

    void initConnect(){


        QObject::connect(tabel->selectionModel(), &QItemSelectionModel::selectionChanged, [this](const QItemSelection& selected){

            if (!selected.indexes().isEmpty()){

                QModelIndex index = selected.indexes().first();
                int linie = index.row();

                auto IDindex = tabel->model()->index(linie, 0);
                QString ID = tabel->model()->data(IDindex, Qt::DisplayRole).toString();

                tractor_selectat = *service.find_tractor(ID.toInt());

                txtNrRoti->setText(QString::number(tractor_selectat.get_nrRoti()));

                LoadData();

            }

        });


        QObject::connect(btnAdd, &QPushButton::clicked, [&](){
            // findtext pt combobox
            if (txtTip->text().isEmpty() or txtDenumire->text().isEmpty()){
                QMessageBox::critical(this, "Eroare", "Date invalide!");
            }

            else if (txtNrRoti->text().toInt() % 2 != 0 or txtNrRoti->text().toInt() < 2 or txtNrRoti->text().toInt() > 16){
                QMessageBox::critical(this, "Eroare", "Date invalide!");
            }

            else {

                int ok = 1;
                vector<Tractor> tractoare = service.get_all();
                for (const auto& tractor : tractoare){
                    if (tractor.get_id() == txtID->text().toInt()){
                        QMessageBox::critical(this, "Eroare", "Date invalide!");
                        ok = 0;
                    }
                }

                if (ok == 1){

                    int ID = txtID->text().toInt();
                    string denumire = txtDenumire->text().toStdString();
                    string tip = txtTip->text().toStdString();
                    int nrRoti = txtNrRoti->text().toInt();

                    Tractor tractor(ID, denumire, tip, nrRoti);

                    service.adauga(tractor);

                    LoadData();

                }

            }

        });

        QObject::connect(comboBox, &QComboBox::currentIndexChanged, this, [&](int index){

            tipul = comboBox->currentText().toStdString();
            LoadData();

        });

        QObject::connect(this, &GUI::circleClicked, [&](int index) {

            int roti = txtNrRoti->text().toInt();
            roti -= 2;

            if (roti >= 2) {

                txtNrRoti->setText(QString::number(roti));

                tractor_selectat.set_nrRoti(roti);

                service.modifica(tractor_selectat.get_id(), tractor_selectat);

                LoadData();
            }

        });

    }


    int circleDiameter = 20;
    int margin = 10;

    void paintEvent(QPaintEvent* event) override {

        QPainter painter(this);
        painter.setRenderHint(QPainter::Antialiasing);

        int numberOfCircles = txtNrRoti->text().toInt();

        for (int i = 0; i < numberOfCircles; ++i) {
            QRectF circle = circleRect(i);
            painter.setBrush(Qt::blue);
            painter.drawEllipse(circle);
        }

    }

    void mousePressEvent(QMouseEvent *event) override {
        int numberOfCircles = txtNrRoti->text().toInt();
        for (int i = 0; i < numberOfCircles; ++i) {
            if (circleRect(i).contains(event->pos())) {
                emit circleClicked(i);
                break;
            }
        }
    }

    QRectF circleRect(int index) const {
        int numberOfCircles = txtNrRoti->text().toInt();
        int xStart = width() - circleDiameter - margin;
        int yStart = height() - (numberOfCircles * (circleDiameter + margin));
        int x = xStart;
        int y = yStart + index * (circleDiameter + margin);
        return QRectF(x, y, circleDiameter, circleDiameter);
    }



};


#endif //TRACTOR_GUI_H
