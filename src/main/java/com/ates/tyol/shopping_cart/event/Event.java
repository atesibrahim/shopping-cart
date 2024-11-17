package com.ates.tyol.shopping_cart.event;

import com.ates.tyol.shopping_cart.service.cart.CartService;

public interface Event {
    String execute(CartService cartService);
}
