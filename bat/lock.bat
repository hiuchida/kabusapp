@echo off

rem �t�@�C�����b�N����o�b�`�t�@�C��
rem �G���^�[�L�[���������܂ŁA�t�@�C�����b�N�Ǘ��p�����b�N���A����PGM����A�N�Z�X�ł��Ȃ�����B

set PORT=18080
set TARGET_HOME=..\target

call setenv.bat %TARGET_HOME%\lib

echo.
set CLASSPATH=%TARGET_HOME%\classes;%TARGET_HOME%\swagger-java-client-1.0.0.jar;%CP%
echo CLASSPATH=%CLASSPATH%

echo.
java -cp %CLASSPATH% v4.MainLock

