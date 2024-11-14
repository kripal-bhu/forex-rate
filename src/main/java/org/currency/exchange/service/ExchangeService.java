package org.currency.exchange.service;

import org.currency.exchange.dto.ExchangeRateDto;
import org.currency.exchange.dto.ExchangeRateResponseDTO;
import org.currency.exchange.dto.RateDetailDTO;
import org.currency.exchange.dto.RateDto;
import org.currency.exchange.model.ExchangeRate;
import org.currency.exchange.model.Rate;
import org.currency.exchange.repository.ExchangeRateRepository;
import org.currency.exchange.repository.RateRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
public class ExchangeService {
    private final ExchangeRateRepository exchangeRateRepository;
    private final RateRepository rateRepository;
    private final RestTemplate restTemplate;

    @Value("${api_endpoints.exchange_rate_on_date.url}")
    private String exchangeApiUrl;
    public ExchangeService(ExchangeRateRepository exchangeRateRepository, RateRepository rateRepository, RestTemplate restTemplate) {
        this.exchangeRateRepository = exchangeRateRepository;
        this.rateRepository = rateRepository;
        this.restTemplate = restTemplate;
    }

    public ExchangeRateDto getExchangeRate(String sourceCurrency) {
        LocalDate today = LocalDate.now();
        Optional<ExchangeRate> existingRate = exchangeRateRepository.findByExchangeDateAndSource(today, sourceCurrency);
        if (existingRate.isPresent()) {
            ExchangeRate exchangeRate = existingRate.get();
            ExchangeRateDto exchangeRateDto = new ExchangeRateDto();
            exchangeRateDto.setDate(exchangeRate.getExchangeDate());
            exchangeRateDto.setSource(exchangeRate.getSource());
            List<RateDto> rateDtoList = new ArrayList<>();
            for(Rate rate:exchangeRate.getRates()){
                RateDto rateDto = new RateDto();
                rateDto.setTarget(rate.getCurrency());
                rateDto.setValue(rate.getRateValue());
                rateDtoList.add(rateDto);
            }
            exchangeRateDto.setRateList(rateDtoList);
            return exchangeRateDto;
        } else {
            ExchangeRate exchangeRate = fetchExchangeRateFromApi(today,sourceCurrency);
            if (exchangeRate==null) {
                throw new RuntimeException("No exchange rate found for USD to " + sourceCurrency);
            }
            exchangeRateRepository.save(exchangeRate);
            ExchangeRateDto exchangeRateDto = new ExchangeRateDto();
            exchangeRateDto.setDate(exchangeRate.getExchangeDate());
            exchangeRateDto.setSource(exchangeRate.getSource());
            List<RateDto> rateDtoList = new ArrayList<>();
            for(Rate rate:exchangeRate.getRates()){
                RateDto rateDto = new RateDto();
                rateDto.setTarget(rate.getCurrency());
                rateDto.setValue(rate.getRateValue());
                rateDtoList.add(rateDto);
            }
            exchangeRateDto.setRateList(rateDtoList);
            return exchangeRateDto;
        }
    }

    public ExchangeRateResponseDTO getLatestThreeRates(String targetCurrency) {
        List<Rate> rateList = rateRepository.findTop3ByCurrencyOrderByExchangeDateDesc(targetCurrency);
        ExchangeRateResponseDTO exchangeRateResponseDTO = new ExchangeRateResponseDTO();
        List<Map<LocalDate, RateDetailDTO>> rates = new ArrayList<>();
        if(rateList!=null){
            exchangeRateResponseDTO.setSource("USD");
            Map<LocalDate, RateDetailDTO> ratesMap = new HashMap<>();;
            for(Rate rate:rateList){
                RateDetailDTO rateDetailDTO = new RateDetailDTO();
                rateDetailDTO.setCurrency(rate.getCurrency());
                rateDetailDTO.setRateValue(rate.getRateValue());
                ratesMap.put(rate.getExchangeDate(),rateDetailDTO);
            }
            rates.add(ratesMap);
            exchangeRateResponseDTO.setRates(rates);
        }
        return exchangeRateResponseDTO;
    }

    private ExchangeRate fetchExchangeRateFromApi(LocalDate today, String targetCurrency) {
        String url = exchangeApiUrl + today + "?to=" + targetCurrency;
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        Map<String, Object> rates = (Map<String, Object>) response.getBody().get("rates");
        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setExchangeDate(LocalDate.parse(response.getBody().get("date").toString()));
        exchangeRate.setSource("USD");
        // Parse and add rate values
        Set<Rate> rateList = new HashSet<>();
        for (Map.Entry<String, Object> entry : rates.entrySet()) {
            Rate rate = new Rate();
            rate.setExchangeDate(LocalDate.parse(response.getBody().get("date").toString()));
            rate.setCurrency(entry.getKey());
            rate.setRateValue(new BigDecimal(entry.getValue().toString()));
            rateList.add(rate);
        }
        exchangeRate.setRates(rateList);
        return exchangeRate;
    }
}