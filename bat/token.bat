@echo off

rem ログインするバッチファイル
rem 成功すると、環境変数TOKENに認証済TOKENを設定する。
rem 認証済TOKENが無効にならない限り、使いまわす。
rem ファイルロックは解除されるため、裏で動作しているPGMによって認証済TOKENが更新される可能性がある。

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

