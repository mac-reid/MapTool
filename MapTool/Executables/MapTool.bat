@ECHO off

IF "%1" == "" (
	echo "You must give an argument that specifies the file in which to run."
	Exit
)

cd ..
set topdir=%CD%
cd MapTool

for %%f in (UserInterface\*.java) do (

	javac -cp .;%topdir%\lwjgl-2.8.5\jar\*;%topdir% .\%%f
)

for %%f in (Backend\*.java) do (

	javac -cp .;%topdir%\lwjgl-2.8.5\jar\*;%topdir% .\%%f
)

java -cp .;%topdir%\lwjgl-2.8.5\jar\*;.\UserInterface\ -Djava.library.path=%topdir%\lwjgl-2.8.5\native\windows\ %1

del /f Backend\*.class
del /f UserInterface\*.class