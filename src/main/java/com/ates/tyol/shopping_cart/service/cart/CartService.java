package com.ates.tyol.shopping_cart.service.cart;

import com.ates.tyol.shopping_cart.entity.item.Item;
import com.ates.tyol.shopping_cart.entity.item.VasItem;

public interface CartService {

    boolean addItem(Item item);

    boolean addVasItemToItem(int itemId, VasItem vasItem);

    boolean removeItem(int itemId);

    void resetCart();

    String displayCart();
}