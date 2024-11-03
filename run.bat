@echo off

REM Establece la ruta de la JRE que deseas usar
set JAVA_HOME=.\JRE
set PATH=%JAVA_HOME%\bin;%PATH%

REM Muestra la versión de Java
java -version

REM Ejecuta los comandos de Gradle
.\gradlew build && .\gradlew bootRun
