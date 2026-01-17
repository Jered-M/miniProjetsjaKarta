package jakartamission.udbl.miniprojet.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
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
public class EmplacementResource {

    @Inject
    private InventoryService inventoryService;

    @Context
    private UriInfo uriInfo;

    @POST
    public Response assign(@Valid LocationRequest request) {
        Product product = inventoryService.assignOrUpdateLocation(request);
        URI created = uriInfo.getAbsolutePathBuilder().path(product.getSku()).build();
        return Response.created(created).entity(toResponse(product)).build();
    }

    @PUT
    @Path("/{sku}/location")
    public Response updateLocation(@PathParam("sku") String sku, LocationRequest request) {
        Product product = inventoryService.updateLocation(sku, request.getLocationCode(), request.getLocationNote());
        return Response.ok(toResponse(product)).build();
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

    @GET
    @Path("/all")
    public Response getAllProducts() {
        List<ProductLocationResponse> products = inventoryService.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return Response.ok(products).build();
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
