package jakartamission.udbl.miniprojet.service;

import java.util.logging.Logger;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class NotificationService {

    private static final Logger LOGGER = Logger.getLogger(NotificationService.class.getName());

    public void notifyStockLow(String sku, int stock) {
        LOGGER.warning(() -> "Stock alert for " + sku + " remaining=" + stock);
    }
}
