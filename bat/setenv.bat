@echo off

rem 環境変数CPを設定するバッチファイル
rem ディレクトリ構成
rem   \bat            このバッチファイル
rem   \target         ビルド生成物のベースパス
rem   \target\classes eclipseでビルドされたクラスファイル
rem   \target\lib     mavenで収集された依存ライブラリファイル

echo setenv.bat start
echo JAVA_HOME=%JAVA_HOME%
echo JRE_HOME=%JRE_HOME%

set LIB_HOME=%1
echo LIB_HOME=%LIB_HOME%

set CP=.

for %%i in (%LIB_HOME%\*.jar) do call :setcp %%i
echo CP=%CP%
echo setenv.bat end

exit /b

:setcp
set CP=%CP%;%1
exit /b
