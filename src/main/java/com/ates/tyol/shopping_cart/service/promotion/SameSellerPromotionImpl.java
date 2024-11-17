package com.ates.tyol.shopping_cart.service.promotion;

import com.ates.tyol.shopping_cart.constant.DefinitionConstants;
import com.ates.tyol.shopping_cart.entity.cart.Cart;
import com.ates.tyol.shopping_cart.entity.item.Item;
import com.ates.tyol.shopping_cart.entity.item.VasItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SameSellerPromotionImpl implements Promotion {

    @Override
    public void applyDiscountAmount(Cart cart) {
        if (isPromotionApplicable(cart)) {
            double discountAmount = cart.getTotalAmountWithoutVasItems() * DefinitionConstants.SAME_SELLER_DISCOUNT_RATE;
            if (cart.getTotalDiscount() == 0 || cart.getTotalDiscount() < discountAmount) {
                cart.setTotalDiscount(discountAmount);
                cart.setAppliedPromotionId(DefinitionConstants.SAME_SELLER_PROMOTION_ID);
            }
        }
    }

    @Override
    public boolean isPromotionApplicable(Cart cart) {
        return cart.getItems().size() > 1 &
                cart.getItems().values().stream()
                        .flatMap(List::stream)
                        .filter(item -> !(item instanceof VasItem))
                        .map(Item::getSellerId)
                        .distinct()
                        .count() == 1;
    }
}
