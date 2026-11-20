cmake_minimum_required(VERSION 3.30)
project(Lab6InClass)

set(CMAKE_CXX_STANDARD 23)

find_package(MPI REQUIRED)

add_executable(Lab6InClass main.cpp)

target_link_libraries(Lab6InClass PRIVATE MPI::MPI_CXX)


