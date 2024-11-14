package org.currency.exchange.service;

import org.currency.exchange.ExchangeApplication;
import org.currency.exchange.dto.ExchangeRateResponseDTO;
import org.currency.exchange.dto.RateDetailDTO;
import org.currency.exchange.model.Rate;
import org.currency.exchange.repository.RateRepository;
import org.currency.exchange.service.ExchangeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
@ContextConfiguration(classes = ExchangeApplication.class)
public class ExchangeServiceTest {

        @Mock
        private RateRepository rateRepository;

        @InjectMocks
        private ExchangeService exchangeService;

        @BeforeEach
        void setUp() {
            // This can be used to initialize any necessary objects before each test if needed
        }

        @Test
        void testGetLatestThreeRates() {
            // mock data for Rate objects
            List<Rate> mockRates = new ArrayList<>();

            Rate rate1 = new Rate();
            rate1.setCurrency("EUR");
            rate1.setExchangeDate(LocalDate.of(2024, 11, 14));
            rate1.setRateValue(new BigDecimal("0.91"));

            Rate rate2 = new Rate();
            rate2.setCurrency("EUR");
            rate2.setExchangeDate(LocalDate.of(2024, 11, 13));
            rate2.setRateValue(new BigDecimal("0.92"));

            Rate rate3 = new Rate();
            rate3.setCurrency("EUR");
            rate3.setExchangeDate(LocalDate.of(2024, 11, 12));
            rate3.setRateValue(new BigDecimal("0.93"));

            mockRates.add(rate1);
            mockRates.add(rate2);
            mockRates.add(rate3);

            // Mock the rateRepository to return the mock data when the method is called
            when(rateRepository.findTop3ByCurrencyOrderByExchangeDateDesc("EUR")).thenReturn(mockRates);

            // Call the method under test
            ExchangeRateResponseDTO responseDTO = exchangeService.getLatestThreeRates("EUR");

            // Validate the results
            assertNotNull(responseDTO);
            assertEquals("USD", responseDTO.getSource());  // Ensure source is "USD"

            List<Map<LocalDate, RateDetailDTO>> rates = responseDTO.getRates();
            assertNotNull(rates);
            assertEquals(1, rates.size());  // It should contain one entry since all rates go into the same map

            Map<LocalDate, RateDetailDTO> ratesMap = rates.get(0);
            assertNotNull(ratesMap);
            assertEquals(3, ratesMap.size());

            // Validate the rates for the expected dates
            RateDetailDTO rateDetail1 = ratesMap.get(LocalDate.of(2024, 11, 14));
            assertNotNull(rateDetail1);
            assertEquals("EUR", rateDetail1.getCurrency());
            assertEquals(new BigDecimal("0.91"), rateDetail1.getRateValue());

            RateDetailDTO rateDetail2 = ratesMap.get(LocalDate.of(2024, 11, 13));
            assertNotNull(rateDetail2);
            assertEquals("EUR", rateDetail2.getCurrency());
            assertEquals(new BigDecimal("0.92"), rateDetail2.getRateValue());

            RateDetailDTO rateDetail3 = ratesMap.get(LocalDate.of(2024, 11, 12));
            assertNotNull(rateDetail3);
            assertEquals("EUR", rateDetail3.getCurrency());
            assertEquals(new BigDecimal("0.93"), rateDetail3.getRateValue());
        }
    }

