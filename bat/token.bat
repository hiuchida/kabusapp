@echo off

rem ���O�C������o�b�`�t�@�C��
rem ��������ƁA���ϐ�TOKEN�ɔF�؍�TOKEN��ݒ肷��B
rem �F�؍�TOKEN�������ɂȂ�Ȃ�����A�g���܂킷�B
rem �t�@�C�����b�N�͉�������邽�߁A���œ��삵�Ă���PGM�ɂ���ĔF�؍�TOKEN���X�V�����\��������B

set PORT=18080
set TOKEN=
set TARGET_HOME=..\target

call setenv.bat %TARGET_HOME%\lib

echo.
set CLASSPATH=%TARGET_HOME%\classes;%TARGET_HOME%\swagger-java-client-1.0.0.jar;%CP%
echo CLASSPATH=%CLASSPATH%

echo.
java -cp %CLASSPATH% v4.MainLogin

echo.
for /F "delims="" tokens=1" %%i in (\tmp\LockedAuthorizedToken.txt) do set TOKEN=%%i
echo TOKEN=%TOKEN%

