#!/bin/bash

PROGRAM_NAME="QSPcc"
DIST_FOLDER="dist"
TEST_PROGRAM_NAME="QSPccTests"
FRONTEND_FOLDER="frontends"
BACKEND_FOLDER="backends"
MIDDLEEND_FOLDER="middleend"
SDK_FOLDER="SDK"

mkdir -p $DIST_FOLDER

cd $SDK_FOLDER
echo "-------------------- BUILDING SDK -------------------"
ant
rc=$?
if [[ $rc != 0 ]]; then
	echo "FAILED TO BUILD SDK"
	exit $rc
fi
cd -

# needed to compile
cp $SDK_FOLDER/build/jar/SDK.jar lib
# store in distribution folder
cp $SDK_FOLDER/build/jar/SDK.jar $DIST_FOLDER

echo "------------------ BUILDING MIDDLE-END --------------"
cp $SDK_FOLDER/build/jar/SDK.jar $MIDDLEEND_FOLDER/lib
cd $MIDDLEEND_FOLDER
ant
rc=$?
cd -
if [[ $rc != 0 ]]; then
                echo "FAILED TO BUILD MIDDLE-END"
                exit $rc
fi
cp $MIDDLEEND_FOLDER/build/jar/MiddleEnd.jar $DIST_FOLDER

echo "------------------ BUILDING FRONTENDS --------------"

for frontend in $(ls -1 $FRONTEND_FOLDER); do
	echo "----------- BUILDING $frontend -------------"
	cp $SDK_FOLDER/build/jar/SDK.jar $FRONTEND_FOLDER/$frontend/lib
	cd $FRONTEND_FOLDER/$frontend
	ant
	rc=$?
	cd -
	if [[ $rc != 0 ]]; then
        	echo "FAILED TO BUILD $frontend"
        	continue
	fi
	# store in distribution folder
	cp $FRONTEND_FOLDER/$frontend/build/jar/$frontend.jar $DIST_FOLDER
done

echo "------------------ BUILDING BACKENDS --------------"

for backend in $(ls -1 $BACKEND_FOLDER); do
        echo "----------- BUILDING $backend -------------"
	cp $SDK_FOLDER/build/jar/SDK.jar $BACKEND_FOLDER/$backend/lib
        cd $BACKEND_FOLDER/$backend
        ant
        rc=$?
	cd -
        if [[ $rc != 0 ]]; then
                echo "FAILED TO BUILD $backend"
                continue
        fi
	# store in distribution folder
	cp $BACKEND_FOLDER/$backend/build/jar/$backend.jar $DIST_FOLDER
done

echo "-------------------- BUILDING LANGUAGE TRANSLATOR -------------------"
ant
rc=$?
if [[ $rc != 0 ]]; then
        echo "FAILED TO BUILD LANGUAGE TRANSLATOR"
        exit $rc
fi
# store in distribution folder
cp build/jar/$PROGRAM_NAME.jar $DIST_FOLDER

# store in distribution folder
cp build/jar/$TEST_PROGRAM_NAME.jar $DIST_FOLDER

# cleanup
rm lib/SDK.jar
