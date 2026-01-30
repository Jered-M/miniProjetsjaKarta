package jakartamission.udbl.miniprojet.resources;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakartamission.udbl.miniprojet.model.Product;
import jakartamission.udbl.miniprojet.model.SaleRecord;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("")
@Stateless
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DemoResource {

    @PersistenceContext(unitName = "miniPU")
    private EntityManager em;

    // ============ PRODUITS ============

    @GET
    @Path("produits")
    public Response listProduits() {
        try {
            List<Product> products = em.createNamedQuery("Product.findAll", Product.class)
                    .getResultList();
            return Response.ok(products).build();
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            error.put("status", 500);
            return Response.status(500).entity(error).build();
        }
    }

    @GET
    @Path("produits/{sku}")
    public Response getProduitBySku(@PathParam("sku") String sku) {
        try {
            Product p = em.createNamedQuery("Product.findBySku", Product.class)
                    .setParameter("sku", sku)
                    .getSingleResult();
            return Response.ok(p).build();
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Produit non trouvé: " + sku);
            error.put("status", 404);
            return Response.status(404).entity(error).build();
        }
    }

    @POST
    @Path("produits")
    public Response createProduit(Product product) {
        try {
            product.setUpdatedAt(LocalDateTime.now());
            em.persist(product);
            return Response.status(201).entity(product).build();
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            error.put("status", 500);
            return Response.status(500).entity(error).build();
        }
    }

    @GET
    @Path("reports/stock-out")
    public Response getProduitRupture() {
        try {
            List<Product> rupture = em.createNamedQuery("Product.outOfStock", Product.class)
                    .getResultList();
            return Response.ok(rupture).build();
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            error.put("status", 500);
            return Response.status(500).entity(error).build();
        }
    }

    // ============ EMPLACEMENTS ============

    @POST
    @Path("emplacements")
    public Response assignEmplacement(Map<String, Object> request) {
        try {
            String sku = (String) request.get("sku");

            Product p = em.createNamedQuery("Product.findBySku", Product.class)
                    .setParameter("sku", sku)
                    .getSingleResult();

            p.setLocationCode((String) request.get("locationCode"));
            p.setLocationNote((String) request.get("locationNote"));
            if (request.get("stockQuantity") != null) {
                Object stock = request.get("stockQuantity");
                int qty = 0;
                if (stock instanceof Number) {
                    qty = ((Number) stock).intValue();
                }
                p.setStockQuantity(qty);
            }
            p.setUpdatedAt(LocalDateTime.now());
            em.merge(p);
            return Response.ok(p).build();
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Produit non trouvé ou erreur: " + e.getMessage());
            error.put("status", 404);
            return Response.status(404).entity(error).build();
        }
    }

    // ============ VENTES ============

    @POST
    @Path("ventes")
    public Response recordSale(SaleRecord saleData) {
        try {
            String sku = saleData.getSku();

            Product product = em.createNamedQuery("Product.findBySku", Product.class)
                    .setParameter("sku", sku)
                    .getSingleResult();

            saleData.setProductName(product.getName());
            saleData.setDateSale(LocalDateTime.now());
            em.persist(saleData);
            return Response.status(201).entity(saleData).build();
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            error.put("status", 500);
            return Response.status(500).entity(error).build();
        }
    }

    @GET
    @Path("ventes")
    public Response listVentes() {
        try {
            List<SaleRecord> sales = em.createNamedQuery("SaleRecord.findAll", SaleRecord.class)
                    .getResultList();
            return Response.ok(sales).build();
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            error.put("status", 500);
            return Response.status(500).entity(error).build();
        }
    }

    @GET
    @Path("rapports/ventes")
    public Response getSalesReport() {
        try {
            List<SaleRecord> sales = em.createNamedQuery("SaleRecord.findAll", SaleRecord.class)
                    .getResultList();
            Map<String, Object> report = new HashMap<>();
            report.put("totalSales", sales.size());
            BigDecimal totalAmount = BigDecimal.ZERO;
            for (SaleRecord sale : sales) {
                totalAmount = totalAmount.add(sale.getTotalPrice());
            }
            report.put("totalAmount", totalAmount);
            report.put("sales", sales);
            return Response.ok(report).build();
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            error.put("status", 500);
            return Response.status(500).entity(error).build();
        }
    }

    // ============ STATUS ============

    @GET
    @Path("status")
    public Response status() {
        try {
            List<Product> prods = em.createNamedQuery("Product.findAll", Product.class).getResultList();
            List<SaleRecord> sales = em.createNamedQuery("SaleRecord.findAll", SaleRecord.class).getResultList();

            Map<String, Object> response = new HashMap<>();
            response.put("status", "✅ API Fonctionne!");
            response.put("productsCount", prods.size());
            response.put("salesCount", sales.size());
            response.put("message", "API de gestion d'inventaire - Persistance Derby");
            response.put("timestamp", LocalDateTime.now().toString());
            return Response.ok(response).build();
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "✅ API Fonctionne!");
            response.put("message", "Persistance Derby active");
            return Response.ok(response).build();
        }
    }

    @GET
    @Path("demo/products")
    public Response demoProducts() {
        try {
            List<Product> products = em.createNamedQuery("Product.findAll", Product.class).getResultList();
            return Response.ok(products).build();
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return Response.ok(error).build();
        }
    }
}
