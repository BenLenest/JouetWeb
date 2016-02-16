package model.enums;

/**
 * Class enumerating all the possible fields of a response header.
 * To use a custom header, use the constructor.
 * * Common non-standard fields are not taken in consideration.
 */
public enum HeaderResponse {

    /* CONSTANTS ========================================================== */

    ACCESS_CONTROL_ALLOW_ORIGIN("Accept"),
    ACCEPT_PATCH("Accept-Patch"),
    ACCEPT_RANGES("Accept-Ranges"),
    AGE("Age"),
    ALLOW("Allow"),
    CACHE_CONTROL("Cache-Control"),
    CONNECTION("Connection"),
    CONTENT_DISPOSITION("Content-Disposition"),
    CONTENT_ENCODING("Content-Encoding"),
    CONTENT_LENGTH("Content-Length"),
    CONTENT_LOCATION("Content-Location"),
    CONTENT_RANGE("Content-Range"),
    CONTENT_TYPE("Content-Type"),
    DATE("Date"),
    ETAG("ETag"),
    EXPIRES("Expires"),
    LAST_MODIFIED("Last-Modified"),
    LINK("Link"),
    LOCATION("Location"),
    PRAGMA("Pragma"),
    PROXY_AUTHENTICATE("Proxy-Authenticate"),
    RETRY_AFTER("Retry-After"),
    SERVER("web.Server"),
    SET_COOKIE("Set-Cookie"),
    STATUS("Status"),
    STRICT_TRANSPORT_SECURITY("Strict-Transport-Security"),
    TRAILER("Trailer"),
    TRANSFER_ENCODING("Transfer-Encoding"),
    TSV("TSV"),
    UPGRADE("Upgrade"),
    VARY("Vary"),
    VIA("Via"),
    WARNING("Warning"),
    WWW_AUTHENTICATE("WWW-Authenticate");

    /* ATTRIBUTES ========================================================== */

    private final String value;

    /* CONSTRUCTOR ========================================================== */

    HeaderResponse(String value) {
        this.value = value;
    }

    /* GETTERS ============================================================= */

    public String getValue() {
        return value;
    }

    /* PUBLIC METHODS ============================================================= */

    public static HeaderResponse findHeaderdByValue(String value) {
        for (HeaderResponse header : HeaderResponse.values()) {
            if (header.value.equals(value)) {
                return header;
            }
        }
        return null;
    }
}