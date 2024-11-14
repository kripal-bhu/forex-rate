package org.currency.exchange.dto;

import org.currency.exchange.model.Rate;

import java.time.LocalDate;
import java.util.List;

public class ExchangeRateDto {
    private LocalDate date;
    private String source;
    List<RateDto> rateList;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public List<RateDto> getRateList() {
        return rateList;
    }

    public void setRateList(List<RateDto> rateList) {
        this.rateList = rateList;
    }
}
