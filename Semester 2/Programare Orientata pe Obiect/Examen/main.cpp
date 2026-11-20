#include <QApplication>
#include <QPushButton>
#include "GUI.h"


int main(int argc, char *argv[]) {
    QApplication a(argc, argv);

    test_repo();
    test_service();

    Repository repo("C:/Users/Next/CLionProjects/ex/file.txt");

    Service service(repo);

    GUI gui(service);

    gui.show();

    return QApplication::exec();
}
/*
nr1 masina1 102 sedan
nr2 masina2 102 hatch
nr3 masina3 102 suv
nr4 masina4 202 van
nr5 masina5 202 sedan
nr6 masina6 102 hatch
nr7 masina7 202 suv
*/
