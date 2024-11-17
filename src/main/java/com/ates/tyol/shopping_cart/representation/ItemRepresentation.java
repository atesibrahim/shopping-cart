package com.ates.tyol.shopping_cart.representation;

import com.ates.tyol.shopping_cart.entity.item.VasItem;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ItemRepresentation {
    private int itemId;
    private int categoryId;
    private int sellerId;
    private double price;
    private int quantity;
    private List<VasItem> vasItems;
}
