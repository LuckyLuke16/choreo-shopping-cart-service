package com.example.shoppingcartservice.service;

import com.example.shoppingcartservice.exception.CartItemAddingException;
import com.example.shoppingcartservice.exception.CartItemDeletionException;
import com.example.shoppingcartservice.exception.NoItemsFoundException;
import com.example.shoppingcartservice.model.ShoppingCartItemDTO;
import com.example.shoppingcartservice.model.entity.ShoppingCartItem;
import com.example.shoppingcartservice.model.entity.ShoppingCartItemId;
import com.example.shoppingcartservice.repository.ShoppingCartItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class ShoppingCartItemService {

    Logger logger = LoggerFactory.getLogger(ShoppingCartItemService.class);

    private ShoppingCartItemRepository shoppingCartItemRepository;


    public ShoppingCartItemService(ShoppingCartItemRepository shoppingCartItemRepository) {
        this.shoppingCartItemRepository = shoppingCartItemRepository;
    }

    public ShoppingCartItemDTO fetchItemsOfUser(String userId) {
        ShoppingCartItemDTO itemsWithQuantity = new ShoppingCartItemDTO();

        try {
            List<ShoppingCartItem> itemsFromDB = shoppingCartItemRepository.findByUserId(userId);
            if(!itemsFromDB.isEmpty())
                itemsWithQuantity = itemToShoppingCartItemDTO(itemsFromDB);
        } catch (Exception e) {
            throw new NoItemsFoundException();
        }
        if(itemsWithQuantity.getItemsFromShoppingCart().isEmpty())
            throw new NoItemsFoundException();

        return itemsWithQuantity;
    }

    private ShoppingCartItemDTO itemToShoppingCartItemDTO(List<ShoppingCartItem> itemsFromDB) {
        ShoppingCartItemDTO itemsWithQuantity = new ShoppingCartItemDTO();
        itemsWithQuantity.setItemsFromShoppingCart(new HashMap<>());

        for(ShoppingCartItem i : itemsFromDB) {
            try {
                itemsWithQuantity.getItemsFromShoppingCart().put(i.getProductId(),i.getQuantity());
            } catch (Exception e) {
                logger.warn("Item with id {} could not be converted to an ItemDTO", i.getProductId());
            }
        }

        return itemsWithQuantity;
    }

    public void deleteShoppingCartItem(int itemId, String userId) {
        try {
            Optional<ShoppingCartItem> itemToDelete = this.shoppingCartItemRepository.findById(new ShoppingCartItemId(userId, itemId));
            itemToDelete.ifPresent(this::reduceQuantityByOne);
            if (itemToDelete.isEmpty()) {
                logger.warn("Item with id: {} of user: {} was not found", itemId, userId);
                throw new NoItemsFoundException();
            }
        } catch(Exception e) {
            logger.warn("Item with id: {} of user: {} could not be deleted", itemId, userId);
            throw new CartItemDeletionException();
        }
    }

    private void reduceQuantityByOne(ShoppingCartItem itemToDelete) {
        this.shoppingCartItemRepository.deleteById(new ShoppingCartItemId(itemToDelete.getUserId(), itemToDelete.getProductId()));
        if(itemToDelete.getQuantity() > 1) {
            itemToDelete.setQuantity(itemToDelete.getQuantity() - 1);
            this.shoppingCartItemRepository.save(itemToDelete);
        }
    }

    public Integer addShoppingCartItem(int itemId, String userId) {
        try{
            Optional<ShoppingCartItem> itemToAdd = this.shoppingCartItemRepository.findById(new ShoppingCartItemId(userId, itemId));
            itemToAdd.ifPresent(this::raiseQuantityOfItem);
            if (itemToAdd.isEmpty()) {
                this.addItemToShoppingCart(itemId, userId);
                logger.info("Item with id: {} was added to shopping cart", itemId);
                return 1;
            }else {
                logger.info("Quantity of item with id: {} was added to shopping cart", itemId);
                return itemToAdd.get().getQuantity() + 1;
            }

        } catch(Exception e) {
            logger.warn("Item with id: {} of user: {} could not be added", itemId, userId);
            throw new CartItemAddingException(itemId);
        }
    }

    private void addItemToShoppingCart(int itemId, String userId) {
        ShoppingCartItem itemToAdd = new ShoppingCartItem();
        itemToAdd.setProductId(itemId);
        itemToAdd.setUserId(userId);
        itemToAdd.setQuantity(1);
        this.shoppingCartItemRepository.save(itemToAdd);
    }

    private void raiseQuantityOfItem(ShoppingCartItem itemToAdd) {
        this.shoppingCartItemRepository.deleteById(new ShoppingCartItemId(itemToAdd.getUserId(), itemToAdd.getProductId()));
        itemToAdd.setQuantity(itemToAdd.getQuantity() + 1);
        this.shoppingCartItemRepository.save(itemToAdd);
    }
}
