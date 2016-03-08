package model.enums;

public enum EnumContentType {

    /* ENUM ================================================================ */

    TEXT_PLAIN("text/plain"),
    TEXT_HTML("text/html"),
    APPLICATION_JSON("application/json");

    /* ATTRIBUTES ========================================================== */

    public final String value;

    /* CONSTRUCTOR ========================================================= */

    EnumContentType(String value) {
        this.value = value;
    }

    /* PUBLIC STATIC METHODS =============================================== */

    public static EnumContentType findContentTypeByValue(String value) {
        for (EnumContentType type : EnumContentType.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        return TEXT_PLAIN;
    }

}
