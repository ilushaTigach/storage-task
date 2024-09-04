package org.telatenko.storagesevicedomain.exeption;

public class ArticleExistsExeption extends RuntimeException {
    /**
     * Создает новое исключение с указанными параметрами.
     *
     * @param name   Название поля, которое вызвало исключение.
     * @param value  Значение поля, которое вызвало исключение.
     * @param nameId Название поля идентификатора.
     * @param id     Значение идентификатора существующего продукта.
     */
    public ArticleExistsExeption(String name, String value, String nameId, String id) {
        super(String.format("ProductEntity with '%s' = %s already exists. His '%s' = %s", name, value, nameId, id));
    }
}
