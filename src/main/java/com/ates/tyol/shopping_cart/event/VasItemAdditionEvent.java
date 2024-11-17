package com.ates.tyol.shopping_cart.event;

import com.ates.tyol.shopping_cart.constant.ErrorMessageConstants;
import com.ates.tyol.shopping_cart.entity.item.VasItem;
import com.ates.tyol.shopping_cart.service.cart.CartService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class VasItemAdditionEvent implements Event {
    private final int itemId;
    private final VasItem vasItem;

    @Override
    public String execute(CartService cartServiceImpl) {
        boolean result;
        String failedReason = ErrorMessageConstants.FAILED_TO_ADD_VAS_ITEM_DUE_TO;
        try {
            result = cartServiceImpl.addVasItemToItem(itemId, vasItem);
        } catch (Exception e) {
            result = false;
            failedReason += e.getMessage();
        }

        return result ? "{\"result\":true, \"message\":\"VAS item added successfully\"}"
                : "{\"result\":false, \"message\":\"" + failedReason + "\"}";
    }
}
