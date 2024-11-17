package com.ates.tyol.shopping_cart.entity.item;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class DefaultItem extends Item {
    private final List<VasItem> vasItems;

    public DefaultItem(int itemId, int categoryId, int sellerId, double price, int quantity) {
        super(itemId, categoryId, sellerId, price, quantity);
        vasItems = new ArrayList<>();
    }

    public void addVasItem(VasItem vasItem) {
        vasItems.add(vasItem);
    }
}