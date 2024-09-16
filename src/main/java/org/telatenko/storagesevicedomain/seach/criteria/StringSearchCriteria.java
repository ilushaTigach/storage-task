package org.telatenko.storagesevicedomain.seach.criteria;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.telatenko.storagesevicedomain.seach.strategy.PredicateStrategy;
import org.telatenko.storagesevicedomain.seach.strategy.StringPredicateStrategy;

@Getter
public class StringSearchCriteria implements SearchCriteria<String>{

    private static final PredicateStrategy<String> strategy = new StringPredicateStrategy();

    private final String field;

    private final OperationType operation;


    @NotBlank
    private final String value;

    public StringSearchCriteria(String field, OperationType operation, String value) {
        this.field = field;
        this.operation = operation;
        this.value = value;
    }

    @Override
    public String getField() {
        return field;
    }


    public PredicateStrategy<String> getStrategy() {
        return strategy;
    }
}

