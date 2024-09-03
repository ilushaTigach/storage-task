package org.telatenko.storagesevicedomain.scheduler;

import jakarta.persistence.LockModeType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telatenko.storagesevicedomain.annotation.MeasureTransactionalExecutionTime;
import org.telatenko.storagesevicedomain.persistence.ProductEntity;
import org.telatenko.storagesevicedomain.persistence.ProductRepository;
import java.math.BigDecimal;
import java.util.List;

@ConditionalOnProperty(
        value = {"app.scheduling.optimization"},
        havingValue = "false",
        matchIfMissing = true
)
@Component
public class PriceIncreaseScheduler {

    private final ProductRepository productRepository;

    @Value("${app.scheduling.priceMultiplier}")
    private String priceMultiplier;

    public PriceIncreaseScheduler(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @MeasureTransactionalExecutionTime
    @Scheduled(fixedRateString = "${app.scheduling.period}")
    @Transactional
    public void increasePrices() {
        List<ProductEntity> products = productRepository.findAllForScheduler(LockModeType.PESSIMISTIC_WRITE);
        BigDecimal multiplier = new BigDecimal(priceMultiplier);
        for (ProductEntity product : products) {
            product.setPrice(product.getPrice().multiply(multiplier));
        }
        productRepository.saveAll(products);
    }
}
