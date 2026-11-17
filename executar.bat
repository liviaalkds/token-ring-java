@echo off

set "operacao=%~1"
if "%operacao%"=="" set "operacao=0"
set "voltas=%~2"
if "%voltas%"=="" set "voltas=1"

start "" java Escravo 1 %operacao% %voltas%
start "" java Escravo 2 %operacao% %voltas%
start "" java Escravo 3 %operacao% %voltas%
timeout /t 1 >nul
java Mestre 0 %operacao% %voltas%
