package jakartamission.udbl.miniprojet.resources;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakartamission.udbl.miniprojet.service.InventoryService;
import java.util.HashMap;
import java.util.Map;

/**
 * Endpoint de diagnostic pour vérifier l'état du serveur
 */
@Path("debug")
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class DebugResource {

    @Inject
    private InventoryService inventoryService;

    @GET
    @Path("/status")
    public Response status() {
        Map<String, Object> status = new HashMap<>();
        status.put("status", "ok");
        status.put("timestamp", System.currentTimeMillis());

        try {
            long count = inventoryService.findAll().size();
            status.put("productsCount", count);
            status.put("database", "connected");
        } catch (Exception e) {
            status.put("database", "error: " + e.getMessage());
        }

        return Response.ok(status).build();
    }

    @GET
    @Path("/health")
    public Response health() {
        return Response.ok("{\"health\":\"ok\"}").build();
    }
}
