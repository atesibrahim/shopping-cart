package com.ates.tyol.shopping_cart.event;

import com.ates.tyol.shopping_cart.service.cart.CartService;

public class CartResetEvent implements Event {

    @Override
    public String execute(CartService cartServiceImpl) {
        cartServiceImpl.resetCart();
        return "{\"result\":true, \"message\":\"Cart has been reset\"}";
    }
}
