package jakartamission.udbl.miniprojet.resources;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import jakartamission.udbl.miniprojet.dto.ErrorResponse;

/**
 * Exception Mapper global pour retourner des erreurs en JSON
 * au lieu de pages HTML
 */
@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception ex) {
        int status = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();

        // On cherche la cause racine la plus informative
        Throwable cause = ex;
        String message = ex.getMessage();

        while (cause != null) {
            // Unwrapping the EJB exception layers
            if (cause.getMessage() != null && !cause.getMessage().isEmpty()) {
                message = cause.getMessage();
            }
            if (cause instanceof IllegalArgumentException) {
                status = Response.Status.BAD_REQUEST.getStatusCode();
            }
            cause = cause.getCause();
        }

        if (message == null || message.isEmpty() || message.equals("null")) {
            message = "Erreur interne: " + ex.getClass().getSimpleName();
        }

        ErrorResponse error = new ErrorResponse(status, message);
        return Response.status(status).entity(error).build();
    }
}
