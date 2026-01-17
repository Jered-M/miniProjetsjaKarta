@echo off
REM Script pour initialiser la base de données Derby

setlocal enabledelayedexpansion

REM Variables
set DERBY_HOME=C:\derby-10.16.1.1
set JAVA_HOME=C:\Program Files\Java\jdk-11
set DB_DIR=%cd%\miniDB
set SQL_FILE=%cd%\src\main\resources\META-INF\init-derby.sql

REM Vérifier si Derby existe
if not exist "%DERBY_HOME%\lib\derby.jar" (
    echo Derby n'a pas été trouvé dans %DERBY_HOME%
    echo Télécharge Derby depuis https://db.apache.org/derby/
    exit /b 1
)

REM Créer le répertoire de la base de données
if not exist "%DB_DIR%" mkdir "%DB_DIR%"

REM Exécuter le script SQL
echo Création de la base de données Derby...
cd "%DB_DIR%"
"%JAVA_HOME%\bin\java" -cp "%DERBY_HOME%\lib\derby.jar;%DERBY_HOME%\lib\derbytools.jar" org.apache.derby.tools.ij "%SQL_FILE%"

if %ERRORLEVEL% EQU 0 (
    echo Base de données créée avec succès!
) else (
    echo Erreur lors de la création de la base de données
    exit /b 1
)

endlocal
