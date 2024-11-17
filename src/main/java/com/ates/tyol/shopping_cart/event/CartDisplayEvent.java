package com.ates.tyol.shopping_cart.event;

import com.ates.tyol.shopping_cart.service.cart.CartService;

public class CartDisplayEvent implements Event {
    private static final String FAILED_TO_DISPLAY_DUE_TO = "Failed to display cart due to ";

    @Override
    public String execute(CartService cartServiceImpl) {
        try {
            String cartDetails = cartServiceImpl.displayCart();
            return "{\"result\":true, \"message\":" + cartDetails + "}";
        } catch (Exception e) {
            String failedReason = FAILED_TO_DISPLAY_DUE_TO;
            failedReason += e.getMessage();
            return "{\"result\":false, \"message\":\"" + failedReason + " \"}";
        }
    }
}
