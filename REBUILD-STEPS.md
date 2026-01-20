# 🛠️ Étapes pour corriger l'erreur et redéployer

## 📋 Checklist

### 1. ✅ Build du projet dans NetBeans

1. **Ouvre NetBeans**
2. **Ouvre le projet miniProjet** (s'il n'est pas déjà ouvert)
   - File → Open Project → sélectionne miniProjet
3. **Clean and Build**
   - Clic droit sur "miniProjet" (dans Projects)
   - Sélectionne **"Clean and Build"**
   - Attends que ça se termine (pas d'erreurs rouges en bas)

### 2. ✅ Vérify Glassfish

1. **Vérife que Glassfish démarre**
   - Dans la fenêtre "Services" (View → Services)
   - Clic droit sur "GlassFish Server 7"
   - Sélectionne **"Start Server"**
   - Attends "GlassFish Server 7 started" dans les logs

### 3. ✅ Redéploie l'application

1. **Appuie sur F6** (ou clic droit sur miniProjet → Run)
2. **Attends l'affichage:**
   - "BUILD SUCCESS"
   - "RUNNING on http://localhost:8080/miniProjet-1.0"

### 4. ✅ Teste les endpoints

**Dans la console du navigateur (F12 → Console):**

Devrait afficher: `✅ Serveur répond: {status: "pong", message: "Serveur en ligne"}`

Si tu vois ça, le serveur fonctionne!

### 5. ✅ Test d'ajout de produit

1. Remplis le formulaire:
   - SKU: TEST001
   - Produit: Test
   - Emplacement: A1
   - Quantité: 10
2. Clique "Assigner"
3. Vérifie la console (F12):
   - Cherche les logs en rouge (erreurs)
   - Envoie-moi les messages d'erreur

---

## 🐛 Déboguer les erreurs

### Si tu vois une erreur dans la console:

**Option 1: Regarde la console de NetBeans**

- Window → Output → miniProjet (selectionne l'onglet)
- Cherche les lignes en rouge ou avec "Exception"
- Envoie-moi le texte

**Option 2: Regarde les logs Glassfish**

- Ouvre: `http://localhost:4848` (admin Glassfish)
- Va dans: Application Server → Logs
- Cherche les erreurs avec "miniProjet"
- Envoie-moi le message d'erreur complet

**Option 3: Ajoute un console.log amélioré**

- Ouvre le navigateur F12 → Console
- Efface les anciens logs
- Clique "Assigner"
- Copie-colle tout ce qui s'affiche
- Envoie-moi

---

## 📝 Fichiers modifiés (à recompiler)

- ✅ InventoryService.java → @Transactional ajouté
- ✅ ProductLocationResponse.java → @JsonbDateFormat ajouté
- ✅ EmplacementResource.java → Routes réorganisées
- ✅ glassfish-resources.xml → JNDI configuré
- ✅ persistence.xml → Logging amélioré
- ✅ TestResource.java → Créé (test /api/test/ping)
- ✅ GlobalExceptionMapper.java → Erreurs en JSON

**Si tu as fait Clean and Build, tout devrait être recompilé!**

---

## 🚀 Commandes rapides

```
# Si tu as Maven installé:
cd C:\Users\HP\Documents\NetBeansProjects\miniProjet
mvn clean install

# Puis redéploie dans NetBeans (F6)
```

---

**Envoie-moi:**

1. ✅ Résultat du /api/test/ping (console F12)
2. ❌ Ou l'erreur que tu vois
3. 📋 Ou les logs de Glassfish
