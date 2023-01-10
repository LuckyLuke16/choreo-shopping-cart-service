package com.example.shoppingcartservice.controller;

import com.example.shoppingcartservice.model.ItemQuantityDTO;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/shopping-cart")
public interface ShoppingCartOperations {

    @GetMapping
    ItemQuantityDTO fetchShoppingCartContent(@RequestParam String user);

}
