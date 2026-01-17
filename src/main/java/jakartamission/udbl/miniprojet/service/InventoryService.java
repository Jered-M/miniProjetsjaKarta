package jakartamission.udbl.miniprojet.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakartamission.udbl.miniprojet.dto.LocationRequest;
import jakartamission.udbl.miniprojet.model.Product;

@ApplicationScoped
public class InventoryService {

    @PersistenceContext(unitName = "miniPU")
    private EntityManager em;

    @Inject
    private NotificationService notificationService;

    public Product assignOrUpdateLocation(LocationRequest request) {
        Optional<Product> existing = findBySku(request.getSku());
        Product product = existing.orElseGet(Product::new);

        product.setSku(request.getSku());
        product.setName(request.getName());
        product.setLocationCode(request.getLocationCode());
        product.setLocationNote(request.getLocationNote());
        product.setStockQuantity(request.getStockQuantity());
        product.setUpdatedAt(LocalDateTime.now());

        if (product.getId() == null) {
            em.persist(product);
        } else {
            product = em.merge(product);
        }

        checkStock(product);
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
        List<Product> result = em.createNamedQuery("Product.findBySku", Product.class)
                .setParameter("sku", sku)
                .getResultList();
        return result.stream().findFirst();
    }

    public List<Product> listOutOfStock() {
        return em.createNamedQuery("Product.outOfStock", Product.class).getResultList();
    }

    public List<Product> findAll() {
        return em.createQuery("SELECT p FROM Product p", Product.class).getResultList();
    }

    private void checkStock(Product product) {
        if (product.getStockQuantity() <= 0) {
            notificationService.notifyStockLow(product.getSku(), product.getStockQuantity());
        }
    }
}
