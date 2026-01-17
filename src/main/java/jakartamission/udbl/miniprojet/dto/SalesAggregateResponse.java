package jakartamission.udbl.miniprojet.dto;

import java.math.BigDecimal;

public class SalesAggregateResponse {

    private String period;
    private int totalQuantity;
    private BigDecimal totalAmount;

    public SalesAggregateResponse(String period, int totalQuantity, BigDecimal totalAmount) {
        this.period = period;
        this.totalQuantity = totalQuantity;
        this.totalAmount = totalAmount;
    }

    public String getPeriod() {
        return period;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
}
