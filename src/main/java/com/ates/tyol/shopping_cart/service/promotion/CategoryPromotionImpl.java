package com.ates.tyol.shopping_cart.service.promotion;

import com.ates.tyol.shopping_cart.constant.DefinitionConstants;
import com.ates.tyol.shopping_cart.entity.cart.Cart;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryPromotionImpl implements Promotion {

    @Override
    public void applyDiscountAmount(Cart cart) {
        if(isPromotionApplicable(cart)) {
            double discountAmount = cart.getTotalPrice() * DefinitionConstants.CATEGORY_DISCOUNT_RATE;
            if (cart.getTotalDiscount() == 0 || cart.getTotalDiscount() < discountAmount) {
                cart.setTotalDiscount(discountAmount);
                cart.setAppliedPromotionId(DefinitionConstants.CATEGORY_PROMOTION_ID);
            }
        }
    }

    @Override
    public boolean isPromotionApplicable(Cart cart) {
        return cart.getItems().values().stream()
                .flatMap(List::stream)
                .anyMatch(item -> item.getCategoryId() == DefinitionConstants.DISCOUNT_CATEGORY_ID);
    }
}