package org.telatenko.storagesevicedomain.seach.criteria;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.telatenko.storagesevicedomain.seach.strategy.BigDecimalPredicatStrategy;
import org.telatenko.storagesevicedomain.seach.strategy.PredicateStrategy;

import java.math.BigDecimal;

@Getter
public class BigDecimalSearchCriteria implements SearchCriteria<BigDecimal> {

    private static final PredicateStrategy<BigDecimal> strategy = new BigDecimalPredicatStrategy();

    private final String field;


    private final OperationType operation;

    @NotNull
    private final BigDecimal value;

    public BigDecimalSearchCriteria(String field, OperationType operation, BigDecimal value) {
        this.field = field;
        this.operation = operation;
        this.value = value;
    }

    @Override
    public String getField() {
        return field;
    }

    public PredicateStrategy<BigDecimal> getStrategy() {
        return strategy;
    }
}
