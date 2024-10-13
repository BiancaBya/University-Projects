#ifndef TRACTOR_MODEL_H
#define TRACTOR_MODEL_H
#include "tractor.h"
#include "QtWidgets"
#include <algorithm>
#include <utility>

class MyTableModel : public QAbstractTableModel{

    std::vector<Tractor> tractoare;
    std::vector<pair<int, string>> nr_tipuri;
    string tip;

public:

    MyTableModel(const vector<Tractor>& tractoare, const vector<pair<int, string>>& tipuri, string t) : tractoare(tractoare), nr_tipuri(tipuri), tip(t){};

    int rowCount(const QModelIndex & parent = QModelIndex()) const override{
        return tractoare.size();
    }

    int columnCount(const QModelIndex & parent = QModelIndex()) const override{
        return 5;
    }

    QVariant data(const QModelIndex & index, int role =  Qt::DisplayRole) const override{

        Tractor tractor = tractoare[index.row()];

        if (role == Qt::DisplayRole) {

            if (index.column() == 0)
                return QString::number(tractor.get_id());
            else if (index.column() == 1)
                return QString::fromStdString(tractor.get_denumire());
            else if (index.column() == 2)
                return QString::fromStdString(tractor.get_tip());
            else if (index.column() == 3)
                return QString::number(tractor.get_nrRoti());
            else if (index.column() == 4){
                for (auto pereche : nr_tipuri)
                    if(pereche.second == tractor.get_tip())
                        return QString::number(pereche.first);
            }

        }

        if (role == Qt::BackgroundRole){

            if (tip == tractor.get_tip()){
                QBrush b(Qt::darkCyan);
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
                case 0: return QString("ID");
                case 1: return QString("Denumire");
                case 2: return QString("Tip");
                case 3: return QString("NrRoti");
                case 4: return QString("Nr tractoare");
                default: return QVariant{};
            }
        }
        return QVariant{};
    }

    void setTractoare(vector<Tractor>& tractoarele, vector<pair<int,string>> tipuri ,string tip_model){

        beginResetModel();

        this->tractoare = tractoarele;
        this->tip = tip_model;
        this->nr_tipuri = tipuri;

        endResetModel();

    }

};


#endif //TRACTOR_MODEL_H
