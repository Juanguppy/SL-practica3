@echo off

java -version

.\gradlew build && .\gradlew bootRun
REM Ejecuta los comandos de Gradle
REM start cmd /c ".\gradlew build && .\gradlew bootRun"

REM timeout /t 5

REM start "" "http://localhost:8080"

