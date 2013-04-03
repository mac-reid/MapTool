#!/bin/tcsh

if ( "$1" == "" ) then
	echo "You must give an argument that specifies the file in which to run."
	exit 2
endif

cd ..
set topdir = `pwd`
cd MapTool

set jar = $topdir/lwjgl-2.8.5/jar
set uiFiles = `ls ./UserInterface/*.java`
set backendFiles = `ls ./Backend/*java`

#foreach file ($uiFiles) 

#	javac -cp .:$jar/slick.jar:$jar/lwjgl.jar:$topdir $file
#end

foreach file ($backendFiles)

	javac -cp .:$jar/slick.jar:$jar/lwjgl.jar:$topdir $file
end

java -cp .:$jar/slick.jar:$jar/lwjgl.jar:./UserInterface/:./Backend -Djava.library.path=$topdir/lwjgl-2.8.5/native/linux $1

#rm -f ./UserInterface/*.class
rm -f ./Backend/*.class