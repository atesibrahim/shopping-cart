package com.ates.tyol.shopping_cart.event;

import com.ates.tyol.shopping_cart.entity.item.VasItem;
import com.ates.tyol.shopping_cart.service.cart.CartService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VasItemAdditionEventTest {

    public static final int ITEM_ID = 123;

    @Mock
    private CartService mockCartService;

    @Test
    void execute_whenVasItemAddedSuccessfully_thenReturnSuccessMessage() {
        int itemId = ITEM_ID;
        VasItem vasItem = getVasItem();
        VasItemAdditionEvent vasItemAdditionCommand = new VasItemAdditionEvent(itemId, vasItem);
        when(mockCartService.addVasItemToItem(itemId, vasItem)).thenReturn(true);

        String result = vasItemAdditionCommand.execute(mockCartService);

        assertTrue(result.contains("\"result\":true"));
        assertTrue(result.contains("\"message\":\"VAS item added successfully\""));
        verify(mockCartService, times(1)).addVasItemToItem(itemId, vasItem);
    }

    @Test
    void execute_whenCartServiceThrowsException_thenReturnFailureMessage() {
        int itemId = ITEM_ID;
        VasItem vasItem = getVasItem();
        VasItemAdditionEvent vasItemAdditionCommand = new VasItemAdditionEvent(itemId, vasItem);
        String errorMessage = "Error adding VAS item";
        when(mockCartService.addVasItemToItem(itemId, vasItem)).thenThrow(new RuntimeException(errorMessage));

        String result = vasItemAdditionCommand.execute(mockCartService);

        assertTrue(result.contains("\"result\":false"));
        assertTrue(result.contains("Failed to add VAS item due to: Error adding VAS item"));
        verify(mockCartService, times(1)).addVasItemToItem(itemId, vasItem);
    }

    @Test
    void execute_whenCartServiceThrowsNullPointerException_thenReturnFailureMessage() {
        int itemId = ITEM_ID;
        VasItem vasItem = getVasItem();
        VasItemAdditionEvent vasItemAdditionCommand = new VasItemAdditionEvent(itemId, vasItem);
        String errorMessage = "Null pointer exception";
        when(mockCartService.addVasItemToItem(itemId, vasItem)).thenThrow(new NullPointerException(errorMessage));

        String result = vasItemAdditionCommand.execute(mockCartService);

        assertTrue(result.contains("\"result\":false"));
        assertTrue(result.contains("Failed to add VAS item due to: Null pointer exception"));
        verify(mockCartService, times(1)).addVasItemToItem(itemId, vasItem);
    }

    private VasItem getVasItem() {
        return new VasItem(1, 2, 3, 100.0, 1);
    }
}
