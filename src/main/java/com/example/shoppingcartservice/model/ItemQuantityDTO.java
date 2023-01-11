package com.example.shoppingcartservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemQuantityDTO {

    private HashMap<Integer, Integer> itemsFromShoppingCart;
}
