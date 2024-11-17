package com.ates.tyol.shopping_cart.factory;

import com.ates.tyol.shopping_cart.constant.DefinitionConstants;
import com.ates.tyol.shopping_cart.constant.ErrorMessageConstants;
import com.ates.tyol.shopping_cart.entity.item.VasItem;
import com.ates.tyol.shopping_cart.event.Event;
import com.ates.tyol.shopping_cart.event.VasItemAdditionEvent;
import com.ates.tyol.shopping_cart.value.VasItemPayload;

public class VasItemEventFactory {

    public static Event createAddVasItemToItemEvent(VasItemPayload payload) {
        if (payload.vasCategoryId() == DefinitionConstants.VAS_ITEM_CATEGORY_ID && payload.vasSellerId() == DefinitionConstants.VAS_ITEM_SELLER_ID) {
            VasItem vasItem = new VasItem(payload.vasItemId(), payload.vasCategoryId(), payload.vasSellerId(), payload.price(), payload.quantity());
            return new VasItemAdditionEvent(payload.itemId(), vasItem);
        } else {
            throw new IllegalArgumentException(ErrorMessageConstants.INVALID_VAS_ITEM_ID_ERROR);
        }
    }
}
