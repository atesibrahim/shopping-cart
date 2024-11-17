package com.ates.tyol.shopping_cart.event;

import com.ates.tyol.shopping_cart.constant.ErrorMessageConstants;
import com.ates.tyol.shopping_cart.service.cart.CartService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ItemRemoveEvent implements Event {
    private final int itemId;

    @Override
    public String execute(CartService cartServiceImpl) {
        String failedReason = ErrorMessageConstants.FAILED_TO_REMOVE_ITEM_DUE_TO;
        boolean result;
        try {
            result = cartServiceImpl.removeItem(itemId);
        } catch (Exception e) {
            result = false;
            failedReason += e.getMessage();
        }
        return result ? "{\"result\":true, \"message\":\"Item removed successfully\"}"
                : "{\"result\":false, \"message\":\"" + failedReason + " \"}";
    }
}