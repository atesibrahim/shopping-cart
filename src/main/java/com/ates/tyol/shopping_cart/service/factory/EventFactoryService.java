package com.ates.tyol.shopping_cart.service.factory;

import com.ates.tyol.shopping_cart.event.Event;

public interface EventFactoryService {
    Event executeEvent(String jsonString);
}
