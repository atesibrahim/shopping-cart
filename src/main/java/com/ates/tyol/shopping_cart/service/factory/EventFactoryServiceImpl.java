package com.ates.tyol.shopping_cart.service.factory;

import com.ates.tyol.shopping_cart.constant.EventFactoryConstants;
import com.ates.tyol.shopping_cart.constant.ErrorMessageConstants;
import com.ates.tyol.shopping_cart.event.CartDisplayEvent;
import com.ates.tyol.shopping_cart.event.CartResetEvent;
import com.ates.tyol.shopping_cart.event.Event;
import com.ates.tyol.shopping_cart.event.ItemRemoveEvent;
import com.ates.tyol.shopping_cart.factory.ItemEventFactory;
import com.ates.tyol.shopping_cart.factory.VasItemEventFactory;
import com.ates.tyol.shopping_cart.value.ItemPayload;
import com.ates.tyol.shopping_cart.value.VasItemPayload;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class EventFactoryServiceImpl implements EventFactoryService {

    @Override
    public Event executeEvent(String jsonString) {
        JSONObject jsonObject = convertStringToJsonObject(jsonString);

        String eventType = getEventType(jsonObject);

        if (eventType.equals(EventFactoryConstants.RESET_CART)) {
            return new CartResetEvent();
        } else if (eventType.equals(EventFactoryConstants.DISPLAY_CART)) {
            return new CartDisplayEvent();
        }

        JSONObject payload = getPayload(jsonObject);
        return getEventBasedOnEventType(eventType, payload);
    }

    private JSONObject convertStringToJsonObject(String jsonString) {
        try {
            return new JSONObject(jsonString);
        } catch (Exception e) {
            throw new IllegalArgumentException(ErrorMessageConstants.INVALID_JSON_ERROR);
        }
    }

    private  String getEventType(JSONObject jsonObject) {
        try {
            return jsonObject.getString(EventFactoryConstants.COMMAND);
        } catch (Exception e) {
            throw new IllegalArgumentException(ErrorMessageConstants.INVALID_COMMAND_ERROR);
        }
    }

    private JSONObject getPayload(JSONObject jsonObject) {
        try {
            return jsonObject.getJSONObject(EventFactoryConstants.PAYLOAD);
        } catch (Exception e) {
            throw new IllegalArgumentException(ErrorMessageConstants.INVALID_PAYLOAD_ERROR);
        }
    }

    private Event getEventBasedOnEventType(String eventType, JSONObject payload) {
        return switch (eventType) {
            case EventFactoryConstants.ADD_ITEM -> ItemEventFactory.createAddItemEvent(convertToItemPayload(payload));
            case EventFactoryConstants.ADD_VAS_ITEM_TO_ITEM -> VasItemEventFactory.createAddVasItemToItemEvent(convertToVasItemPayload(payload));
            case EventFactoryConstants.REMOVE_ITEM -> {
                int removeItemId = payload.getInt(EventFactoryConstants.ITEM_ID);
                yield new ItemRemoveEvent(removeItemId);
            }
            default -> throw new IllegalArgumentException(ErrorMessageConstants.UNKNOWN_COMMAND_ERROR + eventType);
        };
    }

    private ItemPayload convertToItemPayload(JSONObject payload) {
        return new ItemPayload(
                payload.getInt(EventFactoryConstants.CATEGORY_ID),
                payload.getInt(EventFactoryConstants.ITEM_ID),
                payload.getInt(EventFactoryConstants.SELLER_ID),
                payload.getDouble(EventFactoryConstants.PRICE),
                payload.getInt(EventFactoryConstants.QUANTITY)
        );
    }

    private VasItemPayload convertToVasItemPayload(JSONObject payload) {
        return new VasItemPayload(
                payload.getInt(EventFactoryConstants.ITEM_ID),
                payload.getInt(EventFactoryConstants.VAS_ITEM_ID),
                payload.getInt(EventFactoryConstants.VAS_CATEGORY_ID),
                payload.getInt(EventFactoryConstants.VAS_SELLER_ID),
                payload.getDouble(EventFactoryConstants.PRICE),
                payload.getInt(EventFactoryConstants.QUANTITY)
        );
    }
}
