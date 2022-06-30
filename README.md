[![DOI](https://zenodo.org/badge/DOI/10.5281/zenodo.5577946.svg)](https://doi.org/10.5281/zenodo.5577946)

# QSPcc: Breaking through model simulation time limits

Mathematical models have grown in size and complexity to the extent that are often computationally intractable. In sensitivity analysis and optimization phases, critical for tuning, validation and qualification, models of ever-increasing complexity may be run thousands of times with varying parameters. Scientific programming languages popular for prototyping, such as MATLAB and R, can be a bottleneck in terms of performance.

Here is where QSPcc comes into play to seamlessly convert freely-written MATLAB mathematical models into fast C code. Contrary to existing solutions, no tweaking or dialect learning is required as full, complete Matlab projects can be ingested and seamlessly compiled by QSPcc. With speed-ups of 22000x peak, and 1605x arithmetic mean on the original modeling projects, our results show that QSPcc consistently delivers superior performances.

Basic translation to R code is also provided to support interoperability between teams and across institutions either commercial, academic or philanthropic. 

In modern model-based drug discovery and development, QSPcc accelerated, or enabled, to holistically track sophisticated system interactions to support decision making in translational research of rare diseases.
 
For details, citation and contact information see the paper *[QSPcc reduces bottlenecks in computational model simulation](https://doi.org/10.1038/s42003-021-02553-9)*.



# Table of contents

QSPcc requires only [Java](https://java.com/en/download/help/download_options.html) 8, or higher, to generate C or R. The latest QSPcc executable can be downloaded from the [releases](https://github.com/cosbi-research/QSPcc/releases/latest).

However, executing the generated C programs requires a compiler (e.g. gcc) and some third-party libraries (e.g Sundials ODE solver). The process can be difficult, so to ease the installation process, we bundled togheter QSPcc, the compilation environment and the required libraries in a convenient [all-in-one solution (METHOD 1)](#method-1-pre-installed-solution).

Expert users with customization needs can proceed to the [manual installation (METHOD 2)](#method-2-custom-installation-technical).

- [METHOD 1: Pre-installed all-in-one solution (easy & effective) 👍](#method-1-pre-installed-solution)
- [METHOD 2: Custom installation (technical)](#method-2-custom-installation-technical)
   + [Installation](#installation)
   + [Translation](#translation)
   + [Execution :rocket:](#execution)
	   * [Parallel execution](#parallel-execution)
	   * [Execution on a cluster](#execution-on-a-cluster)
- [Execution options](#execution-options)
   + [Save the timeseries dynamics](#save-timeseries-dynamics) 
   + [Execution of models translated to R](#execution-of-models-translated-into-r)
- [FAQ and DIY](#faq-and-diy)
- [Troubleshooting](#troubleshooting)



# METHOD 1: Pre-installed solution

In this section we'll be using Docker, a utility allowing to bundle many libraries and configurations easily avoiding the need of installing them manually. You only need the terminal/command line.

There are 2 simple setup steps:

- Download the Docker utility:
[MAC OSX](https://docs.docker.com/docker-for-mac/install/) | [Windows](https://docs.docker.com/docker-for-windows/install/) | Linux: install `docker-ce` package from your package manager.

- Pull the QSPcc docker image by typing in your command line/terminal:
```
docker pull cosbi/qspcc
```
   Or visit [QSPcc's DockerHub page](https://hub.docker.com/r/cosbi/qspcc).


### QSPcc usage

Make sure the Docker utility is running. The following command starts the QSPcc server console, making your home directory available to the QSPcc server:
```
docker run -v ~:/qspcc -it --rm cosbi/qspcc
```
Windows users can type `docker run -v $HOME:/qspcc -it --rm cosbi/qspcc`.

Sharing your home (`~`) casts its exact structure of directories into your QSPcc server home (`/qspcc`). Your operating system may ask for your permission to grant Docker the access to your files. If you want to share a specific project folder rather than your home, change the `~` above with the absolute path of your project folder. In the QSPcc server console you can move (`cd`) anywhere within the folder you share.

⚠️  **Beware**: you also have got the power to create/modify/**delete** contents in your shared folder! 


Optionally, you may want to run a complete test of all models described in the paper:
```
qspcc-server:~> qspcc-tests
```

To translate, eg. Matalb to C, `cd` to the project files folder and type:
```
qspcc-server:~> qspcc --from MATLAB --to C --source main.m --dest C_folder/
```

You can inspect the C code placed in the `C_folder` heading to the folder on your computer you shared before.
To compile (in the example below, `make`) into fast binary code _and_ execute it (`./main`), just type in your shell:

```
qspcc-server:~> cd C_folder
qspcc-server:~/C_folder> make && ./main
```

By now, you should have already experienced a speedup of 1 or 2 *orders of magnitude* 🚀.

When you have finished your work you can exit the QSPcc server console:

```
qspcc-server:~> exit
```


Additional advanced [translation](#translation) and [execution](#execution-options) options are described in the detailed execution guide.


### Customization of the QSPcc Docker image

The QSPcc Docker container provides all the features described in the paper. It can be further extended with the following improvements:

- [MATLAB C/MEX](https://www.mathworks.com/help/matlab/call-mex-file-functions.html) automatic generation of all the required MEX code to couple your fast C code directly into MATLAB,
- [Hardware acceleration](https://software.intel.com/content/www/us/en/develop/tools/oneapi/components/onemkl.html) by exploiting Intel MKL, a fast math library to further speed-up your matrix computations
- [interoperability module](https://www.mathworks.com/help/matlab/import_export/supported-file-formats.html) ability to load/save using native MAT file format

For help on this, adding new languages and other advanced features you may want to get in touch with the [COSBI Bioinformatics lab](mailto:bioinformatics@cosbi.eu). We'll be happy to help!


# METHOD 2: Custom installation (technical)

## INSTALLATION

If you prefer to have full control and/or make enterprise decisions about the entire deployment, here is the step-by-step process:

- [QSPcc executable installation](#qspcc-executable-installation)
- [Sundials, for Ordinary Differential Equantion (ODE) systems](#sundials-for-ode-systems)
  + [Installation from repository (linux, admin only)](#installation-from-repository-linux-administrator-only)
  + [Installation from tar file (linux and macOS)](#installation-from-tar-file-linux-and-macos)
- [TCMALLOC (Linux & Mac only)](#tcmalloc-linux-and-macos)


### QSPcc executable installation

Rather than a Docker you may want to install all dependencies directly on your machines. As we'll find out soon, there are some important scalabilty benefits in this approach.

QSPcc requires only [Java](https://java.com/en/download/help/download_options.html) 8 and upward, but we strongly encourage to use the latest version of Java.
Nothing else is needed to generate the translated *source code*.

To install QSPcc simply type, from the root of the downloaded code:

`sudo ./install.sh`

by default the install script will install in /usr/local/bin.
You can specify a different installation path as a first argument of the install script, example:

`sudo ./install.sh /usr/`

If you are not administrator you can specify a folder in your home directory

`./install.sh /home/<username>/qspcc`

Here follows the requirements to actually *run* or *compile* the translated source code.
After the C and R dependencies installation you may want to test if everything is ok by typing

`./test.sh`

This command will translate, compile and execute a set of test models.

**R**

Models translated to R can require the following R packages:
- R.matlab
- R.utils
- deSolve
- pracma
- stats
- utils
- Matrix

Note that depending on the source program some libraries won't be loaded in the target program.

**C**

Models translated to C can be *compiled*, at your choice, with any of the following compilers: gcc (6 or above), clang (6 or above), icc (Intel C compiler), pgc++ (Nvidia C compiler).

The `make` tool is required as well as the following libraries:

- `libc6` - dynamically linked, required on both the compilation machine and the execution machine. `libc6` and `zlib` are either pre-installed or can be easily installed in any unix-based system and also in windows with cygwin. Check this out in case of non-standard paths:
```
-stdlib,--c-std-lib-path <LIB-PATH>         (C target) The path to the library files (.so, .a, .dynlib)
                                            for zlib and other standard libraries. default: /usr/lib
```
- [Sundials](https://computing.llnl.gov/projects/sundials/sundials-software) - ODE integration - statically linked, required on the compile machine but not on the execution machine
- [TCMalloc](https://github.com/gperftools/gperftools) - Memory optimization - statically linked, required on the compile machine, but not the execution machine
  - `lc++` (dynamically linked, required on the execution machine, but not the compile machine


**NOTE**: we have tried our best to simplify the whole process by generating statically compiled executables. This means that, once compiled, the executables run without further depencies on any other computer.

### Sundials (for ODE systems)

The C files translated by QSPcc will work with Sundials version 2.x, 3.x, 4.x, 5.0.x-5.1.x.
Use the C command line option `sunver` to specify the version of Sundials you wish to use.

#### Installation from repository (Linux, administrator only)

- sudo apt-get update
- sudo apt-get install build-essential 
- sudo apt-get install libsundials-serial libsundials-serial-dev 

This will install the repository version.

#### Installation from tar file (Linux and MacOS)

In the [Sundials website](https://computing.llnl.gov/projects/sundials/sundials-software), scroll down to "Software Releases" and from the list download the CVODE package. At the time of writing the latest version is `CVODE 5.7.0`. Note in the list there are also packages `SUNDIALS xxx` and `CVODES xxx`, which are _not_ of interest here.
Unzip the tar file. 

This is a quick outline of the process on linux, for a administrator user:

- sudo apt-get update
- sudo apt-get install ccmake and cmake-gui

If you are a Linux user without administrator access you can follow the MACOSX guide also on Linux and alter the installation paths to some path you have write access to (usually your home directory).

A MACOSX user should download from the [cmake website](https://cmake.org/download/) for MACOSX and install it as any other MAC program.

After the CMAKE installation 

- mkdir /tmp/sundials-build
- cd /tmp/sundials-build

- run cmake-gui (or ccmake)
- choose the source folder in the text editor "Where is the source code": /home/<username>/Downloads/cvode-5.7.0
- choose the build folder in the text editor "Where to build the binaries": /tmp/sundials-build
- click "configure" and select your target operating system and the compiler you want to use
- change the installation paths as you wish, the default for QSPcc is `/usr/local/sundials-5.7.0`.
  Select if unchecked `ENABLE_OPENMP` `BUILD_STATIC_LIBS` `BUILD_SHARED_LIBS` `CMAKE_POSITION_INDEPENDENT_CODE`, 
  uncheck if checked `USE_GENERIC_MATH`
  set `CMAKE_C_FLAGS` to `-O2 -march=native`
  set `CMAKE_C_COMPILER` to the best compiler for your architecture, for instance icc on intel processors or pgc++ (needed for `OPENMP_DEVICE_ENABLE`)
  and be sure `SUNDIALS_PRECISION` is set to `double` and `SUNDIALS_INDEX_TYPE` is set to int32\_t (or `SUNDIALS_INDEX_SIZE` is set to 32)
  Optionally, if you are not administrator, you can alter the installation path to a directory in your `HOME` folder.
- click "generate" to generate the platform-dependent build files
- **IF YOU DON'T HAVE CMAKE GUI:** run this one-line command and replace the paths in angular brackets with your paths
```
cmake -DCMAKE_INSTALL_PREFIX=<INSTALLATION_PATH> \
      -DENABLE_OPENMP=ON -DENABLE_PTHREAD=ON -DBUILD_STATIC_LIBS=ON \
      -DBUILD_SHARED_LIBS=ON -DCMAKE_POSITION_INDEPENDENT_CODE=ON \
      -DUSE_GENERIC_MATH=OFF -DCMAKE_C_FLAGS="-O2 -march=native -fopenmp" \
      -DSUNDIALS_PRECISION=double  \
      -DEXAMPLES_ENABLE_C=OFF -DEXAMPLES_INSTALL=OFF \
      -DSUNDIALS_INDEX_SIZE=32    \
      ~/<CVODE_DOWNLOAD_PATH>/cvode-5.7.0
```
Optionally, if you want to enable lapack banded and dense solver, install libblas3/liblapack3 (`apt install libblas3 liblapack3` on debian based linux systems) and then add these options:
```
      -DENABLE_LAPACK=ON \
      -DLAPACK_LIBRARIES="<LAPACK_INSTALL_PATH>/lib/liblapack.so;<LAPACK_INSTALL_PATH>/lib/libblas.so"
```

- in a shell, cd to /tmp/sundials-build
- type 'make'
- type 'sudo make install'
- This will install static and dynamic libraries in /usr/local/lib and sundials headers in /usr/local/include 
  (or if you altered the installation path, in the <INSTALLATION_PATH>/lib and <INSTALLATION_PATH>/include)
- to be sure everything was installed correctly you may want to run 'make test'
- sometimes (on mac) happens that all the tests fails, if this is the case check manually by entering 
  in the examples folder in your installation path (`/usr/local/examples` by default) and type make (or sudo make if you don't have write privileges). After the complation succeeds run all the executables, they should terminate without errors.

If you install this library in a non-standard path, set the following flags to QSPcc executable when translating:
```
 -suninclude,--c-sundials-include-path <INCLUDE-PATH>  (C target) The include path for the local installation of
                                                       the Sundials library. default: /usr/local/include
 -sunlib,--c-sundials-lib-path <LIB-PATH>              (C target) The path to the library files (.so, .a, .dynlib)
                                                       for the local installation of the Sundials library.
                                                       default: /usr/local/lib
```
Depending on the version and algorithm you use, set the following flag to QSPcc executable when translating:
```
 -sunver,--c-sundials-version <VERSION>                (C target) Sundials major version to be used by the
                                                       translated program. Supported values are 2,3,4,5. Default
                                                       is '5'
```

### TCMALLOC (Linux and MacOS)

Download the [latest release](https://github.com/gperftools/gperftools/releases/latest) of TCMALLOC.

- cd to the directory containing the source code
- ./configure --prefix=/usr/local/tcmalloc
- make
- optionally, make check
- make install

If you install this library in a non-standard path set the following flags to QSPcc executable when translating:
```
 -tcmallocinclude,--c-tcmalloc-include-path <INCLUDE-PATH>  (C target) Faster version of malloc, include path. default:
                                                            /usr/local/tcmalloc/include
 -tcmalloclib,--c-tcmalloc-path <LIB-PATH>                  (C target) Faster version of malloc, library path. default:
                                                            /usr/local/tcmalloc/lib
```

## USAGE

QSPcc helps you boost your research in two steps:
1. translation from Matlab to C
2. Compilation of C code and fast binary execution

Furthermore QSPcc can also automatically produce executables that can be run within MATLAB through [MATLAB C/MEX](https://www.mathworks.com/help/matlab/call-mex-file-functions.html) interface,
can exploit [hardware acceleration](https://software.intel.com/content/www/us/en/develop/tools/oneapi/components/onemkl.html) by exploiting Intel MKL, 
a fast math library to further speed-up your matrix computations, and can be interoperable with MATLAB with native support for [MAT](https://www.mathworks.com/help/matlab/import_export/supported-file-formats.html) files.

Contact the [COSBI Bioinformatics lab](mailto:bioinformatics@cosbi.eu) for these and other ready to use advanced features.

## TRANSLATION

To get you started, any generic MATLAB program can be translated typing:

```
$ qspcc --from MATLAB --to C --source /project/main.m --dest c_folder/ 
```

The generated C code will be placed in the subfolder `c_folder/` which will be automatically created for you, relative to the current working directory (i.e. where you ran qspcc itself).

The options in the example are virtually all-what-you-need:
- `-f` / `--from` the source language (case-insensitive). Currently Matlab. New languages can be developed, [here how](SDK_DEVELOPERS_GUIDE.md).
- `-t` / `--to` the target language (case-insensitive). Currently C, R.  New languages can be developed, [here how](SDK_DEVELOPERS_GUIDE.md).
- `-s` / `--source` the main script of the program to be translated (can be also a function without parameters). References to external files belonging in the same folder will be automatically detected and translated.
- `-d` / `--dest` the folder where the user wish to store the translated program, automatically created if it doesn't exists

Additional options of interest are:
```
-slm,--c-asym-sparse-matrix                     (c target) Assume a sparse matrix structure. Accepts the
                                                   following parameter values: REAL_SYMM for real symmetric
                                                   matrices, REAL_SYMM_POSITIVE_DEF for real symmetric
                                                   positive-definite matrices, REAL_NOSYMM for real
                                                   non-symmetric matrices. By default REAL_NOSYMM is assumed.
-si,--c-store-input                             (c target) Makes the CSV file contents hard-coded in the
                                                   output program at compile time (faster). To read CSV files
                                                   everytime at runtime (slower) do not specify this flag.
-prealloccheck,--c-preallocated-memory-check    (c target) By default no memory checks are performed for
                                                   variables preallocated with functions such as
                                                   eye,ones,nan,zeroes (faster). Enable this flag (slower) if
                                                   you get memory access errors and want to check the
                                                   variables' memory.
-mist,--mist-template <TEMPLATE-FOLDER>         (mist target) Mist is a C formatting guideline. A
                                                   <TEMPLATE> formatting folder is provided as input. Please
                                                   check the docs folder for info on the stylistic guidelines.
```

Advanced options are available as custom extensions. Contact the [COSBI Bioinformatics lab](mailto:bioinformatics@cosbi.eu) for other ready to use advanced features.


**Note**:The QSPcc command line is the same for both the Custom installation on your machines and the Docker image installation. The latter has only some additional Docker-related paramenters. 

The next section describes how to compile and execute the generated efficient code.


							   
## EXECUTION

* [Parallel execution](#parallel-execution)
* [Execution on a cluster](#execution-on-a-cluster)

A custom installation and execution, while requiring some tecnical effort, can be beneficial in many ways including
- cluster and parallel computing
- full enterprise control on the execution and scalibity efficiency.

If you opted for a custom installation on your system, by default the binaries generated are *fully static*, meaning that once compiled they can be run anywhere, even on a system that doesn't have sundials, mkl and other dependent libraries. See the [Execution options](#execution-options) to learn how that is done.

If `main.m` was the starting source file, to execute its compiled binary form, just type `./main` in your shell.


### Parallel execution

By default MATLAB parallelizes operations on matrices (pointwise, dot product, linear solve etc) and explicitly parallelizes for-loops with `parfor` construct.

QSPcc can do the same for fast C code by exploiting OpenMP for pointwise operations and `parfor` (sum, subtract, point-wise multiplication).

By default every pointwise operation on matrices are *not* parallelized. This is only because in practice we have seen that on our laptops, automatic parallelization slows down the execution.
Thread parallelization can be explictly turned on by decorating the original code with a comment containing the `@qspcc` string followed by an arbitrary one-line 
[openmp directive](https://www.openmp.org/wp-content/uploads/OpenMP-4.5-1115-CPP-web.pdf), 
or by using the `parfor` Matlab construct.

For example:

```
parfor i=1:10
...
end
```
or 

```
% @qspcc parallel for if( a > 100 )
for i=1:10
...
end
```

You can also set the environment variable `OMP_NUM_THREADS` to the number of parallel threads you want to use. 

For example the following command line will execute the pointwise operations in the program in serial mode  (without threads)

```
$ OMP_NUM_THREADS=1 ./main
```

Non-pointwise operations can be parallelized using the [hardware acceleration](https://software.intel.com/content/www/us/en/develop/tools/oneapi/components/onemkl.html) module, a fast math library to parallelize matrix operations. Contact the [COSBI Bioinformatics lab](mailto:bioinformatics@cosbi.eu) for further details.

**NOTE:** OPENMP parallel instructions are not compatible with sundials LapackDense solver (enabled by `lapack_solver=on` make option).
Remove `parfor` statements from your code if you plan to use lapack dense solver.

### Execution on a cluster
Here we report our own experience with a cluster environment with SLURM. Other batch and queuing systems may be supported as well.

```
$ qspcc --from MATLAB --to C --source /project/main.m --dest c_folder/ 
$ make -C c_folder/ 
$ scp -r c_folder/  <user>@SERVER:~
$ ssh <user>@SERVER "cd c_folder/ ; make simulation"
```

That will submit a pre-compiled sbatch to your slurm cluster.

Note that it's not required for the slurm cluster to contain all the required libraries because the executable file generated is *fully static*.
This means that you can compile once and run *everywhere*.
							   
	
							  
							   
							   
# Execution options

  * [Sanity Checks](#sanity-checks-for-c-code)
  * [Save Timeseries Dynamics](#save-timeseries-dynamics)
  * [Profiling CPU](#profiling-cpu-run-time)
  * [Execution of models translated into R](#execution-of-models-translated-into-r)

							   
If you selected the C target of the compiler the output will be a folder containing *.c and *.h files shipped with a Makefile (for mac and linux).
Steps to compile under mac and linux 

- cd to the destination folder, where the translator created the C files
- inspect the `Makefile` and change the paths to the lib and include folder if needed.
- type `make` to compile, see the table below for all the compile-time flags.
- after compilation execute the resulting program as `./main` (if the orginal Matlab file was `main.m`)

Note that if you are not using a Docker it's not necessary that the machine that is executing the code is the same machine that compiles the code. 
The executable can be moved to a different machine that doesn't have any library installed and it will *just work*.
This means that the libraries are needed only on the machine that you use to compile the program.							   

							 	   
							   
|option|values,<br>if provided|description|sundials<br>version|
|------|-----|-----------|-------|
|sanitychecks|on|The generated code will make extra checks, e.g. matrix access out-of-bound, or out-of-range values for functions. It will also enable to debug the C code. See [Sanity Checks](#sanity-checks-for-c-code) | all |
|dump|number|The derivatives generated *dydt* (the results of the function to be integrated `dydt=f(t,y)`) and the approximate solution *y* at every timestep will be stored in an auto-generated *\_sundials\_*-prefixed file to ease debugging of simulation errors. See [Save Timeseries Dynamics](#save-timeseries-dynamics) to learn more.| all |
|profiler|on|Enable the CPU run-time analysis to spot slow parts of the model. See [Profiling CPU](#profiling-cpu-run-time) section for further details.|all|
|dense\_solver|on|Default linear solver used also if no other linear solver option is selected.| all |
|lapack\_solver|on|Linear solver that solves the linear system using the highly optimized lapack libraries. **NOTE** Sundials must have been compiled with lapack support to use this. **NOTE2:** OPENMP parallel instructions are not compatible with sundials LapackDense solver (enabled by `lapack_solver=on` make option). Remove `parfor` statements from your code if you plan to use lapack dense solver. | 5 |
|band\_solver|number|Use this linear solver to solve banded linear systems. The number passed as value (ex. `band_solver=10`) specifies the number of upper-diagonal and lower-diagonal to consider.| 5 |
|lapack\_band\_solver|number|Use this linear solver to solve banded linear systems using the highly optimized lapack libraries. The number passed as value (ex. `lapack_band_solver=10`) specifies the number of upper-diagonal and lower-diagonal to consider. **NOTE** Sundials must have been compiled with lapack support to use this.| 5 |
|spgmr\_solver|number|Use the *Scaled, Preconditioned, Generalized Minimum Residual* iterative linear solver. The number passed as value (ex. `spgmr_solver=0`) specifies the number of Krylov basis vector to use. Choose `0` to autodetect.| 2, 5 |
|precondition\_band|number|Add a band matrix preconditioner to the selected linear solver. It uses difference quotients of the ODE right-hand side function *f* to generate a band matrix of bandwidth 2*`number`+1| 5 |
|fixedpoint\_iter|number|Replace the default Newton iteration with a fixed point (functional) iteration with optional Anderson acceleration. The acceleration subspace size `number` is required. Choose `0` to use a pure functional iteration without Anderson acceleration.| 5 |

Refer to the sundials cvode manual for a more in-depth description of the parameters. The solver setup can be further tuned manually editing the file `objs/sundialsLib.c` and `objs/sundialsLib.h` available in the destination folder (the one selected with `--dest` option).

## Sanity checks for C code

QSPcc C translation comes with a `Makefile` used by the `make` command to compile the translated code to machine code.

The default compilation creates a binary program optimized for speed, but QSPcc-translated C programs do have some 
sanity check options that allow users to check for the correctness of the translated program and catch errors 
as easily as possible.

`make sanitychecks=on` will produce machine code optimized for troubleshooting.
The produced executable will automatically check and report errors for the following situations

* access to array or matrices out of bounds `ex. a = ones(1,2); a(3)`
* access to uninitialized variables `ex. a(1)`
* matrix copy out of bounds `ex. b = ones(1,2); a = b(3:4)`
* unexpected parameter value for 'core' functions `ex. factorial(-1)`
* integrated functions (with `odeXX` family in MATLAB) that returns NaN or Inf `ex. ode15([0 1], f, yinit); f(0.1, y) = NaN`

and it is ready to be run with the C debugger `gdb` (under linux) or `lldb` under macOS.

```
$ gdb ./<YOUR_PROGRAM_NAME>
```

Refer to the [gdb manual](http://sourceware.org/gdb/current/onlinedocs/gdb/) for further instructions on how to debug C programs.

## Save Timeseries Dynamics

Remember the Cauchy formulation of the IVP (initial value problem)

```
y0 = INIT
dydt = f(t, y)
```

in this context, we call `f` the right hand side function.
Sundials, the C IVP solver shipped with QSPcc, solves the IVP numerically producing a timeseries 
(just as the `odeXX` family of matlab functions) by repeatedly calling `f`.

`make dump=1000` will make every simulation in the program generate a couple of CSV files 

* _sundials_y_dump.csv
* _sundials_dydt_dump.csv
* plot.sh

containing respectively the system states (one every `1000` time-steps) visited by the integrator (y), and the output value of the right hand 
side function (dydt) every `1000` time-steps visited by the integrator.

The plot.sh will generate a *png* file using *gnuplot* to depict the generated timeseries.

When the simulation results differ between MATLAB and C (or in general between any source language and C)
the user can pass the `dump` option to get a hint on the values that differ between the source program and the translated 
C program.
						   

## Profiling CPU run-time

To inspect the slowest parts of the generated C model you can combine the [google perf tool pprof](https://github.com/google/pprof) with the `sanitychecks=on` option.
In ubuntu it can be installed through 

```
apt install google-perftools
```

For example, type

```
make sanitychecks=on profiler=on
```

Now run the translated program with this special syntax 

```
LD_PRELOAD=/usr/local/tcmalloc/lib/libprofiler.so CPUPROFILE=main.prof CPUPROFILE_FREQUENCY=100000 ./main
```

update the path to `libprofiler.so` according to the location on your system.
update the C model name `./main` according to your actual model name.

- CPUPROFILE  indicates the output file for profiling data
- CPUPROFILE_FREQUENCY  indicates the profiler sampling frequency

At the end of the execution, a `main.prof` file will be generated in the current directory.

Now to inspect the call graph graphically type

```
google-pprof --web ./main main.prof
```

As before, adjust the model name `./main` anccording to your actual model name.

**NOTE:** Here we used `google-pprof` as the name of the profiler, but on your system it can be called `pprof`.

A sample profiler output can be found [here](docs/profiler.svg).

							  						   

## Execution of models translated into R

The output of the translator for the R target is a folder containing *.R files.
To execute the program identify the main R script (it will have the same name of the main script of your source program) and run/edit it using RStudio or directly from the command line with:

```
RScript PROGRAM_NAME.R
```



# FAQ and DIY

In this section we collect some common differences between MATLAB and C and other resources when translating from MATLAB to C.

  * [Can I translate a portion of a larger project?](#can-i-translate-a-portion-of-a-larger-project)
  * [I'd like to use fast C in Matlab with MEX, where do I start?](#id-like-to-use-fast-c-in-matlab-with-mex-where-do-i-start)
  * [How does MATLAB ode solvers compare to QSPcc C translation?](#how-does-matlab-ode-solvers-compare-to-qspcc-c-translation)
  * [Why do I get same dynamics but different integration time steps?](#why-do-i-get-same-dynamics-but-different-integration-time-steps)
  * [Should I prefer matrix slicing by row?](#should-i-prefer-matrix-slicing-by-row)
  * ['Function is undefined, comment or provide an implementation'. What can I do?](#function-is-undefined-comment-or-provide-an-implementation-what-can-i-do)
  * [Can I use a language different from MATLAB (Python for example)?](#can-i-use-a-language-different-from-matlab-python-for-example)


## Can I translate a portion of a larger project?

Yes you can. QSPcc is a general compiler so it will translate any program, from a large multi-file project to a single statement of your choice.

Just prepare a file with your (valid) MATLAB code and give the standard translate command `qspcc --from MATLAB --to C --source myscript.m --dest c_folder/` following the [translation](#translation) instructions above.

But if `myscript.m` has a function-entry point, you have to specify the type of the inputs with the special QSPcc syntax explained below.

QSPcc is intended to target primarily C so, as every compiler, performs type inference to be able to generate efficient code.
Type inference is done analysing the code to infer the type of variables based on how they are used: this is cool and complex Computer Science stuff! 
MATLAB, as an interpreted language, has in contrast a loose type checking of variables, essentially allowing the user to leave out the types of variables that it will figure out when executing the code itself. 
This is one of the many differences that makes C code blazingly faster than interpreted languages.
For example if in MATLAB you have a function like this one:

```
function b=f(a)
	b=a+2
end
```

The variable `a` can be either a `SCALAR` or an `INT` or a `MATRIX` and based on the type the translation will differ.

To avoid this ambiguity, you have to specify the types you expect for the entry-point function inputs 
in the comments preceding the function definition with the following syntax:

```
% ==FUNCTION== "<NAME OF FUNCTION>"
%    <FUNCTION PARAMETER NAME 1> <TYPE 1>
%    .
%    .
%    <FUNCTION PARAMETER NAME N> <TYPE N>
% ==END==
function <FUNCTION RETURN NAMES>=<NAME OF FUNCTION>(<FUNCTION PARAMETER NAME 1>, ..., <FUNCTION PARAMETER NAME N>)
	<FUNCTION BODY>
end
```

The tag `<TYPE>` accepts the following values 

- SCALAR
- INT
- BOOL
- MATRIX[N1, N2, N3...Nn] of SCALAR|INT|BOOL

where `N1..Nn` are the matrix dimensions.
If one or more matrix dimensions are unknown, you can replace the number with `INT` (ex. MATRIX[INT,INT] of SCALAR)

real-world example:

```
% ==FUNCTION== "Integration_function"
% ==PARAMETERS==  % ALLOWED PARAMETER TYPES: MATRIX, INT, SCALAR, BOOL
%    parameters_Liver_Tissue_Plasma             MATRIX[67, 1 ]  of SCALAR
%    calculated_parameters_Liver_Tissue_Plasma  MATRIX[83, 1 ]  of SCALAR
%    parameters_Spleen                          MATRIX[34, 1 ]  of SCALAR 
%    parameters_PK                              MATRIX[43, 1 ]  of SCALAR
%    DosePatients                               MATRIX[INT, INT]  of SCALAR
%    SubLoc                                     INT
%    tspan_Liver_Tissue_Plasma                  MATRIX[12, 1 ]  of SCALAR 
%    tspan_Spleen                               MATRIX[22, 1 ]  of SCALAR
%    yinit_Liver_Tissue_Plasma                  MATRIX[1,  52] of SCALAR 
%    yinit_Spleen                               MATRIX[1,  12] of SCALAR
% ==END==
function [tout_Liver_Tissue_Plasma, yout_Liver_Tissue_Plasma,tout_Spleen, yout_Spleen] = Integration_function(parameters_Liver_Tissue_Plasma, calculated_parameters_Liver_Tissue_Plasma, parameters_Spleen, parameters_PK, DosePatients,SubLoc, tspan_Liver_Tissue_Plasma,tspan_Spleen,yinit_Liver_Tissue_Plasma, yinit_Spleen)
 ....
end
```

## I'd like to use fast C in MATLAB with MEX, where do I start?

To bind custom C code (generated with QSPcc, or manually) with MATLAB, users should use the Mathworks MEX interface. 
Writing the MEX interface between C and MATLAB is generally complex because involves writing C code manually. 
See the [complete mathworks guide](https://www.mathworks.com/help/matlab/call-mex-file-functions.html) on how to write a C/MEX file.

We can help you to avoid this headache with a QSPcc module to automatically generate a MEX interface for you. Get in touch with the [COSBI Bioinformatics lab](mailto:bioinformatics@cosbi.eu) for further informations.

## How does MATLAB ode solvers compare to QSPcc C translation?

If in your MATLAB code you use a MATLAB solver like ode15s, ode45, ode23 etc. beware that the results may differ sligthly in the QSPcc C translation, because MATLAB and Sundials (the high-performance C library used by QSPcc under the hood) do employ different algorithms to solve the same problems. You should already be aware that both offer *a solution*, which is approximate in nature and, while both are correct at large, you may need high resolution precision to minimize the differences in the floating point outputs.

In this approximate scenario, the only real controls you have on both approaches to make them align, are the tolerances (or potentially, 
the maximum time step sizes): *decreasing the tollerances both in MATLAB and consequently in the QSPcc translation
should reduce the differences in the integration results (timeseries)*.

For instance if in your problem some variables are of the order of 1e-3 and other variables are 
of the order of 1e-5 then a rule of thumb for the tollerances can be:
 
- For the solution components having magnitudes 1e-3, perhaps an abstol of around 1e-10; 
- for the solution components with magnitude 1e-5, we've found useful an abstol of around 1e-12.

You should then be able to increase/decrease the value of reltol (again, for both odeXX and QSPcc) to control the number of significant digits in both solutions (down to around 1e-9 or 1e-10).  Since both odeXX and QSPcc only provide control over the local (in time) solution error, then the global solution error may indeed be slightly different than the specified tolerances.
This relationship between local and global error is highly problem-dependent, so it is recommended that you experiment with the tolerances a bit to gain some intuition about how they relate for your problem. 


## Why do I get same dynamics but different integration time steps?

While integrating with the MATLAB odeXX family of integrators you have to specify the integration times.
In MATLAB if you provide only the start and end of integration times, a matrix with an undefined number of rows will be returned, 
depending on the number of internal steps that MATLAB does.
In C you always have to specify the integration step, if not provided the translator will guess an integration step but the output will be different from MATLAB (the number of rows in the output timeseries will be different). 
If you want to have the exact same behaviour as MATLAB, always specify the integration step.

Example:

```
[t1,y1]=ode45(@(t,y) dfs(t,y,par), [tin tfin], ini, options);
```

will produce a number of rows different from the equivalent QSPcc C translation. change to

```
[t1,y1]=ode45(@(t,y) dfs(t,y,par), tin:tstep:tfin, ini, options);
```

to have the same number of rows in the output matrix both in MATLAB and QSPcc.


## Should I prefer matrix slicing by row?

Current internal implementation of slicing (and composing) matrices optimizes the code written using a row-based style rather than column-based.
We are testing an improvement to automatically handle both scenarios. While C is faster than interpreted language, for maximum efficiency in the meanwhile please prefer slicing by row, in particular when big matrices are involved. For example:

```
y = ones(1000000, 10)

y(:,8)
```

the current C code is slower than the equivalent C operation on the transposed matrix:

```
y = ones(1000000, 10)

yt = y';

yt(8,:)
```

Also avoid to call multiple times `y(:,8)` because every call will perform another slicing. Save it for later reuse in a variable instead

```
t = (max(y(:,8))-y(:,8))*0.1+y(:,8);
```

will become

```
y8 = y(:,8);
t = (max(y8)-8)*0.1+y8;
```


## 'Function is undefined, comment or provide an implementation'. What can I do?

This may happen because every single piece of code is individually translated by QSPcc into the equivalent efficient C implementation. The current language support in QSPcc is the result of a dozen of real-world projects which cover a wide spectrum of constructs, but not all. For example, user-functions that have been used in your code without providing a definition will trigger this error. If the error refers to a function of yours (e.g. my_objective_function()), please make sure the its definition and file is accessible to the project.

There are some graphical functions (eg. figure(), legend(), etc.) that are cosmetic in nature. Although they have no algorithmic effect, a number of challenges arise if they'd be supported in C (that should generate a... graphic output). These statements have  not been supported in the C translation. If you encounter additional functions with no "critical" algorithmic effects for your logic, try overriding these functions in Matlab with idle code such as `function figure() end`. The C translation will work perfectly and you'll not modify your custom logic, critical to your model development in Matlab.

There are other functions whose effects are not cosmetic but critial to the correct execution. The good news is that the addition of a new functions can be as easy as writing the Matlab function to do the job. It is also possible leaveraging external C libraries (e.g. Sundials, etc..), or implementing ad-hoc algorithms. There are two options here:

1. Try it for your self. You will *learn* how to do it and control all details. Concise, yet in-dept documentation is found [here](SDK_DEVELOPERS_GUIDE.md)
2. Get in touch with the [COSBI Bioinformatics lab](mailto:bioinformatics@cosbi.eu) where we'll be happy to help! Your learning will be reduced and of course we'll need to organize the process.

## Can I use a language different from MATLAB (Python for example)?

QSPcc is modular, if you need languages not supported yet, it's possible and documented how to add your own language. 
There is a concise, yet in-dept documentation in the [Software Development Kit](SDK_DEVELOPERS_GUIDE.md) and in the code itself. 

You may also want to get in touch with the [COSBI Bioinformatics lab](mailto:bioinformatics@cosbi.eu). We'll be happy to help!


# Troubleshooting

  * [Never change the input values within the Matlab integration function](#never-change-the-input-values-within-the-matlab-integration-function)
  * [Reuse of the same variable in different contexts](#reuse-of-the-same-variable-in-different-contexts)
  * [Syntax Errors and workarounds](#syntax-errors-and-workarounds)
  * [Divergent dynamics, convergence errors or never-terminating simulations](#divergent-dynamics-convergence-errors-or-never-terminating-simulations)


## Re-assigning the input variable within the MATLAB integration function

Matlab odeXX family evaluates the new input value y' used for an assignment independently from the previous value y (that is just discarded).
For efficiency reasons in C, the Sundials library reuses the memory for y from one call to another.
This implies that altering the function's input value y in Matlab won't have any effect on a discarded variable, while in C the memory will be reused and the new value y' will be a perturbation of the previous value y (eg. with DQ algorithm, or Newton iteration).

This is an example of Matlab code that produces a faulty translation, as `y`is modified within the integration function:

```
ode(@f, ....)
...
function dxdt = f(t,y)
  y = reshape(y, 5, 5)
  dxdt = [2*y(1,:); 4*y(2,:); 10*y(3,:); 1.1*y(4,:); 6*y(5,:)] 
end
```

While Matlab will discard the current `y` and reallocate new memory when it comes to the next iteration, in C all this overhead is avoided by reusing the same memory for `y`. Altering `y` during the integration, will result in a structure/value of `y` that is not the intended one for the next cycle and will produce integration errors.

## Reuse of the same variable in different contexts

MATLAB creates a copy of matrix variables every time it is passed in a function.
This operation is costly, and in C for efficiency reasons is avoided and the same memory is shared between
the caller and the callee. This allows for constant call time regardless of the dimensions of the input and subsequent optimization. 
This implies that if the function modifies the input matrix, the same change will be reflected also in the callee.

Example:

```
function y = f(y)
   y(1,1) = 10;
end

y1 = ones(2,2);
m1 = f(y1);
```

In MATLAB y1 is a 2x2 matrix full of ones, while m1 is a 2x2 matrix full of ones except the first element that contains 10.
In C both y1 and m1 do have the same value (because they share the same memory).

To avoid this situation simply rename the output variable to something different than y, so that the input values won't be changed.

The same problem may also appear in a script, where a variable is used many times in different contexts, sometimes as a matrix, and sometimes as a scalar.
To avoid ambiguities and translation problems we strongly reccommend to use different name variables for objects with different types.

Avoid reusing the same name for different variables, is also a good coding practice and improves readability.


## Syntax Errors and workarounds

Syntactical errors will be reported in the QSPcc output with line and character where the error occurred. 
The following valid MATLAB constructs were not fully supported in QSPcc, and will be reported by QSPcc as errors. 

- [Negative exponentiation without parenthesis](#negative-exponentiation-without-parenthesis)
- [Omission of leading zeros not supported](#omission-of-leading-zeros-not-supported)
- [Matrices with negative values have to be comma-separated](#matrices-with-negative-values-have-to-be-comma-separated)
- [tilde sign `~` not supported as an assignment variable](#tilde-sign--not-supported-as-an-assignment-variable)


### Negative exponentiation without parenthesis

The exponent of a negative value if not parenthesized will cause a syntax error

```
(1./(1+250000.^-(u(1))))
```

To fix it it's sufficient to add a parenthesis in this way

```
(1./(1+250000.^(-u(1))))
```

### Omission of leading zeros not supported

For example this will generate a translation error

```
[.3 .3 0]
```

while this will be tanslated regularly:

```
[0.3 0.3 0]
```

### Matrices with negative values have to be comma-separated 

For example

```
fp_approx = [0.1724    0.1787   -0.0818    0.2775];
```

will be interpreted as a 1x3 matrix with values 0.1724, 0.1787-0.0818, 0.2775
To fix this use commas to separate cells

```
fp_approx = [0.1724,    0.1787,   -0.0818,    0.2775];
```

### tilde sign `~` not supported as an assignment variable

In Matlab you can neglect a value returned from a function by assigning it to a tilde `~`, in this way:

```
[~,a] = fun(...)
```

This syntax has not been supported in QSPcc. To work around simply assign to an unused variable:

```
[not_used, a] = fun(...)
```

## Divergent dynamics, convergence errors or never-terminating simulations

The following general procedure to solve integration errors was distilled from hands-on experience at Fondazione The Microsoft Research - University of Trento Centre for Computational and Systems Biology (COSBI).
 
1. Try changing linear and non-linear solvers. QSPcc supports many linear solver implementations along with a default finite-difference banded preconditioner, see the [available solvers](#execution-options) for details.
2. If you cannot get your model to behave as expected, type `make clean`. Start a new [translation](#translation) and configure the execution to [save the timeseries](#save-timeseries-dynamics) with `make dump=1000`. Then execute again the translated program. This execution will generate a file **_sundials_y_timeseries.csv** containing the timeseries produced by the integrator, and a file **_sundials_dydt_timeseries.csv** containing the timeseries of the derivatives produced by the integrand function. These dynamics can be plotted using the `plot.sh` script, or a tool of your choice, to inspect which variables are behaving differently from what you expect. See the [advanced execution guide](#execution-options) for details on the options. 
   - According to plot insights, update the code to fix the misbehaving variables, or disable them using ad-hoc boolean flags to enable-disable portions of the code.
   - If you still have no clues about the equations that are misbehaving, you can always enable/disable portions of the code in a [dichotomic search](https://en.wikipedia.org/wiki/Dichotomic_search) using conditional code in your model to isolate the portion that is causing the anomaly. We have found that this often leads to pinpointing portions of the Matlab model that are still experimental, under development, or not completely refined. As described in the QSPcc's supplementary file, we found a model that Matlab is able to keep simulating with a large number of NAs in the dynamics.
   - re-compile, run and repeat this step 2. until you spot the issue.
3. if you are not able to find a specific portion of the model that is misbehaving, type `make clean` and again `make` adding an extra option `sanitychecks=on`, e.g. `make sanitychecks=on`. Then use [gdb](https://www.gnu.org/software/gdb/) or [lldb](https://lldb.llvm.org/) to debug the C code. This is also the first option for segmentation faults or other C-specific errors. We were often on this point at the early development of QSPcc. This action is not on our regular procedure anymore as the QSPcc compiler gets more mature.

If you still are in trouble we can help you. Get in touch with the [COSBI Bioinformatics lab](mailto:bioinformatics@cosbi.eu) for further informations.

