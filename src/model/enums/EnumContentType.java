package model.enums;

/**
 * Class enumerating the different content-types.
 */
public enum EnumContentType {

    /* CONSTANTS ========================================================== */

    TEXT_PLAIN("text/plain"),
    TEXT_HTML("text/html"),
    APPLICATION_JSON("application/json");

    /* ATTRIBUTES ========================================================== */

    private final String value;

    /* CONSTRUCTOR ========================================================== */

    EnumContentType(String value) {
        this.value = value;
    }

    /* PUBLIC METHODS ========================================================== */

    public static EnumContentType findContentTypeByValue(String value) {
        for (EnumContentType type : EnumContentType.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        return TEXT_PLAIN;
    }

}
