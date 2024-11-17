package com.ates.tyol.shopping_cart.entity.cart;

import com.ates.tyol.shopping_cart.entity.item.Item;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Getter
@Setter
public class Cart {
    private Map<Integer, List<Item>> items;
    private double totalPrice;
    private double totalAmount;
    private double totalDiscount;
    private double totalAmountWithoutVasItems;
    private int appliedPromotionId;

    private Cart() {
        this.items = new ConcurrentHashMap<>();
        this.totalPrice = 0.0;
        this.totalAmount = 0.0;
        this.totalDiscount = 0.0;
        this.appliedPromotionId = 0;
    }
}
