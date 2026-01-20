package jakartamission.udbl.miniprojet.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakartamission.udbl.miniprojet.dto.LocationRequest;
import jakartamission.udbl.miniprojet.dto.ProductLocationResponse;
import jakartamission.udbl.miniprojet.model.Product;
import jakartamission.udbl.miniprojet.service.InventoryService;

@Path("emplacements")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class EmplacementResource {

    @Inject
    private InventoryService inventoryService;

    @Context
    private UriInfo uriInfo;

    @POST
    public Response assign(@Valid LocationRequest request) {
        try {
            System.out.println("\n🔵 POST /emplacements - Début du traitement");
            System.out.println("   SKU: " + (request.getSku() != null ? request.getSku() : "null"));
            System.out.println("   Name: " + (request.getName() != null ? request.getName() : "null"));
            System.out.println(
                    "   Location: " + (request.getLocationCode() != null ? request.getLocationCode() : "null"));
            System.out.println("   Qty: " + request.getStockQuantity());

            // Validation manuelle pour être sûr
            if (request.getSku() == null || request.getSku().trim().isEmpty()) {
                throw new IllegalArgumentException("SKU ne peut pas être vide");
            }
            if (request.getName() == null || request.getName().trim().isEmpty()) {
                throw new IllegalArgumentException("Name ne peut pas être vide");
            }
            if (request.getLocationCode() == null || request.getLocationCode().trim().isEmpty()) {
                throw new IllegalArgumentException("LocationCode ne peut pas être vide");
            }
            if (request.getStockQuantity() < 0) {
                throw new IllegalArgumentException("StockQuantity ne peut pas être négatif");
            }

            System.out.println("   ✓ Validation réussie");

            System.out.println("   Appel de inventoryService.assignOrUpdateLocation()...");
            Product product = inventoryService.assignOrUpdateLocation(request);

            System.out.println("   ✓ Produit créé/mis à jour: ID=" + product.getId());

            URI created = uriInfo.getAbsolutePathBuilder().path(product.getSku()).build();
            System.out.println("✅ Réponse prête: " + product.getSku() + "\n");

            return Response.created(created).entity(toResponse(product)).build();
        } catch (IllegalArgumentException e) {
            System.err.println("❌ Validation échouée: " + e.getMessage());
            e.printStackTrace();
            return Response.status(400)
                    .entity(new ErrorResponse(400, "Validation: " + e.getMessage()))
                    .build();
        } catch (Exception e) {
            System.err.println("❌ ERREUR dans assign(): " + e.getClass().getName());
            System.err.println("   Message: " + e.getMessage());
            e.printStackTrace();

            Throwable cause = e.getCause();
            if (cause != null) {
                System.err.println("   Cause: " + cause.getClass().getName() + " - " + cause.getMessage());
                cause.printStackTrace();
            }

            return Response.status(500)
                    .entity(new ErrorResponse(500, e.getClass().getSimpleName() + ": " + e.getMessage()))
                    .build();
        }
    }

    @PUT
    @Path("/{sku}/location")
    public Response updateLocation(@PathParam("sku") String sku, LocationRequest request) {
        Product product = inventoryService.updateLocation(sku, request.getLocationCode(), request.getLocationNote());
        return Response.ok(toResponse(product)).build();
    }

    @GET
    @Path("/all")
    public Response getAllProducts() {
        List<ProductLocationResponse> products = inventoryService.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return Response.ok(products).build();
    }

    @GET
    @Path("/{sku}")
    public Response getBySku(@PathParam("sku") String sku) {
        return inventoryService.findBySku(sku)
                .map(product -> Response.ok(toResponse(product)).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).entity("Produit introuvable").build());
    }

    @GET
    public Response search(@QueryParam("sku") String sku) {
        if (sku == null || sku.isBlank()) {
            List<ProductLocationResponse> outOfStock = inventoryService.listOutOfStock().stream()
                    .map(this::toResponse)
                    .collect(Collectors.toList());
            return Response.ok(outOfStock).build();
        }
        return getBySku(sku);
    }

    private ProductLocationResponse toResponse(Product product) {
        return new ProductLocationResponse(
                product.getSku(),
                product.getName(),
                product.getLocationCode(),
                product.getLocationNote() != null ? product.getLocationNote() : "",
                product.getStockQuantity(),
                product.getUpdatedAt());
    }

    public static class ErrorResponse {
        public int status;
        public String message;

        public ErrorResponse(int status, String message) {
            this.status = status;
            this.message = message;
        }

        public int getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }
    }
}
