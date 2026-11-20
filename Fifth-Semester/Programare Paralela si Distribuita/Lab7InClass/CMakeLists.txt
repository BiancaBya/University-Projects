cmake_minimum_required(VERSION 3.30)
project(Lab7InClass)

set(CMAKE_CXX_STANDARD 23)

add_executable(Lab7InClass main.cpp)

find_package(MPI REQUIRED)


target_link_libraries(Lab7InClass PRIVATE MPI::MPI_CXX)

