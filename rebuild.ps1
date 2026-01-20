# Script de rebuild et redéploiement
Write-Host "🔨 Nettoyage du projet..." -ForegroundColor Cyan
mvn clean

Write-Host "🏗️ Compilation du projet..." -ForegroundColor Cyan
mvn install

if ($LASTEXITCODE -eq 0) {
    Write-Host "✅ Build réussi!" -ForegroundColor Green
    Write-Host "📦 Le WAR est prêt dans: target/miniProjet-1.0.war" -ForegroundColor Green
    Write-Host "🚀 Déploie maintenant sur Glassfish" -ForegroundColor Yellow
}
else {
    Write-Host "❌ Build échoué!" -ForegroundColor Red
}
