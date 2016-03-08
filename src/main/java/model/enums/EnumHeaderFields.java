package model.enums;

public enum EnumHeaderFields {

    /* ENUM ================================================================ */

    /**
     * La liste des headers disponible est volontairement réduite à ceux dont
     * nous avons besoin dans le cadre de ce projet par souci de simplicité.
     */
    CONTENT_TYPE("Content-Type"),
    CONTENT_LENGTH("Content-Length"),
    HOST("Host"),
    COOKIE("Cookie");

    /* ATTRIBUTES ========================================================== */


    public final String value;

    /* CONSTRUCTOR ========================================================= */

    EnumHeaderFields(String value) {
        this.value = value;
    }

    /* PUBLIC STATIC METHODS =============================================== */

    public static EnumHeaderFields findHeaderByValue(String value) {
        for (EnumHeaderFields headerField : EnumHeaderFields.values()) {
            if (headerField.value.equals(value)) {
                return headerField;
            }
        }
        return null;
    }
}
