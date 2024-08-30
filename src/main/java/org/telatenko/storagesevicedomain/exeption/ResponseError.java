package org.telatenko.storagesevicedomain.exeption;

import lombok.Data;
import org.springframework.http.HttpStatus;
import java.util.Map;

@Data
public class ResponseError {

    private final HttpStatus status;

    private final String message;

    private Map<String, String> errors;

    public ResponseError(HttpStatus status,
                         String message
                                ) {
        this.status = status;
        this.message = message;

    }

    public ResponseError(HttpStatus status, String message, Map<String, String> errors) {
        this.status = status;
        this.message = message;
        this.errors = errors;
    }
}
