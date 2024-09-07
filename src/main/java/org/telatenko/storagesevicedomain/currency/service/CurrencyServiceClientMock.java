package org.telatenko.storagesevicedomain.currency.service;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class CurrencyServiceClientMock implements CurrencyServiceClient {

    private final Random random = new Random();

    @Override
    public Map<String, BigDecimal> getExchangeRates() {
        Map<String, BigDecimal> rates = new HashMap<>();
        rates.put("CNY", new BigDecimal(random.nextDouble() * 100));
        rates.put("USD", new BigDecimal(random.nextDouble() * 100));
        rates.put("EUR", new BigDecimal(random.nextDouble() * 100));
        return rates;
    }
}
