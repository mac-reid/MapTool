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

foreach file ($uiFiles) 

	javac -d $topdir/bin -cp .:$jar/org.eclipse.swt_3.100.0.v4236b.jar:$jar/org.eclipse.swt.gtk.linux.x86_3.100.1.v4236b.jar:$jar/slick.jar:$jar/lwjgl.jar:$topdir $file
end

foreach file ($backendFiles)

	javac -d $topdir/bin -cp .::$jar/org.eclipse.swt_3.100.0.v4236b.jar:$jar/org.eclipse.swt.gtk.linux.x86_3.100.1.v4236b.jar:$jar/slick.jar:$jar/lwjgl.jar:$topdir $file
end

echo "hi"

java -cp \
 .:$jar/org.eclipse.swt_3.100.0.v4236b.jar:$jar/org.eclipse.swt.gtk.linux.x86_3.100.1.v4236b.jar:$jar/slick.jar:$jar/lwjgl.jar:./UserInterface/:./Backend:$topdir/bin \
 -Djava.library.path=$topdir/lwjgl-2.8.5/native/linux $1

 echo "wa" 