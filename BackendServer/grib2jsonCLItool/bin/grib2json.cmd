@echo off
set "LIB_DIR=%~dp0..\lib"
for /f "delims=" %%i in ('dir /b "%LIB_DIR%\grib2json-*.jar"') do set "LAUNCH_JAR=%LIB_DIR%\%%i"
"C:\Program Files\Eclipse Adoptium\jdk-11.0.22.7-hotspot\bin\java.exe" -Xmx512M -jar "%LAUNCH_JAR%" %*
