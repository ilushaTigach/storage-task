package org.telatenko.storagesevicedomain.exeption;

public class DeleteObjectExeption  extends RuntimeException{
    public DeleteObjectExeption(){
        super(String.format("ProductEntity already deleted"));
    }
}
