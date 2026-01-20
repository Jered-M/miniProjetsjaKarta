# 🔧 Guide de Configuration Glassfish - JNDI et Derby

## 🎯 Problème

L'erreur `jdbc/serviceEmplacement not found` signifie que:

1. Le pool de connexion **n'existe pas** dans Glassfish
2. Derby **n'est pas lancé**
3. Le pool **ne peut pas se connecter** à Derby

---

## ✅ Vérifications à faire

### 1. Vérifier que Glassfish démarre correctement

```bash
# Dans NetBeans:
- Clic droit sur "GlassFish Server" (Services)
- "Start Server"
- Attendre "GlassFish Server 7 started" dans les logs
```

### 2. Accéder à la console d'administration

```
URL: http://localhost:4848
Utilisateur: admin
Mot de passe: admin (ou vide)
```

### 3. Vérifier les ressources JNDI

```
Allez dans:
1. Resources > JDBC > Connection Pools
   - Cherchez: DerbyPoolEmplacement
   - Cliquez sur "Ping" pour tester
   - Si c'est rouge = Derby n'est pas accessible

2. Resources > JDBC > JDBC Resources
   - Cherchez: jdbc/serviceEmplacement
   - Doit pointer vers: DerbyPoolEmplacement
```

---

## 🚀 Solution 1: Créer le pool via l'interface web

### A. Connection Pool

1. Allez dans: **Resources > JDBC > Connection Pools**
2. Cliquez sur **"New..."**
3. **Remplissez:**
   - **Pool Name:** `DerbyPoolEmplacement`
   - **Resource Type:** `javax.sql.DataSource`
   - **Database Driver Vendor:** `Derby`
4. Cliquez **"Next"**
5. **Propriétés supplémentaires:**
   - **serverName:** `localhost`
   - **portNumber:** `1527`
   - **databaseName:** `miniDB`
   - **user:** `APP`
   - **password:** `APP`
6. Cliquez **"Finish"**

### B. JDBC Resource

1. Allez dans: **Resources > JDBC > JDBC Resources**
2. Cliquez sur **"New..."**
3. **Remplissez:**
   - **JNDI Name:** `jdbc/serviceEmplacement`
   - **Pool Name:** `DerbyPoolEmplacement`
4. Cliquez **"OK"**

### C. Tester la connexion

1. Retournez à la liste des pools
2. Sélectionnez `DerbyPoolEmplacement`
3. Cliquez sur **"Ping"**
4. **Succès?** → Vert ✅
   **Échoué?** → Derby n'est pas lancé

---

## 🚀 Solution 2: Redéployer l'application

Si le fichier `glassfish-resources.xml` est correct (vérifié ✅), les ressources se créent automatiquement au redéploiement:

```
1. Arrêter Glassfish (dans NetBeans: clic droit > Stop)
2. Supprimer l'application (Applications > Sélectionner miniProjet-1.0 > Supprimer)
3. Clean and Build (clic droit sur miniProjet > Clean and Build)
4. Redémarrer Glassfish
5. Redéployer l'application (F6 ou clic droit > Run)
```

---

## 🚀 Solution 3: Lancer Derby Network Server

Si c'est Glassfish autonome (pas NetBeans):

```bash
# Windows (depuis le répertoire Derby)
cd C:\path\to\glassfish\javadb\bin
startNetworkServer.bat

# Unix/Linux
./startNetworkServer.sh
```

---

## ❌ Si ça ne marche toujours pas

Allez dans la console Glassfish et cherchez dans les logs:

```
http://localhost:4848
Navigation: Application Server > Logs
Cherchez les erreurs avec "miniProjet" ou "JDBC"
```

Puis envoyez-moi le message d'erreur complet!

---

## ✅ Fichiers correctement configurés

- ✅ `persistence.xml` - utilise `jdbc/serviceEmplacement`
- ✅ `glassfish-resources.xml` - crée le pool et la ressource JNDI
- ✅ `EmplacementResource.java` - routes correctes
- ✅ `InventoryService.java` - avec transactions
