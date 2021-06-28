@ECHO OFF

REM Utility script to run languageTranslator command line



SET classpath=lib/reflections-0.9.10.jar;lib/guava-19.0.jar;lib/javassist-3.22.0-GA.jar;lib/commons-cli-1.4.jar;lib/antlr-3.5-complete.jar;lib/commons-io-2.5.jar;lib/log4j-api-2.7.jar;lib/log4j-core-2.7.jar;dist/SDK.jar;dist/MiddleEnd.jar;dist/CBackend.jar;dist/RBackend.jar;dist/MISTBackend.jar;dist/MatlabFrontend.jar;dist/QSPcc.jar


java -cp %classpath% eu.cosbi.qspcc.start.Starter Windows %~dp0 no %*
