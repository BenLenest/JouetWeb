package model.enums;

import javafx.application.Application;

public enum ContentType {

    /* CONSTANTS ========================================================== */

    TEXT_PLAIN("text/plain"),
    TEXT_HTML("text/html"),
    APPLICATION_JSON("application/json");

    /* ATTRIBUTES ========================================================== */

    private final String value;

    /* CONSTRUCTOR ========================================================== */

    ContentType(String value) {
        this.value = value;
    }

    /* GETTERS ========================================================== */

    public String getValue() {
        return value;
    }

    /* PUBLIC METHODS ========================================================== */

    public static ContentType findHeaderdByValue(String value) {
        for (ContentType type : ContentType.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        return null;
    }
}
