#ifndef QT1_COSREADONLYGUI_H
#define QT1_COSREADONLYGUI_H
#include <QtWidgets>
#include "service.h"
#include "cos.h"
#include "observer.h"

class CosReadOnly : public QWidget, public Observer{

public:

    CosReadOnly(CarteService& service, Cos& cos) : service(service), cos(cos){
        initGUI();
        cos.addObserver(this);
    };

private:

    CarteService& service;
    Cos& cos;

    void initGUI(){

        setWindowTitle("Cos Read Only");

    }

    void paintEvent(QPaintEvent* event) override {

        QPainter painter(this);

        for (int i = 0; i < cos.get_lungime(); ++i) {

            int x = QRandomGenerator::global()->bounded(width());
            int y = QRandomGenerator::global()->bounded(height());

            int diameter = 20;
            painter.setBrush(Qt::blue);
            painter.drawEllipse(x, y, diameter, diameter);

        }
    }

    void update() override{
        repaint();
    }

};


#endif //QT1_COSREADONLYGUI_H




