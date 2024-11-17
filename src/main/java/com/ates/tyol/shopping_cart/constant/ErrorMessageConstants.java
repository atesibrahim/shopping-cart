package com.ates.tyol.shopping_cart.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ErrorMessageConstants {
    public static final String FILE_PATH_NOT_PROVIDED = "Missed file path. Please check and make sure you provide a valid path of the file.";
    public static final String UNKNOWN_COMMAND_ERROR = "Command type is not correct: ";
    public static final String VAS_ITEM_CANNOT_BE_ADDED_DIRECTLY_TO_CART_ERROR = "VasItem cannot be added directly to cart; use addVasItemToItem command.";
    public static final String INVALID_VAS_ITEM_ID_ERROR = "Invalid VasItem: Check category ID and seller ID requirements.";
    public static final String INVALID_COMMAND_ERROR = "Error occurred during json parsing. There is no command field in the json.";
    public static final String INVALID_PAYLOAD_ERROR = "Error occurred during json parsing. There is no payload field in the json.";
    public static final String ITEM_NOT_FOUND_ERROR = "Item not found in the cart. ItemId: ";
    public static final String VAS_ITEM_CATEGORY_ERROR = "Cannot add VasItem to different items than Electronics or Furniture.";
    public static final String DEFAULT_ITEM_NOT_FOUND_ERROR = "Default Item not found in the cart. Cannot add VasItem.";
    public static final String VAS_ITEM_NOT_ADDED_ERROR = "VasItem can only be added to the DefaultItem already in the cart.";
    public static final String VAS_ITEM_EXCEED_ERROR = "Cannot add VasItem: only 3 VasItems are allowed per DefaultItem.";
    public static final String CART_DISPLAY_ERROR = "Error occured while converting cart to json.";
    public static final String UNIQUE_ITEM_LIMIT_ERROR = "Cannot add more than max unique item to the cart.";
    public static final String TOTAL_ITEM_LIMIT_ERROR = "Cannot add more than max total item to the cart.";
    public static final String TOTAL_AMOUNT_LIMIT_ERROR = "Cannot add more than max total amount to the cart.";
    public static final String DIGITAL_QUANTITY_CAN_NOT_EXCEEDS_COUNT = "Digital quantity can not exceeds count: ";
    public static final String INVALID_JSON_ERROR = "Invalid JSON format.";
    public static final String FAILED_TO_REMOVE_ITEM_DUE_TO = "Failed to remove item due to:";
    public static final String FAILED_TO_ADD_VAS_ITEM_DUE_TO = "Failed to add VAS item due to: ";
}
