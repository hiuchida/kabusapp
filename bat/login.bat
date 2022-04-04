@echo off

rem 直接ログインするバッチファイル
rem 成功すると、環境変数TOKENに認証済TOKENを設定する。
rem ※毎回サーバーの認証済TOKENが更新されるため、token.batの使用を推奨する。

set PORT=18080
set TOKENJSON=
set TOKEN=

set JSON=application/json

rem ログイン
curl -o TOKENRESP -X POST -H "Content-Type: %JSON%" -H "Accept: %JSON%" "http://localhost:%PORT%/kabusapi/token" -d "@req\login.txt"
for /F "delims="" tokens=1" %%i in (TOKENRESP) do set TOKENJSON=%%i
set TOKEN=%TOKENJSON:~25,-2%
echo TOKENJSON=%TOKENJSON%
echo TOKEN=%TOKEN%

del /Q TOKENRESP

