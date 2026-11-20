cmake_minimum_required(VERSION 3.30)
project(ExamenPractic)

set(CMAKE_CXX_STANDARD 23)

find_package(MPI REQUIRED)

add_executable(ExamenPractic main.cpp)

target_link_libraries(ExamenPractic PRIVATE MPI::MPI_CXX)
