@echo off

rem ���ڃ��O�C������o�b�`�t�@�C��
rem ��������ƁA���ϐ�TOKEN�ɔF�؍�TOKEN��ݒ肷��B
rem ������T�[�o�[�̔F�؍�TOKEN���X�V����邽�߁Atoken.bat�̎g�p�𐄏�����B

set PORT=18080
set TOKENJSON=
set TOKEN=

set JSON=application/json

rem ���O�C��
curl -o TOKENRESP -X POST -H "Content-Type: %JSON%" -H "Accept: %JSON%" "http://localhost:%PORT%/kabusapi/token" -d "@req\login.txt"
for /F "delims="" tokens=1" %%i in (TOKENRESP) do set TOKENJSON=%%i
set TOKEN=%TOKENJSON:~25,-2%
echo TOKENJSON=%TOKENJSON%
echo TOKEN=%TOKEN%

del /Q TOKENRESP

