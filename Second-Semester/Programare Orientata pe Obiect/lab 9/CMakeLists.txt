cmake_minimum_required(VERSION 3.28)
project(lab9)

set(CMAKE_CXX_STANDARD 23)

add_executable(lab9 main.cpp
        carte.cpp
        carte.h
        cos.h
        exceptions.h
        repository.cpp
        repository.h
        service.cpp
        service.h
        teste.cpp
        teste.h
        undo.cpp
        undo.h
        validator.h
        vector_dinamic.h)
