@echo off

REM Establece la ruta de la JRE
set JAVA_HOME=.\JRE
set PATH=%JAVA_HOME%\bin;%PATH%

REM Muestra la versión de Java
java -version

REM Ejecuta los comandos de Gradle
start cmd /c ".\gradlew build && .\gradlew bootRun"

timeout /t 3

start "" "http://localhost:8080"

