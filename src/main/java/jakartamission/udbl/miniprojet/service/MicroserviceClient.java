package jakartamission.udbl.miniprojet.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service pour la communication entre micro-services.
 * Permet d'appeler d'autres services REST.
 */
@Stateless
public class MicroserviceClient {

    private static final Logger LOGGER = Logger.getLogger(MicroserviceClient.class.getName());

    /**
     * Fait une requête GET à un autre micro-service
     * 
     * @param serviceUrl URL complète du service
     * @return Response du service
     */
    public Response callGetService(String serviceUrl) {
        Client client = ClientBuilder.newClient();
        try {
            LOGGER.log(Level.INFO, "Appel GET: " + serviceUrl);
            Response response = client.target(serviceUrl)
                    .request(MediaType.APPLICATION_JSON)
                    .get();
            return response;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de l'appel GET: " + e.getMessage(), e);
            return null;
        } finally {
            client.close();
        }
    }

    /**
     * Fait une requête POST à un autre micro-service
     * 
     * @param serviceUrl URL complète du service
     * @param data       Données à envoyer (Map)
     * @return Response du service
     */
    public Response callPostService(String serviceUrl, Map<String, Object> data) {
        Client client = ClientBuilder.newClient();
        try {
            LOGGER.log(Level.INFO, "Appel POST: " + serviceUrl);
            Response response = client.target(serviceUrl)
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(data, MediaType.APPLICATION_JSON));
            return response;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de l'appel POST: " + e.getMessage(), e);
            return null;
        } finally {
            client.close();
        }
    }

    /**
     * Fait une requête PUT à un autre micro-service
     * 
     * @param serviceUrl URL complète du service
     * @param data       Données à envoyer (Map)
     * @return Response du service
     */
    public Response callPutService(String serviceUrl, Map<String, Object> data) {
        Client client = ClientBuilder.newClient();
        try {
            LOGGER.log(Level.INFO, "Appel PUT: " + serviceUrl);
            Response response = client.target(serviceUrl)
                    .request(MediaType.APPLICATION_JSON)
                    .put(Entity.entity(data, MediaType.APPLICATION_JSON));
            return response;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de l'appel PUT: " + e.getMessage(), e);
            return null;
        } finally {
            client.close();
        }
    }

    /**
     * Fait une requête DELETE à un autre micro-service
     * 
     * @param serviceUrl URL complète du service
     * @return Response du service
     */
    public Response callDeleteService(String serviceUrl) {
        Client client = ClientBuilder.newClient();
        try {
            LOGGER.log(Level.INFO, "Appel DELETE: " + serviceUrl);
            Response response = client.target(serviceUrl)
                    .request(MediaType.APPLICATION_JSON)
                    .delete();
            return response;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de l'appel DELETE: " + e.getMessage(), e);
            return null;
        } finally {
            client.close();
        }
    }
}
