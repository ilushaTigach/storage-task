package org.telatenko.storagesevicedomain.seach.criteria;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.telatenko.storagesevicedomain.seach.deserializer.OperationTypeDeserializer;

@JsonDeserialize(using = OperationTypeDeserializer.class)
public enum OperationType {
    EQUALS("="),
    LIKE("~"),
    GREATER_THAN_OR_EQUAL(">="),
    LESS_THAN_OR_EQUAL("<=");

    private final String symbol;

    OperationType(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public static OperationType fromSymbol(String symbol) {
        for (OperationType type : values()) {
            if (type.symbol.equals(symbol)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown operation symbol: " + symbol);
    }
}
