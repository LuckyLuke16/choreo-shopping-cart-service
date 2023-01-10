package com.example.shoppingcartservice.exception;

public class NoItemsFoundException extends RuntimeException{

    public NoItemsFoundException() {
        super("No items found");
    }
}
