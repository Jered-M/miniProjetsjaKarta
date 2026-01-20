# Rebuild complet avec persistance JPA
$GlassFishHome = "C:\glassfish-7.0.24\glassfish7"
$JavaHome = "C:\zulu17.62.17-ca-jdk17.0.17-win_x64\zulu17.62.17-ca-jdk17.0.17-win_x64"
$projectPath = "c:\Users\HP\Documents\NetBeansProjects\miniProjet"
$srcPath = "$projectPath\src\main\java"
$resPath = "$projectPath\src\main\resources"
$classesPath = "$projectPath\target\classes"
$warPath = "$projectPath\target\miniProjet-1.0.war"

Write-Host "✓ Nettoyage target/classes" -ForegroundColor Cyan
Remove-Item -Path $classesPath -Recurse -Force -ErrorAction SilentlyContinue
New-Item -Path $classesPath -ItemType Directory -Force | Out-Null

Write-Host "✓ Compilation Java..." -ForegroundColor Cyan
$javaFiles = @(
    "$srcPath\jakartamission\udbl\miniprojet\JakartaRestConfiguration.java",
    "$srcPath\jakartamission\udbl\miniprojet\resources\DemoResource.java",
    "$srcPath\jakartamission\udbl\miniprojet\model\Product.java",
    "$srcPath\jakartamission\udbl\miniprojet\model\SaleRecord.java"
)

$javacExe = "$JavaHome\bin\javac.exe"
$classpath = "$classesPath;C:\glassfish-7.0.24\glassfish7\glassfish\domains\domain1\lib\*"
& $javacExe -encoding UTF-8 -d $classesPath -cp $classpath $javaFiles 2>&1 | Select-Object -Last 20

if ($LASTEXITCODE -ne 0) {
    Write-Host "✗ Erreur de compilation" -ForegroundColor Red
    Exit 1
}

Write-Host "✓ Copie ressources..." -ForegroundColor Cyan
Copy-Item "$resPath\META-INF\persistence.xml" "$classesPath\META-INF\" -Force
Copy-Item "$resPath\META-INF\*.sql" "$classesPath\META-INF\" -Force -ErrorAction SilentlyContinue

Write-Host "✓ Création WAR..." -ForegroundColor Cyan
$webappPath = "$projectPath\src\main\webapp"
if (Test-Path $warPath) { Remove-Item $warPath -Force }

# Créer le WAR avec jar
$jarExe = "$JavaHome\bin\jar.exe"
Push-Location $projectPath\target
& $jarExe -cf miniProjet-1.0.war -C classes . 2>&1 | Select-Object -Last 5
& $jarExe -uf miniProjet-1.0.war -C "$webappPath" . 2>&1 | Out-Null
Pop-Location

Write-Host "✓ Dédéploiement ancien..." -ForegroundColor Cyan
& "$GlassFishHome\bin\asadmin.bat" undeploy miniProjet-1.0 2>&1 | Out-Null
Start-Sleep -Seconds 2

Write-Host "✓ Redéploiement..." -ForegroundColor Cyan
& "$GlassFishHome\bin\asadmin.bat" deploy --force $warPath 2>&1 | Select-String "deployed|failure"

Write-Host "`n✓ Déploiement terminé!" -ForegroundColor Green
Write-Host "Accédez à l'application: http://localhost:8080/miniProjet/" -ForegroundColor Green
