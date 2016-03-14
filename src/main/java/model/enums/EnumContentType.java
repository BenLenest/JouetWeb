package model.enums;

public enum EnumContentType {

    /* ENUM ================================================================ */

    TEXT_PLAIN("text/plain"),
    TEXT_HTML("text/html"),
    TEXT_CSS("text/css"),
    APPLICATION_JSON("application/json"),
    APPLICATION_JAVASCRIPT("application/javascript");

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
