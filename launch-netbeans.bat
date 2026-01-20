@echo off
REM Script pour rebuild via NetBeans
echo ========================================
echo Ouverture du projet miniProjet dans NetBeans
echo ========================================

REM Cherche NetBeans
for /d %%D in ("C:\Program Files*" "C:\Users\*\AppData") do (
    if exist "%%D\NetBeans*\bin\netbeans.exe" (
        echo Lancement NetBeans depuis: %%D
        cd /d "C:\Users\HP\Documents\NetBeansProjects\miniProjet"
        start "" "%%D\NetBeans*\bin\netbeans.exe" --open "C:\Users\HP\Documents\NetBeansProjects\miniProjet"
        goto :eof
    )
)

echo NetBeans non trouvé! 
echo Essaie manuellement: 
echo 1. Ouvre NetBeans
echo 2. Ouvre le projet miniProjet
echo 3. Click droit sur le projet ^> Clean and Build
