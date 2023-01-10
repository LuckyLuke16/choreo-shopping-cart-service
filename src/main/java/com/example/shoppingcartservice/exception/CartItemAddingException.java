package com.example.shoppingcartservice.exception;

public class CartItemAddingException extends RuntimeException {

    public CartItemAddingException(int itemId){super("Item with id " + itemId +"could not be deleted");}

}
