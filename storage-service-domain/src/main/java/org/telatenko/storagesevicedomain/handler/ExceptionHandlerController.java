package org.telatenko.storagesevicedomain.handler;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.telatenko.storagesevicedomain.exeption.NotFoundProductException;
import org.telatenko.storagesevicedomain.exeption.ResponseError;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(value = {ConstraintViolationException.class})
    public ResponseEntity<ResponseError> handleValidationExceptions(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.put(
                    violation.getPropertyPath().toString(),
                    violation.getMessage()
            );
        }
        ResponseError responseError = new ResponseError(
                HttpStatus.BAD_REQUEST,
                "Validation failed",
                errors
        );
        return new ResponseEntity<>(responseError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {NotFoundProductException.class})
    public ResponseEntity<ResponseError> handleException(NotFoundProductException exception) {
        ResponseError responseError = new ResponseError(
                HttpStatus.NOT_FOUND,
                exception.getMessage()
        );
        return new ResponseEntity<>(responseError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {RuntimeException.class})
    public ResponseEntity<ResponseError> handleException(RuntimeException exception) {
        ResponseError responseError = new ResponseError(
                HttpStatus.UNPROCESSABLE_ENTITY,
                exception.getMessage()
        );
        return new ResponseEntity<>(responseError, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}