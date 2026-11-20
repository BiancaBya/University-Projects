cmake_minimum_required(VERSION 3.28)
project(Proiect_C__)

set(CMAKE_CXX_STANDARD 14)

include_directories(.)

add_executable(Proiect_C__
        App.cpp
        Dictionar.cpp
        Dictionar.h
        IteratorDictionar.cpp
        IteratorDictionar.h
        TestExtins.cpp
        TestExtins.h
        TestScurt.cpp
        TestScurt.h)
