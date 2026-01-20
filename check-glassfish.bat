@echo off
REM Script pour vérifier et démarrer le serveur Derby

echo.
echo ========================================
echo Vérification du serveur Derby
echo ========================================
echo.

REM Cherche le répertoire Glassfish
for /d %%D in ("C:\Program Files*\glassfish*" "C:\Users\*\AppData\Local\*glassfish*") do (
    if exist "%%D\bin\asadmin.bat" (
        echo Trouvé Glassfish: %%D
        echo.
        echo Vérification du pool de connexion JNDI...
        cd /d "%%D\bin"
        call asadmin list-jdbc-connection-pools
        echo.
        echo Vérification de la ressource JNDI...
        call asadmin list-jdbc-resources
        goto :eof
    )
)

echo Glassfish non trouvé!
echo Vérifiez que Glassfish est installé et dans le PATH
pause
