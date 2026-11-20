cmake_minimum_required(VERSION 3.27)
project(Colectie)

set(CMAKE_CXX_STANDARD 14)

include_directories(.)

add_executable(Colectie
        App.cpp
        Colectie.cpp
        Colectie.h
        IteratorColectie.cpp
        IteratorColectie.h
        TestExtins.h
        TestExtins.cpp
        TestScurt.h
        TestScurt.cpp)
