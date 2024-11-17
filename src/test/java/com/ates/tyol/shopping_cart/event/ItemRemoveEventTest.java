package com.ates.tyol.shopping_cart.event;

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
class ItemRemoveEventTest {

    public static final int ITEM_ID = 1234;

    @Mock
    private CartService mockCartService;

    @Test
    void execute_whenItemRemovedSuccessfully_thenReturnSuccessMessage() {
        int itemId = ITEM_ID;
        when(mockCartService.removeItem(itemId)).thenReturn(true);
        ItemRemoveEvent itemRemoveCommand = new ItemRemoveEvent(itemId);

        String result = itemRemoveCommand.execute(mockCartService);

        assertTrue(result.contains("\"result\":true"));
        assertTrue(result.contains("\"message\":\"Item removed successfully\""));
        verify(mockCartService, times(1)).removeItem(itemId);
    }

    @Test
    void execute_whenCartServiceThrowsException_thenReturnFailureMessage() {
        int itemId = ITEM_ID;
        String errorMessage = "Item could not be removed";
        when(mockCartService.removeItem(itemId)).thenThrow(new RuntimeException(errorMessage));
        ItemRemoveEvent itemRemoveCommand = new ItemRemoveEvent(itemId);

        String result = itemRemoveCommand.execute(mockCartService);

        assertTrue(result.contains("\"result\":false"));
        verify(mockCartService, times(1)).removeItem(itemId);
    }

    @Test
    void execute_whenCartServiceThrowsNullPointerException_thenReturnFailureMessage() {
        int itemId = ITEM_ID;
        String errorMessage = "Null pointer exception";
        when(mockCartService.removeItem(itemId)).thenThrow(new NullPointerException(errorMessage));
        ItemRemoveEvent itemRemoveCommand = new ItemRemoveEvent(itemId);

        String result = itemRemoveCommand.execute(mockCartService);

        assertTrue(result.contains("\"result\":false"));
        verify(mockCartService, times(1)).removeItem(itemId);
    }
}
