@echo off
REM Script pour compiler et déployer avec NetBeans

echo ===================================
echo Compilation du projet miniProjet
echo ===================================

cd /d C:\Users\HP\Documents\NetBeansProjects\miniProjet

REM Chercher Java
for /f "delims=" %%i in ('where java 2^>nul') do set JAVA_HOME=%%i
if "%JAVA_HOME%"=="" (
    echo Erreur: Java n'a pas été trouvé
    exit /b 1
)

echo Java trouvé: %JAVA_HOME%

REM Chercher GlassFish
set GLASSFISH_HOME=C:\glassfish7
if not exist "%GLASSFISH_HOME%\bin\asadmin.bat" (
    echo Erreur: GlassFish n'a pas été trouvé dans %GLASSFISH_HOME%
    echo Configure le chemin correct et réessaye
    exit /b 1
)

echo GlassFish trouvé: %GLASSFISH_HOME%

REM Créer le répertoire build s'il n'existe pas
if not exist "target" mkdir target

REM Compiler le projet
echo.
echo Compilation du projet...
call "%GLASSFISH_HOME%\bin\asadmin.bat" deploy --force=true "target/miniProjet-1.0.war" 2>nul

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ✓ Projet compilé et déployé avec succès!
    echo ✓ La base de données s'initialise automatiquement
    echo.
    echo Accède à l'application:
    echo http://localhost:8080/miniProjet-1.0/
) else (
    echo.
    echo ✗ Erreur lors du déploiement
    echo Essaye avec NetBeans:
    echo 1. Ouvre NetBeans
    echo 2. Charge le projet
    echo 3. Clic-droit -> Build
    echo 4. Clic-droit -> Deploy
)

pause
