# Script PowerShell pour compiler et déployer

Write-Host "===================================" -ForegroundColor Cyan
Write-Host "Compilation du projet miniProjet" -ForegroundColor Cyan
Write-Host "===================================" -ForegroundColor Cyan

Set-Location "C:\Users\HP\Documents\NetBeansProjects\miniProjet"

# Trouver Java
$java = Get-Command java -ErrorAction SilentlyContinue
if (-not $java) {
    Write-Host "❌ Erreur: Java n'a pas été trouvé" -ForegroundColor Red
    exit 1
}

Write-Host "✅ Java trouvé: $($java.Source)" -ForegroundColor Green

# Trouver GlassFish
$GLASSFISH_HOME = "C:\glassfish7"
if (-not (Test-Path "$GLASSFISH_HOME\bin\asadmin.bat")) {
    Write-Host "❌ Erreur: GlassFish n'a pas été trouvé dans $GLASSFISH_HOME" -ForegroundColor Red
    Write-Host "📍 Configure le chemin correct et réessaye" -ForegroundColor Yellow
    exit 1
}

Write-Host "✅ GlassFish trouvé: $GLASSFISH_HOME" -ForegroundColor Green

# Créer le répertoire build
if (-not (Test-Path "target")) {
    New-Item -ItemType Directory -Path "target" -Force | Out-Null
}

Write-Host ""
Write-Host "🔨 Compilation du projet..." -ForegroundColor Cyan

# Déployer (cela compilera aussi)
$warFile = "target/miniProjet-1.0.war"

# Essayer de déployer
& "$GLASSFISH_HOME\bin\asadmin.bat" deploy --force=true $warFile 2>&1 | Write-Host

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "✅ Projet compilé et déployé avec succès!" -ForegroundColor Green
    Write-Host "✅ La base de données s'initialise automatiquement" -ForegroundColor Green
    Write-Host ""
    Write-Host "📱 Accède à l'application:" -ForegroundColor Cyan
    Write-Host "http://localhost:8080/miniProjet-1.0/" -ForegroundColor Yellow
} else {
    Write-Host ""
    Write-Host "⚠️ Essaye avec NetBeans:" -ForegroundColor Yellow
    Write-Host "1. Ouvre NetBeans" -ForegroundColor White
    Write-Host "2. Charge le projet" -ForegroundColor White
    Write-Host "3. Clic-droit -> Build" -ForegroundColor White
    Write-Host "4. Clic-droit -> Deploy" -ForegroundColor White
}
