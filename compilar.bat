@echo off
setlocal enabledelayedexpansion
cd /d "%~dp0"

if exist out rmdir /s /q out
mkdir out
if exist fuentes.txt del fuentes.txt

rem Lista de fuentes entre comillas y con "/" (javac trata "\" como escape en @archivos)
for /r src %%f in (*.java) do (
    set "ruta=%%f"
    echo "!ruta:\=/!">>fuentes.txt
)

javac -encoding UTF-8 -d out @fuentes.txt
if errorlevel 1 (
    del fuentes.txt
    echo.
    echo ERROR: la compilacion fallo.
    exit /b 1
)
del fuentes.txt

rem Copiar recursos (todo lo que no sea .java) a out\
robocopy src out /E /XF *.java /NFL /NDL /NJH /NJS /NP >nul
if errorlevel 8 (
    echo ERROR: no se pudieron copiar los recursos.
    exit /b 1
)

echo Compilacion terminada. Ejecuta ejecutar.bat
exit /b 0
