package com.ates.tyol.shopping_cart.factory;

import com.ates.tyol.shopping_cart.constant.DefinitionConstants;
import com.ates.tyol.shopping_cart.constant.ErrorMessageConstants;
import com.ates.tyol.shopping_cart.entity.item.DefaultItem;
import com.ates.tyol.shopping_cart.entity.item.DigitalItem;
import com.ates.tyol.shopping_cart.event.Event;
import com.ates.tyol.shopping_cart.event.ItemAdditionEvent;
import com.ates.tyol.shopping_cart.value.ItemPayload;

public class ItemEventFactory {
    public static Event createAddItemEvent(ItemPayload payload) {
        if (payload.categoryId() == DefinitionConstants.DIGITAL_ITEM_CATEGORY_ID) {
            return new ItemAdditionEvent(new DigitalItem(payload.itemId(), payload.categoryId(), payload.sellerId(), payload.price(), payload.quantity()));
        } else if (payload.categoryId() == DefinitionConstants.VAS_ITEM_CATEGORY_ID) {
            throw new IllegalArgumentException(ErrorMessageConstants.VAS_ITEM_CANNOT_BE_ADDED_DIRECTLY_TO_CART_ERROR);
        }
        return new ItemAdditionEvent(new DefaultItem(payload.itemId(), payload.categoryId(), payload.sellerId(), payload.price(), payload.quantity()));
    }
}
