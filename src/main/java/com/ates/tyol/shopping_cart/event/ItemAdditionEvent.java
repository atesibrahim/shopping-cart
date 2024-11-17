package com.ates.tyol.shopping_cart.event;

import com.ates.tyol.shopping_cart.entity.item.Item;
import com.ates.tyol.shopping_cart.service.cart.CartService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ItemAdditionEvent implements Event {
    private static final String FAILED_TO_ADD_ITEM_TO_CART_DUE_TO = "Failed to add item to cart due to: ";
    private final Item item;

    @Override
    public String execute(CartService cartServiceImpl) {

        String failedReason = FAILED_TO_ADD_ITEM_TO_CART_DUE_TO;
        boolean result;
        try {
            result = cartServiceImpl.addItem(item);
        } catch (Exception e) {
            result = false;
            failedReason += e.getMessage();
        }

        return result ? "{\"result\":true, \"message\":\"Item added successfully\"}"
                : "{\"result\":false, \"message\":\"" + failedReason + "\"}";
    }
}