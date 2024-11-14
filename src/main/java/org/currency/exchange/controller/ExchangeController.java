package org.currency.exchange.controller;

import org.currency.exchange.dto.ExchangeRateDto;
import org.currency.exchange.dto.ExchangeRateResponseDTO;
import org.currency.exchange.model.ExchangeRate;
import org.currency.exchange.model.Rate;
import org.currency.exchange.service.ExchangeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fx")
public class ExchangeController {
        private final ExchangeService exchangeService;

        public ExchangeController(ExchangeService exchangeService) {
            this.exchangeService = exchangeService;
        }

        @GetMapping
        public ExchangeRateDto getExchangeRate(@RequestParam String to) {
            return exchangeService.getExchangeRate(to);
        }

        @GetMapping("/{targetCurrency}")
        public ExchangeRateResponseDTO getLatestThreeRates(@PathVariable String targetCurrency) {
            return exchangeService.getLatestThreeRates(targetCurrency);
        }
    }
