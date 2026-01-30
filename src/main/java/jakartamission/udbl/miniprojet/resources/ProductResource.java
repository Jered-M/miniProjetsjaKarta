package jakartamission.udbl.miniprojet.resources;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakartamission.udbl.miniprojet.dto.LocationRequest;
import jakartamission.udbl.miniprojet.dto.ProductLocationResponse;
import jakartamission.udbl.miniprojet.model.Product;
import jakartamission.udbl.miniprojet.service.InventoryService;

@Path("produits")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class ProductResource {

    @Inject
    private InventoryService inventoryService;

    @Context
    private UriInfo uriInfo;

    /**
     * Récupérer tous les produits
     */
    @GET
    public Response listAll() {
        System.out.println("📍 GET /api/produits - Début");
        List<ProductLocationResponse> products = inventoryService.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        System.out.println("✅ Produits trouvés: " + products.size());
        return Response.ok(products).build();
    }

    /**
     * Récupérer un produit par son SKU
     */
    @GET
    @Path("/{sku}")
    public Response getProductBySku(@PathParam("sku") String sku) {
        return inventoryService.findBySku(sku)
                .map(product -> Response.ok(toResponse(product)).build())
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .entity("Produit avec le SKU " + sku + " introuvable").build());
    }

    /**
     * Créer un nouveau produit ou mettre à jour un existant
     */
    @POST
    public Response createProduct(@Valid LocationRequest request) {
        try {
            System.out.println("📍 POST /api/produits - Création/Mise à jour pour SKU: " + request.getSku());
            Product product = inventoryService.assignOrUpdateLocation(request);
            URI created = uriInfo.getAbsolutePathBuilder().path(product.getSku()).build();
            return Response.created(created).entity(toResponse(product)).build();
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de createProduct: " + e.getMessage());
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            error.put("status", 500);
            return Response.status(500).entity(error).build();
        }
    }

    /**
     * Supprimer un produit par son SKU
     */
    @DELETE
    @Path("/{sku}")
    public Response deleteProduct(@PathParam("sku") String sku) {
        boolean deleted = inventoryService.deleteProductBySku(sku);
        if (deleted) {
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Produit avec le SKU " + sku + " introuvable").build();
        }
    }

    private ProductLocationResponse toResponse(Product product) {
        return new ProductLocationResponse(
                product.getSku(),
                product.getName(),
                product.getLocationCode(),
                product.getLocationNote(),
                product.getStockQuantity(),
                product.getUpdatedAt());
    }
}
