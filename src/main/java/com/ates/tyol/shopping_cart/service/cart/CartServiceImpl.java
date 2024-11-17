package com.ates.tyol.shopping_cart.service.cart;

import com.ates.tyol.shopping_cart.constant.DefinitionConstants;
import com.ates.tyol.shopping_cart.constant.ErrorMessageConstants;
import com.ates.tyol.shopping_cart.entity.cart.Cart;
import com.ates.tyol.shopping_cart.entity.item.DefaultItem;
import com.ates.tyol.shopping_cart.entity.item.Item;
import com.ates.tyol.shopping_cart.entity.item.VasItem;
import com.ates.tyol.shopping_cart.service.promotion.CategoryPromotionImpl;
import com.ates.tyol.shopping_cart.service.promotion.SameSellerPromotionImpl;
import com.ates.tyol.shopping_cart.service.promotion.TotalPricePromotionImpl;
import com.ates.tyol.shopping_cart.representation.CartRepresentation;
import com.ates.tyol.shopping_cart.representation.ItemRepresentation;
import com.ates.tyol.shopping_cart.exception.BusinessException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final SameSellerPromotionImpl sameSellerPromotionImpl;
    private final CategoryPromotionImpl categoryPromotionImpl;
    private final TotalPricePromotionImpl totalPricePromotionImpl;
    private final ObjectMapper objectMapper;

    private static final int MAX_UNIQUE_ITEMS = 10;
    private static final int MAX_TOTAL_ITEMS = 30;
    private static final double MAX_TOTAL_AMOUNT = 500_000.0;
    public static final int MAXIMUM_VAS_ITEM_COUNT = 3;

    private final AtomicInteger totalItemCount = new AtomicInteger(0);

    private final Cart cart;

    @Override
    public boolean addItem(Item item) {
        checkLimits(item);

        cart.getItems().compute(item.getItemId(), (k, v) -> {
            if (v == null) {
                v = new ArrayList<>();
            }
            v.add(item);
            return v;
        });
        addAmountToTotalPrice(item);
        totalItemCount.addAndGet(item.getQuantity());
        return true;
    }

    @Override
    public boolean addVasItemToItem(int itemId, VasItem vasItem) {
        DefaultItem defaultItem = checkVasItem(itemId, vasItem);
        defaultItem.addVasItem(vasItem);
        addAmountToTotalPrice(vasItem);
        totalItemCount.addAndGet(vasItem.getQuantity());
        return true;
    }

    @Override
    public boolean removeItem(int itemId) {
        if (!cart.getItems().containsKey(itemId)) {
            throw new BusinessException(ErrorMessageConstants.ITEM_NOT_FOUND_ERROR + itemId);
        }

        Item itemFirst = cart.getItems().get(itemId).getFirst();
        extractAmountFromPrice(itemFirst);
        totalItemCount.addAndGet(-itemFirst.getQuantity());

        cart.getItems().computeIfPresent(itemId, (k, v) -> {
            v.removeIf(item -> item.getItemId() == itemId);
            return v.isEmpty() ? null : v;
        });

        return true;
    }

    @Override
    public void resetCart() {
        cart.getItems().clear();
        cart.setTotalAmount(0.0);
        cart.setTotalDiscount(0.0);
        cart.setAppliedPromotionId(0);
    }

    @Override
    public String displayCart() {
        List<ItemRepresentation> itemList = mapCartItemsToListItemDto();
        CartRepresentation cartDto = mapListItemDtoCartDto(itemList);

        try {
            return objectMapper.writeValueAsString(cartDto);
        } catch (Exception e) {
            throw new BusinessException(ErrorMessageConstants.CART_DISPLAY_ERROR);
        }
    }

    private void checkLimits(Item item) {
        if (getUniqueItemCount(item.getItemId()) >= MAX_UNIQUE_ITEMS) {
            throw new BusinessException(ErrorMessageConstants.UNIQUE_ITEM_LIMIT_ERROR);
        }

        if (getTotalItemCount(item) > MAX_TOTAL_ITEMS) {
            throw new BusinessException(ErrorMessageConstants.TOTAL_ITEM_LIMIT_ERROR);
        }

        double potentialNewTotal = getPotentialNewTotal(item);
        if (potentialNewTotal > MAX_TOTAL_AMOUNT) {
            throw new BusinessException(ErrorMessageConstants.TOTAL_AMOUNT_LIMIT_ERROR);
        }
    }

    private double getPotentialNewTotal(Item item) {
        return cart.getTotalPrice() + item.getPrice() * item.getQuantity();
    }

    private int getUniqueItemCount(int itemId) {
        final int itemSize = cart.getItems().size();
        return cart.getItems().containsKey(itemId) ? itemSize : itemSize + 1;
    }

    private int getTotalItemCount(Item item) {
        return totalItemCount.get() + item.getQuantity();
    }

    private void addAmountToTotalPrice(Item item) {
        final double itemPrice = item.getPrice() * item.getQuantity();
        addAmountToTotalAmountWithoutVasItem(item, itemPrice);
        final double totalAddPrice = cart.getTotalPrice() + itemPrice;
        cart.setTotalPrice(totalAddPrice);
        updateTotalAmount();
    }

    private void addAmountToTotalAmountWithoutVasItem(Item item, double itemPrice) {
        if (!(item instanceof VasItem)) {
            cart.setTotalAmountWithoutVasItems(cart.getTotalAmountWithoutVasItems() + itemPrice);
        }
    }

    private void extractAmountFromPrice(Item item) {
        final double itemPrice = item.getPrice() * item.getQuantity();
        extractAmountFromTotalAmountWithoutVasItem(item, itemPrice);
        final double totalExtractPrice = cart.getTotalPrice() - itemPrice;
        cart.setTotalPrice(totalExtractPrice);
        updateTotalAmount();
    }

    private void extractAmountFromTotalAmountWithoutVasItem(Item item, double itemPrice) {
        if (!(item instanceof VasItem)) {
            cart.setTotalAmountWithoutVasItems(cart.getTotalAmountWithoutVasItems() - itemPrice);
        }
    }

    private void updateTotalAmount() {
        applyMaximumPromotionAmount();
        double totalPrice = cart.getTotalPrice();
        double appliedMaximumPromotionAmount = cart.getTotalDiscount();
        cart.setTotalAmount(totalPrice - appliedMaximumPromotionAmount);
        updateTotalDiscount(appliedMaximumPromotionAmount);
    }

    private void updateTotalDiscount(double appliedMaximumPromotionAmount) {
        cart.setTotalDiscount(appliedMaximumPromotionAmount);
    }

    private DefaultItem checkVasItem(int itemId, VasItem vasItem) {
        boolean match = cart.getItems().values().stream().noneMatch(itemList ->
                itemList.stream().anyMatch(checkItemCategoryId()));
        if (match) {
            throw new BusinessException(ErrorMessageConstants.VAS_ITEM_CATEGORY_ERROR);
        }

        Item existingItem = cart.getItems().values().stream().flatMap(List::stream)
                .filter(checkItemCategoryId())
                .filter(item -> item.getItemId() == itemId).findFirst()
                .orElseThrow(() -> new BusinessException(ErrorMessageConstants.DEFAULT_ITEM_NOT_FOUND_ERROR));

        if (!(existingItem instanceof DefaultItem defaultItem)) {
            throw new BusinessException(ErrorMessageConstants.VAS_ITEM_NOT_ADDED_ERROR);
        }

        if (isVasItemSizeExceed(defaultItem, vasItem)) {
            throw new BusinessException(ErrorMessageConstants.VAS_ITEM_EXCEED_ERROR);
        }
        return defaultItem;
    }

    private Predicate<Item> checkItemCategoryId() {
        return item -> item.getCategoryId() == DefinitionConstants.ELECTRONIC_CATEGORY_ID
                || item.getCategoryId() == DefinitionConstants.FURNITURE_CATEGORY_ID;
    }

    private boolean isVasItemSizeExceed(DefaultItem defaultItem, VasItem vasItem) {
        return defaultItem.getVasItems().size() + vasItem.getQuantity() > MAXIMUM_VAS_ITEM_COUNT;
    }

    private void applyMaximumPromotionAmount() {
        sameSellerPromotionImpl.applyDiscountAmount(cart);
        categoryPromotionImpl.applyDiscountAmount(cart);
        totalPricePromotionImpl.applyDiscountAmount(cart);
    }

    private List<ItemRepresentation> mapCartItemsToListItemDto() {
        return cart.getItems().values()
                .stream()
                .map(itemList1 -> {
                    Item item = itemList1.getFirst();
                    return ItemRepresentation.builder()
                            .itemId(item.getItemId())
                            .categoryId(item.getCategoryId())
                            .sellerId(item.getSellerId())
                            .price(item.getPrice())
                            .quantity(item.getQuantity())
                            .vasItems(item instanceof DefaultItem defaultItem ? defaultItem.getVasItems() : List.of())
                            .build();
                }).toList();
    }

    private CartRepresentation mapListItemDtoCartDto(List<ItemRepresentation> itemList) {
        return CartRepresentation.builder()
                .items(itemList)
                .totalDiscount(cart.getTotalDiscount())
                .totalAmount(cart.getTotalAmount())
                .appliedPromotionId(cart.getAppliedPromotionId())
                .build();
    }
}