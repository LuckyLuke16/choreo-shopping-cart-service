package com.example.shoppingcartservice.repository;

import com.example.shoppingcartservice.model.entity.ShoppingCartItem;
import com.example.shoppingcartservice.model.entity.ShoppingCartItemId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoppingCartItemRepository extends JpaRepository<ShoppingCartItem, ShoppingCartItemId> {
    List<ShoppingCartItem> findByUserId(String userId);

}
