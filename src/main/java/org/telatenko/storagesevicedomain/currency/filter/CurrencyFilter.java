package org.telatenko.storagesevicedomain.currency.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.telatenko.storagesevicedomain.currency.provider.CurrencyProvider;
import java.io.IOException;

@Slf4j
@Component
public class CurrencyFilter extends OncePerRequestFilter {

    private final CurrencyProvider currencyProvider;

    public CurrencyFilter(CurrencyProvider currencyProvider) {
        this.currencyProvider = currencyProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String currency = request.getHeader("currency");
        if (currency != null) {
            log.info("Setting currency to: {}", currency);
            currencyProvider.setCurrency(currency);
        } else {
            log.info("Currency header is not present, using default currency: {}", currencyProvider.getCurrency());
        }
        filterChain.doFilter(request, response);
    }
}
