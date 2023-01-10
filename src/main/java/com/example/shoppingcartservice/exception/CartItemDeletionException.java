package com.example.shoppingcartservice.exception;

public class CartItemDeletionException extends RuntimeException {
    public CartItemDeletionException(){super("Item could not be deleted");}
}
