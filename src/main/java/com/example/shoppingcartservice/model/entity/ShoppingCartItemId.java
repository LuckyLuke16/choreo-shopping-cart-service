package com.example.shoppingcartservice.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ShoppingCartItemId implements Serializable {

    private String userId;

    private int productId;
}
