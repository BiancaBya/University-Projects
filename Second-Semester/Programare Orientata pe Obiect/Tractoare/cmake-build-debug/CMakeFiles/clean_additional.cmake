# Additional clean files
cmake_minimum_required(VERSION 3.16)

if("${CONFIG}" STREQUAL "" OR "${CONFIG}" STREQUAL "Debug")
  file(REMOVE_RECURSE
  "CMakeFiles\\tractor_autogen.dir\\AutogenUsed.txt"
  "CMakeFiles\\tractor_autogen.dir\\ParseCache.txt"
  "tractor_autogen"
  )
endif()
