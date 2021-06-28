#!/bin/bash

INSTALL_PREFIX=${1:-/usr/local}

rm -rf $INSTALL_PREFIX/qspcc
rm -f $INSTALL_PREFIX/bin/qspcc 2> /dev/null
rm -f $INSTALL_PREFIX/bin/qspcc-tests 2> /dev/null
