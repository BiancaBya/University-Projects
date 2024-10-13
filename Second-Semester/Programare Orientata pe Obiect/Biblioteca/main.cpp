#include <QApplication>
#include <GUI.h>
#include "GUI_modele.h"
#include "service.h"
#include "teste.h"
#include <vector>
#include <string>
using std::vector;
using std::string;



int main(int argc, char *argv[])
{
    QApplication a(argc, argv);

    Test::test_all();

    RepoInFile carti("C:/Users/Next/CLionProjects/qt1/fisier.txt");
    CarteService service(carti);

    //GUI gui(service);
    //gui.show();

    GUIModele gui(service);
    gui.show();

    return a.exec();
}


