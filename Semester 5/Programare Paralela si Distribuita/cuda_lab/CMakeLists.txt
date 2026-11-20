cmake_minimum_required(VERSION 3.30)

set(CUDAToolkit_ROOT "C:/Program Files/NVIDIA GPU Computing Toolkit/CUDA/v13.1")
set(CMAKE_CUDA_ARCHITECTURES 89)

project(cuda_lab LANGUAGES CXX CUDA)

set(CMAKE_CXX_STANDARD 23)
set(CMAKE_CUDA_STANDARD 14)

find_package(CUDAToolkit REQUIRED)

set(CMAKE_CUDA_RUNTIME_LIBRARY Shared)

add_executable(
        cuda_lab
        main.cu
#        main_lab1.cu
)

target_link_libraries(cuda_lab PRIVATE CUDA::cudart)

set_target_properties(cuda_lab PROPERTIES
        CUDA_SEPARABLE_COMPILATION ON)

