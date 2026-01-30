# Configuration du Micro-Service

## Vue d'ensemble

Votre projet est maintenant configuré comme un **micro-service** capable de :

- ✅ Communiquer avec d'autres services REST
- ✅ Faire des appels GET, POST, PUT, DELETE
- ✅ Agréger les données de plusieurs services
- ✅ Fournir un endpoint de vérification de santé (healthcheck)

## Nouveaux Endpoints

### 1. Vérification de l'état du service (Healthcheck)

```
GET /api/microservices/status
```

**Réponse :**

```json
{
  "service": "miniProjet",
  "status": "UP",
  "type": "microservice",
  "timestamp": 1234567890
}
```

### 2. Appeler un autre micro-service

```
GET /api/microservices/call?url=http://autre-service:8080/api/endpoint
```

**Exemple :**

```bash
curl "http://localhost:8080/miniProjet-1.0/api/microservices/call?url=http://service2:8080/api/produits"
```

### 3. Orchestration - Agréger les données de deux services

```
GET /api/microservices/orchestrate?service1=http://service1:8080/api/data&service2=http://service2:8080/api/data
```

**Exemple :**

```bash
curl "http://localhost:8080/miniProjet-1.0/api/microservices/orchestrate?service1=http://localhost:8080/miniProjet-1.0/api/produits&service2=http://autre-service:8080/api/users"
```

**Réponse :**

```json
{
  "service1_data": [...],
  "service2_data": [...],
  "aggregation_time": 1234567890
}
```

## Architecture de Communication

```
┌─────────────────────────┐
│   miniProjet Service    │
│  (Ce Micro-service)     │
└─────────────────────────┘
        │
        ├──→ MicroserviceClient (client HTTP)
        │
        ├──→ GET  http://service-2:8080/api/...
        ├──→ POST http://service-3:8080/api/...
        ├──→ PUT  http://service-4:8080/api/...
        └──→ DELETE http://service-5:8080/api/...
```

## Comment utiliser MicroserviceClient

### Dans vos services existants

```java
@Stateless
public class MonService {

    @Inject
    private MicroserviceClient microserviceClient;

    public void appelAutreService() {
        // Appel GET
        Response response = microserviceClient.callGetService("http://autre-service:8080/api/endpoint");

        // Appel POST
        Map<String, Object> data = new HashMap<>();
        data.put("nom", "valeur");
        Response response = microserviceClient.callPostService("http://autre-service:8080/api/endpoint", data);
    }
}
```

## Configuration des URLs des Services

Pour faciliter la configuration, vous pouvez ajouter les URLs dans le fichier `glassfish-resources.xml` :

```xml
<custom-resource jndi-name="java:app/config/serviceUrl1" factory-class="org.glassfish.resources.custom.factory.PrimitivesAndStringFactory">
    <property name="value" value="http://service-2:8080/api"/>
</custom-resource>
```

## Dépendances Ajoutées

- `jakarta.ws.rs-api` : Client HTTP REST
- `jakarta.json-api` : Traitement JSON

## Prochaines étapes

1. **Compiler le projet** :

```bash
mvn clean package
```

2. **Déployer** sur GlassFish :

```bash
asadmin deploy target/miniProjet-1.0.war
```

3. **Tester les endpoints** :

```bash
# Vérifier que le service est up
curl http://localhost:8080/miniProjet-1.0/api/microservices/status

# Appeler un autre service
curl "http://localhost:8080/miniProjet-1.0/api/microservices/call?url=http://autre-service:8080/api/produits"
```

## Notes de Sécurité

- Validez les URLs entrantes pour éviter les injections
- Utilisez HTTPS en production
- Implémentez une authentification/autorisation entre services
- Ajoutez des timeouts pour les appels distants
- Loggez tous les appels inter-services
