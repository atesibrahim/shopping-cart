package com.ates.tyol.shopping_cart.service.factory;

import com.ates.tyol.shopping_cart.constant.DefinitionConstants;
import com.ates.tyol.shopping_cart.constant.ErrorMessageConstants;
import com.ates.tyol.shopping_cart.event.CartDisplayEvent;
import com.ates.tyol.shopping_cart.event.CartResetEvent;
import com.ates.tyol.shopping_cart.event.Event;
import com.ates.tyol.shopping_cart.event.ItemAdditionEvent;
import com.ates.tyol.shopping_cart.event.ItemRemoveEvent;
import com.ates.tyol.shopping_cart.event.VasItemAdditionEvent;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class EventFactoryServiceImplTest {

    @InjectMocks
    private EventFactoryServiceImpl commandFactoryService;

    @Test
    void executeEvent_whenInvalidJson_thenThrowIllegalArgumentException() {
        String invalidJson = "{ invalid json }";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                commandFactoryService.executeEvent(invalidJson)
        );

        assertEquals(ErrorMessageConstants.INVALID_JSON_ERROR, exception.getMessage());
    }

    @Test
    void executeCommand_whenInvalidEventType_thenThrowIllegalArgumentException() {
        String invalidCommandJson = "{\"command\": \"invalid_command\", \"payload\": {}}";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                commandFactoryService.executeEvent(invalidCommandJson)
        );

        assertEquals(ErrorMessageConstants.UNKNOWN_COMMAND_ERROR + "invalid_command", exception.getMessage());
    }

    @Test
    void executeCommand_whenCommandTypeIsResetCart_thenReturnCartResetEvent() {
        String json = "{\"command\": \"resetCart\", \"payload\": {}}";

        Event result = commandFactoryService.executeEvent(json);

        assertInstanceOf(CartResetEvent.class, result);
    }

    @Test
    void executeCommand_whenCommandTypeIsDisplayCart_thenReturnCartDisplayEvent() {
        String json = "{\"command\": \"displayCart\", \"payload\": {}}";

        Event result = commandFactoryService.executeEvent(json);

        assertInstanceOf(CartDisplayEvent.class, result);
    }

    @Test
    void executeCommand_whenAddItemCommand_thenReturnItemAdditionEvent() {
        String json = "{\"command\": \"addItem\", \"payload\": {\"categoryId\": 1, \"itemId\": 1, \"sellerId\": 1, \"price\": 100.0, \"quantity\": 1}}";

        Event result = commandFactoryService.executeEvent(json);

        assertInstanceOf(ItemAdditionEvent.class, result);
    }

    @Test
    void executeCommand_whenAddDigitalItemCommand_thenReturnItemAdditionEvent() {
        String json = "{\"command\": \"addItem\", \"payload\": {\"categoryId\": 7889, \"itemId\": 1, \"sellerId\": 1, \"price\": 100.0, \"quantity\": 1}}";

        Event result = commandFactoryService.executeEvent(json);

        assertInstanceOf(ItemAdditionEvent.class, result);
    }

    @Test
    void executeCommand_whenAddDigitalItemEvent_thenThrowException() {
        String json = "{\"command\": \"addItem\", \"payload\": {\"categoryId\": 7889, \"itemId\": 1, \"sellerId\": 1, \"price\": 100.0, \"quantity\": 10}}";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                commandFactoryService.executeEvent(json)
        );

        assertEquals(ErrorMessageConstants.DIGITAL_QUANTITY_CAN_NOT_EXCEEDS_COUNT + DefinitionConstants.MAX_DIGITAL_ITEM_QUANTITY, exception.getMessage());
    }

    @Test
    void executeCommand_whenAddVasItemToItemCommand_thenReturnVasItemAdditionToItemEvent() {
        String json = """
                            {"command":"addVasItemToItem", "payload": {"itemId":5, "vasItemId":101, "vasCategoryId":3242, "vasSellerId":5003, "price":50.0, "quantity":1}}
                """;

        Event result = commandFactoryService.executeEvent(json);

        assertInstanceOf(VasItemAdditionEvent.class, result);
    }

    @Test
    void executeCommand_whenAddVasItemEvent_thenThrowIllegalArgumentException() {
        String json = "{\"command\": \"addItem\", \"payload\": {\"categoryId\": 3242, \"itemId\": 1, \"sellerId\": 1, \"price\": 100.0, \"quantity\": 1}}";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                commandFactoryService.executeEvent(json)
        );

        assertEquals(ErrorMessageConstants.VAS_ITEM_CANNOT_BE_ADDED_DIRECTLY_TO_CART_ERROR, exception.getMessage());
    }

    @Test
    void executeCommand_whenAddVasItemToItemEventInvalidCategory_thenThrowIllegalArgumentException() {
        String json = """
                            {"command":"addVasItemToItem", "payload": {"itemId":5, "vasItemId":101, "vasCategoryId":324, "vasSellerId":5003, "price":50.0, "quantity":1}}
                """;
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                commandFactoryService.executeEvent(json)
        );

        assertEquals(ErrorMessageConstants.INVALID_VAS_ITEM_ID_ERROR, exception.getMessage());
    }

    @Test
    void executeCommand_whenRemoveItemCommand_thenReturnItemRemoveEvent() {
        String json = "{\"command\": \"removeItem\", \"payload\": {\"itemId\": 1}}";

        Event result = commandFactoryService.executeEvent(json);

        assertInstanceOf(ItemRemoveEvent.class, result);
    }

    @Test
    void executeEvent_whenMissingPayload_thenThrowIllegalArgumentException() {
        String json = "{\"command\": \"addItem\"}"; // Missing payload

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                commandFactoryService.executeEvent(json)
        );

        assertEquals(ErrorMessageConstants.INVALID_PAYLOAD_ERROR, exception.getMessage());
    }

    @Test
    void executeEvent_whenPayloadMissingRequiredFields_thenThrowIllegalArgumentException() {
        String json = "{\"command\": \"addItem\", \"payload\": {\"categoryId\": 1, \"itemId\": 1}}"; // Missing seller_id, price, and quantity

        JSONException exception = assertThrows(JSONException.class, () ->
                commandFactoryService.executeEvent(json)
        );

        assertEquals("JSONObject[\"sellerId\"] not found.", exception.getMessage());
    }

    @Test
    void executeCommand_whenEventTypeIsUnknown_thenThrowIllegalArgumentException() {
        String json = "{\"command\": \"unknown_command\", \"payload\": {}}";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                commandFactoryService.executeEvent(json)
        );

        assertEquals(ErrorMessageConstants.UNKNOWN_COMMAND_ERROR + "unknown_command", exception.getMessage());
    }
}
