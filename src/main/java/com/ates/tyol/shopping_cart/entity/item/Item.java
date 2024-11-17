package com.ates.tyol.shopping_cart.entity.item;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class Item {
    private int itemId;
    private int categoryId;
    private int sellerId;
    private double price;
    private int quantity;
}
