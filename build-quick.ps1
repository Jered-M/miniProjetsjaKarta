# Script rapide de rebuild et redéploiement
param(
    [string]$GlassFishHome = "C:\glassfish-7.0.24\glassfish7",
    [string]$JavaHome = "C:\zulu17.62.17-ca-jdk17.0.17-win_x64\zulu17.62.17-ca-jdk17.0.17-win_x64"
)

$projectPath = "c:\Users\HP\Documents\NetBeansProjects\miniProjet"
$srcPath = "$projectPath\src\main\java"
$classesPath = "$projectPath\target\classes"
$warPath = "$projectPath\target\miniProjet-1.0.war"

Write-Host "🔨 Compilation du code source..." -ForegroundColor Cyan

# Compiler tous les fichiers Java
$javacExe = "$JavaHome\bin\javac.exe"
$files = @(
    "$srcPath\jakartamission\udbl\miniprojet\JakartaRestConfiguration.java",
    "$srcPath\jakartamission\udbl\miniprojet\resources\DemoResource.java"
)

& $javacExe -encoding UTF-8 -d "$classesPath" -cp "$classesPath" @files 2>&1

if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Compilation échouée!" -ForegroundColor Red
    exit 1
}

Write-Host "✅ Compilation réussie!" -ForegroundColor Green

Write-Host "📦 Création du WAR..." -ForegroundColor Cyan
cd $projectPath
# Utiliser jar pour créer le WAR
$jarExe = "$JavaHome\bin\jar.exe"
& $jarExe -cf "$warPath" -C "$classesPath" .

Write-Host "✅ WAR créé!" -ForegroundColor Green

Write-Host "🚀 Redéploiement..." -ForegroundColor Cyan
$asadminExe = "$GlassFishHome\glassfish\bin\asadmin.bat"

# Retirer l'ancienne application
& $asadminExe undeploy miniProjet-1.0 --force 2>&1 | Out-Null
Start-Sleep -Seconds 2

# Déployer la nouvelle
& $asadminExe deploy --force "$warPath"

if ($LASTEXITCODE -eq 0) {
    Write-Host "✅ Déploiement réussi!" -ForegroundColor Green
    Write-Host "🌐 URL: http://localhost:8080/miniProjet/" -ForegroundColor Green
}
else {
    Write-Host "❌ Déploiement échoué!" -ForegroundColor Red
}
