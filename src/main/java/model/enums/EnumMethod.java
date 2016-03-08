package model.enums;

public enum EnumMethod {

    /* ENUM ================================================================ */

    GET("GET"),
    HEAD("HEAD"),
    POST("POST"),
    OPTIONS("OPTIONS"),
    CONNECT("CONNECT"),
    TRACE("TRACE"),
    PUT("PUT"),
    PATCH("PATCH"),
    DELETE("DELETE");

    /* ATTRIBUTES ========================================================== */

    public final String value;

    /* CONSTRUCTOR ========================================================= */

    EnumMethod(String value) {
        this.value = value;
    }

    /* PUBLIC STATIC METHODS =============================================== */

    public static EnumMethod findMethodByValue(String value) {
        for (EnumMethod method : EnumMethod.values()) {
            if (method.value.equals(value)) {
                return method;
            }
        }
        return GET;
    }

}
