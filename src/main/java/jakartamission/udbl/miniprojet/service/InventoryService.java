package jakartamission.udbl.miniprojet.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakartamission.udbl.miniprojet.dto.LocationRequest;
import jakartamission.udbl.miniprojet.model.Product;

@Stateless
public class InventoryService {

    @PersistenceContext(unitName = "miniPU")
    private EntityManager em;

    @Inject
    private NotificationService notificationService;

    public Product assignOrUpdateLocation(LocationRequest request) {
        // Validation des données
        System.out.println("   [InventoryService] Validation de la requête...");
        if (request == null || request.getSku() == null || request.getSku().isBlank()) {
            throw new IllegalArgumentException("SKU ne peut pas être vide");
        }
        if (request.getName() == null || request.getName().isBlank()) {
            throw new IllegalArgumentException("Nom du produit ne peut pas être vide");
        }
        if (request.getLocationCode() == null || request.getLocationCode().isBlank()) {
            throw new IllegalArgumentException("Code emplacement ne peut pas être vide");
        }
        if (request.getStockQuantity() < 0) {
            throw new IllegalArgumentException("Quantité en stock ne peut pas être négative");
        }
        System.out.println("   [InventoryService] ✓ Validation réussie");

        System.out.println("   [InventoryService] Recherche du produit existant avec SKU: " + request.getSku());
        Optional<Product> existing = findBySku(request.getSku());
        Product product = existing.orElseGet(() -> {
            System.out.println("   [InventoryService]   -> Nouveau produit");
            return new Product();
        });

        if (existing.isPresent()) {
            System.out.println("   [InventoryService]   -> Produit existant trouvé, ID=" + product.getId());
        }

        System.out.println("   [InventoryService] Mise à jour des propriétés du produit");
        product.setSku(request.getSku());
        product.setName(request.getName());
        product.setLocationCode(request.getLocationCode());
        product.setLocationNote(request.getLocationNote() != null ? request.getLocationNote() : "");
        product.setStockQuantity(request.getStockQuantity());
        product.setUpdatedAt(LocalDateTime.now());

        System.out.println("   [InventoryService] Persistance du produit...");
        if (product.getId() == null) {
            System.out.println("   [InventoryService]   Appel de em.persist()");
            em.persist(product);
        } else {
            System.out.println("   [InventoryService]   Appel de em.merge()");
            product = em.merge(product);
        }

        System.out.println("   [InventoryService] Appel de em.flush()");
        em.flush();

        System.out.println("   [InventoryService] Vérification du stock");
        checkStock(product);

        System.out.println("   [InventoryService] ✓ Produit sauvegardé avec ID=" + product.getId());
        return product;
    }

    public Product updateLocation(String sku, String locationCode, String locationNote) {
        Product product = findBySku(sku)
                .orElseThrow(() -> new IllegalArgumentException("Produit introuvable: " + sku));
        product.setLocationCode(locationCode);
        product.setLocationNote(locationNote);
        product.setUpdatedAt(LocalDateTime.now());
        return em.merge(product);
    }

    public Product adjustStock(String sku, int newStock) {
        Product product = findBySku(sku)
                .orElseThrow(() -> new IllegalArgumentException("Produit introuvable: " + sku));
        product.setStockQuantity(newStock);
        product.setUpdatedAt(LocalDateTime.now());
        checkStock(product);
        return em.merge(product);
    }

    public Optional<Product> findBySku(String sku) {
        System.out.println("   [InventoryService.findBySku] Recherche avec SKU=" + sku);
        try {
            List<Product> result = em.createNamedQuery("Product.findBySku", Product.class)
                    .setParameter("sku", sku)
                    .getResultList();
            System.out.println("   [InventoryService.findBySku] Résultats trouvés: " + result.size());
            if (!result.isEmpty()) {
                System.out.println("   [InventoryService.findBySku] Produit trouvé: ID=" + result.get(0).getId());
            }
            return result.stream().findFirst();
        } catch (Exception e) {
            System.err.println(
                    "   [InventoryService.findBySku] ❌ ERREUR: " + e.getClass().getName() + " - " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<Product> listOutOfStock() {
        return em.createNamedQuery("Product.outOfStock", Product.class).getResultList();
    }

    public List<Product> findAll() {
        return em.createQuery("SELECT p FROM Product p", Product.class).getResultList();
    }

    public boolean deleteProductBySku(String sku) {
        Optional<Product> product = findBySku(sku);
        if (product.isPresent()) {
            em.remove(em.merge(product.get()));
            return true;
        }
        return false;
    }

    private void checkStock(Product product) {
        if (product.getStockQuantity() <= 0) {
            notificationService.notifyStockLow(product.getSku(), product.getStockQuantity());
        }
    }
}
