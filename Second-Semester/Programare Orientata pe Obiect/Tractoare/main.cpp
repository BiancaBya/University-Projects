#include <QApplication>
#include <QPushButton>
#include "GUI.h"

int main(int argc, char *argv[]) {

    QApplication a(argc, argv);

    Repository repository("C:/Users/Next/CLionProjects/tractor/file.txt");

    Service service(repository);

    GUI gui (service);

    gui.show();

    return QApplication::exec();
}

/*
 *
1 tractor1 tip_1 4
2 tractor2 tip_1 4
3 tractor3 tip_2 6
4 tractor4 tip_1 10
5 tractor5 tip_2 6
6 tractor6 tip_3 4
7 tractor7 tip_1 6

 */

