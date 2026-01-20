# 🔧 Corrections appliquées - Erreur "réponse invalide"

## 🎯 Problème identifié

L'erreur "Unexpected token '<'" signifie que le serveur renvoie du **HTML** (page d'erreur 500) au lieu de JSON.

## ✅ Corrections apportées

### 1. **pom.xml**

- ✅ Ajouté la dépendance `jakarta.json.bind-api` pour sérialiser `LocalDateTime` en JSON

### 2. **InventoryService.java**

- ✅ Ajouté `@Transactional` pour gérer automatiquement les transactions
- ✅ Ajouté validation des champs (SKU, name, locationCode non vides)
- ✅ Ajouté `em.flush()` après chaque persist/merge pour forcer la sauvegarde
- ✅ Gestion des null pour `locationNote`
- ✅ Ajouté validation quantité >= 0

### 3. **ProductLocationResponse.java**

- ✅ Ajouté `@JsonbDateFormat` pour la sérialisation de `LocalDateTime`
- ✅ Conversion de `locationNote` en chaîne vide si null

### 4. **index.html (Frontend)**

- ✅ Vérification du `Content-Type` avant JSON.parse()
- ✅ Meilleure gestion des erreurs avec détails affichés
- ✅ Logs console pour debugging
- ✅ Validation client des champs obligatoires

### 5. **JakartaRestConfiguration.java**

- ✅ Changé de `/resources` à `/api`
- ✅ Ajouté `ProductResource`
- ✅ Ajouté `GlobalExceptionMapper` pour erreurs en JSON

---

## 📋 Prochaines étapes

### 1. **Build du projet**

```
Dans NetBeans:
- Click droit sur "miniProjet" > Clean and Build
- Vérifier qu'il n'y a pas d'erreurs rouges en bas
```

### 2. **Vérifier la base de données Derby**

```sql
-- Exécuter ce script via NetBeans Services > Databases
SELECT * FROM products;
```

### 3. **Redéployer sur Glassfish**

```
- Arrêter l'ancienne application
- Supprimer le WAR ancien
- Redéployer le nouveau WAR
```

### 4. **Tester les endpoints**

**POST - Ajouter un produit**

```bash
curl -X POST http://localhost:8080/miniProjet-1.0/api/emplacements \
  -H "Content-Type: application/json" \
  -d '{
    "sku": "TEST001",
    "name": "Mon Produit",
    "locationCode": "A1-01",
    "locationNote": "Aisle 1",
    "stockQuantity": 50
  }'
```

**GET - Lister tous les produits**

```bash
curl http://localhost:8080/miniProjet-1.0/api/emplacements/all
```

---

## 🐛 Si l'erreur persiste

1. **Ouvre la console de NetBeans** (Fenêtre > Output > miniProjet)
2. **Cherche les erreurs rouges** dans les logs
3. **Envoie-moi** le message d'erreur complet de la console

---

## 📝 Fichiers modifiés

- pom.xml ✅
- InventoryService.java ✅
- ProductLocationResponse.java ✅
- JakartaRestConfiguration.java ✅
- index.html ✅
- GlobalExceptionMapper.java ✅ (créé)
- ErrorResponse.java ✅ (créé)
