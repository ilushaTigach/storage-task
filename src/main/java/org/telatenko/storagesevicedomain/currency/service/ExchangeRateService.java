package org.telatenko.storagesevicedomain.currency.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class ExchangeRateService {

    private final CurrencyServiceClient currencyServiceClient;
    private final ObjectMapper objectMapper;

    public ExchangeRateService(CurrencyServiceClient currencyServiceClient, ObjectMapper objectMapper) {
        this.currencyServiceClient = currencyServiceClient;
        this.objectMapper = objectMapper;
    }

    @Cacheable(value = "exchangeRates", unless = "#result == null")
    public Map<String, BigDecimal> getExchangeRates() {
        Map<String, BigDecimal> rates = fetchExchangeRatesFromExternalService();
        if (rates == null || rates.isEmpty()) {
            rates = fetchExchangeRatesFromLocalFile();
        }
        return rates;
    }

    private Map<String, BigDecimal> fetchExchangeRatesFromExternalService() {
        log.info("Fetching exchange rates from external service");
        try {
            Map<String, BigDecimal> rates = currencyServiceClient.getExchangeRates();
            if (rates != null && !rates.isEmpty()) {
                log.info("Successfully fetched exchange rates from external service: {}", rates);
                return rates;
            } else {
                log.warn("Empty or null response from external service");
            }
        } catch (Exception e) {
            log.error("Failed to fetch exchange rates from external service", e);
        }
        return null;
    }

    private Map<String, BigDecimal> fetchExchangeRatesFromLocalFile() {
        log.info("Fetching exchange rates from local JSON file");
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("exchange-rate.json")) {
            if (inputStream == null) {
                log.error("Local JSON file not found");
                return new HashMap<>();
            }
            Map<String, BigDecimal> rates = objectMapper.readValue(inputStream, new TypeReference<Map<String, BigDecimal>>() {});
            log.info("Successfully fetched exchange rates from local JSON file: {}", rates);
            return rates;
        } catch (IOException e) {
            log.error("Failed to fetch exchange rates from local JSON file", e);
            return new HashMap<>();
        }
    }
}
