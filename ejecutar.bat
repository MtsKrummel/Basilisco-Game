@echo off
if "%~1"=="" (
    java -cp out demo.Main
) else (
    java -cp out %1
)
