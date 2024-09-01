package org.telatenko.storagesevicedomain.exeption;

/**
 * Исключение, которое выбрасывается, когда продукт с указанным идентификатором не найден.
 */
public class NotFoundProductException extends RuntimeException {
    /**
     * Создает новое исключение с указанными параметрами.
     *
     * @param name  Название поля, которое вызвало исключение.
     * @param value Значение поля, которое вызвало исключение.
     */
    public NotFoundProductException(String name, String value) {
        super(String.format("ProductEntity with '%s' = %s not found", name, value));
    }
}
