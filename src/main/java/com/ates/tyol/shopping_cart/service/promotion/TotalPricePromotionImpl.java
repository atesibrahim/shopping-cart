package com.ates.tyol.shopping_cart.service.promotion;

import com.ates.tyol.shopping_cart.constant.DefinitionConstants;
import com.ates.tyol.shopping_cart.constant.PromotionNumericConstants;
import com.ates.tyol.shopping_cart.entity.cart.Cart;
import org.springframework.stereotype.Component;

import java.util.NavigableMap;
import java.util.TreeMap;

@Component
public class TotalPricePromotionImpl implements Promotion {

    private static final NavigableMap<Double, Double> promotionMap = new TreeMap<>();

    static {
        promotionMap.put(PromotionNumericConstants.MINIMUM_PRICE_PROMOTION_CAN_BE_APPLIED, PromotionNumericConstants.TWO_HUNDRED_FIFTY);
        promotionMap.put(PromotionNumericConstants.FIVE_THOUSAND, PromotionNumericConstants.FIVE_HUNDRED);
        promotionMap.put(PromotionNumericConstants.TEN_THOUSAND, PromotionNumericConstants.ONE_THOUSAND);
        promotionMap.put(PromotionNumericConstants.FIFTY_THOUSAND, PromotionNumericConstants.TWO_THOUSAND);
    }

    @Override
    public void applyDiscountAmount(Cart cart) {
        if (isPromotionApplicable(cart)) {
            double totalPrice = cart.getTotalPrice();

            double discountAmount = promotionMap.floorEntry(totalPrice) != null
                    ? promotionMap.floorEntry(totalPrice).getValue()
                    : PromotionNumericConstants.ZERO;

            if (cart.getTotalDiscount() == 0 || cart.getTotalDiscount() < discountAmount) {
                cart.setTotalDiscount(discountAmount);
                cart.setAppliedPromotionId(DefinitionConstants.TOTAL_PRICE_PROMOTION_ID);
            }
        }
    }

    @Override
    public boolean isPromotionApplicable(Cart cart) {
        return  cart.getTotalPrice() >= PromotionNumericConstants.MINIMUM_PRICE_PROMOTION_CAN_BE_APPLIED;
    }
}
