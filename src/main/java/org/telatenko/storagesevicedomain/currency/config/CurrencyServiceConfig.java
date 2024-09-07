package org.telatenko.storagesevicedomain.currency.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.telatenko.storagesevicedomain.currency.service.CurrencyServiceClient;
import org.telatenko.storagesevicedomain.currency.service.CurrencyServiceClientImpl;
import org.telatenko.storagesevicedomain.currency.service.CurrencyServiceClientMock;

@Configuration
public class CurrencyServiceConfig {

    @Bean
    @Primary
    public CurrencyServiceClient currencyServiceClient(@Value("${currency.service.mock}") boolean isMock,
                                                       CurrencyServiceClientImpl currencyServiceClientImpl,
                                                       CurrencyServiceClientMock currencyServiceClientMock) {
        return isMock ? currencyServiceClientMock : currencyServiceClientImpl;
    }
}
