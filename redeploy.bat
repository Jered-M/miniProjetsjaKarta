@echo off
setlocal enabledelayedexpansion

REM Chemins
set JAVA_HOME=C:\zulu17.62.17-ca-jdk17.0.17-win_x64\zulu17.62.17-ca-jdk17.0.17-win_x64
set SRC=c:\Users\HP\Documents\NetBeansProjects\miniProjet\src\main\java
set CLASSES=c:\Users\HP\Documents\NetBeansProjects\miniProjet\target\classes
set WAR_ROOT=c:\Users\HP\Documents\NetBeansProjects\miniProjet\target\miniProjet-1.0

REM Nettoyage des anciennes classes
echo Nettoyage des classes compilees...
if exist "%CLASSES%\jakartamission" (
    rmdir /s /q "%CLASSES%\jakartamission"
)

REM Copier les sources et recompiler
echo Recompilation du projet...
cd /d "c:\Users\HP\Documents\NetBeansProjects\miniProjet"

REM Copier tous les fichiers source vers classes (comme ressources)
xcopy "%SRC%\*" "%CLASSES%\" /S /Y /EXCLUDE:*.class > nul

REM Recreer le WAR
echo Recreation du WAR...
del "target\miniProjet-1.0.war"
cd /d "%WAR_ROOT%"
"%JAVA_HOME%\bin\jar.exe" -cf "../miniProjet-1.0.war" .

echo Redemarrage de l'application...
cd /d "c:\Users\HP\Documents\NetBeansProjects\miniProjet"
"C:\glassfish-7.0.24\glassfish7\glassfish\bin\asadmin.bat" deploy --force "target\miniProjet-1.0.war"

echo Done!
