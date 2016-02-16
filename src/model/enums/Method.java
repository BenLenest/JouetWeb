package model.enums;

/**
 * Class enumerating the different methods.
 */
public enum Method {

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

    Method(String value) {
        this.value = value;
    }

    /* PUBLIC METHODS ========================================================== */

    public static Method findMethodByValue(String value) {
        for (Method method : Method.values()) {
            if (method.value.equals(value)) {
                return method;
            }
        }
        return GET;
    }
}
