package jakartamission.udbl.miniprojet.resources;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * Exception Mapper global pour retourner des erreurs en JSON
 * au lieu de pages HTML
 */
@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception ex) {
        int status = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
        String message = ex.getMessage() != null ? ex.getMessage() : "Erreur serveur interne";

        if (ex instanceof IllegalArgumentException) {
            status = Response.Status.BAD_REQUEST.getStatusCode();
        }

        ErrorResponse error = new ErrorResponse(status, message);

        return Response
                .status(status)
                .entity(error)
                .build();
    }
}
