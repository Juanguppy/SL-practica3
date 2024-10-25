@echo off
:: Definir las rutas a las carpetas de capturas y transcripciones
set "capturas=capturas"
set "transcripciones=transcripcion"
set "tesseract=.\tesseract.exe"

:: Comprobar que las carpetas existen
if not exist "%capturas%" (
    echo La carpeta de capturas no existe.
    exit /b
)
if not exist "%transcripciones%" (
    echo La carpeta de transcripciones no existe.
    exit /b
)

:: Procesar cada archivo en la carpeta de capturas
for %%f in (%capturas%\*.png) do (
    setlocal enabledelayedexpansion
    set "nombre_archivo=%%~nf"
    set "transcripcion=%transcripciones%\!nombre_archivo!.txt"

    :: Comprobar si existe la transcripción correspondiente
    if exist "!transcripcion!" (
        echo Procesando !nombre_archivo!.png con transcripcion !transcripcion!
        "%tesseract%" "%%f" "!nombre_archivo!" -l eng --psm 6 makebox
    ) else (
        echo Advertencia: No se encontró transcripción para %%f
    )
    endlocal
)

echo Completado.
pause
