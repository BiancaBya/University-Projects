cmake_minimum_required(VERSION 3.30)
project(Lab3)

set(CMAKE_CXX_STANDARD 23)

find_package(MPI REQUIRED)

add_executable(Lab3 main.cpp)
#        Seq.cpp main.cpp)

target_link_libraries(Lab3 PRIVATE MPI::MPI_CXX)

