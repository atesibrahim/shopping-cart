package com.ates.tyol.shopping_cart.entity.item;

import com.ates.tyol.shopping_cart.constant.DefinitionConstants;
import com.ates.tyol.shopping_cart.constant.ErrorMessageConstants;

public class DigitalItem extends Item {

    public DigitalItem(int itemId, int categoryId, int sellerId, double price, int quantity) {
        super(itemId, categoryId, sellerId, price, quantity);
        if (quantity > DefinitionConstants.MAX_DIGITAL_ITEM_QUANTITY) {
            throw new IllegalArgumentException(ErrorMessageConstants.DIGITAL_QUANTITY_CAN_NOT_EXCEEDS_COUNT + DefinitionConstants.MAX_DIGITAL_ITEM_QUANTITY);
        }
    }
}