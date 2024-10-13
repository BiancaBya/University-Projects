#include <QtWidgets>
#include "carte.h"


class MyListModel : public QAbstractListModel {

    std::vector<Carte> carti;

public:

    explicit MyListModel(const std::vector<Carte>& carti, QObject* parent = nullptr) : QAbstractListModel(parent), carti(carti) {}

    int rowCount(const QModelIndex& parent = QModelIndex()) const override {
        return carti.size();
    }

    QVariant data(const QModelIndex& index, int role = Qt::DisplayRole) const override {

        if (!index.isValid() || index.row() >= static_cast<int>(carti.size()) || role != Qt::DisplayRole)
            return QVariant{};

        const auto& carte = carti[index.row()];
        return QString::fromStdString(carte.get_titlu() + " | " + carte.get_autor() + " | " + carte.get_gen() + " | " + std::to_string(carte.get_an()));

    }

    void setCarti(const std::vector<Carte>& cartile) {

        beginResetModel();
        this->carti = cartile;
        endResetModel();

    }

};


class MyTableModel : public QAbstractTableModel{

    std::vector<Carte> carti;

public:

    explicit MyTableModel(const vector<Carte>& carti) : carti(carti){};

    int rowCount(const QModelIndex & parent = QModelIndex()) const override{
        return carti.size();
    }

    int columnCount(const QModelIndex & parent = QModelIndex()) const override{
        return 4;
    }

    QVariant data(const QModelIndex & index, int role =  Qt::DisplayRole) const override{

        Carte carte = carti[index.row()];

        if (role == Qt::DisplayRole) {

            if (index.column() == 0)
                return QString::fromStdString(carte.get_titlu());
            else if (index.column() == 1)
                return QString::fromStdString(carte.get_autor());
            else if (index.column() == 2)
                return QString::fromStdString(carte.get_gen());
            else if (index.column() == 3)
                return QString::number(carte.get_an());

        }

        return QVariant{};
    }

    QVariant headerData(int section, Qt::Orientation orientation, int role = Qt::DisplayRole) const override {
        if (role != Qt::DisplayRole)
            return QVariant{};

        if (orientation == Qt::Horizontal) {
            switch (section) {
                case 0: return QString("Titlu");
                case 1: return QString("Autor");
                case 2: return QString("Gen");
                case 3: return QString("An");
                default: return QVariant{};
            }
        }
        return QVariant{};
    }

    void setCarti(const vector<Carte> cartile){

        beginResetModel();
        this->carti = cartile;
        endResetModel();

    }

};





