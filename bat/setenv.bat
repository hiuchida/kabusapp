@echo off

rem ���ϐ�CP��ݒ肷��o�b�`�t�@�C��
rem �f�B���N�g���\��
rem   \bat            ���̃o�b�`�t�@�C��
rem   \target         �r���h�������̃x�[�X�p�X
rem   \target\classes eclipse�Ńr���h���ꂽ�N���X�t�@�C��
rem   \target\lib     maven�Ŏ��W���ꂽ�ˑ����C�u�����t�@�C��

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
