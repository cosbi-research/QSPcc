# Contributing to QSPcc repository <!-- omit in toc -->

## Getting started <!-- omit in toc -->

Before you begin, you may want to check if a GitHub [issue](https://github.com/cosbi-research/QSPcc/issues) has been filed.

### Contributing to the code

QSPcc is written in Java so knowing the language is a pre-requisite. To enhance the existing code, add a new frontend or a new backend you need to have a good understanding of both the source language (ex. MATLAB) and the target language (ex. C) you wish to add or update.

An understanding of formal language parsing, type inference and code generation strategies will also help you modifying the frontend, the middleend and the backend respectively (see [dragon book](https://en.wikipedia.org/wiki/Compilers:_Principles,_Techniques,_and_Tools) for details).
As an example, the parsing technology powering the MATLAB frontend is [antlr3](https://www.antlr3.org/) but that's not mandatory.

If you feel you are ready, start digging into the codebase starting from the [SDK developer guide](SDK_DEVELOPERS_GUIDE.md),
and don't forget to check to see if an [issue exists](https://github.com/cosbi-research/QSPcc/issues) already for the change you want to make.

When you think you are ready to make a change, fork the repo.
When you think you completed your work, please open a pull request with a clear list of what you've done (read more about [pull requests](https://docs.github.com/en/github/collaborating-with-pull-requests/proposing-changes-to-your-work-with-pull-requests/about-pull-requests)).
If you have merge conflicts or need help with git usage, check out this [git tutorial](https://lab.github.com/githubtraining/managing-merge-conflicts).

### Contributing to the tests

You don't know about java or compilers but you love QSPcc and want to add a new test case in which QSPcc saved your day?
Please submit a pull request with the new model under [tests](https://github.com/cosbi-research/QSPcc/tree/develop/tests) folder with a clear list of what you've done (read more about [pull requests](https://docs.github.com/en/github/collaborating-with-pull-requests/proposing-changes-to-your-work-with-pull-requests/about-pull-requests)).
A new model should have a `README.md` file describing the project, how QSPcc helped you and further bibliographic references if available.
The model structure is free, except that an entry-point script named `main` that measures the total run-time and print it out to the console at the end of the execution should exists.

### Contributing to the documentation

You don't know about java or compilers but you have found an error in the documentation or feel it could be improved?
Please submit a pull request with the updated [README.md](README.md) (or others if you feel comfortable),
and don't forget to check if an [issue exists](https://github.com/cosbi-research/QSPcc/issues) already for the change you want to make.


