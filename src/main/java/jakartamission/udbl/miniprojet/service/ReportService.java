package jakartamission.udbl.miniprojet.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakartamission.udbl.miniprojet.dto.SaleRecordRequest;
import jakartamission.udbl.miniprojet.dto.SalesAggregateResponse;
import jakartamission.udbl.miniprojet.model.Product;
import jakartamission.udbl.miniprojet.model.SaleRecord;

@ApplicationScoped
public class ReportService {

    private EntityManagerFactory emf;

    @Inject
    private InventoryService inventoryService;

    public ReportService() {
        this.emf = Persistence.createEntityManagerFactory("miniPU");
    }

    public SaleRecord recordSale(SaleRecordRequest request) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Objects.requireNonNull(request.getSku(), "sku obligatoire");
            LocalDate saleDate = request.getSaleDate() != null ? request.getSaleDate() : LocalDate.now();

            Product product = inventoryService.findBySku(request.getSku())
                    .orElseThrow(() -> new IllegalArgumentException("Produit introuvable: " + request.getSku()));

            SaleRecord record = new SaleRecord();
            record.setSku(product.getSku());
            record.setProductName(product.getName());
            record.setDateSale(saleDate.atStartOfDay());
            record.setQuantity(request.getQuantity());
            // Calculer le prix unitaire depuis le montant total et la quantité
            BigDecimal unitPrice = request.getTotalAmount().divide(BigDecimal.valueOf(request.getQuantity()), 2,
                    java.math.RoundingMode.HALF_UP);
            record.setUnitPrice(unitPrice);
            record.setTotalPrice(request.getTotalAmount());
            em.persist(record);

            int remaining = Math.max(0, product.getStockQuantity() - request.getQuantity());
            inventoryService.adjustStock(product.getSku(), remaining);
            em.getTransaction().commit();
            return record;
        } finally {
            em.close();
        }
    }

    public List<SalesAggregateResponse> aggregateSales(LocalDate from, LocalDate to, String groupBy) {
        EntityManager em = emf.createEntityManager();
        try {
            LocalDate start = from != null ? from : LocalDate.now().minusDays(30);
            LocalDate end = to != null ? to : LocalDate.now();
            String grouping = groupBy != null ? groupBy.toLowerCase() : "month";

            List<SaleRecord> records = em.createQuery(
                    "SELECT s FROM SaleRecord s WHERE CAST(s.dateSale AS date) BETWEEN :from AND :to ORDER BY s.dateSale",
                    SaleRecord.class)
                    .setParameter("from", start)
                    .setParameter("to", end)
                    .getResultList();

            Map<String, Aggregation> buckets = new LinkedHashMap<>();
            for (SaleRecord record : records) {
                String key = grouping.equals("day")
                        ? record.getDateSale().toLocalDate().format(DateTimeFormatter.ISO_DATE)
                        : record.getDateSale().getYear() + "-"
                                + String.format("%02d", record.getDateSale().getMonthValue());

                buckets.computeIfAbsent(key, k -> new Aggregation())
                        .add(record.getQuantity(), record.getTotalPrice());
            }

            List<SalesAggregateResponse> response = new ArrayList<>();
            for (Map.Entry<String, Aggregation> entry : buckets.entrySet()) {
                Aggregation agg = entry.getValue();
                response.add(new SalesAggregateResponse(entry.getKey(), agg.totalQuantity, agg.totalAmount));
            }
            return response;
        } finally {
            em.close();
        }
    }

    private static class Aggregation {
        private int totalQuantity;
        private BigDecimal totalAmount = BigDecimal.ZERO;

        void add(int quantity, BigDecimal amount) {
            totalQuantity += quantity;
            totalAmount = totalAmount.add(amount);
        }
    }
}
