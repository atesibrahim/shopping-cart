package com.ates.tyol.shopping_cart.event;

import com.ates.tyol.shopping_cart.entity.item.Item;
import com.ates.tyol.shopping_cart.service.cart.CartService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemAdditionEventTest {

    @InjectMocks
    private ItemAdditionEvent itemAdditionCommand;

    @Mock
    private CartService mockCartService;

    @Mock
    private Item mockItem;

    @Test
    void execute_whenItemAddedSuccessfully_thenReturnSuccessMessage() {
        when(mockCartService.addItem(mockItem)).thenReturn(true);

        String result = itemAdditionCommand.execute(mockCartService);

        assertTrue(result.contains("\"result\":true"));
        assertTrue(result.contains("\"message\":\"Item added successfully\""));
        verify(mockCartService, times(1)).addItem(mockItem);
    }

    @Test
    void execute_whenCartServiceThrowsException_thenReturnFailureMessage() {
        String errorMessage = "Item could not be added";
        when(mockCartService.addItem(mockItem)).thenThrow(new RuntimeException(errorMessage));

        String result = itemAdditionCommand.execute(mockCartService);

        assertTrue(result.contains("\"result\":false"));
        assertTrue(result.contains("Failed to add item to cart due to: Item could not be added"));
        verify(mockCartService, times(1)).addItem(mockItem);
    }

    @Test
    void execute_whenCartServiceThrowsNullPointerException_thenReturnFailureMessage() {
        String errorMessage = "Null pointer exception";
        when(mockCartService.addItem(mockItem)).thenThrow(new NullPointerException(errorMessage));

        String result = itemAdditionCommand.execute(mockCartService);

        assertTrue(result.contains("\"result\":false"));
        assertTrue(result.contains("Failed to add item to cart due to: Null pointer exception"));
        verify(mockCartService, times(1)).addItem(mockItem);
    }
}
