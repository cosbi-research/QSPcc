#!/bin/bash
# TO BE EVENTUALLY EXECUTED AFTER INSTALL

set -e

DOCKERIZED=${2:-no}
INSTALL_PREFIX=${1:-/usr/local}
INSTALL_PATH=$INSTALL_PREFIX/qspcc
EXAMPLES_PATH=$INSTALL_PATH/tests
SCRIPTS_PATH=$INSTALL_PATH/scripts
BIN_PATH=$INSTALL_PREFIX/bin
QSPCC_USER=$(whoami)

install -d $EXAMPLES_PATH
install -d $SCRIPTS_PATH

find scripts -type f -exec install -b -m 755 {} $SCRIPTS_PATH \;
for file in $(find tests -type f); do
	install -d $INSTALL_PATH/$(dirname $file)
	install -b -m 644 $file $INSTALL_PATH/$(dirname $file)
done


sed -i'.orig' -e "s|TRANSLATOR_HOME=.*|TRANSLATOR_HOME=\"$INSTALL_PATH\"|" test.sh

install -b -m 755 test.sh $INSTALL_PATH/qspcc-tests

if [ "$DOCKERIZED" == "yes" ]; then
	chown qspcc:qspcc $EXAMPLES_PATH
else
	chown $QSPCC_USER $EXAMPLES_PATH
fi
# add to path
ln -sf $INSTALL_PATH/qspcc-tests $BIN_PATH/qspcc-tests

