package jakartamission.udbl.miniprojet.resources;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Endpoint pour la communication inter-microservices.
 * Permet d'appeler d'autres services et d'agréger les réponses.
 */
@Path("microservices")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MicroserviceResource {

    private static final Logger LOGGER = Logger.getLogger(MicroserviceResource.class.getName());

    /**
     * Appelle un autre micro-service en GET
     * 
     * @param serviceUrl URL complète du service à appeler
     * @return Réponse du service
     */
    @GET
    @Path("call")
    public Response callExternalService(@QueryParam("url") String serviceUrl) {
        if (serviceUrl == null || serviceUrl.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(errorResponse("URL du service requis"))
                    .build();
        }

        Client client = ClientBuilder.newClient();
        try {
            LOGGER.log(Level.INFO, "Appel GET: " + serviceUrl);
            Response response = client.target(serviceUrl)
                    .request(MediaType.APPLICATION_JSON)
                    .get();
            return response;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de l'appel GET: " + e.getMessage(), e);
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .entity(errorResponse("Service indisponible: " + e.getMessage()))
                    .build();
        } finally {
            client.close();
        }
    }

    /**
     * Status du microservice - Healthcheck
     * 
     * @return Status OK
     */
    @GET
    @Path("status")
    public Response getStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("service", "miniProjet");
        status.put("status", "UP");
        status.put("type", "microservice");
        status.put("timestamp", System.currentTimeMillis());
        return Response.ok(status).build();
    }

    /**
     * Endpoint d'orchestration - combine les réponses de plusieurs services
     * 
     * @return Réponse agrégée
     */
    @GET
    @Path("orchestrate")
    public Response orchestrateServices(
            @QueryParam("service1") String service1,
            @QueryParam("service2") String service2) {

        Map<String, Object> aggregatedData = new HashMap<>();
        Client client = ClientBuilder.newClient();

        try {
            if (service1 != null && !service1.isEmpty()) {
                try {
                    LOGGER.log(Level.INFO, "Appel service1: " + service1);
                    Response resp1 = client.target(service1)
                            .request(MediaType.APPLICATION_JSON)
                            .get();
                    if (resp1 != null) {
                        aggregatedData.put("service1_status", resp1.getStatus());
                        aggregatedData.put("service1_data", resp1.getEntity());
                    }
                } catch (Exception e) {
                    aggregatedData.put("service1_error", e.getMessage());
                }
            }

            if (service2 != null && !service2.isEmpty()) {
                try {
                    LOGGER.log(Level.INFO, "Appel service2: " + service2);
                    Response resp2 = client.target(service2)
                            .request(MediaType.APPLICATION_JSON)
                            .get();
                    if (resp2 != null) {
                        aggregatedData.put("service2_status", resp2.getStatus());
                        aggregatedData.put("service2_data", resp2.getEntity());
                    }
                } catch (Exception e) {
                    aggregatedData.put("service2_error", e.getMessage());
                }
            }

            aggregatedData.put("aggregation_time", System.currentTimeMillis());
            return Response.ok(aggregatedData).build();
        } finally {
            client.close();
        }
    }

    private Map<String, Object> errorResponse(String message) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", message);
        error.put("timestamp", System.currentTimeMillis());
        return error;
    }
}
