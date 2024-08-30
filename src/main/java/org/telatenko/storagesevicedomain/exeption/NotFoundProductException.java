package org.telatenko.storagesevicedomain.exeption;

public class NotFoundProductException extends RuntimeException {
    public NotFoundProductException(String name, String value) {
        super(String.format("ProductEntity with '%s' = %s not found", name, value));
    }
}
