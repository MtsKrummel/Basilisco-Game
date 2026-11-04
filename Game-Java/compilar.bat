@echo off
if exist out rmdir /s /q out
mkdir out
dir /s /b src\*.java > fuentes.txt
javac -encoding UTF-8 -d out @fuentes.txt
del fuentes.txt
if not exist out\juego\pantallas mkdir out\juego\pantallas
copy /y src\juego\pantallas\*.png out\juego\pantallas\ >nul
