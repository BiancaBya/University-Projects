#ifndef EX_MODEL_H
#define EX_MODEL_H
#include "masina.h"
#include "QtWidgets"
#include <algorithm>
#include <utility>

class MyTableModel : public QAbstractTableModel{

    std::vector<Masina> masini;

public:

    MyTableModel(const vector<Masina>& m) : masini(m){};

    int rowCount(const QModelIndex & parent = QModelIndex()) const override{
        return masini.size();
    }

    int columnCount(const QModelIndex & parent = QModelIndex()) const override{
        return 4;
    }

    QVariant data(const QModelIndex & index, int role =  Qt::DisplayRole) const override{

        Masina masina = masini[index.row()];

        if (role == Qt::DisplayRole) {

            if (index.column() == 0)
                return QString::fromStdString(masina.get_inmatriculare());
            else if (index.column() == 1)
                return QString::fromStdString(masina.get_denumire());
            else if (index.column() == 2)
                return QString::number(masina.get_an());
            else if (index.column() == 3)
                return QString::fromStdString(masina.get_tip());


        }

        if (role == Qt::BackgroundRole){

            if (masina.get_tip() == "sedan"){
                QBrush b(Qt::darkCyan);
                return b;
            }

            if (masina.get_tip() == "hatch"){
                QBrush b(Qt::darkBlue);
                return b;
            }

            if (masina.get_tip() == "suv"){
                QBrush b(Qt::blue);
                return b;
            }

            if (masina.get_tip() == "van"){
                QBrush b(Qt::red);
                return b;
            }

        }

        return QVariant{};
    }

    QVariant headerData(int section, Qt::Orientation orientation, int role = Qt::DisplayRole) const override {
        if (role != Qt::DisplayRole)
            return QVariant{};

        if (orientation == Qt::Horizontal) {
            switch (section) {
                case 0: return QString("Nrinmatriculare");
                case 1: return QString("Denumire");
                case 2: return QString("An");
                case 3: return QString("Tip");
                default: return QVariant{};
            }
        }
        return QVariant{};
    }

    void setTractoare(vector<Masina>& masinile){

        beginResetModel();
        this->masini = masinile;
        endResetModel();

    }

};

#endif //EX_MODEL_H
