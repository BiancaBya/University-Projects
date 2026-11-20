cmake_minimum_required(VERSION 3.27)
project(OOP_lab2 C)

set(CMAKE_C_STANDARD 23)

add_executable(OOP_lab2 main.c
        oferta.c
        oferta.h
        teste.c
        teste.h
        lista.c
        lista.h
        service.c
        service.h
        customsort.c
        customsort.h
)
