package com.example.shoppingcartservice.controller;


import com.example.shoppingcartservice.model.ShoppingCartItemDTO;
import com.example.shoppingcartservice.service.ShoppingCartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;



@RestController
public class ShoppingCartController implements ShoppingCartOperations {

    private final ShoppingCartItemService shoppingCartItemService;

    @Autowired
    public ShoppingCartController(ShoppingCartItemService shoppingCartItemService) {
        this.shoppingCartItemService = shoppingCartItemService;
    }

    public ShoppingCartItemDTO fetchShoppingCartContent(String user) {
        ShoppingCartItemDTO itemsWithQuantity;

        try {
            itemsWithQuantity = shoppingCartItemService.fetchItemsOfUser(user);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return itemsWithQuantity;
    }
}
