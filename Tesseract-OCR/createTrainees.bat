@echo off
:: Definir las rutas a las carpetas de capturas y trainees
set "capturas=capturas"
set "trainees=trainees"
set "tesseract=.\tesseract.exe"

:: Comprobar que las carpetas existen
if not exist "%capturas%" (
    echo La carpeta de capturas no existe.
    exit /b
)

:: Crear la carpeta de salida para los archivos .tr si no existe
if not exist "%trainees%" (
    mkdir "%trainees%"
)

:: Procesar cada archivo en la carpeta de capturas
for %%f in (%capturas%\*.png) do (
    setlocal enabledelayedexpansion
    set "nombre_archivo=%%~nf"
    set "archivo_box=%capturas%\!nombre_archivo!.box"
    set "salida_tr=%trainees%\!nombre_archivo!"

    :: Comprobar si existe el archivo .box correspondiente en la misma carpeta
    if exist "!archivo_box!" (
        echo Generando archivo .tr para !nombre_archivo!.png usando !archivo_box!
        "%tesseract%" "%%f" "!salida_tr!" nobatch box.train
    ) else (
        echo Advertencia: No se encontró archivo .box para %%f
    )
    endlocal
)

echo Completado.
pause
