package org.telatenko.storagesevicedomain.seach.criteria;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotNull;
import org.telatenko.storagesevicedomain.seach.strategy.PredicateStrategy;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        visible = true,
        include = JsonTypeInfo.As.PROPERTY,
        property = "field")
@JsonSubTypes({
        @JsonSubTypes.Type(value = StringSearchCriteria.class, name = "title"),
        @JsonSubTypes.Type(value = LocalDateTimeSearchCriteria.class, name = "createdAd"),
        @JsonSubTypes.Type(value = StringSearchCriteria.class, name = "description"),
        @JsonSubTypes.Type(value = BigDecimalSearchCriteria.class, name = "price")
})
public interface SearchCriteria<T> {

    String getField();

    @NotNull
    OperationType getOperation();

    @NotNull
    T getValue();

    PredicateStrategy<T> getStrategy();
}