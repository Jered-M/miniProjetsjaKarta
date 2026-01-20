package jakartamission.udbl.miniprojet.resources;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Endpoint de test pour vérifier que le serveur répond en JSON
 */
@Path("test")
@Produces(MediaType.APPLICATION_JSON)
public class TestResource {

    @GET
    @Path("/ping")
    public Response ping() {
        return Response.ok()
                .entity(new TestResponse("pong", "Serveur en ligne"))
                .build();
    }

    @GET
    @Path("/error")
    public Response testError() {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new TestResponse("error", "Test d'erreur serveur"))
                .build();
    }

    public static class TestResponse {
        public String status;
        public String message;

        public TestResponse(String status, String message) {
            this.status = status;
            this.message = message;
        }

        public String getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }
    }
}
