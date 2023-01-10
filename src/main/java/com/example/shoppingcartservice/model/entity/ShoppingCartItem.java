package com.example.shoppingcartservice.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Data
@Entity
@IdClass(ShoppingCartItemId.class)
@Table(name = "items")
public class ShoppingCartItem {

    @Id
    private int productId;

    @Id
    private String userId;

    private int quantity;

}
