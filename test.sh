#!/bin/bash

# the current location where the qspcc folder were extracted
# usually this shouldn't be changed
export TRANSLATOR_HOME=$(pwd)

set -e 

source "$TRANSLATOR_HOME"/scripts/env.sh

"$TRANSLATOR_HOME"/scripts/clean.sh

echo "Automatic tests will be performed in three phases: "
echo "1) source-to-source translation with QSPcc (eg MATLAB to C) "
echo "2) compilation from C to optimized binary"
echo "3) execution of the binary model + timeseries plots"
echo ""
echo "#### PHASE 1: source-to-source translation with QSPcc "
echo ""

"$TRANSLATOR_HOME"/scripts/translate_tests.sh

echo "#### PHASE 2 and 3: compilation and execution + timeseries plots"
echo ""

"$TRANSLATOR_HOME"/scripts/execute_c.sh


echo ""
echo "All tests completed successfully"
