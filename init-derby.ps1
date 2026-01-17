# Script PowerShell pour initialiser Derby

# Variables
$DERBY_VERSION = "10.16.1.1"
$DERBY_HOME = "C:\derby-$DERBY_VERSION"
$JAVA_HOME = "C:\Program Files\Java\jdk-11"
$PROJECT_ROOT = Get-Location
$DB_DIR = "$PROJECT_ROOT\miniDB"
$SQL_FILE = "$PROJECT_ROOT\src\main\resources\META-INF\init-derby.sql"

# Vérifier si Derby est installé
if (-not (Test-Path "$DERBY_HOME\lib\derby.jar")) {
    Write-Host "❌ Derby n'a pas été trouvé dans $DERBY_HOME" -ForegroundColor Red
    Write-Host "📥 Télécharge Derby: https://db.apache.org/derby/downloads/" -ForegroundColor Yellow
    exit 1
}

# Créer le répertoire de la base de données
if (-not (Test-Path $DB_DIR)) {
    New-Item -ItemType Directory -Path $DB_DIR -Force | Out-Null
    Write-Host "✅ Répertoire créé: $DB_DIR" -ForegroundColor Green
}

# Exécuter le script SQL
Write-Host "🚀 Création de la base de données Derby..." -ForegroundColor Cyan
Push-Location $DB_DIR

$javaCmd = @(
    "$JAVA_HOME\bin\java",
    "-cp", "$DERBY_HOME\lib\derby.jar;$DERBY_HOME\lib\derbytools.jar",
    "org.apache.derby.tools.ij",
    $SQL_FILE
)

& $javaCmd[0] $javaCmd[1..($javaCmd.Length - 1)]

if ($LASTEXITCODE -eq 0) {
    Write-Host "✅ Base de données créée avec succès!" -ForegroundColor Green
    Write-Host "📊 Localisation: $DB_DIR" -ForegroundColor Cyan
}
else {
    Write-Host "❌ Erreur lors de la création de la base de données" -ForegroundColor Red
    exit 1
}

Pop-Location
