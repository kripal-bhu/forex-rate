package org.currency.exchange.dto;

import java.math.BigDecimal;

public class RateDetailDTO {
    private String currency;
    private BigDecimal rateValue;

    // Getters and Setters

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getRateValue() {
        return rateValue;
    }

    public void setRateValue(BigDecimal rateValue) {
        this.rateValue = rateValue;
    }
}
