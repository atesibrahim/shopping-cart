package com.ates.tyol.shopping_cart.service.cart;

import com.ates.tyol.shopping_cart.constant.ErrorMessageConstants;
import com.ates.tyol.shopping_cart.entity.cart.Cart;
import com.ates.tyol.shopping_cart.entity.item.DefaultItem;
import com.ates.tyol.shopping_cart.entity.item.Item;
import com.ates.tyol.shopping_cart.entity.item.VasItem;
import com.ates.tyol.shopping_cart.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class CartServiceImplTest {

    @Autowired
    private CartServiceImpl cartService;

    @Autowired
    private Cart cart;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        cartService.resetCart();
    }

    @Test
    void addItem_whenValidItemApplyTotalPricePromotion_thenItemAddedSuccessfully() {
        DefaultItem item = new DefaultItem(1, 1, 1, 1000.0, 1);

        boolean result = cartService.addItem(item);

        assertTrue(result);
        assertEquals(1, cart.getItems().size());
    }

    @Test
    void addItem_whenValidItemApplyTotalPricePromotion2_thenItemAddedSuccessfully() {
        cart.setTotalDiscount(1.0);
        DefaultItem item = new DefaultItem(1, 1, 1, 1000.0, 1);

        boolean result = cartService.addItem(item);

        assertTrue(result);
        assertEquals(1, cart.getItems().size());
    }

    @Test
    void addItem_whenValidItemApplySameSellerPromotion_thenItemAddedSuccessfully() {
        DefaultItem item = new DefaultItem(1, 1, 1, 10.0, 1);
        cartService.addItem(item);
        item = new DefaultItem(2, 1, 1, 100.0, 1);
        boolean result = cartService.addItem(item);

        assertTrue(result);
        assertEquals(2, cart.getItems().size());
    }

    @Test
    void addItem_whenValidItemApplySameSellerPromotion2_thenItemAddedSuccessfully() {
        cart.setTotalDiscount(1.0);
        DefaultItem item = new DefaultItem(1, 1, 1, 10.0, 1);
        cartService.addItem(item);
        item = new DefaultItem(2, 1, 1, 100.0, 1);
        boolean result = cartService.addItem(item);

        assertTrue(result);
        assertEquals(2, cart.getItems().size());
    }

    @Test
    void addItem_whenValidItemApplyCategoryPromotion_thenItemAddedSuccessfully() {
        DefaultItem item = new DefaultItem(1, 3003, 1, 100.0, 1);
        boolean result = cartService.addItem(item);

        assertTrue(result);
        assertEquals(1, cart.getItems().size());
    }

    @Test
    void addItem_whenValidItemApplyCategoryPromotion2_thenItemAddedSuccessfully() {
        cart.setTotalDiscount(1.0);
        DefaultItem item = new DefaultItem(1, 3003, 1, 100.0, 1);
        boolean result = cartService.addItem(item);

        assertTrue(result);
        assertEquals(1, cart.getItems().size());
    }

    @Test
    void addItem_whenExceedsUniqueLimit_thenThrowBusinessException() {
        DefaultItem item = new DefaultItem(11, 1, 1, 100.0, 1);
        Map<Integer, List<Item>> items = new ConcurrentHashMap<>();
        for (int i = 1; i <= 10; i++) {
            items.put(i, List.of(new DefaultItem(i, 1, 1, 100.0, 1)));
        }
        cart.setItems(items);

        BusinessException exception = assertThrows(BusinessException.class, () -> cartService.addItem(item));

        assertEquals(ErrorMessageConstants.UNIQUE_ITEM_LIMIT_ERROR, exception.getMessage());
    }

    @Test
    void addItem_whenExceedsTotalItemsLimit_thenThrowBusinessException() {
        DefaultItem item = new DefaultItem(1, 1, 1, 100.0, 31);

        BusinessException exception = assertThrows(BusinessException.class, () -> cartService.addItem(item));

        assertEquals(ErrorMessageConstants.TOTAL_ITEM_LIMIT_ERROR, exception.getMessage());
    }

    @Test
    void addItem_whenExceedsTotalAmountLimit_thenThrowBusinessException() {
        DefaultItem item = new DefaultItem(1, 1, 1, 500_001.0, 1);

        BusinessException exception = assertThrows(BusinessException.class, () -> cartService.addItem(item));

        assertEquals(ErrorMessageConstants.TOTAL_AMOUNT_LIMIT_ERROR, exception.getMessage());
    }

    @Test
    void addVasItemToItem_whenValidVasItem_thenItemAddedSuccessfully() {
        int itemId = 1;
        VasItem vasItem = new VasItem(1, 3242, 5003, 50.0, 1);
        DefaultItem item = new DefaultItem(1, 1001, 1, 100.0, 1);

        cartService.addItem(item);

        boolean result = cartService.addVasItemToItem(itemId, vasItem);

        assertTrue(result);
    }

    @Test
    void addVasItemToItem_whenCategoryNotCorrect_thenThrowBusinessException() {
        int itemId = 1;
        VasItem vasItem = new VasItem(1, 3242, 5003, 50.0, 1);
        DefaultItem item = new DefaultItem(1, 11, 1, 100.0, 1);

        cartService.addItem(item);

        BusinessException exception = assertThrows(BusinessException.class, () -> cartService.addVasItemToItem(itemId, vasItem));

        assertEquals(ErrorMessageConstants.VAS_ITEM_CATEGORY_ERROR, exception.getMessage());
    }

    @Test
    void addVasItemToItem_whenExceedsVasItemLimit_thenThrowBusinessException() {
        int itemId = 1;
        VasItem vasItem = new VasItem(1, 3242, 5003, 50.0, 5);
        DefaultItem item = new DefaultItem(1, 1001, 1, 100.0, 1);

        cartService.addItem(item);

        BusinessException exception = assertThrows(BusinessException.class, () -> cartService.addVasItemToItem(itemId, vasItem));

        assertEquals(ErrorMessageConstants.VAS_ITEM_EXCEED_ERROR, exception.getMessage());
    }

    @Test
    void removeItem_whenItemExists_thenItemRemovedSuccessfully() {
        int itemId = 1;
        DefaultItem item = new DefaultItem(itemId, 1, 1, 100.0, 1);
        cartService.addItem(item);

        boolean result = cartService.removeItem(itemId);

        assertTrue(result);
    }

    @Test
    void removeItem_whenItemDoesNotExist_thenThrowBusinessException() {
        int itemId = 1;

        BusinessException exception = assertThrows(BusinessException.class, () -> cartService.removeItem(itemId));

        assertEquals(ErrorMessageConstants.ITEM_NOT_FOUND_ERROR + itemId, exception.getMessage());
    }

    @Test
    void resetCart_whenCalled_thenCartResetSuccessfully() {
        cartService.resetCart();

        assertEquals(0, cart.getItems().size());
        assertEquals(0.0, cart.getTotalAmount());
        assertEquals(0.0, cart.getTotalDiscount());
        assertEquals(0, cart.getAppliedPromotionId());
    }

    @Test
    void displayCart_whenValidCart_thenReturnCartAsJson() {
        DefaultItem item = new DefaultItem(1, 1, 1, 100.0, 1);

        cartService.addItem(item);

        String result = cartService.displayCart();

        assertTrue(result.contains("totalAmount"));
        assertTrue(result.contains("totalDiscount"));
        assertTrue(result.contains("items"));
    }
}
