package com.ates.tyol.shopping_cart.value;


public record VasItemPayload(int itemId, int vasItemId, int vasCategoryId, int vasSellerId, double price,
                             int quantity) {
}