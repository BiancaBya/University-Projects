#ifndef MELODII1_TABELVIEW_H
#define MELODII1_TABELVIEW_H
#include "melodie.h"
#include "QtWidgets"
#include <algorithm>

class MyTableModel : public QAbstractTableModel{

    std::vector<Melodie> melodii;
    std::vector<int> ranks;

public:

    MyTableModel(const vector<Melodie>& carti, const vector<int>& ranks) : melodii(carti), ranks(ranks){};

    int rowCount(const QModelIndex & parent = QModelIndex()) const override{
        return melodii.size();
    }

    int columnCount(const QModelIndex & parent = QModelIndex()) const override{
        return 5;
    }

    QVariant data(const QModelIndex & index, int role =  Qt::DisplayRole) const override{

        Melodie melodie = melodii[index.row()];

        if (role == Qt::DisplayRole) {

            if (index.column() == 0)
                return QString::number(melodie.get_id());
            else if (index.column() == 1)
                return QString::fromStdString(melodie.get_titlu());
            else if (index.column() == 2)
                return QString::fromStdString(melodie.get_artist());
            else if (index.column() == 3)
                return QString::number(melodie.get_rank());
            else if (index.column() == 4)
                return QString::number(ranks[melodie.get_rank()]);

        }

        return QVariant{};
    }

    QVariant headerData(int section, Qt::Orientation orientation, int role = Qt::DisplayRole) const override {
        if (role != Qt::DisplayRole)
            return QVariant{};

        if (orientation == Qt::Horizontal) {
            switch (section) {
                case 0: return QString("ID");
                case 1: return QString("Titlu");
                case 2: return QString("Artist");
                case 3: return QString("Rank");
                case 4: return QString("Nr melodii");
                default: return QVariant{};
            }
        }
        return QVariant{};
    }

    void setMelodii(vector<Melodie> melodiile){

        beginResetModel();

        for (int i=0; i < melodiile.size() -1; i++)
            for (int j=i+1; j < melodiile.size(); j++)
                if (melodiile[i].get_rank() > melodiile[j].get_rank()) {
                    Melodie aux;
                    aux = melodiile[i];
                    melodiile[i] = melodiile[j];
                    melodiile[j] = aux;
                }

        vector <int> rank;

        for (int i=0; i <= 10; i++)
            rank.push_back(0);

        for (const auto& melodie : melodiile){
            rank[melodie.get_rank()] ++;
        }

        this->ranks = rank;
        this->melodii = melodiile;
        endResetModel();

    }

};


#endif //MELODII1_TABELVIEW_H
