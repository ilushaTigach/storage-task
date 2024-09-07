package org.telatenko.storagesevicedomain.currency.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.Map;

@Slf4j
@Service
public class CurrencyServiceClientImpl implements CurrencyServiceClient {

    private final WebClient webClient;

    public CurrencyServiceClientImpl(WebClient.Builder webClientBuilder, @Value("${currency.service.url}") String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    @Override
    public Map<String, BigDecimal> getExchangeRates() {
        log.info("Fetching exchange rates from external service");
        try {
            Map<String, BigDecimal> rates = webClient.get()
                    .uri("/exchange-rates")
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, BigDecimal>>() {})
                    .retryWhen(Retry.fixedDelay(2, Duration.ofSeconds(1)))
                    .doOnSuccess(response -> log.info("Successfully fetched exchange rates: {}", response))
                    .doOnError(error -> log.error("Failed to fetch exchange rates from external service", error))
                    .block();
            return rates;
        } catch (Exception e) {
            log.error("Failed to fetch exchange rates from external service", e);
            throw e;
        }
    }
}