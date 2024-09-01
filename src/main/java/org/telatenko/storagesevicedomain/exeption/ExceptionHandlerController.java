package org.telatenko.storagesevicedomain.exeption;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.HashMap;
import java.util.Map;

/**
 * Глобальный обработчик исключений для REST контроллеров.
 * Этот класс предоставляет методы для обработки различных исключений и возвращения соответствующих HTTP ответов.
 */
@RestControllerAdvice
public class ExceptionHandlerController {

    /**
     * Обрабатывает исключения, связанные с нарушением ограничений валидации.
     *
     * @param ex Исключение ConstraintViolationException.
     * @return ResponseEntity с объектом ResponseError, содержащим информацию об ошибках валидации.
     */
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

    /**
     * Обрабатывает исключение NotFoundProductException.
     *
     * @param exception Исключение NotFoundProductException.
     * @return ResponseEntity с объектом ResponseError, содержащим информацию об ошибке.
     */
    @ExceptionHandler(value = {NotFoundProductException.class})
    public ResponseEntity<ResponseError> handleException(NotFoundProductException exception) {
        ResponseError responseError = new ResponseError(
                HttpStatus.NOT_FOUND,
                exception.getMessage()
        );
        return new ResponseEntity<>(responseError, HttpStatus.NOT_FOUND);
    }

    /**
     * Обрабатывает общие исключения RuntimeException.
     *
     * @param exception Исключение RuntimeException.
     * @return ResponseEntity с объектом ResponseError, содержащим информацию об ошибке.
     */
    @ExceptionHandler(value = {RuntimeException.class})
    public ResponseEntity<ResponseError> handleException(RuntimeException exception) {
        ResponseError responseError = new ResponseError(
                HttpStatus.UNPROCESSABLE_ENTITY,
                exception.getMessage()
        );
        return new ResponseEntity<>(responseError, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    /**
     * Обрабатывает исключение ArticleExistsExeption.
     *
     * @param exception Исключение ArticleExistsExeption.
     * @return ResponseEntity с объектом ResponseError, содержащим информацию об ошибке.
     */
    @ExceptionHandler(value = {ArticleExistsExeption.class})
    public ResponseEntity<ResponseError> handleException(ArticleExistsExeption exception) {
        ResponseError responseError = new ResponseError(
                HttpStatus.BAD_REQUEST,
                exception.getMessage()
        );
        return new ResponseEntity<>(responseError, HttpStatus.BAD_REQUEST);
    }

    /**
     * Обрабатывает исключение DeleteObjectExeption.
     *
     * @param exception Исключение DeleteObjectExeption.
     * @return ResponseEntity с объектом ResponseError, содержащим информацию об ошибке.
     */
    @ExceptionHandler(value = {DeleteObjectExeption.class})
    public ResponseEntity<ResponseError> handleException(DeleteObjectExeption exception) {
        ResponseError responseError = new ResponseError(
                HttpStatus.OK,
                exception.getMessage()
        );
        return new ResponseEntity<>(responseError, HttpStatus.OK);
    }
}