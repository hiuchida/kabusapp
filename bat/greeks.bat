@echo off

rem �c����Greeks���W�v����o�b�`�t�@�C��

set PORT=18080
set TOKEN=
set TARGET_HOME=..\target

call setenv.bat %TARGET_HOME%\lib

echo.
set CLASSPATH=%TARGET_HOME%\classes;%TARGET_HOME%\swagger-java-client-1.0.0.jar;%CP%
echo CLASSPATH=%CLASSPATH%

echo.
java -cp %CLASSPATH% v4.MainGreeks

