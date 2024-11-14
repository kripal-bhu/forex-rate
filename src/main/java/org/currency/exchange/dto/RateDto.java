package org.currency.exchange.dto;

import java.math.BigDecimal;

public class RateDto {
    private String target;
    private BigDecimal value;

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
