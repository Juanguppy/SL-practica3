@echo off

REM Establece la ruta de la JRE
set JAVA_HOME=.\JRE
set PATH=%JAVA_HOME%\bin;%PATH%

REM Muestra la versión de Java
java -version

start cmd /k "java -jar .\database-sl-grupo5-2024-SNAPSHOT.jar"

timeout /t 5

start "" "http://localhost:8080"

