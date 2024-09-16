package org.telatenko.storagesevicedomain.seach.criteria;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.telatenko.storagesevicedomain.seach.strategy.LocalDateTimePredicateStrategy;
import org.telatenko.storagesevicedomain.seach.strategy.PredicateStrategy;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class LocalDateTimeSearchCriteria implements SearchCriteria<LocalDateTime> {

    private static final PredicateStrategy<LocalDateTime> strategy = new LocalDateTimePredicateStrategy();

    private final String field;

    private final OperationType operation;

    @NotNull
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime value;

    @Override
    public String getField() {
        return field;
    }

    public PredicateStrategy<LocalDateTime> getStrategy() {
        return strategy;
    }
}