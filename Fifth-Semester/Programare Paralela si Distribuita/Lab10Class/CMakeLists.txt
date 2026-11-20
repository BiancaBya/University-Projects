cmake_minimum_required(VERSION 3.30)
project(Lab10Class)

set(CMAKE_CXX_STANDARD 23)

find_package(OpenMP REQUIRED)

add_executable(Lab10Class main.cpp)

if(OpenMP_CXX_FOUND)
    target_link_libraries(Lab10Class PUBLIC OpenMP::OpenMP_CXX)
endif()