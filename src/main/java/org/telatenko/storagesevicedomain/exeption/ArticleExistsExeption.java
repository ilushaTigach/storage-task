package org.telatenko.storagesevicedomain.exeption;

public class ArticleExistsExeption extends RuntimeException {
    public ArticleExistsExeption(String name, String value, String nameId, String id) {
        super(String.format("ProductEntity with '%s' = %s already exists. His '%s' = %s", name, value, nameId, id));
    }
}
