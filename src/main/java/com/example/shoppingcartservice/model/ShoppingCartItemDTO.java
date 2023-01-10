package com.example.shoppingcartservice.model;

import lombok.Data;

import java.util.HashMap;

@Data
public class ShoppingCartItemDTO {

    private HashMap<Integer, Integer> itemsFromShoppingCart;
}
