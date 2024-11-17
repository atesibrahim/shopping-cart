package com.ates.tyol.shopping_cart.event;

import com.ates.tyol.shopping_cart.service.cart.CartService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartDisplayEventTest {

    @InjectMocks
    private CartDisplayEvent cartDisplayCommand;

    @Mock
    private CartService mockCartService;

    @Test
    void execute_whenCartServiceDisplaysCartSuccessfully_thenReturnSuccessMessage() {
        String cartDetails = "{\"cartItems\": [{\"item\": \"item1\", \"quantity\": 2}, {\"item\": \"item2\", \"quantity\": 1}]}";
        when(mockCartService.displayCart()).thenReturn(cartDetails);

        String result = cartDisplayCommand.execute(mockCartService);

        assertTrue(result.contains("\"result\":true"));
        assertTrue(result.contains(cartDetails));
        assertFalse(result.contains("\"result\":false"));
    }

    @Test
    void execute_whenCartServiceThrowsException_thenReturnFailureMessage() {
        String errorMessage = "Service is unavailable";
        when(mockCartService.displayCart()).thenThrow(new RuntimeException(errorMessage));

        String result = cartDisplayCommand.execute(mockCartService);

        assertTrue(result.contains("\"result\":false"));
        assertTrue(result.contains("Failed to display cart due to"));
        assertTrue(result.contains(errorMessage));
    }

    @Test
    void execute_whenCartServiceThrowsNullPointerException_thenReturnFailureMessage() {
        String errorMessage = "Null pointer exception";
        when(mockCartService.displayCart()).thenThrow(new NullPointerException(errorMessage));

        String result = cartDisplayCommand.execute(mockCartService);

        assertTrue(result.contains("\"result\":false"));
        assertTrue(result.contains("Failed to display cart due to"));
        assertTrue(result.contains(errorMessage));
    }

    @Test
    void execute_whenCartServiceReturnsEmptyCart_thenReturnSuccessWithEmptyCartMessage() {
        String emptyCartDetails = "{\"cartItems\": []}";
        when(mockCartService.displayCart()).thenReturn(emptyCartDetails);

        String result = cartDisplayCommand.execute(mockCartService);

        assertTrue(result.contains("\"result\":true"));
        assertTrue(result.contains(emptyCartDetails));
        assertFalse(result.contains("\"result\":false"));
    }
}
