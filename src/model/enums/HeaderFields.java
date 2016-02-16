package model.enums;

/**
 * Class enumerating all the possible fields of a Header.
 * To use a custom header, use the constructor.
 */
public enum HeaderFields {

    /* CONSTANTS ========================================================== */

    ACCEPT("Accept", ""),
    ACCEPT_CHARSET("Accept-Charset", ""),
    ACCEPT_ENCODING("Accept-Encoding", ""),
    ACCEPT_DATETIME("Accept-Datetime", ""),
    AUTHORIZATION("Authorization", ""),
    CONNECTION("Connection", ""),
    EXPECT("Expect", ""),
    FORWARDED("Forwarded", ""),
    FROM("From", ""),
    IF_MODIFIED_SINCE("If-Modified-Since", ""),
    IF_RANGE("If-Range", ""),
    IF_UNMODIFIED_SINCE("If-Unmodified-Since", ""),
    VIA("Via", ""),
    WARNING("Warning", "");

    /* ATTRIBUTES ========================================================== */

    private final String name;
    private final String value;

    /* CONSTRUCTOR ========================================================== */

    HeaderFields(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /* GETTERS ============================================================= */

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
