#!/bin/bash

"$TRANSLATOR_HOME"/scripts/clean.sh

CLASSPATH="${TRANSLATOR_HOME}/lib/reflections-0.9.10.jar${PATHSEP}${TRANSLATOR_HOME}/lib/guava-19.0.jar${PATHSEP}${TRANSLATOR_HOME}/lib/javassist-3.22.0-GA.jar${PATHSEP}${TRANSLATOR_HOME}/lib/junit-4.12.jar${PATHSEP}${TRANSLATOR_HOME}/lib/hamcrest-core-1.3.jar${PATHSEP}${TRANSLATOR_HOME}/lib/parameterized-suite-1.1.0.jar${PATHSEP}${TRANSLATOR_HOME}/lib/antlr-3.5-complete.jar${PATHSEP}${TRANSLATOR_HOME}/lib/commons-io-2.5.jar${PATHSEP}${TRANSLATOR_HOME}/lib/disruptor-3.4.2.jar${PATHSEP}${TRANSLATOR_HOME}/lib/log4j-api-2.16.0.jar${PATHSEP}${TRANSLATOR_HOME}/lib/log4j-core-2.16.0.jar${PATHSEP}${TRANSLATOR_HOME}/dist/SDK.jar${PATHSEP}${TRANSLATOR_HOME}/dist/MiddleEnd.jar${PATHSEP}${TRANSLATOR_HOME}/dist/CBackendExtension.jar${PATHSEP}${TRANSLATOR_HOME}/dist/CBackend.jar${PATHSEP}${TRANSLATOR_HOME}/dist/RBackend.jar${PATHSEP}${TRANSLATOR_HOME}/dist/MISTBackend.jar${PATHSEP}${TRANSLATOR_HOME}/dist/MatlabFrontend.jar${PATHSEP}${TRANSLATOR_HOME}/dist/QSPcc.jar${PATHSEP}${TRANSLATOR_HOME}/dist/QSPccTests.jar"

java -cp "$CLASSPATH" org.junit.runner.JUnitCore eu.cosbi.qspcc.tests.TranslatorTestSuite 

