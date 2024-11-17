package com.ates.tyol.shopping_cart.service.promotion;

import com.ates.tyol.shopping_cart.entity.cart.Cart;

public interface Promotion {
    void applyDiscountAmount(Cart cart);
    boolean isPromotionApplicable(Cart cart);
}
