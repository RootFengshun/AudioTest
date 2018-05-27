# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

set( CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -Wall -pedantic" )

include_directories(${PROJECT_SOURCE_DIR}/include)

add_definitions(-DFIXED_POINT -DUSE_KISS_FFT -DHAVE_CONFIG_H)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

add_library( # Sets the name of the library.
             native-lib

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
             src/main/cpp/native-lib.cpp
              src/main/cpp/noise_suppression.c
              src/main/cpp/fft4g.c
              src/main/cpp/ns_core.c

              src/main/cpp/analog_agc.c
              src/main/cpp/digital_agc.c
              src/main/cpp/division_operations.c
              src/main/cpp/copy_set_operations.c
              src/main/cpp/dot_product_with_scale.c
              src/main/cpp/downsample_fast.c
              src/main/cpp/resample_by_2.c
              src/main/cpp/spl_sqrt.c)

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
                       native-lib

                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib} )