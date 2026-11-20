cmake_minimum_required(VERSION 3.27)
project(buget_bun C)

set(CMAKE_C_STANDARD 23)

add_executable(buget_bun main.c
        buget.c
        buget.h
        MyList.c
        MyList.h
        service.c
        service.h)
