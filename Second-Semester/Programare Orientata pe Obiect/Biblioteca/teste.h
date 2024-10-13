#ifndef QT1_TESTE_H
#define QT1_TESTE_H

#include "service.h"

class Test
{

public:

    Test() = default;


    static void test_create_carte();

    static void test_get_titlu();

    static void test_get_autor();

    static void test_get_gen();

    static void test_get_an();

    static void test_set_titlu();

    static void test_set_autor();

    static void test_set_gen();

    static void test_set_an();

    static void test_add_file();

    static void test_modifica_file();

    static void test_sterge_file();

    static void test_add_dictionar();

    static void test_modificare_dictionar();

    static void test_sterge_dictionar();

    static void test_find_dictionar();

    static void test_all_dictionar();

    static void test_exceptie_repo();



    static void test_adaugare_carte();

    static void test_modificare_carte();

    static void test_stergere_carte();

    static void test_find_carte();



    static void test_service_add();

    static void test_service_modificare();

    static void test_service_stergere();

    static void test_service_find();

    static void test_filtrare_titlu();

    static void test_filtrare_an();

    static void test_comparare();

    static void test_sortare();

    static void test_get_all();

    static void test_find_carte_titlu();


    static void test_raport();

    static void test_export_cos_cos();

    static void test_carti_random_cos();

    static void test_golire_cos_cos();

    static void test_adauga_cos_cos();

    static void test_undo();

    static void test_validari();

    static void test_mesaj();


    static void test_all();


};

#endif //QT1_TESTE_H


