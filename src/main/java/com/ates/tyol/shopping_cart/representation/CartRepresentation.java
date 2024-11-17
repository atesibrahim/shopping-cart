package com.ates.tyol.shopping_cart.representation;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class CartRepresentation {
    List<ItemRepresentation> items;
    private double totalDiscount;
    private double totalAmount;
    private int appliedPromotionId;
}
