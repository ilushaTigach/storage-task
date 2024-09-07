package org.telatenko.storagesevicedomain.currency.service;

import java.math.BigDecimal;
import java.util.Map;

public interface CurrencyServiceClient {
    Map<String, BigDecimal> getExchangeRates();
}
