package model.enums;

/**
 * Class enumerating the different methods.
 */
public enum EnumMethod {

    /* CONSTANTS ========================================================== */

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

    private final String value;

    /* CONSTRUCTOR ========================================================== */

    EnumMethod(String value) {
        this.value = value;
    }

    /* PUBLIC METHODS ========================================================== */

    public static EnumMethod findMethodByValue(String value) {
        for (EnumMethod method : EnumMethod.values()) {
            if (method.value.equals(value)) {
                return method;
            }
        }
        return GET;
    }
}
