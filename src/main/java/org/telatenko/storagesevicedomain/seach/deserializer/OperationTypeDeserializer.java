package org.telatenko.storagesevicedomain.seach.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.telatenko.storagesevicedomain.seach.criteria.OperationType;

import java.io.IOException;

public class OperationTypeDeserializer extends JsonDeserializer<OperationType> {

    @Override
    public OperationType deserialize(JsonParser p, DeserializationContext ctxt) throws IOException{
        String symbol = p.getText();
        return OperationType.fromSymbol(symbol);
    }
}
