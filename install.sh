#!/bin/bash

set -e

DOCKERIZED=${2:-no}
INSTALL_PREFIX=${1:-/usr/local}
INSTALL_PATH=$INSTALL_PREFIX/qspcc
LIB_PATH=$INSTALL_PATH/lib
DIST_PATH=$INSTALL_PATH/dist
BIN_PATH=$INSTALL_PREFIX/bin
QSPCC_USER=$(whoami)

install -d $INSTALL_PATH
install -d $LIB_PATH
install -d $DIST_PATH
install -d $BIN_PATH

find lib -type f -exec install -b -m 755 {} $LIB_PATH \;
find dist -type f -exec install -b -m 755 {} $DIST_PATH \;

# install in bin folder
sed -i'.orig' -e "s|TRANSLATOR_HOME=.*|TRANSLATOR_HOME=\"$INSTALL_PATH\"|" qspcc
sed -i'.orig' -e "s|DOCKERIZED=.*|DOCKERIZED=\"$DOCKERIZED\"|" qspcc

install -b -m 755 qspcc.bat $INSTALL_PATH
install -b -m 755 qspcc $INSTALL_PATH

mkdir -p $INSTALL_PATH/logs
chmod 755 $INSTALL_PATH/logs
if [ "$DOCKERIZED" == "yes" ]; then
	chown qspcc:qspcc $INSTALL_PATH/logs
else
	chown $QSPCC_USER $INSTALL_PATH/logs
fi
# add to path
ln -sf $INSTALL_PATH/qspcc $BIN_PATH/qspcc

