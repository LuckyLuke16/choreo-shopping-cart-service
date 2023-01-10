package com.example.shoppingcartservice.model.order;

import com.example.shoppingcartservice.model.ItemQuantityDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {

    private String paymentMethod;

    private Address address;

    private ItemQuantityDTO orderedItems;
}
