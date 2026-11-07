@echo off
cd /d "%~dp0"

if not exist out (
    echo No existe la carpeta out, compilando primero...
    call compilar.bat
    if errorlevel 1 exit /b 1
)

if "%~1"=="" (
    java -cp out demo.Main
) else (
    java -cp out %1
)
