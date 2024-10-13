#include "GUI.h"

int main(int argc, char *argv[])
{

    QApplication a(argc, argv);

    Repository repo("C:/Users/Next/CLionProjects/melodii1/file.txt");
    Service service(repo);
    GUI gui(service);

    gui.show();

    return a.exec();

}


/*
1 melodie1 artist1 1
2 melodie2 artist2 2
3 melodie3 artist3 3
4 melodie4 artist4 4
5 melodie5 artist5 5
6 melodie6 artist6 4
7 melodie7 artist7 4
8 melodie8 artist8 3
9 melodie9 artist9 1
10 melodie10 artist10 1
 */