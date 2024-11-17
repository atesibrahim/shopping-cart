package com.ates.tyol.shopping_cart.value;

public record ItemPayload(int categoryId, int itemId, int sellerId, double price, int quantity) {
}