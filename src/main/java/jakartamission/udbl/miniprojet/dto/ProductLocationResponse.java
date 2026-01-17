package jakartamission.udbl.miniprojet.dto;

import java.time.LocalDateTime;

public class ProductLocationResponse {

    private String sku;
    private String name;
    private String locationCode;
    private String locationNote;
    private int stockQuantity;
    private LocalDateTime updatedAt;

    public ProductLocationResponse() {
    }

    public ProductLocationResponse(String sku, String name, String locationCode, String locationNote, int stockQuantity,
            LocalDateTime updatedAt) {
        this.sku = sku;
        this.name = name;
        this.locationCode = locationCode;
        this.locationNote = locationNote;
        this.stockQuantity = stockQuantity;
        this.updatedAt = updatedAt;
    }

    public String getSku() {
        return sku;
    }

    public String getName() {
        return name;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public String getLocationNote() {
        return locationNote;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
