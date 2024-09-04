package org.telatenko.storagesevicedomain.exeption;

/**
 * Исключение, которое выбрасывается, когда попытка удаления уже удаленного продукта.
 */
public class DeleteObjectExeption  extends RuntimeException{
    /**
     * Создает новое исключение с сообщением о том, что продукт уже удален.
     */
    public DeleteObjectExeption(){
        super(String.format("ProductEntity already deleted"));
    }
}
