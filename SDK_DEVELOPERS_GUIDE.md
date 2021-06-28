# QSPcc Software Development Kit (SDK) overview

The QSPcc Software Development Kit (SDK) is succintly outlined here, allowing developers to build new QSPcc modules as source/target languages. Every presented source class is commented in a way that should make straightforward its usage and meaning.
You may also want to check if a GitHub [issue](https://github.com/cosbi-research/QSPcc/issues) has been filed, or get in touch with the Cosbi Bioinformatics lab led by lombardo@cosbi.eu. We'll be happy to help!

For a tour on how to contribute, please check out the [Contributing information](CONTRIBUTING.md).

# Table of Contents

* [Source organization and Dev environment](#source-organization-and-dev-environment)
* [Extesion scenarios](#extesion-scenarios)
   + [Add a new user-defined function](#add-a-new-user-defined-function)
   + [Add a new source language](#add-a-new-source-language)
   + [Add a new target language](#add-a-new-target-language)

# Source organization and Dev environment
The code is organized in 9 subprojects:
- _/SDK_: Contains the "glue" that ties together all the QSPcc parts. It contains the interfaces for the Frontend, the Backend and the Middleend, the Exceptions and other utilities.
- _/src/eu/cosbi/QSPcc_: Contains the [**CompilerController**](https://github.com/cosbi-research/QSPcc/blob/develop/src/eu/cosbi/qspcc/CompilerController.java) that phisically instantiate and wire together the different modules, as well as the junit tests.
- _/frontends/MatlabFrontend_: Contains the implementation for the MATLAB lexer/parser (exploiting antlr3)
- _/frontends/RFrontend_: Contains the implementation for the R lexer/parser (exploiting antlr4) **still BETA**
- _/frontends/DummyRFrontend_: Contains an hard-coded implementation of a sample R program. Can be useful for beginners who want to freely experiment with the tree, without the need for the lexer/parser machinery.
- _/MiddleEnd_: Contains the heart of the compler, the type inference part that translates an AST to a Type-annotated AST.
- _/backends/CBackend_: Contains the implementation for the C language generator
- _/backends/RBackend_: Contains the implementation for the R language generator
- _/backends/MISTBackend_: Contains the implementation for the C/MIST template generator, see the [documentation](https://github.com/cosbi-research/QSPcc/blob/develop/docs) for further informations.

In [Eclipse](https://www.eclipse.org/downloads/) you can import all of them at once through the menu:
File -> Import -> Existing projects into workspace -> select the QSPcc root directory and check "Search for nested project" -> Finish.


# Extesion scenarios

QSPcc can be extended to add support for:

- [New user-defined function](#add-a-new-user-defined-function) (for example in C): This allows users to translate models that use standard source language functions that are not implemented in the target language (for example some of the many variant of interp1 in MATLAB that are not supported in C)
- [New Source language](#add-a-new-source-language) (such as Python): This allows QSPcc to translate programs/models written in a language different from the ones already implemented, for example one may add support for python as a source language, and this automatically allows to re-use the QSPcc machinery to translate in any of the supported backends (such as C)
- [New Target Language](#add-a-new-target-language) (such as Python): This allows QSPcc to translate programs/models to a language different from the ones already implemented, for example one may add support for python as a target language, and this automatically allows to re-use the QSPcc machinery to translate any of the supported source languages in your newly defined target language.

To troubleshoot and fine-tune the C translations you can [enable C debugging](README.md#sanity-checks-for-c-code). 


## Add a new user-defined function

The simplest way to add an user-defined function is to implement it in your source language (such as MATLAB). This is convenient because it's a language you already know and QSPcc will automatically translate it in the target language for you.

On the other hand, if you already have a target language translation for the function you want to add (for example an optimization procedure in C), the simplest way to join the QSPcc translation with your manual implementation is 
to define a stub function in your source language (such as MATLAB) that just takes the required number of parameters and simply returns some constants with the right type, for example

```
function [e,f,g] = my_optimization_procedure_C(a,b,c,d)
   e = ones(b,d);
   f = 1;
   g = ones(a,c);
end
```

In this way QSPcc will detect the correct types for the parameters `a,b,c,d` and the return types `e,f,g` and will generate an equivalent target language stub function that does nothing.
After the translation you can edit the source code for `my_optimization_procedure_C` to adapt it to execute your C implementation.

Beware that this requires a bit of programming skills in the target language, as well as knowledge of the target language build process (for instance C is compiled through Makefiles, and if your custom procedure use another custom C library that library should be compiled in as well. To do this, the Makefile should be modified accordingly) so writing out the implementation in the source language is reccomended because less error-prone and easier.

## Add a new source language

This requires programming knowledge in java, a minimum familiarity with the `ant` build tool to compile your new source module, 
as well as understanding how a parser generator (we will make examples with `ANTLR`) works and what is and how to implement a formal language.

Implementing a new source language, basically means implementing a *parser* able to read the source files and translate them into an Abstract Syntax Tree (AST).
Usually this step is accomplished with the aid of a parser generator such as ANTLR, but also any other parser generator will work.
Here we will refer to ANTLR since it's the one used both for R and MATLAB source languages.

Once you implemented (or generated with ANTLR) your parser in Java, writing a QSPcc frontend module is a matter of subclassing an abstract class in the QSPcc SDK
named [**CompilerFrontend**](https://github.com/cosbi-research/QSPcc/blob/develop/SDK/src/eu/cosbi/qspcc/interfaces/CompilerFrontend.java), implementing it's abstract methods, 
and create a ant build file to compile your work in a QSPcc-ready frontend module (see an [example build file](https://github.com/cosbi-research/QSPcc/blob/develop/frontends/MatlabFrontend/build.xml)).

It may be necessary to extend the [**NodeType**](https://github.com/cosbi-research/QSPcc/blob/develop/SDK/src/eu/cosbi/qspcc/ast/NodeType.java) class with language-specific nodes, as well as [**ErrorMessage**](https://github.com/cosbi-research/QSPcc/blob/develop/SDK/src/eu/cosbi/qspcc/exceptions/ErrorMessage.java) with language-specific error messages.
One way could be to subclass **NodeType** and **ErrorMessage** or implement common java interfaces such as [**ErrorCode**](https://github.com/cosbi-research/QSPcc/blob/develop/SDK/src/eu/cosbi/qspcc/exceptions/ErrorCode.java).

Now that you have a general undestanding of the macro-steps involved let's break down this **CompilerFrontend** class abstract methods in detail.
The entry point of your source language is the class that implements **CompilerFrontend**, see the example MATLAB implementation [here](https://github.com/cosbi-research/QSPcc/blob/develop/frontends/MatlabFrontend/src/eu/cosbi/qspcc/frontend/Matlab.java).
Don't look at the variable types such as **IFunction**, **Program** and **GType**, we will cover them later.

```
public abstract IFunction[] globalFunctionsFor(String backendClassName);
```

This is the first function you should implement. It defines the list of core functions of the source language that are globally defined (es. `globalFunctionsFor("C")`), meaning a list of functions/variables that the user should not provide an implementation for.

```
public abstract Program walk(IFunction[] coreFunctions, List<Tuple<GType, String>> gvars)
	    throws SyntaxException, ListException;
```


This is the last (and main) function you should implement in your **CompilerFrontend** implementation, it's where all the parsing and AST generation happens.
It takes as input 
 - The array of globally defined variables/functions defined by **globalFunctionsFor**, the function you implemented before
 - A list of couple type/name with the list of global variables eventually specified through the `-i / --input` command-line switch.

And produces a graph of compilation units (called [**Program**](https://github.com/cosbi-research/QSPcc/blob/develop/SDK/src/eu/cosbi/qspcc/ast/Program.java)) 
where the nodes are the input language files, named compilation units ([**AAST**](https://github.com/cosbi-research/QSPcc/blob/develop/SDK/src/eu/cosbi/qspcc/ast/AAST.java)) 
and the edges are the caller/callee relationships between the compilation units, meaning that if source file `main.m` calls the function `f` defined in `f.m` the **Program** graph
will contain an edge between the **AAST** `main` and the **AAST** `f`.

Producing a **Program** instance is a matter of walking your AST generated through your own parser, and generating for each file a corresponding **AAST** and single **Program** that links them.
You can see an example implementation for the MATLAB language [here](https://github.com/cosbi-research/QSPcc/blob/develop/frontends/MatlabFrontend/src/eu/cosbi/qspcc/frontend/Matlab.java#L44).

You can also look at [the ANTLR walk implementation for MATLAB](https://github.com/cosbi-research/QSPcc/blob/develop/frontends/MatlabFrontend/src/eu/cosbi/qspcc/frontend/MatlabCompilationUnit.java) and the [AAST implementation for MATLAB](https://github.com/cosbi-research/QSPcc/blob/develop/frontends/MatlabFrontend/src/eu/cosbi/qspcc/frontend/MatlabAAST.java) for a practical example.

Now that we have seen what **Program** and **AAST** are and what they are used for, we caan dive into a slightly finer granularity. Each **AAST** can be viewed as a graph of **AASTNode** where each **AASTNode** represents a portion of the code (with positional information) in the source file that **AAST** refers to, and in practice often corresponds to a node in the **AST** generated by your parser.

For example in the MATLAB frontend [**CommonTree**](https://github.com/cosbi-research/QSPcc/blob/develop/frontends/MatlabFrontend/src/eu/cosbi/qspcc/frontend/MatlabCompilationUnit.java#L46) is a custom node in the MATLAB parser output, and the **MatlabCompilationUnit** translates it to an equivalent **AASTNode** trough the subclass [**GenericMatlabTree**](https://github.com/cosbi-research/QSPcc/blob/develop/frontends/MatlabFrontend/src/eu/cosbi/qspcc/frontend/GenericMatlabTree.java).
This is an [example](https://github.com/cosbi-research/QSPcc/blob/develop/frontends/MatlabFrontend/src/eu/cosbi/qspcc/frontend/MatlabCompilationUnit.java#L210) of this translation code.

To conclude our overview, we should discuss the types **GType** (and subclasses) and **IFunction**.

### GType

GType stands for Generic Type and is the base class that represent a type that can be assigned to a variable.
It has a list of subclasses, one for each supported type. See the full list [here](https://github.com/cosbi-research/QSPcc/tree/develop/SDK/src/eu/cosbi/qspcc/expressions/type).
A type is never instantiated directly, but always through the `GType.get` factory methods that accepts a [`BType` enum](https://github.com/cosbi-research/QSPcc/blob/develop/SDK/src/eu/cosbi/qspcc/expressions/type/TypeDefinition.java#L4). Each enum value corresponds to a type implementation. 
Extra parameters are passed as parameters to `GType.get`, see an example [here](https://github.com/cosbi-research/QSPcc/blob/develop/middleend/src/eu/cosbi/qspcc/statements/ExpressionStatement.java#L216) for the definition of a scalar type (with value).

### IFunction

The [`IFunction`](https://github.com/cosbi-research/QSPcc/blob/develop/SDK/src/eu/cosbi/qspcc/interfaces/CompilerFrontend.java#L37) interface is defined in the **CompilerFrontend** abstract class itself, and defines the potentially polimorphic behaviour of a source language function. It's purpose is to define the expected input/output types for fixed function, given a program context.
Let's break down `IFunction` into it's main interface methods. For the complete list and for a description of all the methods see the [source code](https://github.com/cosbi-research/QSPcc/blob/develop/SDK/src/eu/cosbi/qspcc/interfaces/CompilerFrontend.java#L37) and [an example implementation for the MATLAB function `ode15s`](https://github.com/cosbi-research/QSPcc/blob/develop/frontends/MatlabFrontend/src/eu/cosbi/qspcc/frontend/corefunction/M2CFunction.java#L1188).

```
public List<GType> getParamTypes();
```

This is the default list of parameter types (will be overridden by the output of `paramTypesUpdater()` if it's not null). 

```
BiFunction<AASTNode, List<GType>, List<GType>> returnTypeUpdater();
```

This function returns a function that takes as input an **AASTNode** with **NodeType** APPLY (meaning a node representing a function call) and a list of intput types, and returns an array of alternative return types, if polimorphism is not needed, the return type list will contain a single argument. If the return type is fixed, and doesn't depend on the context, this method will return null.

```
BiFunction<AASTNode, List<GType>, List<GType>> paramTypesUpdater();
```

The same as before, but for formal parameter types, that can be updated based on actual parameter types. Return null if not needed.

## Add a new target language

Adding a new target language, basically means writing a *generator* that translates a typed **AAST** provided by QSPcc into source code in the target language.
Writing a QSPcc backend module is a matter of subclassing an abstract class in the QSPcc SDK
named [**CompilerBackend**](https://github.com/cosbi-research/QSPcc/blob/develop/SDK/src/eu/cosbi/qspcc/interfaces/CompilerBackend.java), implementing it's abstract methods, 
and create a ant build file to compile your work in a QSPcc-ready frontend module (see [here](https://github.com/cosbi-research/QSPcc/blob/develop/backends/CBackend/build.xml) for an example build file).
A new backend instance will be instantiated for every **AAST** so each backend is responsible to translate his own compilation unit.

Let's break down the main abstract methods to be implemented, for a full description of all the methods refer to the [source code](https://github.com/cosbi-research/qsp-cc/blob/develop/SDK/src/eu/cosbi/qspcc/interfaces/CompilerBackend.java).

```
public abstract void onTranslationStart(Program programName) throws Exception;
```

**onTranslationStart** is called just before code generation starts. 

```
public abstract void onTranslationEnd(Program programName, List<String> fun_names) throws Exception;
```

**onTranslationEnd** is called after code generation. 

```
public void prepareOutput();
```

**prepareOutput** is responsible to flush to disk output files.

```
public void finalizeOutput();
```

**finalizeOutput** is responsible to flush to disk output files.


```
public void translate(AAST program, boolean stopOnError) throws GException;
```

**translate** is called to perform code translation. Usually it walks the **AAST** (or compilation unit) bottom-up and prepares the generation of the output.

```
public void genCode(AAST program) throws TypeException, UnboundException, SyntaxException;
```
Companion method for `translate` will phisically write to disk the buffers filled by translate.

All these methods will be called in the following order:
 - onTranslationStart: only called in the backend instance that represents the entry point for the translated program.
 - prepareOutput: called for each backend before translation starts
 - translate: called for each backend instance
 - genCode: called for each backend instance
 - finalizeOutput: called for each backend instance
 - onTranslationEnd: only called in the backend instance that represents the entry point for the translated program, when all the backend instances completed the call to finalizeOutput().
