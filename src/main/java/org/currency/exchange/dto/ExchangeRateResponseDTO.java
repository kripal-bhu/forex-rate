package org.currency.exchange.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class ExchangeRateResponseDTO {

    private String source;
    private List<Map<LocalDate, RateDetailDTO> > rates;
    // Getters and Setters

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public List<Map<LocalDate, RateDetailDTO>> getRates() {
        return rates;
    }

    public void setRates(List<Map<LocalDate, RateDetailDTO>> rates) {
        this.rates = rates;
    }
}
