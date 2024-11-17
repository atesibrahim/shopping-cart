package com.ates.tyol.shopping_cart.event;

import com.ates.tyol.shopping_cart.service.cart.CartService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CartResetEventTest {

    @InjectMocks
    private CartResetEvent cartResetCommand;

    @Mock
    private CartService mockCartService;

    @Test
    void execute_whenCartServiceResetsCartSuccessfully_thenReturnSuccessMessage() {
        String result = cartResetCommand.execute(mockCartService);

        assertTrue(result.contains("\"result\":true"));
        assertTrue(result.contains("\"message\":\"Cart has been reset\""));
        verify(mockCartService, times(1)).resetCart();
    }
}