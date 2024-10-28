@echo off
setlocal enabledelayedexpansion

REM Archivo de salida
set output=salida.txt
REM Limpia el archivo de salida
> %output% echo.

REM Itera sobre todos los archivos .txt en el directorio actual
for %%f in (*.txt) do (
    set lineNum=0
    for /f "tokens=2" %%a in (%%f) do (
        set /a lineNum+=1
        REM Ignora la primera y última línea
        if !lineNum! gtr 1 (
            set line=!line!
        )
        REM Si es la última línea, salta
        if !lineNum! equ 1 (
            set line=!line!
        ) else (
        REM Escribe la segunda columna en el archivo de salida
            echo %%a >> %output%
        )
    )
)

echo Columna 2 de cada archivo almacenada en %output%
